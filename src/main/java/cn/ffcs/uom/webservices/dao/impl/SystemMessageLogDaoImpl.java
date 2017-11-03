package cn.ffcs.uom.webservices.dao.impl;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.webservices.dao.SystemMessageLogDao;
import cn.ffcs.uom.webservices.model.SystemMessageLog;

@Repository("systemMessageLogDao")
public class SystemMessageLogDaoImpl extends BaseDaoImpl implements
		SystemMessageLogDao {

	@Override
	public void addSystemMessageLog(SystemMessageLog systemMmessageLog) {
		try {
			String sql = "INSERT INTO SYSTEM_MESSAGE_LOG (SYSTEM_CODE, RESULT, ERR_CODE, SYSTEM_MESSAGE_INFO, CREATE_DATE, SYSTEM_MESSAGE_LOG_ID) values (?, ?, ?, ?, ?, "
					+ systemMmessageLog.getSeqName() + ".NEXTVAL)";
			this.getJdbcTemplate().update(
					sql,
					new Object[] { systemMmessageLog.getSystemCode(),
							systemMmessageLog.getResult(),
							systemMmessageLog.getErrCode(),
							systemMmessageLog.getSystemMessageInfo(),
							systemMmessageLog.getCreateDate() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
