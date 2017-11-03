package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleGroupDao;

public class RbacRoleGroup extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleGroupId() {
		return super.getId();
	}

	public void setRbacRoleGroupId(Long rbacRoleGroupId) {
		super.setId(rbacRoleGroupId);
	}

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private Long rbacGroupId;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleGroupDao repository() {
		return (RbacRoleGroupDao) ApplicationContextUtil
				.getBean("rbacRoleGroupDao");
	}

}