package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class GrpStaffChannelRelaListboxExtBean {
	@Getter
	@Setter
	private Panel grpStaffChannelRelaListboxExtPanel;
	@Getter
	@Setter
	private Listbox grpStaffChannelRelaListbox;
	@Getter
	@Setter
	private Paging grpStaffChannelRelaListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div grpStaffChannelRelaSearchDiv;
	/**
	 * 员工编码.
	 **/
	@Getter
	@Setter
	private Textbox salesCode;
	/**
	 * 员工名称.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
}
