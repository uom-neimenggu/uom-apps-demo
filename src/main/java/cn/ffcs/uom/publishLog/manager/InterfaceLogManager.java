/**
 * 
 */
package cn.ffcs.uom.publishLog.manager;

import java.util.List;

import cn.ffcs.uom.publishLog.model.InterfaceLog;

/**
 * @author wenyaopeng
 *
 */
public interface InterfaceLogManager {

	public List<InterfaceLog> queryInterfaceLog(String ftpTaskInstanceId,String targetSystem);

}
