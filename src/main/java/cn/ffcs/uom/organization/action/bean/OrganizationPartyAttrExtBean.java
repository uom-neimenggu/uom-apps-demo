package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Toolbarbutton;

import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.party.component.PartyInfoExt;
import cn.ffcs.uom.party.component.PartyRoleListboxExt;

public class OrganizationPartyAttrExtBean {
	private Toolbarbutton editButton;
	private Toolbarbutton saveButton;
	private Toolbarbutton recoverButton;
	private PartyInfoExt partyInfoExt;
	private Tabbox tabBox;
	private Tab partyContactInfoTab;
	private Tab partyCertificationTab;
	private Tab partyRoleTab;
	private PartyContactInfoListboxExt partyContactInfoListboxExt;
	private PartyCertificationListboxExt partyCertificationListboxExt;
	private PartyRoleListboxExt partyRoleListboxExt;
	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;

	public Toolbarbutton getEditButton() {
		return editButton;
	}

	public void setEditButton(Toolbarbutton editButton) {
		this.editButton = editButton;
	}

	public Toolbarbutton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Toolbarbutton saveButton) {
		this.saveButton = saveButton;
	}

	public Toolbarbutton getRecoverButton() {
		return recoverButton;
	}

	public void setRecoverButton(Toolbarbutton recoverButton) {
		this.recoverButton = recoverButton;
	}

	public PartyInfoExt getPartyInfoExt() {
		return partyInfoExt;
	}

	public void setPartyInfoExt(PartyInfoExt partyInfoExt) {
		this.partyInfoExt = partyInfoExt;
	}

	public Tabbox getTabBox() {
		return tabBox;
	}

	public void setTabBox(Tabbox tabBox) {
		this.tabBox = tabBox;
	}

	public Tab getPartyContactInfoTab() {
		return partyContactInfoTab;
	}

	public void setPartyContactInfoTab(Tab partyContactInfoTab) {
		this.partyContactInfoTab = partyContactInfoTab;
	}

	public Tab getPartyCertificationTab() {
		return partyCertificationTab;
	}

	public void setPartyCertificationTab(Tab partyCertificationTab) {
		this.partyCertificationTab = partyCertificationTab;
	}

	public Tab getPartyRoleTab() {
		return partyRoleTab;
	}

	public void setPartyRoleTab(Tab partyRoleTab) {
		this.partyRoleTab = partyRoleTab;
	}

	public PartyContactInfoListboxExt getPartyContactInfoListboxExt() {
		return partyContactInfoListboxExt;
	}

	public void setPartyContactInfoListboxExt(
			PartyContactInfoListboxExt partyContactInfoListboxExt) {
		this.partyContactInfoListboxExt = partyContactInfoListboxExt;
	}

	public PartyCertificationListboxExt getPartyCertificationListboxExt() {
		return partyCertificationListboxExt;
	}

	public void setPartyCertificationListboxExt(
			PartyCertificationListboxExt partyCertificationListboxExt) {
		this.partyCertificationListboxExt = partyCertificationListboxExt;
	}

	public PartyRoleListboxExt getPartyRoleListboxExt() {
		return partyRoleListboxExt;
	}

	public void setPartyRoleListboxExt(PartyRoleListboxExt partyRoleListboxExt) {
		this.partyRoleListboxExt = partyRoleListboxExt;
	}

}
