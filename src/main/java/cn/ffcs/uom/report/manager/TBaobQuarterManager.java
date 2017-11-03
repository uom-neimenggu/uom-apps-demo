package cn.ffcs.uom.report.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TBaobQuarter;

/**
 * @author zhanglu
 *
 */
public interface TBaobQuarterManager {
	public PageInfo queryTBaobQuarterPageInfo(TBaobQuarter tBaobQuarter, int currentPage, int pageSize);
	
	public List<Map<String, Object>> queryTBaobQuarterList(TBaobQuarter tBaobQuarter);
}
