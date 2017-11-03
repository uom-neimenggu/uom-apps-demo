package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.staff.model.StaffAttrSpecSort;

public interface StaffAttrSpecSortManager {
	/**
	 * 获取员工属性种类
	 * 
	 * @return
	 */
	public List<StaffAttrSpecSort> getStaffAttrSpecSortList();

}
