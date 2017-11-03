package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * VOrgAuditBillId entity. @author MyEclipse Persistence Tools
 */

public class VOrgAuditBill implements java.io.Serializable {
    
    // Fields
    
    private String monitorId;
    private String monitorName;
    private String names;
    private double codes;
    private String orgId;
    private String orgName;
    private String orgFullName;
    private String orgCode;
    private String orgUuid;
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
    
    public String getOrgId() {
        return this.orgId;
    }
    
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    public String getOrgFullName() {
        return this.orgFullName;
    }
    
    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }
    
    public String getOrgCode() {
        return this.orgCode;
    }
    
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
    public String getOrgUuid() {
        return this.orgUuid;
    }
    
    public void setOrgUuid(String orgUuid) {
        this.orgUuid = orgUuid;
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
