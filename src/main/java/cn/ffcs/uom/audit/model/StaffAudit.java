package cn.ffcs.uom.audit.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.audit.dao.StaffAuditDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;

public class StaffAudit implements Serializable {

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
	private Long staffTotal;
	@Getter
	@Setter
	private Long exceNumber;
	@Getter
	@Setter
	private Double staffPercent;
	@Getter
	@Setter
	private Date updateDate;

	public String getStaffPercentStr() {
		DecimalFormat df = new DecimalFormat("##.00");
		return df.format(staffPercent * 100) + "%";
	}

	public String getUpdateDateStr() {
		return DateUtil.getShortDateStr(updateDate);
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static StaffAuditDao repository() {
		return (StaffAuditDao) ApplicationContextUtil.getBean("staffAuditDao");
	}
}
