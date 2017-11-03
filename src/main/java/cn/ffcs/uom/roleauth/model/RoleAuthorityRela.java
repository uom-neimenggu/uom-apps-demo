package cn.ffcs.uom.roleauth.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.roleauth.dao.AuthorityDao;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;
import cn.ffcs.uom.staffrole.model.StaffRole;

public class RoleAuthorityRela extends UomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long roleAuthRelaId;
	public Long getRoleAuthRelaId() {
		return roleAuthRelaId;
	}
	public void setRoleAuthRelaId(Long roleAuthRelaId) {
		super.setId(roleAuthRelaId);
		this.roleAuthRelaId = roleAuthRelaId;
	}
	@Getter
	@Setter
	private Long authorityId;
	@Getter
	@Setter
	private Long roleId;
	@Setter
	private StaffAuthority authority;
	public StaffAuthority getAuthority() {
		if(!StrUtil.isNullOrEmpty(this.authorityId)){
			authority = (StaffAuthority)repository().getObject(StaffAuthority.class, this.authorityId);
			return authority;
		}
		return null;
	}
	public static AuthorityDao repository() {
		return (AuthorityDao) ApplicationContextUtil.getBean("authorityDao");
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
