package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;

public interface RbacBusinessSystemResourceDao extends BaseDao {

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


}
