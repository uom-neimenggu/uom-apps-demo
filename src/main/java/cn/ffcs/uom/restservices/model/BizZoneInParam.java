package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BizZoneInParam {

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_NAME")
	private String bizZoneName;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_NBR")
	private String bizZoneNbr;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_LEVER")
	private String bizZoneLever;

	@Setter
	@Getter
	@XmlElement(name = "BIZ_ZONE_TYPE_CD")
	private String bizZoneTypeCd;

	@Setter
	@Getter
	@XmlElement(name = "IS_CORE")
	private String isCore;

	@Setter
	@Getter
	@XmlElement(name = "COMMON_REGION_ID")
	private String commonRegionId;

	@Setter
	@Getter
	@XmlElement(name = "STATUS_CD")
	private String statusCd;

	@Setter
	@Getter
	@XmlElement(name = "STATUS_DATE")
	private String statusDate;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

}
