package cn.ffcs.uom.dataPermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.dataPermission.dao.AroleTelcomRegionDao;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;

@Repository("aroleTelcomRegionDao")
public class AroleTelcomRegionDaoImpl extends BaseDaoImpl implements
		AroleTelcomRegionDao {

	@Override
	public List<AroleTelcomRegion> queryAroleTelcomRegionList(
			AroleTelcomRegion aroleTelcomRegion) {

		if (null != aroleTelcomRegion) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM AROLE_TELCOM_REGION WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (aroleTelcomRegion.getAroleTelcomRegionId() != null) {
				sb.append(" AND AROLE_TELCOM_REGION_ID = ?");
				params.add(aroleTelcomRegion.getAroleTelcomRegionId());
			}

			if (aroleTelcomRegion.getAroleId() != null) {
				sb.append(" AND AROLE_ID = ?");
				params.add(aroleTelcomRegion.getAroleId());
			}

			if (aroleTelcomRegion.getTelcomRegionId() != null) {
				sb.append(" AND TELCOM_REGION_ID = ?");
				params.add(aroleTelcomRegion.getTelcomRegionId());
			}

			if (aroleTelcomRegion.getFlag() != null) {
				sb.append(" AND FLAG = ?");
				params.add(aroleTelcomRegion.getFlag());
			}

			sb.append(" ORDER BY AROLE_TELCOM_REGION_ID");

			return super.jdbcFindList(sb.toString(), params,
					AroleTelcomRegion.class);
		}

		return null;

	}

	@Override
	public AroleTelcomRegion queryAroleTelcomRegion(
			AroleTelcomRegion aroleTelcomRegion) {
		List<AroleTelcomRegion> aroleTelcomRegionList = this
				.queryAroleTelcomRegionList(aroleTelcomRegion);
		if (null != aroleTelcomRegionList && aroleTelcomRegionList.size() > 0) {
			return aroleTelcomRegionList.get(0);
		}
		return null;
	}

}
