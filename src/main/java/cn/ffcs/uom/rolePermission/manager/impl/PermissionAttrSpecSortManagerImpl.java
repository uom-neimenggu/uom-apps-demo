package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.rolePermission.manager.PermissionAttrSpecSortManager;
import cn.ffcs.uom.rolePermission.model.PermissionAttrSpecSort;

@Service("permissionAttrSpecSortManager")
@Scope("prototype")
public class PermissionAttrSpecSortManagerImpl implements
		PermissionAttrSpecSortManager {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PermissionAttrSpecSort> getPermissionAttrSpecSortList() {
		String sql = "SELECT * FROM PERMISSION_ATTR_SPEC_SORT A WHERE A.STATUS_CD = ? ORDER BY PERMISSION_ATTR_SPEC_SORT_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return PermissionAttrSpecSort.repository().jdbcFindList(sql, params,
				PermissionAttrSpecSort.class);
	}
}
