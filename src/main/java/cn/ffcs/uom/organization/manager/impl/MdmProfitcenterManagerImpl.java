package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmProfitcenterDao;
import cn.ffcs.uom.organization.manager.MdmProfitcenterManager;
import cn.ffcs.uom.organization.model.MdmProfitcenter;

@Service("mdmProfitcenterManager")
@Scope("prototype")
public class MdmProfitcenterManagerImpl implements MdmProfitcenterManager {
	@Resource
	private MdmProfitcenterDao mdmProfitcenterDao;

	@Override
	public PageInfo queryPageInfoByMdmProfitcenter(
			MdmProfitcenter mdmProfitcenter, int currentPage, int pageSize) {
		return mdmProfitcenterDao.queryPageInfoByMdmProfitcenter(
				mdmProfitcenter, currentPage, pageSize);
	}
}
