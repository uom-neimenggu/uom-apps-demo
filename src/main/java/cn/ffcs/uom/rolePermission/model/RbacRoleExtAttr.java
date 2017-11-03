package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

public class RbacRoleExtAttr extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleExtAttrId() {
		return super.getId();
	}

	public void setRbacRoleExtAttrId(Long rbacRoleExtAttrId) {
		super.setId(rbacRoleExtAttrId);
	}

	@Getter
	@Setter
	private Long rbacRoleId;

	@Getter
	@Setter
	private Long rbacRoleAttrSpecId;

	@Getter
	@Setter
	private String rbacRoleAttrValue;

}