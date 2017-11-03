package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

public interface RbacBusinessSystemRelationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation, int currentPage, int pageSize);

	public List<RbacBusinessSystemRelation> queryRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	public RbacBusinessSystemRelation queryRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	public List<RbacBusinessSystemRelation> querySubTreeRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);



}
