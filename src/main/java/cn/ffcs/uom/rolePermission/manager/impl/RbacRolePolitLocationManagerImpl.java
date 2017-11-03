package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRolePolitLocationDao;
import cn.ffcs.uom.rolePermission.manager.RbacRolePolitLocationManager;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;

@Service("rbacRolePolitLocationManager")
@Scope("prototype")
public class RbacRolePolitLocationManagerImpl implements
		RbacRolePolitLocationManager {

	@Resource
	private RbacRolePolitLocationDao rbacRolePolitLocationDao;

	@Override
	public PageInfo queryPageInfoRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation, int currentPage,
			int pageSize) {
		return rbacRolePolitLocationDao.queryPageInfoRbacRolePolitLocation(
				rbacRolePolitLocation, currentPage, pageSize);
	}

	@Override
	public List<RbacRolePolitLocation> queryRbacRolePolitLocationList(
			RbacRolePolitLocation rbacRolePolitLocation) {
		return rbacRolePolitLocationDao
				.queryRbacRolePolitLocationList(rbacRolePolitLocation);
	}

	@Override
	public RbacRolePolitLocation queryRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation) {
		return rbacRolePolitLocationDao
				.queryRbacRolePolitLocation(rbacRolePolitLocation);
	}

	@Override
	public void saveRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation) {
		rbacRolePolitLocation.addOnly();
	}

	@Override
	public void updateRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation) {
		rbacRolePolitLocation.updateOnly();
	}

	@Override
	public void removeRbacRolePolitLocation(
			RbacRolePolitLocation rbacRolePolitLocation) {
		rbacRolePolitLocation.removeOnly();
	}

	@Override
	public void addRbacRolePolitLocationList(
			List<RbacRolePolitLocation> rbacRolePolitLocationList) {
		for (RbacRolePolitLocation rbacRolePolitLocation : rbacRolePolitLocationList) {
			rbacRolePolitLocation.addOnly();
		}
	}

	@Override
	public void updateRbacRolePolitLocationList(
			List<RbacRolePolitLocation> rbacRolePolitLocationList) {
		for (RbacRolePolitLocation rbacRolePolitLocation : rbacRolePolitLocationList) {
			rbacRolePolitLocation.updateOnly();
		}
	}

}
