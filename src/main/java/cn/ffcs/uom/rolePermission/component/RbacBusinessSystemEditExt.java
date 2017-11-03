package cn.ffcs.uom.rolePermission.component;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.component.bean.RbacBusinessSystemEditExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

@Controller
@Scope("prototype")
public class RbacBusinessSystemEditExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacBusinessSystemEditExtBean bean = new RbacBusinessSystemEditExtBean();

	@Autowired
	@Qualifier("rbacBusinessSystemManager")
	private RbacBusinessSystemManager rbacBusinessSystemManager = (RbacBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_business_system_edit_ext.zul";

	/**
	 * 选中的 selectRbacBusinessSystem.
	 */
	private RbacBusinessSystem selectRbacBusinessSystem;

	/**
	 * 编辑中的editRbacBusinessSystem.
	 */
	private RbacBusinessSystem editRbacBusinessSystem;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacBusinessSystemEditExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		bindCombobox();
		RbacBusinessSystemEditExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_BUSINESS_SYSTEM_INFO,
				RbacBusinessSystemEditExt.this, "onSaveRbacBusinessSystem");
		this.addForward(
				RolePermissionConstants.ON_SELECT_TREE_RBAC_BUSINESS_SYSTEM,
				this, "onSelectRbacBusinessSystemResponse");
		this.setRbacBusinessSystemButtonValid(true, false, false);
	}

	/**
	 * 系统选择.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRbacBusinessSystemResponse(final ForwardEvent event)
			throws Exception {

		this.selectRbacBusinessSystem = (RbacBusinessSystem) event.getOrigin()
				.getData();

		if (this.selectRbacBusinessSystem == null
				|| this.selectRbacBusinessSystem.getRbacBusinessSystemId() == null) {
			this.selectRbacBusinessSystem = new RbacBusinessSystem();
		}

		this.bean.getRbacBusinessSystemCode().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemCode());
		this.bean.getRbacBusinessSystemName().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemName());
		this.bean.getRbacBusinessSystemUrl().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemUrl());
		ListboxUtils.selectByCodeValue(this.bean.getRbacBusinessSystemDomain(),
				selectRbacBusinessSystem.getRbacBusinessSystemDomain());
		this.bean.getRbacBusinessSystemDesc().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemDesc());

		this.setRbacBusinessSystemButtonValid(true, false, false);
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> rbacBusinessSystemDomain = UomClassProvider.getValuesList(
				"RbacBusinessSystem", "rbacBusinessSystemDomain");
		ListboxUtils.rendererForEdit(bean.getRbacBusinessSystemDomain(),
				rbacBusinessSystemDomain);

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		this.setRbacBusinessSystemButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 */
	public void onSave() {

		if (bean.getRbacBusinessSystemCode() == null
				|| StrUtil.isEmpty(bean.getRbacBusinessSystemCode().getValue())) {
			ZkUtil.showError("系统编码不能为空", "提示信息");
			return;
		}

		if (bean.getRbacBusinessSystemName() == null
				|| StrUtil.isEmpty(bean.getRbacBusinessSystemName().getValue())) {
			ZkUtil.showError("系统名称不能为空", "提示信息");
			return;
		}

		this.editRbacBusinessSystem = this.selectRbacBusinessSystem;

		this.editRbacBusinessSystem.setRbacBusinessSystemCode(bean
				.getRbacBusinessSystemCode().getValue().trim());
		this.editRbacBusinessSystem.setRbacBusinessSystemName(bean
				.getRbacBusinessSystemName().getValue().trim());
		this.editRbacBusinessSystem.setRbacBusinessSystemUrl(bean
				.getRbacBusinessSystemUrl().getValue());
		if (bean.getRbacBusinessSystemDomain().getSelectedItem().getValue() != null) {
			this.editRbacBusinessSystem.setRbacBusinessSystemDomain(bean
					.getRbacBusinessSystemDomain().getSelectedItem().getValue()
					.toString());
		}
		this.editRbacBusinessSystem.setRbacBusinessSystemDesc(bean
				.getRbacBusinessSystemDesc().getValue());

		rbacBusinessSystemManager
				.updateRbacBusinessSystem(editRbacBusinessSystem);
		this.setRbacBusinessSystemButtonValid(true, false, false);

		/**
		 * 抛出保存事件，用来在系统树中保存更改后的系统名称
		 */
		Events.postEvent(
				RolePermissionConstants.ON_SAVE_RBAC_BUSINESS_SYSTEM_INFO,
				this, editRbacBusinessSystem);
	}

	/**
	 * 点击恢复
	 */
	public void onRecover() {
		this.setRbacBusinessSystemButtonValid(true, false, false);
		this.bean.getRbacBusinessSystemCode().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemCode());
		this.bean.getRbacBusinessSystemName().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemName());
		this.bean.getRbacBusinessSystemUrl().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemUrl());
		ListboxUtils.selectByCodeValue(this.bean.getRbacBusinessSystemDomain(),
				selectRbacBusinessSystem.getRbacBusinessSystemDomain());
		this.bean.getRbacBusinessSystemDesc().setValue(
				selectRbacBusinessSystem.getRbacBusinessSystemDesc());

	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canView
	 *            查看按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setRbacBusinessSystemButtonValid(final Boolean canEdit,
			final Boolean canSave, final Boolean canRecover) {
		if (canEdit != null) {
			this.bean.getEditButton().setDisabled(!canEdit);
		}
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if ("rbacBusinessSystemTreePage".equals(page)) {
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

}
