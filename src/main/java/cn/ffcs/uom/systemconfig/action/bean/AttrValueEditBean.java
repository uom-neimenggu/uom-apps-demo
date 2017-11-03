package cn.ffcs.uom.systemconfig.action.bean;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class AttrValueEditBean {
	private Window attrValueEditWin;
	private Textbox attrValue;
	private Textbox attrValueName;
	private Textbox attrDesc;
	private Button okButton;
	private Button cancelButton;

	public Window getAttrValueEditWin() {
		return attrValueEditWin;
	}

	public void setAttrValueEditWin(Window attrValueEditWin) {
		this.attrValueEditWin = attrValueEditWin;
	}

	public Textbox getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(Textbox attrValue) {
		this.attrValue = attrValue;
	}

	public Textbox getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(Textbox attrValueName) {
		this.attrValueName = attrValueName;
	}

	public Textbox getAttrDesc() {
		return attrDesc;
	}

	public void setAttrDesc(Textbox attrDesc) {
		this.attrDesc = attrDesc;
	}

	public Button getOkButton() {
		return okButton;
	}

	public void setOkButton(Button okButton) {
		this.okButton = okButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

}
