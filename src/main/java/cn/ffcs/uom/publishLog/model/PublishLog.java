package cn.ffcs.uom.publishLog.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.publishLog.dao.PublishLogDao;

/**
 * 发布日志实体.
 * 
 * @author faq
 * 
 **/
@SuppressWarnings("serial")
public class PublishLog extends UomEntity implements Serializable {

	/**
	 * 发布日志标识.
	 */
	public Long getPublishLogId() {
		return super.getId();
	}

	public void setPublishLogId(Long publishLogId) {
		super.setId(publishLogId);
	}

	/**
	 * 组织树标识.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;	

	/**
	 * 发布人.
	 **/
	@Getter
	@Setter
	private Long publisher;	
	/**
	 * 发布时间.
	 **/
	@Getter
	@Setter
	private Date publishDate;
	/**
	 * 树生成时间.
	 **/
	@Getter
	@Setter
	private Date genDate;
	/**
	 * 上次树生成时间.
	 **/
	@Getter
	@Setter
	private Date lastGenDate;
	/**
	 *发布信息.
	 **/
	@Getter
	@Setter
	private String publishMsg;
	
	/**
	 * 构造方法
	 */
	public PublishLog() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OperateLog
	 */
	public static PublishLog newInstance() {
		return new PublishLog();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static PublishLogDao repository() {
		return (PublishLogDao) ApplicationContextUtil.getBean("publishLogDao");
	}
	
}
