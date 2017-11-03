package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleDao;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

@Repository("rbacUserRoleDao")
@Transactional
public class RbacUserRoleDaoImpl extends BaseDaoImpl implements RbacUserRoleDao {

	@Override
	public PageInfo queryPageInfoRbacUserRole(RbacUserRole rbacUserRole,
			int currentPage, int pageSize) {

		if (null != rbacUserRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,D.STAFF_ACCOUNT,B.STAFF_NAME,C.* FROM RBAC_ROLE A,STAFF B,RBAC_USER_ROLE C,STAFF_ACCOUNT D");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.STAFF_ID = C.RBAC_USER_ID AND B.STAFF_ID = D.STAFF_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ? AND D.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacUserRole.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacUserRole.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacUserRole.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacUserRole.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacUserRole.getStaffAccount())) {
				sb.append(" AND D.STAFF_ACCOUNT = ?");
				params.add(rbacUserRole.getStaffAccount());
			}

			if (!StrUtil.isEmpty(rbacUserRole.getStaffName())) {
				sb.append(" AND B.STAFF_NAME LIKE ?");
				params.add("%" + rbacUserRole.getStaffName() + "%");
			}

			if (rbacUserRole.getRbacUserRoleId() != null) {
				sb.append(" AND C.RBAC_USER_ROLE_ID = ?");
				params.add(rbacUserRole.getRbacUserRoleId());
			}

			if (rbacUserRole.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacUserRole.getRbacRoleId());
			}

			if (rbacUserRole.getRbacUserId() != null) {
				sb.append(" AND C.RBAC_USER_ID = ?");
				params.add(rbacUserRole.getRbacUserId());
			}

			sb.append(" ORDER BY C.RBAC_USER_ROLE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacUserRole.class);
		}

		return null;

	}

	@Override
	public List<RbacUserRole> queryRbacUserRoleList(RbacUserRole rbacUserRole) {

		if (null != rbacUserRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_USER_ROLE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacUserRole.getRbacUserRoleId() != null) {
				sb.append(" AND RBAC_USER_ROLE_ID = ?");
				params.add(rbacUserRole.getRbacUserRoleId());
			}

			if (rbacUserRole.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacUserRole.getRbacRoleId());
			}

			if (rbacUserRole.getRbacUserId() != null) {
				sb.append(" AND RBAC_USER_ID = ?");
				params.add(rbacUserRole.getRbacUserId());
			}

			sb.append(" ORDER BY RBAC_USER_ROLE_ID");

			return super
					.jdbcFindList(sb.toString(), params, RbacUserRole.class);
		}

		return null;

	}

	@Override
	public RbacUserRole queryRbacUserRole(RbacUserRole rbacUserRole) {
		List<RbacUserRole> rbacUserRoleList = this
				.queryRbacUserRoleList(rbacUserRole);
		if (null != rbacUserRoleList && rbacUserRoleList.size() > 0) {
			return rbacUserRoleList.get(0);
		}
		return null;
	}

}
