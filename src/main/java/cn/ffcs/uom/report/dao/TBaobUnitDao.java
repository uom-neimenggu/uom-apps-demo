package cn.ffcs.uom.report.dao;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TBaobUnit;

public interface TBaobUnitDao extends BaseDao {

	public PageInfo queryTBaobUnitPageInfo(TBaobUnit tBaobUnit, int currentPage, int pageSize);

	public List<Map<String, Object>> queryTBaobUnitList(TBaobUnit tBaobUnit);
	
}
