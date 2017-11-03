package cn.ffcs.uom.staff.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.staff.dao.StaffExtendAttrDao;
import cn.ffcs.uom.staff.manager.StaffExtendAttrManager;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

@Service("staffExtendAttrManager")
@Scope("prototype")
public class StaffExtendAttrManagerImpl implements StaffExtendAttrManager {

	@Resource
	private StaffExtendAttrDao staffExtendAttrDao;

	@Override
	public StaffExtendAttr queryStaffExtendAttr(StaffExtendAttr staffExtendAttr) {
		// TODO Auto-generated method stub
		return staffExtendAttrDao.queryStaffExtendAttr(staffExtendAttr);
	}

	@Override
	public List<StaffExtendAttr> queryStaffExtendAttrList(
			StaffExtendAttr staffExtendAttr) {
		// TODO Auto-generated method stub
		return staffExtendAttrDao.queryStaffExtendAttrList(staffExtendAttr);
	}

}
