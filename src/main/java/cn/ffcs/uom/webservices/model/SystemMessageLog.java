package cn.ffcs.uom.webservices.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.dao.SystemMessageLogDao;

/**
 * 短信日志表实体.
 * 
 * @author
 * 
 **/
public class SystemMessageLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信日志主键.
	 **/
	@Getter
	@Setter
	private Long systemMessageLogId;
	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 * 结果.
	 **/
	@Getter
	@Setter
	private Long result;
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
	private String systemMessageInfo;
	/**
	 * 创建时间.
	 **/
	@Getter
	@Setter
	private Date createDate;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static SystemMessageLogDao repository() {
		return (SystemMessageLogDao) ApplicationContextUtil
				.getBean("systemMessageLogDao");
	}

	/**
	 * 
	 * @return
	 */
	public static String getSeqName() {
		return "SEQ_SYSTEM_MESSAGE_LOG_ID";
	}
}
