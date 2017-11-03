package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.PermissionAttrSpec;

public interface PermissionAttrSpecManager {
	/**
	 * 获取属性规列表
	 * 
	 * @return
	 */
	public List<PermissionAttrSpec> queryPermissionAttrSpecList();
}
