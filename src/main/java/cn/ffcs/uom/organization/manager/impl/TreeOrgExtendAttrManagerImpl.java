package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.manager.TreeOrgExtendAttrManager;
import cn.ffcs.uom.organization.model.OrgAttrSpec;

@Service("treeOrgExtendAttrManager")
@Scope("prototype")
public class TreeOrgExtendAttrManagerImpl implements TreeOrgExtendAttrManager {

	@Override
	public List<OrgAttrSpec> getOrgAttrSpecList() {
		String sql = "SELECT A.* FROM ORG_ATTR_SPEC A,(SELECT DISTINCT (ORG_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_ORG_EXTEND_ATTR WHERE STATUS_CD = ?) B WHERE A.STATUS_CD = ? AND A.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return OrgAttrSpec.repository().jdbcFindList(sql, params,
				OrgAttrSpec.class);
	}

	@Override
	public List<OrgAttrSpec> getOrgAttrSpecListByParams(Map params) {
		StringBuffer sb = new StringBuffer(
				"SELECT C.* FROM ORG_ATTR_SPEC_SORT A, (SELECT DISTINCT (ORG_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_ORG_EXTEND_ATTR WHERE STATUS_CD = ?");
		List paramList = new ArrayList();
		paramList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		String treeType = (String) params.get("treeType");
		if (!StrUtil.isEmpty(treeType)) {
			sb.append(" AND TREE_TYPE = ?");
			paramList.add(treeType);
		}
		Long orgId = (Long) params.get("orgId");
		if (orgId != null) {
			sb.append(" AND ORG_ID = ?");
			paramList.add(orgId);
		}
		sb.append(") B,ORG_ATTR_SPEC C WHERE A.STATUS_CD = ?");
		paramList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		String sortType = (String) params.get("sortType");
		if (!StrUtil.isEmpty(sortType)) {
			sb.append(" AND A.SORT_TYPE = ?");
			paramList.add(sortType);
		}
		sb.append(" AND C.STATUS_CD = ?");
		paramList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		sb
				.append(" AND C.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID AND A.ORG_ATTR_SPEC_SORT_ID = B.SORT_ID");
		return OrgAttrSpec.repository().jdbcFindList(sb.toString(), paramList,
				OrgAttrSpec.class);
	}
}
