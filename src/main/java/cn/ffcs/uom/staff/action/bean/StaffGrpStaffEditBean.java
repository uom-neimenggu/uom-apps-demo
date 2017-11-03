package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.restservices.component.GrpStaffBandboxExt;
import cn.ffcs.uom.staff.component.StaffBandboxExt;

public class StaffGrpStaffEditBean {
	@Getter
	@Setter
	private Window staffGrpStaffEditWindow;
	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;
	@Getter
	@Setter
	private GrpStaffBandboxExt grpStaffBandboxExt;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
}
