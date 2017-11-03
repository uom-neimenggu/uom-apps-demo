package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionDao;
import cn.ffcs.uom.rolePermission.model.RbacPermission;

@Repository("rbacPermissionDao")
@Transactional
public class RbacPermissionDaoImpl extends BaseDaoImpl implements
		RbacPermissionDao {

	@Override
	public PageInfo queryPageInfoRbacPermission(RbacPermission rbacPermission,
			int currentPage, int pageSize) {

		if (null != rbacPermission) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermission.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermission.getRbacPermissionId());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionCode())) {
				sb.append(" AND RBAC_PERMISSION_CODE = ?");
				params.add(rbacPermission.getRbacPermissionCode());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionName())) {
				sb.append(" AND RBAC_PERMISSION_NAME LIKE ?");
				params.add("%" + rbacPermission.getRbacPermissionName() + "%");
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionBean())) {
				sb.append(" AND RBAC_PERMISSION_BEAN = ?");
				params.add(rbacPermission.getRbacPermissionBean());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionType())) {
				sb.append(" AND RBAC_PERMISSION_TYPE = ?");
				params.add(rbacPermission.getRbacPermissionType());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacPermission.class);
		}

		return null;

	}

	@Override
	public List<RbacPermission> queryRbacPermissionList(
			RbacPermission rbacPermission) {

		if (null != rbacPermission) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermission.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermission.getRbacPermissionId());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionCode())) {
				sb.append(" AND RBAC_PERMISSION_CODE = ?");
				params.add(rbacPermission.getRbacPermissionCode());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionName())) {
				sb.append(" AND RBAC_PERMISSION_NAME = ?");
				params.add(rbacPermission.getRbacPermissionName());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionBean())) {
				sb.append(" AND RBAC_PERMISSION_BEAN = ?");
				params.add(rbacPermission.getRbacPermissionBean());
			}

			if (!StrUtil.isEmpty(rbacPermission.getRbacPermissionType())) {
				sb.append(" AND RBAC_PERMISSION_TYPE = ?");
				params.add(rbacPermission.getRbacPermissionType());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacPermission.class);
		}

		return null;

	}

	@Override
	public RbacPermission queryRbacPermission(RbacPermission rbacPermission) {
		List<RbacPermission> rbacPermissionList = this
				.queryRbacPermissionList(rbacPermission);
		if (null != rbacPermissionList && rbacPermissionList.size() > 0) {
			return rbacPermissionList.get(0);
		}
		return null;
	}

}
