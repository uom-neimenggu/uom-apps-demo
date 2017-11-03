package cn.ffcs.uom.dataPermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationDao;
import cn.ffcs.uom.organization.model.Organization;

public class AroleOrganization extends UomEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 权限角色组织标识
	 */
	@Getter
	@Setter
	private Long aroleOrganizationId;
	
	/**
	 * 权限角色标识
	 */
	@Getter
	@Setter
	private Long aroleId;
	
	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;
	
	public static AroleOrganizationDao repository() {
		return (AroleOrganizationDao) ApplicationContextUtil
				.getBean("aroleOrganizationDao");
	}

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.orgId != null) {
			String sql = "from Organization where statusCd=? and orgId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.orgId);
			List<Organization> list = repository().findListByHQLAndParams(sql,
					params);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
}
