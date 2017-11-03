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
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.component.bean.RbacPermissionEditExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionExtAttrManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Controller
@Scope("prototype")
public class RbacPermissionEditExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacPermissionEditExtBean bean = new RbacPermissionEditExtBean();

	@Autowired
	@Qualifier("rbacPermissionManager")
	private RbacPermissionManager rbacPermissionManager = (RbacPermissionManager) ApplicationContextUtil
			.getBean("rbacPermissionManager");

	@Autowired
	@Qualifier("rbacPermissionExtAttrManager")
	private RbacPermissionExtAttrManager rbacPermissionExtAttrManager = (RbacPermissionExtAttrManager) ApplicationContextUtil
			.getBean("rbacPermissionExtAttrManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_edit_ext.zul";

	/**
	 * 选中的 selectRbacPermission.
	 */
	private RbacPermission selectRbacPermission;

	/**
	 * 选中的权限关系
	 */
	private RbacPermissionRelation rbacPermissionRelation;

	/**
	 * 编辑中的editRbacPermission.
	 */
	private RbacPermission editRbacPermission;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacPermissionEditExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		bindCombobox();
		RbacPermissionEditExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_PERMISSION_INFO,
				RbacPermissionEditExt.this, "onSaveRbacPermission");
		this.addForward(RolePermissionConstants.ON_SELECT_TREE_RBAC_PERMISSION,
				this, "onSelectRbacPermissionResponse");
		this.setRbacPermissionButtonValid(true, false, false);
	}

	/**
	 * 权限选择.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRbacPermissionResponse(final ForwardEvent event)
			throws Exception {

		this.rbacPermissionRelation = (RbacPermissionRelation) event
				.getOrigin().getData();

		if (this.rbacPermissionRelation == null
				|| this.rbacPermissionRelation.getRbacPermissionId() == null) {
			this.selectRbacPermission = new RbacPermission();
		} else {
			this.selectRbacPermission = rbacPermissionRelation
					.getRbacPermission();
		}

		this.bean.getRbacPermissionId().setValue(
				selectRbacPermission.getRbacPermissionId());
		this.bean.getRbacPermissionCode().setValue(
				selectRbacPermission.getRbacPermissionCode());
		this.bean.getRbacPermissionName().setValue(
				selectRbacPermission.getRbacPermissionName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacPermissionType(),
				selectRbacPermission.getRbacPermissionType());
		this.bean.getRbacPermissionDesc().setValue(
				selectRbacPermission.getRbacPermissionDesc());
		/**
		 * 扩展属性
		 */
		List<RbacPermissionExtAttr> extendAttrList = selectRbacPermission
				.getRbacPermissionExtAttrList();
		this.bean.getRbacPermissionExtAttrExt().setExtendValueList(
				extendAttrList);
		// this.bean.getRbacPermissionExtAttrExt().setAllDisable(true);
		this.setRbacPermissionButtonValid(true, false, false);
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> rbacPermissionType = UomClassProvider.getValuesList(
				"RbacPermission", "rbacPermissionType");
		ListboxUtils.rendererForEdit(bean.getRbacPermissionType(),
				rbacPermissionType);

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		this.setRbacPermissionButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 * 
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onSave() throws PortalException, SystemException {

		if (bean.getRbacPermissionCode() == null
				|| StrUtil.isEmpty(bean.getRbacPermissionCode().getValue())) {
			ZkUtil.showError("权限编码不能为空", "提示信息");
			return;
		}

		if (bean.getRbacPermissionName() == null
				|| StrUtil.isEmpty(bean.getRbacPermissionName().getValue())) {
			ZkUtil.showError("权限名称不能为空", "提示信息");
			return;
		}

		this.editRbacPermission = this.selectRbacPermission;

		this.editRbacPermission.setRbacPermissionCode(bean
				.getRbacPermissionCode().getValue().trim());
		this.editRbacPermission.setRbacPermissionName(bean
				.getRbacPermissionName().getValue().trim());
		if (bean.getRbacPermissionType().getSelectedItem().getValue() != null) {
			this.editRbacPermission.setRbacPermissionType(bean
					.getRbacPermissionType().getSelectedItem().getValue()
					.toString());
		}
		this.editRbacPermission.setRbacPermissionDesc(bean
				.getRbacPermissionDesc().getValue());

		/**
		 * 扩展属性
		 */
		List<RbacPermissionExtAttr> extendAttrList = editRbacPermission
				.getRbacPermissionExtAttrList();

		List<RbacPermissionExtAttr> beanList = this.bean
				.getRbacPermissionExtAttrExt().getExtendValueList();

		if (extendAttrList == null || extendAttrList.size() <= 0) {
			editRbacPermission.setRbacPermissionExtAttrList(beanList);
		} else {
			for (RbacPermissionExtAttr rbacPermissionExtAttr : beanList) {
				for (RbacPermissionExtAttr dbRbacPermissionExtAttr : extendAttrList) {
					if (rbacPermissionExtAttr.getRbacPermissionAttrSpecId()
							.equals(dbRbacPermissionExtAttr
									.getRbacPermissionAttrSpecId())) {
						String rbacPermissionAttrVlue = rbacPermissionExtAttr
								.getRbacPermissionAttrValue();
						BeanUtils.copyProperties(rbacPermissionExtAttr,
								dbRbacPermissionExtAttr);
						rbacPermissionExtAttr
								.setRbacPermissionAttrValue(rbacPermissionAttrVlue);
					}
				}
			}
		}

		editRbacPermission.setRbacPermissionExtAttrList(beanList);

		rbacPermissionManager.updateRbacPermission(editRbacPermission);

		this.setRbacPermissionButtonValid(true, false, false);
		/**
		 * 抛出保存事件，用来在权限树中保存更改后的权限名称
		 */
		Events.postEvent(RolePermissionConstants.ON_SAVE_RBAC_PERMISSION_INFO,
				this, editRbacPermission);
		this.updateRbacPermissionToRaptornuke();
	}

	public void updateRbacPermissionToRaptornuke() throws PortalException,
			SystemException {

		RbacPermission rbacParentPermission = rbacPermissionRelation
				.getRbacParentPermission();

		if (rbacParentPermission != null) {

			RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();
			rbacPermissionExtAttr.setRbacPermissionId(rbacParentPermission
					.getRbacPermissionId());
			rbacPermissionExtAttr
					.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1);
			rbacPermissionExtAttr = rbacPermissionExtAttrManager
					.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

			if (rbacPermissionExtAttr != null
					&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

				rbacPermissionExtAttr = new RbacPermissionExtAttr();
				rbacPermissionExtAttr.setRbacPermissionId(rbacParentPermission
						.getRbacPermissionId());
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2);
				rbacPermissionExtAttr = rbacPermissionExtAttrManager
						.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

				if (rbacPermissionExtAttr != null
						&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2_ATTR_VALUE_1
								.equals(rbacPermissionExtAttr
										.getRbacPermissionAttrValue())) {

					rbacPermissionExtAttr = new RbacPermissionExtAttr();
					rbacPermissionExtAttr
							.setRbacPermissionId(rbacParentPermission
									.getRbacPermissionId());
					rbacPermissionExtAttr
							.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_4);

					rbacPermissionExtAttr = rbacPermissionExtAttrManager
							.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

					if (rbacPermissionExtAttr != null
							&& !StrUtil.isEmpty(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

						rbacPermissionManager.updateRbacPermissionToRaptornuke(
								editRbacPermission, rbacPermissionExtAttr);

						rbacPermissionExtAttr = new RbacPermissionExtAttr();
						rbacPermissionExtAttr
								.setRbacPermissionId(editRbacPermission
										.getRbacPermissionId());
						this.bean
								.getRbacPermissionExtAttrExt()
								.setExtendValueList(
										rbacPermissionExtAttrManager
												.queryRbacPermissionExtAttrList(rbacPermissionExtAttr));
					}
				}
			}
		}
	}

	/**
	 * 点击恢复
	 */
	public void onRecover() {
		this.setRbacPermissionButtonValid(true, false, false);
		this.bean.getRbacPermissionId().setValue(
				selectRbacPermission.getRbacPermissionId());
		this.bean.getRbacPermissionCode().setValue(
				selectRbacPermission.getRbacPermissionCode());
		this.bean.getRbacPermissionName().setValue(
				selectRbacPermission.getRbacPermissionName());
		ListboxUtils.selectByCodeValue(this.bean.getRbacPermissionType(),
				selectRbacPermission.getRbacPermissionType());
		this.bean.getRbacPermissionDesc().setValue(
				selectRbacPermission.getRbacPermissionDesc());
		/**
		 * 扩展属性
		 */
		List<RbacPermissionExtAttr> extendAttrList = selectRbacPermission
				.getRbacPermissionExtAttrList();
		this.bean.getRbacPermissionExtAttrExt().setExtendValueList(
				extendAttrList);
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setRbacPermissionButtonValid(final Boolean canEdit,
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
	 * @throws SystemException
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
		} else if ("rbacPermissionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

}
