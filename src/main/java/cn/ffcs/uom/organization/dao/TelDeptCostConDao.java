package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaTelDeptCostCon;

public interface TelDeptCostConDao extends BaseDao {
	/**
	 * 分页查询TelDeptCostCon
	 * @param telDeptCostCon
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByTelDeptCostCon(
			HanaTelDeptCostCon telDeptCostCon, int currentPage, int pageSize);
}
