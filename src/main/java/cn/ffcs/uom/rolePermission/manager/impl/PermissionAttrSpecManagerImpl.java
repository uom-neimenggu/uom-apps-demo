package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.rolePermission.manager.PermissionAttrSpecManager;
import cn.ffcs.uom.rolePermission.model.PermissionAttrSpec;

@Service("permissionAttrSpecManager")
@Scope("prototype")
public class PermissionAttrSpecManagerImpl implements PermissionAttrSpecManager {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PermissionAttrSpec> queryPermissionAttrSpecList() {
		String sql = "SELECT * FROM PERMISSION_ATTR_SPEC WHERE STATUS_CD =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return PermissionAttrSpec.repository().jdbcFindList(sql, params,
				PermissionAttrSpec.class);
	}
}
