package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.staff.model.StaffExtendAttr;

public interface StaffExtendAttrManager {

	public StaffExtendAttr queryStaffExtendAttr(StaffExtendAttr staffExtendAttr);

	public List<StaffExtendAttr> queryStaffExtendAttrList(
			StaffExtendAttr staffExtendAttr);
}
