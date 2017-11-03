package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;

public interface RbacRoleOrganizationLevelDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleOrganizationLevel
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

}
