package cn.ffcs.uom.staff.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.dao.StaffGrpStaffDao;
import cn.ffcs.uom.staff.model.StaffGrpStaff;
import cn.ffcs.uom.staff.model.StaffPosition;

@Repository("staffGrpStaffDao")
public class StaffGrpStaffDaoImpl extends BaseDaoImpl implements
		StaffGrpStaffDao {

	@Override
	public Long addStaffPosition(StaffPosition staffPosition) {
		Serializable ser = this.getHibernateTemplate().save(staffPosition);
		Long id = Long.parseLong(ser.toString());
		return id;
	}

	@Override
	public StaffGrpStaff queryStaffGrpStaff(StaffGrpStaff staffGrpStaff) {
		List<StaffGrpStaff> list = this.queryStaffGrpStaffList(staffGrpStaff);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<StaffGrpStaff> queryStaffGrpStaffList(
			StaffGrpStaff staffGrpStaff) {

		if (staffGrpStaff != null) {

			List<Object> params = new ArrayList<Object>();
			StringBuffer sb = new StringBuffer(
					"SELECT ST.STAFF_NAME UOM_STAFF_NAME,SA.STAFF_ACCOUNT UOM_STAFF_ACCOUNT,CU.USERNAME USER_NAME,CU.LOGINNAME");
			sb.append(" LOGIN_NAME,GS.STAFF_NAME,SGS.* FROM STAFF_GRP_STAFF SGS LEFT JOIN STAFF ST ON SGS.STAFF_ID = ST.STAFF_ID");
			sb.append(" LEFT JOIN STAFF_ACCOUNT SA ON ST.STAFF_ID = SA.STAFF_ID LEFT JOIN (SELECT * FROM CT_USER WHERE DEL_STATUS = ?) CU ON SA.GUID = CU.LOGINNAME");
			sb.append(" LEFT JOIN GRP_STAFF GS ON SGS.SALES_CODE = GS.SALES_CODE WHERE SGS.STATUS_CD = ? AND ST.STATUS_CD = ? AND SA.STATUS_CD = ? AND GS.STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffGrpStaff.getStaffId() != null) {
				sb.append(" AND ST.STAFF_ID = ?");
				params.add(staffGrpStaff.getStaffId());
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getStaffName())) {
				sb.append(" AND GS.STAFF_NAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getStaffName()));
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getSalesCode())) {
				sb.append(" AND GS.SALES_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getSalesCode()));

			}

			if (!StrUtil.isEmpty(staffGrpStaff.getUserName())) {
				sb.append(" AND CU.USERNAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getUserName()));
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getLoginName())) {
				sb.append(" AND CU.LOGINNAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getLoginName()));

			}

			return StaffGrpStaff.repository().jdbcFindList(sb.toString(),
					params, StaffGrpStaff.class);
		}

		return null;

	}

	@Override
	public PageInfo queryPageInfoByStaffGrpStaff(StaffGrpStaff staffGrpStaff,
			int currentPage, int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT ST.STAFF_NAME UOM_STAFF_NAME,SA.STAFF_ACCOUNT UOM_STAFF_ACCOUNT,CU.USERNAME USER_NAME,CU.LOGINNAME");
		sb.append(" LOGIN_NAME,GS.STAFF_NAME,SGS.* FROM STAFF_GRP_STAFF SGS LEFT JOIN STAFF ST ON SGS.STAFF_ID = ST.STAFF_ID");
		sb.append(" LEFT JOIN STAFF_ACCOUNT SA ON ST.STAFF_ID = SA.STAFF_ID LEFT JOIN (SELECT * FROM CT_USER WHERE DEL_STATUS = ?) CU ON SA.GUID = CU.LOGINNAME");
		sb.append(" LEFT JOIN GRP_STAFF GS ON SGS.SALES_CODE = GS.SALES_CODE WHERE SGS.STATUS_CD = ? AND ST.STATUS_CD = ? AND SA.STATUS_CD = ? AND GS.STATUS_CD = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staffGrpStaff != null) {

			if (staffGrpStaff.getStaffId() != null) {
				sb.append(" AND ST.STAFF_ID = ?");
				params.add(staffGrpStaff.getStaffId());
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getStaffName())) {
				sb.append(" AND GS.STAFF_NAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getStaffName()));
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getSalesCode())) {
				sb.append(" AND GS.SALES_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getSalesCode()));

			}

			if (!StrUtil.isEmpty(staffGrpStaff.getUserName())) {
				sb.append(" AND CU.USERNAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getUserName()));
			}

			if (!StrUtil.isEmpty(staffGrpStaff.getLoginName())) {
				sb.append(" AND CU.LOGINNAME = ?");
				params.add(StringEscapeUtils.escapeSql(staffGrpStaff.getLoginName()));

			}

		}

		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, StaffGrpStaff.class);

	}
}
