package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacResourceDao;
import cn.ffcs.uom.rolePermission.manager.RbacResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;

@Service("rbacResourceManager")
@Scope("prototype")
public class RbacResourceManagerImpl implements RbacResourceManager {

	@Resource
	private RbacResourceDao rbacResourceDao;

	@Override
	public PageInfo queryPageInfoRbacResource(RbacResource rbacResource,
			int currentPage, int pageSize) {
		return rbacResourceDao.queryPageInfoRbacResource(rbacResource,
				currentPage, pageSize);
	}

	@Override
	public List<RbacResource> queryRbacResourceList(
			RbacResource rbacResource) {
		return rbacResourceDao.queryRbacResourceList(rbacResource);
	}

	@Override
	public RbacResource queryRbacResource(RbacResource rbacResource) {
		return rbacResourceDao.queryRbacResource(rbacResource);
	}

	@Override
	public void saveRbacResource(RbacResource rbacResource) {
		rbacResource.addOnly();
	}

	@Override
	public void updateRbacResource(RbacResource rbacResource) {
		rbacResource.updateOnly();
	}

	@Override
	public void removeRbacResource(RbacResource rbacResource) {
		rbacResource.removeOnly();
	}

}
