package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;

public interface RbacRoleOrganizationManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleOrganization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization, int currentPage,
			int pageSize);

	public List<RbacRoleOrganization> queryRbacRoleOrganizationList(
			RbacRoleOrganization rbacRoleOrganization);

	public RbacRoleOrganization queryRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization);

	/**
	 * 保存
	 * 
	 * @param rbacRoleOrganization
	 */
	public void saveRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization);

	/**
	 * 修改
	 * 
	 * @param rbacRoleOrganization
	 */
	public void updateRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization);

	/**
	 * 删除
	 * 
	 * @param rbacRoleOrganization
	 */
	public void removeRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization);

	public void addRbacRoleOrganizationList(
			List<RbacRoleOrganization> rbacRoleOrganizationList);

	public void addRbacRoleOrganizationToRaptornuke(
			RbacRoleOrganization rbacRoleOrganization) throws PortalException,
			SystemException;

	public void removeRbacRoleOrganizationToRaptornuke(
			RbacRoleOrganization rbacRoleOrganization) throws PortalException,
			SystemException;

}
