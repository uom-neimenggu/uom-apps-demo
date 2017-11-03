package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

public interface RbacUserRoleBusinessSystemManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacUserRoleBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem,
			int currentPage, int pageSize);

	public List<RbacUserRoleBusinessSystem> queryRbacUserRoleBusinessSystemList(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	public RbacUserRoleBusinessSystem queryRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	/**
	 * 保存
	 * 
	 * @param rbacUserRoleBusinessSystem
	 */
	public void saveRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	/**
	 * 修改
	 * 
	 * @param rbacUserRoleBusinessSystem
	 */
	public void updateRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	/**
	 * 删除
	 * 
	 * @param rbacUserRoleBusinessSystem
	 */
	public void removeRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	public void addRbacUserRoleBusinessSystemList(
			List<RbacUserRoleBusinessSystem> rbacUserRoleBusinessSystemList);

}
