package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeBandboxExt;

public class RbacRolePolitLocationEditBean {

	@Getter
	@Setter
	private Window rbacRolePolitLocationEditWindow;

	@Getter
	@Setter
	private RbacRoleRelationTreeBandboxExt rbacRoleRelationTreeBandboxExt;
	
	@Getter
	@Setter
	private PoliticalLocationTreeBandbox politicalLocationTreeBandbox;

	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
