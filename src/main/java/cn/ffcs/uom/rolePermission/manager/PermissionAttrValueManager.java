package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.PermissionAttrValue;

public interface PermissionAttrValueManager {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<PermissionAttrValue> queryPermissionAttrValueList(
			PermissionAttrValue permissionAttrValue,
			String permissionAttrValuePosition);
}
