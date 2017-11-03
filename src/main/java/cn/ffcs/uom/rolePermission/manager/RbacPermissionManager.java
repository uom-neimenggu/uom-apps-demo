package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

public interface RbacPermissionManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermission
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermission(RbacPermission rbacPermission,
			int currentPage, int pageSize);

	public List<RbacPermission> queryRbacPermissionList(
			RbacPermission rbacPermission);

	public RbacPermission queryRbacPermission(RbacPermission rbacPermission);

	/**
	 * 保存
	 * 
	 * @param rbacPermission
	 */
	public void saveRbacPermission(RbacPermission rbacPermission);

	/**
	 * 修改
	 * 
	 * @param rbacPermission
	 */
	public void updateRbacPermission(RbacPermission rbacPermission);

	/**
	 * 删除
	 * 
	 * @param rbacPermission
	 */
	public void removeRbacPermission(RbacPermission rbacPermission);

	public void updateRbacPermissionToRaptornuke(RbacPermission rbacPermission,
			RbacPermissionExtAttr rbacPermissionExtAttr)
			throws PortalException, SystemException;

}
