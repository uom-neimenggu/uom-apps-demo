package cn.ffcs.uom.rolePermission.manager;

import java.util.List;

import cn.ffcs.uom.rolePermission.model.RoleAttrValue;

public interface RoleAttrValueManager {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<RoleAttrValue> queryRoleAttrValueList(RoleAttrValue roleAttrValue,
			String roleAttrValuePosition);
}
