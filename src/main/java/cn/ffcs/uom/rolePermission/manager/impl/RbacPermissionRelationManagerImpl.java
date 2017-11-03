package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRelationDao;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Service("rbacPermissionRelationManager")
@Scope("prototype")
public class RbacPermissionRelationManagerImpl implements
		RbacPermissionRelationManager {

	@Resource
	private RbacPermissionRelationDao rbacPermissionRelationDao;

	@Override
	public PageInfo queryPageInfoRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation, int currentPage,
			int pageSize) {
		return rbacPermissionRelationDao.queryPageInfoRbacPermissionRelation(
				rbacPermissionRelation, currentPage, pageSize);
	}

	@Override
	public List<RbacPermissionRelation> queryRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation) {
		return rbacPermissionRelationDao
				.queryRbacPermissionRelationList(rbacPermissionRelation);
	}

	@Override
	public RbacPermissionRelation queryRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		return rbacPermissionRelationDao
				.queryRbacPermissionRelation(rbacPermissionRelation);
	}

	@Override
	public List<RbacPermissionRelation> querySubTreeRbacPermissionRelationList(
			RbacPermissionRelation rbacPermissionRelation) {
		return rbacPermissionRelationDao
				.querySubTreeRbacPermissionRelationList(rbacPermissionRelation);
	}

	@Override
	public void saveRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		rbacPermissionRelation.addOnly();
	}

	@Override
	public void updateRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		rbacPermissionRelation.updateOnly();
	}

	@Override
	public void removeRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		rbacPermissionRelation.removeOnly();
	}

}
