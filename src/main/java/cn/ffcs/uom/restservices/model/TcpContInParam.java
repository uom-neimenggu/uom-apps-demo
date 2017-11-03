package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class TcpContInParam {

	@Setter
	@Getter
	@XmlElement(name = "TransactionID")
	private String transactionID;

	@Setter
	@Getter
	@XmlElement(name = "ActionCode")
	private String actionCode;

	@Setter
	@Getter
	@XmlElement(name = "BusCode")
	private String busCode;

	@Setter
	@Getter
	@XmlElement(name = "ServiceCode")
	private String serviceCode;

	@Setter
	@Getter
	@XmlElement(name = "ServiceContractVer")
	private String serviceContractVer;

	@Setter
	@Getter
	@XmlElement(name = "ServiceLevel")
	private String serviceLevel;

	@Setter
	@Getter
	@XmlElement(name = "SrcOrgID")
	private String srcOrgID;

	@Setter
	@Getter
	@XmlElement(name = "SrcSysID")
	private String srcSysID;

	@Setter
	@Getter
	@XmlElement(name = "SrcSysSign")
	private String srcSysSign;

	@Setter
	@Getter
	@XmlElement(name = "DstOrgID")
	private String dstOrgID;

	@Setter
	@Getter
	@XmlElement(name = "DstSysID")
	private String dstSysID;

	@Setter
	@Getter
	@XmlElement(name = "ReqTime")
	private String reqTime;

}
