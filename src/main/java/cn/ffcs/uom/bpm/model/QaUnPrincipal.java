package cn.ffcs.uom.bpm.model;

/**
 * QaUnPrincipal entity. @author MyEclipse Persistence Tools
 */

public class QaUnPrincipal implements java.io.Serializable {
    
    // Fields
    
    private Long principalId;
    private String principalName;
    private String cellNum;
    private String email;
    private String sort;
    private String tag;
    private String statusCd;
    // Constructors
    
    /** default constructor */
    public QaUnPrincipal() {
    }
    
    public Long getPrincipalId() {
        return this.principalId;
    }
    
    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
    }
    
    public String getPrincipalName() {
        return this.principalName;
    }
    
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
    
    public String getCellNum() {
        return this.cellNum;
    }
    
    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSort() {
        return this.sort;
    }
    
    public void setSort(String sort) {
        this.sort = sort;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
}
