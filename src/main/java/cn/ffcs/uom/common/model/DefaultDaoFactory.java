package cn.ffcs.uom.common.model;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

public class DefaultDaoFactory {
	private static BaseDao defaultBaseDao;

	public static BaseDao getDefaultDao() {
		if (defaultBaseDao == null) {
			defaultBaseDao = (BaseDao) ApplicationContextUtil
					.getBean("defaultBaseDao");
		}
		return defaultBaseDao;
	}
}
