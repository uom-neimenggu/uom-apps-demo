package cn.ffcs.uom.accconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SysAccListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window sysAccMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox sysAccListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging sysAccListboxPaging;
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
	private Div sysAccWindowDiv;
	/**
	 * 系统名称.
	 **/
	@Getter
	@Setter
	private Textbox sysName;
	/**
	 * 配置名称.
	 **/
	@Getter
	@Setter
	private Textbox accName;
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
