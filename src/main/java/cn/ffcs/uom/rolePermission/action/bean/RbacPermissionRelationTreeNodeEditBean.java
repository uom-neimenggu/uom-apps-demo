package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionBandboxExt;

public class RbacPermissionRelationTreeNodeEditBean {
	@Getter
	@Setter
	private Window rbacPermissionRelationTreeNodeEditWindow;
	
	@Getter
	@Setter
	private RbacPermissionBandboxExt rbacPermissionBandboxExt;
}
