package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationPositionBandboxExt;

public class StaffPositionEditBean {
	@Getter
	@Setter
	private Window staffPositionEditWindow;
	@Getter
	@Setter
	private Textbox staffName;
	@Getter
	@Setter
	private OrganizationPositionBandboxExt organizationPositionBandboxExt;
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
}
