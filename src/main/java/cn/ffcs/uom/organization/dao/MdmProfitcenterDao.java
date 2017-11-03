package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdmProfitcenter;

public interface MdmProfitcenterDao extends BaseDao {
	/**
	 * 分页查询mdmProfitcenter
	 * @param mdmProfitcenter
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByMdmProfitcenter(
			MdmProfitcenter mdmProfitcenter, int currentPage, int pageSize);
}
