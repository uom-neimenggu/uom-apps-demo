package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.staff.model.StaffAttrSpec;

public interface TreeStaffExtendAttrManager {
	/**
	 * 获取员工规格列表
	 * 
	 * @param treeType
	 * @param attrSpecSortTypeTree
	 * @return
	 */
	public List<StaffAttrSpec> getStaffAttrSpecListByTreeTypeAndSortType(
			String treeType, String sortType);

}
