package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmProfitcenter;

public interface MdmProfitcenterManager {
	/**
	 * queryPageInfoByMdmProfitcenter
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByMdmProfitcenter(
			MdmProfitcenter mdmProfitcenter, int currentPage, int pageSize);
}
