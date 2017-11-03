package cn.ffcs.uom.report.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TBaobUnitDao;
import cn.ffcs.uom.report.manager.TBaobUnitManager;
import cn.ffcs.uom.report.model.TBaobUnit;

@Service("tBaobUnitManager")
@Scope("prototype")
public class TBaobUnitManagerImpl implements TBaobUnitManager {
	@Resource
	private TBaobUnitDao tBaobUnitDao;

	@Override
	public PageInfo queryTBaobUnitPageInfo(TBaobUnit tBaobUnit,
			int currentPage, int pageSize) {
		return tBaobUnitDao.queryTBaobUnitPageInfo(tBaobUnit, currentPage, pageSize);
	}

	@Override
	public List<Map<String, Object>> queryTBaobUnitList(TBaobUnit tBaobUnit) {
		return tBaobUnitDao.queryTBaobUnitList(tBaobUnit);
	}

}
