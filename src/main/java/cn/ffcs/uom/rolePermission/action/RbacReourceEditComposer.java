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
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;

@Controller
@Scope("prototype")
public class RbacReourceEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * rbacRoleEditBean.
	 */
	private RbacRoleEditBean bean = new RbacRoleEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("rbacRoleManager")
	private RbacRoleManager rbacRoleManager;

	/**
	 * 角色.
	 */
	private RbacRole rbacRole;

	/**
	 * 父角色.
	 */
	@SuppressWarnings("unused")
	private RbacRole rbacParentRole;

	/**
	 * 修改角色.
	 */
	private RbacRole oldRbacRole;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacRoleEditWindow() {

		this.bindCombobox();

		opType = StrUtil.strnull(arg.get("opType"));

		if ("addChildNode".equals(opType)) {
			this.bean.getRbacRoleEditWindow().setTitle("新增子节点");
			rbacParentRole = (RbacRole) arg.get("rbacRole");
		} else if ("addRootNode".equals(opType)) {
			this.bean.getRbacRoleEditWindow().setTitle("新增根节点");
		} else {
			if ("view".equals(opType)) {
				this.bean.getRbacRoleEditWindow().setTitle("角色查看");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			} else {
				this.bean.getRbacRoleEditWindow().setTitle("角色修改");
			}
			oldRbacRole = (RbacRole) arg.get("rbacRole");
			if (oldRbacRole != null) {
				this.bean.getRbacRoleCode().setValue(
						oldRbacRole.getRbacRoleCode());
				this.bean.getRbacRoleName().setValue(
						oldRbacRole.getRbacRoleName());
				ListboxUtils.selectByCodeValue(this.bean.getRbacRoleType(),
						oldRbacRole.getRbacRoleType());
				this.bean.getRbacRoleDesc().setValue(
						oldRbacRole.getRbacRoleDesc());
			}
		}
	}

	/**
	 * 绑定combobox.
	 */
	private void bindCombobox() {
		List<NodeVo> rbacRoleType = UomClassProvider.getValuesList("RbacRole",
				"rbacRoleType");
		ListboxUtils.rendererForEdit(bean.getRbacRoleType(), rbacRoleType);

	}

	/**
	 * 保存.
	 */
	public void onOk() {

		if (StrUtil.isEmpty(bean.getRbacRoleCode().getValue())) {
			ZkUtil.showError("角色编码不能为空,请填写", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(bean.getRbacRoleName().getValue())) {
			ZkUtil.showError("角色名称不能为空,请填写", "提示信息");
			return;
		}

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacRole = new RbacRole();
		} else if ("mod".equals(opType)) {
			rbacRole = oldRbacRole;
		}

		rbacRole.setRbacRoleCode(bean.getRbacRoleCode().getValue());
		rbacRole.setRbacRoleName(bean.getRbacRoleName().getValue());
		if (bean.getRbacRoleType().getSelectedItem().getValue() != null) {
			rbacRole.setRbacRoleType(bean.getRbacRoleType().getSelectedItem()
					.getValue().toString());
		}
		rbacRole.setRbacRoleDesc(bean.getRbacRoleDesc().getValue());

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacRoleManager.saveRbacRole(rbacRole);
		} else if ("mod".equals(opType)) {
			rbacRoleManager.updateRbacRole(rbacRole);
		}

		// 抛出成功事件
		Events.postEvent("onOK", bean.getRbacRoleEditWindow(), rbacRole);
		bean.getRbacRoleEditWindow().onClose();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getRbacRoleEditWindow().onClose();
	}

}
