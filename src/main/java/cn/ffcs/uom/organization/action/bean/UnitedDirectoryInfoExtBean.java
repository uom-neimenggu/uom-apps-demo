package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationExtendAttrExt;
import cn.ffcs.uom.party.component.PartyBandboxExt;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class UnitedDirectoryInfoExtBean {
	@Setter
	@Getter
	private Panel unitedDirectoryInfoPanel;

	@Setter
	@Getter
	private Textbox deptname;

	@Setter
	@Getter
	private Textbox ctou;

}
