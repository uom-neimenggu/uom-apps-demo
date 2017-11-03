package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacResourceRelationDao;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Repository("rbacResourceRelationDao")
@Transactional
public class RbacResourceRelationDaoImpl extends BaseDaoImpl implements
		RbacResourceRelationDao {

	@Override
	public PageInfo queryPageInfoRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation, int currentPage,
			int pageSize) {

		if (null != rbacResourceRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_RESOURCE_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacResourceRelation.getRbacResourceRelaId() != null) {
				sb.append(" AND RBAC_RESOURCE_RELA_ID = ?");
				params.add(rbacResourceRelation.getRbacResourceRelaId());
			}

			if (rbacResourceRelation.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacResourceRelation.getRbacResourceId());
			}

			if (rbacResourceRelation.getRbacParentResourceId() != null) {
				sb.append(" AND RBAC_PARENT_RESOURCE_ID = ?");
				params.add(rbacResourceRelation.getRbacParentResourceId());
			}

			sb.append(" ORDER BY RBAC_RESOURCE_RELA_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacResourceRelation.class);
		}

		return null;

	}

	@Override
	public List<RbacResourceRelation> queryRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation) {

		if (null != rbacResourceRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_RESOURCE_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacResourceRelation.getRbacResourceRelaId() != null) {
				sb.append(" AND RBAC_RESOURCE_RELA_ID = ?");
				params.add(rbacResourceRelation.getRbacResourceRelaId());
			}

			if (rbacResourceRelation.getRbacResourceId() != null) {
				sb.append(" AND RBAC_RESOURCE_ID = ?");
				params.add(rbacResourceRelation.getRbacResourceId());
			}

			if (rbacResourceRelation.getRbacParentResourceId() != null) {
				sb.append(" AND RBAC_PARENT_RESOURCE_ID = ?");
				params.add(rbacResourceRelation.getRbacParentResourceId());
			}

			sb.append(" ORDER BY RBAC_RESOURCE_RELA_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacResourceRelation.class);
		}

		return null;

	}

	@Override
	public RbacResourceRelation queryRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		List<RbacResourceRelation> rbacResourceRelationList = this
				.queryRbacResourceRelationList(rbacResourceRelation);
		if (null != rbacResourceRelationList
				&& rbacResourceRelationList.size() > 0) {
			return rbacResourceRelationList.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<RbacResourceRelation> querySubTreeRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation) {
		List params = new ArrayList();
		String sql = "SELECT * FROM (SELECT * FROM RBAC_RESOURCE_RELATION WHERE STATUS_CD = ?) A START WITH A.RBAC_PARENT_RESOURCE_ID = ? CONNECT BY PRIOR A.RBAC_RESOURCE_ID = A.RBAC_PARENT_RESOURCE_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rbacResourceRelation.getRbacResourceId());
		return super.jdbcFindList(sql, params, RbacResourceRelation.class);
	}

}
