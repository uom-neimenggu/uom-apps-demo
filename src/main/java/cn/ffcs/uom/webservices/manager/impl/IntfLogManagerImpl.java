package cn.ffcs.uom.webservices.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.webservices.dao.IntfLogDao;
import cn.ffcs.uom.webservices.manager.IntfLogManager;
import cn.ffcs.uom.webservices.model.IntfLog;

@Service("intfLogManager")
@Scope("prototype")
public class IntfLogManagerImpl implements IntfLogManager {
	@Resource
	private IntfLogDao intfLogDao;

	@Override
	public void saveIntfLog(IntfLog intfLog) {
		intfLogDao.addIntfLog(intfLog);
	}
}
