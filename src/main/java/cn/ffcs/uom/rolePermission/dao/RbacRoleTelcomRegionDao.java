package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;

public interface RbacRoleTelcomRegionDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleTelcomRegion
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion, int currentPage,
			int pageSize);

	public List<RbacRoleTelcomRegion> queryRbacRoleTelcomRegionList(
			RbacRoleTelcomRegion rbacRoleTelcomRegion);

	public RbacRoleTelcomRegion queryRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion);

}
