package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacRoleOrganizationListboxExtBean {

	@Getter
	@Setter
	private Panel rbacRoleOrganizationListboxExtPanel;

	@Getter
	@Setter
	private Div rbacRoleOrganizationSearchDiv;

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
	private Toolbarbutton addRbacRoleOrganizationButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacRoleOrganizationButton;

	@Getter
	@Setter
	private Toolbarbutton viewOrganizationPathButton;

	@Getter
	@Setter
	private Listbox rbacRoleOrganizationListbox;

	@Getter
	@Setter
	private Paging rbacRoleOrganizationListPaging;

}
