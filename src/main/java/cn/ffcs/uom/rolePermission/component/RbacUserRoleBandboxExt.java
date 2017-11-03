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
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

@Controller
@Scope("prototype")
public class RbacUserRoleBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_user_role_bandbox_ext.zul";

	/**
	 * rbacUserRoleListboxExt
	 */
	@Getter
	private RbacUserRoleListboxExt rbacUserRoleListboxExt;

	/**
	 * 员工角色
	 */
	@Getter
	private RbacUserRole rbacUserRole;

	/**
	 * 构造函数
	 */
	public RbacUserRoleBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.rbacUserRoleListboxExt.addForward(
				RolePermissionConstants.ON_SELECT_RBAC_USER_ROLE_REQUEST, this,
				RolePermissionConstants.ON_SELECT_RBAC_USER_ROLE_RESPONSE);
		this.rbacUserRoleListboxExt.addForward(
				RolePermissionConstants.ON_CLEAN_RBAC_USER_ROLE, this,
				"onCleanRbacUserRoleReponse");
		this.rbacUserRoleListboxExt.addForward(
				RolePermissionConstants.ON_CLOSE_RBAC_USER_ROLE, this,
				"onCloseRbacUserRoleReponse");
		/**
		 * 设置按钮
		 */
		rbacUserRoleListboxExt.setRbacUserRoleOptDivVisible(false);
		rbacUserRoleListboxExt.setRbacUserRoleBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getRbacUserRole();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof RbacUserRole) {
			this.setRbacUserRole((RbacUserRole) assignObject);
		}
	}

	public void setRbacUserRole(RbacUserRole rbacUserRole) {
		this.setValue(rbacUserRole == null ? "" : rbacUserRole.getRbacRole()
				.getRbacRoleName());
		this.rbacUserRole = rbacUserRole;
	}

	/**
	 * 选择员工角色
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacUserRoleResponse(final ForwardEvent event)
			throws Exception {
		rbacUserRole = (RbacUserRole) event.getOrigin().getData();
		if (rbacUserRole != null) {
			this.setValue(rbacUserRole.getRbacRoleName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanRbacUserRoleReponse(final ForwardEvent event)
			throws Exception {
		this.setRbacUserRole(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseRbacUserRoleReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
