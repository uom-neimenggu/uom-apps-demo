package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;

import cn.ffcs.uom.organization.model.StaffOrganization;

public class BaseStaffOrgFilter implements StaffOrgFilter {

	private StaffOrgFilter _filter;

	public void setFilter(StaffOrgFilter filter) {
		if (_filter == null) {
			_filter = filter;
		} else {
			_filter.setFilter(filter);
		}
	}

	public boolean validate(StaffOrganization entity, Object... args) {
		if(_filter != null)
			return _filter.validate(entity, args);
		return true;
	}
}
