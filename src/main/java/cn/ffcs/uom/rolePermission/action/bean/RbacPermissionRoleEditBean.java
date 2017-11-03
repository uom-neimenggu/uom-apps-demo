package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionRelationTreeBandboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeBandboxExt;

public class RbacPermissionRoleEditBean {

	@Getter
	@Setter
	private Window rbacPermissionRoleEditWindow;

	@Getter
	@Setter
	private RbacRoleRelationTreeBandboxExt rbacRoleRelationTreeBandboxExt;
	
	@Getter
	@Setter
	private RbacPermissionRelationTreeBandboxExt rbacPermissionRelationTreeBandboxExt;

	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
