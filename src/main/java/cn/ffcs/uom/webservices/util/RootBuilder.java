package cn.ffcs.uom.webservices.util;

import java.util.Date;

import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.webservices.bean.comm.MsgHead;
import cn.ffcs.uom.webservices.constants.WsConstants;

public class RootBuilder {
	/**
	 * 生成头
	 * 
	 * @param msgType
	 * @param from
	 * @param to
	 * @param beginDate
	 * @return
	 */
	public static MsgHead getMsgHead(String msgType, String from, String to,
			Date beginDate) {
		MsgHead msgHead = new MsgHead();
		msgHead.setFrom(from);
		msgHead.setTo(to);
		msgHead.setMsgType(msgType);
		msgHead.setSerial(DefaultDaoFactory.getDefaultDao().genTransId(
				DateUtil.getDateByDateFormat("yyyyMMdd", beginDate), 18,
				"SEQ_INTF_TRANS"));
		msgHead.setTime(DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				beginDate));
		msgHead.setSysSign("");
		return msgHead;
	}
	
	/**
	 * 生成返回头
	 * 
	 * @param msgType
	 * @param from
	 * @param to
	 * @param beginDate
	 * @return
	 */
	public static MsgHead getResponseMsgHead(MsgHead reqMsgHead,
			Date responseDate) {
		MsgHead msgHead = new MsgHead();
		msgHead.setFrom(WsConstants.SYSTEM_CODE_UOM);
		msgHead.setTo(reqMsgHead.getFrom());
		msgHead.setMsgType(reqMsgHead.getMsgType());
		msgHead.setSerial(reqMsgHead.getSerial());
		msgHead.setTime(DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				responseDate));
		msgHead.setSysSign("");
		return msgHead;
	}
}
