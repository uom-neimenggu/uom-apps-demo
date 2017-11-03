package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class TcpContOutParam {

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
	@XmlElement(name = "RspTime")
	private String rspTime;

	@Setter
	@Getter
	@XmlElement(name = "Response")
	private ResponseOutParam response;

}
