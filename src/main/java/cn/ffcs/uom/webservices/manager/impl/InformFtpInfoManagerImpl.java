package cn.ffcs.uom.webservices.manager.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.bean.comm.MsgHead;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.InformFtpInfoManager;
import cn.ffcs.uom.webservices.manager.IntfLogManager;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.IntfLog;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;
import cn.ffcs.uom.webservices.util.CastorParser;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;
import cn.ffcs.uom.webservices.util.IntfLogBuilder;
import cn.ffcs.uom.webservices.util.RootBuilder;
import cn.ffcs.uom.webservices.util.WsClientUtil;
import cn.ffcs.uom.webservices.util.WsUtil;

@Service("informFtpInfoManager")
@Scope("prototype")
public class InformFtpInfoManagerImpl implements InformFtpInfoManager {
	/**
	 * 日志记录器
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 短信配置
	 */
	@Resource
	private SystemMessageLogManager systemMessageLogManager;
	/**
	 * 查询条件
	 */
	private BusinessSystem businessSystem;
	/**
	 * 接口日志
	 */
	@Resource
	private IntfLogManager intfLogManager;
	/**
	 * 系统接口配置
	 */
	@Resource
	private SystemIntfInfoConfigManager systemIntfInfoConfigManager;

	/**
	 * 将所有接口调用方法整合成调用同一个方法  朱林涛 2016-02-19
	 */
	@Override
	public cn.ffcs.uom.webservices.bean.ftpinform.Root informFtpInfo(
			IntfTaskInstance intfTaskInstance) {
		Date beginDate = new Date();
		// 消息ID--用于保存到接口日志表中，方便和OIP平台联调
		String msgId = EsbHeadUtil.getMsgId(EsbHeadUtil.FTP_SENDER);
		String inXml = intfTaskInstance.getRequestContent();
		String outXml = "";
		// final String msgType = "informFtpInfo";
		cn.ffcs.uom.webservices.bean.ftpinform.Root root = null;
		cn.ffcs.uom.webservices.bean.ftpinform.Root result = null;
		try {
			root = (cn.ffcs.uom.webservices.bean.ftpinform.Root) CastorParser
					.toObject(inXml,
							cn.ffcs.uom.webservices.bean.ftpinform.Root.class);
			businessSystem = new BusinessSystem();
			businessSystem.setSystemCode(root.getMsgHead().getTo());
			String systemCode = null;
			if (root != null && root.getMsgHead() != null) {
				systemCode = root.getMsgHead().getTo();
			}
			if (StrUtil.isEmpty(systemCode)) {
				throw new Exception(
						"IntfTaskInstance request_content->msgHead:to is null value");
			}

			SystemIntfInfoConfig systemIntfInfoConfig = systemIntfInfoConfigManager
					.querySystemIntfInfoConfigBySystemCode(systemCode);
			if (systemIntfInfoConfig == null) {
				throw new Exception(
						"cant not get SystemIntfInfoConfig by systemCode:"
								+ systemCode);
			}

			String syncType = null;
			if (root.getMsgBody() != null
					&& root.getMsgBody().getInParam() != null
					&& !StrUtil.isEmpty(root.getMsgBody().getInParam()
							.getSyncType())) {
				syncType = root.getMsgBody().getInParam().getSyncType();
			}
			if (StrUtil.isEmpty(syncType)) {
				throw new Exception("cant not get syncType:" + systemCode);
			}

			/**
			 * 获取开关状态
			 */
			boolean intfSwitch = false;

			if (WsConstants.SYNC_ALL.equals(syncType)) {
				intfSwitch = WsConstants.INTF_SWITCH_OPEN
						.equals(systemIntfInfoConfig.getIntfSwitchAll());
			} else if (WsConstants.SYNC_PART.equals(syncType)) {
				intfSwitch = WsConstants.INTF_SWITCH_OPEN
						.equals(systemIntfInfoConfig.getIntfSwitchIncrease());
			}

			if (!intfSwitch) {
				throw new Exception("IntfSwitch closed. SystemCode:"
						+ systemCode);
			}

			/**
			 * 获取OIP地址
			 */
			String oipUrl = systemIntfInfoConfig.getOipUrl();
			if (StrUtil.isEmpty(oipUrl)) {
				throw new Exception("oipUrl is null. SystemCode:" + systemCode);
			}

			/**
			 * 获取接口地址-针对CRM和CRM渠道设置的-直接调用下游系统接口地址，不通过OIP
			 */
			String url = systemIntfInfoConfig.getIntfUrl();
			if (!StrUtil.isEmpty(url)) {
				oipUrl = url;
			}

			logger.info(systemIntfInfoConfig.getSystemCode()
					+ "[informFtpInfo]: " + oipUrl);

			/**
			 * 获取命名空间
			 */
			String nameSpace = systemIntfInfoConfig.getNameSpace();

			/**
			 * 获取方法名
			 */
			String operationName = systemIntfInfoConfig.getOperationName();
			if (StrUtil.isEmpty(operationName)) {
				throw new Exception("operationName is null. SystemCode:"
						+ systemCode);
			}
			/**
			 * 获取参数名
			 */
			String paramName = systemIntfInfoConfig.getParamName();
			if (StrUtil.isEmpty(paramName)) {
				throw new Exception("paramName is null. SystemCode:"
						+ systemCode);
			}
			/**
			 * 获取OIP服务编码
			 */
			String serviceCode = systemIntfInfoConfig.getServiceCode();
			if (StrUtil.isEmpty(serviceCode)) {
				throw new Exception("serviceCode is null. SystemCode:"
						+ systemCode);
			}
			/**
			 * 生成请求头：重新生成序列号和请求时间
			 */
			MsgHead msgHead = RootBuilder.getMsgHead(
					intfTaskInstance.getMsgType(), WsConstants.SYSTEM_CODE_UOM,
					systemCode, beginDate);
			root.setMsgHead(msgHead);

			inXml = CastorParser.toXML(root);
			/**
			 * 删除命名空间
			 */
			inXml = WsUtil.delNameSpace(inXml);

			String transId = root.getMsgHead().getSerial();

			// 通过OIP调用接口
			if (StrUtil.isEmpty(nameSpace)) {
				System.out
						.println("4、第"
								+ intfTaskInstance.getInvokeTimes()
								+ "次OIP开始调用下游系统接口"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
				outXml = WsClientUtil.wsCallOnOip(EsbHeadUtil.getEsbHead(
						EsbHeadUtil.FTP_SENDER, serviceCode, msgId, transId),
						inXml, oipUrl, operationName, paramName);
				System.out
						.println("4、第"
								+ intfTaskInstance.getInvokeTimes()
								+ "次OIP结束调用下游系统接口"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
			} else {
				System.out
						.println("4、第"
								+ intfTaskInstance.getInvokeTimes()
								+ "次OIP开始调用下游系统接口"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
				outXml = WsClientUtil.wsCallOnOip(EsbHeadUtil.getEsbHead(
						EsbHeadUtil.FTP_SENDER, serviceCode, msgId, transId),
						inXml, oipUrl, nameSpace, operationName, paramName);
				System.out
						.println("4、第"
								+ intfTaskInstance.getInvokeTimes()
								+ "次OIP结束调用下游系统接口"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
			}

			try {
				result = (cn.ffcs.uom.webservices.bean.ftpinform.Root) CastorParser
						.toObject(
								outXml,
								cn.ffcs.uom.webservices.bean.ftpinform.Root.class);
			} catch (Exception e) {
				throw new Exception("返回参数格式错误");
			}
			Date endDate = new Date();
			IntfLog intfLog = IntfLogBuilder.getFtpInformIntfLog(root, result,
					beginDate, endDate, inXml, outXml, intfTaskInstance);
			intfLog.setFtpTaskInstanceId(intfTaskInstance
					.getFtpTaskInstanceId());
			intfLog.setMsgId(msgId);
			intfLogManager.saveIntfLog(intfLog);
			return result;
		} catch (Exception e) {
			logger.info("Exception:" + e.getMessage());
			Date endDate = new Date();
			IntfLog intfLog = IntfLogBuilder.getFtpInformExceptionIntfLog(root,
					beginDate, endDate, inXml, outXml, e);
			systemMessageLogManager.saveSystemMessageLog(businessSystem,
					null, e.getMessage(), intfTaskInstance);
			intfLog.setFtpTaskInstanceId(intfTaskInstance
					.getFtpTaskInstanceId());
			intfLog.setMsgId(msgId);
			intfLogManager.saveIntfLog(intfLog);
		}
		return null;
	}
}
