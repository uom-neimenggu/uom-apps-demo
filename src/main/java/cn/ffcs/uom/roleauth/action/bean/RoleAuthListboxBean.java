package cn.ffcs.uom.roleauth.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class RoleAuthListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window roleAuthMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox authListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging authListboxPaging;
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
	private Div authWindowDiv;
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
	 * 权限名称.
	 **/
	@Getter
	@Setter
	private Textbox authName;
	
	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryAuthList;

	@Getter
	@Setter
	private Div authListBandboxDiv;
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
