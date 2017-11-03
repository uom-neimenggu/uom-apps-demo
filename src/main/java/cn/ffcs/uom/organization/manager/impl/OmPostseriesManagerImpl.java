package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OmPostseriesDao;
import cn.ffcs.uom.organization.manager.OmPostseriesManager;
import cn.ffcs.uom.organization.model.HanaOmPostseries;

@Service("omPostseriesManager")
@Scope("prototype")
public class OmPostseriesManagerImpl implements OmPostseriesManager {
	@Resource
	private OmPostseriesDao omPostseriesDao;

	@Override
	public PageInfo queryPageInfoByOmPostseries(
			HanaOmPostseries omPostseries, int currentPage, int pageSize) {
		return omPostseriesDao.queryPageInfoByOmPostseries(
				omPostseries, currentPage, pageSize);
	}
}
