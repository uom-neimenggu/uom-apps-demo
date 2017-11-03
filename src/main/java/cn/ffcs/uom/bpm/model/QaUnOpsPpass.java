package cn.ffcs.uom.bpm.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * QaUnOpsPpass entity. @author MyEclipse Persistence Tools
 */

public class QaUnOpsPpass implements java.io.Serializable {
    
    // Fields
    private Long qaUnOpsPpassId;
    private Long execSctIdenti;
    private Long execKidIdenti;
    private String systemCode;
    private String unit;
    private String checkType;
    private Date createDate;
    private String total;
    private String unqualifiledTotal;
    private String perOfPass;
    private BigDecimal score;
    
    // Constructors
    
    /** default constructor */
    public QaUnOpsPpass() {
    }
    
    // Property accessors
    
    public Long getQaUnOpsPpassId() {
        return this.qaUnOpsPpassId;
    }
    
    public void setQaUnOpsPpassId(Long qaUnOpsPpassId) {
        this.qaUnOpsPpassId = qaUnOpsPpassId;
    }
    
    public String getSystemCode() {
        return this.systemCode;
    }
    
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getCheckType() {
        return this.checkType;
    }
    
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getTotal() {
        return this.total;
    }
    
    public void setTotal(String total) {
        this.total = total;
    }
    
    public String getUnqualifiledTotal() {
        return this.unqualifiledTotal;
    }
    
    public void setUnqualifiledTotal(String unqualifiledTotal) {
        this.unqualifiledTotal = unqualifiledTotal;
    }
    
    public String getPerOfPass() {
        return this.perOfPass;
    }
    
    public void setPerOfPass(String perOfPass) {
        this.perOfPass = perOfPass;
    }
    
    public BigDecimal getScore() {
        return this.score;
    }
    
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Long getExecSctIdenti() {
        return execSctIdenti;
    }

    public void setExecSctIdenti(Long execSctIdenti) {
        this.execSctIdenti = execSctIdenti;
    }

	/**
	 * @return the execKidIdenti
	 */
	public Long getExecKidIdenti() {
		return execKidIdenti;
	}

	/**
	 * @param execKidIdenti the execKidIdenti to set
	 */
	public void setExecKidIdenti(Long execKidIdenti) {
		this.execKidIdenti = execKidIdenti;
	}
    
}
