/**
 * 
 */
package cn.ffcs.uom.hisQuery.manager.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.hisQuery.manager.StaffHisManager;
import cn.ffcs.uom.staff.model.Staff;

/**
 * @author yahui
 * 
 */
@Service("staffHisManager")
@Scope("prototype")
public class StaffHisManagerImpl implements StaffHisManager {
	

	@Override
	public PageInfo queryStaffHisPageInfoByParams(Map paramsMap,
			int currentPage, int pageSize) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM (SELECT STAFF_ID,STAFF_CODE,STAFF_NAME,STATUS_CD,EFF_DATE,EXP_DATE FROM STAFF WHERE 1=1 ");
		if (paramsMap.get("queryDate") != null) {
			sb.append(" AND EFF_DATE<=?");
			params.add(paramsMap.get("queryDate"));
			if (paramsMap.get("orgId") != null) {
				sb
						.append(" AND STAFF_ID IN (SELECT STAFF_ID FROM STAFF_ORGANIZATION WHERE ORG_ID IS NOT NULL AND EFF_DATE<?");
				params.add(paramsMap.get("queryDate"));
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
			if (paramsMap.get("relaCd") != null
					|| paramsMap.get("orgId") != null) {
				sb
						.append(" UNION SELECT STAFF_ID FROM STAFF_ORGANIZATION_HIS WHERE ORG_ID IS NOT NULL AND EFF_DATE<? AND EXP_DATE>?");
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("queryDate"));
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}

			if (paramsMap.get("orgId") != null) {
				sb.append(")");
			}
			sb
					.append(" UNION SELECT STAFF_ID,STAFF_CODE,STAFF_NAME,STATUS_CD,EFF_DATE,EXP_DATE FROM STAFF_HIS WHERE EFF_DATE<? AND EXP_DATE>?");
			params.add(paramsMap.get("queryDate"));
			params.add(paramsMap.get("queryDate"));

			if (paramsMap.get("orgId") != null) {
				sb
						.append(" AND STAFF_ID IN (SELECT STAFF_ID FROM STAFF_ORGANIZATION WHERE ORG_ID IS NOT NULL AND EFF_DATE<?");
				params.add(paramsMap.get("queryDate"));
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
			if (paramsMap.get("orgId") != null) {
				sb
						.append(" UNION SELECT STAFF_ID FROM STAFF_ORGANIZATION_HIS WHERE ORG_ID IS NOT NULL AND EFF_DATE<? AND EXP_DATE>?");
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("queryDate"));
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}

			if (paramsMap.get("orgId") != null) {
				sb.append(")");
			}
		} else {
			sb.append(" AND STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (paramsMap.get("orgId") != null) {
				sb
						.append(" AND STAFF_ID IN (SELECT STAFF_ID FROM STAFF_ORGANIZATION WHERE STATUS_CD = ? AND ORG_ID IS NOT NULL");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
			if (paramsMap.get("orgId") != null) {
				sb.append(")");
			}
		}
		sb.append(")");
		sb.append("ORDER BY STAFF_ID");
		
		
		/*return this.staff.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, staff.class);*/
		return DefaultDaoFactory.getDefaultDao().jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, Staff.class);
	}

	@Override
	public Staff queryStaffDetail(Map paramsMap) {

		List<Staff> staffs = null;
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM staff WHERE 1=1 ");
		if(paramsMap.get("staffId") != null){
			sql.append("AND staff_id = ? ");
			params.add(paramsMap.get("staff_id"));
		}
		if(paramsMap.get("effDate") != null){
			sql.append(" AND eff_date <= ?");
			params.add(paramsMap.get("effDate"));
		}
		staffs = DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),params, Staff.class);
		if(staffs != null && staffs.size()>0){
			return staffs.get(0);
		}else{
			List paramsLog = new ArrayList();
			StringBuffer sqlLog = new StringBuffer();
			sqlLog.append("SELECT * FROM STAFF_LOG WHERE 1=1 ");
			if(paramsMap.get("staffId") != null){
				sqlLog.append(" AND staff_id = ?");
				paramsLog.add(paramsMap.get("staffId"));
			}
			if(paramsMap.get("effDate") != null){
				sqlLog.append(" AND eff_date<= ?");
				paramsLog.add(paramsMap.get("effDate"));
			}
			if(paramsMap.get("expDate") != null){
				sqlLog.append(" AND exp_date > ?");
				paramsLog.add(paramsMap.get("expDate"));
			}
			staffs = DefaultDaoFactory.getDefaultDao().jdbcFindList(sqlLog.toString(),paramsLog, Staff.class);
			if(staffs != null && staffs.size()>0){
				return staffs.get(0);
			}
		}
		return null;
	}

}
