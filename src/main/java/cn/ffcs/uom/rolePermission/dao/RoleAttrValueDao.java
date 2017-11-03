package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.rolePermission.model.RoleAttrValue;

public interface RoleAttrValueDao extends BaseDao {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<RoleAttrValue> queryRoleAttrValueList(
			RoleAttrValue roleAttrValue, String roleAttrValuePosition);
}
