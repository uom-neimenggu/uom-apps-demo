package cn.ffcs.uom.webservices.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;

public class WsClientUtil {
	/**
	 * 普通使用
	 * 
	 * @param inXml
	 * @param callUrl
	 * @param method
	 * @param paramName
	 *            默认arg0
	 * @return
	 * @throws Exception
	 */
	public static String wsCall(String inXml, String callUrl, String method,
			String paramName) throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(15 * 1000));
			call.setTargetEndpointAddress(callUrl);
			call.setOperation(method);
			call.addParameter(paramName, XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);
			result = (String) call.invoke(new Object[] { inXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 有命名空间使用
	 * 
	 * @param inXml
	 * @param callUrl
	 * @param nameSpace
	 * @param method
	 * @param paramName
	 *            默认arg0
	 * @return
	 * @throws Exception
	 */
	public static String wsCall(String inXml, String callUrl, String nameSpace,
			String method, String paramName) throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(15 * 1000));
			call.setTargetEndpointAddress(callUrl);
			QName qName = new QName(nameSpace, method);
			call.setOperationName(qName);
			call.addParameter(paramName, XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);
			result = (String) call.invoke(new Object[] { inXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 从oip上调用
	 * 
	 * @param inXml
	 * @param callUrl
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public static String wsCallOnOip(String esbHead, String inXml,
			String callUrl, String method, String paramName) throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(15 * 1000));
			call.setTargetEndpointAddress(callUrl);
			call.setOperation(method);
			call.addParameter(paramName, XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);

			StringReader sr = new StringReader(esbHead);
			InputSource is = new InputSource(sr);
			Element esb = (Element) DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(is).getElementsByTagName("Esb")
					.item(0);
			call.addHeader(new SOAPHeaderElement(esb));

			result = (String) call.invoke(new Object[] { inXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 从oip上调用
	 * 
	 * @param inXml
	 * @param callUrl
	 * @param nameSpace
	 * @param method
	 * @param paramName
	 * @return
	 * @throws Exception
	 */
	public static String wsCallOnOip(String esbHead, String inXml,
			String callUrl, String nameSpace, String method, String paramName)
			throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(15 * 1000));
			call.setTargetEndpointAddress(callUrl);
			QName qName = new QName(nameSpace, method);
			call.setOperationName(qName);
			call.addParameter(paramName, XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);

			StringReader sr = new StringReader(esbHead);
			InputSource is = new InputSource(sr);
			Element esb = (Element) DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(is).getElementsByTagName("Esb")
					.item(0);
			call.addHeader(new SOAPHeaderElement(esb));

			result = (String) call.invoke(new Object[] { inXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 调增量接口
	 */
	public static String wsCallCreateIncrementalData(String callUrl,
			String method, Long treeId, Date lastDate, Date thisDate,
			String syncType) throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(3600 * 1000));
			call.setTargetEndpointAddress(callUrl);
			call.setOperation(method);
			call.addParameter("treeId", XMLType.XSD_LONG, ParameterMode.IN);
			call.addParameter("lastDate", XMLType.XSD_DATETIME,
					ParameterMode.IN);
			call.addParameter("thisDate", XMLType.XSD_DATETIME,
					ParameterMode.IN);
			call.addParameter("syncType", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("username", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);
			result = (String) call.invoke(new Object[] { treeId, lastDate,
					thisDate, syncType, WsConstants.SERVICE_USERNAME,
					WsConstants.SERVICE_PASSWORD });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 通过OIP发送短信
	 * 
	 * @param phoneNumber手机号
	 * @param message短信内容
	 * @return result
	 */
	public static String sendMessageOnOip(String phoneNumber, String message) {
		String sendMessageUrl = null;
		String inXml = null;
		String outXml = null;
		String result = null;
		String esbHead = null;
		String msgId = null;
		try {

			sendMessageUrl = UomClassProvider
					.getIntfUrl(WsConstants.OIP_SENDMESSAGE_URL);

			if (StrUtil.isEmpty(sendMessageUrl)) {
				result = "发送短信的url为空";
				return result;
			}

			inXml = createReqXml(WsConstants.OIP_BUSINESS_ID, "GESS",
					phoneNumber, message, "0");

			msgId = EsbHeadUtil.getEAMMsgId(WsConstants.OIP_SENDER);

			esbHead = EsbHeadUtil.getEsbHead(WsConstants.OIP_SENDER,
					WsConstants.OIP_SERVICE_CODE, msgId, "");

			outXml = WsClientUtil.wsCallUomOnOip(sendMessageUrl,
					WsConstants.RONDOW_METHODNAME, inXml,
					WsConstants.RONDOW_SYSNAME, WsConstants.RONDOW_INTERFACEID,
					esbHead);

			result = outXml;

		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	private static String createReqXml(String businessId, String sysName,
			String phoneNumber, String message, String sendToCustomer) {
		StringBuffer reqXml = new StringBuffer();
		reqXml.append("<BUSINESS_ID>");
		reqXml.append(businessId);
		reqXml.append("</BUSINESS_ID><SYSNAME>");
		reqXml.append(sysName);
		reqXml.append("</SYSNAME><RECV_TELNO>");
		reqXml.append(phoneNumber);
		reqXml.append("</RECV_TELNO><SM_TEXT>");
		reqXml.append(message);
		reqXml.append("</SM_TEXT><SEND_TO_CUSTOMER>");
		reqXml.append(sendToCustomer);
		reqXml.append("</SEND_TO_CUSTOMER>");

		return reqXml.toString();
	}

	/**
	 * 通过OIP调用短信平台发送短信
	 * 
	 * @param url
	 * @param methodName
	 * @param inXML
	 * @param sysName
	 * @param interfaceid
	 * @return
	 */
	public static String wsCallUomOnOip(String url, String methodName,
			String inXML, String sysName, String interfaceid, String esbHead) {
		String outStr = "";
		String flumeLogerrorMsg = null;
		try {
			// String endpoint = "" + url;

			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setOperationName(methodName);
			// call.setOperationName(new QName("" + url, "" + methodName));
			call.setTargetEndpointAddress(url);
			// call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.addParameter("sysname", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("interfaceid", XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter("xml", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			call.setTimeout(new Integer(10000));

			StringReader sr = new StringReader(esbHead);
			InputSource is = new InputSource(sr);
			Element esb = (Element) DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(is).getElementsByTagName("Esb")
					.item(0);
			call.addHeader(new SOAPHeaderElement(esb));

			outStr = (String) call.invoke(new Object[] { sysName, interfaceid,
					inXML });

		} catch (Exception ex) {
			outStr += ex.getMessage();
			ex.printStackTrace();

		}
		return "" + outStr;
	}

	/**
	 * 调增eam解锁接口
	 */
	public static String wsCallStaffUnLock(String callUrl, String method,
			String reqXml) throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(3600 * 1000));
			call.setTargetEndpointAddress(callUrl);
			call.setOperation(method);
			call.addParameter("reqXml", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);
			result = (String) call.invoke(new Object[] { reqXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 
	 */
	public static String wsCallUtil(String callUrl, String method, String reqXml)
			throws Exception {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(3600 * 1000));
			call.setTargetEndpointAddress(callUrl);
			call.setOperation(method);
			call.addParameter("reqXml", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING, String.class);
			result = (String) call.invoke(new Object[] { reqXml });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 调国政通接口
	 */
	public static String wsCallIdCheck(String callUrl,
			String method, String msgId,String msgName,String seriNo) throws Exception {
		Service service = new Service();
		String result = null;
		Call call = (Call) service.createCall();
		call.setTimeout(new Integer(15 * 1000));
		call.setTargetEndpointAddress(callUrl);
		call.setOperation(method);
		call.addParameter("msgId", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("msgName", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("seriNo", XMLType.XSD_STRING, ParameterMode.IN);
		call.setReturnType(XMLType.XSD_STRING, String.class);
		result = (String) call.invoke(new Object[] {msgId,msgName,seriNo});
		return result;
	}
	
	
	public static String test(){
		String staffUnLockServiceUrl = "http://134.64.116.90:9021/eam-apps/services/passwordServices";
		try {
			String resultXml = wsCallStaffUnLock(staffUnLockServiceUrl,"unlock",getReqXml("H8484944"));
			return resultXml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}
	public static String getReqXml(String staffAccount) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String currTime = sdf.format(new Date());
		StringBuffer reqXmlBuffer = new StringBuffer();
		reqXmlBuffer
				.append("<root><sessionHeader><serviceCode>EAM130013</serviceCode><!--固定值--><transactionID>");
		reqXmlBuffer.append(currTime).append("0");
		reqXmlBuffer
				.append("</transactionID><!--【8位日期编码YYYYMMDD】＋【10位随机数】--><clientId>test1</clientId><!--系统编码--></sessionHeader><sessionBody><unlockStaff><staffAccount>");
		reqXmlBuffer.append(staffAccount);
		reqXmlBuffer
				.append("</staffAccount><!--需要解锁的账号--></unlockStaff></sessionBody></root>");
		return reqXmlBuffer.toString();
	}
}
