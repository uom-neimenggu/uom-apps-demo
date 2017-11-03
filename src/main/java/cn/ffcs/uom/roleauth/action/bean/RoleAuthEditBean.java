package cn.ffcs.uom.roleauth.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.roleauth.component.AuthorityTreeBandboxExt;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;

public class RoleAuthEditBean {

	@Getter
	@Setter
	private Window roleAuthEditWindow;
	@Getter
	@Setter
	private AuthorityTreeBandboxExt authBandboxExt;
	@Getter
	@Setter
	private StaffRoleTreeBandboxExt staffRoleBandboxExt;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
	
}
