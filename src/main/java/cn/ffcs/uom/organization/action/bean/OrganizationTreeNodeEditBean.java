package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;

public class OrganizationTreeNodeEditBean {
	@Getter
	@Setter
	private Window organizationTreeNodeEditWindow;
	@Getter
	@Setter
	private Row orgRelaCdRow;
	@Getter
	@Setter
	private Row orgRelaCdMultipleRow;
	@Getter
	@Setter
	private Row orgTypeCdMultipleRow;
	@Getter
	@Setter
	private TreeChooserBandbox orgRelaCd;
	@Getter
	@Setter
	private TreeChooserBandbox orgRelaCds;
	@Getter
	@Setter
	private TreeChooserBandbox orgTypeCd;
	/**
     * 变更原因.
     */
    @Setter
    @Getter
    private Textbox reason;
	@Getter
	@Setter
	private OrganizationBandboxExt organizationBandboxExt;
}
