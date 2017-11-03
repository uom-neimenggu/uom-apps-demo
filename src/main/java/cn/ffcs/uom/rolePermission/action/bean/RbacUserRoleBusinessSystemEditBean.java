package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemRelationTreeBandboxExt;
import cn.ffcs.uom.rolePermission.component.RbacUserRoleBandboxExt;

public class RbacUserRoleBusinessSystemEditBean {

	@Getter
	@Setter
	private Window rbacUserRoleBusinessSystemEditWindow;

	@Getter
	@Setter
	private RbacUserRoleBandboxExt rbacUserRoleBandboxExt;

	@Getter
	@Setter
	private RbacBusinessSystemRelationTreeBandboxExt rbacBusinessSystemRelationTreeBandboxExt;

	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
