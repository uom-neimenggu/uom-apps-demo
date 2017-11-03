package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

public interface RbacBusinessSystemManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem, int currentPage, int pageSize);

	public List<RbacBusinessSystem> queryRbacBusinessSystemList(
			RbacBusinessSystem rbacBusinessSystem);

	public RbacBusinessSystem queryRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem);

	/**
	 * 保存
	 * 
	 * @param rbacBusinessSystem
	 */
	public void saveRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem);

	/**
	 * 修改
	 * 
	 * @param rbacBusinessSystem
	 */
	public void updateRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem);

	/**
	 * 删除
	 * 
	 * @param rbacBusinessSystem
	 */
	public void removeRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem);

}
