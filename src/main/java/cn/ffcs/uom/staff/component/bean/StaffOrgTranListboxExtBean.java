package cn.ffcs.uom.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class StaffOrgTranListboxExtBean {

	@Getter
	@Setter
	private Button addStaffOrgTranButton;

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
	private Button editStaffOrgTranButton;

	@Getter
	@Setter
	private Button delStaffOrgTranButton;

	@Getter
	@Setter
	private Listbox staffOrgTranListbox;

	@Getter
	@Setter
	private Paging staffOrgTranListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffOrgTranSearchDiv;
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
