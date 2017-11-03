package cn.ffcs.uom.dataPermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;

public interface AroleOrganizationLevelDao extends BaseDao {
	/**
	 * 分页取类信息
	 * 
	 * @param aAroleOrganizationLevel
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageInfo queryPageInfoByAroleOrganizationLevel(
			AroleOrganizationLevel aAroleOrganizationLevel, int currentPage,
			int pageSize) throws Exception;

	/**
	 * 取角色组织层级列表
	 * 
	 * @param aAroleOrganizationLevel
	 * @return
	 * @throws Exception
	 */
	public List<AroleOrganizationLevel> queryAroleOrganizationLevelList(
			AroleOrganizationLevel aAroleOrganizationLevel);

	public AroleOrganizationLevel queryAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel);

	/**
	 * 保存记录
	 * 
	 * @param aAroleOrganizationLevel
	 */
	public void addAroleOrganizationLevel(
			AroleOrganizationLevel aAroleOrganizationLevel);

	/**
	 * 删除记录
	 * 
	 * @param aAroleOrganizationLevel
	 */
	public void removeAroleOrganizationLevel(
			AroleOrganizationLevel aAroleOrganizationLevel);

}
