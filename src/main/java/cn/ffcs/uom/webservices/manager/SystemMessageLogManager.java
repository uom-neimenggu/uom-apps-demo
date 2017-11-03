package cn.ffcs.uom.webservices.manager;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;
import cn.ffcs.uom.webservices.model.SystemMessageLog;

public interface SystemMessageLogManager {
	/**
	 * 保存短信日志
	 * 
	 * @param systemMessageLog
	 */
	public void saveSystemMessageLog(SystemMessageLog systemMessageLog);

	/**
	 * 保存短信日志
	 */
	public void saveSystemMessageLog(BusinessSystem businessSystem,
			String exception, String systemMessage,
			IntfTaskInstance intfTaskInstance);
	/**
	 * 发送短信
	 * @param systemMessage
	 */
	public void saveSystemMessageLog(BusinessSystem businessSystem, String exception, String systemMessage);
	/**
	 * 发送短信，系统监控使用
	 * @param systemMessageConfig
	 * @param systemMessage
	 */
	public boolean saveSystemMessageLog(BusinessSystem businessSystem, String systemMessage);
}
