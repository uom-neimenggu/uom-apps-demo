package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.RoleAttrSpec;

public interface RoleAttrSpecManager {
	/**
	 * 获取属性规列表
	 * 
	 * @return
	 */
	public List<RoleAttrSpec> queryRoleAttrSpecList();
}
