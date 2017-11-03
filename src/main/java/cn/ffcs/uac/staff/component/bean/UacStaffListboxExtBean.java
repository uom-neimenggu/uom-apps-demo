package cn.ffcs.uac.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class UacStaffListboxExtBean {
	@Getter
	@Setter
	private Listbox uacStaffListbox;

	@Getter
	@Setter
	private Listbox pageListbox;
	
	@Getter
	@Setter
	private Paging uacStaffListboxPaging;
	
	@Getter
	@Setter
	private Button viewUacStaffButton;
	
	@Getter
	@Setter
	private Button addUacStaffButton;
	
	@Getter
	@Setter
	private Button editUacStaffButton;
	
	@Getter
	@Setter
	private Button delUacStaffButton;
	
	@Getter
	@Setter
	private Div uacStaffDiv;
	
	@Getter
	@Setter
	private Textbox ecode;

	@Getter
	@Setter
	private Textbox account;

	@Getter
	@Setter
	private Textbox staffName;
	
}
