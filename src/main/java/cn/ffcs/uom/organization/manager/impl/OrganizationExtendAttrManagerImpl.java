package cn.ffcs.uom.organization.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.organization.dao.OrganizationExtendAttrDao;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

@Service("organizationExtendAttrManager")
@Scope("prototype")
public class OrganizationExtendAttrManagerImpl implements
		OrganizationExtendAttrManager {

	@Resource(name = "organizationExtendAttrDao")
	private OrganizationExtendAttrDao organizationExtendAttrDao;

	@Override
	public OrganizationExtendAttr queryOrganizationExtendAttr(
			OrganizationExtendAttr organizationExtendAttr) {
		// TODO Auto-generated method stub
		return organizationExtendAttrDao
				.queryOrganizationExtendAttr(organizationExtendAttr);
	}

	@Override
	public OrganizationExtendAttr queryOrganizationExtendAttrStatusCd1100(
			OrganizationExtendAttr organizationExtendAttr) {
		// TODO Auto-generated method stub
		return organizationExtendAttrDao
				.queryOrganizationExtendAttrStatusCd1100(organizationExtendAttr);
	}

	@Override
	public List<OrganizationExtendAttr> queryOrganizationExtendAttrList(
			OrganizationExtendAttr organizationExtendAttr) {
		// TODO Auto-generated method stub
		return organizationExtendAttrDao
				.queryOrganizationExtendAttrList(organizationExtendAttr);
	}

}
