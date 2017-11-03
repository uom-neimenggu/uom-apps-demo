package cn.ffcs.uom.common.dao;

import java.util.List;

import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;

public interface CascadeRelationDao extends BaseDao {

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

}
