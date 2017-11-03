package cn.ffcs.uom.report.dao;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TRealnameData;

public interface TRealnameDataDao extends BaseDao {

	public PageInfo queryTRealnameDataPageInfo(TRealnameData tRealnameData, int currentPage, int pageSize);

	public List<Map<String, Object>> queryTRealnameDataList(TRealnameData tRealnameData);
	
}
