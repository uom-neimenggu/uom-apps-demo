package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeBandboxExt;
import cn.ffcs.uom.staff.component.StaffBandboxExt;

public class RbacUserRoleEditBean {

	@Getter
	@Setter
	private Window rbacUserRoleEditWindow;

	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;

	@Getter
	@Setter
	private RbacRoleRelationTreeBandboxExt rbacRoleRelationTreeBandboxExt;

	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
