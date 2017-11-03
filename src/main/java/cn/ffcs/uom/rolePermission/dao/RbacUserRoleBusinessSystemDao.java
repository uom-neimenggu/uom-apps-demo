package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

public interface RbacUserRoleBusinessSystemDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem,
			int currentPage, int pageSize);

	public List<RbacUserRoleBusinessSystem> queryRbacUserRoleBusinessSystemList(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

	public RbacUserRoleBusinessSystem queryRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem);

}
