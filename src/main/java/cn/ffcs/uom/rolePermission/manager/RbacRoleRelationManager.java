package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public interface RbacRoleRelationManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleRelation
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

	/**
	 * 保存
	 * 
	 * @param rbacRoleRelation
	 */
	public void saveRbacRoleRelation(RbacRoleRelation rbacRoleRelation);

	/**
	 * 修改
	 * 
	 * @param rbacRoleRelation
	 */
	public void updateRbacRoleRelation(RbacRoleRelation rbacRoleRelation);

	/**
	 * 删除
	 * 
	 * @param rbacRoleRelation
	 */
	public void removeRbacRoleRelation(RbacRoleRelation rbacRoleRelation);

}
