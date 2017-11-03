package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.OrganizationTreeRelationDao;

/**
 *组织树引用关系表实体.
 * 
 * @author
 * 
 **/
public class OrganizationTreeRelation extends UomEntity implements Serializable {
	/**
	 *关联树关系标识.
	 **/
	public Long getOrganizationTreeRelationId() {
		return super.getId();
	}

	public void setOrganizationTreeRelationId(Long organizationTreeRelationId) {
		super.setId(organizationTreeRelationId);
	}
	/**
	 *关联关系标识.
	 **/
	@Getter
	@Setter
	private Long orgRelId;
	/**
	 *组织树类型.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 *组织排序.
	 **/
	@Getter
	@Setter
	private Long orgSeq;

	/**
	 * 构造方法
	 */
	public OrganizationTreeRelation() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationTreeRelation
	 */
	public static OrganizationTreeRelation newInstance() {
		return new OrganizationTreeRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrganizationTreeRelationDao repository() {
		return (OrganizationTreeRelationDao) ApplicationContextUtil
				.getBean("organizationTreeRelationDao");
	}
}
