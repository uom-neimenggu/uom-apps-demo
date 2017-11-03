package cn.ffcs.uom.webservices.manager.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.SystemMessageLogDao;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageLog;
import cn.ffcs.uom.webservices.util.WsClientUtil;

@Service("systemMessageLogManager")
@Scope("prototype")
public class SystemMessageLogManagerImpl implements SystemMessageLogManager {
	@Resource
	private SystemMessageLogDao systemMessageLogDao;

	/**
	 * 业务系统
	 */
	@Resource
	private BusinessSystemManager businessSystemManager;
	/**
	 * 短信日志
	 */
	@Resource
	private MessageConfigManager messageConfigManager;

	/**
	 * 短信日志
	 */
	private BusinessSystem businessSystem;
	/**
	 * 短信日志
	 */
	private SystemMessageLog systemMessageLog;

	@Override
	public void saveSystemMessageLog(SystemMessageLog systemMessageLog) {
		systemMessageLogDao.addSystemMessageLog(systemMessageLog);
	}

	/**
	 * 保存短信日志
	 */
	@Override
	public void saveSystemMessageLog(BusinessSystem businessSystem,
			String exception, String systemMessage,
			IntfTaskInstance intfTaskInstance) {
		// 确保短信只下发一次
		if (intfTaskInstance != null && 2L == intfTaskInstance.getInvokeTimes()) {
			systemMessageLog = new SystemMessageLog();
			systemMessageLog.setSystemCode(businessSystem.getSystemCode());
			systemMessageLog.setCreateDate(new Date());

			businessSystem = new BusinessSystem();
			businessSystem.setSystemCode(businessSystem.getSystemCode());
			List<BusinessSystem> businessSystemList = businessSystemManager
					.queryBusinessSystemList(businessSystem);
			if (businessSystemList != null && businessSystemList.size() == 1) {
				businessSystem = businessSystemList.get(0);
			}

			List<SystemConfigUser> systemMessageConfigList = messageConfigManager.querySystemConfigUserListByBusiSys(businessSystem);

			if (systemMessageConfigList != null
					&& systemMessageConfigList.size() > 0) {
				systemMessage = "[" + businessSystem.getSystemDesc() + "系统 "
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
						+ "]主数据平台短信告警信息：" + systemMessage;
				systemMessage = systemMessage.length() <= 500 ? systemMessage
						: systemMessage.substring(0, 500);
				String outXml = WsClientUtil
						.sendMessageOnOip(
								messageConfigManager
										.getTelephoneNumberInfo(systemMessageConfigList),
								systemMessage);
				if (!StrUtil.isEmpty(outXml)) {
					outXml = outXml.length() <= 500 ? outXml : outXml
							.substring(0, 500);
					if (outXml.equals(WsConstants.RONDOW_RESULT_SUCCESS)) {
						systemMessageLog.setResult(1L);
						systemMessageLog.setSystemMessageInfo(systemMessage);
					} else if (outXml.equals(WsConstants.RONDOW_RESULT_FAIL)) {
						systemMessageLog.setResult(0L);
						systemMessageLog.setSystemMessageInfo(systemMessage);
					} else {
						systemMessage = "["
								+ businessSystem.getSystemDesc()
								+ "系统 "
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss")
								+ "]主数据平台短信告警信息：" + outXml;
						systemMessage = systemMessage.length() <= 500 ? systemMessage
								: systemMessage.substring(0, 500);
						systemMessageLog.setResult(0L);
						systemMessageLog.setErrCode(outXml);
						systemMessageLog.setSystemMessageInfo(systemMessage);
					}
				} else {
					systemMessageLog.setResult(0L);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				}
				if (!StrUtil.isEmpty(exception)) {
					exception = exception.length() <= 500 ? exception
							: exception.substring(0, 500);
					systemMessageLog.setErrCode(exception);
				}
			} else {
				systemMessageLog.setResult(0L);
				systemMessageLog.setSystemMessageInfo("["
						+ businessSystem.getSystemDesc() + "系统 "
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
						+ "]主数据平台短信告警信息：未配置短信告警手机号码，请配置！");
			}
			this.saveSystemMessageLog(systemMessageLog);
		}
	}

	/**
	 * 保存短信日志
	 */
	@Override
	public void saveSystemMessageLog(BusinessSystem businessSystem,
			String exception, String systemMessage) {
		businessSystem.setSystemCode(WsConstants.SYSTEM_CODE_UOM); // 主数据平台

		businessSystem = new BusinessSystem();
		businessSystem.setSystemCode(businessSystem.getSystemCode());
		List<BusinessSystem> businessSystemList = businessSystemManager
				.queryBusinessSystemList(businessSystem);
		if (businessSystemList != null && businessSystemList.size() == 1) {
			businessSystem = businessSystemList.get(0);
		}

		List<SystemConfigUser> systemMessageConfigList = messageConfigManager.querySystemConfigUserListByBusiSys(businessSystem);

		if (systemMessageConfigList != null
				&& systemMessageConfigList.size() > 0) {
			systemMessageLog = new SystemMessageLog();
			systemMessageLog.setSystemCode(businessSystem.getSystemCode());
			systemMessageLog.setCreateDate(new Date());
			systemMessage = "[" + businessSystem.getSystemDesc() + "系统 "
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ "]主数据平台短信告警信息：" + systemMessage;
			systemMessage = systemMessage.length() <= 500 ? systemMessage
					: systemMessage.substring(0, 500);
			String outXml = WsClientUtil.sendMessageOnOip(
					messageConfigManager
							.getTelephoneNumberInfo(systemMessageConfigList),
					systemMessage);
			// String outXml = "1";
			if (!StrUtil.isEmpty(outXml)) {
				outXml = outXml.length() <= 500 ? outXml : outXml.substring(0,
						500);
				if (outXml.equals(WsConstants.RONDOW_RESULT_SUCCESS)) {
					systemMessageLog.setResult(1L);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				} else if (outXml.equals(WsConstants.RONDOW_RESULT_FAIL)) {
					systemMessageLog.setResult(0L);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				} else {
					systemMessage = "["
							+ businessSystem.getSystemDesc()
							+ "系统 "
							+ DateUtil.dateToStr(new Date(),
									"yyyy-MM-dd HH:mm:ss") + "]主数据平台短信告警信息："
							+ outXml;
					systemMessage = systemMessage.length() <= 500 ? systemMessage
							: systemMessage.substring(0, 500);
					systemMessageLog.setResult(0L);
					systemMessageLog.setErrCode(outXml);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				}
			} else {
				systemMessageLog.setResult(0L);
				systemMessageLog.setSystemMessageInfo(systemMessage);
			}
			if (!StrUtil.isEmpty(exception)) {
				exception = exception.length() <= 500 ? exception : exception
						.substring(0, 500);
				systemMessageLog.setErrCode(exception);
			}
			this.saveSystemMessageLog(systemMessageLog);
		} else {
			systemMessageLog = new SystemMessageLog();
			systemMessageLog.setResult(0L);
			systemMessageLog.setSystemMessageInfo("["
					+ businessSystem.getSystemDesc() + "系统 "
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ "]主数据平台短信告警信息：未配置短信告警手机号码，请配置！");
			this.saveSystemMessageLog(systemMessageLog);
		}
	}

	/**
	 * 保存短信日志,系统监控中使用
	 */
	@Override
	public boolean saveSystemMessageLog(
			BusinessSystem bs, String systemMessage) {
		boolean result = false;
		businessSystem = new BusinessSystem();
		businessSystem.setSystemCode(bs.getSystemCode());
		List<BusinessSystem> businessSystemList = businessSystemManager
				.queryBusinessSystemList(businessSystem);
		if (businessSystemList != null && businessSystemList.size() == 1) {
			businessSystem = businessSystemList.get(0);
		}

		List<SystemConfigUser> systemMessageConfigList = messageConfigManager.querySystemConfigUserListByBusiSys(bs);

		if (systemMessageConfigList != null
				&& systemMessageConfigList.size() > 0) {
			systemMessageLog = new SystemMessageLog();
			systemMessageLog.setSystemCode(bs.getSystemCode());
			systemMessageLog.setCreateDate(new Date());
			systemMessage = "[" + businessSystem.getSystemDesc() + "系统 "
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ "]主数据平台短信告警信息：" + systemMessage;
			systemMessage = systemMessage.length() <= 500 ? systemMessage
					: systemMessage.substring(0, 500);
			String outXml = WsClientUtil.sendMessageOnOip(
					messageConfigManager
							.getTelephoneNumberInfo(systemMessageConfigList),
					systemMessage);
			// String outXml = "1";
			if (!StrUtil.isEmpty(outXml)) {
				outXml = outXml.length() <= 500 ? outXml : outXml.substring(0,
						500);
				if (outXml.equals(WsConstants.RONDOW_RESULT_SUCCESS)) {
					systemMessageLog.setResult(1L);
					systemMessageLog.setSystemMessageInfo(systemMessage);
					result = true;
				} else if (outXml.equals(WsConstants.RONDOW_RESULT_FAIL)) {
					systemMessageLog.setResult(0L);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				} else {
					systemMessage = "["
							+ businessSystem.getSystemDesc()
							+ "系统 "
							+ DateUtil.dateToStr(new Date(),
									"yyyy-MM-dd HH:mm:ss") + "]主数据平台短信告警信息："
							+ outXml;
					systemMessage = systemMessage.length() <= 500 ? systemMessage
							: systemMessage.substring(0, 500);
					systemMessageLog.setResult(0L);
					systemMessageLog.setErrCode(outXml);
					systemMessageLog.setSystemMessageInfo(systemMessage);
				}
			} else {
				systemMessageLog.setResult(0L);
				systemMessageLog.setSystemMessageInfo(systemMessage);
			}
			this.saveSystemMessageLog(systemMessageLog);
		} else {
			systemMessageLog = new SystemMessageLog();
			systemMessageLog.setResult(0L);
			systemMessageLog.setSystemMessageInfo("["
					+ businessSystem.getSystemDesc() + "系统 "
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ "]主数据平台短信告警信息：未配置短信告警手机号码，请配置！");
			this.saveSystemMessageLog(systemMessageLog);

		}

		return result;
	}

}
