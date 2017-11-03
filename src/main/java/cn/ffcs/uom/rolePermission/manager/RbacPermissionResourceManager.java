package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;

public interface RbacPermissionResourceManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermissionResource
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource, int currentPage,
			int pageSize);

	public List<RbacPermissionResource> queryRbacPermissionResourceList(
			RbacPermissionResource rbacPermissionResource);

	public RbacPermissionResource queryRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource);

	/**
	 * 保存
	 * 
	 * @param rbacPermissionResource
	 */
	public void saveRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource);

	/**
	 * 修改
	 * 
	 * @param rbacPermissionResource
	 */
	public void updateRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource);

	/**
	 * 删除
	 * 
	 * @param rbacPermissionResource
	 */
	public void removeRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource);

	public void addRbacPermissionResourceList(
			List<RbacPermissionResource> rbacPermissionResourceList);

}
