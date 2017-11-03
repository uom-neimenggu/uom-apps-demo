package cn.ffcs.uom.systemconfig.action.bean;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class AttrSpecEditBean {
	private Window attrSpecEditWin;
	private Textbox javaCode;
	private Textbox attrCd;
	private Textbox attrName;
	private Listbox attrType;
	private Listbox attrValueType;
	private Textbox attrDesc;
	private Button okButton;
	private Button cancelButton;

	public Window getAttrSpecEditWin() {
		return attrSpecEditWin;
	}

	public void setAttrSpecEditWin(Window attrSpecEditWin) {
		this.attrSpecEditWin = attrSpecEditWin;
	}

	public Textbox getJavaCode() {
		return javaCode;
	}

	public void setJavaCode(Textbox javaCode) {
		this.javaCode = javaCode;
	}

	public Textbox getAttrCd() {
		return attrCd;
	}

	public void setAttrCd(Textbox attrCd) {
		this.attrCd = attrCd;
	}

	public Textbox getAttrName() {
		return attrName;
	}

	public void setAttrName(Textbox attrName) {
		this.attrName = attrName;
	}

	public Listbox getAttrType() {
		return attrType;
	}

	public void setAttrType(Listbox attrType) {
		this.attrType = attrType;
	}

	public Listbox getAttrValueType() {
		return attrValueType;
	}

	public void setAttrValueType(Listbox attrValueType) {
		this.attrValueType = attrValueType;
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
