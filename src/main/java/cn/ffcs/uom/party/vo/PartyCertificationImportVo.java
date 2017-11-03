package cn.ffcs.uom.party.vo;

import cn.ffcs.uom.party.model.PartyCertification;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年4月26日
 * @功能说明：保存页面进行员工证件信息批量导入的时候单条信息保存
 *
 */
public class PartyCertificationImportVo {
    
    /**
     * 操作类型
     */
    private String operation;
    
    /**
     * 变更原因
     */
    private String reason;
    
    /**
     * 员工账号信息
     */
    private String staffAccount;
    
    /**
     * 证件名
     */
    private String certName;
    
    /**
     * 证件号码
     */
    private String certNumber;
    
    /**
     * 发证机关
     */
    private String certOrg;
    
    /**
     * 证件地址
     */
    private String certAddress;
    
    /**
     * 参与人证件信息
     */
    private PartyCertification partyCertification;
    
    /**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the staffAccount
	 */
	public String getStaffAccount() {
		return staffAccount;
	}

	/**
	 * @param staffAccount the staffAccount to set
	 */
	public void setStaffAccount(String staffAccount) {
		this.staffAccount = staffAccount;
	}

	/**
	 * @return the certName
	 */
	public String getCertName() {
		return certName;
	}

	/**
	 * @param certName the certName to set
	 */
	public void setCertName(String certName) {
		this.certName = certName;
	}

	/**
	 * @return the certNumber
	 */
	public String getCertNumber() {
		return certNumber;
	}

	/**
	 * @param certNumber the certNumber to set
	 */
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	/**
	 * @return the certOrg
	 */
	public String getCertOrg() {
		return certOrg;
	}

	/**
	 * @param certOrg the certOrg to set
	 */
	public void setCertOrg(String certOrg) {
		this.certOrg = certOrg;
	}

	/**
	 * @return the certAddress
	 */
	public String getCertAddress() {
		return certAddress;
	}

	/**
	 * @param certAddress the certAddress to set
	 */
	public void setCertAddress(String certAddress) {
		this.certAddress = certAddress;
	}

	/**
	 * @return the partyCertification
	 */
	public PartyCertification getPartyCertification() {
		return partyCertification;
	}

	/**
	 * @param partyCertification the partyCertification to set
	 */
	public void setPartyCertification(PartyCertification partyCertification) {
		this.partyCertification = partyCertification;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
