package cn.ffcs.uom.gridUnit.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;

public interface GridUnitRefManager {
	/**
	 * 分页取类信息
	 * 
	 * @param gridUnitRef
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGridUnitRef(GridUnitRef gridUnitRef,
			int currentPage, int pageSize);

	public List<GridUnitRef> queryGridUnitRefList(GridUnitRef gridUnitRef);

}
