package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacRolePolitLocationListboxExtBean {

	@Getter
	@Setter
	private Panel rbacRolePolitLocationListboxExtPanel;

	@Getter
	@Setter
	private Div rbacRolePolitLocationSearchDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox locationCode;

	@Getter
	@Setter
	private Textbox locationName;

	@Getter
	@Setter
	private Toolbarbutton addRbacRolePolitLocationButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacRolePolitLocationButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacRolePolitLocationButton;

	@Getter
	@Setter
	private Listbox rbacRolePolitLocationListbox;

	@Getter
	@Setter
	private Paging rbacRolePolitLocationListPaging;

}
