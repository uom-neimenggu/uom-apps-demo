package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;

public interface RbacPermissionRoleDao extends BaseDao {

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

}
