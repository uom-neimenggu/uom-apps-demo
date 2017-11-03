package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.rolePermission.dao.PermissionAttrValueDao;
import cn.ffcs.uom.rolePermission.manager.PermissionAttrValueManager;
import cn.ffcs.uom.rolePermission.model.PermissionAttrValue;

@Service("permissionAttrValueManager")
@Scope("prototype")
public class PermissionAttrValueManagerImpl implements PermissionAttrValueManager {

	@Resource
	private PermissionAttrValueDao permissionAttrValueDao;

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@Override
	public List<PermissionAttrValue> queryPermissionAttrValueList(
			PermissionAttrValue permissionAttrValue, String permissionAttrValuePosition) {

		return permissionAttrValueDao.queryPermissionAttrValueList(permissionAttrValue,
				permissionAttrValuePosition);

	}

}
