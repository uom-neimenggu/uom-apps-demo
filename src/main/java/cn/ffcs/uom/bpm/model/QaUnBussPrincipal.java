package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * QaUnBussPrincipal entity. @author MyEclipse Persistence Tools
 */

public class QaUnBussPrincipal implements java.io.Serializable {
    
    // Fields
    
    private Long bussPrincipalId;
    private String systemCode;
    private String status;
    private Date createDate;
    
    // Constructors
    
    /** default constructor */
    public QaUnBussPrincipal() {
    }
   

    
    public Long getBussPrincipalId() {
        return this.bussPrincipalId;
    }
    
    public void setBussPrincipalId(Long bussPrincipalId) {
        this.bussPrincipalId = bussPrincipalId;
    }
    
    public String getSystemCode() {
        return this.systemCode;
    }
    
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
}
