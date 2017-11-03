package cn.ffcs.uom.position.model;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.dao.OrgPositionDao;

/**
 *组织岗位关系实体.
 * 
 * @author
 * 
 **/
public class OrgPosition extends UomEntity implements java.io.Serializable {
	/**
	 * 组织岗位标识
	 */	
	public Long getOrgPositionId() {
		return super.getId();
	}

	public void setOrgPositionId(Long orgPositionId) {
		super.setId(orgPositionId);
	}
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 *岗位标识.
	 **/
	@Getter
	@Setter
	private Long positionId;
	/**
	 *组织树类型.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;

	/**
	 * 创建对象实例.
	 * 
	 * @return OrgPosition
	 */
	public static OrgPosition newInstance() {
		return new OrgPosition();
	}

	/**
	 * 仓库
	 * 
	 * @return
	 */
	public static OrgPositionDao repository() {
		return (OrgPositionDao) ApplicationContextUtil
				.getBean("orgPositionDao");
	}

	public String getOrgName() {
		if (this.getOrgId() != null) {
			Organization organization = (Organization) Organization
					.repository()
					.getObject(Organization.class, this.getOrgId());
			if (organization != null) {
				return organization.getOrgName();
			}
		}
		return "";
	}

	public String getOrgFullName() {
		if (this.getOrgId() != null) {
			Organization organization = (Organization) Organization
					.repository()
					.getObject(Organization.class, this.getOrgId());
			if (organization != null) {
				return organization.getOrgFullName();
			}
		}
		return "";
	}
	
	public String getPositionName() {
		if (this.getPositionId() != null) {
			Position position = (Position) Organization.repository().getObject(
					Position.class, this.getPositionId());
			if (position != null) {
				return position.getPositionName();
			}
		}
		return "";
	}
}
