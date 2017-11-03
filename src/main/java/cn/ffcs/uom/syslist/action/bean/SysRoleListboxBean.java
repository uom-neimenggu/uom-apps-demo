package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SysRoleListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window sysRoleMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox sysRoleListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging sysRoleListboxPaging;
	/**
	 * 新增按钮.
	 */
	@Getter
	@Setter
	private Button addButton;
	/**
	 * 查看按钮.
	 */
	@Getter
	@Setter
	private Button viewButton;
	/**
	 * 编辑按钮.
	 */
	@Getter
	@Setter
	private Button editButton;
	/**
	 * 删除按钮.
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
	private Div sysRoleWindowDiv;
	/**
	 * 角色编码.
	 **/
	@Getter
	@Setter
	private Textbox roleCode;
	/**
	 * 角色名称.
	 **/
	@Getter
	@Setter
	private Textbox roleName;
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
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closePosiitonButton;
}
