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
import cn.ffcs.uom.rolePermission.model.RbacResource;

@Controller
@Scope("prototype")
public class RbacResourceBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_resource_bandbox_ext.zul";

	/**
	 * rbacResourceListboxExt
	 */
	private RbacResourceListboxExt rbacResourceListboxExt;

	/**
	 * 角色
	 */
	@Getter
	private RbacResource rbacResource;

	/**
	 * 构造函数
	 */
	public RbacResourceBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.rbacResourceListboxExt.addForward(
				RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_REQUEST, this,
				RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_RESPONSE);
		this.rbacResourceListboxExt.addForward(
				RolePermissionConstants.ON_CLEAN_RBAC_RESOURCE, this,
				"onCleanRbacResourceReponse");
		this.rbacResourceListboxExt.addForward(
				RolePermissionConstants.ON_CLOSE_RBAC_RESOURCE, this,
				"onCloseRbacResourceReponse");
		/**
		 * 设置按钮
		 */
		rbacResourceListboxExt.setRbacResourceOptDivVisible(false);
		rbacResourceListboxExt.setRbacResourceBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getRbacResource();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof RbacResource) {
			this.setRbacResource((RbacResource) assignObject);
		}
	}

	public void setRbacResource(RbacResource rbacResource) {
		this.setValue(rbacResource == null ? "" : rbacResource
				.getRbacResourceName());
		this.rbacResource = rbacResource;
	}

	/**
	 * 选择角色
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacResourceResponse(final ForwardEvent event)
			throws Exception {
		rbacResource = (RbacResource) event.getOrigin().getData();
		if (rbacResource != null) {
			this.setValue(rbacResource.getRbacResourceName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanRbacResourceReponse(final ForwardEvent event)
			throws Exception {
		this.setRbacResource(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseRbacResourceReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
