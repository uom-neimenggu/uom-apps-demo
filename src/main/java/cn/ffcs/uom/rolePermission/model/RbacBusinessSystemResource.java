package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemResourceDao;

public class RbacBusinessSystemResource extends UomEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacBusinessSysResourceId() {
		return super.getId();
	}

	public void setRbacBusinessSysResourceId(Long rbacBusinessSysResourceId) {
		super.setId(rbacBusinessSysResourceId);
	}

	@Getter
	@Setter
	private Long rbacBusinessSystemId;

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
	private String rbacBusinessSystemCode;

	@Getter
	@Setter
	private String rbacBusinessSystemName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacBusinessSystemResourceDao repository() {
		return (RbacBusinessSystemResourceDao) ApplicationContextUtil
				.getBean("rbacBusinessSystemResourceDao");
	}

}