package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;
import cn.ffcs.uom.syslist.component.SysListTreeBandboxExt;

public class SysRoleEditBean {

	@Getter
	@Setter
	private Window sysRoleEditWindow;
	@Getter
	@Setter
	private StaffRoleTreeBandboxExt staffRoleBandboxExt;
	@Getter
	@Setter
	private SysListTreeBandboxExt sysListBandboxExt;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
	
}
