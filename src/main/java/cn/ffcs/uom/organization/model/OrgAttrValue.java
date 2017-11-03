package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.OrgAttrValueDao;

/**
 *组织属性取值实体.
 * 
 * @author
 * 
 **/
public class OrgAttrValue extends UomEntity implements Serializable {
	/**
	 *属性值标识.
	 **/
	public Long getOrgAttrValueId() {
		return super.getId();
	}

	public void setOrgAttrValueId(Long orgAttrValueId) {
		super.setId(orgAttrValueId);
	}
	/**
	 *属性规格标识.
	 **/
	@Getter
	@Setter
	private Long orgAttrSpecId;
	/**
	 *属性值.
	 **/
	@Getter
	@Setter
	private String orgAttrValue;
	/**
	 *属性名称.
	 **/
	@Getter
	@Setter
	private String orgAttrValueName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgAttrValueDao repository() {
		return (OrgAttrValueDao) ApplicationContextUtil
				.getBean("orgAttrValueDao");
	}
}
