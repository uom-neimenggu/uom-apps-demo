package cn.ffcs.uom.staff.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.staff.model.StaffPosition;

public interface StaffPositionDao extends BaseDao {
	/**
	 * 新增员工岗位
	 * @param staffPosition
	 * @return
	 */
	public Long addStaffPosition(StaffPosition staffPosition);

}
