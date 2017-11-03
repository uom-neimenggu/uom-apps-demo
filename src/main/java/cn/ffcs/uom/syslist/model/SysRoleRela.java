package cn.ffcs.uom.syslist.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.dao.SysListDao;

public class SysRoleRela extends UomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long sysRoleRelaId;
	public Long getSysRoleRelaId() {
		return sysRoleRelaId;
	}
	public void setSysRoleRelaId(Long sysRoleRelaId) {
		super.setId(sysRoleRelaId);
		this.sysRoleRelaId = sysRoleRelaId;
	}
	@Getter
	@Setter
	private Long sysListId;
	@Getter
	@Setter
	private Long roleId;

	public static SysListDao repositorySysList() {
		return (SysListDao) ApplicationContextUtil.getBean("sysListDao");
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

	@Setter
	private StaffRole staffRole;
	@Getter
	@Setter
	private StaffRole qryStaffRole;
	public StaffRole getStaffRole() {
		if(!StrUtil.isNullOrEmpty(this.roleId)){
			staffRole = (StaffRole)repositoryStaffRole().getObject(StaffRole.class, this.roleId);
			return staffRole;			
		}
		return null;
	}
	public static StaffRoleDao repositoryStaffRole() {
		return (StaffRoleDao) ApplicationContextUtil.getBean("staffRoleDao");
	}
}
