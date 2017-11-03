package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.organization.manager.OrgAttrSpecSortManager;
import cn.ffcs.uom.organization.model.OrgAttrSpecSort;

@Service("orgAttrSpecSortManager")
@Scope("prototype")
public class OrgAttrSpecSortManagerImpl implements OrgAttrSpecSortManager {
	public List<OrgAttrSpecSort> getOrgAttrSpecSortList() {
		String sql = "SELECT * FROM ORG_ATTR_SPEC_SORT A WHERE A.STATUS_CD = ? ORDER BY ORG_ATTR_SPEC_SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return OrgAttrSpecSort.repository().jdbcFindList(sql, params,
				OrgAttrSpecSort.class);
	}
}
