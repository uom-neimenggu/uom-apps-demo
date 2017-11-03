package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleLocalNetworkDao;

/**
 * 角色行政管理区域关系实体.
 * 
 * @author
 * 
 **/
public class RbacRoleLocalNetwork extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色行政管理区域关系标识.
	 **/
	public Long getRbacRoleLocalNetworkId() {
		return super.getId();
	}

	public void setRbacRoleLocalNetworkId(Long rbacRoleLocalNetworkId) {
		super.setId(rbacRoleLocalNetworkId);
	}

	/**
	 * 角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacRoleId;

	/**
	 * 行政管理区域标识.
	 **/
	@Getter
	@Setter
	private Long rbacLocalNetworkId;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleLocalNetworkDao repository() {
		return (RbacRoleLocalNetworkDao) ApplicationContextUtil
				.getBean("rbacRoleLocalNetworkDao");
	}

}
