package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.WbPsnMsgDao;
import cn.ffcs.uom.organization.manager.WbPsnMsgManager;
import cn.ffcs.uom.organization.model.HanaWbPsnMsg;

@Service("wbPsnMsgManager")
@Scope("prototype")
public class WbPsnMsgManagerImpl implements WbPsnMsgManager {
	@Resource
	private WbPsnMsgDao wbPsnMsgDao;

	@Override
	public PageInfo queryPageInfoByWbPsnMsg(
			HanaWbPsnMsg wbPsnMsg, int currentPage, int pageSize) {
		return wbPsnMsgDao.queryPageInfoByWbPsnMsg(
				wbPsnMsg, currentPage, pageSize);
	}
}
