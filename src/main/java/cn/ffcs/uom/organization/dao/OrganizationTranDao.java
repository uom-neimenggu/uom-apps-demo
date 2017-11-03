package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

public interface OrganizationTranDao extends BaseDao {
	/**
	 * 分页取类信息
	 * 
	 * @param organizationTran
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrganizationTran(
			OrganizationTran organizationTran, int currentPage, int pageSize);

	public List<OrganizationTran> queryOrganizationTranList(
			OrganizationTran organizationTran, String pattern);

	/**
	 * 分页取类信息
	 * 
	 * @param uomGroupOrgTran
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByUomGroupOrgTran(
			UomGroupOrgTran uomGroupOrgTran, int currentPage, int pageSize);

	public List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran, String pattern);

	/**
	 * 查询所有非1100的关系数据 .
	 * 
	 * @param uomGroupOrgTran
	 * @return
	 * @author xiaof 2016年12月21日 xiaof
	 */
	public List<UomGroupOrgTran> queryUomGroupOrgTranNotStatusCd1100List(
			UomGroupOrgTran UomGroupOrgTran);

	/**
	 * 查询非失效的该网点下的以店包区记录
	 * 
	 * @param uomGroupOrgTran
	 * @return
	 */
	public List<UomGroupOrgTran> queryUomGroupOrgTranStoreAreaNot1100List(
			UomGroupOrgTran uomGroupOrgTran);

	List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran);

}
