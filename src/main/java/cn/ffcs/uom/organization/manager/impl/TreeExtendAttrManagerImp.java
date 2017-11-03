package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.organization.manager.TreeExtendAttrManager;
import cn.ffcs.uom.organization.model.OrgAttrSpec;

@Service("treeExtendAttrManager")
@Scope("prototype")
public class TreeExtendAttrManagerImp implements TreeExtendAttrManager {

	@Override
	public List<OrgAttrSpec> getOrgAttrSpecList() {
		String sql = "SELECT A.* FROM ORG_ATTR_SPEC A,(SELECT DISTINCT (ORG_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_EXTEND_ATTR WHERE STATUS_CD = ?) B WHERE A.STATUS_CD = ? AND A.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return OrgAttrSpec.repository().jdbcFindList(sql, params,
				OrgAttrSpec.class);
	}

	@Override
	public List<OrgAttrSpec> getOrgAttrSpecListByTreeTypeAndSortType(
			String treeType, String sortType) {
		String sql = "SELECT C.* FROM ORG_ATTR_SPEC_SORT A, (SELECT DISTINCT (ORG_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_EXTEND_ATTR WHERE STATUS_CD = ? AND TREE_TYPE = ?) B, ORG_ATTR_SPEC C WHERE A.STATUS_CD = ? AND A.SORT_TYPE = ? AND C.STATUS_CD = ? AND C.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID AND A.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(treeType);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(sortType);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return OrgAttrSpec.repository().jdbcFindList(sql, params,
				OrgAttrSpec.class);
	}
}
