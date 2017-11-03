package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SystemMonitorConfigEditBean {
	@Getter
	@Setter
	private Window systemMonitorConfigEditWin;
	@Getter
	@Setter
	private Textbox systemCode;
	@Getter
	@Setter
	private Listbox systemMonitorSwitch;
	@Getter
	@Setter
	private Listbox eventName;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;
}
