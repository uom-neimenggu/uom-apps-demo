package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaTelSyncCostcenter;

public interface TelSyncCostcenterManager {
	/**
	 * queryPageInfoByTelSyncCostcenter
	 * @param telDeptCostCon
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByTelSyncCostcenter(
			HanaTelSyncCostcenter telSyncCostcenter, int currentPage, int pageSize);
}
