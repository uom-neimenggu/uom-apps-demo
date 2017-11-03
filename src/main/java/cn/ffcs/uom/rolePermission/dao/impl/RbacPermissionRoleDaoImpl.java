package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRoleDao;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;

@Repository("rbacPermissionRoleDao")
@Transactional
public class RbacPermissionRoleDaoImpl extends BaseDaoImpl implements
		RbacPermissionRoleDao {

	@Override
	public PageInfo queryPageInfoRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole, int currentPage, int pageSize) {

		if (null != rbacPermissionRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.RBAC_PERMISSION_CODE,B.RBAC_PERMISSION_NAME,C.* FROM RBAC_ROLE A,RBAC_PERMISSION B,RBAC_PERMISSION_ROLE C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.RBAC_PERMISSION_ID = C.RBAC_PERMISSION_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacPermissionRole.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacPermissionRole.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacPermissionRole.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacPermissionRole.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacPermissionRole.getRbacPermissionCode())) {
				sb.append(" AND B.RBAC_PERMISSION_CODE = ?");
				params.add(rbacPermissionRole.getRbacPermissionCode());
			}

			if (!StrUtil.isEmpty(rbacPermissionRole.getRbacPermissionName())) {
				sb.append(" AND B.RBAC_PERMISSION_NAME LIKE ?");
				params.add("%" + rbacPermissionRole.getRbacPermissionName()
						+ "%");
			}

			if (rbacPermissionRole.getRbacPermissionRoleId() != null) {
				sb.append(" AND C.RBAC_PERMISSION_ROLE_ID = ?");
				params.add(rbacPermissionRole.getRbacPermissionRoleId());
			}

			if (rbacPermissionRole.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacPermissionRole.getRbacRoleId());
			}

			if (rbacPermissionRole.getRbacPermissionId() != null) {
				sb.append(" AND C.RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionRole.getRbacPermissionId());
			}

			sb.append(" ORDER BY C.RBAC_PERMISSION_ROLE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacPermissionRole.class);
		}

		return null;

	}

	@Override
	public List<RbacPermissionRole> queryRbacPermissionRoleList(
			RbacPermissionRole rbacPermissionRole) {

		if (null != rbacPermissionRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_ROLE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionRole.getRbacPermissionRoleId() != null) {
				sb.append(" AND RBAC_PERMISSION_ROLE_ID = ?");
				params.add(rbacPermissionRole.getRbacPermissionRoleId());
			}

			if (rbacPermissionRole.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacPermissionRole.getRbacRoleId());
			}

			if (rbacPermissionRole.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionRole.getRbacPermissionId());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_ROLE_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacPermissionRole.class);
		}

		return null;

	}

	@Override
	public RbacPermissionRole queryRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole) {
		List<RbacPermissionRole> rbacPermissionRoleList = this
				.queryRbacPermissionRoleList(rbacPermissionRole);
		if (null != rbacPermissionRoleList && rbacPermissionRoleList.size() > 0) {
			return rbacPermissionRoleList.get(0);
		}
		return null;
	}

}
