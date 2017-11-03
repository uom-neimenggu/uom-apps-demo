package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacBusinessSystemResourceListboxExtBean {

	@Getter
	@Setter
	private Panel rbacBusinessSystemResourceListboxExtPanel;

	@Getter
	@Setter
	private Div rbacBusinessSystemResourceSearchDiv;

	@Getter
	@Setter
	private Textbox rbacResourceCode;

	@Getter
	@Setter
	private Textbox rbacResourceName;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemCode;

	@Getter
	@Setter
	private Textbox rbacBusinessSystemName;

	@Getter
	@Setter
	private Toolbarbutton addRbacBusinessSystemResourceButton;

	@Getter
	@Setter
	private Toolbarbutton editRbacBusinessSystemResourceButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacBusinessSystemResourceButton;

	@Getter
	@Setter
	private Listbox rbacBusinessSystemResourceListbox;

	@Getter
	@Setter
	private Paging rbacBusinessSystemResourceListPaging;

}
