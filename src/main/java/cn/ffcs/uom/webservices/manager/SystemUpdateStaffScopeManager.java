package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.webservices.model.SystemUpdateStaffScope;

public interface SystemUpdateStaffScopeManager {
	/**
	 * 查询系统配置范围列表
	 * 
	 * @param querySystemUpdateStaffScope
	 */
	public List<SystemUpdateStaffScope> querySystemUpdateStaffScopeList(
			SystemUpdateStaffScope querySystemUpdateStaffScope);
}
