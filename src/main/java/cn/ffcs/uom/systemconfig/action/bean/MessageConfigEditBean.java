package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class MessageConfigEditBean {
	@Getter
	@Setter
	private Window messageConfigEditWin;
	@Getter
	@Setter
	private Textbox userName;
	@Getter
	@Setter
	private Textbox telephoneNumber;
	@Getter
	@Setter
	private Textbox uomAcct;
	@Getter
	@Setter
	private Textbox emailAddress;
	@Getter
	@Setter
	private Listbox overallSituation;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
