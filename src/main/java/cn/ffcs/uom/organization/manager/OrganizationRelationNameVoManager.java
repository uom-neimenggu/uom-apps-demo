package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.vo.OrganizationRelationNameVo;

public interface OrganizationRelationNameVoManager {
	/**
	 * 获取组织路径Vo
	 * 
	 * @param orgId
	 * @return
	 */
	public List<OrganizationRelationNameVo> getOrganizationTreePathVo(
			OrganizationRelation organizationRelation);

	/**
	 * 获取组织路径Vo
	 * 
	 * @param orgId
	 * @return
	 */
	public List<String> getOrganizationTreePath(List<OrganizationRelation> organizationRelationList);
}
