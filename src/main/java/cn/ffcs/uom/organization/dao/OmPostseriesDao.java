package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaOmPostseries;

public interface OmPostseriesDao extends BaseDao {
	/**
	 * 分页查询OmPostseries
	 * @param omPostseries
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOmPostseries(
			HanaOmPostseries omPostseries, int currentPage, int pageSize);
}
