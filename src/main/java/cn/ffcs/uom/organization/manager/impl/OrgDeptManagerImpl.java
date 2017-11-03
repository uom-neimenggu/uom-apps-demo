package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OrgDeptDao;
import cn.ffcs.uom.organization.manager.OrgDeptManager;
import cn.ffcs.uom.organization.model.HanaOrgDept;

@Service("orgDeptManager")
@Scope("prototype")
public class OrgDeptManagerImpl implements OrgDeptManager {
	@Resource
	private OrgDeptDao orgDeptDao;

	@Override
	public PageInfo queryPageInfoByOrgDept(
			HanaOrgDept orgDept, int currentPage, int pageSize) {
		return orgDeptDao.queryPageInfoByOrgDept(
				orgDept, currentPage, pageSize);
	}
}
