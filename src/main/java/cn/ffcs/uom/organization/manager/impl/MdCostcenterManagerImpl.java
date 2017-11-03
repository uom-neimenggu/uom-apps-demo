package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdCostcenterDao;
import cn.ffcs.uom.organization.manager.MdCostcenterManager;
import cn.ffcs.uom.organization.model.MdCostcenter;

@Service("mdCostcenterManager")
@Scope("prototype")
public class MdCostcenterManagerImpl implements MdCostcenterManager {
	@Resource
	private MdCostcenterDao mdCostcenterDao;

	@Override
	public PageInfo queryPageInfoByMdCostcenter(
			MdCostcenter mdCostcenter, int currentPage, int pageSize) {
		return mdCostcenterDao.queryPageInfoByMdCostcenter(
				mdCostcenter, currentPage, pageSize);
	}
}
