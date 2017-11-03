package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemRelationDao;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

@Service("rbacBusinessSystemRelationManager")
@Scope("prototype")
public class RbacBusinessSystemRelationManagerImpl implements
		RbacBusinessSystemRelationManager {

	@Resource
	private RbacBusinessSystemRelationDao rbacBusinessSystemRelationDao;

	@Override
	public PageInfo queryPageInfoRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation,
			int currentPage, int pageSize) {
		return rbacBusinessSystemRelationDao
				.queryPageInfoRbacBusinessSystemRelation(
						rbacBusinessSystemRelation, currentPage, pageSize);
	}

	@Override
	public List<RbacBusinessSystemRelation> queryRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		return rbacBusinessSystemRelationDao
				.queryRbacBusinessSystemRelationList(rbacBusinessSystemRelation);
	}

	@Override
	public RbacBusinessSystemRelation queryRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		return rbacBusinessSystemRelationDao
				.queryRbacBusinessSystemRelation(rbacBusinessSystemRelation);
	}

	@Override
	public List<RbacBusinessSystemRelation> querySubTreeRbacBusinessSystemRelationList(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		return rbacBusinessSystemRelationDao
				.querySubTreeRbacBusinessSystemRelationList(rbacBusinessSystemRelation);
	}

	@Override
	public void saveRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		rbacBusinessSystemRelation.addOnly();
	}

	@Override
	public void updateRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		rbacBusinessSystemRelation.updateOnly();
	}

	@Override
	public void removeRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		rbacBusinessSystemRelation.removeOnly();
	}

}
