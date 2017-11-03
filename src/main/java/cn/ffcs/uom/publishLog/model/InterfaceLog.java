/**
 * 
 */
package cn.ffcs.uom.publishLog.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wenyaopeng
 *
 */
public class InterfaceLog {

	@Getter
	@Setter
	private int logId;
	
	@Getter
	@Setter
	private String transId;
	
	@Getter
	@Setter
	private String msgType;
	
	@Getter
	@Setter
	private String system;
	
	@Getter
	@Setter
	private int result;
	
	@Getter
	@Setter
	private String requestContent;
	
	@Getter
	@Setter
	private String responseContent;
	
	@Getter
	@Setter
	private java.sql.Timestamp beginDate;
	
	@Getter
	@Setter
	private java.sql.Timestamp endDate;
	
	@Getter
	@Setter
	private int consumeTime;
	
	@Getter
	@Setter
	private String errCode;
	
	@Getter
	@Setter
	private String errMsg;
	
	@Getter
	@Setter
	private int ftpTaskInstanceId;
	
	@Getter
	@Setter
	private String msgId;
	
	public InterfaceLog() {
		super();
	}
	
	/**
	 * 创建对象实例.
	 * 
	 * @return
	 */
	public static InterfaceLog newInstance(){
		return new InterfaceLog();
	}
	
}
