package cn.ffcs.uom.party.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.staff.component.StaffExtendAttrExt;

public class PartyInfoExtBean {
	private Groupbox partyAttrGroupbox;
	private Textbox partyName;
	private Textbox partyNameFirst;
	private Textbox partyAbbrname;
	private Textbox englishName;
	private Listbox partyType;
	private TreeChooserBandbox roleType;
	private Row isAddStaffRow;
	private Label addStaffInfoLab;
	private Listbox addStaffInfo;
	private Groupbox staffAttrGroupbox;
	private PoliticalLocationTreeBandbox locationId;
	private Textbox staffName;
	private Textbox staffCode;
	private Textbox staffDesc;
	private TreeChooserBandbox workProp;
	private Listbox partTime;
	private Listbox staffPosition;
	private Textbox titleNote;
	private Textbox staffAcct;
	private Textbox staffPassword;
	private StaffExtendAttrExt staffExtendAttrExt;
	private Groupbox partyCertGroupbox;
	private Listbox certType;
	private Textbox certOrg;
	private Textbox certAddress;
	private Textbox certNumber;
	private Listbox certSort;
	private Listbox identityCardId;
	private Groupbox partyContactGroupbox;
	private Listbox headFlag;
	private TreeChooserBandbox contactType;
	private Textbox contactName;
	private Listbox contactGender;
	private Textbox contactAddress;
	private Textbox contactEmployer;
	private Textbox homePhone;
	private Textbox officePhone;
	private Textbox mobilePhone;
	private Textbox innerEmail;
	private Textbox mobilePhoneSpare;
	private Textbox email;
	private Textbox postCode;
	private Textbox postAddress;
	private Textbox fax;
	private Textbox qqNumber;
	private Textbox contactDesc;
	private Groupbox individualGroupbox;
	private Datebox birthday;
	private TreeChooserBandbox marriageStatus;
	private Listbox politicsStatus;
	private Listbox educationLevel;
	private Listbox gender;
	private TreeChooserBandbox nationality;
	private Listbox nation;
	private Textbox nativePlace;
	private Textbox employer;
	private Listbox religion;
	private Textbox sameNameCode;
	private Groupbox partyOrgGroupbox;
	private Listbox orgType;
	private Textbox orgContent;
	private Listbox orgScale;
	private Textbox principal;
	@Getter
	@Setter
	private Label roleTypeLab;

	public Groupbox getPartyAttrGroupbox() {
		return partyAttrGroupbox;
	}

	public void setPartyAttrGroupbox(Groupbox partyAttrGroupbox) {
		this.partyAttrGroupbox = partyAttrGroupbox;
	}

	public Textbox getPartyName() {
		return partyName;
	}

	public void setPartyName(Textbox partyName) {
		this.partyName = partyName;
	}

	public Textbox getPartyNameFirst() {
		return partyNameFirst;
	}

	public void setPartyNameFirst(Textbox partyNameFirst) {
		this.partyNameFirst = partyNameFirst;
	}

	public Textbox getPartyAbbrname() {
		return partyAbbrname;
	}

	public void setPartyAbbrname(Textbox partyAbbrname) {
		this.partyAbbrname = partyAbbrname;
	}

	public Textbox getEnglishName() {
		return englishName;
	}

	public void setEnglishName(Textbox englishName) {
		this.englishName = englishName;
	}

	public Listbox getPartyType() {
		return partyType;
	}

	public void setPartyType(Listbox partyType) {
		this.partyType = partyType;
	}

	public TreeChooserBandbox getRoleType() {
		return roleType;
	}

	public void setRoleType(TreeChooserBandbox roleType) {
		this.roleType = roleType;
	}

	public Label getAddStaffInfoLab() {
		return addStaffInfoLab;
	}

	public void setAddStaffInfoLab(Label addStaffInfoLab) {
		this.addStaffInfoLab = addStaffInfoLab;
	}

	public Listbox getAddStaffInfo() {
		return addStaffInfo;
	}

	public void setAddStaffInfo(Listbox addStaffInfo) {
		this.addStaffInfo = addStaffInfo;
	}

	public Groupbox getStaffAttrGroupbox() {
		return staffAttrGroupbox;
	}

	public void setStaffAttrGroupbox(Groupbox staffAttrGroupbox) {
		this.staffAttrGroupbox = staffAttrGroupbox;
	}

	public PoliticalLocationTreeBandbox getLocationId() {
		return locationId;
	}

	public void setLocationId(PoliticalLocationTreeBandbox locationId) {
		this.locationId = locationId;
	}

	public Textbox getStaffName() {
		return staffName;
	}

	public void setStaffName(Textbox staffName) {
		this.staffName = staffName;
	}

	public Textbox getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(Textbox staffCode) {
		this.staffCode = staffCode;
	}

	public Textbox getStaffDesc() {
		return staffDesc;
	}

	public void setStaffDesc(Textbox staffDesc) {
		this.staffDesc = staffDesc;
	}

	public TreeChooserBandbox getWorkProp() {
		return workProp;
	}

	public void setWorkProp(TreeChooserBandbox workProp) {
		this.workProp = workProp;
	}

	public Listbox getPartTime() {
		return partTime;
	}

	public void setPartTime(Listbox partTime) {
		this.partTime = partTime;
	}

	public Listbox getStaffPosition() {
		return staffPosition;
	}

	public void setStaffPosition(Listbox staffPosition) {
		this.staffPosition = staffPosition;
	}

	public Textbox getTitleNote() {
		return titleNote;
	}

	public void setTitleNote(Textbox titleNote) {
		this.titleNote = titleNote;
	}

	public Textbox getStaffAcct() {
		return staffAcct;
	}

	public void setStaffAcct(Textbox staffAcct) {
		this.staffAcct = staffAcct;
	}

	public Textbox getStaffPassword() {
		return staffPassword;
	}

	public void setStaffPassword(Textbox staffPassword) {
		this.staffPassword = staffPassword;
	}

	public StaffExtendAttrExt getStaffExtendAttrExt() {
		return staffExtendAttrExt;
	}

	public void setStaffExtendAttrExt(StaffExtendAttrExt staffExtendAttrExt) {
		this.staffExtendAttrExt = staffExtendAttrExt;
	}

	public Groupbox getPartyCertGroupbox() {
		return partyCertGroupbox;
	}

	public void setPartyCertGroupbox(Groupbox partyCertGroupbox) {
		this.partyCertGroupbox = partyCertGroupbox;
	}

	public Listbox getCertType() {
		return certType;
	}

	public void setCertType(Listbox certType) {
		this.certType = certType;
	}

	public Textbox getCertOrg() {
		return certOrg;
	}

	public void setCertOrg(Textbox certOrg) {
		this.certOrg = certOrg;
	}

	public Textbox getCertAddress() {
		return certAddress;
	}

	public void setCertAddress(Textbox certAddress) {
		this.certAddress = certAddress;
	}

	public Textbox getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(Textbox certNumber) {
		this.certNumber = certNumber;
	}

	public Listbox getCertSort() {
		return certSort;
	}

	public void setCertSort(Listbox certSort) {
		this.certSort = certSort;
	}

	public Listbox getIdentityCardId() {
		return identityCardId;
	}

	public void setIdentityCardId(Listbox identityCardId) {
		this.identityCardId = identityCardId;
	}

	public Groupbox getPartyContactGroupbox() {
		return partyContactGroupbox;
	}

	public void setPartyContactGroupbox(Groupbox partyContactGroupbox) {
		this.partyContactGroupbox = partyContactGroupbox;
	}

	public Listbox getHeadFlag() {
		return headFlag;
	}

	public void setHeadFlag(Listbox headFlag) {
		this.headFlag = headFlag;
	}

	public TreeChooserBandbox getContactType() {
		return contactType;
	}

	public void setContactType(TreeChooserBandbox contactType) {
		this.contactType = contactType;
	}

	public Textbox getContactName() {
		return contactName;
	}

	public void setContactName(Textbox contactName) {
		this.contactName = contactName;
	}

	public Listbox getContactGender() {
		return contactGender;
	}

	public void setContactGender(Listbox contactGender) {
		this.contactGender = contactGender;
	}

	public Textbox getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(Textbox contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Textbox getContactEmployer() {
		return contactEmployer;
	}

	public void setContactEmployer(Textbox contactEmployer) {
		this.contactEmployer = contactEmployer;
	}

	public Textbox getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(Textbox homePhone) {
		this.homePhone = homePhone;
	}

	public Textbox getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(Textbox officePhone) {
		this.officePhone = officePhone;
	}

	public Textbox getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(Textbox mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Textbox getInnerEmail() {
		return innerEmail;
	}

	public void setInnerEmail(Textbox innerEmail) {
		this.innerEmail = innerEmail;
	}

	public Textbox getMobilePhoneSpare() {
		return mobilePhoneSpare;
	}

	public void setMobilePhoneSpare(Textbox mobilePhoneSpare) {
		this.mobilePhoneSpare = mobilePhoneSpare;
	}

	public Row getIsAddStaffRow() {
		return isAddStaffRow;
	}

	public void setIsAddStaffRow(Row isAddStaffRow) {
		this.isAddStaffRow = isAddStaffRow;
	}

	public Textbox getEmail() {
		return email;
	}

	public void setEmail(Textbox email) {
		this.email = email;
	}

	public Textbox getPostCode() {
		return postCode;
	}

	public void setPostCode(Textbox postCode) {
		this.postCode = postCode;
	}

	public Textbox getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(Textbox postAddress) {
		this.postAddress = postAddress;
	}

	public Textbox getFax() {
		return fax;
	}

	public void setFax(Textbox fax) {
		this.fax = fax;
	}

	public Textbox getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(Textbox qqNumber) {
		this.qqNumber = qqNumber;
	}

	public Textbox getContactDesc() {
		return contactDesc;
	}

	public void setContactDesc(Textbox contactDesc) {
		this.contactDesc = contactDesc;
	}

	public Groupbox getIndividualGroupbox() {
		return individualGroupbox;
	}

	public void setIndividualGroupbox(Groupbox individualGroupbox) {
		this.individualGroupbox = individualGroupbox;
	}

	public Datebox getBirthday() {
		return birthday;
	}

	public void setBirthday(Datebox birthday) {
		this.birthday = birthday;
	}

	public TreeChooserBandbox getMarriageStatus() {
		return marriageStatus;
	}

	public void setMarriageStatus(TreeChooserBandbox marriageStatus) {
		this.marriageStatus = marriageStatus;
	}

	public Listbox getPoliticsStatus() {
		return politicsStatus;
	}

	public void setPoliticsStatus(Listbox politicsStatus) {
		this.politicsStatus = politicsStatus;
	}

	public Listbox getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(Listbox educationLevel) {
		this.educationLevel = educationLevel;
	}

	public Listbox getGender() {
		return gender;
	}

	public void setGender(Listbox gender) {
		this.gender = gender;
	}

	public TreeChooserBandbox getNationality() {
		return nationality;
	}

	public void setNationality(TreeChooserBandbox nationality) {
		this.nationality = nationality;
	}

	public Listbox getNation() {
		return nation;
	}

	public void setNation(Listbox nation) {
		this.nation = nation;
	}

	public Textbox getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(Textbox nativePlace) {
		this.nativePlace = nativePlace;
	}

	public Textbox getEmployer() {
		return employer;
	}

	public void setEmployer(Textbox employer) {
		this.employer = employer;
	}

	public Listbox getReligion() {
		return religion;
	}

	public void setReligion(Listbox religion) {
		this.religion = religion;
	}

	public Textbox getSameNameCode() {
		return sameNameCode;
	}

	public void setSameNameCode(Textbox sameNameCode) {
		this.sameNameCode = sameNameCode;
	}

	public Groupbox getPartyOrgGroupbox() {
		return partyOrgGroupbox;
	}

	public void setPartyOrgGroupbox(Groupbox partyOrgGroupbox) {
		this.partyOrgGroupbox = partyOrgGroupbox;
	}

	public Listbox getOrgType() {
		return orgType;
	}

	public void setOrgType(Listbox orgType) {
		this.orgType = orgType;
	}

	public Textbox getOrgContent() {
		return orgContent;
	}

	public void setOrgContent(Textbox orgContent) {
		this.orgContent = orgContent;
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

}
