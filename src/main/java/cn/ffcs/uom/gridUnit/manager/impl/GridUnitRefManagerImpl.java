package cn.ffcs.uom.gridUnit.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.dao.GridUnitRefDao;
import cn.ffcs.uom.gridUnit.manager.GridUnitRefManager;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;

@Service("gridUnitRefManager")
@Scope("prototype")
public class GridUnitRefManagerImpl implements GridUnitRefManager {

	@Resource
	private GridUnitRefDao gridUnitRefDao;

	@Override
	public PageInfo queryPageInfoByGridUnitRef(GridUnitRef gridUnitRef,
			int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return gridUnitRefDao.queryPageInfoByGridUnitRef(gridUnitRef,
				currentPage, pageSize);
	}

	@Override
	public List<GridUnitRef> queryGridUnitRefList(GridUnitRef gridUnitRef) {
		// TODO Auto-generated method stub
		return gridUnitRefDao.queryGridUnitRefList(gridUnitRef);
	}

}
