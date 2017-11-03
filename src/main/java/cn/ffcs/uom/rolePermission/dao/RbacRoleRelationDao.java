package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public interface RbacRoleRelationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation, int currentPage, int pageSize);

	public List<RbacRoleRelation> queryRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation);

	public RbacRoleRelation queryRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation);

	public List<RbacRoleRelation> querySubTreeRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation);

}
