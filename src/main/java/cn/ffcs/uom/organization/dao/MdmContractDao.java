package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmContract;

public interface MdmContractDao extends BaseDao {
	/**
	 * 分页查询mdmContract
	 * @param mdmContract
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByMdmContract(
			MdmContract mdmContract, int currentPage, int pageSize);
}
