package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionExtAttrDao;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionExtAttrManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

@Service("rbacPermissionExtAttrManager")
@Scope("prototype")
public class RbacPermissionExtAttrManagerImpl implements
		RbacPermissionExtAttrManager {

	@Resource
	private RbacPermissionExtAttrDao rbacPermissionExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacPermissionExtAttr(
			RbacPermissionExtAttr rbacPermissionExtAttr, int currentPage,
			int pageSize) {
		// TODO Auto-generated method stub
		return rbacPermissionExtAttrDao.queryPageInfoRbacPermissionExtAttr(
				rbacPermissionExtAttr, currentPage, pageSize);
	}

	@Override
	public List<RbacPermissionExtAttr> queryRbacPermissionExtAttrList(
			RbacPermissionExtAttr rbacPermissionExtAttr) {
		// TODO Auto-generated method stub
		return rbacPermissionExtAttrDao
				.queryRbacPermissionExtAttrList(rbacPermissionExtAttr);
	}

	@Override
	public RbacPermissionExtAttr queryRbacPermissionExtAttr(
			RbacPermissionExtAttr rbacPermissionExtAttr) {
		// TODO Auto-generated method stub
		return rbacPermissionExtAttrDao
				.queryRbacPermissionExtAttr(rbacPermissionExtAttr);
	}
}
