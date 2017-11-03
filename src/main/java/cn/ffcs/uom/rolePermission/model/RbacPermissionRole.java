package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRoleDao;

public class RbacPermissionRole extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacPermissionRoleId() {
		return super.getId();
	}

	public void setRbacPermissionRoleId(Long rbacPermissionRoleId) {
		super.setId(rbacPermissionRoleId);
	}

	@Getter
	@Setter
	private Long rbacPermissionId;

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
	private String rbacPermissionCode;

	@Getter
	@Setter
	private String rbacPermissionName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacPermissionRoleDao repository() {
		return (RbacPermissionRoleDao) ApplicationContextUtil
				.getBean("rbacPermissionRoleDao");
	}

}