package cn.ffcs.uom.dataPermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;

public interface AroleOrganizationManager {
	/**
	 * 分页取类信息
	 * 
	 * @param aroleOrganization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public PageInfo queryPageInfoByRoleOrganization(AroleOrganization aroleOrganization,
			int currentPage, int pageSize) throws Exception;
	
	/**
	 * 取角色组织列表
	 * 
	 * @param aroleOrganization
	 * @return
	 * @throws Exception
	 */
	public List<AroleOrganization> queryRoleOrganizationList(
			AroleOrganization aroleOrganization) throws Exception;
	
	/**
	 * 删除记录
	 * 
	 * @param aroleOrganization
	 */
	public void removeRoleOrganization(
			AroleOrganization aroleOrganization);

	/**
	 * 保存记录
	 * 
	 * @param aroleOrganization
	 */
	public void addRoleOrganization(
			AroleOrganization aroleOrganization);
}
