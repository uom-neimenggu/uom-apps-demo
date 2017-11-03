package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.rolePermission.component.RbacRoleExtAttrExt;

public class RbacRoleEditExtBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacRoleEditExtPanel;

	/**
	 * 角色标识.
	 **/
	@Getter
	@Setter
	private Longbox rbacRoleId;

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
