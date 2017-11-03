package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

public interface RbacResourceRelationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacResource
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation, int currentPage, int pageSize);

	public List<RbacResourceRelation> queryRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation);

	public RbacResourceRelation queryRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation);

	public List<RbacResourceRelation> querySubTreeRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation);



}
