package cn.ffcs.uom.bpm.model;

import java.sql.Timestamp;

/**
 * QaExecInformRel entity. @author MyEclipse Persistence Tools
 */

public class QaExecInformRel implements java.io.Serializable {
    
    // Fields
    
    private Double qaExecInformRelId;
    private String execSctIdenti;
    private Double informMethodId;
    private String statusCd;
    private Timestamp createDate;
    
    // Constructors
    
    /** default constructor */
    public QaExecInformRel() {
    }
    
    /** minimal constructor */
    public QaExecInformRel(Double qaExecInformRelId) {
        this.qaExecInformRelId = qaExecInformRelId;
    }
    
    /** full constructor */
    public QaExecInformRel(Double qaExecInformRelId, String execSctIdenti, Double informMethodId,
        String statusCd, Timestamp createDate) {
        this.qaExecInformRelId = qaExecInformRelId;
        this.execSctIdenti = execSctIdenti;
        this.informMethodId = informMethodId;
        this.statusCd = statusCd;
        this.createDate = createDate;
    }
    
    // Property accessors
    
    public Double getQaExecInformRelId() {
        return this.qaExecInformRelId;
    }
    
    public void setQaExecInformRelId(Double qaExecInformRelId) {
        this.qaExecInformRelId = qaExecInformRelId;
    }
    
    public String getExecSctIdenti() {
        return this.execSctIdenti;
    }
    
    public void setExecSctIdenti(String execSctIdenti) {
        this.execSctIdenti = execSctIdenti;
    }
    
    public Double getInformMethodId() {
        return this.informMethodId;
    }
    
    public void setInformMethodId(Double informMethodId) {
        this.informMethodId = informMethodId;
    }
    
    public String getStatusCd() {
        return this.statusCd;
    }
    
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
    
    public Timestamp getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
}
