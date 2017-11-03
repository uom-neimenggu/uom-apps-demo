package cn.ffcs.uom.staff.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

public interface StaffExtendAttrDao extends BaseDao {

	public StaffExtendAttr queryStaffExtendAttr(StaffExtendAttr staffExtendAttr);

	public List<StaffExtendAttr> queryStaffExtendAttrList(
			StaffExtendAttr staffExtendAttr);

}
