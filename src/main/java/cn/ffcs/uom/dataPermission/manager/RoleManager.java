package cn.ffcs.uom.dataPermission.manager;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.uom.common.vo.PageInfo;

public interface RoleManager {
	/**
	 * 分页取类信息
	 * 
	 * @param role
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public PageInfo queryPageInfoByRole(Role role,
			int currentPage, int pageSize) throws Exception;
}
