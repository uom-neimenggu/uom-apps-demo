package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleBusinessSystemDao;

public class RbacUserRoleBusinessSystem extends UomEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacUserRoleBusSysId() {
		return super.getId();
	}

	public void setRbacUserRoleBusSysId(Long rbacUserRoleBusSysId) {
		super.setId(rbacUserRoleBusSysId);
	}

	@Getter
	@Setter
	private Long rbacBusinessSystemId;

	@Getter
	@Setter
	private Long rbacUserRoleId;

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

	@Getter
	@Setter
	private String rbacBusinessSystemCode;

	@Getter
	@Setter
	private String rbacBusinessSystemName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacUserRoleBusinessSystemDao repository() {
		return (RbacUserRoleBusinessSystemDao) ApplicationContextUtil
				.getBean("rbacUserRoleBusinessSystemDao");
	}

}