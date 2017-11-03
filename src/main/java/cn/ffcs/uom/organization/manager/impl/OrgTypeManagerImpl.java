package cn.ffcs.uom.organization.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.dao.OrgTypeDao;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.model.OrgType;

@Service("orgTypeManager")
@Scope("prototype")
public class OrgTypeManagerImpl implements OrgTypeManager {

	@Autowired
	private OrgTypeDao orgTypeDao;

	@Override
	public List<OrgType> getOrgTypeList(TreeOrgTypeRule totr) {
		return orgTypeDao.getOrgTypeList(totr);
	}

	@Override
	public List<OrgType> queryOrgTypeList(OrgType orgType) {
		return orgTypeDao.queryOrgTypeList(orgType);
	}
}
