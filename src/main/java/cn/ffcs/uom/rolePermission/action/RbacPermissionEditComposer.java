package cn.ffcs.uom.rolePermission.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.action.bean.RbacPermissionEditBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionExtAttrManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

@Controller
@Scope("prototype")
public class RbacPermissionEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * rbacPermissionEditBean.
	 */
	private RbacPermissionEditBean bean = new RbacPermissionEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("rbacPermissionManager")
	private RbacPermissionManager rbacPermissionManager;

	@Autowired
	@Qualifier("rbacPermissionExtAttrManager")
	private RbacPermissionExtAttrManager rbacPermissionExtAttrManager;

	/**
	 * 权限.
	 */
	private RbacPermission rbacPermission;

	/**
	 * 父权限.
	 */
	private RbacPermission rbacParentPermission;

	/**
	 * 修改权限.
	 */
	private RbacPermission oldRbacPermission;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacPermissionEditWindow() {

		this.bindCombobox();

		opType = StrUtil.strnull(arg.get("opType"));

		if ("addChildNode".equals(opType)) {
			this.bean.getRbacPermissionEditWindow().setTitle("新增子节点");
			rbacParentPermission = (RbacPermission) arg.get("rbacPermission");
		} else if ("addRootNode".equals(opType)) {
			this.bean.getRbacPermissionEditWindow().setTitle("新增根节点");
		} else {
			if ("view".equals(opType)) {
				this.bean.getRbacPermissionEditWindow().setTitle("权限查看");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			} else {
				this.bean.getRbacPermissionEditWindow().setTitle("权限修改");
			}
			oldRbacPermission = (RbacPermission) arg.get("rbacPermission");
			if (oldRbacPermission != null) {
				this.bean.getRbacPermissionCode().setValue(
						oldRbacPermission.getRbacPermissionCode());
				this.bean.getRbacPermissionName().setValue(
						oldRbacPermission.getRbacPermissionName());
				ListboxUtils.selectByCodeValue(
						this.bean.getRbacPermissionType(),
						oldRbacPermission.getRbacPermissionType());
				this.bean.getRbacPermissionDesc().setValue(
						oldRbacPermission.getRbacPermissionDesc());
			}
		}
	}

	/**
	 * 绑定combobox.
	 */
	private void bindCombobox() {
		List<NodeVo> rbacPermissionType = UomClassProvider.getValuesList(
				"RbacPermission", "rbacPermissionType");
		ListboxUtils.rendererForEdit(bean.getRbacPermissionType(),
				rbacPermissionType);

	}

	/**
	 * 保存.
	 * 
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onOk() throws PortalException, SystemException {

		if (StrUtil.isEmpty(bean.getRbacPermissionCode().getValue())) {
			ZkUtil.showError("权限编码不能为空,请填写", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(bean.getRbacPermissionName().getValue())) {
			ZkUtil.showError("权限名称不能为空,请填写", "提示信息");
			return;
		}

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacPermission = new RbacPermission();
		} else if ("mod".equals(opType)) {
			rbacPermission = oldRbacPermission;
		}

		rbacPermission.setRbacPermissionCode(bean.getRbacPermissionCode()
				.getValue());
		rbacPermission.setRbacPermissionName(bean.getRbacPermissionName()
				.getValue());
		if (bean.getRbacPermissionType().getSelectedItem().getValue() != null) {
			rbacPermission.setRbacPermissionType(bean.getRbacPermissionType()
					.getSelectedItem().getValue().toString());
		}
		rbacPermission.setRbacPermissionDesc(bean.getRbacPermissionDesc()
				.getValue());

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			List<RbacPermissionExtAttr> beanList = this.bean
					.getRbacPermissionExtAttrExt().getExtendValueList();
			rbacPermission.setRbacPermissionExtAttrList(beanList);
			rbacPermissionManager.saveRbacPermission(rbacPermission);
			this.updateRbacPermissionToRaptornuke();
		} else if ("mod".equals(opType)) {
			rbacPermissionManager.updateRbacPermission(rbacPermission);
			this.updateRbacPermissionToRaptornuke();
		}

		// 抛出成功事件
		Events.postEvent("onOK", bean.getRbacPermissionEditWindow(),
				rbacPermission);
		bean.getRbacPermissionEditWindow().onClose();

	}

	public void updateRbacPermissionToRaptornuke() throws PortalException,
			SystemException {

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
								rbacPermission, rbacPermissionExtAttr);

					}
				}
			}
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getRbacPermissionEditWindow().onClose();
	}

}
