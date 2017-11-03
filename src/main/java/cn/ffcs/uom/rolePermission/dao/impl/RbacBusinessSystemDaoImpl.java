package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemDao;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

@Repository("rbacBusinessSystemDao")
@Transactional
public class RbacBusinessSystemDaoImpl extends BaseDaoImpl implements
		RbacBusinessSystemDao {

	@Override
	public PageInfo queryPageInfoRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem, int currentPage, int pageSize) {

		if (null != rbacBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_BUSINESS_SYSTEM WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemId());
			}

			if (!StrUtil
					.isEmpty(rbacBusinessSystem.getRbacBusinessSystemCode())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_CODE = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemCode());
			}

			if (!StrUtil
					.isEmpty(rbacBusinessSystem.getRbacBusinessSystemName())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_NAME LIKE ?");
				params.add("%" + rbacBusinessSystem.getRbacBusinessSystemName()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacBusinessSystem.getRbacBusinessSystemUrl())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_URL LIKE ?");
				params.add("%" + rbacBusinessSystem.getRbacBusinessSystemUrl()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacBusinessSystem
					.getRbacBusinessSystemDomain())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_DOMAIN = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemDomain());
			}

			sb.append(" ORDER BY RBAC_BUSINESS_SYSTEM_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacBusinessSystem.class);
		}

		return null;

	}

	@Override
	public List<RbacBusinessSystem> queryRbacBusinessSystemList(
			RbacBusinessSystem rbacBusinessSystem) {

		if (null != rbacBusinessSystem) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_BUSINESS_SYSTEM WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacBusinessSystem.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemId());
			}

			if (!StrUtil
					.isEmpty(rbacBusinessSystem.getRbacBusinessSystemCode())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_CODE = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemCode());
			}

			if (!StrUtil
					.isEmpty(rbacBusinessSystem.getRbacBusinessSystemName())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_NAME = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemName());
			}

			if (!StrUtil.isEmpty(rbacBusinessSystem.getRbacBusinessSystemUrl())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_URL = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemUrl());
			}

			if (!StrUtil.isEmpty(rbacBusinessSystem
					.getRbacBusinessSystemDomain())) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_DOMAIN = ?");
				params.add(rbacBusinessSystem.getRbacBusinessSystemDomain());
			}

			sb.append(" ORDER BY RBAC_BUSINESS_SYSTEM_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacBusinessSystem.class);
		}

		return null;

	}

	@Override
	public RbacBusinessSystem queryRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem) {
		List<RbacBusinessSystem> rbacBusinessSystemList = this
				.queryRbacBusinessSystemList(rbacBusinessSystem);
		if (null != rbacBusinessSystemList && rbacBusinessSystemList.size() > 0) {
			return rbacBusinessSystemList.get(0);
		}
		return null;
	}

}
