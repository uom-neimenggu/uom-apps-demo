package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.SystemIntfInfoConfigDao;

/**
 * 配置-人员系统对应关系
 * 
 * @author
 * 
 **/
public class SystemBusiUser extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 人员系统对应关系标识主键
	 **/
	public Long getSysBusiUserId() {
		return super.getId();
	}

	/**
	 * 人员系统对应关系标识主键
	 **/
	public void setSysBusiUserId(Long sysBusiUserId) {
		super.setId(sysBusiUserId);
	}

	/**
	 * 接入系统标识
	 **/
	@Getter
	@Setter
	private Long businessSystemId;
	
	/**
	 * 人员标识
	 **/
	@Getter
	@Setter
	private Long systemConfigUserId;
	
	/**
	 * 排序号
	 **/
	@Getter
	@Setter
	private Long sort;
	
	/**
	 * 邮件地址
	 **/
	@Getter
	@Setter
	private String emailAddress;
	
	/**
	 * 全局管理标识
	 **/
	@Getter
	@Setter
	private String overallSituation;

	/**
	 * 获取系统短信通知配置
	 * 
	 * @return
	 */
	public SystemBusiUser getSystemConfigUser() {
		if (this.getSystemConfigUserId() != null) {
			Object object = SystemBusiUser.repository().getObject(
					SystemBusiUser.class, this.getSystemConfigUserId());
			if (object != null) {
				return (SystemBusiUser) object;
			}
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static SystemIntfInfoConfigDao repository() {
		return (SystemIntfInfoConfigDao) ApplicationContextUtil
				.getBean("systemIntfInfoConfigDao");
	}
}
