package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class StaffBizZoneRelaInParam {

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_NBR")
	private String bizZoneNbr;

	@Setter
	@Getter
	@XmlElement(name = "SALES_CODE")
	private String salesCode;
	@Setter
	@Getter
	@XmlElement(name = "RELA_TYPE")
	private String relaType;

	@Setter
	@Getter
	@XmlElement(name = "DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

}
