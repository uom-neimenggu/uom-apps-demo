package cn.ffcs.uom.bpm.model;

import java.util.Date;

/**
 * QaInformMethod entity. @author MyEclipse Persistence Tools
 */

public class QaInformMethod implements java.io.Serializable {
    
    // Fields
    
    private static final long serialVersionUID = 1L;
    private Long informMethodId;
    private String informType;
    private String informName;
    private String informTemplate;
    private String statusCd;
    private Date createDate;
    
    // Constructors
    
    /** default constructor */
    public QaInformMethod() {
    }
    
    /** minimal constructor */
    public QaInformMethod(Long informMethodId) {
        this.informMethodId = informMethodId;
    }
    
    /** full constructor */
    public QaInformMethod(Long informMethodId, String informType, String informName,
        String informTemplate, String statusCd, Date createDate) {
        this.informMethodId = informMethodId;
        this.informType = informType;
        this.informName = informName;
        this.informTemplate = informTemplate;
        this.statusCd = statusCd;
        this.createDate = createDate;
    }
    
    // Property accessors
    
    public Long getInformMethodId() {
        return this.informMethodId;
    }
    
    public void setInformMethodId(Long informMethodId) {
        this.informMethodId = informMethodId;
    }
    
    public String getInformType() {
        return this.informType;
    }
    
    public void setInformType(String informType) {
        this.informType = informType;
    }
    
    public String getInformName() {
        return this.informName;
    }
    
    public void setInformName(String informName) {
        this.informName = informName;
    }
    
    public String getInformTemplate() {
        return this.informTemplate;
    }
    
    public void setInformTemplate(String informTemplate) {
        this.informTemplate = informTemplate;
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
