package cn.ffcs.uom.webservices.util;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;

@SuppressWarnings("unchecked")
public final class WsUtil {
	/**
	 * 把msgHead和msgBody打包成报文 .
	 * 
	 */
	public static String toXml(final Class<?> xsdClass, Object msgHead,
			final Object msgBodyChild) throws Exception {

		// 取msgBodyChild的类名, 如: Info, InParam, OutParam
		String msgBodyChildName = msgBodyChild.getClass().getSimpleName();
		// 首字母改成小写, 如: info, inParam, outParam
		msgBodyChildName = msgBodyChildName.substring(0, 1).toLowerCase()
				+ msgBodyChildName.substring(1);

		final Object root = xsdClass.newInstance();

		// 二手服务调用, msgHead的处理...
		final Class<?> msgHeadClass = PropertyUtils.getPropertyType(root,
				"msgHead");
		if (msgHead.getClass() != msgHeadClass) {
			final Object dest = msgHeadClass.newInstance();
			PropertyUtils.copyProperties(dest, msgHead);
			msgHead = dest;
		}

		PropertyUtils.setSimpleProperty(root, "msgHead", msgHead);
		final Object msgBody = PropertyUtils.getPropertyType(root, "msgBody")
				.newInstance();
		PropertyUtils
				.setSimpleProperty(msgBody, msgBodyChildName, msgBodyChild);
		PropertyUtils.setSimpleProperty(root, "msgBody", msgBody);
		final Writer w = new java.io.StringWriter();
		org.exolab.castor.xml.Marshaller.marshal(root, w);
		final String inXml = delNameSpace(w.toString());

		return inXml;
	}

	/**
	 * 删除命名空间
	 * 
	 * @param s
	 * @return
	 */
	public static String delNameSpace(String s) {
		if (s == null) {
			return null;
		}
		final String regEx = "\\sxmlns(|:ns\\d+)=\"http://[^\"]+\"|ns\\d+:";

		final Pattern p = Pattern.compile(regEx);
		final Matcher m = p.matcher(s);
		s = m.replaceAll("");
		return s;
	}

	/**
	 * 截取xml字符串
	 * 
	 * @param inXml
	 * @param tagName
	 * @return
	 */
	public static String getXmlContent(final String inXml, final String tagName) {
		if (StrUtil.isNullOrEmpty(tagName) || StrUtil.isNullOrEmpty(inXml)) {
			return "";
		}
		int startIndex = -1;
		int endIndex = -1;
		final String str_maskStartStr = "<" + tagName + ">";
		final String str_maskEndStr = "</" + tagName + ">";

		if (inXml != null) {
			startIndex = inXml.indexOf(str_maskStartStr);
			endIndex = inXml.indexOf(str_maskEndStr);
			if (startIndex != -1 && endIndex != -1) {
				final int contentStart = inXml.indexOf('>', startIndex) + 1;
				return inXml.substring(contentStart, endIndex);
			}
		}
		return "";
	}

	/**
	 * 拼接异常报文
	 * 
	 * @param inXml
	 * @param e
	 * @return
	 */
	public static String getOutXmlByException(String inXml, Exception e) {
		final String msgType = WsUtil.getXmlContent(inXml, "msgType");
		final String sysSign = WsUtil.getXmlContent(inXml, "sysSign");
		final String serial = WsUtil.getXmlContent(inXml, "serial");
		final String to = WsUtil.getXmlContent(inXml, "from");
		final String time = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				new Date());
		final StringBuffer sb = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root><msgHead><time>");
		sb.append(time);
		sb.append("</time><from>");
		sb.append(WsConstants.SYSTEM_CODE_UOM);
		sb.append("</from><to>");
		sb.append(to);
		sb.append("</to><sysSign>");
		sb.append(sysSign);
		sb.append("</sysSign><msgType>");
		sb.append(msgType);
		sb.append("</msgType><serial>");
		sb.append(serial);
		sb.append("</serial></msgHead><msgBody><outParam><result>");
		sb.append(WsConstants.FAILED);
		sb.append("</result>");
		sb.append("<error><id>");
		sb.append("1001");
		sb.append("</id><message>");
		sb.append(WsUtil.getSupperErrorMsg(e).getMessage());
		sb.append("</message></error>");
		sb.append("</outParam></msgBody></root>");
		return sb.toString();
	}

	/**
	 * 获取最上层异常
	 * 
	 * @param e
	 * @return
	 */
	public static Throwable getSupperErrorMsg(final Throwable e) {
		if (e instanceof InvocationTargetException) {
			final InvocationTargetException invoExce = (InvocationTargetException) e;
			if (invoExce.getTargetException() != null) {
				return WsUtil.getSupperErrorMsg(invoExce.getTargetException());
			} else {
				return e;
			}
		} else {
			return e;
		}
	}
}
