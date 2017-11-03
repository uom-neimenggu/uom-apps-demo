package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SysListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffSysMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox sysListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging sysListboxPaging;
	/**
	 * 系统新增按钮.
	 */
	@Getter
	@Setter
	private Button addButton;
	/**
	 * 系统查看按钮.
	 */
	@Getter
	@Setter
	private Button viewButton;
	/**
	 * 系统编辑按钮.
	 */
	@Getter
	@Setter
	private Button editButton;
	/**
	 * 系统删除按钮.
	 */
	@Getter
	@Setter
	private Button delButton;
	/**
	 * staffRoleSearchWindowDiv.
	 */
	@Getter
	@Setter
	private Div searchWindowDiv;
	/**
	 * staffRoleWindowDiv.
	 */
	@Getter
	@Setter
	private Div sysWindowDiv;
	/**
	 * 员工编码.
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
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private Textbox clientCode;
	/**
	 * 系统类型.
	 **/
	@Getter
	@Setter
	private Listbox sysType;
	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button querySysList;
	/**
	 * staffRoleWindowDiv.
	 */
	@Getter
	@Setter
	private Div sysListBandboxDiv;
	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closePosiitonButton;
	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanStaffRoleButton;
}
