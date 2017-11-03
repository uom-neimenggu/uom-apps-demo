package cn.ffcs.uom.staffrole.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

@Repository("staffRoleDao")
@Transactional
public class StaffRoleDaoImpl extends BaseDaoImpl implements StaffRoleDao {

	public List<StaffRole> queryStaffRoles(StaffRole staffRole) {
		StringBuilder sb = new StringBuilder(
				"select * from RBAC_STAFF_ROLE a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (null != staffRole) {
			if (null != staffRole.getRoleParentId()) {
				sb.append(" and ROLE_PARENT_ID = ?");
				params.add(staffRole.getRoleParentId());
			}
		}
		return super.jdbcFindList(sb.toString(), params, StaffRole.class);
	}

	public List<StaffRoleRela> queryStaffRoleList(StaffRoleRela staffRoleRela) {

		StringBuilder sb = new StringBuilder(
				"SELECT A.* FROM RBAC_STAFF_ROLE_RELA A,RBAC_STAFF_ROLE B WHERE A.ROLE_ID = B.ROLE_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (null != staffRoleRela) {

			if (null != staffRoleRela.getStaffId()) {
				sb.append(" AND A.STAFF_ID = ?");
				params.add(staffRoleRela.getStaffId());
			}

			if (null != staffRoleRela.getRoleParentId()) {
				sb.append(" AND B.ROLE_PARENT_ID = ?");
				params.add(staffRoleRela.getRoleParentId());
			}

		}
		return super.jdbcFindList(sb.toString(), params, StaffRoleRela.class);
	}

	public List<StaffRoleRela> queryStaffRoleRelas(StaffRoleRela srr) {
		StringBuilder sb = new StringBuilder(
				"select * from RBAC_STAFF_ROLE_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (null != srr) {
			if (null != srr.getStaffId()) {
				sb.append(" and STAFF_ID = ?");
				params.add(srr.getStaffId());
			}
		}
		return super.jdbcFindList(sb.toString(), params, StaffRoleRela.class);
	}

	public PageInfo queryStaffRoleRela(StaffRoleRela staffRoleRela,
			int currentPage, int pageSize) {
		StringBuilder sb = new StringBuilder(
				"select b.*,d.staff_account from STAFF a,RBAC_STAFF_ROLE_RELA b,RBAC_STAFF_ROLE c,STAFF_ACCOUNT D where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (null != staffRoleRela) {
			if (null != staffRoleRela.getRoleId()) {
				sb.append(" and b.ROLE_ID = ?");
				params.add(staffRoleRela.getRoleId());
			}
			if (null != staffRoleRela.getStaffId()) {
				sb.append(" and b.STAFF_ID = ?");
				params.add(staffRoleRela.getStaffId());
			}
			Staff staff = staffRoleRela.getQryStaff();
			if (null != staff && !StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sb.append(" and a.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName()) + "%");
			}
			if (null != staff && !StrUtil.isNullOrEmpty(staff.getStaffCode())) {
				sb.append(" and a.STAFF_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffCode()));
			}
			if (null != staff
					&& !StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sb.append(" and d.STAFF_ACCOUNT = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffAccount()));
			}
		}
		sb.append(" and a.staff_id = b.staff_id and b.role_id = c.role_id AND A.STAFF_ID = D.STAFF_ID");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, StaffRoleRela.class);
	}

	public StaffRoleRela queryStaffRoleRela(StaffRoleRela srr) {
		StringBuilder sb = new StringBuilder(
				"select * from RBAC_STAFF_ROLE_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (null != srr) {
			if (null != srr.getStaffId()) {
				sb.append(" and STAFF_ID = ?");
				params.add(srr.getStaffId());
			}
			if (null != srr.getRoleId()) {
				sb.append(" and ROLE_ID = ?");
				params.add(srr.getRoleId());
			}
		}
		List<StaffRoleRela> srrs = super.jdbcFindList(sb.toString(), params,
				StaffRoleRela.class);
		if (null != srrs && srrs.size() > 0) {
			return srrs.get(0);
		}
		return null;
	}

    @Override
    public List<StaffRole> queryStaffRoles(Staff staff) {
        // TODO Auto-generated method stub
        String sql = "SELECT T1.* FROM RBAC_STAFF_ROLE T1,RBAC_STAFF_ROLE_RELA T2,STAFF T3 WHERE T3.STAFF_ID=T2.STAFF_ID AND T2.ROLE_ID=T1.ROLE_ID AND T1.STATUS_CD=? AND T2.STATUS_CD=? AND T3.STATUS_CD=? AND T3.STAFF_ID=?";
        List<Object> params = new ArrayList<Object>();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(staff.getStaffId());
        return super.jdbcFindList(sql, params, StaffRole.class);
    }

}
