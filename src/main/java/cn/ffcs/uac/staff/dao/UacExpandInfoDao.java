package cn.ffcs.uac.staff.dao;

import java.util.List;

import cn.ffcs.uac.staff.model.UacExpandInfo;
import cn.ffcs.uom.common.dao.BaseDao;

public interface UacExpandInfoDao extends BaseDao{
	public void delUacExpandInfoList(List<UacExpandInfo> uacExpandInfoList);

	public void addUacExpandInfot(UacExpandInfo uacExpandInfo);

	public void delUacExpandInfo(UacExpandInfo uacExpandInfo);

	public void updateUacExpandInfo(UacExpandInfo uacExpandInfo);
}
