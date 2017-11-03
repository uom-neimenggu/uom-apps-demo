package cn.ffcs.uom.staff.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.staff.model.StaffAttrSpec;

@SuppressWarnings("rawtypes")
public interface TreeOrgStaffExtendAttrManager {
	/**
	 * 获取员工规格列表
	 * 
	 * @param params
	 * @return
	 */

    public List<StaffAttrSpec> getStaffAttrSpecListByByParams(Map params);

}
