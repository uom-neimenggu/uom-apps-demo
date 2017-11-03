package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRolePolitLocationDao;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;

@Repository("rbacRolePolitLocationDao")
@Transactional
public class RbacRolePolitLocationDaoImpl extends BaseDaoImpl implements
		RbacRolePolitLocationDao {

	@Override
	public PageInfo queryPageInfoRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation, int currentPage,
			int pageSize) {

		if (null != rbacRolePolitLocation) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.LOCATION_CODE,B.LOCATION_NAME,C.* FROM RBAC_ROLE A,POLITICAL_LOCATION B,RBAC_ROLE_POLIT_LOCATION C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.LOCATION_ID = C.RBAC_POLIT_LOCATION_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacRolePolitLocation.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacRolePolitLocation.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRolePolitLocation.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRolePolitLocation.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacRolePolitLocation.getLocationCode())) {
				sb.append(" AND B.LOCATION_CODE = ?");
				params.add(rbacRolePolitLocation.getLocationCode());
			}

			if (!StrUtil.isEmpty(rbacRolePolitLocation.getLocationName())) {
				sb.append(" AND B.LOCATION_NAME LIKE ?");
				params.add("%" + rbacRolePolitLocation.getLocationName() + "%");
			}

			if (rbacRolePolitLocation.getRbacRolePolitLocationId() != null) {
				sb.append(" AND C.RBAC_ROLE_POLIT_LOCATION_ID = ?");
				params.add(rbacRolePolitLocation.getRbacRolePolitLocationId());
			}

			if (rbacRolePolitLocation.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacRolePolitLocation.getRbacRoleId());
			}

			if (rbacRolePolitLocation.getRbacPolitLocationId() != null) {
				sb.append(" AND C.RBAC_POLIT_LOCATION_ID = ?");
				params.add(rbacRolePolitLocation.getRbacPolitLocationId());
			}

			sb.append(" ORDER BY C.RBAC_ROLE_POLIT_LOCATION_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRolePolitLocation.class);
		}

		return null;

	}

	@Override
	public List<RbacRolePolitLocation> queryRbacRolePolitLocationList(
			RbacRolePolitLocation rbacRolePolitLocation) {

		if (null != rbacRolePolitLocation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_POLIT_LOCATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRolePolitLocation.getRbacRolePolitLocationId() != null) {
				sb.append(" AND RBAC_ROLE_POLIT_LOCATION_ID = ?");
				params.add(rbacRolePolitLocation.getRbacRolePolitLocationId());
			}

			if (rbacRolePolitLocation.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRolePolitLocation.getRbacRoleId());
			}

			if (rbacRolePolitLocation.getRbacPolitLocationId() != null) {
				sb.append(" AND RBAC_POLIT_LOCATION_ID = ?");
				params.add(rbacRolePolitLocation.getRbacPolitLocationId());
			}

			sb.append(" ORDER BY RBAC_ROLE_POLIT_LOCATION_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRolePolitLocation.class);
		}

		return null;

	}

	@Override
	public RbacRolePolitLocation queryRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation) {
		List<RbacRolePolitLocation> rbacRolePolitLocationList = this
				.queryRbacRolePolitLocationList(rbacRolePolitLocation);
		if (null != rbacRolePolitLocationList
				&& rbacRolePolitLocationList.size() > 0) {
			return rbacRolePolitLocationList.get(0);
		}
		return null;
	}

}
