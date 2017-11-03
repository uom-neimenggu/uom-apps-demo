package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.systemconfig.dao.IdentityCardConfigDao;
import cn.ffcs.uom.webservices.constants.WsConstants;

/**
 * 身份证类型.
 * 
 * @author
 * 
 **/
public class IdentityCardConfig extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 身份证类型ID.
	 **/
	public Long getIdentityCardId() {
		return super.getId();
	}

	public void setIdentityCardId(Long identityCardId) {
		super.setId(identityCardId);
	}

	/**
	 * 身份证类型名称.
	 **/
	@Getter
	@Setter
	private String identityCardName;
	/**
	 * 身份证类型前缀.
	 **/
	@Getter
	@Setter
	private String identityCardPrefix;

	/**
	 * 身份证类型开关.
	 **/
	@Getter
	@Setter
	private String identityCardSwitch;

	/**
	 * 获取开关名称.
	 **/
	public String getIdentityCardSwitchName() {
		if (identityCardSwitch.equals(WsConstants.IDENTITY_CARD_SWITCH_OPEN)) {
			return "开";
		} else {
			return "关";
		}
	}

	public IdentityCardConfig() {
		super();
	}

	public static IdentityCardConfigDao repository() {
		return (IdentityCardConfigDao) ApplicationContextUtil
				.getBean("identityCardConfigDao");
	}

}
