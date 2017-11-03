package cn.ffcs.uom.webservices.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.dao.IntfLogDao;

/**
 * 接口日志表实体.
 * 
 * @author
 * 
 **/
public class IntfLog implements Serializable {
	/**
	 * 日志主键.
	 **/
	@Getter
	@Setter
	private Long logId;
	/**
	 * 流水号.
	 **/
	@Getter
	@Setter
	private String transId;
	/**
	 * FTP任务实例标识.
	 **/
	@Getter
	@Setter
	private Long ftpTaskInstanceId;
	/**
	 * 消息ID.
	 **/
	@Getter
	@Setter
	private String msgId;
	/**
	 * 消息类型.
	 **/
	@Getter
	@Setter
	private String msgType;
	/**
	 * 请求系统.
	 **/
	@Getter
	@Setter
	private String system;
	/**
	 * 结果.
	 **/
	@Getter
	@Setter
	private Long result;
	/**
	 * 请求报文.
	 **/
	@Getter
	@Setter
	private String requestContent;
	/**
	 * 返回报文.
	 **/
	@Getter
	@Setter
	private String responseContent;
	/**
	 * 开始时间.
	 **/
	@Getter
	@Setter
	private Date beginDate;
	/**
	 * 结束时间.
	 **/
	@Getter
	@Setter
	private Date endDate;
	/**
	 * 消耗时间.
	 **/
	@Getter
	@Setter
	private Long consumeTime;
	/**
	 * 错误编码.
	 **/
	@Getter
	@Setter
	private String errCode;
	/**
	 * 错误描述.
	 **/
	@Getter
	@Setter
	private String errMsg;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static IntfLogDao repository() {
		return (IntfLogDao) ApplicationContextUtil.getBean("intfLogDao");
	}

	/**
	 * 
	 * @return
	 */
	public static String getSeqName() {
		return "SEQ_INTF_LOG_ID";
	}
}
