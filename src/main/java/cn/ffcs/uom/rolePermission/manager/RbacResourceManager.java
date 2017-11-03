package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacResource;

public interface RbacResourceManager {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacResource
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacResource(RbacResource rbacResource, int currentPage,
			int pageSize);

	public List<RbacResource> queryRbacResourceList(RbacResource rbacResource);

	public RbacResource queryRbacResource(RbacResource rbacResource);

	/**
	 * 保存
	 * 
	 * @param rbacResource
	 */
	public void saveRbacResource(RbacResource rbacResource);

	/**
	 * 修改
	 * 
	 * @param rbacResource
	 */
	public void updateRbacResource(RbacResource rbacResource);

	/**
	 * 删除
	 * 
	 * @param rbacResource
	 */
	public void removeRbacResource(RbacResource rbacResource);

}
