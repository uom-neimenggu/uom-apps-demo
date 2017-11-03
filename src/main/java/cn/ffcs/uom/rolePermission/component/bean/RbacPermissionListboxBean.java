package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacPermissionListboxBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacPermissionPanel;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacPermissionSearchDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacPermissionOptDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacPermissionBandboxDiv;

	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox rbacPermissionListbox;

	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging rbacPermissionListboxPaging;

	/**
	 * 权限新增按钮.
	 */
	@Getter
	@Setter
	private Button addRbacPermissionButton;

	/**
	 * 权限查看按钮.
	 */
	@Getter
	@Setter
	private Button viewRbacPermissionButton;

	/**
	 * 权限编辑按钮.
	 */
	@Getter
	@Setter
	private Button editRbacPermissionButton;

	/**
	 * 权限删除按钮.
	 */
	@Getter
	@Setter
	private Button delRbacPermissionButton;

	/**
	 * 权限编码.
	 **/
	@Getter
	@Setter
	private Textbox rbacPermissionCode;

	/**
	 * 权限名称.
	 **/
	@Getter
	@Setter
	private Textbox rbacPermissionName;

	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryRbacPermission;

	/**
	 * 重置按钮.
	 */
	@Getter
	@Setter
	private Button resetRbacPermission;

	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closeRbacPermissionButton;

	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanRbacPermissionButton;

}
