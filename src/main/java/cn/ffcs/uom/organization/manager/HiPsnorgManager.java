package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.HanaHiPsnorg;

public interface HiPsnorgManager {
	/**
	 * queryPageInfoByHiPsnorg
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByHiPsnorg(
			HanaHiPsnorg hiPsnorg, int currentPage, int pageSize);
}
