package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 员工账号 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
public class StaffAccount extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工账号标识
	 */
	public Long getStaffAccountId() {
		return super.getId();
	}

	public void setStaffAccountId(Long staffAccountId) {
		super.setId(staffAccountId);
	}

	/**
	 * 员工Id
	 */
	@Setter
	@Getter
	private Long staffId;

	/**
	 * 员工账号
	 */
	@Setter
	@Getter
	private String staffAccount;
	/**
	 * 员工账号密码
	 */
	@Setter
	@Getter
	private String staffPassword;
	/**
	 * 集团UID
	 */
	@Setter
	@Getter
	private String guid;
	/**
	 * 员工账号单向加密密码
	 */
	@Setter
	@Getter
	private String rePassword;
	/**
	 * UOM自定义字段01
	 */
	@Setter
	@Getter
	private String uomDef01;

	public static StaffAccount newInstance() {
		return new StaffAccount();
	}

}
