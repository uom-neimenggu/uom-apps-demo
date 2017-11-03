package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOrgOrgs;

public interface OrgOrgsManager {
	/**
	 * queryPageInfoByOrgOrgs
	 * @param orgOrgs
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByOrgOrgs(
			HanaOrgOrgs orgOrgs, int currentPage, int pageSize);
}
