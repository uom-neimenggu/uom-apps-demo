package cn.ffcs.uom.gridUnit.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.gridUnit.dao.GridUnitRefDao;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class GridUnitRef extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID.
	 **/
	public Long getGridUnitRefId() {
		return super.getId();
	}

	public void setGridUnitRefId(Long gridUnitRefId) {
		super.setId(gridUnitRefId);
	}

	/**
	 * 网格单元标识.
	 **/
	@Getter
	@Setter
	private Long orgId;

	/**
	 * 网格单元ID.
	 **/
	@Getter
	@Setter
	private Long mmeFid;

	/**
	 * 网格ID.
	 **/
	@Getter
	@Setter
	private Long relaOrgId;

	/**
	 * 网格单元名称.
	 **/
	@Getter
	@Setter
	private String gridName;

	/**
	 * 网格组织编码.
	 **/
	@Getter
	@Setter
	private String orgCode;

	/**
	 * 网格名称.
	 **/
	@Getter
	@Setter
	private String orgName;

	/**
	 * 数据权限：区域
	 */
	@Getter
	@Setter
	private TelcomRegion permissionTelcomRegion;

	/**
	 * 获取网格单元
	 * 
	 * @return
	 */
	public GridUnit getGridUnit() {
		if (this.getOrgId() != null) {
			Object object = this.repository().getObject(GridUnit.class,
					this.getOrgId());
			if (object != null) {
				return (GridUnit) object;
			}
		}
		return null;
	}

	/**
	 * 获取网格单元名称
	 * 
	 * @return
	 */
	// public String getGridName() {
	// GridUnit gridUnit = this.getGridUnit();
	// if (gridUnit != null) {
	// return gridUnit.getGridName();
	// }
	// return "";
	// }

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.getRelaOrgId() != null) {
			Object object = this.repository().getObject(Organization.class,
					this.getRelaOrgId());
			if (object != null) {
				return (Organization) object;
			}
		}
		return null;
	}

	/**
	 * 获取组织名称
	 * 
	 * @return
	 */
	// public String getOrgName() {
	// Organization org = this.getOrganization();
	// if (org != null) {
	// if (!StrUtil.isEmpty(org.getOrgFullName())) {
	// return org.getOrgFullName();
	// } else {
	// return org.getOrgName();
	// }
	// }
	// return "";
	// }

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static GridUnitRefDao repository() {
		return (GridUnitRefDao) ApplicationContextUtil
				.getBean("gridUnitRefDao");
	}

}
