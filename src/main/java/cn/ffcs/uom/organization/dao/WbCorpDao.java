package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaWbCorp;

public interface WbCorpDao extends BaseDao {
	/**
	 * 分页查询WbCorp
	 * @param wbCorp
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByWbCorp(
			HanaWbCorp wbCorp, int currentPage, int pageSize);
}
