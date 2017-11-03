package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

public interface RbacPermissionRelationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermission
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation, int currentPage, int pageSize);

	public List<RbacPermissionRelation> queryRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation);

	public RbacPermissionRelation queryRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation);

	public List<RbacPermissionRelation> querySubTreeRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation);


}
