package cn.ffcs.uom.webservices.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.webservices.dao.SystemIntfInfoConfigDao;

/**
 * 系统接口信息配置表实体.
 * 
 * @author
 * 
 **/
public class SystemIntfInfoConfig extends UomEntity implements Serializable {
	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 * 消息类型.
	 **/
	@Getter
	@Setter
	private String msgType;
	/**
	 * 全量接口开关.
	 **/
	@Getter
	@Setter
	private String intfSwitchAll;
	/**
	 * 增量接口开关.
	 **/
	@Getter
	@Setter
	private String intfSwitchIncrease;
	/**
	 * 接口地址.
	 **/
	@Getter
	@Setter
	private String intfUrl;
	/**
	 * 接口方法名.
	 **/
	@Getter
	@Setter
	private String operationName;
	/**
	 * 接口参数名.
	 **/
	@Getter
	@Setter
	private String paramName;
	/**
	 * 命名空间.
	 **/
	@Getter
	@Setter
	private String nameSpace;
	/**
	 * OIP编码.
	 **/
	@Getter
	@Setter
	private String serviceCode;
	/**
	 * OIP地址.
	 **/
	@Getter
	@Setter
	private String oipUrl;

	/**
	 * 
	 * @return systemIntfInfoConfigId属性.
	 * 
	 **/
	public Long getSystemIntfInfoConfigId() {
		return super.getId();
	}

	/**
	 * @param systemIntfInfoConfigId
	 *            "systemIntfInfoConfigId"设置方法定义. 设置systemIntfInfoConfigId属性
	 **/
	public void setSystemIntfInfoConfigId(Long systemIntfInfoConfigId) {
		super.setId(systemIntfInfoConfigId);
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

	// public String getIntfSwitchAllName() {
	// if (this.getSystemIntfInfoConfigId() != null) {
	//
	// SystemIntfInfoConfig systemIntfInfoConfig = (SystemIntfInfoConfig)
	// SystemIntfInfoConfig
	// .repository().getObject(SystemIntfInfoConfig.class,
	// this.getSystemIntfInfoConfigId());
	//
	// if (systemIntfInfoConfig != null) {
	// if (systemIntfInfoConfig.getIntfSwitchAll().equals("1")) {
	// return "开";
	// } else {
	// return "关";
	// }
	// }
	// }
	//
	// return "";
	// }

	/**
	 * 获取系统接口配置
	 * 
	 * @return
	 */
	public SystemIntfInfoConfig getSystemIntfInfoConfig() {
		if (this.getSystemIntfInfoConfigId() != null) {
			Object object = this.repository().getObject(
					SystemIntfInfoConfig.class,
					this.getSystemIntfInfoConfigId());
			if (object != null) {
				return (SystemIntfInfoConfig) object;
			}
		}
		return null;
	}

	/**
	 * 获取系统接口全量开关
	 * 
	 * @return
	 */
	public String getIntfSwitchAllName() {
		SystemIntfInfoConfig systemIntfInfoConfig = this
				.getSystemIntfInfoConfig();
		if (systemIntfInfoConfig != null) {
			if (systemIntfInfoConfig.getIntfSwitchAll().equals("1")) {
				return "开";
			} else {
				return "关";
			}
		}
		return "";
	}

	/**
	 * 获取系统接口增量开关
	 * 
	 * @return
	 */
	public String getIntfSwitchIncreaseName() {
		SystemIntfInfoConfig systemIntfInfoConfig = this
				.getSystemIntfInfoConfig();
		if (systemIntfInfoConfig != null) {
			if (systemIntfInfoConfig.getIntfSwitchIncrease().equals("1")) {
				return "开";
			} else {
				return "关";
			}
		}
		return "";
	}

	// public String getIntfSwitchIncreaseName() {
	// if (this.getSystemIntfInfoConfigId() != null) {
	//
	// SystemIntfInfoConfig systemIntfInfoConfig = (SystemIntfInfoConfig)
	// SystemIntfInfoConfig
	// .repository().getObject(SystemIntfInfoConfig.class,
	// this.getSystemIntfInfoConfigId());
	//
	// if (systemIntfInfoConfig != null) {
	// if (systemIntfInfoConfig.getIntfSwitchIncrease().equals("1")) {
	// return "开";
	// } else {
	// return "关";
	// }
	// }
	// }
	//
	// return "";
	// }
}
