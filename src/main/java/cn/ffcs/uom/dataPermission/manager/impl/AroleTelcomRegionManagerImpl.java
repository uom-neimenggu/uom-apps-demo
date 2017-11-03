package cn.ffcs.uom.dataPermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleTelcomRegionManager;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;

@Service("aroleTelcomRegionManager")
@Scope("prototype")
public class AroleTelcomRegionManagerImpl implements AroleTelcomRegionManager {

	public PageInfo queryPageInfoByRoleTelcomRegion(AroleTelcomRegion aroleTelcomRegion, int currentPage, int pageSize) throws SystemException {
		StringBuffer hql = new StringBuffer(
				" From AroleTelcomRegion where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleTelcomRegion != null) {
			if (aroleTelcomRegion.getAroleId() != null) {
				hql.append(" and AROLE_ID=?");
				params.add(aroleTelcomRegion.getAroleId());
			}
		}
		hql.append(" order by AROLE_TELCOM_REGION_ID");
		return aroleTelcomRegion.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}

	public void removeRoleTelcomRegion(AroleTelcomRegion aroleTelcomRegion) {
		aroleTelcomRegion.removeOnly();
	}

	public void addRoleTelcomRegion(AroleTelcomRegion aroleTelcomRegion) {
		aroleTelcomRegion.addOnly();
	}
}
