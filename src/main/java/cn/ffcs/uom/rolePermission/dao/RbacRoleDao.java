package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRole;

public interface RbacRoleDao extends BaseDao {

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

}
