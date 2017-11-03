package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacBusinessSystemListboxBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacBusinessSystemPanel;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacBusinessSystemSearchDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacBusinessSystemOptDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacBusinessSystemBandboxDiv;

	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox rbacBusinessSystemListbox;

	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging rbacBusinessSystemListboxPaging;

	/**
	 * 系统新增按钮.
	 */
	@Getter
	@Setter
	private Button addRbacBusinessSystemButton;

	/**
	 * 系统查看按钮.
	 */
	@Getter
	@Setter
	private Button viewRbacBusinessSystemButton;

	/**
	 * 系统编辑按钮.
	 */
	@Getter
	@Setter
	private Button editRbacBusinessSystemButton;

	/**
	 * 系统删除按钮.
	 */
	@Getter
	@Setter
	private Button delRbacBusinessSystemButton;

	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemCode;

	/**
	 * 系统名称.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemName;

	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryRbacBusinessSystem;

	/**
	 * 重置按钮.
	 */
	@Getter
	@Setter
	private Button resetRbacBusinessSystem;

	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closeRbacBusinessSystemButton;

	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanRbacBusinessSystemButton;

}
