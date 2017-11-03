package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

public interface RbacBusinessSystemRelationManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacBusinessSystemRelation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation,
			int currentPage, int pageSize);

	public List<RbacBusinessSystemRelation> queryRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	public RbacBusinessSystemRelation queryRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	public List<RbacBusinessSystemRelation> querySubTreeRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	/**
	 * 保存
	 * 
	 * @param rbacBusinessSystemRelation
	 */
	public void saveRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	/**
	 * 修改
	 * 
	 * @param rbacBusinessSystemRelation
	 */
	public void updateRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

	/**
	 * 删除
	 * 
	 * @param rbacBusinessSystemRelation
	 */
	public void removeRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation);

}
