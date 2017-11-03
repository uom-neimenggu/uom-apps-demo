package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * QaSysttemInformRel entity. @author MyEclipse Persistence Tools
 */

public class QaSysttemInformRel implements java.io.Serializable {
    
    // Fields
    
    private Long qaExecInformRelId;
    private Long businessSystemId;
    private Long informMethodId;
    private String statusCd;
    private Date createDate;
    
    // Constructors
    
    /** default constructor */
    public QaSysttemInformRel() {
    }
    
    public Long getQaExecInformRelId() {
        return this.qaExecInformRelId;
    }
    
    public void setQaExecInformRelId(Long qaExecInformRelId) {
        this.qaExecInformRelId = qaExecInformRelId;
    }
    
    public Long getBusinessSystemId() {
        return this.businessSystemId;
    }
    
    public void setBusinessSystemId(Long businessSystemId) {
        this.businessSystemId = businessSystemId;
    }
    
    public Long getInformMethodId() {
        return this.informMethodId;
    }
    
    public void setInformMethodId(Long informMethodId) {
        this.informMethodId = informMethodId;
    }
    
    public String getStatusCd() {
        return this.statusCd;
    }
    
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
}
