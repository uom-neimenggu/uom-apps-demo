package cn.ffcs.uom.dataPermission.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.RoleManager;

@Service("roleManager")
@Scope("prototype")
public class RoleManagerImpl implements RoleManager {

	public PageInfo queryPageInfoByRole(Role role, int currentPage, int pageSize)
			throws SystemException {
		PageInfo pageInfo = new PageInfo();

		if (role == null) {
			// 总页数
			int totalCounts = RoleLocalServiceUtil.getRolesCount();
			int totalPages = totalCounts / pageSize;
			if ((totalCounts % pageSize) > 0) {
				totalPages++;
			}
			int start = (currentPage - 1) * pageSize;
			int end = start + pageSize;

			pageInfo.setTotalCount(totalCounts);
			pageInfo.setTotalPageCount(totalPages);
			pageInfo.setCurrentPage(currentPage);
			pageInfo.setPerPageCount(pageSize);

			pageInfo.setDataList(RoleLocalServiceUtil.getRoles(start, end));
		}

		return pageInfo;
	}
}
