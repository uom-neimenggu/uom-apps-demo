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

public class OrganizationInfoExtBean {
	@Setter
	@Getter
	private Panel orgInfoPanel;
	private Textbox orgName;
	private Textbox orgCode;
	// @Setter
	// @Getter
	// private Textbox groupChannelCode;
	@Setter
	@Getter
	private Textbox edaCode;
	private Textbox orgFullName;
	private Textbox orgShortName;
	private Listbox orgType;
	private Listbox existType;
	private Listbox orgLeave;
	private Textbox orgNameEn;
	private Listbox orgScale;
	private Textbox principal;
	private Textbox orgGroupCode;
	private Textbox orgBusinessCode;
	private TelcomRegionTreeBandbox telcomRegionTreeBandbox;
	private PoliticalLocationTreeBandbox politicalLocationTreeBandbox;
	private Textbox orgContent;
	private Textbox phone1;
	private Textbox phone2;
	private Textbox phone3;
	private Textbox phone4;
	private Textbox email1;
	private Textbox email2;
	private Textbox email3;
	private Textbox address;
	private OrganizationExtendAttrExt organizationExtendAttrExt;
	private TreeChooserBandbox orgTypeCd;
	private PartyBandboxExt orgParty;
	private Textbox postCode;
	private Longbox orgPriority;
	@Setter
	@Getter
	private Longbox edaSort;
	private Longbox areaCodeId;
	private Listbox cityTown;
	@Setter
	@Getter
	private Label addressLab;
	
	/**
     * 变更原因.
     */
    @Setter
    @Getter
    private Textbox reason;

	// // 财务个性化属性
	// @Setter
	// @Getter
	// private Groupbox financeIndividualizationGroupbox;
	// // 财务组织编码
	// @Setter
	// @Getter
	// private Textbox financeOrgCode;
	// // 财务组织组编码
	// @Setter
	// @Getter
	// private Textbox financeOrgGroupCode;
	// // 财务组织长名称
	// @Setter
	// @Getter
	// private Textbox financeOrgExtentName;
	// // 财务组织短名称
	// @Setter
	// @Getter
	// private Textbox financeOrgShortName;
	// // 财务组织预留字段1
	// @Setter
	// @Getter
	// private Textbox reserveOne;
	// // 财务组织预留字段2
	// @Setter
	// @Getter
	// private Textbox reserveTwo;

	public Textbox getOrgName() {
		return orgName;
	}

	public void setOrgName(Textbox orgName) {
		this.orgName = orgName;
	}

	public Textbox getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Textbox orgCode) {
		this.orgCode = orgCode;
	}

	public Textbox getOrgFullName() {
		return orgFullName;
	}

	public void setOrgFullName(Textbox orgFullName) {
		this.orgFullName = orgFullName;
	}

	public Textbox getOrgShortName() {
		return orgShortName;
	}

	public void setOrgShortName(Textbox orgShortName) {
		this.orgShortName = orgShortName;
	}

	public Listbox getOrgType() {
		return orgType;
	}

	public void setOrgType(Listbox orgType) {
		this.orgType = orgType;
	}

	public Listbox getExistType() {
		return existType;
	}

	public void setExistType(Listbox existType) {
		this.existType = existType;
	}

	public Listbox getOrgLeave() {
		return orgLeave;
	}

	public void setOrgLeave(Listbox orgLeave) {
		this.orgLeave = orgLeave;
	}

	public Textbox getOrgNameEn() {
		return orgNameEn;
	}

	public void setOrgNameEn(Textbox orgNameEn) {
		this.orgNameEn = orgNameEn;
	}

	public Listbox getOrgScale() {
		return orgScale;
	}

	public void setOrgScale(Listbox orgScale) {
		this.orgScale = orgScale;
	}

	public Textbox getPrincipal() {
		return principal;
	}

	public void setPrincipal(Textbox principal) {
		this.principal = principal;
	}

	public Textbox getOrgGroupCode() {
		return orgGroupCode;
	}

	public void setOrgGroupCode(Textbox orgGroupCode) {
		this.orgGroupCode = orgGroupCode;
	}

	public Textbox getOrgBusinessCode() {
		return orgBusinessCode;
	}

	public void setOrgBusinessCode(Textbox orgBusinessCode) {
		this.orgBusinessCode = orgBusinessCode;
	}

	public TelcomRegionTreeBandbox getTelcomRegionTreeBandbox() {
		return telcomRegionTreeBandbox;
	}

	public void setTelcomRegionTreeBandbox(
			TelcomRegionTreeBandbox telcomRegionTreeBandbox) {
		this.telcomRegionTreeBandbox = telcomRegionTreeBandbox;
	}

	public PoliticalLocationTreeBandbox getPoliticalLocationTreeBandbox() {
		return politicalLocationTreeBandbox;
	}

	public void setPoliticalLocationTreeBandbox(
			PoliticalLocationTreeBandbox politicalLocationTreeBandbox) {
		this.politicalLocationTreeBandbox = politicalLocationTreeBandbox;
	}

	public Textbox getOrgContent() {
		return orgContent;
	}

	public void setOrgContent(Textbox orgContent) {
		this.orgContent = orgContent;
	}

	public Textbox getPhone1() {
		return phone1;
	}

	public void setPhone1(Textbox phone1) {
		this.phone1 = phone1;
	}

	public Textbox getPhone2() {
		return phone2;
	}

	public void setPhone2(Textbox phone2) {
		this.phone2 = phone2;
	}

	public Textbox getEmail1() {
		return email1;
	}

	public void setEmail1(Textbox email1) {
		this.email1 = email1;
	}

	public Textbox getEmail2() {
		return email2;
	}

	public void setEmail2(Textbox email2) {
		this.email2 = email2;
	}

	public Textbox getAddress() {
		return address;
	}

	public void setAddress(Textbox address) {
		this.address = address;
	}

	public OrganizationExtendAttrExt getOrganizationExtendAttrExt() {
		return organizationExtendAttrExt;
	}

	public void setOrganizationExtendAttrExt(
			OrganizationExtendAttrExt organizationExtendAttrExt) {
		this.organizationExtendAttrExt = organizationExtendAttrExt;
	}

	public TreeChooserBandbox getOrgTypeCd() {
		return orgTypeCd;
	}

	public void setOrgTypeCd(TreeChooserBandbox orgTypeCd) {
		this.orgTypeCd = orgTypeCd;
	}

	public PartyBandboxExt getOrgParty() {
		return orgParty;
	}

	public void setOrgParty(PartyBandboxExt orgParty) {
		this.orgParty = orgParty;
	}

	public Textbox getPhone3() {
		return phone3;
	}

	public void setPhone3(Textbox phone3) {
		this.phone3 = phone3;
	}

	public Textbox getPhone4() {
		return phone4;
	}

	public void setPhone4(Textbox phone4) {
		this.phone4 = phone4;
	}

	public Textbox getEmail3() {
		return email3;
	}

	public void setEmail3(Textbox email3) {
		this.email3 = email3;
	}

	public Textbox getPostCode() {
		return postCode;
	}

	public void setPostCode(Textbox postCode) {
		this.postCode = postCode;
	}

	public Longbox getOrgPriority() {
		return orgPriority;
	}

	public void setOrgPriority(Longbox orgPriority) {
		this.orgPriority = orgPriority;
	}

	public Longbox getAreaCodeId() {
		return areaCodeId;
	}

	public void setAreaCodeId(Longbox areaCodeId) {
		this.areaCodeId = areaCodeId;
	}

	public Listbox getCityTown() {
		return cityTown;
	}

	public void setCityTown(Listbox cityTown) {
		this.cityTown = cityTown;
	}

}
