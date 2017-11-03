/**
 * 
 */
package cn.ffcs.uom.hisQuery.manager;

import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;

/**
 * @author yahui
 *
 */
public interface OrgRelaHisManager {	
	/**
	 * 查询历史记录vo
	 * 
	 * @param parmsMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryOrgRelaPageInfoByParams(Map paramsMap,
			int currentPage, int pageSize);

}
