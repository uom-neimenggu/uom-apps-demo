package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionResourceDao;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;

@Service("rbacPermissionResourceManager")
@Scope("prototype")
public class RbacPermissionResourceManagerImpl implements RbacPermissionResourceManager {

	@Resource
	private RbacPermissionResourceDao rbacPermissionResourceDao;

	@Override
	public PageInfo queryPageInfoRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource, int currentPage, int pageSize) {
		return rbacPermissionResourceDao.queryPageInfoRbacPermissionResource(
				rbacPermissionResource, currentPage, pageSize);
	}

	@Override
	public List<RbacPermissionResource> queryRbacPermissionResourceList(
			RbacPermissionResource rbacPermissionResource) {
		return rbacPermissionResourceDao
				.queryRbacPermissionResourceList(rbacPermissionResource);
	}

	@Override
	public RbacPermissionResource queryRbacPermissionResource(
			RbacPermissionResource rbacPermissionResource) {
		return rbacPermissionResourceDao
				.queryRbacPermissionResource(rbacPermissionResource);
	}

	@Override
	public void saveRbacPermissionResource(RbacPermissionResource rbacPermissionResource) {
		rbacPermissionResource.addOnly();
	}

	@Override
	public void updateRbacPermissionResource(RbacPermissionResource rbacPermissionResource) {
		rbacPermissionResource.updateOnly();
	}

	@Override
	public void removeRbacPermissionResource(RbacPermissionResource rbacPermissionResource) {
		rbacPermissionResource.removeOnly();
	}

	@Override
	public void addRbacPermissionResourceList(
			List<RbacPermissionResource> rbacPermissionResourceList) {
		for (RbacPermissionResource rbacPermissionResource : rbacPermissionResourceList) {
			rbacPermissionResource.addOnly();
		}
	}

}
