package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacResource;

public interface RbacResourceDao extends BaseDao {

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



}
