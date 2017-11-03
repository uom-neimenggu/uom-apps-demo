package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.rolePermission.component.RbacPermissionExtAttrExt;

public class RbacPermissionEditExtBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacPermissionEditExtPanel;

	/**
	 * 权限标识.
	 **/
	@Getter
	@Setter
	private Longbox rbacPermissionId;

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
	 * 编辑按钮.
	 */
	@Getter
	@Setter
	private Button editButton;

	/**
	 * 保存按钮.
	 */
	@Getter
	@Setter
	private Button saveButton;

	/**
	 * 恢复按钮.
	 */
	@Getter
	@Setter
	private Button recoverButton;

}
