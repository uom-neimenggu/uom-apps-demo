package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionRelationTreeBandboxExt;
import cn.ffcs.uom.rolePermission.component.RbacResourceRelationTreeBandboxExt;

public class RbacPermissionResourceEditBean {

	@Getter
	@Setter
	private Window rbacPermissionResourceEditWindow;

	@Getter
	@Setter
	private RbacResourceRelationTreeBandboxExt rbacResourceRelationTreeBandboxExt;

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
