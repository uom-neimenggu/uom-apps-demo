/**
 * 
 */
package cn.ffcs.uom.publishLog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.publishLog.dao.InterfaceLogDao;
import cn.ffcs.uom.publishLog.model.InterfaceLog;

/**
 * @author wenyaopeng
 *
 */
@Repository("interfaceLogDao")
public class InterfaceLogDaoImpl extends BaseDaoImpl implements InterfaceLogDao{

	@Override
	public List<InterfaceLog> queryInterfaceLog(String ftpTaskInstanceId,String targetSystem) {
		if(ftpTaskInstanceId != null && targetSystem != null){
			StringBuffer sb =new StringBuffer();
			sb.append("select * from intf_log where ftp_task_instance_id = ? and system = ? order by log_id desc");
			List params = new ArrayList();
			params.add(ftpTaskInstanceId);
			params.add(targetSystem);
			return super.jdbcFindList(sb.toString(), params, InterfaceLog.class);
		}
		return null;
	}

}
