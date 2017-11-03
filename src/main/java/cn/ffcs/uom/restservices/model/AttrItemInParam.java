package cn.ffcs.uom.restservices.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import cn.ffcs.uom.common.model.UomEntity;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class AttrItemInParam extends UomEntity implements Serializable {

	@Setter
	@Getter
	@XmlElement(name = "ATTR_ID")
	private Long attrId;

	@Setter
	@Getter
	@XmlElement(name = "ATTR_VALUE")
	private String attrValue;

	@Setter
	@Getter
	@XmlElement(name = "DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@XmlElement(name = "ACTION")
	private String action;

}
