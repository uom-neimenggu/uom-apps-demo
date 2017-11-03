package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOrgDept;

public interface OrgDeptDao extends BaseDao {
	/**
	 * 分页查询HiPsnorg
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgDept(
			HanaOrgDept orgDept, int currentPage, int pageSize);
}
