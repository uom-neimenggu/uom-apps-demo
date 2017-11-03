package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.action.bean.IdentityCardConfigEditBean;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

@Controller
@Scope("prototype")
public class IdentityCardConfigEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private IdentityCardConfigEditBean bean = new IdentityCardConfigEditBean();
	/**
	 * manager
	 */
	private IdentityCardConfigManager identityCardConfigManager = (IdentityCardConfigManager) ApplicationContextUtil
			.getBean("identityCardConfigManager");

	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 业务系统配置信息
	 */
	private IdentityCardConfig currentIdentityCardConfig;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$identityCardConfigEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {

			currentIdentityCardConfig = (IdentityCardConfig) arg
					.get("identityCardConfig");

			if (currentIdentityCardConfig == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

			this.bean.getIdentityCardConfigEditWin().setTitle("身份证类型信息配置修改");

			this.bean.getIdentityCardId().setValue(
					currentIdentityCardConfig.getIdentityCardId().toString());

			ListboxUtils.selectByCodeValue(this.bean.getIdentityCardSwitch(),
					currentIdentityCardConfig.getIdentityCardSwitch());

			this.bean.getIdentityCardName().setValue(
					currentIdentityCardConfig.getIdentityCardName());

			this.bean.getIdentityCardPrefix().setValue(
					currentIdentityCardConfig.getIdentityCardPrefix());

		} else if ("add".equals(opType)) {
			currentIdentityCardConfig = null;
			this.bean.getIdentityCardConfigEditWin().setTitle("身份证类型信息配置新增");
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

		IdentityCardConfig identityCardConfig = null;

		if ("add".equals(opType)) {
			identityCardConfig = new IdentityCardConfig();
		} else if ("mod".endsWith(opType)) {
			identityCardConfig = currentIdentityCardConfig;
		}

		identityCardConfig.setIdentityCardSwitch(this.bean
				.getIdentityCardSwitch().getSelectedItem().getValue()
				.toString());

		identityCardConfig.setIdentityCardName(this.bean.getIdentityCardName()
				.getValue());

		identityCardConfig.setIdentityCardPrefix(this.bean
				.getIdentityCardPrefix().getValue());

		if ("add".equals(opType)) {
			this.identityCardConfigManager
					.saveIdentityCardConfig(identityCardConfig);
		} else {
			this.identityCardConfigManager
					.updateIdentityCardConfig(identityCardConfig);
		}
		this.bean.getIdentityCardConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, identityCardConfig);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getIdentityCardConfigEditWin().onClose();
	}

	/**
	 * 信息验证
	 */
	public String infoValid() throws Exception {

		if (StrUtil.isEmpty(this.bean.getIdentityCardSwitch().getSelectedItem()
				.getValue().toString())) {
			return "身份证类型开关必填";
		}

		if (StrUtil.isEmpty(this.bean.getIdentityCardName().getValue())) {
			return "身份证类型名称必填";
		}

		return null;
	}
}
