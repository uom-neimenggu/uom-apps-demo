package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleExtAttrManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

@Service("rbacRoleExtAttrManager")
@Scope("prototype")
public class RbacRoleExtAttrManagerImpl implements RbacRoleExtAttrManager {

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacRoleExtAttr(
			RbacRoleExtAttr rbacRoleExtAttr, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return rbacRoleExtAttrDao.queryPageInfoRbacRoleExtAttr(rbacRoleExtAttr,
				currentPage, pageSize);
	}

	@Override
	public List<RbacRoleExtAttr> queryRbacRoleExtAttrList(
			RbacRoleExtAttr rbacRoleExtAttr) {
		// TODO Auto-generated method stub
		return rbacRoleExtAttrDao.queryRbacRoleExtAttrList(rbacRoleExtAttr);
	}

	@Override
	public RbacRoleExtAttr queryRbacRoleExtAttr(RbacRoleExtAttr rbacRoleExtAttr) {
		// TODO Auto-generated method stub
		return rbacRoleExtAttrDao.queryRbacRoleExtAttr(rbacRoleExtAttr);
	}
}
