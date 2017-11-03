package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class BusinessSystemEditBean {
	@Getter
	@Setter
	private Window businessSystemEditWin;
	@Getter
	@Setter
	private Textbox businessSystemId;
	@Getter
	@Setter
	private Textbox systemCode;
	@Getter
	@Setter
	private Textbox systemName;
	@Getter
	@Setter
	private Textbox systemDesc;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
