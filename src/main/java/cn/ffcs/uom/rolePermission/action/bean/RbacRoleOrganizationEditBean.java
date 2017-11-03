package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeBandboxExt;

public class RbacRoleOrganizationEditBean {

	@Getter
	@Setter
	private Window rbacRoleOrganizationEditWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt organizationBandboxExt;

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
