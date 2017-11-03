package cn.ffcs.uom.restservices.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import cn.ffcs.uom.common.model.UomEntity;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpStaff extends UomEntity implements Serializable {

	/**
	 * 员工标识
	 */
	@XmlTransient
	public Long getGrpStaffId() {
		return super.getId();
	}

	public void setGrpStaffId(Long grpStaffId) {
		super.setId(grpStaffId);
	}

	@Setter
	@Getter
	@XmlElement(name = "SALES_CODE")
	private String salesCode;

	@Setter
	@Getter
	@XmlElement(name = "STAFF_CODE")
	private String staffCode;

	@Setter
	@Getter
	@XmlElement(name = "ORG_ID")
	private Long orgId;

	@Setter
	@Getter
	@XmlElement(name = "STAFF_NAME")
	private String staffName;

	@Setter
	@Getter
	@XmlElement(name = "CERT_TYPE")
	private String certType;

	@Setter
	@Getter
	@XmlElement(name = "CERT_NUMBER")
	private String certNumber;

	@Setter
	@Getter
	@XmlElement(name = "MOBILE_PHONE")
	private String mobilePhone;

	@Setter
	@Getter
	@XmlElement(name = "E_MAIL")
	private String email;

	@Setter
	@Getter
	@XmlElement(name = "COMMON_REGION_ID")
	private String commonRegionId;

	@Setter
	@Getter
	@XmlElement(name = "STAFF_DESC")
	private String staffDesc;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

}
