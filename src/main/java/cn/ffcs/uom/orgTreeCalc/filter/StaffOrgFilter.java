package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;

import cn.ffcs.uom.organization.model.StaffOrganization;

public interface StaffOrgFilter {
	boolean validate(StaffOrganization entity, Object... args);

	void setFilter(StaffOrgFilter filter);
}
