package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

public interface RbacUserRoleDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacUserRole(RbacUserRole rbacUserRole,
			int currentPage, int pageSize);

	public List<RbacUserRole> queryRbacUserRoleList(RbacUserRole rbacUserRole);

	public RbacUserRole queryRbacUserRole(RbacUserRole rbacUserRole);

}
