package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmContract;

public interface MdmContractManager {
	/**
	 * queryPageInfoByMdmContract
	 * @param mdmContract
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByMdmContract(
			MdmContract mdmContract, int currentPage, int pageSize);
}
