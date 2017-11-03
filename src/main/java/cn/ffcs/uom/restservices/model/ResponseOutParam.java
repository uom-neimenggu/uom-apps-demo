package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseOutParam {

	@Setter
	@Getter
	@XmlElement(name = "RspType")
	private String rspType;

	@Setter
	@Getter
	@XmlElement(name = "RspCode")
	private String rspCode;

	@Setter
	@Getter
	@XmlElement(name = "RspDesc")
	private String rspDesc;

}
