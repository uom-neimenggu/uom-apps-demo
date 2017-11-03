package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.model.Staff;

public class refStaffTypeCdFilter extends BaseStaffOrgFilter {
	private List<String> staffTypes;
	private Set<Long> staffIdSet = new HashSet<Long>();
	private Logger logger = Logger.getLogger(this.getClass());

	public refStaffTypeCdFilter(List<String> staffTypes) {
		this(staffTypes, null);
	}
	
	public refStaffTypeCdFilter(List<String> staffTypes, Date date) {
		this.staffTypes = staffTypes;
		cacheStaff(date);
		logger.debug("refStaffTypeCdFilter : staffTypes:" + staffTypes.toString() + " staffIdSet:" + staffIdSet.toString());
	}

	@Override
	public boolean validate(StaffOrganization entity, Object... args) {
		boolean rtn = false;
		if (entity != null) {
			if (staffIdSet.contains(entity.getStaffId())) {
				rtn = true;
			}
		}
		return rtn && super.validate(entity, args);
	}

	private void cacheStaff(Date date) {
		String sql = "";
		List params = new ArrayList();
		if (date == null) {
			sql += "SELECT * FROM STAFF where 1=1 and STATUS_CD = ?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		} else {
			sql += "SELECT * FROM V_STAFF where 1=1 AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		}

		List<Staff> staffList = Staff.repository().jdbcFindList(sql, params,
				Staff.class);
		//将员工类型落在staffTypes里的StaffId存放到staffIdSet
		for (int i = 0; i < staffList.size(); i++) {
			if (staffTypes.contains(staffList.get(i).getWorkProp())) {
				staffIdSet.add(staffList.get(i).getStaffId());
			}
		}
	}
}
