package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacRoleOrganizationLevelListboxExtBean {

	@Getter
	@Setter
	private Panel rbacRoleOrganizationLevelListboxExtPanel;

	@Getter
	@Setter
	private Div rbacRoleOrganizationLevelSearchDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox orgCode;

	@Getter
	@Setter
	private Textbox orgName;

	@Getter
	@Setter
	private Toolbarbutton addRbacRoleOrganizationLevelButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacRoleOrganizationLevelButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacRoleOrganizationLevelButton;

	@Getter
	@Setter
	private Toolbarbutton viewOrganizationPathButton;

	@Getter
	@Setter
	private Listbox rbacRoleOrganizationLevelListbox;

	@Getter
	@Setter
	private Paging rbacRoleOrganizationLevelListPaging;

}
