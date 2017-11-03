package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.HiPsnorgDao;
import cn.ffcs.uom.organization.manager.HiPsnorgManager;
import cn.ffcs.uom.organization.model.HanaHiPsnorg;

@Service("hiPsnorgManager")
@Scope("prototype")
public class HiPsnorgManagerImpl implements HiPsnorgManager {
	@Resource
	private HiPsnorgDao hiPsnorgDao;

	@Override
	public PageInfo queryPageInfoByHiPsnorg(
			HanaHiPsnorg hiPsnorg, int currentPage, int pageSize) {
		return hiPsnorgDao.queryPageInfoByHiPsnorg(
				hiPsnorg, currentPage, pageSize);
	}
}
