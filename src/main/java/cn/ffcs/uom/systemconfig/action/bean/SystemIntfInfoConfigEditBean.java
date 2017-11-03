package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SystemIntfInfoConfigEditBean {
	@Getter
	@Setter
	private Window systemIntfInfoConfigEditWin;
	@Getter
	@Setter
	private Textbox systemCode;
	@Getter
	@Setter
	private Textbox msgType;
	@Getter
	@Setter
	private Listbox intfSwitchAll;
	@Getter
	@Setter
	private Listbox intfSwitchIncrease;
	@Getter
	@Setter
	private Textbox serviceCode;
	@Getter
	@Setter
	private Textbox oipUrl;
	@Getter
	@Setter
	private Textbox operationName;
	@Getter
	@Setter
	private Textbox paramName;
	@Getter
	@Setter
	private Textbox nameSpace;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
