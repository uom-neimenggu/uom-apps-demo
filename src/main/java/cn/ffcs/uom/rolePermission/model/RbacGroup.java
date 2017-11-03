package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacGroupDao;

public class RbacGroup extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacGroupId() {
		return super.getId();
	}

	public void setRbacGroupId(Long rbacGroupId) {
		super.setId(rbacGroupId);
	}

	@Getter
	@Setter
	private String rbacGroupCode;

	@Getter
	@Setter
	private String rbacGroupName;

	@Getter
	@Setter
	private String rbacGroupType;

	@Getter
	@Setter
	private String rbacGroupDesc;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacGroupDao repository() {
		return (RbacGroupDao) ApplicationContextUtil.getBean("rbacGroupDao");
	}

}