package cn.ffcs.uom.systemconfig.action.bean;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class SysClassAttrInfoBean {
	private Window sysClassAttrInfoWin;
	private Div attrSpecWindowDiv;
	private Toolbarbutton addAttrSpecButton;
	private Toolbarbutton editAttrSpecButton;
	private Toolbarbutton deleteAttrSpecButton;
	private Listbox attrSpecListbox;
	private Listheader listheaderIsDany;
	private Listheader listheaderAreaIdName;
	private Listcell listcellIsDany;
	private Listcell listcellAreaIdName;
	private Paging attrSpecPaging;
	private Div attrValueWindowDiv;
	private Toolbarbutton addAttrValueButton;
	private Toolbarbutton editAttrValueButton;
	private Toolbarbutton deleteAttrValueButton;
	private Listbox attrValueListbox;
	private Paging attrValuePaging;

	public Window getSysClassAttrInfoWin() {
		return sysClassAttrInfoWin;
	}

	public void setSysClassAttrInfoWin(Window sysClassAttrInfoWin) {
		this.sysClassAttrInfoWin = sysClassAttrInfoWin;
	}

	public Div getAttrSpecWindowDiv() {
		return attrSpecWindowDiv;
	}

	public void setAttrSpecWindowDiv(Div attrSpecWindowDiv) {
		this.attrSpecWindowDiv = attrSpecWindowDiv;
	}

	public Toolbarbutton getAddAttrSpecButton() {
		return addAttrSpecButton;
	}

	public void setAddAttrSpecButton(Toolbarbutton addAttrSpecButton) {
		this.addAttrSpecButton = addAttrSpecButton;
	}

	public Toolbarbutton getEditAttrSpecButton() {
		return editAttrSpecButton;
	}

	public void setEditAttrSpecButton(Toolbarbutton editAttrSpecButton) {
		this.editAttrSpecButton = editAttrSpecButton;
	}

	public Toolbarbutton getDeleteAttrSpecButton() {
		return deleteAttrSpecButton;
	}

	public void setDeleteAttrSpecButton(Toolbarbutton deleteAttrSpecButton) {
		this.deleteAttrSpecButton = deleteAttrSpecButton;
	}

	public Listbox getAttrSpecListbox() {
		return attrSpecListbox;
	}

	public void setAttrSpecListbox(Listbox attrSpecListbox) {
		this.attrSpecListbox = attrSpecListbox;
	}

	public Listheader getListheaderIsDany() {
		return listheaderIsDany;
	}

	public void setListheaderIsDany(Listheader listheaderIsDany) {
		this.listheaderIsDany = listheaderIsDany;
	}

	public Listheader getListheaderAreaIdName() {
		return listheaderAreaIdName;
	}

	public void setListheaderAreaIdName(Listheader listheaderAreaIdName) {
		this.listheaderAreaIdName = listheaderAreaIdName;
	}

	public Listcell getListcellIsDany() {
		return listcellIsDany;
	}

	public void setListcellIsDany(Listcell listcellIsDany) {
		this.listcellIsDany = listcellIsDany;
	}

	public Listcell getListcellAreaIdName() {
		return listcellAreaIdName;
	}

	public void setListcellAreaIdName(Listcell listcellAreaIdName) {
		this.listcellAreaIdName = listcellAreaIdName;
	}

	public Paging getAttrSpecPaging() {
		return attrSpecPaging;
	}

	public void setAttrSpecPaging(Paging attrSpecPaging) {
		this.attrSpecPaging = attrSpecPaging;
	}

	public Div getAttrValueWindowDiv() {
		return attrValueWindowDiv;
	}

	public void setAttrValueWindowDiv(Div attrValueWindowDiv) {
		this.attrValueWindowDiv = attrValueWindowDiv;
	}

	public Toolbarbutton getAddAttrValueButton() {
		return addAttrValueButton;
	}

	public void setAddAttrValueButton(Toolbarbutton addAttrValueButton) {
		this.addAttrValueButton = addAttrValueButton;
	}

	public Toolbarbutton getEditAttrValueButton() {
		return editAttrValueButton;
	}

	public void setEditAttrValueButton(Toolbarbutton editAttrValueButton) {
		this.editAttrValueButton = editAttrValueButton;
	}

	public Toolbarbutton getDeleteAttrValueButton() {
		return deleteAttrValueButton;
	}

	public void setDeleteAttrValueButton(Toolbarbutton deleteAttrValueButton) {
		this.deleteAttrValueButton = deleteAttrValueButton;
	}

	public Listbox getAttrValueListbox() {
		return attrValueListbox;
	}

	public void setAttrValueListbox(Listbox attrValueListbox) {
		this.attrValueListbox = attrValueListbox;
	}

	public Paging getAttrValuePaging() {
		return attrValuePaging;
	}

	public void setAttrValuePaging(Paging attrValuePaging) {
		this.attrValuePaging = attrValuePaging;
	}
}
