package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;

public interface RbacBusinessSystemResourceManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacBusinessSystemResource
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource, int currentPage,
			int pageSize);

	public List<RbacBusinessSystemResource> queryRbacBusinessSystemResourceList(
			RbacBusinessSystemResource rbacBusinessSystemResource);

	public RbacBusinessSystemResource queryRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource);

	/**
	 * 保存
	 * 
	 * @param rbacBusinessSystemResource
	 */
	public void saveRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource);

	/**
	 * 修改
	 * 
	 * @param rbacBusinessSystemResource
	 */
	public void updateRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource);

	/**
	 * 删除
	 * 
	 * @param rbacBusinessSystemResource
	 */
	public void removeRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource);

	public void addRbacBusinessSystemResourceList(
			List<RbacBusinessSystemResource> rbacBusinessSystemResourceList);

}
