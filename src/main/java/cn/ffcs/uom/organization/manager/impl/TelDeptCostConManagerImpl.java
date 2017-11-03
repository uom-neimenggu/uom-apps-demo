package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.TelDeptCostConDao;
import cn.ffcs.uom.organization.manager.TelDeptCostConManager;
import cn.ffcs.uom.organization.model.HanaTelDeptCostCon;

@Service("telDeptCostConManager")
@Scope("prototype")
public class TelDeptCostConManagerImpl implements TelDeptCostConManager {
	@Resource
	private TelDeptCostConDao telDeptCostConDao;

	@Override
	public PageInfo queryPageInfoByTelDeptCostCon(
			HanaTelDeptCostCon telDeptCostCon, int currentPage, int pageSize) {
		return telDeptCostConDao.queryPageInfoByTelDeptCostCon(
				telDeptCostCon, currentPage, pageSize);
	}
}
