package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacRoleExtAttrExt;

public class RbacRoleEditBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window rbacRoleEditWindow;

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
	 * 角色类型.
	 **/
	@Getter
	@Setter
	private Listbox rbacRoleType;

	/**
	 * 角色描述.
	 **/
	@Getter
	@Setter
	private Textbox rbacRoleDesc;

	/**
	 * 角色扩展属性.
	 **/
	@Getter
	@Setter
	private RbacRoleExtAttrExt rbacRoleExtAttrExt;

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
