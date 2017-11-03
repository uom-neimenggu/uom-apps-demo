/**
 * 
 */
package cn.ffcs.uom.publishLog.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 下发查询 实体
 * 
 * @author wenyaopeng
 *
 */
@SuppressWarnings("serial")
public class PublishQuery extends UomEntity implements Serializable{
	@Getter
	@Setter
	private String treeId;
	@Getter
	@Setter
	private String ftpTaskInstanceId;
	@Getter
	@Setter
	private String intfTaskInstanceId;
	@Getter
	@Setter
	private String syncType;
	
	public String getSyncTypeName() {
		return "1".equals(this.syncType)?"全量":"增量";
	}
	@Getter
	@Setter
	private String systemDesc;
	@Getter
    @Setter
	private String  msgType;
	@Getter
    @Setter
	private String targetSystem;
	@Getter
    @Setter
	private String invokeResule;
	@Getter
    @Setter
	private String invokeTimes;
	@Getter
    @Setter
	private Timestamp lastDate;
	@Getter
    @Setter
	private Timestamp thisDate;
	@Getter
    @Setter
	private String remotePath;
	@Getter
    @Setter
	private String noticePath;
	
	
	public PublishQuery(){
		super();
	}
	
	/**
	 * 创建对象实例.
	 * 
	 * @return
	 */
	public static PublishQuery newInstance(){
		return new PublishQuery();
	}
}
