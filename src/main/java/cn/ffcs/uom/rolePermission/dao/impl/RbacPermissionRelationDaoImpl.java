package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRelationDao;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Repository("rbacPermissionRelationDao")
@Transactional
public class RbacPermissionRelationDaoImpl extends BaseDaoImpl implements
		RbacPermissionRelationDao {

	@Override
	public PageInfo queryPageInfoRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation, int currentPage,
			int pageSize) {

		if (null != rbacPermissionRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionRelation.getRbacPermissionRelaId() != null) {
				sb.append(" AND RBAC_PERMISSION_RELA_ID = ?");
				params.add(rbacPermissionRelation.getRbacPermissionRelaId());
			}

			if (rbacPermissionRelation.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionRelation.getRbacPermissionId());
			}

			if (rbacPermissionRelation.getRbacParentPermissionId() != null) {
				sb.append(" AND RBAC_PARENT_PERMISSION_ID = ?");
				params.add(rbacPermissionRelation.getRbacParentPermissionId());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_RELA_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacPermissionRelation.class);
		}

		return null;

	}

	@Override
	public List<RbacPermissionRelation> queryRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation) {

		if (null != rbacPermissionRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionRelation.getRbacPermissionRelaId() != null) {
				sb.append(" AND RBAC_PERMISSION_RELA_ID = ?");
				params.add(rbacPermissionRelation.getRbacPermissionRelaId());
			}

			if (rbacPermissionRelation.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionRelation.getRbacPermissionId());
			}

			if (rbacPermissionRelation.getRbacParentPermissionId() != null) {
				sb.append(" AND RBAC_PARENT_PERMISSION_ID = ?");
				params.add(rbacPermissionRelation.getRbacParentPermissionId());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_RELA_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacPermissionRelation.class);
		}

		return null;

	}

	@Override
	public RbacPermissionRelation queryRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		List<RbacPermissionRelation> rbacPermissionRelationList = this
				.queryRbacPermissionRelationList(rbacPermissionRelation);
		if (null != rbacPermissionRelationList
				&& rbacPermissionRelationList.size() > 0) {
			return rbacPermissionRelationList.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<RbacPermissionRelation> querySubTreeRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation) {
		List params = new ArrayList();
		String sql = "SELECT * FROM (SELECT * FROM RBAC_PERMISSION_RELATION WHERE STATUS_CD = ?) A START WITH A.RBAC_PARENT_PERMISSION_ID = ? CONNECT BY PRIOR A.RBAC_PERMISSION_ID = A.RBAC_PARENT_PERMISSION_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rbacPermissionRelation.getRbacPermissionId());
		return super.jdbcFindList(sql, params, RbacPermissionRelation.class);
	}

}
