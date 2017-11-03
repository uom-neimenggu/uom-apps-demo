package cn.ffcs.uom.staffrole.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.staff.component.StaffBandboxExt;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;

public class StaffRoleEditBean {

	@Getter
	@Setter
	private Window staffRoleEditWindow;
	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;
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
