package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionResourceDao;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;

@Repository("rbacPermissionResourceDao")
@Transactional
public class RbacPermissionResourceDaoImpl extends BaseDaoImpl implements
		RbacPermissionResourceDao {

	@Override
	public PageInfo queryPageInfoRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource, int currentPage,
			int pageSize) {

		if (null != rbacPermissionResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_RESOURCE_CODE,A.RBAC_RESOURCE_NAME,B.RBAC_PERMISSION_CODE,B.RBAC_PERMISSION_NAME,C.* ");
			sb.append("FROM RBAC_RESOURCE A,RBAC_PERMISSION B,RBAC_PERMISSION_RESOURCE C,RBAC_PERMISSION_RELATION D");
			sb.append(" WHERE A.RBAC_RESOURCE_ID = C.RBAC_RESOURCE_ID AND B.RBAC_PERMISSION_ID = D.RBAC_PERMISSION_ID AND C.RBAC_PERMISSION_RELA_ID = D.RBAC_PERMISSION_RELA_ID");
			sb.append(" AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ? AND D.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacPermissionResource.getRbacResourceCode())) {
				sb.append(" AND A.RBAC_RESOURCE_CODE = ?");
				params.add(rbacPermissionResource.getRbacResourceCode());
			}

			if (!StrUtil.isEmpty(rbacPermissionResource.getRbacResourceName())) {
				sb.append(" AND A.RBAC_RESOURCE_NAME LIKE ?");
				params.add("%" + rbacPermissionResource.getRbacResourceName()
						+ "%");
			}

			if (!StrUtil
					.isEmpty(rbacPermissionResource.getRbacPermissionCode())) {
				sb.append(" AND B.RBAC_PERMISSION_CODE = ?");
				params.add(rbacPermissionResource.getRbacPermissionCode());
			}

			if (!StrUtil
					.isEmpty(rbacPermissionResource.getRbacPermissionName())) {
				sb.append(" AND B.RBAC_PERMISSION_NAME LIKE ?");
				params.add("%" + rbacPermissionResource.getRbacPermissionName()
						+ "%");
			}

			if (rbacPermissionResource.getRbacPermissionResourceId() != null) {
				sb.append(" AND C.RBAC_PERMISSION_RESOURCE_ID = ?");
				params.add(rbacPermissionResource.getRbacPermissionResourceId());
			}

			if (rbacPermissionResource.getRbacResourceId() != null) {
				sb.append(" AND C.RBAC_RESOURCE_ID = ?");
				params.add(rbacPermissionResource.getRbacResourceId());
			}

			if (rbacPermissionResource.getRbacPermissionRelaId() != null) {
				sb.append(" AND C.RBAC_PERMISSION_RELA_ID = ?");
				params.add(rbacPermissionResource.getRbacPermissionRelaId());
			}

			sb.append(" ORDER BY C.RBAC_PERMISSION_RESOURCE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacPermissionResource.class);
		}

		return null;

	}

	@Override
	public List<RbacPermissionResource> queryRbacPermissionResourceList(
			RbacPermissionResource rbacPermissionResource) {

		if (null != rbacPermissionResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_RESOURCE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionResource.getRbacPermissionResourceId() != null) {
				sb.append(" AND RBAC_PERMISSION_RESOURCE_ID = ?");
				params.add(rbacPermissionResource.getRbacPermissionResourceId());
			}

			if (rbacPermissionResource.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacPermissionResource.getRbacResourceId());
			}

			if (rbacPermissionResource.getRbacPermissionRelaId() != null) {
				sb.append(" AND RBAC_PERMISSION_RELA_ID = ?");
				params.add(rbacPermissionResource.getRbacPermissionRelaId());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_RESOURCE_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacPermissionResource.class);
		}

		return null;

	}

	@Override
	public RbacPermissionResource queryRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource) {
		List<RbacPermissionResource> rbacPermissionResourceList = this
				.queryRbacPermissionResourceList(rbacPermissionResource);
		if (null != rbacPermissionResourceList
				&& rbacPermissionResourceList.size() > 0) {
			return rbacPermissionResourceList.get(0);
		}
		return null;
	}

}
