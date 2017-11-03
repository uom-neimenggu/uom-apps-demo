package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacResourceDao;
import cn.ffcs.uom.rolePermission.model.RbacResource;

@Repository("rbacResourceDao")
@Transactional
public class RbacResourceDaoImpl extends BaseDaoImpl implements RbacResourceDao {

	@Override
	public PageInfo queryPageInfoRbacResource(RbacResource rbacResource,
			int currentPage, int pageSize) {

		if (null != rbacResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_RESOURCE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacResource.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacResource.getRbacResourceId());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceCode())) {
				sb.append(" AND RBAC_RESOURCE_CODE = ?");
				params.add(rbacResource.getRbacResourceCode());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceName())) {
				sb.append(" AND RBAC_RESOURCE_NAME LIKE ?");
				params.add("%" + rbacResource.getRbacResourceName() + "%");
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceUrl())) {
				sb.append(" AND RBAC_RESOURCE_URL LIKE ?");
				params.add("%" + rbacResource.getRbacResourceUrl() + "%");
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceLeaf())) {
				sb.append(" AND RBAC_RESOURCE_LEAF = ?");
				params.add(rbacResource.getRbacResourceLeaf());
			}

			sb.append(" ORDER BY RBAC_RESOURCE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacResource.class);
		}

		return null;

	}

	@Override
	public List<RbacResource> queryRbacResourceList(RbacResource rbacResource) {

		if (null != rbacResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_RESOURCE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacResource.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacResource.getRbacResourceId());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceCode())) {
				sb.append(" AND RBAC_RESOURCE_CODE = ?");
				params.add(rbacResource.getRbacResourceCode());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceName())) {
				sb.append(" AND RBAC_RESOURCE_NAME = ?");
				params.add(rbacResource.getRbacResourceName());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceUrl())) {
				sb.append(" AND RBAC_RESOURCE_URL = ?");
				params.add(rbacResource.getRbacResourceUrl());
			}

			if (!StrUtil.isEmpty(rbacResource.getRbacResourceLeaf())) {
				sb.append(" AND RBAC_RESOURCE_LEAF = ?");
				params.add(rbacResource.getRbacResourceLeaf());
			}

			sb.append(" ORDER BY RBAC_RESOURCE_ID");

			return super
					.jdbcFindList(sb.toString(), params, RbacResource.class);
		}

		return null;

	}

	@Override
	public RbacResource queryRbacResource(RbacResource rbacResource) {
		List<RbacResource> rbacResourceList = this
				.queryRbacResourceList(rbacResource);
		if (null != rbacResourceList && rbacResourceList.size() > 0) {
			return rbacResourceList.get(0);
		}
		return null;
	}

}
