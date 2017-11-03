package cn.ffcs.uom.restservices.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BusiStoreAttrInParam {

	@Setter
	@Getter
	@XmlElement(name = "BUSI_STORE_NBR")
	private String busiStoreNbr;

	@Setter
	@Getter
	@XmlElementWrapper(name = "ATTR_ITEMS")
	@XmlElement(name = "ATTR_ITEM")
	private List<AttrItemInParam> attrItems;

}
