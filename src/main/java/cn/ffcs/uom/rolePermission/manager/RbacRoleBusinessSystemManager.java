package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

public interface RbacRoleBusinessSystemManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem, int currentPage,
			int pageSize);

	public List<RbacRoleBusinessSystem> queryRbacRoleBusinessSystemList(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	public RbacRoleBusinessSystem queryRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	/**
	 * 保存
	 * 
	 * @param rbacRoleBusinessSystem
	 */
	public void saveRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	/**
	 * 修改
	 * 
	 * @param rbacRoleBusinessSystem
	 */
	public void updateRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	/**
	 * 删除
	 * 
	 * @param rbacRoleBusinessSystem
	 */
	public void removeRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	public void addRbacRoleBusinessSystemList(
			List<RbacRoleBusinessSystem> rbacRoleBusinessSystemList);

}
