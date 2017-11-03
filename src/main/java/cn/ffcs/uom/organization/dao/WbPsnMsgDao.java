package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaWbPsnMsg;

public interface WbPsnMsgDao extends BaseDao {
	/**
	 * 分页查询wbPsnMsg
	 * @param wbPsnMsg
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByWbPsnMsg(
			HanaWbPsnMsg wbPsnMsg, int currentPage, int pageSize);
}
