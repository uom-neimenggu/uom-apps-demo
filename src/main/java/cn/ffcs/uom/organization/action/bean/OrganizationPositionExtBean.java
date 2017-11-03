package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class OrganizationPositionExtBean {
	private Panel organizationRelationListboxComp;
	private Div organizationRelationWindowDiv;
	private Toolbarbutton addButton;
	private Toolbarbutton delButton;
	private Toolbarbutton cleanButton;
	private Toolbarbutton closeButton;
	private Listbox OrganizationPositionListbox;
	private Paging organizationPositionPaging;
	
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div positionOrganizationSearchDiv;
	/**
	 * 岗位编码.
	 **/
	@Getter
	@Setter
	private Textbox positionCode;
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private Textbox positionName;

	public Panel getOrganizationRelationListboxComp() {
		return organizationRelationListboxComp;
	}

	public void setOrganizationRelationListboxComp(
			Panel organizationRelationListboxComp) {
		this.organizationRelationListboxComp = organizationRelationListboxComp;
	}

	public Div getOrganizationRelationWindowDiv() {
		return organizationRelationWindowDiv;
	}

	public void setOrganizationRelationWindowDiv(
			Div organizationRelationWindowDiv) {
		this.organizationRelationWindowDiv = organizationRelationWindowDiv;
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

	public Listbox getOrganizationPositionListbox() {
		return OrganizationPositionListbox;
	}

	public void setOrganizationPositionListbox(
			Listbox organizationPositionListbox) {
		OrganizationPositionListbox = organizationPositionListbox;
	}

	public Paging getOrganizationPositionPaging() {
		return organizationPositionPaging;
	}

	public void setOrganizationPositionPaging(Paging organizationPositionPaging) {
		this.organizationPositionPaging = organizationPositionPaging;
	}

	public Toolbarbutton getCleanButton() {
		return cleanButton;
	}

	public void setCleanButton(Toolbarbutton cleanButton) {
		this.cleanButton = cleanButton;
	}

	public Toolbarbutton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(Toolbarbutton closeButton) {
		this.closeButton = closeButton;
	}

}
