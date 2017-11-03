package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOrgDept;

public interface OrgDeptManager {
	/**
	 * queryPageInfoByOrgDept
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByOrgDept(
			HanaOrgDept orgDept, int currentPage, int pageSize);
}
