package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationLevelDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;

@Repository("rbacRoleOrganizationLevelDao")
@Transactional
public class RbacRoleOrganizationLevelDaoImpl extends BaseDaoImpl implements
		RbacRoleOrganizationLevelDao {

	@Override
	public PageInfo queryPageInfoRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel,
			int currentPage, int pageSize) {

		if (null != rbacRoleOrganizationLevel) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.ORG_CODE,B.ORG_NAME,C.* FROM RBAC_ROLE A,ORGANIZATION B,RBAC_ROLE_ORGANIZATION_LEVEL C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.ORG_ID = C.RBAC_ORG_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacRoleOrganizationLevel.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRoleOrganizationLevel.getRbacRoleName()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getOrgCode())) {
				sb.append(" AND B.ORG_CODE = ?");
				params.add(rbacRoleOrganizationLevel.getOrgCode());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getOrgName())) {
				sb.append(" AND B.ORG_NAME LIKE ?");
				params.add("%" + rbacRoleOrganizationLevel.getOrgName() + "%");
			}

			if (rbacRoleOrganizationLevel.getRbacRoleOrgLevelId() != null) {
				sb.append(" AND C.RBAC_ROLE_ORG_LEVEL_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacRoleOrgLevelId());
			}

			if (rbacRoleOrganizationLevel.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacRoleId());
			}

			if (rbacRoleOrganizationLevel.getRbacOrgId() != null) {
				sb.append(" AND C.RBAC_ORG_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacOrgId());
			}

			if (rbacRoleOrganizationLevel.getRbacLowerLevel() != null) {
				sb.append(" AND C.RBAC_LOWER_LEVEL = ?");
				params.add(rbacRoleOrganizationLevel.getRbacLowerLevel());
			}

			if (rbacRoleOrganizationLevel.getRbacHigherLevel() != null) {
				sb.append(" AND C.RBAC_HIGHER_LEVEL = ?");
				params.add(rbacRoleOrganizationLevel.getRbacHigherLevel());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getRelaCd())) {
				sb.append(" AND C.RELA_CD = ?");
				params.add(rbacRoleOrganizationLevel.getRelaCd());
			}

			sb.append(" ORDER BY C.RBAC_ROLE_ORG_LEVEL_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleOrganizationLevel.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleOrganizationLevel> queryRbacRoleOrganizationLevelList(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {

		if (null != rbacRoleOrganizationLevel) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_ORGANIZATION_LEVEL WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleOrganizationLevel.getRbacRoleOrgLevelId() != null) {
				sb.append(" AND RBAC_ROLE_ORG_LEVEL_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacRoleOrgLevelId());
			}

			if (rbacRoleOrganizationLevel.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacRoleId());
			}

			if (rbacRoleOrganizationLevel.getRbacOrgId() != null) {
				sb.append(" AND RBAC_ORG_ID = ?");
				params.add(rbacRoleOrganizationLevel.getRbacOrgId());
			}

			if (rbacRoleOrganizationLevel.getRbacLowerLevel() != null) {
				sb.append(" AND RBAC_LOWER_LEVEL = ?");
				params.add(rbacRoleOrganizationLevel.getRbacLowerLevel());
			}

			if (rbacRoleOrganizationLevel.getRbacHigherLevel() != null) {
				sb.append(" AND RBAC_HIGHER_LEVEL = ?");
				params.add(rbacRoleOrganizationLevel.getRbacHigherLevel());
			}

			if (!StrUtil.isEmpty(rbacRoleOrganizationLevel.getRelaCd())) {
				sb.append(" AND RELA_CD = ?");
				params.add(rbacRoleOrganizationLevel.getRelaCd());
			}

			sb.append(" ORDER BY RBAC_ROLE_ORG_LEVEL_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleOrganizationLevel.class);
		}

		return null;

	}

	@Override
	public RbacRoleOrganizationLevel queryRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList = this
				.queryRbacRoleOrganizationLevelList(rbacRoleOrganizationLevel);
		if (null != rbacRoleOrganizationLevelList
				&& rbacRoleOrganizationLevelList.size() > 0) {
			return rbacRoleOrganizationLevelList.get(0);
		}
		return null;
	}

}
