package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdCostcenter;

public interface MdCostcenterManager {
	/**
	 * queryPageInfoByMdCostcenter
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByMdCostcenter(
			MdCostcenter mdCostcenter, int currentPage, int pageSize);
}
