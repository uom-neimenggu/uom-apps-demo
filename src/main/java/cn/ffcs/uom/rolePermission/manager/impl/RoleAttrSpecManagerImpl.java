package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.rolePermission.manager.RoleAttrSpecManager;
import cn.ffcs.uom.rolePermission.model.RoleAttrSpec;

@Service("roleAttrSpecManager")
@Scope("prototype")
public class RoleAttrSpecManagerImpl implements RoleAttrSpecManager {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RoleAttrSpec> queryRoleAttrSpecList() {
		String sql = "SELECT * FROM ROLE_ATTR_SPEC WHERE STATUS_CD =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return RoleAttrSpec.repository().jdbcFindList(sql, params,
				RoleAttrSpec.class);
	}
}
