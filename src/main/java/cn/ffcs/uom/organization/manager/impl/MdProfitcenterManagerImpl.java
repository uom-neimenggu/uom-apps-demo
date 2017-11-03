package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdProfitcenterDao;
import cn.ffcs.uom.organization.manager.MdProfitcenterManager;
import cn.ffcs.uom.organization.model.MdProfitcenter;

@Service("mdProfitcenterManager")
@Scope("prototype")
public class MdProfitcenterManagerImpl implements MdProfitcenterManager {
	@Resource
	private MdProfitcenterDao mdProfitcenterDao;

	@Override
	public PageInfo queryPageInfoByMdProfitcenter(
			MdProfitcenter mdProfitcenter, int currentPage, int pageSize) {
		return mdProfitcenterDao.queryPageInfoByMdProfitcenter(
				mdProfitcenter, currentPage, pageSize);
	}
}
