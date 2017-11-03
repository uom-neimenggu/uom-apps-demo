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
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.model.Staff;

public class refStaffOtFilter extends BaseStaffOrgFilter {
	private List<String> staffOts;
	private Set<Long> orgIdSet = new HashSet<Long>();
	private Logger logger = Logger.getLogger(this.getClass());

	public refStaffOtFilter(List<String> staffOts) {
		this(staffOts, null);
	}
	
	public refStaffOtFilter(List<String> staffOts, Date date) {
		this.staffOts = staffOts;
		cacheOrgType(date);
		logger.debug("refStaffOtFilter : staffOts:" + staffOts.toString() + " orgIdSet:" + orgIdSet.toString());
	}

	@Override
	public boolean validate(StaffOrganization entity, Object... args) {
		boolean rtn = false;
		if (entity != null) {
			if (orgIdSet.contains(entity.getOrgId())) {
				rtn = true;
			}
		}
		return rtn && super.validate(entity, args);
	}
	
	private void cacheOrgType(Date date) {
		String sql = "";
		List params = new ArrayList();
		if (date == null) {
			sql += "SELECT * FROM ORG_TYPE where 1=1 and STATUS_CD = ?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		} else {
			sql += "SELECT * FROM V_ORG_TYPE where 1=1 AND EFF_DATE <= to_date(? ,'yyyyMMddHH24miss') AND EXP_DATE > to_date(? ,'yyyyMMddHH24miss') AND STATUS_CD != ?";
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(DateUtil.getYYYYMMDDHHmmss(date));
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		}

		List<OrgType> orgTypeList = DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, OrgType.class);
		//将组织关系类型落在staffOts里的OrgId存放到orgIdSet
		for (int i = 0; i < orgTypeList.size(); i++) {
			if (staffOts.contains(orgTypeList.get(i).getOrgTypeCd())) {
				orgIdSet.add(orgTypeList.get(i).getOrgId());
			}
		}
	}
}
