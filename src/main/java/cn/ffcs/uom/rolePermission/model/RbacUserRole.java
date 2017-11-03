package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleDao;

public class RbacUserRole extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacUserRoleId() {
		return super.getId();
	}

	public void setRbacUserRoleId(Long rbacUserRoleId) {
		super.setId(rbacUserRoleId);
	}

	@Getter
	@Setter
	private Long rbacUserId;

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String staffAccount;

	@Getter
	@Setter
	private String staffName;

	/**
	 * 获取角色
	 * 
	 * @return
	 */
	public RbacRole getRbacRole() {
		if (this.rbacRoleId != null) {
			return (RbacRole) RbacRole.repository().getObject(RbacRole.class,
					this.rbacRoleId);
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacUserRoleDao repository() {
		return (RbacUserRoleDao) ApplicationContextUtil
				.getBean("rbacUserRoleDao");
	}

}