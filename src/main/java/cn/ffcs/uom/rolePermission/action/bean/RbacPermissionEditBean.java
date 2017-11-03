package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionExtAttrExt;

public class RbacPermissionEditBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window rbacPermissionEditWindow;

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
	 * 权限类型.
	 **/
	@Getter
	@Setter
	private Listbox rbacPermissionType;

	/**
	 * 权限描述.
	 **/
	@Getter
	@Setter
	private Textbox rbacPermissionDesc;

	/**
	 * 权限扩展属性.
	 **/
	@Getter
	@Setter
	private RbacPermissionExtAttrExt rbacPermissionExtAttrExt;

	/**
	 * 确定按钮.
	 */
	@Getter
	@Setter
	private Button okButton;

	/**
	 * 取消按钮.
	 */
	@Getter
	@Setter
	private Button cancelButton;

}
