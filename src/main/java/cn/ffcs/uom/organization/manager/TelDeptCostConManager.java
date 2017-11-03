package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaTelDeptCostCon;

public interface TelDeptCostConManager {
	/**
	 * queryPageInfoByTelDeptCostCon
	 * @param telDeptCostCon
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByTelDeptCostCon(
			HanaTelDeptCostCon telDeptCostCon, int currentPage, int pageSize);
}
