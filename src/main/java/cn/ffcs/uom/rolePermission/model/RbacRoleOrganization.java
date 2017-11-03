package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationDao;

public class RbacRoleOrganization extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleOrgId() {
		return super.getId();
	}

	public void setRbacRoleOrgId(Long rbacRoleOrgId) {
		super.setId(rbacRoleOrgId);
	}

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private Long rbacOrgId;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String orgCode;

	@Getter
	@Setter
	private String orgName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleOrganizationDao repository() {
		return (RbacRoleOrganizationDao) ApplicationContextUtil
				.getBean("rbacRoleOrganizationDao");
	}

}