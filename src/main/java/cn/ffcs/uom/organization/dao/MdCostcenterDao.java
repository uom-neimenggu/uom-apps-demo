package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.MdCostcenter;

public interface MdCostcenterDao extends BaseDao {
	/**
	 * 分页查询mdCostcenter
	 * @param mdCostcenter
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByMdCostcenter(
			MdCostcenter mdCostcenter, int currentPage, int pageSize);
}
