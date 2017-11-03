package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleTelcomRegionDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;

@Repository("rbacRoleTelcomRegionDao")
@Transactional
public class RbacRoleTelcomRegionDaoImpl extends BaseDaoImpl implements
		RbacRoleTelcomRegionDao {

	@Override
	public PageInfo queryPageInfoRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion, int currentPage,
			int pageSize) {

		if (null != rbacRoleTelcomRegion) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_ROLE_CODE,A.RBAC_ROLE_NAME,B.REGION_CODE,B.REGION_NAME,C.* FROM RBAC_ROLE A,TELCOM_REGION B,RBAC_ROLE_TELCOM_REGION C");
			sb.append(" WHERE A.RBAC_ROLE_ID = C.RBAC_ROLE_ID AND B.TELCOM_REGION_ID = C.RBAC_TELCOM_REGION_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacRoleTelcomRegion.getRbacRoleCode())) {
				sb.append(" AND A.RBAC_ROLE_CODE = ?");
				params.add(rbacRoleTelcomRegion.getRbacRoleCode());
			}

			if (!StrUtil.isEmpty(rbacRoleTelcomRegion.getRbacRoleName())) {
				sb.append(" AND A.RBAC_ROLE_NAME LIKE ?");
				params.add("%" + rbacRoleTelcomRegion.getRbacRoleName() + "%");
			}

			if (!StrUtil.isEmpty(rbacRoleTelcomRegion.getRegionCode())) {
				sb.append(" AND B.REGION_CODE = ?");
				params.add(rbacRoleTelcomRegion.getRegionCode());
			}

			if (!StrUtil.isEmpty(rbacRoleTelcomRegion.getRegionName())) {
				sb.append(" AND B.REGION_NAME LIKE ?");
				params.add("%" + rbacRoleTelcomRegion.getRegionName() + "%");
			}

			if (rbacRoleTelcomRegion.getRbacRoleTelcomRegionId() != null) {
				sb.append(" AND C.RBAC_ROLE_TELCOM_REGION_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacRoleTelcomRegionId());
			}

			if (rbacRoleTelcomRegion.getRbacRoleId() != null) {
				sb.append(" AND C.RBAC_ROLE_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacRoleId());
			}

			if (rbacRoleTelcomRegion.getRbacTelcomRegionId() != null) {
				sb.append(" AND C.RBAC_TELCOM_REGION_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacTelcomRegionId());
			}

			sb.append(" ORDER BY C.RBAC_ROLE_TELCOM_REGION_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleTelcomRegion.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleTelcomRegion> queryRbacRoleTelcomRegionList(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {

		if (null != rbacRoleTelcomRegion) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_TELCOM_REGION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleTelcomRegion.getRbacRoleTelcomRegionId() != null) {
				sb.append(" AND RBAC_ROLE_TELCOM_REGION_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacRoleTelcomRegionId());
			}

			if (rbacRoleTelcomRegion.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacRoleId());
			}

			if (rbacRoleTelcomRegion.getRbacTelcomRegionId() != null) {
				sb.append(" AND RBAC_TELCOM_REGION_ID = ?");
				params.add(rbacRoleTelcomRegion.getRbacTelcomRegionId());
			}

			sb.append(" ORDER BY RBAC_ROLE_TELCOM_REGION_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleTelcomRegion.class);
		}

		return null;

	}

	@Override
	public RbacRoleTelcomRegion queryRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList = this
				.queryRbacRoleTelcomRegionList(rbacRoleTelcomRegion);
		if (null != rbacRoleTelcomRegionList
				&& rbacRoleTelcomRegionList.size() > 0) {
			return rbacRoleTelcomRegionList.get(0);
		}
		return null;
	}

}
