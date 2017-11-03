package cn.ffcs.uom.report.dao;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TBaobQuarter;

public interface TBaobQuarterDao extends BaseDao {

	public PageInfo queryTBaobQuarterPageInfo(TBaobQuarter tBaobQuarter, int currentPage, int pageSize);

	public List<Map<String, Object>> queryTBaobQuarterList(
			TBaobQuarter tBaobQuarter);
	
}
