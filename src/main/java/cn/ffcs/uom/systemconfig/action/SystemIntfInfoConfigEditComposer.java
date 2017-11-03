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
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.SystemIntfInfoConfigEditBean;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

@Controller
@Scope("prototype")
public class SystemIntfInfoConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private SystemIntfInfoConfigEditBean bean = new SystemIntfInfoConfigEditBean();
	/**
	 * manager
	 */
	private SystemIntfInfoConfigManager systemIntfInfoConfigManager = (SystemIntfInfoConfigManager) ApplicationContextUtil
			.getBean("systemIntfInfoConfigManager");

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
	private SystemIntfInfoConfig currentSystemIntfInfoConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$systemIntfInfoConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

			currentSystemIntfInfoConfig = (SystemIntfInfoConfig) arg
					.get("systemIntfInfoConfig");

			if (currentSystemIntfInfoConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getSystemIntfInfoConfigEditWin().setTitle("业务系统信息配置修改");

			this.bean.getSystemCode().setValue(
					currentSystemIntfInfoConfig.getSystemCode());

			this.bean.getMsgType().setValue(
					currentSystemIntfInfoConfig.getMsgType());

			ListboxUtils.selectByCodeValue(this.bean.getIntfSwitchAll(),
					currentSystemIntfInfoConfig.getIntfSwitchAll());

			ListboxUtils.selectByCodeValue(this.bean.getIntfSwitchIncrease(),
					currentSystemIntfInfoConfig.getIntfSwitchIncrease());

			this.bean.getServiceCode().setValue(
					currentSystemIntfInfoConfig.getServiceCode());

			this.bean.getOipUrl().setValue(
					currentSystemIntfInfoConfig.getOipUrl());

			this.bean.getOperationName().setValue(
					currentSystemIntfInfoConfig.getOperationName());

			this.bean.getParamName().setValue(
					currentSystemIntfInfoConfig.getParamName());

			this.bean.getNameSpace().setValue(
					currentSystemIntfInfoConfig.getNameSpace());

		} else if ("add".equals(opType)) {
			currentSystemIntfInfoConfig = null;
			this.bean.getSystemIntfInfoConfigEditWin().setTitle("业务系统信息配置新增");
			businessSystem = (BusinessSystem) arg.get("businessSystem");
			if (businessSystem != null) {
				this.bean.getSystemCode().setValue(
						businessSystem.getSystemCode());
			} else {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
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

		SystemIntfInfoConfig systemIntfInfoConfig = null;
		List<SystemIntfInfoConfig> list = null;

		if ("add".equals(opType)) {
			systemIntfInfoConfig = new SystemIntfInfoConfig();
		} else if ("mod".endsWith(opType)) {
			systemIntfInfoConfig = currentSystemIntfInfoConfig;
		}

		systemIntfInfoConfig
				.setSystemCode(this.bean.getSystemCode().getValue());

		systemIntfInfoConfig.setMsgType(this.bean.getMsgType().getValue());

		systemIntfInfoConfig.setIntfSwitchAll(this.bean.getIntfSwitchAll()
				.getSelectedItem().getValue().toString());

		systemIntfInfoConfig.setIntfSwitchIncrease(this.bean
				.getIntfSwitchIncrease().getSelectedItem().getValue()
				.toString());

		systemIntfInfoConfig.setServiceCode(this.bean.getServiceCode()
				.getValue());

		systemIntfInfoConfig.setOipUrl(this.bean.getOipUrl().getValue());

		systemIntfInfoConfig.setOperationName(this.bean.getOperationName()
				.getValue());

		systemIntfInfoConfig.setParamName(this.bean.getParamName().getValue());

		systemIntfInfoConfig.setNameSpace(this.bean.getNameSpace().getValue());

		if ("add".equals(opType)) {

			this.systemIntfInfoConfigManager
					.addSystemIntfInfoConfig(systemIntfInfoConfig);
		} else {
			this.systemIntfInfoConfigManager
					.updateSystemIntfInfoConfig(systemIntfInfoConfig);
		}
		this.bean.getSystemIntfInfoConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, systemIntfInfoConfig);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getSystemIntfInfoConfigEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {

		if (StrUtil.isEmpty(this.bean.getSystemCode().getValue())) {
			return "系统编码必填";
		}

		if (StrUtil.isEmpty(this.bean.getMsgType().getValue())) {
			return "消息类型必填";
		}

		if (StrUtil.isEmpty(this.bean.getIntfSwitchAll().getSelectedItem()
				.getValue().toString())) {
			return "全量开关必填";
		}

		if (StrUtil.isEmpty(this.bean.getIntfSwitchIncrease().getSelectedItem()
				.getValue().toString())) {
			return "增量开关必填";
		}

		if (StrUtil.isEmpty(this.bean.getServiceCode().getValue())) {
			return "OIP编码必填";
		}
		if (StrUtil.isEmpty(this.bean.getOipUrl().getValue())) {
			return "OIP地址必填";
		}

		if (StrUtil.isEmpty(this.bean.getOperationName().getValue())) {
			return "方法名称必填";
		}

		if (StrUtil.isEmpty(this.bean.getParamName().getValue())) {
			return "参数名称必填";
		}

		if (StrUtil.isEmpty(this.bean.getSystemCode().getValue())) {
			return "业务系统编码必填";
		}

		return null;
	}
}
