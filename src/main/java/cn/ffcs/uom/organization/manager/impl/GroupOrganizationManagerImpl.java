package cn.ffcs.uom.organization.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.GroupOrganizationDao;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.model.GroupOrganization;

@Service("groupOrganizationManager")
@Scope("prototype")
public class GroupOrganizationManagerImpl implements GroupOrganizationManager {
	@Resource
	private GroupOrganizationDao groupOrganizationDao;

	@Override
	public PageInfo queryPageInfoByGroupOrganization(
			GroupOrganization groupOrganization, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return groupOrganizationDao.queryPageInfoByGroupOrganization(
				groupOrganization, currentPage, pageSize);
	}

	@Override
	public List<GroupOrganization> queryGroupOrganizationList(
			GroupOrganization groupOrganization) {
		// TODO Auto-generated method stub
		return groupOrganizationDao
				.queryGroupOrganizationList(groupOrganization);
	}

}
