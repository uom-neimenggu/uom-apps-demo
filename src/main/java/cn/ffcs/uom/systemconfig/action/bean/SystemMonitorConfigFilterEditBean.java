package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class SystemMonitorConfigFilterEditBean {
	@Getter
	@Setter
	private Window systemMonitorConfigFilterEditWin;
	@Getter
	@Setter
	private Textbox systemMonitorConfigId;
	@Getter
	@Setter
	private Listbox systemMonitorFilterSwitch;
	@Getter
	@Setter
	private Listbox filterName;
	@Getter
	@Setter
	private Listbox relationOperator;
	@Getter
	@Setter
	private Textbox filterValue;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;
}
