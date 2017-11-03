package cn.ffcs.uom.dataPermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;

@Service("aroleOrganizationManager")
@Scope("prototype")
public class AroleOrganizationManagerImpl implements AroleOrganizationManager {

	public PageInfo queryPageInfoByRoleOrganization(
			AroleOrganization aroleOrganization, int currentPage, int pageSize)
			throws Exception {
		StringBuffer hql = new StringBuffer(
				" From AroleOrganization where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleOrganization != null) {
			if (aroleOrganization.getAroleId() != null) {
				hql.append(" and AROLE_ID=?");
				params.add(aroleOrganization.getAroleId());
			}
		}
		hql.append(" order by AROLE_ORGANIZATION_ID");
		return aroleOrganization.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}

	public void removeRoleOrganization(AroleOrganization aroleOrganization) {
		aroleOrganization.removeOnly();
	}

	public void addRoleOrganization(AroleOrganization aroleOrganization) {
		aroleOrganization.addOnly();
	}

	@Override
	public List<AroleOrganization> queryRoleOrganizationList(
			AroleOrganization aroleOrganization) throws Exception {
		StringBuffer hql = new StringBuffer(
				" From AroleOrganization where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleOrganization != null) {
			if (aroleOrganization.getAroleId() != null) {
				hql.append(" and AROLE_ID=?");
				params.add(aroleOrganization.getAroleId());
			}
		}
		hql.append(" order by AROLE_ORGANIZATION_ID");
		return aroleOrganization.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

}
