package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRole;

public interface RbacRoleManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRole(RbacRole rbacRole, int currentPage,
			int pageSize);

	public List<RbacRole> queryRbacRoleList(RbacRole rbacRole);

	public RbacRole queryRbacRole(RbacRole rbacRole);

	/**
	 * 保存
	 * 
	 * @param rbacRole
	 */
	public void saveRbacRole(RbacRole rbacRole);

	/**
	 * 修改
	 * 
	 * @param rbacRole
	 */
	public void updateRbacRole(RbacRole rbacRole);

	/**
	 * 删除
	 * 
	 * @param rbacRole
	 */
	public void removeRbacRole(RbacRole rbacRole);

	public void updateRbacRoleToRaptornuke(RbacRole rbacRole)
			throws PortalException, SystemException;

}
