package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.webservices.manager.SystemUpdateStaffScopeManager;
import cn.ffcs.uom.webservices.model.SystemUpdateStaffScope;

@Service("systemUpdateStaffScopeManager")
@Scope("prototype")
public class SystemUpdateStaffScopeManagerImpl implements
		SystemUpdateStaffScopeManager {

	@Override
	public List<SystemUpdateStaffScope> querySystemUpdateStaffScopeList(
			SystemUpdateStaffScope querySystemUpdateStaffScope) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_UPDATE_STAFF_SCOPE WHERE STATUS_CD =?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (querySystemUpdateStaffScope != null) {
			if (!StrUtil.isEmpty(querySystemUpdateStaffScope.getSystemCode())) {
				sb.append(" AND SYSTEM_CODE=?");
				params.add(querySystemUpdateStaffScope.getSystemCode());
			}
		}
		return SystemUpdateStaffScope.repository().jdbcFindList(sb.toString(),
				params, SystemUpdateStaffScope.class);
	}

}
