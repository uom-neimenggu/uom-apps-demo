package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacPermissionResourceListboxExtBean {

	@Getter
	@Setter
	private Panel rbacPermissionResourceListboxExtPanel;

	@Getter
	@Setter
	private Div rbacPermissionResourceSearchDiv;

	@Getter
	@Setter
	private Textbox rbacResourceCode;

	@Getter
	@Setter
	private Textbox rbacResourceName;

	@Getter
	@Setter
	private Textbox rbacPermissionCode;

	@Getter
	@Setter
	private Textbox rbacPermissionName;

	@Getter
	@Setter
	private Toolbarbutton addRbacPermissionResourceButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacPermissionResourceButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacPermissionResourceButton;

	@Getter
	@Setter
	private Listbox rbacPermissionResourceListbox;

	@Getter
	@Setter
	private Paging rbacPermissionResourceListPaging;

}
