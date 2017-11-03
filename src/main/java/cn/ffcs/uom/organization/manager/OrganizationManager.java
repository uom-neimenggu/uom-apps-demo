package cn.ffcs.uom.organization.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.vo.OrganizationHXImportVo;

public interface OrganizationManager {
	/**
	 * 分页取类信息
	 * 
	 * @param organization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrganization(Organization organization, int currentPage,
		int pageSize);
	
	/**
	 * 根据组织树根节点ID获取分页取类信息
	 * 
	 * @param orgTreeRootId
	 * @param organization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgTreeRootId(String orgTreeRootId, Organization organization,
		int currentPage, int pageSize);
	
	/**
	 * 分页取类信息(忽略status_cd)
	 * 
	 * @param organization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrganizationNoStatusCd(Organization organization,
		int currentPage, int pageSize);
	
	/**
	 * 分页去营销第五级组织，包区组织
	 * .
	 * 
	 * @param organization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author xiaof
	 * 2016年9月21日 xiaof
	 */
	public PageInfo queryPageInfoByPackAreaOrganization(Organization organization,
        int currentPage, int pageSize);
	
	/**
	 * 分页查询代理商渠道网点
	 * .
	 * 
	 * @param organization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author xiaof
	 * 2016年9月21日 xiaof
	 */
	public PageInfo queryPageInfoByAgentChannelOrganization(Organization organization,
        int currentPage, int pageSize);
	
	/**
	 * 删除记录
	 * 
	 * @param organization
	 */
	public void removeOrganization(Organization organization);
	
	/**
	 * 更新记录
	 * 
	 * @param organization
	 */
	public void updateOrganization(Organization organization);
	
	/**
	 * 保存记录
	 * 
	 * @param organization
	 */
	public void addOrganization(Organization organization);
	
	/**
	 * @param id
	 * @return
	 */
	public Organization getById(Long id);
	
	/**
	 * @param id
	 * @return
	 */
	public Organization getByIdStatusCd1100(Long id);
	
	/**
	 * 查询组织关系列表
	 * 
	 * @param organizationRelation
	 * @return
	 */
	public List<OrganizationRelation> queryOrganizationRelationList(OrganizationRelation or);
	
	/**
	 * 获取组织树下级组织
	 * 
	 * @param orgId
	 * @return
	 */
	public List<OrganizationRelation> querySubTreeOrganizationRelationList(Long orgId);
	
	/**
	 * 获取树上级组织
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Organization> queryParentTreeOrganizationList(Long orgId, String orgType);
	
	/**
	 * 根据组织编码查找组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgCode(Organization organization);
	
	/**
	 * 分页查询失效组织信息
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo forQuertyOrganizationActivation(Organization organization, int currentPage,
		int pageSize);
	
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
	 * 获取组织层级
	 */
	public Integer getOrgTreeLevel(Long orgId);
	
	/**
	 * 获取营销组织层级
	 */
	public Integer getMarketingTreeLevel(Long orgId);
	
	/**
	 * 验证上传文件数据
	 * 
	 * @param waitUpLoadOrganizationListData
	 * @param waitUpLoadBusinessOutletsOrgList
	 * @param checkInfoList
	 * @param objDataArray
	 * @param totalColumn
	 * @param caseIndex
	 * @param markingType 营销树类型，2016,2017。。
	 * @return
	 * @throws Exception
	 */
	public int checkUpLoadFileData(List<Organization> waitUpLoadOrganizationList,
		List<Organization> waitUpLoadBusinessOutletsOrgList, List<String> checkInfoList,
		String[][] objDataArray, int totalColumn, int[] caseIndex) throws Exception;
	
	public int checkUpLoadHXData(List<String> levalList,
	    List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList, List<String> checkInfoList,
		String[][] objDataArray, int totalColumn, int[] caseIndex, String markingType) throws Exception;
	
	/**
	 * 保存代理商、营业网点列表
	 * 
	 * @param waitUpLoadOrganizationList
	 * @param waitUpLoadBusinessOutletsOrgList
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> saveAgentOrBusinessOutlets(List<String> levelList,
		List<Organization> waitUpLoadOrganizationList,
		List<Organization> waitUpLoadBusinessOutletsOrgList,
		List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList, int saveType) throws Exception;
	
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
	 * 级联更新组织全称（只更新归属关系的组织）
	 * 
	 * @param organization
	 */
	public void updateOrgFullNameOnCascade(Organization organization);
	
	/**
	 * 接口调用返回信息
	 * 
	 * @param newOrg
	 * @param oldOrg
	 * @param optType
	 *            操作类型 1新增组织 2添加组织关系 3修改组织 4删除组织关系 5删除组织
	 * @return msg
	 */
	public String getGridValid(Organization newOrg, Organization oldOrg, Organization relaOrg,
		String optType);
	
	/**
	 * 新增组织，组织关系，组织类型
	 * 
	 * @param org
	 */
	public void addOrgNetwork(Organization organization);
	
	/**
	 * 修改组织，组织关系，组织类型
	 * 
	 * @param org
	 */
	public void updateOrgNetwork(Organization organization);
	
	/**
	 * 删除组织，组织关系，组织类型
	 * 
	 * @param org
	 */
	public void delOrgNetwork(Organization organization);
	
	/**
	 * 验证组织扩展属性-网格
	 * 
	 * @param organization
	 * @return
	 */
	public String getDoValidOrganizationExtendAttrGrid(Organization organization);
	
	public void updateOrganizationExtendAttr(Organization organization);
	
}
