package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class OrgTreeEditBean {
	@Getter
	@Setter
	private Window orgTreeEditWin;
	@Getter
	@Setter
	private Textbox orgTreeName;
	@Getter
	@Setter
	private Listbox ISCalc;
	@Getter
	@Setter
	private Datebox preTime;
	@Getter
	@Setter
	private Datebox lastTime;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
