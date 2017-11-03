package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleBusinessSystemDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

@Service("rbacRoleBusinessSystemManager")
@Scope("prototype")
public class RbacRoleBusinessSystemManagerImpl implements
		RbacRoleBusinessSystemManager {

	@Resource
	private RbacRoleBusinessSystemDao rbacRoleBusinessSystemDao;

	@Override
	public PageInfo queryPageInfoRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem, int currentPage,
			int pageSize) {
		return rbacRoleBusinessSystemDao.queryPageInfoRbacRoleBusinessSystem(
				rbacRoleBusinessSystem, currentPage, pageSize);
	}

	@Override
	public List<RbacRoleBusinessSystem> queryRbacRoleBusinessSystemList(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		return rbacRoleBusinessSystemDao
				.queryRbacRoleBusinessSystemList(rbacRoleBusinessSystem);
	}

	@Override
	public RbacRoleBusinessSystem queryRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		return rbacRoleBusinessSystemDao
				.queryRbacRoleBusinessSystem(rbacRoleBusinessSystem);
	}

	@Override
	public void saveRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		rbacRoleBusinessSystem.addOnly();
	}

	@Override
	public void updateRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		rbacRoleBusinessSystem.updateOnly();
	}

	@Override
	public void removeRbacRoleBusinessSystem(
			RbacRoleBusinessSystem rbacRoleBusinessSystem) {
		rbacRoleBusinessSystem.removeOnly();
	}

	@Override
	public void addRbacRoleBusinessSystemList(
			List<RbacRoleBusinessSystem> rbacRoleBusinessSystemList) {
		for (RbacRoleBusinessSystem rbacRoleBusinessSystem : rbacRoleBusinessSystemList) {
			rbacRoleBusinessSystem.addOnly();
		}
	}

}
