package cn.ffcs.uac.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacStaffUomRelationDao;
import cn.ffcs.uac.staff.model.UacStaffUomRelation;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;

@Repository("uacStaffUomRelationDao")
public class UacStaffUomRelationDaoImpl extends BaseDaoImpl implements
		UacStaffUomRelationDao {
	@Override
	public UacStaffUomRelation queryUacStaffUomRelation(
			UacStaffUomRelation uacStaffUomRelation) {
		List<UacStaffUomRelation> uacStaffUomRelaList = queryUacStaffUomRelationList(uacStaffUomRelation);
		if (null == uacStaffUomRelaList || uacStaffUomRelaList.size() == 0) {
			return null;
		}
		return uacStaffUomRelaList.get(0);
	}

	@Override
	public HibernateTemplate getHibernateTplt() {
		return getHibernateTemplate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UacStaffUomRelation> queryUacStaffUomRelationList(
			UacStaffUomRelation uacStaffUomRelation) {
		if (uacStaffUomRelation != null) {
			StringBuilder hql = new StringBuilder(
					"from UacStaffUomRelation u where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			hql.append(" and u.statusCd = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uacStaffUomRelation.getUacStaffId())) {
				hql.append(" AND u.uacStaffId = ?");
				params.add(uacStaffUomRelation.getUacStaffId());
			}

			return getHibernateTemplate()
					.find(hql.toString(), params.toArray());
		}

		return null;
	}

}
