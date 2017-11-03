package cn.ffcs.uom.audit.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.audit.dao.StaffAuditDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

public class StaffAuditBill implements Serializable {
	@Getter
	@Setter
	private Long systemDomainId;
	@Getter
	@Setter
	private String systemName;
	@Getter
	@Setter
	private String orgName;
	@Getter
	@Setter
	private String staffId;
	@Getter
	@Setter
	private String staffName;
	@Getter
	@Setter
	private String staffCode;
	@Getter
	@Setter
	private String staffAccount;
	@Getter
	@Setter
	private String staffUuId;
	@Getter
	@Setter
	private String orgFullName;
	@Getter
	@Setter
	private String nonStandard;
	@Getter
	@Setter
	private Date updateDate;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static StaffAuditDao repository() {
		return (StaffAuditDao) ApplicationContextUtil.getBean("staffAuditDao");
	}
}
