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
import cn.ffcs.uom.rolePermission.model.RbacPermission;

@Controller
@Scope("prototype")
public class RbacPermissionBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_bandbox_ext.zul";

	/**
	 * rbacPermissionListboxExt
	 */
	private RbacPermissionListboxExt rbacPermissionListboxExt;

	/**
	 * 角色
	 */
	@Getter
	private RbacPermission rbacPermission;

	/**
	 * 构造函数
	 */
	public RbacPermissionBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.rbacPermissionListboxExt.addForward(
				RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_REQUEST,
				this,
				RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_RESPONSE);
		this.rbacPermissionListboxExt.addForward(
				RolePermissionConstants.ON_CLEAN_RBAC_PERMISSION, this,
				"onCleanRbacPermissionReponse");
		this.rbacPermissionListboxExt.addForward(
				RolePermissionConstants.ON_CLOSE_RBAC_PERMISSION, this,
				"onCloseRbacPermissionReponse");
		/**
		 * 设置按钮
		 */
		rbacPermissionListboxExt.setRbacPermissionOptDivVisible(false);
		rbacPermissionListboxExt.setRbacPermissionBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getRbacPermission();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof RbacPermission) {
			this.setRbacPermission((RbacPermission) assignObject);
		}
	}

	public void setRbacPermission(RbacPermission rbacPermission) {
		this.setValue(rbacPermission == null ? "" : rbacPermission
				.getRbacPermissionName());
		this.rbacPermission = rbacPermission;
	}

	/**
	 * 选择角色
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacPermissionResponse(final ForwardEvent event)
			throws Exception {
		rbacPermission = (RbacPermission) event.getOrigin().getData();
		if (rbacPermission != null) {
			this.setValue(rbacPermission.getRbacPermissionName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanRbacPermissionReponse(final ForwardEvent event)
			throws Exception {
		this.setRbacPermission(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseRbacPermissionReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
