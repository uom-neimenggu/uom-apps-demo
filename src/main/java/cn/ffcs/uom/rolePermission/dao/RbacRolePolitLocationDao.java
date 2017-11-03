package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;

public interface RbacRolePolitLocationDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRolePolitLocation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation, int currentPage,
			int pageSize);

	public List<RbacRolePolitLocation> queryRbacRolePolitLocationList(
			RbacRolePolitLocation rbacRolePolitLocation);

	public RbacRolePolitLocation queryRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation);

}
