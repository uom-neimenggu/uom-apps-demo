package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacUserGroupDao;

public class RbacUserGroup extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacUserGroupId() {
		return super.getId();
	}

	public void setRbacUserGroupId(Long rbacUserGroupId) {
		super.setId(rbacUserGroupId);
	}

	@Getter
	@Setter
	private Long rbacUserId;

	@Getter
	@Setter
	private Long rbacGroupId;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacUserGroupDao repository() {
		return (RbacUserGroupDao) ApplicationContextUtil
				.getBean("rbacUserGroupDao");
	}

}