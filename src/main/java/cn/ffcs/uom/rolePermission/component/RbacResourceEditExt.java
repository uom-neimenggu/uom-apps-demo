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
import cn.ffcs.uom.rolePermission.component.bean.RbacResourceEditExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;

@Controller
@Scope("prototype")
public class RbacResourceEditExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacResourceEditExtBean bean = new RbacResourceEditExtBean();

	@Autowired
	@Qualifier("rbacResourceManager")
	private RbacResourceManager rbacResourceManager = (RbacResourceManager) ApplicationContextUtil
			.getBean("rbacResourceManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_resource_edit_ext.zul";

	/**
	 * 选中的 selectRbacResource.
	 */
	private RbacResource selectRbacResource;

	/**
	 * 编辑中的editRbacResource.
	 */
	private RbacResource editRbacResource;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacResourceEditExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		bindCombobox();
		RbacResourceEditExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_RESOURCE_INFO,
				RbacResourceEditExt.this, "onSaveRbacResource");
		this.addForward(RolePermissionConstants.ON_SELECT_TREE_RBAC_RESOURCE,
				this, "onSelectRbacResourceResponse");
		this.setRbacResourceButtonValid(true, false, false);
	}

	/**
	 * 资源选择.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRbacResourceResponse(final ForwardEvent event)
			throws Exception {

		this.selectRbacResource = (RbacResource) event.getOrigin().getData();

		if (this.selectRbacResource == null
				|| this.selectRbacResource.getRbacResourceId() == null) {
			this.selectRbacResource = new RbacResource();
		}

		this.bean.getRbacResourceCode().setValue(
				selectRbacResource.getRbacResourceCode());
		this.bean.getRbacResourceName().setValue(
				selectRbacResource.getRbacResourceName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacResourceLeaf(),
				selectRbacResource.getRbacResourceLeaf());
		this.bean.getRbacResourceUrl().setValue(
				selectRbacResource.getRbacResourceUrl());
		this.bean.getRbacResourceDesc().setValue(
				selectRbacResource.getRbacResourceDesc());

		this.setRbacResourceButtonValid(true, false, false);
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> rbacResourceLeaf = UomClassProvider.getValuesList(
				"RbacResource", "rbacResourceLeaf");
		ListboxUtils.rendererForEdit(bean.getRbacResourceLeaf(),
				rbacResourceLeaf);

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		this.setRbacResourceButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 */
	public void onSave() {

		if (bean.getRbacResourceCode() == null
				|| StrUtil.isEmpty(bean.getRbacResourceCode().getValue())) {
			ZkUtil.showError("资源编码不能为空", "提示信息");
			return;
		}

		if (bean.getRbacResourceName() == null
				|| StrUtil.isEmpty(bean.getRbacResourceName().getValue())) {
			ZkUtil.showError("资源名称不能为空", "提示信息");
			return;
		}

		if (StrUtil.isNullOrEmpty(bean.getRbacResourceLeaf().getSelectedItem()
				.getValue())
				|| StrUtil.isEmpty(bean.getRbacResourceLeaf().getSelectedItem()
						.getValue().toString())) {
			ZkUtil.showError("叶子节点不能为空", "提示信息");
			return;
		}

		this.editRbacResource = this.selectRbacResource;

		this.editRbacResource.setRbacResourceCode(bean.getRbacResourceCode()
				.getValue().trim());
		this.editRbacResource.setRbacResourceName(bean.getRbacResourceName()
				.getValue().trim());
		this.editRbacResource.setRbacResourceLeaf(bean.getRbacResourceLeaf()
				.getSelectedItem().getValue().toString());
		this.editRbacResource.setRbacResourceDesc(bean.getRbacResourceDesc()
				.getValue());

		rbacResourceManager.updateRbacResource(editRbacResource);
		this.setRbacResourceButtonValid(true, false, false);

		/**
		 * 抛出保存事件，用来在资源树中保存更改后的资源名称
		 */
		Events.postEvent(RolePermissionConstants.ON_SAVE_RBAC_RESOURCE_INFO,
				this, editRbacResource);
	}

	/**
	 * 点击恢复
	 */
	public void onRecover() {
		this.setRbacResourceButtonValid(true, false, false);
		this.bean.getRbacResourceCode().setValue(
				selectRbacResource.getRbacResourceCode());
		this.bean.getRbacResourceName().setValue(
				selectRbacResource.getRbacResourceName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacResourceLeaf(),
				selectRbacResource.getRbacResourceLeaf());
		this.bean.getRbacResourceUrl().setValue(
				selectRbacResource.getRbacResourceUrl());
		this.bean.getRbacResourceDesc().setValue(
				selectRbacResource.getRbacResourceDesc());

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
	private void setRbacResourceButtonValid(final Boolean canEdit,
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
		} else if ("rbacResourceTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

}
