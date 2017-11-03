package cn.ffcs.uom.systemconfig.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.manager.SystemOrgTreeConfigManager;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.businesssystem.model.SystemOrgTreeConfig;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.action.bean.SystemOrgTreeConfigEditBean;

@Controller
@Scope("prototype")
public class SystemOrgTreeConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private SystemOrgTreeConfigEditBean bean = new SystemOrgTreeConfigEditBean();
	/**
	 * manager
	 */
	private SystemOrgTreeConfigManager systemOrgTreeConfigManager = (SystemOrgTreeConfigManager) ApplicationContextUtil
			.getBean("systemOrgTreeConfigManagerImpl");

	/**
	 * 业务系统
	 */
	private BusinessSystem businessSystem;
	/**
	 * 业务系统组织树配置
	 */
	private SystemOrgTreeConfig systemOrgTreeConfig;

	private String opType;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate$systemOrgTreeConfigEditWin() throws Exception {
		// 绑定列表
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {

		// 绑定业务系统列表
		List<NodeVo> systemNameListbox = systemOrgTreeConfigManager
				.getBusinessSystemListbox();
		ListboxUtils.rendererForEdit(bean.getSystemNameListbox(),
				systemNameListbox);

		// 绑定组织树列表
		List<NodeVo> orgTreeNameList = systemOrgTreeConfigManager
				.getOrgTreeListbox();
		ListboxUtils.rendererForEdit(bean.getOrgTreeNameListbox(),
				orgTreeNameList);

		// 绑定生成开关列表
		List<NodeVo> generationSwitchList = UomClassProvider.getValuesList(
				"SystemOrgTreeConfig", "generationSwitch");
		ListboxUtils.rendererForEdit(bean.getGenerationSwitchListbox(),
				generationSwitchList);

	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if (BaseUnitConstants.ENTT_STATE_ADD.equals(opType)) {
			businessSystem = (BusinessSystem) arg.get("businessSystem");
			if (businessSystem != null) {
				this.bean.getSystemOrgTreeConfigEditWin().setTitle(
						"新增业务系统组织树配置");
				this.bean.getSystemNameListbox().setDisabled(true);
				ListboxUtils.selectByCodeValue(
						this.bean.getSystemNameListbox(), businessSystem
								.getBusinessSystemId().toString());
			} else {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
		} else if (BaseUnitConstants.ENTT_STATE_MOD.equals(opType)) {
			systemOrgTreeConfig = (SystemOrgTreeConfig) arg
					.get("systemOrgTreeConfig");
			if (systemOrgTreeConfig != null) {
				this.bean.getSystemOrgTreeConfigEditWin().setTitle(
						"修改业务系统组织树配置");
				this.bean.getSystemNameListbox().setDisabled(true);
				this.bean.getOrgTreeNameListbox().setDisabled(true);
				ListboxUtils.selectByCodeValue(
						this.bean.getSystemNameListbox(), systemOrgTreeConfig
								.getBusinessSystemId().toString());
				ListboxUtils.selectByCodeValue(this.bean
						.getOrgTreeNameListbox(), systemOrgTreeConfig
						.getOrgTreeId().toString());
				ListboxUtils.selectByCodeValue(
						this.bean.getGenerationSwitchListbox(),
						systemOrgTreeConfig.getGenerationSwitch());
			} else {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}

		}

	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {

		if (this.bean.getOrgTreeNameListbox().getSelectedItem().getValue() == null) {
			ZkUtil.showError("组织树不能为空，请选择！", "提示信息");
			return;
		}

		if (this.bean.getGenerationSwitchListbox().getSelectedItem().getValue() == null) {
			ZkUtil.showError("生成开关不能为空，请选择！", "提示信息");
			return;
		}

		if (BaseUnitConstants.ENTT_STATE_ADD.equals(opType)) {
			if (businessSystem != null
					&& businessSystem.getBusinessSystemId() != null) {
				systemOrgTreeConfig = new SystemOrgTreeConfig();
				systemOrgTreeConfig.setBusinessSystemId(businessSystem
						.getBusinessSystemId());
				systemOrgTreeConfig.setOrgTreeId(new Long(this.bean
						.getOrgTreeNameListbox().getSelectedItem().getValue()
						.toString()));
				List<SystemOrgTreeConfig> list = systemOrgTreeConfigManager
						.querySystemOrgTreeConfigList(systemOrgTreeConfig);
				if (list == null || list.size() < 0) {
					systemOrgTreeConfig = null;
					ZkUtil.showError("该业务系统组织树已经配置，请重新选择！", "提示信息");
					return;
				}
				systemOrgTreeConfig.setGenerationSwitch(this.bean
						.getGenerationSwitchListbox().getSelectedItem()
						.getValue().toString());
				systemOrgTreeConfigManager
						.addSystemOrgTreeConfig(systemOrgTreeConfig);
			}
		} else if (BaseUnitConstants.ENTT_STATE_MOD.equals(opType)) {
			systemOrgTreeConfig.setGenerationSwitch(this.bean
					.getGenerationSwitchListbox().getSelectedItem().getValue()
					.toString());
			systemOrgTreeConfigManager
					.updateSystemOrgTreeConfig(systemOrgTreeConfig);
		}

		this.bean.getSystemOrgTreeConfigEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getSystemOrgTreeConfigEditWin().onClose();
	}
}
