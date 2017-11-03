package cn.ffcs.uom.report.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TBaobUnit;

public interface TBaobUnitManager {
	public PageInfo queryTBaobUnitPageInfo(TBaobUnit tBaobUnit, int currentPage, int pageSize);
	
	public List<Map<String, Object>> queryTBaobUnitList(TBaobUnit tBaobUnit);
}
