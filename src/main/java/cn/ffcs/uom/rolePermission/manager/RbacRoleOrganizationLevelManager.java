package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;

public interface RbacRoleOrganizationLevelManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleOrganization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel,
			int currentPage, int pageSize);

	public List<RbacRoleOrganizationLevel> queryRbacRoleOrganizationLevelList(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel);

	public RbacRoleOrganizationLevel queryRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel);

	/**
	 * 保存
	 * 
	 * @param rbacRoleOrganization
	 */
	public void saveRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel);

	/**
	 * 修改
	 * 
	 * @param rbacRoleOrganization
	 */
	public void updateRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel);

	/**
	 * 删除
	 * 
	 * @param rbacRoleOrganization
	 */
	public void removeRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel);

	public void addRbacRoleOrganizationLevelList(
			List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList);

	public void updateRbacRoleOrganizationLevelList(
			List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList);

	public void updateRbacRoleOrganizationLevelToRaptornuke(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel)
			throws PortalException, SystemException;

	public void removeRbacRoleOrganizationLevelToRaptornuke(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel)
			throws PortalException, SystemException;

}
