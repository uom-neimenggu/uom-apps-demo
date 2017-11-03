package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class RbacRoleTelcomRegionListboxExtBean {

	@Getter
	@Setter
	private Panel rbacRoleTelcomRegionListboxExtPanel;

	@Getter
	@Setter
	private Div rbacRoleTelcomRegionSearchDiv;

	@Getter
	@Setter
	private Textbox rbacRoleCode;

	@Getter
	@Setter
	private Textbox rbacRoleName;

	@Getter
	@Setter
	private Textbox regionCode;

	@Getter
	@Setter
	private Textbox regionName;

	@Getter
	@Setter
	private Toolbarbutton addRbacRoleTelcomRegionButton;

	// @Getter
	// @Setter
	// private Toolbarbutton editRbacRoleTelcomRegionButton;

	@Getter
	@Setter
	private Toolbarbutton delRbacRoleTelcomRegionButton;

	@Getter
	@Setter
	private Listbox rbacRoleTelcomRegionListbox;

	@Getter
	@Setter
	private Paging rbacRoleTelcomRegionListPaging;

}
