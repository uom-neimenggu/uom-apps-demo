package cn.ffcs.uom.dataPermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.dataPermission.dao.AroleProfessionalTreeDao;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.Organization;

public class AroleProfessionalTree extends UomEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 权限角色组织标识
	 */
	@Getter
	@Setter
	private Long aroleProfessionalTreeId;
	
	/**
	 * 权限角色标识
	 */
	@Getter
	@Setter
	private Long aroleId;
	
	/**
	 * 组织树ID
	 */
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 专业树ID
	 */
	@Getter
	@Setter
	private Long professionalTreeId;
	/**
	 * 组织ID
	 */
	@Getter
	@Setter
	private Long orgId;
	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private String orgRela;
	
	public static AroleProfessionalTreeDao repository() {
		return (AroleProfessionalTreeDao) ApplicationContextUtil
				.getBean("aroleProfessionalTreeDao");
	}

	/**
	 * 获取业务树
	 * 
	 * @return
	 */
	public OrgTree getOrgTree() {
		if (this.orgTreeId != null) {
			String sql = "from OrgTree where statusCd=? and orgTreeId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.orgTreeId);
			List<OrgTree> list = repository().findListByHQLAndParams(sql,params);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
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
	
	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public String getProfessionalTreeName() {
		if (this.professionalTreeId != null) {
			String sql = "from Organization where statusCd=? and orgId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.professionalTreeId);
			List<Organization> list = repository().findListByHQLAndParams(sql,
					params);
			if (list != null && list.size() > 0) {
				return list.get(0).getOrgName();
			}
		}
		return null;
	}
}
