package cn.ffcs.uom.report.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.model.TRealnameData;

public interface TRealnameDataManager {
	public PageInfo queryTRealnameDataPageInfo(TRealnameData tRealnameData, int currentPage, int pageSize);
	
	public List<Map<String, Object>> queryTRealnameDataList(TRealnameData tRealnameData);
}
