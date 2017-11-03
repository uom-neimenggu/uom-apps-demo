package cn.ffcs.uom.webservices.util;

import java.util.Date;

import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;

public class EsbHeadUtil {
	/**
	 * FTP通知的sender
	 */
	public static final String FTP_SENDER = "1207";
	/**
	 * CRM渠道服务编码
	 */
	public static final String FTP_SERVCODE = "1001.UOMFileTransferNotice.SynReq";

	/**
	 * CPMIS服务编码
	 */
	public static final String FTP_SERVCODE_CPMIS = "mdp.UOMFileTransferNotice.SynReq";
	/**
	 * CRM服务编码
	 */
	public static final String FTP_SERVCODE_CRM = "1001.UOMFileTransferNoticeforcrm.SynReq";

	/**
	 * 获取通知esb请求头
	 * 
	 * @return
	 */
	public static String getEsbHead(String sender, String servCode,
			String msgId, String transId) {
		Date nowDate = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append("<Esb>");
		sb.append("<Route>");
		sb.append("<Sender>");
		sb.append(sender);
		sb.append("</Sender>");
		sb.append("<Time>");
		sb.append(nowDate);
		sb.append("</Time>");
		sb.append("<ServCode>");
		sb.append(servCode);
		sb.append("</ServCode>");
		sb.append("<MsgId>");
		sb.append(msgId);
		sb.append("</MsgId>");
		sb.append("<TransId>");
		sb.append(transId);
		sb.append("</TransId>");
		sb.append("</Route>");
		sb.append("</Esb>");
		return sb.toString();
	}

	/**
	 * 获取Url参数
	 * 
	 * @return
	 */
	public static String getUrlParameter(boolean isRest, String sender,
			String servCode, String msgId, String transId) {
		StringBuffer sb = new StringBuffer();
		sb.append("?isRest=").append(isRest).append("&sender=").append(sender)
				.append("&servCode=").append(servCode).append("&msgId=")
				.append(msgId).append("&transactionId=").append(transId);
		return sb.toString();
	}

	/**
	 * 获取msgId
	 * 
	 * @param sender
	 * @return
	 */
	public static String getMsgId(String sender) {
		try {
			String seq = DefaultDaoFactory.getDefaultDao().getSeqNextval(
					"SEQ_ESB_MSG_ID");
			String strSeq = StrUtil.padLeading(seq, 10, "0");
			String msgId = sender + "_"
					+ DateUtil.getDateByDateFormat("yyyyMMdd", new Date())
					+ "_" + strSeq;
			return msgId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取msgId
	 * 
	 * @param sender
	 * @return
	 */
	public static String getOipHttpJsonMsgId(String sender) {
		try {
			String strSeq = PubUtil.createRandom(3);
			String msgId = sender
					+ "_"
					+ DateUtil.getDateByDateFormat("yyyyMMddHHmmssSSS",
							new Date()) + "_" + strSeq;
			return msgId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取msgId
	 * 
	 * @param sender
	 * @return
	 */
	public static String getEAMMsgId(String sender) {
		try {
			String strSeq = PubUtil.createRandom(9);
			String msgId = sender + "_" + DateUtil.getYYYYMMDD(null) + "_"
					+ strSeq;
			return msgId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取通知soap报文
	 * 
	 * @return
	 */
	public static String getSoapXml(String esb, String xml) {
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"");
		sb.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb.append("<soapenv:Header>");
		sb.append(esb);
		sb.append("</soapenv:Header>");
		sb.append("<soapenv:Body>");
		sb.append("<ns1:receiveResportHeart soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"");
		sb.append(" xmlns:ns1=\"http://webservice.Scp.oip.starit.com.cn/\">");
		sb.append("<arg0 xsi:type=\"xsd:string\">");
		sb.append(xml);
		sb.append("</arg0></ns1:receiveResportHeart>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return sb.toString();
	}
}
