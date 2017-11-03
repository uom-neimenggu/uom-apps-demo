package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.EmailUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.SystemMessageConfigEditBean;
import cn.ffcs.uom.systemconfig.manager.SysBusiUserManager;
import cn.ffcs.uom.webservices.manager.SystemMessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemBusiUser;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Controller
@Scope("prototype")
public class SystemMessageConfigEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private SystemMessageConfigEditBean bean = new SystemMessageConfigEditBean();
	/**
	 * manager
	 */
	private SystemMessageConfigManager systemMessageConfigManager = (SystemMessageConfigManager) ApplicationContextUtil
			.getBean("systemMessageConfigManager");

	/**
	 * manager
	 */
	private SysBusiUserManager sysBusiUserManager = (SysBusiUserManager) ApplicationContextUtil
			.getBean("sysBusiUserManager");

	/**
	 * 业务系统
	 */
	private BusinessSystem businessSystem;

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统配置信息
	 */
	private SystemMessageConfig currentSystemMessageConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$sysMsgConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

			currentSystemMessageConfig = (SystemMessageConfig) arg
					.get("systemMessageConfig");

			if (currentSystemMessageConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getSysMsgConfigEditWin().setTitle("短信通知信息配置修改");

			this.bean.getSystemCode().setValue(
					currentSystemMessageConfig.getSystemCode());

			ListboxUtils.selectByCodeValue(this.bean.getSystemMessageSwitch(),
					currentSystemMessageConfig.getSystemMessageSwitch());

			this.bean.getContactName().setValue(
					currentSystemMessageConfig.getContactName());

			this.bean.getTelephoneNumber().setValue(
					currentSystemMessageConfig.getTelephoneNumber());

			if (!StrUtil.isNullOrEmpty(currentSystemMessageConfig
					.getNoticeOrder())) {
				this.bean.getNoticeOrder().setValue(
						currentSystemMessageConfig.getNoticeOrder());
			}

			this.bean.getEmailAddress().setValue(
					currentSystemMessageConfig.getEmailAddress());

		} else if ("add".equals(opType)) {
			currentSystemMessageConfig = null;
			this.bean.getSysMsgConfigEditWin().setTitle("通知人员新增");
			businessSystem = (BusinessSystem) arg.get("businessSystem");
		}
	}

	/**
	 * 点击确定
	 */
	public void onSubmit() throws Exception {
		// 信息验证
		String msg = this.infoValid();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		SystemMessageConfig systemMessageConfig = null;
		List<SystemMessageConfig> list = null;

		if ("add".equals(opType)) {
			systemMessageConfig = new SystemMessageConfig();
		} else if ("mod".endsWith(opType)) {
			systemMessageConfig = currentSystemMessageConfig;
		}

		systemMessageConfig.setSystemCode(this.bean.getSystemCode().getValue());

		systemMessageConfig.setSystemMessageSwitch(this.bean
				.getSystemMessageSwitch().getSelectedItem().getValue()
				.toString());

		systemMessageConfig.setContactName(this.bean.getContactName()
				.getValue());

		systemMessageConfig.setTelephoneNumber(this.bean.getTelephoneNumber()
				.getValue());

		systemMessageConfig.setNoticeOrder(this.bean.getNoticeOrder()
				.getValue());

		systemMessageConfig.setEmailAddress(this.bean.getEmailAddress()
				.getValue());

		if ("add".equals(opType)) {

			this.systemMessageConfigManager
					.addSystemMessageConfig(systemMessageConfig);
		} else {
			this.systemMessageConfigManager
					.updateSystemMessageConfig(systemMessageConfig);
		}
		this.bean.getSysMsgConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, systemMessageConfig);
	}

	/**
	 * 点击确定
	 */
	public void onSubmitNew() throws Exception {
		// 信息验证
		String msg = this.infoValidNew();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		SystemConfigUser systemConfigUser = bean.getMessageConfigBandboxExt()
				.getSystemConfigUser();

		SystemBusiUser sysBusiUser = new SystemBusiUser();
		sysBusiUser.setBusinessSystemId(businessSystem.getId());
		sysBusiUser.setSystemConfigUserId(systemConfigUser.getId());
		sysBusiUser.setSort(1L);
		SystemBusiUser querySysBusiUser = sysBusiUserManager
				.querySysBusiUserByUserSys(businessSystem.getId(), systemConfigUser.getId());
		
		if(querySysBusiUser != null) {
			ZkUtil.showError("该系统已配置该人员请重新选择", "提示信息");
			return;
		}

		if ("add".equals(opType)) {
			this.sysBusiUserManager.addSysBusiUser(sysBusiUser);
		}

		this.bean.getSysMsgConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, systemConfigUser);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getSysMsgConfigEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValidNew() throws Exception {
		if (StrUtil.isEmpty(this.bean.getMessageConfigBandboxExt().getValue())) {
			return "配置人员必填";
		}

		return null;
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {

		if (StrUtil.isEmpty(this.bean.getSystemCode().getValue())) {
			return "系统编码必填";
		}

		if (StrUtil.isEmpty(this.bean.getSystemMessageSwitch()
				.getSelectedItem().getValue().toString())) {
			return "短信通知开关必填";
		}

		if (StrUtil.isEmpty(this.bean.getContactName().getValue())) {
			return "联系人姓名必填";
		}

		if (StrUtil.isEmpty(this.bean.getTelephoneNumber().getValue())) {
			return "手机号码必填";
		}

		if (this.bean.getNoticeOrder() == null
				|| this.bean.getNoticeOrder().getValue() == null) {
			return "通知顺序不能为空";
		}

		if (!StrUtil.isEmpty(this.bean.getEmailAddress().getValue())) {
			if (!EmailUtil.isEmailAddress(this.bean.getEmailAddress()
					.getValue())) {
				return "邮件地址格式不对，请重新输入。";
			}
		}

		if (!StrUtil.checkTelephoneNumber(this.bean.getTelephoneNumber()
				.getValue())) {
			return "手机号码格式不对或者不存在此号码段，请重新输入11位数的手机号。";
		}

		SystemMessageConfig systemMessageConfig = new SystemMessageConfig();
		systemMessageConfig.setSystemCode(this.bean.getSystemCode().getValue());
		systemMessageConfig.setTelephoneNumber(this.bean.getTelephoneNumber()
				.getValue());

		if ("mod".endsWith(opType)) {
			systemMessageConfig
					.setSystemMessageConfigId(currentSystemMessageConfig
							.getSystemMessageConfigId());
		}

		systemMessageConfig = systemMessageConfigManager
				.querySystemMessageConfig(systemMessageConfig);

		if (!StrUtil.isNullOrEmpty(systemMessageConfig)) {
			return "该手机号已经被使用，请重新输入手机号。";
		}

		return null;
	}
}
