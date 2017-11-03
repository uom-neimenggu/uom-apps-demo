package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemDao;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

@Service("rbacBusinessSystemManager")
@Scope("prototype")
public class RbacBusinessSystemManagerImpl implements RbacBusinessSystemManager {

	@Resource
	private RbacBusinessSystemDao rbacBusinessSystemDao;

	@Override
	public PageInfo queryPageInfoRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem, int currentPage, int pageSize) {
		return rbacBusinessSystemDao.queryPageInfoRbacBusinessSystem(
				rbacBusinessSystem, currentPage, pageSize);
	}

	@Override
	public List<RbacBusinessSystem> queryRbacBusinessSystemList(
			RbacBusinessSystem rbacBusinessSystem) {
		return rbacBusinessSystemDao
				.queryRbacBusinessSystemList(rbacBusinessSystem);
	}

	@Override
	public RbacBusinessSystem queryRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem) {
		return rbacBusinessSystemDao
				.queryRbacBusinessSystem(rbacBusinessSystem);
	}

	@Override
	public void saveRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem) {
		rbacBusinessSystem.addOnly();
	}

	@Override
	public void updateRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem) {
		rbacBusinessSystem.updateOnly();
	}

	@Override
	public void removeRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem) {
		rbacBusinessSystem.removeOnly();
	}

}
