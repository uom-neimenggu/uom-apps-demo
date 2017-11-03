package cn.ffcs.uom.staff.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.staff.manager.TreeStaffExtendAttrManager;
import cn.ffcs.uom.staff.model.StaffAttrSpec;

@Service("treeStaffExtendAttrManager")
@Scope("prototype")
@SuppressWarnings({"unchecked","rawtypes"})
public class TreeStaffExtendAttrManagerImpl implements
		TreeStaffExtendAttrManager {


    @Override
	public List<StaffAttrSpec> getStaffAttrSpecListByTreeTypeAndSortType(
			String treeType, String sortType) {
		String sql = "SELECT C.* FROM STAFF_ATTR_SPEC_SORT A,(SELECT DISTINCT (STAFF_ATTR_SPEC_SORT_ID) SORT_ID FROM TREE_STAFF_EXTEND_ATTR WHERE STATUS_CD = ? AND TREE_TYPE = ?) B, STAFF_ATTR_SPEC C WHERE A.STATUS_CD = ? AND A.SORT_TYPE = ? AND C.STATUS_CD = ? AND C.STAFF_ATTR_SPEC_SORT_ID = B.SORT_ID AND A.STAFF_ATTR_SPEC_SORT_ID = B.SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(treeType);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(sortType);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return StaffAttrSpec.repository().jdbcFindList(sql, params,
				StaffAttrSpec.class);
	}

}
