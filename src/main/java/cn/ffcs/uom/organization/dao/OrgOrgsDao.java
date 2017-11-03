package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOrgOrgs;

public interface OrgOrgsDao extends BaseDao {
	/**
	 * 分页查询OrgOrgs
	 * @param orgOrgs
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgOrgs(
			HanaOrgOrgs orgOrgs, int currentPage, int pageSize);
}
