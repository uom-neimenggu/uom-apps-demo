package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacRoleBandboxExt;

public class RbacRoleRelationTreeNodeEditBean {
	@Getter
	@Setter
	private Window rbacRoleRelationTreeNodeEditWindow;
	@Getter
	@Setter
	private RbacRoleBandboxExt rbacRoleBandboxExt;
}
