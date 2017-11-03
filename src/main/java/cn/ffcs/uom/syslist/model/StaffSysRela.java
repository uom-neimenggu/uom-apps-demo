package cn.ffcs.uom.syslist.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.syslist.dao.SysListDao;

public class StaffSysRela extends UomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long staffSysListId;
	public Long getStaffSysListId() {
		return staffSysListId;
	}
	public void setStaffSysListId(Long staffSysListId) {
		super.setId(staffSysListId);
		this.staffSysListId = staffSysListId;
	}
	@Getter
	@Setter
	private Long staffId;
	@Getter
	@Setter
	private Long sysListId;
	@Setter
	private Staff staff;
	@Getter
	@Setter
	private Staff qryStaff;
	public Staff getStaff() {
		if(!StrUtil.isNullOrEmpty(this.staffId)){
			staff = repositoryStaff().queryStaff(this.staffId);
			return staff;
		}
		return null;
	}
	public static StaffDao repositoryStaff() {
		return (StaffDao) ApplicationContextUtil.getBean("staffDao");
	}
	@Setter
	private SysList sysList;
	public SysList getSysList() {
		if(!StrUtil.isNullOrEmpty(this.sysListId)){
			sysList = (SysList)repositorySysList().getObject(SysList.class, this.sysListId);
			return sysList;			
		}
		return null;
	}
	public static SysListDao repositorySysList() {
		return (SysListDao) ApplicationContextUtil.getBean("sysListDao");
	}

}