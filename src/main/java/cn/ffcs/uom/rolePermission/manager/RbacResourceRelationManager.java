package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

public interface RbacResourceRelationManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacResourceRelation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation, int currentPage,
			int pageSize);

	public List<RbacResourceRelation> queryRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation);

	public RbacResourceRelation queryRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation);

	public List<RbacResourceRelation> querySubTreeRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation);

	/**
	 * 保存
	 * 
	 * @param rbacResourceRelation
	 */
	public void saveRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation);

	/**
	 * 修改
	 * 
	 * @param rbacResourceRelation
	 */
	public void updateRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation);

	/**
	 * 删除
	 * 
	 * @param rbacResourceRelation
	 */
	public void removeRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation);

}
