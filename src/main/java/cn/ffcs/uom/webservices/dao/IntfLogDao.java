package cn.ffcs.uom.webservices.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.webservices.model.IntfLog;

/**
 * 日志使用
 * 
 * @author ZhaoF
 * 
 */
public interface IntfLogDao extends BaseDao {
	/**
	 * 保存接口日志
	 * 
	 * @param intfLog
	 */
	public void addIntfLog(IntfLog intfLog);
}
