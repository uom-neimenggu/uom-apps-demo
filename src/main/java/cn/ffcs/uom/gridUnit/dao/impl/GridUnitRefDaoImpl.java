package cn.ffcs.uom.gridUnit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.dao.GridUnitRefDao;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;

@Repository("gridUnitRefDao")
public class GridUnitRefDaoImpl extends BaseDaoImpl implements GridUnitRefDao {

	@Override
	public PageInfo queryPageInfoByGridUnitRef(GridUnitRef gridUnitRef,
			int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT GUR.*,GU.GRID_NAME,ORG.ORG_NAME,ORG.ORG_CODE FROM GRID_UNIT_REF GUR,GRID_UNIT GU,ORGANIZATION ORG WHERE GUR.ORG_ID = GU.GRID_UNIT_ID");
		sb.append("  AND GUR.RELA_ORG_ID = ORG.ORG_ID AND GUR.STATUS_CD = ? AND GU.STATUS_CD = ? AND ORG.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (gridUnitRef != null) {

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getGridUnitRefId())) {
				sb.append(" AND GUR.GRID_UNIT_REF_ID = ?");
				params.add(gridUnitRef.getGridUnitRefId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgId())) {
				sb.append(" AND GUR.ORG_ID = ?");
				params.add(gridUnitRef.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getMmeFid())) {
				sb.append(" AND GUR.MME_FID = ?");
				params.add(gridUnitRef.getMmeFid());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getRelaOrgId())) {
				sb.append(" AND GUR.RELA_ORG_ID = ?");
				params.add(gridUnitRef.getRelaOrgId());
			}

			sb.append(" AND GU.AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (gridUnitRef.getPermissionTelcomRegion() != null
					&& gridUnitRef.getPermissionTelcomRegion()
							.getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(gridUnitRef.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(gridUnitRef.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sb.append("  AND CASCADE_VALUE = ?");
				params.add(gridUnitRef.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sb.append(")");

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getGridName())) {
				sb.append(" AND GU.GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnitRef.getGridName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(gridUnitRef.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgName())) {
				sb.append(" AND ORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnitRef.getOrgName()) + "%");
			}

			sb.append(" ORDER BY GRID_UNIT_REF_ID ASC");

		}

		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, GridUnitRef.class);

	}

	@Override
	public List<GridUnitRef> queryGridUnitRefList(GridUnitRef gridUnitRef) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT GUR.*,GU.GRID_NAME,ORG.ORG_NAME,ORG.ORG_CODE FROM GRID_UNIT_REF GUR,GRID_UNIT GU,ORGANIZATION ORG WHERE GUR.ORG_ID = GU.GRID_UNIT_ID");
		sb.append("  AND GUR.RELA_ORG_ID = ORG.ORG_ID AND GUR.STATUS_CD = ? AND GU.STATUS_CD = ? AND ORG.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (gridUnitRef != null) {

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getGridUnitRefId())) {
				sb.append(" AND GUR.GRID_UNIT_REF_ID = ?");
				params.add(gridUnitRef.getGridUnitRefId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgId())) {
				sb.append(" AND GUR.ORG_ID = ?");
				params.add(gridUnitRef.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getMmeFid())) {
				sb.append(" AND GUR.MME_FID = ?");
				params.add(gridUnitRef.getMmeFid());
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getRelaOrgId())) {
				sb.append(" AND GUR.RELA_ORG_ID = ?");
				params.add(gridUnitRef.getRelaOrgId());
			}

			sb.append(" AND GU.AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (gridUnitRef.getPermissionTelcomRegion() != null
					&& gridUnitRef.getPermissionTelcomRegion()
							.getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(gridUnitRef.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(gridUnitRef.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sb.append("  AND CASCADE_VALUE = ?");
				params.add(gridUnitRef.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sb.append(")");

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getGridName())) {
				sb.append(" AND GU.GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnitRef.getGridName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(gridUnitRef.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(gridUnitRef.getOrgName())) {
				sb.append(" AND ORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnitRef.getOrgName()) + "%");
			}

			sb.append(" ORDER BY GRID_UNIT_REF_ID ASC");

		}

		return super.jdbcFindList(sb.toString(), params, GridUnitRef.class);

	}
}
