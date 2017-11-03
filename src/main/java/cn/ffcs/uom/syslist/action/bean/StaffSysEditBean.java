package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.staff.component.StaffBandboxExt;
import cn.ffcs.uom.syslist.component.SysListTreeBandboxExt;

public class StaffSysEditBean {

	@Getter
	@Setter
	private Window staffSysEditWindow;
	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;
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
