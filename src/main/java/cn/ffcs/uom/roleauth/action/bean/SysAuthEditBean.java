package cn.ffcs.uom.roleauth.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.roleauth.component.AuthorityTreeBandboxExt;
import cn.ffcs.uom.syslist.component.SysListTreeBandboxExt;

public class SysAuthEditBean {

	@Getter
	@Setter
	private Window sysAuthEditWindow;
	@Getter
	@Setter
	private AuthorityTreeBandboxExt authBandboxExt;
	@Getter
	@Setter
	private SysListTreeBandboxExt sysBandboxExt;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
	
}
