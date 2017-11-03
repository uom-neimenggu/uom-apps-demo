package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

public interface OrganizationTranManager {
	/**
	 * 分页取类信息
	 * 
	 * @param group
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrganizationTran(
			OrganizationTran organizationTran, int currentPage, int pageSize);

	public List<OrganizationTran> queryOrganizationTranList(
			OrganizationTran organizationTran, String pattern);

	/**
	 * 新增
	 * 
	 * @param organizationTran
	 */
	public void addOrganizationTran(OrganizationTran organizationTran);

	/**
	 * 删除
	 * 
	 * @param organizationTran
	 */
	public void removeOrganizationTran(OrganizationTran organizationTran);

	/**
	 * 更新记录
	 * 
	 * @param group
	 */
	public void updateOrganizationTran(OrganizationTran organizationTran);

	/**
	 * 分页取类信息
	 * 
	 * @param group
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByUomGroupOrgTran(
			UomGroupOrgTran uomGroupOrgTran, int currentPage, int pageSize);

	public List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran, String pattern);

	/**
	 * 新增
	 * 
	 * @param uomGroupOrgTran
	 */
	public void addUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran);

	/**
	 * 删除
	 * 
	 * @param uomGroupOrgTran
	 */
	public void removeUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran);

	/**
	 * 更新记录
	 * 
	 * @param group
	 */
	public void updateUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran);
	
	/**
	 * 添加渠道网点关系以及设置流程状态
	 * .
	 * 这里主要是要设置流程的当前状态
	 * @param uomGroupOrgTran
	 * @author xiaof
	 * 2016年12月15日 xiaof
	 */
	public void addChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran);
	
	/**
     * 更新渠道网点关系以及设置流程状态，这里的更新不做直接更新，是先删除，然后新增
     * .
     * 这里主要是要设置流程的当前状态
     * @param uomGroupOrgTran
     * @author xiaof
     * 2016年12月15日 xiaof
     */
	public void updateChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran);
	
	/**
     * 删除渠道网点关系以及设置流程状态，直接设置为无效状态1100
     * .
     * 这里主要是要设置流程的当前状态
     * @param uomGroupOrgTran
     * @author xiaof
     * 2016年12月15日 xiaof
     */
	public void removeChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran);
	
	/**
	 * 查询网点包区的关系，由于流程对应有多种状态，所以这里应该查所有非1100的数据
	 * .
	 * 
	 * @param uomGroupOrgTran
	 * @return
	 * @author xiaof
	 * 2016年12月21日 xiaof
	 */
	public List<UomGroupOrgTran> queryChannelPackAreaTranList(UomGroupOrgTran uomGroupOrgTran);
	
	/**
	 * 查询网点包区的关系，只查询该网点是否存在以店包区
	 * @param uomGroupOrgTran
	 * @return
	 */
	public List<UomGroupOrgTran> queryUomGroupOrgTranStoreAreaList(UomGroupOrgTran uomGroupOrgTran);

	List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran);

}
