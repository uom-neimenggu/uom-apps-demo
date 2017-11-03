package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleRelationDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Service("rbacRoleRelationManager")
@Scope("prototype")
public class RbacRoleRelationManagerImpl implements RbacRoleRelationManager {

	@Resource
	private RbacRoleRelationDao rbacRoleRelationDao;

	@Override
	public PageInfo queryPageInfoRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation, int currentPage, int pageSize) {
		return rbacRoleRelationDao.queryPageInfoRbacRoleRelation(
				rbacRoleRelation, currentPage, pageSize);
	}

	@Override
	public List<RbacRoleRelation> queryRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation) {
		return rbacRoleRelationDao.queryRbacRoleRelationList(rbacRoleRelation);
	}

	@Override
	public RbacRoleRelation queryRbacRoleRelation(
			RbacRoleRelation rbacRoleRelation) {
		return rbacRoleRelationDao.queryRbacRoleRelation(rbacRoleRelation);
	}

	@Override
	public List<RbacRoleRelation> querySubTreeRbacRoleRelationList(
			RbacRoleRelation rbacRoleRelation) {
		return rbacRoleRelationDao
				.querySubTreeRbacRoleRelationList(rbacRoleRelation);
	}

	@Override
	public void saveRbacRoleRelation(RbacRoleRelation rbacRoleRelation) {
		rbacRoleRelation.addOnly();
	}

	@Override
	public void updateRbacRoleRelation(RbacRoleRelation rbacRoleRelation) {
		rbacRoleRelation.updateOnly();
	}

	@Override
	public void removeRbacRoleRelation(RbacRoleRelation rbacRoleRelation) {
		rbacRoleRelation.removeOnly();
	}

}
