package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleBusinessSystemDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

@Repository("rbacRoleBusinessSystemDao")
@Transactional
public class RbacRoleBusinessSystemDaoImpl extends BaseDaoImpl implements
		RbacRoleBusinessSystemDao {

	@Override
	public PageInfo queryPageInfoRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem, int currentPage,
			int pageSize) {

		if (null != rbacRoleBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.RBAC_BUSINESS_SYSTEM_CODE,B.RBAC_BUSINESS_SYSTEM_NAME,C.* FROM RBAC_ROLE A,RBAC_BUSINESS_SYSTEM B,RBAC_ROLE_BUSINESS_SYSTEM C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.RBAC_BUSINESS_SYSTEM_ID = C.RBAC_BUSINESS_SYSTEM_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacRoleBusinessSystem.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacRoleBusinessSystem.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRoleBusinessSystem.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRoleBusinessSystem.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacRoleBusinessSystem
					.getRbacBusinessSystemCode())) {
				sb.append(" AND B.RBAC_BUSINESS_SYSTEM_CODE = ?");
				params.add(rbacRoleBusinessSystem.getRbacBusinessSystemCode());
			}

			if (!StrUtil.isEmpty(rbacRoleBusinessSystem
					.getRbacBusinessSystemName())) {
				sb.append(" AND B.RBAC_BUSINESS_SYSTEM_NAME LIKE ?");
				params.add("%"
						+ rbacRoleBusinessSystem.getRbacBusinessSystemName()
						+ "%");
			}

			if (rbacRoleBusinessSystem.getRbacRoleBusinessSystemId() != null) {
				sb.append(" AND C.RBAC_ROLE_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacRoleBusinessSystemId());
			}

			if (rbacRoleBusinessSystem.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacRoleId());
			}

			if (rbacRoleBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND C.RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacBusinessSystemId());
			}

			// sb.append(" ORDER BY C.RBAC_ROLE_BUSINESS_SYSTEM_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleBusinessSystem.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleBusinessSystem> queryRbacRoleBusinessSystemList(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {

		if (null != rbacRoleBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_BUSINESS_SYSTEM WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleBusinessSystem.getRbacRoleBusinessSystemId() != null) {
				sb.append(" AND RBAC_ROLE_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacRoleBusinessSystemId());
			}

			if (rbacRoleBusinessSystem.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacRoleId());
			}

			if (rbacRoleBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacRoleBusinessSystem.getRbacBusinessSystemId());
			}

			// sb.append(" ORDER BY RBAC_ROLE_BUSINESS_SYSTEM_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleBusinessSystem.class);
		}

		return null;

	}

	@Override
	public RbacRoleBusinessSystem queryRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		List<RbacRoleBusinessSystem> rbacRoleBusinessSystemList = this
				.queryRbacRoleBusinessSystemList(rbacRoleBusinessSystem);
		if (null != rbacRoleBusinessSystemList
				&& rbacRoleBusinessSystemList.size() > 0) {
			return rbacRoleBusinessSystemList.get(0);
		}
		return null;
	}

}
