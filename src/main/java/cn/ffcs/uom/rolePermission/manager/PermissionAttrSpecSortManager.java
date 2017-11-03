package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.PermissionAttrSpecSort;

public interface PermissionAttrSpecSortManager {
	/**
	 * 获取权限属性规格类型列表
	 * 
	 * @return
	 */
	public List<PermissionAttrSpecSort> getPermissionAttrSpecSortList();
}
