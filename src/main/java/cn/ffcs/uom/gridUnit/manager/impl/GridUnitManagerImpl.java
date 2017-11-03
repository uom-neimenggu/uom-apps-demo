package cn.ffcs.uom.gridUnit.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.dao.GridUnitDao;
import cn.ffcs.uom.gridUnit.manager.GridUnitManager;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Service("gridUnitManager")
@Scope("prototype")
public class GridUnitManagerImpl implements GridUnitManager {

	@Resource
	private GridUnitDao gridUnitDao;

	@Override
	public PageInfo queryPageInfoByGridUnit(GridUnit gridUnit, int currentPage,
			int pageSize) {
		// TODO Auto-generated method stub
		return gridUnitDao.queryPageInfoByGridUnit(gridUnit, currentPage,
				pageSize);
	}

	@Override
	public List<GridUnit> queryGridUnitList(GridUnit gridUnit) {
		// TODO Auto-generated method stub
		return gridUnitDao.queryGridUnitList(gridUnit);
	}
	
	@Override
	public List<Map<String, Object>> queryGridUnitNoStaffTranList(
			TelcomRegion telcomRegion) {
		return gridUnitDao.queryGridUnitNoStaffTranList(telcomRegion);
	}

}
