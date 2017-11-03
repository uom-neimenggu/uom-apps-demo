package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacResourceBandboxExt;

public class RbacResourceRelationTreeNodeEditBean {
	@Getter
	@Setter
	private Window rbacResourceRelationTreeNodeEditWindow;
	
	@Getter
	@Setter
	private RbacResourceBandboxExt rbacResourceBandboxExt;
}
