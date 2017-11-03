package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleBusinessSystemDao;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

@Repository("rbacUserRoleBusinessSystemDao")
@Transactional
public class RbacUserRoleBusinessSystemDaoImpl extends BaseDaoImpl implements
		RbacUserRoleBusinessSystemDao {

	@Override
	public PageInfo queryPageInfoRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem,
			int currentPage, int pageSize) {

		if (null != rbacUserRoleBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,D.STAFF_ACCOUNT,B.STAFF_NAME,F.RBAC_BUSINESS_SYSTEM_CODE,F.RBAC_BUSINESS_SYSTEM_NAME,E.*");
			sb.append(" FROM RBAC_ROLE A,STAFF B,RBAC_USER_ROLE C,STAFF_ACCOUNT D,RBAC_USER_ROLE_BUSINESS_SYS E,RBAC_BUSINESS_SYSTEM F");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.STAFF_ID = C.RBAC_USER_ID AND B.STAFF_ID = D.STAFF_ID AND E.RBAC_BUSINESS_SYSTEM_ID = F.RBAC_BUSINESS_SYSTEM_ID AND E.RBAC_USER_ROLE_ID = C.RBAC_USER_ROLE_ID");
			sb.append(" AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ? AND D.STATUS_CD = ? AND E.STATUS_CD = ? AND F.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacUserRoleBusinessSystem.getRbacRoleName()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem.getStaffAccount())) {
				sb.append(" AND D.STAFF_ACCOUNT = ?");
				params.add(rbacUserRoleBusinessSystem.getStaffAccount());
			}

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem.getStaffName())) {
				sb.append(" AND B.STAFF_NAME LIKE ?");
				params.add("%" + rbacUserRoleBusinessSystem.getStaffName()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem
					.getRbacBusinessSystemCode())) {
				sb.append(" AND F.RBAC_BUSINESS_SYSTEM_CODE = ?");
				params.add(rbacUserRoleBusinessSystem
						.getRbacBusinessSystemCode());
			}

			if (!StrUtil.isEmpty(rbacUserRoleBusinessSystem
					.getRbacBusinessSystemName())) {
				sb.append(" AND F.RBAC_BUSINESS_SYSTEM_NAME LIKE ?");
				params.add("%"
						+ rbacUserRoleBusinessSystem
								.getRbacBusinessSystemName() + "%");
			}

			if (rbacUserRoleBusinessSystem.getRbacUserRoleBusSysId() != null) {
				sb.append(" AND E.RBAC_USER_ROLE_BUS_SYS_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacUserRoleBusSysId());
			}

			if (rbacUserRoleBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND E.RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacBusinessSystemId());
			}

			if (rbacUserRoleBusinessSystem.getRbacUserRoleId() != null) {
				sb.append(" AND E.RBAC_USER_ROLE_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacUserRoleId());
			}

			sb.append(" ORDER BY E.RBAC_USER_ROLE_BUS_SYS_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacUserRoleBusinessSystem.class);
		}

		return null;

	}

	@Override
	public List<RbacUserRoleBusinessSystem> queryRbacUserRoleBusinessSystemList(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {

		if (null != rbacUserRoleBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_USER_ROLE_BUSINESS_SYS WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacUserRoleBusinessSystem.getRbacUserRoleBusSysId() != null) {
				sb.append(" AND RBAC_USER_ROLE_BUS_SYS_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacUserRoleBusSysId());
			}

			if (rbacUserRoleBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacBusinessSystemId());
			}

			if (rbacUserRoleBusinessSystem.getRbacUserRoleId() != null) {
				sb.append(" AND RBAC_USER_ROLE_ID = ?");
				params.add(rbacUserRoleBusinessSystem.getRbacUserRoleId());
			}

			sb.append(" ORDER BY RBAC_USER_ROLE_BUS_SYS_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacUserRoleBusinessSystem.class);
		}

		return null;

	}

	@Override
	public RbacUserRoleBusinessSystem queryRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		List<RbacUserRoleBusinessSystem> rbacUserRoleBusinessSystemList = this
				.queryRbacUserRoleBusinessSystemList(rbacUserRoleBusinessSystem);
		if (null != rbacUserRoleBusinessSystemList
				&& rbacUserRoleBusinessSystemList.size() > 0) {
			return rbacUserRoleBusinessSystemList.get(0);
		}
		return null;
	}

}
