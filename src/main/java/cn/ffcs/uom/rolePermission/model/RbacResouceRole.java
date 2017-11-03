package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacResouceRoleDao;

public class RbacResouceRole extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacResouceRoleId() {
		return super.getId();
	}

	public void setRbacResouceRoleId(Long rbacResouceRoleId) {
		super.setId(rbacResouceRoleId);
	}

	@Getter
	@Setter
	private Long rbacResouceId;

	@Getter
	@Setter
	private Long rbacRoleId;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacResouceRoleDao repository() {
		return (RbacResouceRoleDao) ApplicationContextUtil
				.getBean("rbacResouceRoleDao");
	}

}