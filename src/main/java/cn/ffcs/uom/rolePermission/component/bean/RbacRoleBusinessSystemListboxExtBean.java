package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacRoleBusinessSystemListboxExtBean {

	@Getter
	@Setter
	private Panel rbacRoleBusinessSystemListboxExtPanel;

	@Getter
	@Setter
	private Div rbacRoleBusinessSystemSearchDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemCode;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemName;

	@Getter
	@Setter
	private Toolbarbutton addRbacRoleBusinessSystemButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacRoleBusinessSystemButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacRoleBusinessSystemButton;

	@Getter
	@Setter
	private Listbox rbacRoleBusinessSystemListbox;

	@Getter
	@Setter
	private Paging rbacRoleBusinessSystemListPaging;

}
