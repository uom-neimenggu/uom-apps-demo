package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaTelSyncCostcenter;

public interface TelSyncCostcenterDao extends BaseDao {
	/**
	 * 分页查询TelSyncCostcenter
	 * @param telSyncCostcenter
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByTelSyncCostcenter(
			HanaTelSyncCostcenter telSyncCostcenter, int currentPage, int pageSize);
}
