package cn.ffcs.uom.staff.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.staff.dao.StaffPositionDao;
import cn.ffcs.uom.staff.model.StaffPosition;

@Repository("staffPositionDao")
public class StaffPositionDaoImpl extends BaseDaoImpl implements
		StaffPositionDao {
	@Override
	public Long addStaffPosition(StaffPosition staffPosition) {
		Serializable ser = this.getHibernateTemplate().save(staffPosition);
		Long id = Long.parseLong(ser.toString());
		return id;
	}
}
