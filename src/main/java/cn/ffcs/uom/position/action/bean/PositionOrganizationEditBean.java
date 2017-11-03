package cn.ffcs.uom.position.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.position.component.PositionBandboxExt;

public class PositionOrganizationEditBean {

	@Getter
	@Setter
	private Window positionOrganizationEditWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt organizationBandboxExt;

	@Getter
	@Setter
	private PositionBandboxExt positionBandboxExt;

	@Getter
	@Setter
	private Button saveBtn;

	@Getter
	@Setter
	private Button cancelBtn;

}
