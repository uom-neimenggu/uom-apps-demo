package cn.ffcs.uom.roleauth.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SysAuthListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window sysAuthMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox sysAuthListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging sysAuthListboxPaging;
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
	
	@Getter
	@Setter
	private Div searchWindowDiv;
	
	@Getter
	@Setter
	private Div sysAuthWindowDiv;
	/**
	 * 角色编码.
	 **/
	@Getter
	@Setter
	private Textbox clientCode;
	/**
	 * 角色名称.
	 **/
	@Getter
	@Setter
	private Textbox sysName;
	
	/**
	 * 权限名称.
	 **/
	@Getter
	@Setter
	private Textbox authName;

}
