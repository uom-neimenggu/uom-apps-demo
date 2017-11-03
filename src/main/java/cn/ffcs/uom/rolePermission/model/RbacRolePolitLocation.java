package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.rolePermission.dao.RbacRolePolitLocationDao;

/**
 * 角色行政管理区域关系实体.
 * 
 * @author
 * 
 **/
public class RbacRolePolitLocation extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色行政管理区域关系标识.
	 **/
	public Long getRbacRolePolitLocationId() {
		return super.getId();
	}

	public void setRbacRolePolitLocationId(Long rbacRolePolitLocationId) {
		super.setId(rbacRolePolitLocationId);
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
	private Long rbacPolitLocationId;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String locationCode;

	@Getter
	@Setter
	private String locationName;

	/**
	 * 获取行政区域
	 * 
	 * @return
	 */
	public PoliticalLocation getPoliticalLocation() {
		if (this.rbacPolitLocationId != null) {
			return (PoliticalLocation) repository().getObject(
					PoliticalLocation.class, this.rbacPolitLocationId);
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRolePolitLocationDao repository() {
		return (RbacRolePolitLocationDao) ApplicationContextUtil
				.getBean("rbacRolePolitLocationDao");
	}

}
