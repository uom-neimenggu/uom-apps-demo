package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleRelationDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Repository("rbacRoleRelationDao")
@Transactional
public class RbacRoleRelationDaoImpl extends BaseDaoImpl implements
		RbacRoleRelationDao {

	@Override
	public PageInfo queryPageInfoRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation, int currentPage, int pageSize) {

		if (null != rbacRoleRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleRelation.getRbacRoleRelaId() != null) {
				sb.append(" AND RBAC_ROLE_RELA_ID = ?");
				params.add(rbacRoleRelation.getRbacRoleRelaId());
			}

			if (rbacRoleRelation.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleRelation.getRbacRoleId());
			}

			if (rbacRoleRelation.getRbacParentRoleId() != null) {
				sb.append(" AND RBAC_PARENT_ROLE_ID = ?");
				params.add(rbacRoleRelation.getRbacParentRoleId());
			}

			sb.append(" ORDER BY RBAC_ROLE_RELA_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleRelation.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleRelation> queryRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation) {

		if (null != rbacRoleRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleRelation.getRbacRoleRelaId() != null) {
				sb.append(" AND RBAC_ROLE_RELA_ID = ?");
				params.add(rbacRoleRelation.getRbacRoleRelaId());
			}

			if (rbacRoleRelation.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleRelation.getRbacRoleId());
			}

			if (rbacRoleRelation.getRbacParentRoleId() != null) {
				sb.append(" AND RBAC_PARENT_ROLE_ID = ?");
				params.add(rbacRoleRelation.getRbacParentRoleId());
			}

			sb.append(" ORDER BY RBAC_ROLE_RELA_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleRelation.class);
		}

		return null;

	}

	@Override
	public RbacRoleRelation queryRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation) {
		List<RbacRoleRelation> rbacRoleRelationList = this
				.queryRbacRoleRelationList(rbacRoleRelation);
		if (null != rbacRoleRelationList && rbacRoleRelationList.size() > 0) {
			return rbacRoleRelationList.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<RbacRoleRelation> querySubTreeRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation) {
		List params = new ArrayList();
		String sql = "SELECT * FROM (SELECT * FROM RBAC_ROLE_RELATION WHERE STATUS_CD = ?) A START WITH A.RBAC_PARENT_ROLE_ID = ? CONNECT BY PRIOR A.RBAC_ROLE_ID = A.RBAC_PARENT_ROLE_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rbacRoleRelation.getRbacRoleId());
		return super.jdbcFindList(sql, params, RbacRoleRelation.class);
	}

}
