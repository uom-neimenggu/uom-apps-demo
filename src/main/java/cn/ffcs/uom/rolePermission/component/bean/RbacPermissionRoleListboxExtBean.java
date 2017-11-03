package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacPermissionRoleListboxExtBean {

	@Getter
	@Setter
	private Panel rbacPermissionRoleListboxExtPanel;

	@Getter
	@Setter
	private Div rbacPermissionRoleSearchDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox rbacPermissionCode;

	@Getter
	@Setter
	private Textbox rbacPermissionName;

	@Getter
	@Setter
	private Toolbarbutton addRbacPermissionRoleButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacPermissionRoleButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacPermissionRoleButton;

	@Getter
	@Setter
	private Listbox rbacPermissionRoleListbox;

	@Getter
	@Setter
	private Paging rbacPermissionRoleListPaging;

}
