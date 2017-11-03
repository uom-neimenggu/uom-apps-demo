package cn.ffcs.uom.hisQuery.manager;

import java.util.Map;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
public interface OrgHisManager {
	/**
	 * 查询历史记录vo
	 * 
	 * @param parmsMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryOrgHisPageInfoByParams(Map paramsMap, int currentPage,
			int pageSize);
	
	public Organization queryOrgByParams(Map parmsMap);

}
