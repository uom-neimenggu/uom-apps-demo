package cn.ffcs.uom.dataPermission.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.model.Organization;

public interface AroleOrganizationLevelManager {
	/**
	 * 分页取类信息
	 * 
	 * @param aroleOrganizationLevel
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageInfo queryPageInfoByAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel, int currentPage,
			int pageSize) throws Exception;

	/**
	 * 取角色组织层级列表
	 * 
	 * @param aroleOrganizationLevel
	 * @return
	 * @throws Exception
	 */
	public List<AroleOrganizationLevel> queryAroleOrganizationLevelList(
			AroleOrganizationLevel aroleOrganizationLevel);

	/**
	 * 保存记录
	 * 
	 * @param aroleOrganizationLevel
	 */
	public void addAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel);

	/**
	 * 删除记录
	 * 
	 * @param aroleOrganizationLevel
	 */
	public void removeAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel);

	/**
	 * 组织层级权限验证
	 * 
	 * @param aroleOrganizationLevel
	 * @param organization
	 * @return
	 */
	public boolean aroleOrganizationLevelValid(
			AroleOrganizationLevel aroleOrganizationLevel,
			Organization organization);

}
