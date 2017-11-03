package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemResourceDao;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;

@Repository("rbacBusinessSystemResourceDao")
@Transactional
public class RbacBusinessSystemResourceDaoImpl extends BaseDaoImpl implements
		RbacBusinessSystemResourceDao {

	@Override
	public PageInfo queryPageInfoRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource,
			int currentPage, int pageSize) {

		if (null != rbacBusinessSystemResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT A.RBAC_RESOURCE_CODE,A.RBAC_RESOURCE_NAME,B.RBAC_BUSINESS_SYSTEM_CODE,B.RBAC_BUSINESS_SYSTEM_NAME,C.* FROM RBAC_RESOURCE A,RBAC_BUSINESS_SYSTEM B,RBAC_BUSINESS_SYSTEM_RESOURCE C");
			sb.append(" WHERE A.RBAC_RESOURCE_ID = C.RBAC_RESOURCE_ID AND B.RBAC_BUSINESS_SYSTEM_ID = C.RBAC_BUSINESS_SYSTEM_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(rbacBusinessSystemResource
					.getRbacResourceCode())) {
				sb.append(" AND A.RBAC_RESOURCE_CODE = ?");
				params.add(rbacBusinessSystemResource.getRbacResourceCode());
			}

			if (!StrUtil.isEmpty(rbacBusinessSystemResource
					.getRbacResourceName())) {
				sb.append(" AND A.RBAC_RESOURCE_NAME LIKE ?");
				params.add("%"
						+ rbacBusinessSystemResource.getRbacResourceName()
						+ "%");
			}

			if (!StrUtil.isEmpty(rbacBusinessSystemResource
					.getRbacBusinessSystemCode())) {
				sb.append(" AND B.RBAC_BUSINESS_SYSTEM_CODE = ?");
				params.add(rbacBusinessSystemResource
						.getRbacBusinessSystemCode());
			}

			if (!StrUtil.isEmpty(rbacBusinessSystemResource
					.getRbacBusinessSystemName())) {
				sb.append(" AND B.RBAC_BUSINESS_SYSTEM_NAME LIKE ?");
				params.add("%"
						+ rbacBusinessSystemResource
								.getRbacBusinessSystemName() + "%");
			}

			if (rbacBusinessSystemResource.getRbacBusinessSysResourceId() != null) {
				sb.append(" AND C.RBAC_BUSINESS_SYSTEM_RESOURCE_ID = ?");
				params.add(rbacBusinessSystemResource
						.getRbacBusinessSysResourceId());
			}

			if (rbacBusinessSystemResource.getRbacResourceId() != null) {
				sb.append(" AND C.RBAC_RESOURCE_ID = ?");
				params.add(rbacBusinessSystemResource.getRbacResourceId());
			}

			if (rbacBusinessSystemResource.getRbacBusinessSystemId() != null) {
				sb.append(" AND C.RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemResource.getRbacBusinessSystemId());
			}

			// sb.append(" ORDER BY C.RBAC_BUSINESS_SYSTEM_RESOURCE_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacBusinessSystemResource.class);
		}

		return null;

	}

	@Override
	public List<RbacBusinessSystemResource> queryRbacBusinessSystemResourceList(
			RbacBusinessSystemResource rbacBusinessSystemResource) {

		if (null != rbacBusinessSystemResource) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_BUSINESS_SYSTEM_RESOURCE WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacBusinessSystemResource.getRbacBusinessSysResourceId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_RESOURCE_ID = ?");
				params.add(rbacBusinessSystemResource
						.getRbacBusinessSysResourceId());
			}

			if (rbacBusinessSystemResource.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacBusinessSystemResource.getRbacResourceId());
			}

			if (rbacBusinessSystemResource.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemResource.getRbacBusinessSystemId());
			}

			// sb.append(" ORDER BY RBAC_BUSINESS_SYSTEM_RESOURCE_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacBusinessSystemResource.class);
		}

		return null;

	}

	@Override
	public RbacBusinessSystemResource queryRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource) {
		List<RbacBusinessSystemResource> rbacBusinessSystemResourceList = this
				.queryRbacBusinessSystemResourceList(rbacBusinessSystemResource);
		if (null != rbacBusinessSystemResourceList
				&& rbacBusinessSystemResourceList.size() > 0) {
			return rbacBusinessSystemResourceList.get(0);
		}
		return null;
	}

}
