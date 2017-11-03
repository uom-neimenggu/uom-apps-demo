package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaWbPsnMsg;

public interface WbPsnMsgManager {
	/**
	 * queryPageInfoByWbPsnMsg
	 * @param wbPsnMsg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByWbPsnMsg(
			HanaWbPsnMsg wbPsnMsg, int currentPage, int pageSize);
}
