package cn.ffcs.uom.report.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TRealnameDataDao;
import cn.ffcs.uom.report.manager.TRealnameDataManager;
import cn.ffcs.uom.report.model.TRealnameData;

@Service("tRealnameDataManager")
@Scope("prototype")
public class TRealnameDataManagerImpl implements TRealnameDataManager {
	@Resource
	private TRealnameDataDao tRealnameDataDao;

	@Override
	public PageInfo queryTRealnameDataPageInfo(TRealnameData tRealnameData,
			int currentPage, int pageSize) {
		return tRealnameDataDao.queryTRealnameDataPageInfo(tRealnameData, currentPage, pageSize);
	}

	@Override
	public List<Map<String, Object>> queryTRealnameDataList(TRealnameData tRealnameData) {
		return tRealnameDataDao.queryTRealnameDataList(tRealnameData);
	}

}
