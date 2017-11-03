package cn.ffcs.uom.accconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.accconfig.component.AccConfigTreeBandboxExt;
import cn.ffcs.uom.syslist.component.SysListTreeBandboxExt;

public class SysAccEditBean {

	@Getter
	@Setter
	private Window sysAccEditWindow;
	@Getter
	@Setter
	private AccConfigTreeBandboxExt accConfigBandboxExt;
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
