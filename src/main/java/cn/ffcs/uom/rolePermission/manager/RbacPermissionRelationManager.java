package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

public interface RbacPermissionRelationManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermissionRelation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation, int currentPage,
			int pageSize);

	public List<RbacPermissionRelation> queryRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation);

	public RbacPermissionRelation queryRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation);

	public List<RbacPermissionRelation> querySubTreeRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation);

	/**
	 * 保存
	 * 
	 * @param rbacPermissionRelation
	 */
	public void saveRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation);

	/**
	 * 修改
	 * 
	 * @param rbacPermissionRelation
	 */
	public void updateRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation);

	/**
	 * 删除
	 * 
	 * @param rbacPermissionRelation
	 */
	public void removeRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation);

}
