package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationLevelDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class RbacRoleOrganizationLevel extends UomEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleOrgLevelId() {
		return super.getId();
	}

	public void setRbacRoleOrgLevelId(Long rbacRoleOrgLevelId) {
		super.setId(rbacRoleOrgLevelId);
	}

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private Long rbacOrgId;

	@Getter
	@Setter
	private Long rbacLowerLevel;

	@Getter
	@Setter
	private Long rbacHigherLevel;

	@Getter
	@Setter
	private String relaCd;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String orgCode;

	@Getter
	@Setter
	private String orgName;

	/**
	 * 关系类型
	 * 
	 * @return
	 */
	public String getRelaCdName() {
		if (!StrUtil.isEmpty(this.getRelaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"OrganizationRelation", "relaCd", this.getRelaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.rbacOrgId != null) {
			return (Organization) Organization.repository().getObject(
					Organization.class, this.rbacOrgId);
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleOrganizationLevelDao repository() {
		return (RbacRoleOrganizationLevelDao) ApplicationContextUtil
				.getBean("rbacRoleOrganizationLevelDao");
	}

}