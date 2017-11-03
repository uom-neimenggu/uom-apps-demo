package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.GroupOrganization;

public interface GroupOrganizationManager {
	/**
	 * 分页取类信息
	 * 
	 * @param group
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGroupOrganization(
			GroupOrganization groupOrganization, int currentPage, int pageSize);

	public List<GroupOrganization> queryGroupOrganizationList(
			GroupOrganization groupOrganization);

}
