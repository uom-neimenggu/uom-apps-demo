package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemRelationDao;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

@Repository("rbacBusinessSystemRelationDao")
@Transactional
public class RbacBusinessSystemRelationDaoImpl extends BaseDaoImpl implements
		RbacBusinessSystemRelationDao {

	@Override
	public PageInfo queryPageInfoRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation,
			int currentPage, int pageSize) {

		if (null != rbacBusinessSystemRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_BUSINESS_SYSTEM_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacBusinessSystemRelation.getRbacBusinessSystemRelaId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_RELA_ID = ?");
				params.add(rbacBusinessSystemRelation
						.getRbacBusinessSystemRelaId());
			}

			if (rbacBusinessSystemRelation.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemRelation.getRbacBusinessSystemId());
			}

			if (rbacBusinessSystemRelation.getRbacParentBusinessSystemId() != null) {
				sb.append(" AND RBAC_PARENT_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemRelation
						.getRbacParentBusinessSystemId());
			}

			sb.append(" ORDER BY RBAC_BUSINESS_SYSTEM_RELA_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacBusinessSystemRelation.class);
		}

		return null;

	}

	@Override
	public List<RbacBusinessSystemRelation> queryRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {

		if (null != rbacBusinessSystemRelation) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_BUSINESS_SYSTEM_RELATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacBusinessSystemRelation.getRbacBusinessSystemRelaId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_RELA_ID = ?");
				params.add(rbacBusinessSystemRelation
						.getRbacBusinessSystemRelaId());
			}

			if (rbacBusinessSystemRelation.getRbacBusinessSystemId() != null) {
				sb.append(" AND RBAC_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemRelation.getRbacBusinessSystemId());
			}

			if (rbacBusinessSystemRelation.getRbacParentBusinessSystemId() != null) {
				sb.append(" AND RBAC_PARENT_BUSINESS_SYSTEM_ID = ?");
				params.add(rbacBusinessSystemRelation
						.getRbacParentBusinessSystemId());
			}

			sb.append(" ORDER BY RBAC_BUSINESS_SYSTEM_RELA_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacBusinessSystemRelation.class);
		}

		return null;

	}

	@Override
	public RbacBusinessSystemRelation queryRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = this
				.queryRbacBusinessSystemRelationList(rbacBusinessSystemRelation);
		if (null != rbacBusinessSystemRelationList
				&& rbacBusinessSystemRelationList.size() > 0) {
			return rbacBusinessSystemRelationList.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<RbacBusinessSystemRelation> querySubTreeRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		List params = new ArrayList();
		String sql = "SELECT * FROM (SELECT * FROM RBAC_BUSINESS_SYSTEM_RELATION WHERE STATUS_CD = ?) A START WITH A.RBAC_PARENT_BUSINESS_SYSTEM_ID = ? CONNECT BY PRIOR A.RBAC_BUSINESS_SYSTEM_ID = A.RBAC_PARENT_BUSINESS_SYSTEM_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(rbacBusinessSystemRelation.getRbacBusinessSystemId());
		return super
				.jdbcFindList(sql, params, RbacBusinessSystemRelation.class);
	}

}
