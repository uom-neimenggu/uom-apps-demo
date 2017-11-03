package cn.ffcs.uom.position.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.position.model.OrgPosition;

public interface OrgPositionDao extends BaseDao {
	/**
	 * 查询组织
	 * 
	 * @param orgPosition
	 * @return
	 */
	public OrgPosition jdbcFindOrgPosition(OrgPosition orgPosition) ;
	/**
	 * 新增组织岗位关系返回id
	 * 
	 * @param orgPosition
	 * @return
	 */
	public void addOrganizationPosition(OrgPosition orgPosition);
}
