/**
 * 
 */
package cn.ffcs.uom.publishLog.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.publishLog.model.InterfaceLog;

/**
 * @author wenyaopeng
 *
 */
public interface InterfaceLogDao extends BaseDao {

	public List<InterfaceLog> queryInterfaceLog(String ftpTaskInstanceId,String targetSystem);
	
}
