package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.UomGridCountyLog;

public interface OrganizationDao extends BaseDao {
	/**
	 * 根据组织编码查找组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgCode(Organization organization);

	/**
	 * 根据组织编码查找组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgCode(String orgCode);

	/**
	 * 根据组织名和组织类型称查找代理商组织
	 * 
	 * @param orgName
	 * @param orgType
	 */
	public Organization queryOrganizationByOrgNameAndOrgType(String orgName,
			String orgType);

	/**
	 * 根据组织名称和组织类型查找非代理商组织
	 * 
	 * @param orgName
	 * @param notSearchOrgType
	 */
	public Organization queryOrganizationByOrgNameAndNotSearchOrgType(
			String orgName, String notSearchOrgType);

	/**
	 * 根据组织ID获取组织实体
	 * 
	 * @param id
	 * @return
	 */
	public Organization getById(Long id);

	/**
	 * 根据组织ID获取组织实体
	 * 
	 * @param id
	 * @return
	 */
	public Organization getByIdStatusCd1100(Long id);

	/**
	 * 根据组织编码获取代理商组织实体
	 * 
	 * @param orgCode
	 * @return
	 */
	public Organization getAgentByOrgCode(String orgCode);

	/**
	 * 根据组织编码获取内部经营实体
	 * 
	 * @param orgCode
	 * @return
	 */
	public Organization getIbeByOrgCode(String orgCode);

	/**
	 * 分页查询失效组织信息
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo forQuertyOrganizationActivation(Organization organization,
			int currentPage, int pageSize);

	/**
	 * 激活失效的组织
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public String updateOrganizationList(List<Organization> organizationList);

	/**
	 * 获取组织修复ID
	 * 
	 * @return
	 */
	public Long getSeqOrgFixId();

	/**
	 * 根据参与人ID查找组织
	 * 
	 * @param partyId
	 */
	public Organization queryOrganizationByPartyId(Long partyId);

	/**
	 * 查询组织信息
	 * 
	 * @param organization
	 * @return
	 */
	public List<Organization> quertyOrganizationList(Organization organization);

	/**
	 * 查询网格城乡属性稽核中间表信息
	 * 
	 * @param uomGridCountyLog
	 * @return
	 */
	public List<UomGridCountyLog> queryUomGridCountyLogByOrgIdList(
			UomGridCountyLog uomGridCountyLog);

	/**
	 * 查询网格城乡属性稽核中间表信息
	 * 
	 * @param uomGridCountyLog
	 * @return
	 */
	public List<UomGridCountyLog> queryUomGridCountyLogList(
			UomGridCountyLog uomGridCountyLog);
}
