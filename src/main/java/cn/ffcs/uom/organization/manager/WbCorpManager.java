package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaWbCorp;

public interface WbCorpManager {
	/**
	 * queryPageInfoByWbCorp
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByWbCorp(
			HanaWbCorp wbCorp, int currentPage, int pageSize);
}
