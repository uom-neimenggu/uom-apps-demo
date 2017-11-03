package cn.ffcs.uom.common.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.dao.CascadeRelationDao;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.StaffOrganization;

@Service("cascadeRelationManager")
@Scope("prototype")
public class CascadeRelationManagerImpl implements CascadeRelationManager {
	@Resource
	private CascadeRelationDao cascadeRelationDao;

	@Override
	public CascadeRelation queryCascadeRelation(CascadeRelation cascadeRelation) {

		return cascadeRelationDao.queryCascadeRelation(cascadeRelation);

	}

	@Override
	public List<CascadeRelation> queryCascadeRelationList(
			CascadeRelation cascadeRelation) {

		return cascadeRelationDao.queryCascadeRelationList(cascadeRelation);

	}

	@Override
	public CascadeRelation queryCascadeRelationByStaffOrganization(
			CascadeRelation cascadeRelation, StaffOrganization staffOrganization) {

		return cascadeRelationDao.queryCascadeRelationByStaffOrganization(
				cascadeRelation, staffOrganization);

	}

	@Override
	public boolean isPermissions(long roleIds[], String relaCd) {

		for (Long roleId : roleIds) {
			CascadeRelation cascadeRelation = new CascadeRelation();
			cascadeRelation.setRelaCd(relaCd);
			cascadeRelation.setCascadeValue(roleId.toString());
			cascadeRelation = this.queryCascadeRelation(cascadeRelation);
			if (cascadeRelation != null
					&& !StrUtil.isEmpty(cascadeRelation.getRelaCascadeValue())) {
				return true;
			}
		}

		return false;

	}
}
