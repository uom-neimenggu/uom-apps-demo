package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *组织扩展属性实体.
 * 
 * @author
 * 
 **/
public class OrganizationExtendAttr extends UomEntity implements Serializable {
	/**
	 *组织扩展属性标识.
	 **/
	public Long getOrgExtendAttrId() {
		return super.getId();
	}

	public void setOrgExtendAttrId(Long orgExtendAttrId) {
		super.setId(orgExtendAttrId);
	}
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 *组织属性规格.
	 **/
	@Getter
	@Setter
	private Long orgAttrSpecId;
	/**
	 *组织属性值.
	 **/
	@Getter
	@Setter
	private String orgAttrValue;

}
