package cn.ffcs.uom.gridUnit.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class StaffGridUnitTranListboxExtBean {

	@Getter
	@Setter
	private Button addStaffGridUnitTranButton;

	@Getter
	@Setter
	private Button editStaffGridUnitTranButton;

	/**
	 * 导入
	 */
	@Getter
	@Setter
	private Button uploadButton;
	/**
	 * 模板下载
	 */
	@Getter
	@Setter
	private Button downloadButton;

	@Getter
	@Setter
	private Button delStaffGridUnitTranButton;
	
	@Getter
	@Setter
	private Button exportStaffGridUnitTranButton;
	
	@Getter
	@Setter
	private Button exportGridUnitNoStaffTranButton;

	@Getter
	@Setter
	private Listbox staffGridUnitTranListbox;

	@Getter
	@Setter
	private Paging staffGridUnitTranListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffGridUnitTranSearchDiv;
	/**
	 * 网格单元ID.
	 **/
	@Getter
	@Setter
	private Longbox mmeFid;
	/**
	 * 网格单元名称.
	 **/
	@Getter
	@Setter
	private Textbox gridName;
	/**
	 * 员工姓名.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 员工账号.
	 **/
	@Getter
	@Setter
	private Textbox staffAccount;

}
