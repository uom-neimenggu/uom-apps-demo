package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmSupplierCreate;

public interface MdmSupplierCreateDao extends BaseDao {
	/**
	 * 分页查询mdmSupplierCreate
	 * @param mdmSupplierCreate
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByMdmSupplierCreate(
			MdmSupplierCreate mdmSupplierCreate, int currentPage, int pageSize);
}
