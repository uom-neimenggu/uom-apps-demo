package cn.ffcs.uom.webservices.services;

import java.lang.reflect.Method;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.manager.UomManager;
import cn.ffcs.uom.webservices.util.WsUtil;

public class UomWsService {

	private static String MSG_TYPE = "msgType";

	private UomManager uomManager = (UomManager) ApplicationContextUtil
			.getBean("uomManager");

	/**
	 * uom服务接口
	 * 
	 * @param inXml
	 * @return
	 */
	public String exchange(String inXml) {
		String outXml = "";
		String msgType = WsUtil.getXmlContent(inXml, MSG_TYPE);
		try {
			if (StrUtil.isNullOrEmpty(msgType)) {
				throw new Exception("msgType 不能为空!");
			}
			Method method = null;
			try {
				method = uomManager.getClass().getMethod(msgType, String.class);
			} catch (Exception e) {
				throw new Exception("msgType: " + msgType + " 未约定!");
			}
			outXml = (String) method.invoke(uomManager, inXml);
		} catch (Exception e) {
			outXml = WsUtil.getOutXmlByException(inXml, e);
		}
		return outXml;
	}
}
