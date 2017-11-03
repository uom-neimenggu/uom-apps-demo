package cn.ffcs.uom.dataPermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationDao;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;

@Repository("aroleOrganizationDao")
public class AroleOrganizationDaoImpl extends BaseDaoImpl implements
		AroleOrganizationDao {

	@Override
	public List<AroleOrganization> queryAroleOrganizationList(
			AroleOrganization aroleOrganization) {

		if (null != aroleOrganization) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM AROLE_ORGANIZATION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (aroleOrganization.getAroleOrganizationId() != null) {
				sb.append(" AND AROLE_ORGANIZATION_ID = ?");
				params.add(aroleOrganization.getAroleOrganizationId());
			}

			if (aroleOrganization.getAroleId() != null) {
				sb.append(" AND AROLE_ID = ?");
				params.add(aroleOrganization.getAroleId());
			}

			if (aroleOrganization.getOrgId() != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(aroleOrganization.getOrgId());
			}

			sb.append(" ORDER BY AROLE_ORGANIZATION_ID");

			return super.jdbcFindList(sb.toString(), params,
					AroleOrganization.class);
		}

		return null;

	}

	@Override
	public AroleOrganization queryAroleOrganization(
			AroleOrganization aroleOrganization) {
		List<AroleOrganization> aroleOrganizationList = this
				.queryAroleOrganizationList(aroleOrganization);
		if (null != aroleOrganizationList && aroleOrganizationList.size() > 0) {
			return aroleOrganizationList.get(0);
		}
		return null;
	}

}
