package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;

public interface RbacRoleTelcomRegionManager {

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

	/**
	 * 保存
	 * 
	 * @param rbacRoleTelcomRegion
	 */
	public void saveRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion);

	/**
	 * 修改
	 * 
	 * @param rbacRoleTelcomRegion
	 */
	public void updateRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion);

	/**
	 * 删除
	 * 
	 * @param rbacRoleTelcomRegion
	 */
	public void removeRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion);

	public void addRbacRoleTelcomRegionList(
			List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList);

	public void updateRbacRoleTelcomRegionList(
			List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList);

	public void updateRbacRoleTelcomRegionToRaptornuke(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) throws PortalException,
			SystemException;

	public void removeRbacRoleTelcomRegionToRaptornuke(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) throws PortalException,
			SystemException;

}
