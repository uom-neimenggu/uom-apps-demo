package cn.ffcs.uac.staff.dao;

import cn.ffcs.uom.common.dao.BaseDao;

public interface RemindInforDao extends BaseDao{

	public void saveRemindInfo(String msg);
}
