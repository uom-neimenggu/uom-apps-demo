package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacUserRoleBusinessSystemListboxExtBean {

	@Getter
	@Setter
	private Panel rbacUserRoleBusinessSystemListboxExtPanel;

	@Getter
	@Setter
	private Div rbacUserRoleBusinessSystemSearchDiv;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemCode;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemName;

	@Getter
	@Setter
	private Textbox staffAccount;

	@Getter
	@Setter
	private Textbox staffName;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Toolbarbutton addRbacUserRoleBusinessSystemButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacUserRoleBusinessSystemButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacUserRoleBusinessSystemButton;

	@Getter
	@Setter
	private Listbox rbacUserRoleBusinessSystemListbox;

	@Getter
	@Setter
	private Paging rbacUserRoleBusinessSystemListPaging;

}
