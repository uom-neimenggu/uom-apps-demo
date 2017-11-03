package cn.ffcs.uom.staff.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.manager.TreeOrgStaffExtendAttrManager;
import cn.ffcs.uom.staff.model.StaffAttrSpec;

@Service("treeOrgStaffExtendAttrManager")
@Scope("prototype")
@SuppressWarnings({"unchecked","rawtypes"})
public class TreeOrgStaffExtendAttrManagerImpl implements
		TreeOrgStaffExtendAttrManager {

	
    @Override
	public List<StaffAttrSpec> getStaffAttrSpecListByByParams(Map params) {
		StringBuffer sb = new StringBuffer(
				"SELECT C.* FROM STAFF_ATTR_SPEC_SORT A, (SELECT DISTINCT (STAFF_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_ORG_STAFF_EXTEND_ATTR WHERE STATUS_CD = ?");
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
		sb.append(") B,STAFF_ATTR_SPEC C WHERE A.STATUS_CD = ?");
		paramList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		String sortType = (String) params.get("sortType");
		if (!StrUtil.isEmpty(sortType)) {
			sb.append(" AND A.SORT_TYPE = ?");
			paramList.add(sortType);
		}
		sb.append(" AND C.STATUS_CD = ?");
		paramList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		sb
				.append(" AND C.STAFF_ATTR_SPEC_SORT_ID = B.SORT_ID AND A.STAFF_ATTR_SPEC_SORT_ID = B.SORT_ID");
		return StaffAttrSpec.repository().jdbcFindList(sb.toString(),
				paramList, StaffAttrSpec.class);
	}

}
