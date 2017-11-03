package cn.ffcs.uom.ftpsyncfile.timetask;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Component("systemMonitorTimerTask")
@Scope("prototype")
public class SystemMonitorTimerTask extends TimerTask {
	
	/**
	 * jdbcTemplate
	 */
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name = "systemMessageLogManager")
	private SystemMessageLogManager systemMessageLogManager;
	
	private BusinessSystem businessSystem = null;
	
	private StringBuffer systemMessageBeforeIds = null;
	public SystemMonitorTimerTask(){
		systemMessageBeforeIds = new StringBuffer();
		businessSystem = new BusinessSystem();
		businessSystem.setSystemCode(WsConstants.SYSTEM_CODE_CPMIS); //主数据平台 
	}
	
	@Override
	public void run() {
		jdbcTemplate.execute(new ConnectionCallback<Object>() {
			@Override
			public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
				try {
					systemMessageBeforeIds.setLength(0);
					CallableStatement cstmt = conn.prepareCall("{CALL PKG_SYSTEM_MONITOR.getMessageData(?)}");
					cstmt.registerOutParameter(1, OracleTypes.CURSOR);
					cstmt.execute();
					ResultSet messageData = (ResultSet) cstmt.getObject(1);//获取数据
					while(messageData.next()){
						businessSystem.setSystemCode(messageData.getString("system_code"));
						boolean sendSmsResult = systemMessageLogManager.saveSystemMessageLog(businessSystem, messageData.getString("system_message_info"));
						if(sendSmsResult){
					    	systemMessageBeforeIds.append(messageData.getString("system_message_before_id")).append(",");
						}
					}
					cstmt.close();
					if(systemMessageBeforeIds.length()>0){
						CallableStatement cs = conn.prepareCall("{CALL PKG_SYSTEM_MONITOR.removeMessageData(?)}");
						cs.setString(1, systemMessageBeforeIds.deleteCharAt(systemMessageBeforeIds.length()-1).toString() );
						cs.execute();
						cs.close();
					}
				} catch (Exception e) {
					if(conn!=null)
					conn.close();
				}
			    
				return true;
			}
		});
		
		
	}
	
}
