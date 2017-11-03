package cn.ffcs.uom.staff.manager.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.staff.manager.StaffAttrSpecSortManager;
import cn.ffcs.uom.staff.model.StaffAttrSpecSort;

@Service("staffAttrSpecSortManager")
@Scope("prototype")
public class StaffAttrSpecSortManagerImpl implements StaffAttrSpecSortManager {

	@Override
	public List<StaffAttrSpecSort> getStaffAttrSpecSortList() {
		return StaffAttrSpecSort.repository().queryStaffAttrSpecSortList();
	}

}
