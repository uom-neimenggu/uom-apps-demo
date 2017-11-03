package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public interface DwOrganizationRelationManager {
	/**
	 * 分页取类信息
	 * 
	 * @param organizationRelation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrganizationRelation(
			Organization organization,
			OrganizationRelation organizationRelation, int currentPage,
			int pageSize);
	   /**
     * 分页取类信息(忽略Status_Cd)
     * 
     * @param organizationRelation
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageInfo queryPageInfoByOrganizationRelationNoStatusCd(
            Organization organization,
            OrganizationRelation organizationRelation, int currentPage,
            int pageSize);

	/**
	 * 删除记录
	 * 
	 * @param organizationRelation
	 */
	public void removeOrganizationRelation(
			OrganizationRelation organizationRelation);

	/**
	 * 更新记录
	 * 
	 * @param organizationRelation
	 */
	public void updateOrganizationRelation(
			OrganizationRelation organizationRelation);

	/**
	 * 保存记录
	 * 
	 * @param organizationRelation
	 */
	public void addOrganizationRelation(
			OrganizationRelation organizationRelation);

	/**
	 * 获取组织关系
	 * 
	 * @param organizationRelation
	 * @return
	 */
	public OrganizationRelation queryOrganizationRelation(
			OrganizationRelation organizationRelation);

	/**
	 * 获取组织关系列表
	 * 
	 * @param organizationRelation
	 * @return
	 */
	public List<OrganizationRelation> queryOrganizationRelationList(
			OrganizationRelation organizationRelation);
	
	/**
	 * 获取树上级组织
	 * 
	 * @param orgId
	 * @return
	 */
	public List<OrganizationRelation> queryParentTreeOrganizationRelationList(
			Long orgId, String orgType);

	/**
	 * 根据行政上级生成组织全称
	 * 
	 * @param orgId
	 * @return
	 */
	public String getOrgFullName(Long orgId);
	
	/**
	 * 验证上级组织进驻关系
	 * @param orgId 进驻组织ID
	 * @param relaOrgId 被进驻组织ID
	 * @return
	 */
	public boolean checkSuperiorOrgEnterRela(Long orgId,Long relaOrgId,String relaCd);
	/**
	 * 验证下级组织进驻关系
	 * @param orgId 进驻组织ID
	 * @param relaOrgId 被进驻组织ID
	 * @return
	 */
	public boolean checkSubordinateOrgEnterRela(Long orgId,Long relaOrgId,String relaCd);
	/**
	 * 验证组织是否是网点
	 * @param orgId
	 * @return
	 */
	public boolean checkOrgIsNetworkPoint(Long orgId);
	/**
	 * 验证组织是否是营业厅
	 * @param orgId
	 * @return
	 */
	public boolean checkOrgIsBusinessHall(Long orgId);
	
	/**
	 * 根据组织ID验证是否有关系存在
	 * 用于 一个网点只能在一个营业厅下
	 * @param orgId
	 * @return
	 */
	public boolean checkOrgRelaIsExist(Long orgId,String relaCd);

	/**
     * 根据组织树id获取组织关系根节点id
     * @param treeId
     * @return
     */
    public OrganizationRelation getOrgRelByTreeId(Long treeId);
	
}
