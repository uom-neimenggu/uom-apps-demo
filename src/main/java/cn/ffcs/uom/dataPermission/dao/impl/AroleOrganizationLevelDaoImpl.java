package cn.ffcs.uom.dataPermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationLevelDao;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;

@Repository("aroleOrganizationLevelDao")
public class AroleOrganizationLevelDaoImpl extends BaseDaoImpl implements
		AroleOrganizationLevelDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PageInfo queryPageInfoByAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel, int currentPage,
			int pageSize) throws Exception {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM AROLE_ORGANIZATION_LEVEL WHERE STATUS_CD = ?");

		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (aroleOrganizationLevel != null) {

			if (aroleOrganizationLevel.getAroleOrgLevelId() != null) {
				sql.append(" AND AROLE_ORG_LEVEL_ID = ?");
				params.add(aroleOrganizationLevel.getAroleOrgLevelId());
			}

			if (aroleOrganizationLevel.getAroleId() != null) {
				sql.append(" AND AROLE_ID = ?");
				params.add(aroleOrganizationLevel.getAroleId());
			}

			if (aroleOrganizationLevel.getOrgId() != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(aroleOrganizationLevel.getOrgId());
			}

			if (aroleOrganizationLevel.getLowerLevel() != null) {
				sql.append(" AND LOWER_LEVEL = ?");
				params.add(aroleOrganizationLevel.getLowerLevel());
			}
			if (aroleOrganizationLevel.getHigherLevel() != null) {
				sql.append(" AND HIGHER_LEVEL = ?");
				params.add(aroleOrganizationLevel.getHigherLevel());
			}
			if (!StrUtil.isEmpty(aroleOrganizationLevel.getRelaCd())) {
				sql.append(" AND RELA_CD = ?");
				params.add(aroleOrganizationLevel.getRelaCd());
			}

		}

		sql.append(" ORDER BY AROLE_ORG_LEVEL_ID");

		return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
				pageSize, AroleOrganizationLevel.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<AroleOrganizationLevel> queryAroleOrganizationLevelList(
			AroleOrganizationLevel aroleOrganizationLevel) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM AROLE_ORGANIZATION_LEVEL WHERE STATUS_CD = ?");

		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (aroleOrganizationLevel != null) {

			if (aroleOrganizationLevel.getAroleOrgLevelId() != null) {
				sql.append(" AND AROLE_ORG_LEVEL_ID = ?");
				params.add(aroleOrganizationLevel.getAroleOrgLevelId());
			}

			if (aroleOrganizationLevel.getAroleId() != null) {
				sql.append(" AND AROLE_ID = ?");
				params.add(aroleOrganizationLevel.getAroleId());
			}

			if (aroleOrganizationLevel.getOrgId() != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(aroleOrganizationLevel.getOrgId());
			}

			if (aroleOrganizationLevel.getLowerLevel() != null) {
				sql.append(" AND LOWER_LEVEL = ?");
				params.add(aroleOrganizationLevel.getLowerLevel());
			}
			if (aroleOrganizationLevel.getHigherLevel() != null) {
				sql.append(" AND HIGHER_LEVEL = ?");
				params.add(aroleOrganizationLevel.getHigherLevel());
			}
			if (!StrUtil.isEmpty(aroleOrganizationLevel.getRelaCd())) {
				sql.append(" AND RELA_CD = ?");
				params.add(aroleOrganizationLevel.getRelaCd());
			}

		}

		sql.append(" ORDER BY AROLE_ORG_LEVEL_ID");

		return super.jdbcFindList(sql.toString(), params,
				AroleOrganizationLevel.class);
	}

	@Override
	public AroleOrganizationLevel queryAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel) {
		List<AroleOrganizationLevel> aroleOrganizationLevelList = this
				.queryAroleOrganizationLevelList(aroleOrganizationLevel);
		if (null != aroleOrganizationLevelList
				&& aroleOrganizationLevelList.size() > 0) {
			return aroleOrganizationLevelList.get(0);
		}
		return null;
	}

	@Override
	public void addAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel) {
		aroleOrganizationLevel.addOnly();
	}

	@Override
	public void removeAroleOrganizationLevel(
			AroleOrganizationLevel aroleOrganizationLevel) {
		aroleOrganizationLevel.removeOnly();
	}

}
