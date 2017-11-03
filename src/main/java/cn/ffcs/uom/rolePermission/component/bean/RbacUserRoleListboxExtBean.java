package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacUserRoleListboxExtBean {

	@Getter
	@Setter
	private Panel rbacUserRoleListboxExtPanel;

	@Getter
	@Setter
	private Div rbacUserRoleSearchDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacUserRoleOptDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacUserRoleBandboxDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox staffAccount;

	@Getter
	@Setter
	private Textbox staffName;

	@Getter
	@Setter
	private Toolbarbutton addRbacUserRoleButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacUserRoleButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacUserRoleButton;

	@Getter
	@Setter
	private Listbox rbacUserRoleListbox;

	@Getter
	@Setter
	private Paging rbacUserRoleListPaging;

}
