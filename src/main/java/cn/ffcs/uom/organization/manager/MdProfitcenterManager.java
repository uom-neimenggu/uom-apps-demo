package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdProfitcenter;

public interface MdProfitcenterManager {
	/**
	 * queryPageInfoByMdProfitcenter
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByMdProfitcenter(
			MdProfitcenter mdProfitcenter, int currentPage, int pageSize);
}
