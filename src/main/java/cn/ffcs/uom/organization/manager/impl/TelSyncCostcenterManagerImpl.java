package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.TelSyncCostcenterDao;
import cn.ffcs.uom.organization.manager.TelSyncCostcenterManager;
import cn.ffcs.uom.organization.model.HanaTelSyncCostcenter;

@Service("telSyncCostcenterManager")
@Scope("prototype")
public class TelSyncCostcenterManagerImpl implements TelSyncCostcenterManager {
	@Resource
	private TelSyncCostcenterDao telSyncCostcenterDao;

	@Override
	public PageInfo queryPageInfoByTelSyncCostcenter(
			HanaTelSyncCostcenter telSyncCostcenter, int currentPage, int pageSize) {
		return telSyncCostcenterDao.queryPageInfoByTelSyncCostcenter(
				telSyncCostcenter, currentPage, pageSize);
	}
}
