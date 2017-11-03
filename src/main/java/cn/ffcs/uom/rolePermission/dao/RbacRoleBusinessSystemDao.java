package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

public interface RbacRoleBusinessSystemDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem, int currentPage,
			int pageSize);

	public List<RbacRoleBusinessSystem> queryRbacRoleBusinessSystemList(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

	public RbacRoleBusinessSystem queryRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem);

}
