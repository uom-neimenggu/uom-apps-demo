package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * 员工管理Bean
 * 
 * @author
 **/
public class GrpStaffListboxExtBean {
	/**
	 * panel.
	 **/
	@Getter
	@Setter
	private Panel grpStaffListboxExtPanel;
	/**
	 * Listbox.
	 **/
	@Getter
	@Setter
	private Listbox grpStaffListBox;
	/**
	 * 员工编码.
	 **/
	@Getter
	@Setter
	private Textbox salesCode;
	/**
	 * 员工工号.
	 **/
	@Getter
	@Setter
	private Textbox staffCode;
	/**
	 * 员工名称.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 证件号.
	 **/
	@Getter
	@Setter
	private Textbox certNumber;

	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging grpStaffListPaging;
	/**
	 * bandbox使用
	 */
	@Getter
	@Setter
	private Div grpStaffBandboxDiv;
	/**
	 * Button
	 */
	@Getter
	@Setter
	private Button proofreadButton;
}
