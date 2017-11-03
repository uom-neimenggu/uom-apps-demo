package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.SystemIntfInfoConfigDao;

/**
 * 配置-人员基本信息表
 * 
 * @author
 * 
 **/
public class SystemConfigUser extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信配置信息主键.
	 **/
	public Long getSystemConfigUserId() {
		return super.getId();
	}

	/**
	 * 短信配置信息主键.
	 **/
	public void setSystemConfigUserId(Long systemConfigUserId) {
		super.setId(systemConfigUserId);
	}

	/**
	 * 姓名.
	 **/
	@Getter
	@Setter
	private String userName;
	/**
	 * 移动电话.
	 **/
	@Getter
	@Setter
	private String telephoneNumber;
	
	/**
	 * 主数据工号.
	 **/
	@Getter
	@Setter
	private String uomAcct;
	
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
	
	public String getOverallSituationName() {
		String returnString = "";
		if("1".equals(this.overallSituation)) {
			returnString = "是";
		} else {
			returnString = "否";
		}
		
		return returnString;
	}

	/**
	 * 获取系统短信通知配置
	 * 
	 * @return
	 */
	public SystemConfigUser getSystemConfigUser() {
		if (this.getSystemConfigUserId() != null) {
			Object object = SystemConfigUser.repository().getObject(
					SystemConfigUser.class, this.getSystemConfigUserId());
			if (object != null) {
				return (SystemConfigUser) object;
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
