package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;

public interface RbacRolePolitLocationManager {

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

	/**
	 * 保存
	 * 
	 * @param rbacRolePolitLocation
	 */
	public void saveRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation);

	/**
	 * 修改
	 * 
	 * @param rbacRolePolitLocation
	 */
	public void updateRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation);

	/**
	 * 删除
	 * 
	 * @param rbacRolePolitLocation
	 */
	public void removeRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation);

	public void addRbacRolePolitLocationList(
			List<RbacRolePolitLocation> rbacRolePolitLocationList);

	public void updateRbacRolePolitLocationList(
			List<RbacRolePolitLocation> rbacRolePolitLocationList);

}
