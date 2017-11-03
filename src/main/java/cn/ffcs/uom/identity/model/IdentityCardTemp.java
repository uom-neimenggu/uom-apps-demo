package cn.ffcs.uom.identity.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.systemconfig.dao.IdentityCardConfigDao;

/**
 * 身份证类型.
 * 
 * @author
 * 
 **/
public class IdentityCardTemp {

	/**
	 * 身份证类型名称.
	 **/
	@Getter
	@Setter
	private String identityCardName;
	/**
	 * 身份证号码.
	 **/
	@Getter
	@Setter
	private String identityCardNumber;
	/**
	 * 创建时间.
	 **/
	@Getter
	@Setter
	private Date createDate;

	public static IdentityCardConfigDao repository() {
		return (IdentityCardConfigDao) ApplicationContextUtil
				.getBean("identityCardConfigDao");
	}

}
