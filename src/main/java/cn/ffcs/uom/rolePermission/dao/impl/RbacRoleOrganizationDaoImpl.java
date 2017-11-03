package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;

@Repository("rbacRoleOrganizationDao")
@Transactional
public class RbacRoleOrganizationDaoImpl extends BaseDaoImpl implements
		RbacRoleOrganizationDao {

	@Override
	public PageInfo queryPageInfoRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization, int currentPage,
			int pageSize) {

		if (null != rbacRoleOrganization) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.ORG_CODE,B.ORG_NAME,C.* FROM RBAC_ROLE A,ORGANIZATION B,RBAC_ROLE_ORGANIZATION C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.ORG_ID = C.RBAC_ORG_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacRoleOrganization.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacRoleOrganization.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganization.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRoleOrganization.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacRoleOrganization.getOrgCode())) {
				sb.append(" AND B.ORG_CODE = ?");
				params.add(rbacRoleOrganization.getOrgCode());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganization.getOrgName())) {
				sb.append(" AND B.ORG_NAME LIKE ?");
				params.add("%" + rbacRoleOrganization.getOrgName() + "%");
			}

			if (rbacRoleOrganization.getRbacRoleOrgId() != null) {
				sb.append(" AND C.RBAC_ROLE_ORG_ID = ?");
				params.add(rbacRoleOrganization.getRbacRoleOrgId());
			}

			if (rbacRoleOrganization.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacRoleOrganization.getRbacRoleId());
			}

			if (rbacRoleOrganization.getRbacOrgId() != null) {
				sb.append(" AND C.RBAC_ORG_ID = ?");
				params.add(rbacRoleOrganization.getRbacOrgId());
			}

			sb.append(" ORDER BY C.RBAC_ROLE_ORG_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleOrganization.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleOrganization> queryRbacRoleOrganizationList(
			RbacRoleOrganization rbacRoleOrganization) {

		if (null != rbacRoleOrganization) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_ORGANIZATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleOrganization.getRbacRoleOrgId() != null) {
				sb.append(" AND RBAC_ROLE_ORG_ID = ?");
				params.add(rbacRoleOrganization.getRbacRoleOrgId());
			}

			if (rbacRoleOrganization.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleOrganization.getRbacRoleId());
			}

			if (rbacRoleOrganization.getRbacOrgId() != null) {
				sb.append(" AND RBAC_ORG_ID = ?");
				params.add(rbacRoleOrganization.getRbacOrgId());
			}

			sb.append(" ORDER BY RBAC_ROLE_ORG_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleOrganization.class);
		}

		return null;

	}

	@Override
	public RbacRoleOrganization queryRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization) {
		List<RbacRoleOrganization> rbacRoleOrganizationList = this
				.queryRbacRoleOrganizationList(rbacRoleOrganization);
		if (null != rbacRoleOrganizationList
				&& rbacRoleOrganizationList.size() > 0) {
			return rbacRoleOrganizationList.get(0);
		}
		return null;
	}

}
