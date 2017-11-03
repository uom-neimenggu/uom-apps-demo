package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmSupplierCreate;

public interface MdmSupplierCreateManager {
	/**
	 * queryPageInfoByMdmSupplierCreate
	 * @param hiPsnorg
	 * @param currentPage
	 * @param pageSize
	 * @return PageInfo
	 */
	public PageInfo queryPageInfoByMdmSupplierCreate(
			MdmSupplierCreate mdmSupplierCreate, int currentPage, int pageSize);
}
