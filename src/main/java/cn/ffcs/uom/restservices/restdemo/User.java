package cn.ffcs.uom.restservices.restdemo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.restservices.model.AttrItemInParam;
import cn.ffcs.uom.restservices.util.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "USER")
public class User extends AttrItemInParam implements Serializable {

	public Long getUserId() {
		return super.getId();
	}

	public void setUserId(Long userId) {
		super.setId(userId);
	}

	@Setter
	@Getter
	@XmlTransient
	private String nameDesc;

	@Setter
	@Getter
	private String nameDesc1;

	@Setter
	@Getter
	private String name;

	@Setter
	@Getter
	@XmlElement(name = "Age")
	private Long age;

	@Setter
	@Getter
	@XmlElement(name = "SEX_")
	private String sex;

	@Setter
	@Getter
	@XmlElement(name = "TEST_DATE")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date testDate;

	@Setter
	@Getter
	@XmlTransient
	private String attrValue;

	@Setter
	@Getter
	@XmlElement(name = "DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@XmlElementWrapper(name = "ATTR_ITEMS")
	@XmlElement(name = "ATTR_ITEM")
	private List<AttrItemInParam> attrItems;

}