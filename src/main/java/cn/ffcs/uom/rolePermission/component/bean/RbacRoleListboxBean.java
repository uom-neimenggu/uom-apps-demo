package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacRoleListboxBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacRolePanel;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacRoleSearchDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacRoleOptDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacRoleBandboxDiv;

	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox rbacRoleListbox;

	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging rbacRoleListboxPaging;

	/**
	 * 角色新增按钮.
	 */
	@Getter
	@Setter
	private Button addRbacRoleButton;

	/**
	 * 角色查看按钮.
	 */
	@Getter
	@Setter
	private Button viewRbacRoleButton;

	/**
	 * 角色编辑按钮.
	 */
	@Getter
	@Setter
	private Button editRbacRoleButton;

	/**
	 * 角色删除按钮.
	 */
	@Getter
	@Setter
	private Button delRbacRoleButton;

	/**
	 * 角色编码.
	 **/
	@Getter
	@Setter
	private Textbox rbacRoleCode;

	/**
	 * 角色名称.
	 **/
	@Getter
	@Setter
	private Textbox rbacRoleName;

	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryRbacRole;

	/**
	 * 重置按钮.
	 */
	@Getter
	@Setter
	private Button resetRbacRole;

	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closeRbacRoleButton;

	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanRbacRoleButton;

}
