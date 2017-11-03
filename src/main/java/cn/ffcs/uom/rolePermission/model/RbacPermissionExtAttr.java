package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

public class RbacPermissionExtAttr extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacPermissionExtAttrId() {
		return super.getId();
	}

	public void setRbacPermissionExtAttrId(Long rbacPermissionExtAttrId) {
		super.setId(rbacPermissionExtAttrId);
	}

	@Getter
	@Setter
	private Long rbacPermissionId;

	@Getter
	@Setter
	private Long rbacPermissionAttrSpecId;

	@Getter
	@Setter
	private String rbacPermissionAttrValue;

}