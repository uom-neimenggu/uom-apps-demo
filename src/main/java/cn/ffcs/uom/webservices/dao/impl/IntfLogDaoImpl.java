package cn.ffcs.uom.webservices.dao.impl;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.webservices.dao.IntfLogDao;
import cn.ffcs.uom.webservices.model.IntfLog;

@Repository("intfLogDao")
public class IntfLogDaoImpl extends BaseDaoImpl implements IntfLogDao {

	@Override
	public void addIntfLog(IntfLog intfLog) {
		try {
			String sql = "insert into INTF_LOG (TRANS_ID, MSG_TYPE, SYSTEM, RESULT, REQUEST_CONTENT, RESPONSE_CONTENT, BEGIN_DATE, END_DATE, CONSUME_TIME, ERR_CODE, ERR_MSG, FTP_TASK_INSTANCE_ID, MSG_ID, LOG_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ intfLog.getSeqName() + ".NEXTVAL)";
			this.getJdbcTemplate()
					.update(sql,
							new Object[] { intfLog.getTransId(),
									intfLog.getMsgType(), intfLog.getSystem(),
									intfLog.getResult(),
									intfLog.getRequestContent(),
									intfLog.getResponseContent(),
									intfLog.getBeginDate(),
									intfLog.getEndDate(),
									intfLog.getConsumeTime(),
									intfLog.getErrCode(), intfLog.getErrMsg(),
									intfLog.getFtpTaskInstanceId(),
									intfLog.getMsgId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
