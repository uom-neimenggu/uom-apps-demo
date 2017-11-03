package cn.ffcs.uom.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class StaffGrpStaffListboxExtBean {
	@Getter
	@Setter
	private Panel staffGrpStaffListboxExtPanel;
	@Getter
	@Setter
	private Toolbarbutton addButton;
	@Getter
	@Setter
	private Toolbarbutton delButton;
	@Getter
	@Setter
	private Listbox staffGrpStaffListbox;
	@Getter
	@Setter
	private Paging staffGrpStaffListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffGrpStaffSearchDiv;
	/**
	 * 集团渠道视图销售员姓名.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 集团渠道视图销售员编码.
	 **/
	@Getter
	@Setter
	private Textbox salesCode;
	/**
	 * 集团MSS姓名.
	 **/
	@Getter
	@Setter
	private Textbox userName;
	/**
	 * 集团MSS账号.
	 **/
	@Getter
	@Setter
	private Textbox loginName;
}
