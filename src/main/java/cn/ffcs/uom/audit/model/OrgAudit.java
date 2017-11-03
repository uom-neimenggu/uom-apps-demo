package cn.ffcs.uom.audit.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.audit.dao.OrgAuditDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;

public class OrgAudit implements Serializable {
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
	private Long orgTotal;
	@Getter
	@Setter
	private Long exceNumber;
	@Getter
	@Setter
	private Double orgPercent;
	@Getter
	@Setter
	private Date updateDate;

	public String getOrgPercentStr() {
		DecimalFormat df = new DecimalFormat("##.00");
		return df.format(orgPercent * 100) + "%";
	}

	public String getUpdateDateStr() {
		return DateUtil.getShortDateStr(updateDate);
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgAuditDao repository() {
		return (OrgAuditDao) ApplicationContextUtil.getBean("orgAuditDao");
	}
}
