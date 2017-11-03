package cn.ffcs.uom.webservices.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.webservices.model.SystemMessageLog;

/**
 * 短信日志
 * 
 */
public interface SystemMessageLogDao extends BaseDao {
	/**
	 * 保存短信日志
	 * 
	 * @param systemmessageLog
	 */
	public void addSystemMessageLog(SystemMessageLog systemmessageLog);
}
