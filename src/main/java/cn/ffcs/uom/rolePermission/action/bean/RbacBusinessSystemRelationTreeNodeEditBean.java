package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemBandboxExt;

public class RbacBusinessSystemRelationTreeNodeEditBean {
	@Getter
	@Setter
	private Window rbacBusinessSystemRelationTreeNodeEditWindow;

	@Getter
	@Setter
	private RbacBusinessSystemBandboxExt rbacBusinessSystemBandboxExt;
}
