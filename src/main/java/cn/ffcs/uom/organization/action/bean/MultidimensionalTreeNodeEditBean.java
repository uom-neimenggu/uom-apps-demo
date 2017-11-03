package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;

public class MultidimensionalTreeNodeEditBean {
	@Getter
	@Setter
	private Window multidimensionalTreeNodeEditWindow;
	@Getter
	@Setter
	private OrganizationBandboxExt organizationBandboxExt;
}
