package cn.ffcs.uom.staff.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.dao.StaffGrpStaffDao;
import cn.ffcs.uom.staff.manager.StaffGrpStaffManager;
import cn.ffcs.uom.staff.model.StaffGrpStaff;

@Service("staffGrpStaffManager")
@Scope("prototype")
public class StaffGrpStaffManagerImpl implements StaffGrpStaffManager {
	@Resource
	private StaffGrpStaffDao staffGrpStaffDao;

	@Override
	public void addStaffGrpStaff(StaffGrpStaff staffGrpStaff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffGrpStaff.setBatchNumber(batchNumber);
		staffGrpStaff.add();
	}

	@Override
	public void removeStaffGrpStaff(StaffGrpStaff staffGrpStaff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffGrpStaff.setBatchNumber(batchNumber);
		staffGrpStaff.remove();
	}

	@Override
	public StaffGrpStaff queryStaffGrpStaff(StaffGrpStaff staffGrpStaff) {
		return staffGrpStaffDao.queryStaffGrpStaff(staffGrpStaff);
	}

	@Override
	public List<StaffGrpStaff> queryStaffGrpStaffList(
			StaffGrpStaff staffGrpStaff) {
		return staffGrpStaffDao.queryStaffGrpStaffList(staffGrpStaff);
	}

	@Override
	public PageInfo queryPageInfoByStaffGrpStaff(StaffGrpStaff staffGrpStaff,
			int currentPage, int pageSize) {
		return staffGrpStaffDao.queryPageInfoByStaffGrpStaff(staffGrpStaff,
				currentPage, pageSize);
	}

}
