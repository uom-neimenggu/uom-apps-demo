package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleBusinessSystemDao;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

@Service("rbacUserRoleBusinessSystemManager")
@Scope("prototype")
public class RbacUserRoleBusinessSystemManagerImpl implements
		RbacUserRoleBusinessSystemManager {

	@Resource
	private RbacUserRoleBusinessSystemDao rbacUserRoleBusinessSystemDao;

	@Override
	public PageInfo queryPageInfoRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem,
			int currentPage, int pageSize) {
		return rbacUserRoleBusinessSystemDao
				.queryPageInfoRbacUserRoleBusinessSystem(
						rbacUserRoleBusinessSystem, currentPage, pageSize);
	}

	@Override
	public List<RbacUserRoleBusinessSystem> queryRbacUserRoleBusinessSystemList(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		return rbacUserRoleBusinessSystemDao
				.queryRbacUserRoleBusinessSystemList(rbacUserRoleBusinessSystem);
	}

	@Override
	public RbacUserRoleBusinessSystem queryRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		return rbacUserRoleBusinessSystemDao
				.queryRbacUserRoleBusinessSystem(rbacUserRoleBusinessSystem);
	}

	@Override
	public void saveRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		rbacUserRoleBusinessSystem.addOnly();
	}

	@Override
	public void updateRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		rbacUserRoleBusinessSystem.updateOnly();
	}

	@Override
	public void removeRbacUserRoleBusinessSystem(
			RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem) {
		rbacUserRoleBusinessSystem.removeOnly();
	}

	@Override
	public void addRbacUserRoleBusinessSystemList(
			List<RbacUserRoleBusinessSystem> rbacUserRoleBusinessSystemList) {
		for (RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem : rbacUserRoleBusinessSystemList) {
			rbacUserRoleBusinessSystem.addOnly();
		}
	}

}
