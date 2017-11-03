package cn.ffcs.uom.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.dao.CascadeRelationDao;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.StaffOrganization;

@Repository("cascadeRelationDao")
public class CascadeRelationDaoImpl extends BaseDaoImpl implements
		CascadeRelationDao {

	@Override
	public CascadeRelation queryCascadeRelation(CascadeRelation cascadeRelation) {

		if (cascadeRelation != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM CASCADE_RELATION WHERE STATUS_CD = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeRelaId())) {
				sql.append(" AND CASCADE_RELA_ID = ?");
				params.add(cascadeRelation.getCascadeRelaId());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCd())) {
				sql.append(" AND RELA_CD = ? ");
				params.add(cascadeRelation.getRelaCd());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCdName())) {
				sql.append(" AND RELA_CD_NAME LIKE ?");
				params.add("%" + cascadeRelation.getRelaCdName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeValue())) {
				sql.append(" AND CASCADE_VALUE = ? ");
				params.add(cascadeRelation.getCascadeValue());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCascadeValue())) {
				sql.append(" AND RELA_CASCADE_VALUE = ?");
				params.add(cascadeRelation.getRelaCascadeValue());
			}

			sql.append(" ORDER BY CASCADE_RELA_ID");

			return super.jdbcFindObject(sql.toString(), params,
					CascadeRelation.class);

		}

		return null;

	}

	@Override
	public CascadeRelation queryCascadeRelationByStaffOrganization(
			CascadeRelation cascadeRelation, StaffOrganization staffOrganization) {

		if (cascadeRelation != null && staffOrganization != null
				&& staffOrganization.getStaffId() != null
				&& staffOrganization.getOrgId() != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT CR.* FROM CASCADE_RELATION CR, STAFF_POSITION SP, ORG_POSITION OP");
			sql.append(" WHERE CR.CASCADE_VALUE = OP.POSITION_ID AND OP.ORG_POSITION_ID = SP.ORG_POSITION_RELA_ID");
			sql.append(" AND CR.STATUS_CD = ? AND SP.STATUS_CD = ? AND OP.STATUS_CD = ?");
			sql.append("");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeRelaId())) {
				sql.append(" AND CR.CASCADE_RELA_ID = ?");
				params.add(cascadeRelation.getCascadeRelaId());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCd())) {
				sql.append(" AND CR.RELA_CD = ?");
				params.add(cascadeRelation.getRelaCd());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCdName())) {
				sql.append(" AND CR.RELA_CD_NAME LIKE ?");
				params.add("%" + cascadeRelation.getRelaCdName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeValue())) {
				sql.append(" AND CR.CASCADE_VALUE = ? ");
				params.add(cascadeRelation.getCascadeValue());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCascadeValue())) {
				sql.append(" AND CR.RELA_CASCADE_VALUE = ?");
				params.add(cascadeRelation.getRelaCascadeValue());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getStaffId())) {
				sql.append(" AND SP.STAFF_ID = ?");
				params.add(staffOrganization.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getOrgId())) {
				sql.append(" AND OP.ORG_ID = ?");
				params.add(staffOrganization.getOrgId());
			}

			sql.append(" ORDER BY CR.CASCADE_RELA_ID");

			return super.jdbcFindObject(sql.toString(), params,
					CascadeRelation.class);

		}

		return null;

	}

	@Override
	public List<CascadeRelation> queryCascadeRelationList(
			CascadeRelation cascadeRelation) {

		if (cascadeRelation != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM CASCADE_RELATION WHERE STATUS_CD = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeRelaId())) {
				sql.append(" AND CASCADE_RELA_ID = ?");
				params.add(cascadeRelation.getCascadeRelaId());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCd())) {
				sql.append(" AND RELA_CD = ? ");
				params.add(cascadeRelation.getRelaCd());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCdName())) {
				sql.append(" AND RELA_CD_NAME LIKE ?");
				params.add("%" + cascadeRelation.getRelaCdName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getCascadeValue())) {
				sql.append(" AND CASCADE_VALUE = ? ");
				params.add(cascadeRelation.getCascadeValue());
			}

			if (!StrUtil.isNullOrEmpty(cascadeRelation.getRelaCascadeValue())) {
				sql.append(" AND RELA_CASCADE_VALUE = ?");
				params.add(cascadeRelation.getRelaCascadeValue());
			}

			sql.append(" ORDER BY CASCADE_RELA_ID");

			return super.jdbcFindList(sql.toString(), params,
					CascadeRelation.class);

		}

		return null;

	}
}
