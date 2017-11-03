package cn.ffcs.uom.webservices.log;

import java.util.Date;
import java.util.Iterator;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.manager.IntfLogManager;
import cn.ffcs.uom.webservices.model.IntfLog;
import cn.ffcs.uom.webservices.util.WsUtil;

public class LogHandle extends BasicHandler {
	private static final long serialVersionUID = -4037035136294954040L;
	private Log log = LogFactory.getLog(this.getClass());
	/**
	 * 接口日志
	 */
	private IntfLogManager intfLogManager = (IntfLogManager) ApplicationContextUtil
			.getBean("intfLogManager");

	public void invoke(MessageContext msgContext) throws AxisFault {
		try {
			Handler serviceHandler = msgContext.getService();
			Message requestMsg = msgContext.getRequestMessage();
			Message responseMsg = msgContext.getResponseMessage();
			if (null == responseMsg) {
				serviceHandler.setOption("BEGIN_DATE", new Date());
			} else {
				Date beginDate = (Date) serviceHandler.getOption("BEGIN_DATE");
				String requestStr = this.getMsgBodyInfo(requestMsg);
				String responseStr = this.getMsgBodyInfo(responseMsg);
				final String msgType = WsUtil.getXmlContent(requestStr,
						"msgType");
				final String serial = WsUtil
						.getXmlContent(requestStr, "serial");
				final String from = WsUtil.getXmlContent(requestStr, "from");
				final String result = WsUtil.getXmlContent(responseStr,
						"result");
				IntfLog intfLog = new IntfLog();
				intfLog.setTransId(serial);
				intfLog.setSystem(from);
				intfLog.setMsgType(msgType);
				intfLog.setResult(new Long(result));
				intfLog.setRequestContent(requestStr);
				intfLog.setResponseContent(responseStr);
				intfLog.setBeginDate(beginDate);
				final String errCode = WsUtil.getXmlContent(responseStr, "id");
				intfLog.setErrCode(errCode);
				final String errMsg = WsUtil.getXmlContent(responseStr,
						"message");
				intfLog.setErrMsg(errMsg);
				Date endDate = new Date();
				intfLog.setEndDate(endDate);
				intfLog.setConsumeTime(Math.abs(endDate.getTime()
						- beginDate.getTime()));
				intfLogManager.saveIntfLog(intfLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getMsgBodyInfo(Message msg) {
		String info = "";
		try {
			SOAPBody body = msg.getSOAPBody();
			Iterator it = body.getChildElements();
			if (it.hasNext()) {
				Iterator it2 = ((SOAPElement) it.next()).getChildElements();
				if (it2.hasNext()) {
					info = ((SOAPElement) it2.next()).getValue();
				}
			}
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return info;
	}
}
