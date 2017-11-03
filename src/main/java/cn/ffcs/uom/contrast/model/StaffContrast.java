package cn.ffcs.uom.contrast.model;

import java.io.Serializable;

public class StaffContrast implements Serializable {
	private Long rowNum;
	private String orgTreeName;
	private String orgName;
	private String orgFullName;
	private String orgUuid;
	private Long orgId;
	private Long orgOssId;
	private Long staffId;
	private String staffUuid;
	private Long staffOssId;
	private String staffName;
	private String staffNewAccount;
	private String staffOaAccount;
	private String staffOssAccount;
	private String staffSeq;
	private Long repeatOrgId;

	public Long getRowNum() {
		return rowNum;
	}

	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	public String getOrgTreeName() {
		return orgTreeName;
	}

	public void setOrgTreeName(String orgTreeName) {
		this.orgTreeName = orgTreeName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgFullName() {
		return orgFullName;
	}

	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}

	public String getOrgUuid() {
		return orgUuid;
	}

	public void setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgOssId() {
		return orgOssId;
	}

	public void setOrgOssId(Long orgOssId) {
		this.orgOssId = orgOssId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffUuid() {
		return staffUuid;
	}

	public void setStaffUuid(String staffUuid) {
		this.staffUuid = staffUuid;
	}

	public Long getStaffOssId() {
		return staffOssId;
	}

	public void setStaffOssId(Long staffOssId) {
		this.staffOssId = staffOssId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffNewAccount() {
		return staffNewAccount;
	}

	public void setStaffNewAccount(String staffNewAccount) {
		this.staffNewAccount = staffNewAccount;
	}

	public String getStaffOaAccount() {
		return staffOaAccount;
	}

	public void setStaffOaAccount(String staffOaAccount) {
		this.staffOaAccount = staffOaAccount;
	}

	public String getStaffOssAccount() {
		return staffOssAccount;
	}

	public void setStaffOssAccount(String staffOssAccount) {
		this.staffOssAccount = staffOssAccount;
	}

	public String getStaffSeq() {
		return staffSeq;
	}

	public void setStaffSeq(String staffSeq) {
		this.staffSeq = staffSeq;
	}

	public Long getRepeatOrgId() {
		return repeatOrgId;
	}

	public void setRepeatOrgId(Long repeatOrgId) {
		this.repeatOrgId = repeatOrgId;
	}

}
