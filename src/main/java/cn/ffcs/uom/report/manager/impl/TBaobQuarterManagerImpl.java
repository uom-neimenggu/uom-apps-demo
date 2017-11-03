package cn.ffcs.uom.report.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TBaobQuarterDao;
import cn.ffcs.uom.report.manager.TBaobQuarterManager;
import cn.ffcs.uom.report.model.TBaobQuarter;

@Service("tBaobQuarterManager")
@Scope("prototype")
public class TBaobQuarterManagerImpl implements TBaobQuarterManager {
	@Resource
	private TBaobQuarterDao tBaobQuarterDao;
	
	@Override
	public PageInfo queryTBaobQuarterPageInfo(TBaobQuarter tBaobQuarter,
			int currentPage, int pageSize) {
		return tBaobQuarterDao.queryTBaobQuarterPageInfo(tBaobQuarter, currentPage, pageSize);
	}

	@Override
	public List<Map<String, Object>> queryTBaobQuarterList(
			TBaobQuarter tBaobQuarter) {
		return tBaobQuarterDao.queryTBaobQuarterList(tBaobQuarter);
	}

}
