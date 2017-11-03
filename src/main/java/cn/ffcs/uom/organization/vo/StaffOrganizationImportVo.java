package cn.ffcs.uom.organization.vo;

import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年10月10日
 * @功能说明：保存页面进行员工组织关系批量导入的时候单条信息保存
 *
 */
public class StaffOrganizationImportVo {
    
    /**
     * 操作类型
     */
    private String operation;
    
    /**
     * 变更原因
     */
    private String reason;
    
    /**
     * 员工姓名，编码
     */
    private String staffName;
    
    /**
     * 员工编码
     */
    private String staffCode;

    /**
     * 员工账号信息，唯一确定员工
     */
    private String staffAccount;

    /**
     * 组织编码.
     **/
    private String orgCode;
    
    /**
     * 归属组织全称
     */
    private String orgFullName;
    
    /**
     * 归属关联关系
     */
    private String ralaCd;
    
    /**
     * 排序号
     */
    private Long staffSeq;
    
    /**
     * 员工组织关系的员工信息
     */
    private Staff staff;
    
    /**
     * 员工组织关系的组织关系
     */
    private Organization org;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(String staffAccount) {
        this.staffAccount = staffAccount;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getRalaCd() {
        return ralaCd;
    }

    public void setRalaCd(String ralaCd) {
        this.ralaCd = ralaCd;
    }

    public Long getStaffSeq() {
        return staffSeq;
    }

    public void setStaffSeq(Long staffSeq) {
        this.staffSeq = staffSeq;
    }

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
