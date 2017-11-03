package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.rolePermission.dao.RoleAttrValueDao;
import cn.ffcs.uom.rolePermission.manager.RoleAttrValueManager;
import cn.ffcs.uom.rolePermission.model.RoleAttrValue;

@Service("roleAttrValueManager")
@Scope("prototype")
public class RoleAttrValueManagerImpl implements RoleAttrValueManager {

	@Resource
	private RoleAttrValueDao roleAttrValueDao;

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@Override
	public List<RoleAttrValue> queryRoleAttrValueList(
			RoleAttrValue roleAttrValue, String roleAttrValuePosition) {

		return roleAttrValueDao.queryRoleAttrValueList(roleAttrValue,
				roleAttrValuePosition);

	}

}
