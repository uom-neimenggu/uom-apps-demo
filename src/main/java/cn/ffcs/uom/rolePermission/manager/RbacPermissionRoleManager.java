package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;

public interface RbacPermissionRoleManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermissionRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole, int currentPage, int pageSize);

	public List<RbacPermissionRole> queryRbacPermissionRoleList(
			RbacPermissionRole rbacPermissionRole);

	public RbacPermissionRole queryRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole);

	/**
	 * 保存
	 * 
	 * @param rbacPermissionRole
	 */
	public void saveRbacPermissionRole(RbacPermissionRole rbacPermissionRole);

	/**
	 * 修改
	 * 
	 * @param rbacPermissionRole
	 */
	public void updateRbacPermissionRole(RbacPermissionRole rbacPermissionRole);

	/**
	 * 删除
	 * 
	 * @param rbacPermissionRole
	 */
	public void removeRbacPermissionRole(RbacPermissionRole rbacPermissionRole);

	public void addRbacPermissionRoleList(
			List<RbacPermissionRole> rbacPermissionRoleList);

	public void addRbacPermissionRoleToRaptornuke(
			RbacPermissionRole rbacPermissionRole) throws PortalException,
			SystemException;

	public void removeRbacPermissionRoleToRaptornuke(
			RbacPermissionRole rbacPermissionRole) throws PortalException,
			SystemException;

}
