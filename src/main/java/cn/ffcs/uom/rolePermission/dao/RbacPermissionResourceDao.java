package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;

public interface RbacPermissionResourceDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermissionResource
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource, int currentPage,
			int pageSize);

	public List<RbacPermissionResource> queryRbacPermissionResourceList(
			RbacPermissionResource rbacPermissionResource);

	public RbacPermissionResource queryRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource);

}
