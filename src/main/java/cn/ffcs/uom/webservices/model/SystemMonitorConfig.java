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
public class SystemMonitorConfig extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信配置信息主键.
	 **/
	public Long getSystemMonitorConfigId() {
		return super.getId();
	}

	/**
	 * 短信配置信息主键.
	 **/
	public void setSystemMonitorConfigId(Long systemMonitorConfigId) {
		super.setId(systemMonitorConfigId);
	}

	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 * 监控事件名.
	 **/
	@Getter
	@Setter
	private String eventName;
	
	/**
	 * 监控开关.
	 **/
	@Getter
	@Setter
	private String systemMonitorSwitch;

	/**
	 * 获取短信通知开关名称.
	 **/
	public String getSystemMonitorSwitchName() {
		SystemMonitorConfig systemMonitorConfig = this.getSystemMonitorConfig();
		if (systemMonitorConfig != null) {
			if (systemMonitorConfig.getSystemMonitorSwitch().equals(
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
	public SystemMonitorConfig getSystemMonitorConfig() {
		if (this.getSystemMonitorConfigId() != null) {
			Object object = SystemMonitorConfig.repository().getObject(
					SystemMonitorConfig.class, this.getSystemMonitorConfigId());
			if (object != null) {
				return (SystemMonitorConfig) object;
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
