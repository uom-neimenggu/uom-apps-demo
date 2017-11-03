package cn.ffcs.uom.gridUnit.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public interface GridUnitManager {
	/**
	 * 分页取类信息
	 * 
	 * @param gridUnit
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGridUnit(GridUnit gridUnit, int currentPage,
			int pageSize);

	public List<GridUnit> queryGridUnitList(GridUnit gridUnit);

	public List<Map<String, Object>> queryGridUnitNoStaffTranList(
			TelcomRegion telcomRegion);

}
