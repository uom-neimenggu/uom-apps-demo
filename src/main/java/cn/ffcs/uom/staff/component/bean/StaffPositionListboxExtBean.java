package cn.ffcs.uom.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class StaffPositionListboxExtBean {
	private Panel staffPositionListboxExtPanel;
	private Toolbarbutton addButton;
	private Toolbarbutton delButton;
	private Listbox staffPositionListbox;
	private Paging staffPositionListboxPaging;
	
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffPositionSearchDiv;
	/**
	 * 组织名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private Textbox positionName;

	public Panel getStaffPositionListboxExtPanel() {
		return staffPositionListboxExtPanel;
	}

	public void setStaffPositionListboxExtPanel(
			Panel staffPositionListboxExtPanel) {
		this.staffPositionListboxExtPanel = staffPositionListboxExtPanel;
	}

	public Toolbarbutton getAddButton() {
		return addButton;
	}

	public void setAddButton(Toolbarbutton addButton) {
		this.addButton = addButton;
	}

	public Toolbarbutton getDelButton() {
		return delButton;
	}

	public void setDelButton(Toolbarbutton delButton) {
		this.delButton = delButton;
	}

	public Listbox getStaffPositionListbox() {
		return staffPositionListbox;
	}

	public void setStaffPositionListbox(Listbox staffPositionListbox) {
		this.staffPositionListbox = staffPositionListbox;
	}

	public Paging getStaffPositionListboxPaging() {
		return staffPositionListboxPaging;
	}

	public void setStaffPositionListboxPaging(Paging staffPositionListboxPaging) {
		this.staffPositionListboxPaging = staffPositionListboxPaging;
	}

}
