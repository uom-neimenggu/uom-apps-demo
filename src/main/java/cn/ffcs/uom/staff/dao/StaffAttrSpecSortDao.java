package cn.ffcs.uom.staff.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.staff.model.StaffAttrSpecSort;

public interface StaffAttrSpecSortDao extends BaseDao {
	/**
	 * 获取员工属性规格列表
	 * 
	 * @return
	 */
	public List<StaffAttrSpecSort> queryStaffAttrSpecSortList();

}
