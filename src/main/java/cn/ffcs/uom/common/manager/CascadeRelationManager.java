package cn.ffcs.uom.common.manager;

import java.util.List;

import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;

public interface CascadeRelationManager {

	public CascadeRelation queryCascadeRelation(CascadeRelation cascadeRelation);

	/**
	 * 获取级联关系列表
	 * 
	 * @return
	 */
	public List<CascadeRelation> queryCascadeRelationList(
			CascadeRelation cascadeRelation);

	public CascadeRelation queryCascadeRelationByStaffOrganization(
			CascadeRelation cascadeRelation, StaffOrganization staffOrganization);

	public boolean isPermissions(long roleIds[], String relaCd);
}
