package cn.ffcs.uom.gridUnit.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.gridUnit.dao.GridUnitDao;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Repository("gridUnitDao")
public class GridUnitDaoImpl extends BaseDaoImpl implements GridUnitDao {

	@Override
	public PageInfo queryPageInfoByGridUnit(GridUnit gridUnit, int currentPage,
			int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM GRID_UNIT WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (gridUnit != null) {

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridUnitId())) {
				sb.append(" AND GRID_UNIT_ID = ?");
				params.add(gridUnit.getGridUnitId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getMmeFid())) {
				sb.append(" AND MME_FID = ?");
				params.add(gridUnit.getMmeFid());
			}

			sb.append(" AND AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (gridUnit.getPermissionTelcomRegion() != null
					&& gridUnit.getPermissionTelcomRegion().getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(gridUnit.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(gridUnit.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sb.append("  AND CASCADE_VALUE = ?");
				params.add(gridUnit.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sb.append(")");

			if (!StrUtil.isNullOrEmpty(gridUnit.getAreaName())) {
				sb.append(" AND AREA_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getAreaName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getSubareaName())) {
				sb.append(" AND SUBAREA_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getSubareaName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridName())) {
				sb.append(" AND GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridType())) {
				sb.append(" AND GRID_TYPE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridType()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridSubtype())) {
				sb.append(" AND GRID_SUBTYPE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridSubtype()) + "%");
			}

			sb.append(" ORDER BY GRID_UNIT_ID ASC");

		}

		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, GridUnit.class);

	}

	@Override
	public List<Map<String, Object>> queryGridUnitNoStaffTranList(
			TelcomRegion telcomRegion) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT GU.GRID_UNIT_ID 网格单元ID,GU.AREA_NAME 分公司,GU.SUBAREA_NAME 子公司,");
		sb.append("GU.MME_FID 网格单元标识,GU.GRID_NAME 网格单元名称,");
		sb.append("to_char(GU.CREATE_DATE, 'yyyy-mm-dd hh24:mi:ss') 生效时间  ");
		sb.append(" FROM GRID_UNIT GU WHERE GU.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		sb.append(" AND NOT EXISTS (SELECT 1 FROM STAFF_ORGANIZATION_TRAN SOT WHERE SOT.ORG_ID = GU.GRID_UNIT_ID AND SOT.STATUS_CD = ?)");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		sb.append(" AND GU.AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(CascadeRelationConstants.RELA_CD_6);

		if (telcomRegion != null
				&& telcomRegion.getTelcomRegionId() != null
				&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
						.equals(telcomRegion.getTelcomRegionId())
				&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
						.equals(telcomRegion.getTelcomRegionId())) {
			sb.append("  AND CASCADE_VALUE = ?");
			params.add(telcomRegion.getTelcomRegionId());
		}

		sb.append(")");

		return this.getJdbcTemplate().queryForList(sb.toString(),
				params.toArray());
	}

	@Override
	public List<GridUnit> queryGridUnitList(GridUnit gridUnit) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM GRID_UNIT WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (gridUnit != null) {

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridUnitId())) {
				sb.append(" AND GRID_UNIT_ID = ?");
				params.add(gridUnit.getGridUnitId());
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getMmeFid())) {
				sb.append(" AND MME_FID = ?");
				params.add(gridUnit.getMmeFid());
			}

			sb.append(" AND AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (gridUnit.getPermissionTelcomRegion() != null
					&& gridUnit.getPermissionTelcomRegion().getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(gridUnit.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(gridUnit.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sb.append("  AND CASCADE_VALUE = ?");
				params.add(gridUnit.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sb.append(")");

			if (!StrUtil.isNullOrEmpty(gridUnit.getAreaName())) {
				sb.append(" AND AREA_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getAreaName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getSubareaName())) {
				sb.append(" AND SUBAREA_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getSubareaName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridName())) {
				sb.append(" AND GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridType())) {
				sb.append(" AND GRID_TYPE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridType()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(gridUnit.getGridSubtype())) {
				sb.append(" AND GRID_SUBTYPE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(gridUnit.getGridSubtype()) + "%");
			}

			sb.append(" ORDER BY GRID_UNIT_ID ASC");

		}

		return super.jdbcFindList(sb.toString(), params, GridUnit.class);

	}
}
