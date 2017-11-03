package cn.ffcs.uom.webservices.util;

import java.util.Date;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.impl.SystemMessageLogManagerImpl;
import cn.ffcs.uom.webservices.model.IntfLog;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

public class IntfLogBuilder {

	/**
	 * 获取正常返回的日志对象
	 * 
	 * @param root
	 * @param beginDate
	 * @param endDate
	 * @param inXml
	 * @param outXml
	 * @return
	 */
	public static IntfLog getFtpInformIntfLog(
			cn.ffcs.uom.webservices.bean.ftpinform.Root requestRoot,
			cn.ffcs.uom.webservices.bean.ftpinform.Root responseRoot,
			Date beginDate, Date endDate, String inXml, String outXml,
			IntfTaskInstance intfTaskInstance) {
		IntfLog intfLog = new IntfLog();
		/**
		 * 短信配置
		 */
		SystemMessageLogManagerImpl systemMessageLogManagerImpl = new SystemMessageLogManagerImpl();
		/**
		 * 查询条件
		 */
		BusinessSystem businessSystem = new BusinessSystem();

		if (requestRoot != null && requestRoot.getMsgHead() != null) {
			intfLog.setMsgType(requestRoot.getMsgHead().getMsgType());
			intfLog.setTransId(requestRoot.getMsgHead().getSerial());
			intfLog.setSystem(requestRoot.getMsgHead().getTo());
			businessSystem = new BusinessSystem();
			businessSystem.setSystemCode(requestRoot.getMsgHead().getTo());
		}
		intfLog.setRequestContent(inXml);
		intfLog.setResponseContent(outXml);
		intfLog.setBeginDate(beginDate);
		intfLog.setEndDate(endDate);
		intfLog.setConsumeTime(Math.abs(endDate.getTime() - beginDate.getTime()));
		if (responseRoot != null
				&& responseRoot.getMsgBody() != null
				&& responseRoot.getMsgBody().getOutParam() != null
				&& !StrUtil.isEmpty(responseRoot.getMsgBody().getOutParam()
						.getResult())) {
			intfLog.setResult(Long.parseLong(responseRoot.getMsgBody()
					.getOutParam().getResult()));
			if (WsConstants.FAILED.equals(responseRoot.getMsgBody()
					.getOutParam().getResult())) {
				cn.ffcs.uom.webservices.bean.ftpinform.Error error = responseRoot
						.getMsgBody().getOutParam().getError();
				if (error != null) {
					if (!StrUtil.isEmpty(error.getId())) {
						intfLog.setErrCode(error.getId());
					}
					if (!StrUtil.isEmpty(error.getMessage())) {
						intfLog.setErrMsg(error.getMessage());
						systemMessageLogManagerImpl.saveSystemMessageLog(
								businessSystem, null, error.getMessage(),
								intfTaskInstance);
					}
				}
			}
		}
		return intfLog;
	}

	/**
	 * 返回ftp信息通知接口异常的日志对象
	 * 
	 * @param root
	 * @param beginDate
	 * @param endDate
	 * @param inXml
	 * @param outXml
	 * @return
	 */
	public static IntfLog getFtpInformExceptionIntfLog(
			cn.ffcs.uom.webservices.bean.ftpinform.Root root, Date beginDate,
			Date endDate, String inXml, String outXml, Exception e) {
		IntfLog intfLog = new IntfLog();
		if (root != null && root.getMsgHead() != null) {
			intfLog.setMsgType(root.getMsgHead().getMsgType());
			intfLog.setTransId(root.getMsgHead().getSerial());
			intfLog.setSystem(root.getMsgHead().getTo());
		}
		intfLog.setRequestContent(inXml);
		intfLog.setResponseContent(outXml);
		intfLog.setBeginDate(beginDate);
		intfLog.setEndDate(endDate);
		intfLog.setConsumeTime(Math.abs(endDate.getTime() - beginDate.getTime()));
		intfLog.setResult(Long.parseLong(WsConstants.FAILED));
		if (e.getMessage() != null && e.getMessage().length() > 100) {
			/**
			 * 防止长度过长
			 */
			intfLog.setErrMsg(e.getMessage().substring(0, 100));
		} else {
			intfLog.setErrMsg(e.getMessage());
		}
		return intfLog;
	}

	/**
	 * 返回ftp信息通知接口异常的日志对象
	 * 
	 * @param root
	 * @param beginDate
	 * @param endDate
	 * @param inXml
	 * @param outXml
	 * @return
	 */
	public static IntfLog getCheckIdIntfLog(
			String idNum, Date beginDate,
			Date endDate, String idName, String outXml, Exception e) {
		IntfLog intfLog = new IntfLog();
		intfLog.setRequestContent(idName+"||"+idNum);
		intfLog.setResponseContent(outXml);
		intfLog.setBeginDate(beginDate);
		intfLog.setEndDate(endDate);
		intfLog.setMsgType("实名校验");
		intfLog.setConsumeTime(Math.abs(endDate.getTime() - beginDate.getTime()));
		intfLog.setResult(Long.parseLong(WsConstants.FAILED));
		if (e.getMessage() != null && e.getMessage().length() > 100) {
			/**
			 * 防止长度过长
			 */
			intfLog.setErrMsg(e.getMessage().substring(0, 100));
		} else {
			intfLog.setErrMsg(e.getMessage());
		}
		return intfLog;
	}
}
