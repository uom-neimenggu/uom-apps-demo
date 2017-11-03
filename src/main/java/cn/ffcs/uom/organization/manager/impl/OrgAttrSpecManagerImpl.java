package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.organization.manager.OrgAttrSpecManager;
import cn.ffcs.uom.organization.model.OrgAttrSpec;

@Service("orgAttrSpecManager")
@Scope("prototype")
public class OrgAttrSpecManagerImpl implements OrgAttrSpecManager {

	@Override
	public List<OrgAttrSpec> queryOrgAttrSpecList() {
		String sql = "SELECT * FROM ORG_ATTR_SPEC WHERE STATUS_CD =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return OrgAttrSpec.repository().jdbcFindList(sql, params,
				OrgAttrSpec.class);
	}
}
