package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

public interface RbacRoleExtAttrDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacRoleExtAttr
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacRoleExtAttr(
			RbacRoleExtAttr rbacRoleExtAttr, int currentPage, int pageSize);

	public List<RbacRoleExtAttr> queryRbacRoleExtAttrList(
			RbacRoleExtAttr rbacRoleExtAttr);

	public RbacRoleExtAttr queryRbacRoleExtAttr(RbacRoleExtAttr rbacRoleExtAttr);

}
