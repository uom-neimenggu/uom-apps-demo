package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class RbacRoleTelcomRegionEditBean {

	@Getter
	@Setter
	private Window rbacRoleTelcomRegionEditWindow;

	@Getter
	@Setter
	private RbacRoleRelationTreeBandboxExt rbacRoleRelationTreeBandboxExt;
	
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegionTreeBandbox;


	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
