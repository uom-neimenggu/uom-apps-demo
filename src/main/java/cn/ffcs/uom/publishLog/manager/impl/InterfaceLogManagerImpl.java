/**
 * 
 */
package cn.ffcs.uom.publishLog.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.publishLog.dao.InterfaceLogDao;
import cn.ffcs.uom.publishLog.manager.InterfaceLogManager;
import cn.ffcs.uom.publishLog.model.InterfaceLog;

/**
 * @author wenyaopeng
 *
 */
@Service("interfaceLogManager")
@Scope("prototype")
@SuppressWarnings("static-access")
public class InterfaceLogManagerImpl implements InterfaceLogManager{

	@Resource
	private InterfaceLogDao interfaceLogDao;
	
	public List<InterfaceLog> queryInterfaceLog(String ftpTaskInstanceId,String targetSystem){
		return interfaceLogDao.queryInterfaceLog(ftpTaskInstanceId,targetSystem);
	}

}
