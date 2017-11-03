package cn.ffcs.uom.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class StaffCtPositionListboxExtBean {
	@Getter
	@Setter
	private Panel staffCtPositionListboxExtPanel;
	
	@Getter
	@Setter
	private Toolbarbutton addButton;
	
	@Getter
	@Setter
	private Toolbarbutton delButton;
	
	@Getter
	@Setter
	private Listbox staffCtPositionListbox;
	
	@Getter
	@Setter
	private Paging staffCtPositionListboxPaging;
	
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffCtPositionSearchDiv;
	
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private Textbox positionName;

}
