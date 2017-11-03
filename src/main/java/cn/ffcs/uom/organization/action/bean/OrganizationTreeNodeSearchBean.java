package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class OrganizationTreeNodeSearchBean {
	@Getter
	@Setter
	private Window organizationTreeNodeSearchWindow;
	@Getter
	@Setter
	private Textbox orgName;
	@Getter
	@Setter
	private Toolbarbutton serarchButton;
}
