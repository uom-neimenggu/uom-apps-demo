package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

public interface OrganizationExtendAttrDao extends BaseDao {

	public OrganizationExtendAttr queryOrganizationExtendAttr(
			OrganizationExtendAttr organizationExtendAttr);

	public OrganizationExtendAttr queryOrganizationExtendAttrStatusCd1100(
			OrganizationExtendAttr organizationExtendAttr);

	public List<OrganizationExtendAttr> queryOrganizationExtendAttrList(
			OrganizationExtendAttr organizationExtendAttr);

}
