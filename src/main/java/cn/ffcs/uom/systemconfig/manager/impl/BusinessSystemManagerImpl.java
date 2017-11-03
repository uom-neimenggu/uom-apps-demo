package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.dao.BusinessSystemDao;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Service("businessSystemManager")
@Scope("prototype")
public class BusinessSystemManagerImpl implements BusinessSystemManager {
	@Resource(name = "businessSystemDao")
	private BusinessSystemDao businessSystemDao;

	@Override
	public void addBusinessSystem(BusinessSystem businessSystem) {
		if (businessSystem != null) {
			businessSystem.addOnly();
		}
	}

	@Override
	public void updateBusinessSystem(BusinessSystem businessSystem) {
		if (businessSystem != null) {
			businessSystem.updateOnly();
		}
	}

	@Override
	public List<BusinessSystem> queryBusinessSystemList(
			BusinessSystem businessSystem) {
		StringBuffer sb = new StringBuffer(
				"From BusinessSystem where statusCd=?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (businessSystem != null) {

			if (businessSystem.getBusinessSystemId() != null) {
				sb.append(" and businessSystemId=?");
				params.add(businessSystem.getBusinessSystemId());
			}

			if (businessSystem.getSystemCode() != null) {
				sb.append(" and systemCode = ? ");
				params.add(businessSystem.getSystemCode());
			}

		}
		sb.append(" ORDER BY businessSystemId");
		return businessSystemDao.findListByHQLAndParams(sb.toString(), params);
	}

	@Override
	public List<BusinessSystem> queryBusinessSystemList() {
		StringBuffer sb = new StringBuffer("From BusinessSystem where statusCd=? and systemCode not in (?)  ORDER BY businessSystemId");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(WsConstants.SYSTEM_CODE_UOM);
		return businessSystemDao.findListByHQLAndParams(sb.toString(), params);
	}
	
	@Override
	public BusinessSystem getBusinessSystemByBusinessSystemId(
			Long businessSystemId) {
		BusinessSystem businessSystem = new BusinessSystem();
		businessSystem.setBusinessSystemId(businessSystemId);
		List<BusinessSystem> list = this
				.queryBusinessSystemList(businessSystem);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public PageInfo queryBusinessSystemPageInfo(
			BusinessSystem queryBusinessSystem, int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM BUSINESS_SYSTEM WHERE STATUS_CD= ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryBusinessSystem != null) {

			String systemCode = queryBusinessSystem.getSystemCode();
			if (!StrUtil.isNullOrEmpty(systemCode)) {
				sb.append(" AND SYSTEM_CODE LIKE ?");
				params.add(StringEscapeUtils.escapeSql(systemCode));
			}

			String systemName = queryBusinessSystem.getSystemName();
			if (!StrUtil.isNullOrEmpty(systemName)) {
				sb.append(" AND SYSTEM_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(systemName) + "%");
			}

		}

		sb.append(" ORDER BY BUSINESS_SYSTEM_ID");

		return this.businessSystemDao.jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, BusinessSystem.class);
	}

	@Override
	public void removeBusinessSystem(BusinessSystem businessSystem) {
		if (businessSystem != null
				&& businessSystem.getBusinessSystemId() != null) {
			businessSystem.removeOnly();
		}
	}
}
