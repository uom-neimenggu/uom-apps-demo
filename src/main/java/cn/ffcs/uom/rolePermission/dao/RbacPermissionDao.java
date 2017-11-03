package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacPermission;

public interface RbacPermissionDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacPermission
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacPermission(RbacPermission rbacPermission, int currentPage,
			int pageSize);

	public List<RbacPermission> queryRbacPermissionList(RbacPermission rbacPermission);

	public RbacPermission queryRbacPermission(RbacPermission rbacPermission);


}
