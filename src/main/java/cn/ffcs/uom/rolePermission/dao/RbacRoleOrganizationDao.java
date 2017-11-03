package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;

public interface RbacRoleOrganizationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization, int currentPage, int pageSize);

	public List<RbacRoleOrganization> queryRbacRoleOrganizationList(
			RbacRoleOrganization rbacRoleOrganization);

	public RbacRoleOrganization queryRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization);

}
