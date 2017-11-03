package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOmPostseries;

public interface OmPostseriesManager {
	/**
	 * queryPageInfoByOmPostseries
	 * @param omPostseries
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByOmPostseries(
			HanaOmPostseries omPostseries, int currentPage, int pageSize);
}
