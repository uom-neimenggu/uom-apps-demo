package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.SystemIntfInfoConfigDao;

/**
 * 短信接口信息配置表.
 * 
 * @author
 * 
 **/
public class SystemMessageConfig extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信配置信息主键.
	 **/
	public Long getSystemMessageConfigId() {
		return super.getId();
	}

	/**
	 * 短信配置信息主键.
	 **/
	public void setSystemMessageConfigId(Long systemMessageConfigId) {
		super.setId(systemMessageConfigId);
	}

	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 * 联系人姓名.
	 **/
	@Getter
	@Setter
	private String contactName;
	/**
	 * 联系电话.
	 **/
	@Getter
	@Setter
	private String telephoneNumber;
	/**
	 * 短信通知开关.
	 **/
	@Getter
	@Setter
	private String systemMessageSwitch;
	/**
	 * 通知顺序.
	 **/
	@Getter
	@Setter
	private Long noticeOrder;
	/**
	 * 邮件地址.
	 **/
	@Getter
	@Setter
	private String emailAddress;

	/**
	 * 获取短信通知开关名称.
	 **/
	public String getSystemMessageSwitchName() {
		SystemMessageConfig systemMessageConfig = this.getSystemMessageConfig();
		if (systemMessageConfig != null) {
			if (systemMessageConfig.getSystemMessageSwitch().equals(
					WsConstants.SYSTEM_MESSAGE_SWITCH_OPEN)) {
				return "开";
			} else {
				return "关";
			}
		}
		return "";
	}

	/**
	 * 获取系统短信通知配置
	 * 
	 * @return
	 */
	public SystemMessageConfig getSystemMessageConfig() {
		if (this.getSystemMessageConfigId() != null) {
			Object object = SystemMessageConfig.repository().getObject(
					SystemMessageConfig.class, this.getSystemMessageConfigId());
			if (object != null) {
				return (SystemMessageConfig) object;
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
