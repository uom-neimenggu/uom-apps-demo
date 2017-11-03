package cn.ffcs.uom.syslist.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.dao.SysListDao;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

@Service("sysListManager")
@Scope("prototype")
public class SysListManagerImpl implements SysListManager {

	@Autowired
	private SysListDao sysListDao;
	
	@Override
	public void removeSysList(SysList sysList) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//sysList.setBatchNumber(batchNumber);
		//sysList.remove();
		sysList.removeOnly();
	}

	@Override
	public void updateSysList(SysList sysList) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//sysList.setBatchNumber(batchNumber);
		//sysList.update();
		sysList.updateOnly();
	}

	@Override
	public void saveSysList(SysList sysList) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//sysList.setBatchNumber(batchNumber);
		//sysList.add();
		sysList.addOnly();
	}

	@Override
	public void removeStaffSysRela(StaffSysRela staffSysRela) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//staffSysRela.setBatchNumber(batchNumber);
		//staffSysRela.remove();
		staffSysRela.removeOnly();
	}

	@Override
	public PageInfo queryStaffSysRela(StaffSysRela staffSysRela, int currentPage, int pageSize) {
		return sysListDao.queryStaffSysRela(staffSysRela, currentPage, pageSize);
	}

	@Override
	public void saveStaffSysRela(StaffSysRela staffSysRela){
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//staffSysRela.setBatchNumber(batchNumber);
		//staffSysRela.add();
		staffSysRela.addOnly();
	}

	@Override
	public void saveStaffSysRelas(List<Staff> staffs, List<SysList> sysLists){
		if(null == sysLists || null == staffs){
			return;
		}
		//String batchNumber = OperateLog.gennerateBatchNumber();
		for (SysList sr : sysLists) {
			for (Staff staff : staffs) {
				StaffSysRela staffSysRela = new StaffSysRela();
				//staffSysRela.setBatchNumber(batchNumber);
				staffSysRela.setSysListId(sr.getSysListId());
				staffSysRela.setStaffId(staff.getStaffId());
				if(null == this.queryStaffSysRela(staffSysRela)){
					//staffSysRela.add();
					staffSysRela.addOnly();
				}				
			}
		}
	}

	@Override
	public StaffSysRela queryStaffSysRela(StaffSysRela staffSysRela){
		return sysListDao.queryStaffSysRela(staffSysRela);
	}

	@Override
	public PageInfo querySysRoleRela(SysRoleRela sysRoleRela, int currentPage, int pageSize) {
		return sysListDao.querySysRoleRela(sysRoleRela, currentPage, pageSize);
	}

	@Override
	public void removeSysRoleRela(SysRoleRela sysRoleRela) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//sysRoleRela.setBatchNumber(batchNumber);
		//sysRoleRela.remove();
		sysRoleRela.removeOnly();
	}
	
	@Override
	public SysRoleRela querySysRoleRela(SysRoleRela sysRoleRela){
		return sysListDao.querySysRoleRela(sysRoleRela);
	}

	@Override
	public void saveSysRoleRelas(List<StaffRole> staffRoles, List<SysList> sysLists) {
		if(null == sysLists || null == staffRoles){
			return;
		}
		//String batchNumber = OperateLog.gennerateBatchNumber();
		for (StaffRole staffRole : staffRoles) {
			for (SysList sr : sysLists) {
				SysRoleRela sysRoleRela = new SysRoleRela();
				//sysRoleRela.setBatchNumber(batchNumber);
				sysRoleRela.setSysListId(sr.getSysListId());
				sysRoleRela.setRoleId(staffRole.getRoleId());
				if(null == this.querySysRoleRela(sysRoleRela)){
					//sysRoleRela.add();
					sysRoleRela.addOnly();
				}
			}
		}
	}

}
