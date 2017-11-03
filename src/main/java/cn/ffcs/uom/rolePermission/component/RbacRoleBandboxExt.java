package cn.ffcs.uom.rolePermission.component;

import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.model.RbacRole;

@Controller
@Scope("prototype")
public class RbacRoleBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_bandbox_ext.zul";

	/**
	 * rbacRoleListboxExt
	 */
	private RbacRoleListboxExt rbacRoleListboxExt;

	/**
	 * 角色
	 */
	@Getter
	private RbacRole rbacRole;

	/**
	 * 构造函数
	 */
	public RbacRoleBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.rbacRoleListboxExt.addForward(
				RolePermissionConstants.ON_SELECT_RBAC_ROLE_REQUEST, this,
				RolePermissionConstants.ON_SELECT_RBAC_ROLE_RESPONSE);
		this.rbacRoleListboxExt.addForward(
				RolePermissionConstants.ON_CLEAN_RBAC_ROLE, this,
				"onCleanRbacRoleReponse");
		this.rbacRoleListboxExt.addForward(
				RolePermissionConstants.ON_CLOSE_RBAC_ROLE, this,
				"onCloseRbacRoleReponse");
		/**
		 * 设置按钮
		 */
		rbacRoleListboxExt.setRbacRoleOptDivVisible(false);
		rbacRoleListboxExt.setRbacRoleBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getRbacRole();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof RbacRole) {
			this.setRbacRole((RbacRole) assignObject);
		}
	}

	public void setRbacRole(RbacRole rbacRole) {
		this.setValue(rbacRole == null ? "" : rbacRole.getRbacRoleName());
		this.rbacRole = rbacRole;
	}

	/**
	 * 选择角色
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacRoleResponse(final ForwardEvent event)
			throws Exception {
		rbacRole = (RbacRole) event.getOrigin().getData();
		if (rbacRole != null) {
			this.setValue(rbacRole.getRbacRoleName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanRbacRoleReponse(final ForwardEvent event)
			throws Exception {
		this.setRbacRole(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseRbacRoleReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
