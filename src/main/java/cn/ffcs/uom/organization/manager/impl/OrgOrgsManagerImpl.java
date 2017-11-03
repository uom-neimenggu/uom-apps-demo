package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OrgOrgsDao;
import cn.ffcs.uom.organization.manager.OrgOrgsManager;
import cn.ffcs.uom.organization.model.HanaOrgOrgs;

@Service("orgOrgsManager")
@Scope("prototype")
public class OrgOrgsManagerImpl implements OrgOrgsManager {
	@Resource
	private OrgOrgsDao orgOrgsDao;

	@Override
	public PageInfo queryPageInfoByOrgOrgs(
			HanaOrgOrgs orgOrgs, int currentPage, int pageSize) {
		return orgOrgsDao.queryPageInfoByOrgOrgs(
				orgOrgs, currentPage, pageSize);
	}
}
