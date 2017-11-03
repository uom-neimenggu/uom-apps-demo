package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * VStaffAuditBillId entity. @author MyEclipse Persistence Tools
 */

public class VStaffAuditBill implements java.io.Serializable {
    
    // Fields
    
    private String monitorId;
    private String monitorName;
    private String names;
    private double codes;
    private String staffId;
    private String staffName;
    private String staffCode;
    private String staffAccount;
    private String staffUuid;
    private String notes;
    private Date updateDate;
    
    
    public String getMonitorId() {
        return this.monitorId;
    }
    
    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }
    
    public String getMonitorName() {
        return this.monitorName;
    }
    
    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }
    
    public String getNames() {
        return this.names;
    }
    
    public void setNames(String names) {
        this.names = names;
    }
    
    public double getCodes() {
        return this.codes;
    }
    
    public void setCodes(double codes) {
        this.codes = codes;
    }
    
    public String getStaffId() {
        return this.staffId;
    }
    
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public String getStaffName() {
        return this.staffName;
    }
    
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    
    public String getStaffCode() {
        return this.staffCode;
    }
    
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    
    public String getStaffAccount() {
        return this.staffAccount;
    }
    
    public void setStaffAccount(String staffAccount) {
        this.staffAccount = staffAccount;
    }
    
    public String getStaffUuid() {
        return this.staffUuid;
    }
    
    public void setStaffUuid(String staffUuid) {
        this.staffUuid = staffUuid;
    }
    
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
}
