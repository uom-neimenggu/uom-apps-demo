package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleBusinessSystemDao;

public class RbacRoleBusinessSystem extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleBusinessSystemId() {
		return super.getId();
	}

	public void setRbacRoleBusinessSystemId(Long rbacRoleBusinessSystemId) {
		super.setId(rbacRoleBusinessSystemId);
	}

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private Long rbacBusinessSystemId;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

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
	public static RbacRoleBusinessSystemDao repository() {
		return (RbacRoleBusinessSystemDao) ApplicationContextUtil
				.getBean("rbacRoleBusinessSystemDao");
	}

}