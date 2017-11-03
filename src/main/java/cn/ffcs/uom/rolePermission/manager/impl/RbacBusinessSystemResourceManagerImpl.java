package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemResourceDao;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;

@Service("rbacBusinessSystemResourceManager")
@Scope("prototype")
public class RbacBusinessSystemResourceManagerImpl implements RbacBusinessSystemResourceManager {

	@Resource
	private RbacBusinessSystemResourceDao rbacBusinessSystemResourceDao;

	@Override
	public PageInfo queryPageInfoRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource, int currentPage, int pageSize) {
		return rbacBusinessSystemResourceDao.queryPageInfoRbacBusinessSystemResource(
				rbacBusinessSystemResource, currentPage, pageSize);
	}

	@Override
	public List<RbacBusinessSystemResource> queryRbacBusinessSystemResourceList(
			RbacBusinessSystemResource rbacBusinessSystemResource) {
		return rbacBusinessSystemResourceDao
				.queryRbacBusinessSystemResourceList(rbacBusinessSystemResource);
	}

	@Override
	public RbacBusinessSystemResource queryRbacBusinessSystemResource(
			RbacBusinessSystemResource rbacBusinessSystemResource) {
		return rbacBusinessSystemResourceDao
				.queryRbacBusinessSystemResource(rbacBusinessSystemResource);
	}

	@Override
	public void saveRbacBusinessSystemResource(RbacBusinessSystemResource rbacBusinessSystemResource) {
		rbacBusinessSystemResource.addOnly();
	}

	@Override
	public void updateRbacBusinessSystemResource(RbacBusinessSystemResource rbacBusinessSystemResource) {
		rbacBusinessSystemResource.updateOnly();
	}

	@Override
	public void removeRbacBusinessSystemResource(RbacBusinessSystemResource rbacBusinessSystemResource) {
		rbacBusinessSystemResource.removeOnly();
	}

	@Override
	public void addRbacBusinessSystemResourceList(
			List<RbacBusinessSystemResource> rbacBusinessSystemResourceList) {
		for (RbacBusinessSystemResource rbacBusinessSystemResource : rbacBusinessSystemResourceList) {
			rbacBusinessSystemResource.addOnly();
		}
	}

}
