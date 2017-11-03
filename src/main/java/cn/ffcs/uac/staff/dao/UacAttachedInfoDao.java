package cn.ffcs.uac.staff.dao;

import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uom.common.dao.BaseDao;

public interface UacAttachedInfoDao extends BaseDao {

	public void addUacAttachedInfo(UacAttachedInfo uacAttachedInfo);

	public void delUacAttachedInfo(UacAttachedInfo uacAttachedInfo);

	public void updateUacAttachedInfo(UacAttachedInfo uacAttachedInfo);

}
