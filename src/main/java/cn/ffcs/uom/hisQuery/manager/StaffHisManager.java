/**
 * 
 */
package cn.ffcs.uom.hisQuery.manager;

import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;

/**
 * @author yahui
 *
 */
public interface StaffHisManager {
	/**
	 * 查询历史记录vo
	 * 
	 * @param parmsMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryStaffHisPageInfoByParams(Map paramsMap, int currentPage,
			int pageSize);
	public Staff queryStaffDetail(Map paramsMap);

}
