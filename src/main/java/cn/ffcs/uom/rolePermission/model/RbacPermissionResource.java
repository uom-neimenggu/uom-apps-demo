package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionResourceDao;

public class RbacPermissionResource extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacPermissionResourceId() {
		return super.getId();
	}

	public void setRbacPermissionResourceId(Long rbacPermissionResourceId) {
		super.setId(rbacPermissionResourceId);
	}

	@Getter
	@Setter
	private Long rbacPermissionRelaId;

	@Getter
	@Setter
	private Long rbacResourceId;

	@Getter
	@Setter
	private String rbacResourceCode;

	@Getter
	@Setter
	private String rbacResourceName;

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
	public static RbacPermissionResourceDao repository() {
		return (RbacPermissionResourceDao) ApplicationContextUtil
				.getBean("rbacPermissionResourceDao");
	}

}