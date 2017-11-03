package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.rolePermission.model.PermissionAttrValue;

public interface PermissionAttrValueDao extends BaseDao {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<PermissionAttrValue> queryPermissionAttrValueList(
			PermissionAttrValue permissionAttrValue,
			String permissionAttrValuePosition);
}
