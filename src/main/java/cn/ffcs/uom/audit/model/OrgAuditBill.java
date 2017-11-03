package cn.ffcs.uom.audit.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.audit.dao.OrgAuditDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

public class OrgAuditBill implements Serializable {
	@Getter
	@Setter
	private Long systemDomainId;
	@Getter
	@Setter
	private String systemName;
	@Getter
	@Setter
	private String orgId;
	@Getter
	@Setter
	private String orgName;
	@Getter
	@Setter
	private String orgFullName;
	@Getter
	@Setter
	private String orgCode;
	@Getter
	@Setter
	private String orgUuId;
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
	public static OrgAuditDao repository() {
		return (OrgAuditDao) ApplicationContextUtil.getBean("orgAuditDao");
	}

}
