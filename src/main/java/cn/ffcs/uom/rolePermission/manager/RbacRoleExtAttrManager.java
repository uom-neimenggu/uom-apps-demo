package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

public interface RbacRoleExtAttrManager {

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
