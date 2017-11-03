package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.rolePermission.manager.RoleAttrSpecSortManager;
import cn.ffcs.uom.rolePermission.model.RoleAttrSpecSort;

@Service("roleAttrSpecSortManager")
@Scope("prototype")
public class RoleAttrSpecSortManagerImpl implements RoleAttrSpecSortManager {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RoleAttrSpecSort> getRoleAttrSpecSortList() {
		String sql = "SELECT * FROM ROLE_ATTR_SPEC_SORT A WHERE A.STATUS_CD = ? ORDER BY ROLE_ATTR_SPEC_SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return RoleAttrSpecSort.repository().jdbcFindList(sql, params,
				RoleAttrSpecSort.class);
	}
}
