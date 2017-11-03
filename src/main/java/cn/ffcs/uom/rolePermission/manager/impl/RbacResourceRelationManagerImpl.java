package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacResourceRelationDao;
import cn.ffcs.uom.rolePermission.manager.RbacResourceRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Service("rbacResourceRelationManager")
@Scope("prototype")
public class RbacResourceRelationManagerImpl implements
		RbacResourceRelationManager {

	@Resource
	private RbacResourceRelationDao rbacResourceRelationDao;

	@Override
	public PageInfo queryPageInfoRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation, int currentPage,
			int pageSize) {
		return rbacResourceRelationDao.queryPageInfoRbacResourceRelation(
				rbacResourceRelation, currentPage, pageSize);
	}

	@Override
	public List<RbacResourceRelation> queryRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation) {
		return rbacResourceRelationDao
				.queryRbacResourceRelationList(rbacResourceRelation);
	}

	@Override
	public RbacResourceRelation queryRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		return rbacResourceRelationDao
				.queryRbacResourceRelation(rbacResourceRelation);
	}

	@Override
	public List<RbacResourceRelation> querySubTreeRbacResourceRelationList(
			RbacResourceRelation rbacResourceRelation) {
		return rbacResourceRelationDao
				.querySubTreeRbacResourceRelationList(rbacResourceRelation);
	}

	@Override
	public void saveRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		rbacResourceRelation.addOnly();
	}

	@Override
	public void updateRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		rbacResourceRelation.updateOnly();
	}

	@Override
	public void removeRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		rbacResourceRelation.removeOnly();
	}

}
