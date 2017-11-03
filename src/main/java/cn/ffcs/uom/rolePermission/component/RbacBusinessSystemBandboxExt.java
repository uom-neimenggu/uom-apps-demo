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
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

@Controller
@Scope("prototype")
public class RbacBusinessSystemBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_business_system_bandbox_ext.zul";

	/**
	 * rbacBusinessSystemListboxExt
	 */
	private RbacBusinessSystemListboxExt rbacBusinessSystemListboxExt;

	/**
	 * 系统
	 */
	@Getter
	private RbacBusinessSystem rbacBusinessSystem;

	/**
	 * 构造函数
	 */
	public RbacBusinessSystemBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.rbacBusinessSystemListboxExt
				.addForward(
						RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_REQUEST,
						this,
						RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_RESPONSE);
		this.rbacBusinessSystemListboxExt.addForward(
				RolePermissionConstants.ON_CLEAN_RBAC_BUSINESS_SYSTEM, this,
				"onCleanRbacBusinessSystemReponse");
		this.rbacBusinessSystemListboxExt.addForward(
				RolePermissionConstants.ON_CLOSE_RBAC_BUSINESS_SYSTEM, this,
				"onCloseRbacBusinessSystemReponse");
		/**
		 * 设置按钮
		 */
		rbacBusinessSystemListboxExt.setRbacBusinessSystemOptDivVisible(false);
		rbacBusinessSystemListboxExt
				.setRbacBusinessSystemBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getRbacBusinessSystem();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof RbacBusinessSystem) {
			this.setRbacBusinessSystem((RbacBusinessSystem) assignObject);
		}
	}

	public void setRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem) {
		this.setValue(rbacBusinessSystem == null ? "" : rbacBusinessSystem
				.getRbacBusinessSystemName());
		this.rbacBusinessSystem = rbacBusinessSystem;
	}

	/**
	 * 选择系统
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacBusinessSystemResponse(final ForwardEvent event)
			throws Exception {
		rbacBusinessSystem = (RbacBusinessSystem) event.getOrigin().getData();
		if (rbacBusinessSystem != null) {
			this.setValue(rbacBusinessSystem.getRbacBusinessSystemName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanRbacBusinessSystemReponse(final ForwardEvent event)
			throws Exception {
		this.setRbacBusinessSystem(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseRbacBusinessSystemReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
