package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * VQaAuditId entity. @author MyEclipse Persistence Tools
 */

public class VQaAudit implements java.io.Serializable {
    
    // Fields
    
    private String monitorId;
    private String osType;
    private String monitorName;
    private String names;
    private String codes;
    private Integer totals;
    private double exceNumber;
    private String percents;
    private Date updateDate;
    private String qualifiedYn;
    private String staQualifiedRate;
    private String types;
    
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
    
    public String getCodes() {
        return this.codes;
    }
    
    public void setCodes(String codes) {
        this.codes = codes;
    }
    
    public Integer getTotals() {
        return this.totals;
    }
    
    public void setTotals(Integer totals) {
        this.totals = totals;
    }
    
    public double getExceNumber() {
        return this.exceNumber;
    }
    
    public void setExceNumber(double exceNumber) {
        this.exceNumber = exceNumber;
    }
    
    public String getPercents() {
        return this.percents;
    }
    
    public void setPercents(String percents) {
        this.percents = percents;
    }
    
    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public String getQualifiedYn() {
        return this.qualifiedYn;
    }
    
    public void setQualifiedYn(String qualifiedYn) {
        this.qualifiedYn = qualifiedYn;
    }
    
    public String getStaQualifiedRate() {
        return this.staQualifiedRate;
    }
    
    public void setStaQualifiedRate(String staQualifiedRate) {
        this.staQualifiedRate = staQualifiedRate;
    }
    
    public String getTypes() {
        return this.types;
    }
    
    public void setTypes(String types) {
        this.types = types;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }
}
