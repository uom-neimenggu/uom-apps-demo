package cn.ffcs.uom.staffrole.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;

public class StaffRoleRela extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long staffRoleRelaId;

	public Long getStaffRoleRelaId() {
		return staffRoleRelaId;
	}

	public void setStaffRoleRelaId(Long staffRoleRelaId) {
		super.setId(staffRoleRelaId);
		this.staffRoleRelaId = staffRoleRelaId;
	}

	@Getter
	@Setter
	private Long staffId;
	@Getter
	@Setter
	private Long roleId;
	@Getter
	@Setter
	private Long roleParentId;
	@Getter
	@Setter
	private String staffRoleName;
	@Setter
	private Staff staff;
	@Getter
	@Setter
	private String staffName;
	@Getter
	@Setter
	private String staffCode;
	@Setter
	private String staffAccount;
	@Getter
	@Setter
	private String staffRoleStaffAccount;
	@Getter
	@Setter
	private String staffAttrValue;
	@Getter
	@Setter
	private Staff qryStaff;

	public Staff getStaff() {
		if (!StrUtil.isNullOrEmpty(this.staffId)) {
			staff = repositoryStaff().queryStaff(this.staffId);
			return staff;
		}
		return null;
	}

	public String getStaffAccount() {
		if (!StrUtil.isNullOrEmpty(this.staffId)) {
			staff = repositoryStaff().queryStaff(this.staffId);
			return staff.getStaffAccountFromDB().getStaffAccount();
		}
		return null;
	}

	public static StaffDao repositoryStaff() {
		return (StaffDao) ApplicationContextUtil.getBean("staffDao");
	}

	@Setter
	private StaffRole staffRole;

	public StaffRole getStaffRole() {
		if (!StrUtil.isNullOrEmpty(this.roleId)) {
			staffRole = (StaffRole) repositoryStaffRole().getObject(
					StaffRole.class, this.roleId);
			return staffRole;
		}
		return null;
	}

	public static StaffRoleDao repositoryStaffRole() {
		return (StaffRoleDao) ApplicationContextUtil.getBean("staffRoleDao");
	}

}