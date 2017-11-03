package cn.ffcs.uac.staff.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.ffcs.uac.staff.model.UacStaffUomRelation;
import cn.ffcs.uom.common.dao.BaseDao;

public interface UacStaffUomRelationDao extends BaseDao {

	public UacStaffUomRelation queryUacStaffUomRelation(
			UacStaffUomRelation uacStaffUomRelation);

	public List<UacStaffUomRelation> queryUacStaffUomRelationList(
			UacStaffUomRelation uacStaffUomRelation);

	public HibernateTemplate getHibernateTplt();

}
