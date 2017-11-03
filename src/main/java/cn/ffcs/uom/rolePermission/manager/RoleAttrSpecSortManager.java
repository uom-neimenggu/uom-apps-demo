package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.RoleAttrSpecSort;

public interface RoleAttrSpecSortManager {
	/**
	 * 获取角色属性规格类型列表
	 * 
	 * @return
	 */
	public List<RoleAttrSpecSort> getRoleAttrSpecSortList();
}
