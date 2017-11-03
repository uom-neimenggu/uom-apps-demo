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
import cn.ffcs.uom.systemconfig.action.bean.MessageConfigEditBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Controller
@Scope("prototype")
public class MessageConfigEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private MessageConfigEditBean bean = new MessageConfigEditBean();
	/**
	 * manager
	 */
	private MessageConfigManager systemMessageConfigManager = (MessageConfigManager) ApplicationContextUtil
			.getBean("messageConfigManager");

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统配置信息
	 */
	private SystemConfigUser currentSystemMessageConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$messageConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

			currentSystemMessageConfig = (SystemConfigUser) arg
					.get("systemMessageConfig");

			if (currentSystemMessageConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getMessageConfigEditWin().setTitle("短信通知人员信息修改");

			this.bean.getUserName().setValue(
					currentSystemMessageConfig.getUserName());

			this.bean.getTelephoneNumber().setValue(
					currentSystemMessageConfig.getTelephoneNumber());
			
			this.bean.getUomAcct().setValue(
					currentSystemMessageConfig.getUomAcct());
			
			this.bean.getEmailAddress().setValue(
					currentSystemMessageConfig.getEmailAddress());
			
			ListboxUtils.selectByCodeValue(this.bean.getOverallSituation(),
					currentSystemMessageConfig.getOverallSituation());

		} else if ("add".equals(opType)) {
			currentSystemMessageConfig = null;
			this.bean.getMessageConfigEditWin().setTitle("短信通知人员信息新增");
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

		SystemConfigUser systemMessageConfig = null;
		List<SystemMessageConfig> list = null;

		if ("add".equals(opType)) {
			systemMessageConfig = new SystemConfigUser();
		} else if ("mod".endsWith(opType)) {
			systemMessageConfig = currentSystemMessageConfig;
		}

		systemMessageConfig.setUserName(this.bean.getUserName().getValue());
		
		systemMessageConfig.setTelephoneNumber(this.bean.getTelephoneNumber().getValue());
		
		systemMessageConfig.setUomAcct(this.bean.getUomAcct().getValue());

		systemMessageConfig.setEmailAddress(this.bean.getEmailAddress()
				.getValue());

		systemMessageConfig.setOverallSituation(this.bean
				.getOverallSituation().getSelectedItem().getValue()
				.toString());

		if ("add".equals(opType)) {
			this.systemMessageConfigManager
					.addMessageConfig(systemMessageConfig);
		} else {
			this.systemMessageConfigManager
					.updateMessageConfig(systemMessageConfig);
		}
		this.bean.getMessageConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, systemMessageConfig);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getMessageConfigEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {
		if (StrUtil.isEmpty(this.bean.getUserName().getValue())) {
			return "联系人姓名必填";
		}

		if (StrUtil.isEmpty(this.bean.getTelephoneNumber().getValue())) {
			return "手机号码必填";
		}
		
		if (StrUtil.isEmpty(this.bean.getOverallSituation()
				.getSelectedItem().getValue().toString())) {
			return "全局管理标识必填";
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

		SystemConfigUser systemConfigUser = new SystemConfigUser();
		systemConfigUser.setTelephoneNumber(this.bean.getTelephoneNumber()
				.getValue());

		if ("mod".endsWith(opType)) {
			systemConfigUser
					.setSystemConfigUserId(currentSystemMessageConfig.getSystemConfigUserId());
		}

		systemConfigUser = systemMessageConfigManager
				.queryMessageConfig(systemConfigUser);

		if (!StrUtil.isNullOrEmpty(systemConfigUser)) {
			return "该手机号已经被使用，请重新输入手机号。";
		}

		return null;
	}
}
