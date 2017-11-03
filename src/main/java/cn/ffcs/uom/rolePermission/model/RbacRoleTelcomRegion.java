package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleTelcomRegionDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 角色电信管理区域关系实体.
 * 
 * @author
 * 
 **/
public class RbacRoleTelcomRegion extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色电信管理区域关系标识.
	 **/
	public Long getRbacRoleTelcomRegionId() {
		return super.getId();
	}

	public void setRbacRoleTelcomRegionId(Long rbacRoleTelcomRegionId) {
		super.setId(rbacRoleTelcomRegionId);
	}

	/**
	 * 角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacRoleId;

	/**
	 * 电信管理区域标识.
	 **/
	@Getter
	@Setter
	private Long rbacTelcomRegionId;

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String regionCode;

	@Getter
	@Setter
	private String regionName;

	/**
	 * 获取电信区域
	 * 
	 * @return
	 */
	public TelcomRegion getTelcomRegion() {
		if (this.rbacTelcomRegionId != null) {
			return (TelcomRegion) repository().getObject(TelcomRegion.class,
					this.rbacTelcomRegionId);
		}
		return null;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleTelcomRegionDao repository() {
		return (RbacRoleTelcomRegionDao) ApplicationContextUtil
				.getBean("rbacRoleTelcomRegionDao");
	}

}
