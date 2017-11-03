package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.WbCorpDao;
import cn.ffcs.uom.organization.manager.WbCorpManager;
import cn.ffcs.uom.organization.model.HanaWbCorp;

@Service("wbCorpManager")
@Scope("prototype")
public class WbCorpManagerImpl implements WbCorpManager {
	@Resource
	private WbCorpDao wbCorpDao;

	@Override
	public PageInfo queryPageInfoByWbCorp(
			HanaWbCorp wbCorp, int currentPage, int pageSize) {
		return wbCorpDao.queryPageInfoByWbCorp(
				wbCorp, currentPage, pageSize);
	}
}
