package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class BusinessSystemSwitchEditBean {
	@Getter
	@Setter
	private Window businessSystemSwitchEditWin;
	@Getter
	@Setter
	private Listbox systemControlSwitch;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;
}
