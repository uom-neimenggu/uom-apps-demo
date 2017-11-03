package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

public interface RbacUserRoleManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacUserRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacUserRole(RbacUserRole rbacUserRole,
			int currentPage, int pageSize);

	public List<RbacUserRole> queryRbacUserRoleList(RbacUserRole rbacUserRole);

	public RbacUserRole queryRbacUserRole(RbacUserRole rbacUserRole);

	/**
	 * 保存
	 * 
	 * @param rbacUserRole
	 */
	public void saveRbacUserRole(RbacUserRole rbacUserRole);

	/**
	 * 修改
	 * 
	 * @param rbacUserRole
	 */
	public void updateRbacUserRole(RbacUserRole rbacUserRole);

	/**
	 * 删除
	 * 
	 * @param rbacUserRole
	 */
	public void removeRbacUserRole(RbacUserRole rbacUserRole);

	public void addRbacUserRoleList(List<RbacUserRole> rbacUserRoleList);

	public void addRbacUserRoleToRaptornuke(RbacUserRole rbacUserRole)
			throws PortalException, SystemException;

	public void removeRbacUserRoleToRaptornuke(RbacUserRole rbacUserRole)
			throws PortalException, SystemException;

}
