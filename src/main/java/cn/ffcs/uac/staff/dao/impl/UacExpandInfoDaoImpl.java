package cn.ffcs.uac.staff.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacExpandInfoDao;
import cn.ffcs.uac.staff.model.UacExpandInfo;
import cn.ffcs.uom.common.dao.BaseDaoImpl;

@Repository("uacExpandInfoDao")
public class UacExpandInfoDaoImpl extends BaseDaoImpl implements
		UacExpandInfoDao {
	@Override
	public void delUacExpandInfoList(List<UacExpandInfo> uacExpandInfoList) {
		for (UacExpandInfo uacExpandInfo : uacExpandInfoList) {
			uacExpandInfo.remove();
		}
	}
	
	@Override
	public void addUacExpandInfot(UacExpandInfo uacExpandInfo) {
		uacExpandInfo.add();
	}

	@Override
	public void delUacExpandInfo(UacExpandInfo uacExpandInfo) {
		uacExpandInfo.remove();
	}

	@Override
	public void updateUacExpandInfo(UacExpandInfo uacExpandInfo) {
		uacExpandInfo.update();
	}
}
