package cn.ffcs.uac.staff.dao.impl;

import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacAttachedInfoDao;
import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uom.common.dao.BaseDaoImpl;

@Repository("uacAttachedInfoDao")
public class UacAttachedInfoDaoImpl extends BaseDaoImpl implements
		UacAttachedInfoDao {
	@Override
	public void addUacAttachedInfo(UacAttachedInfo uacAttachedInfo) {
		uacAttachedInfo.add();
	}

	@Override
	public void delUacAttachedInfo(UacAttachedInfo uacAttachedInfo) {
		uacAttachedInfo.remove();
	}

	@Override
	public void updateUacAttachedInfo(UacAttachedInfo uacAttachedInfo) {
		uacAttachedInfo.update();
	}
}
