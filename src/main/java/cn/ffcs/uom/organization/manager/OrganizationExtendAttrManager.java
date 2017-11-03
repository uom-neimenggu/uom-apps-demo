package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

public interface OrganizationExtendAttrManager {

	public OrganizationExtendAttr queryOrganizationExtendAttr(
			OrganizationExtendAttr organizationExtendAttr);
	
	public OrganizationExtendAttr queryOrganizationExtendAttrStatusCd1100(
			OrganizationExtendAttr organizationExtendAttr);

	public List<OrganizationExtendAttr> queryOrganizationExtendAttrList(
			OrganizationExtendAttr organizationExtendAttr);
}
