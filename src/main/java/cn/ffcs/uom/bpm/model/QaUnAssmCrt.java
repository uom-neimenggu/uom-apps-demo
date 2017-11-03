package cn.ffcs.uom.bpm.model;

import cn.ffcs.uom.common.model.UomEntity;

/**
 * QaUnAssmCrt entity. @author MyEclipse Persistence Tools
 */

public class QaUnAssmCrt extends UomEntity implements java.io.Serializable {
    
    // Fields
    
    private Long assmCrtId;
    private Long execSctIdenti;
    private String perOfPass;
    private String salt;
    
    // Constructors
    
    /** default constructor */
    public QaUnAssmCrt() {
    }
    
    // Property accessors
    
    public Long getAssmCrtId() {
        return this.assmCrtId;
    }
    
    public void setAssmCrtId(Long assmCrtId) {
        this.assmCrtId = assmCrtId;
    }
    
    public Long getExecSctIdenti() {
        return this.execSctIdenti;
    }
    
    public void setExecSctIdenti(Long execSctIdenti) {
        this.execSctIdenti = execSctIdenti;
    }
    
    public String getPerOfPass() {
        return this.perOfPass;
    }
    
    public void setPerOfPass(String perOfPass) {
        this.perOfPass = perOfPass;
    }
    
    public String getSalt() {
        return this.salt;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
}
