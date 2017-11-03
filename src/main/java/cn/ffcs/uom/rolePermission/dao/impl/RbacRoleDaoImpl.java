package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleDao;
import cn.ffcs.uom.rolePermission.model.RbacRole;

@Repository("rbacRoleDao")
@Transactional
public class RbacRoleDaoImpl extends BaseDaoImpl implements RbacRoleDao {

	@Override
	public PageInfo queryPageInfoRbacRole(RbacRole rbacRole, int currentPage,
			int pageSize) {

		if (null != rbacRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRole.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRole.getRbacRoleId());
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleCode())) {
				sb.append(" AND RBAC_ROLE_CODE = ?");
				params.add(rbacRole.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleName())) {
				sb.append(" AND RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRole.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleType())) {
				sb.append(" AND RBAC_ROLE_TYPE = ?");
				params.add(rbacRole.getRbacRoleType());
			}

			sb.append(" ORDER BY RBAC_ROLE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRole.class);
		}

		return null;

	}

	@Override
	public List<RbacRole> queryRbacRoleList(RbacRole rbacRole) {

		if (null != rbacRole) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRole.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRole.getRbacRoleId());
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleCode())) {
				sb.append(" AND RBAC_ROLE_CODE = ?");
				params.add(rbacRole.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleName())) {
				sb.append(" AND RBAC_ROLE_NAME = ?");
				params.add(rbacRole.getRbacRoleName());
			}

			if (!StrUtil.isEmpty(rbacRole.getRbacRoleType())) {
				sb.append(" AND RBAC_ROLE_TYPE = ?");
				params.add(rbacRole.getRbacRoleType());
			}

			sb.append(" ORDER BY RBAC_ROLE_ID");

			return super.jdbcFindList(sb.toString(), params, RbacRole.class);
		}

		return null;

	}

	@Override
	public RbacRole queryRbacRole(RbacRole rbacRole) {
		List<RbacRole> rbacRoleList = this.queryRbacRoleList(rbacRole);
		if (null != rbacRoleList && rbacRoleList.size() > 0) {
			return rbacRoleList.get(0);
		}
		return null;
	}

}
