package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.SystemMonitorConfigDao;

/**
 * 短信接口信息配置表.
 * 
 * @author
 * 
 **/
public class SystemMonitorConfigFilter extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信配置信息主键.
	 **/
	public Long getSystemMonitorFilterId() {
		return super.getId();
	}

	/**
	 * 短信配置信息主键.
	 **/
	public void setSystemMonitorFilterId(Long systemMonitorFilterId) {
		super.setId(systemMonitorFilterId);
	}
    
	/**
	 * 监控标识
	 **/
	@Getter
	@Setter
	private Long systemMonitorConfigId;
	/**
	 * 条件名称
	 **/
	@Getter
	@Setter
	private String filterName;
	/**
	 * 关系运算符
	 **/
	@Getter
	@Setter
	private String relationOperator;
	/**
	 * 条件值
	 **/
	@Getter
	@Setter
	private String filterValue;
	
	/**
	 * 监控条件开关.
	 **/
	@Getter
	@Setter
	private String systemMonitorFilterSwitch;
	/**
	 * 获取短信通知开关名称.
	 **/
	public String getSystemMonitorFilterSwitchName() {
		SystemMonitorConfigFilter systemMonitorConfigFilter = this.getSystemMonitorConfigFilter();
		if (systemMonitorConfigFilter != null) {
			if (systemMonitorConfigFilter.getSystemMonitorFilterSwitch().equals(
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
	public SystemMonitorConfigFilter getSystemMonitorConfigFilter() {
		if (this.getSystemMonitorFilterId() != null) {
			Object object = SystemMonitorConfigFilter.repository().getObject(SystemMonitorConfigFilter.class, this.getSystemMonitorFilterId());
			if (object != null) {
				return (SystemMonitorConfigFilter) object;
			}
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static SystemMonitorConfigDao repository() {
		return (SystemMonitorConfigDao) ApplicationContextUtil.getBean("systemMonitorConfigDao");
	}
}
