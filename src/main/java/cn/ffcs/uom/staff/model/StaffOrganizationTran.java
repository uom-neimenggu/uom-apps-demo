package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.systemconfig.model.AttrExtValue;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/***
 * 员工组织业务关系 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-9
 * @Email wangyong@ffcs.cn
 * @功能说明：
 * 
 */
public class StaffOrganizationTran extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工组织业务关系标识
	 */
	public Long getStaffOrgTranId() {
		return super.getId();
	}

	public void setStaffOrgTranId(Long staffOrgTranId) {
		super.setId(staffOrgTranId);
	}

	/**
	 * 员工标识
	 */
	@Getter
	@Setter
	private Long staffId;

	/**
	 * 员工账号
	 */
	@Getter
	@Setter
	private String staffAccount;

	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;

	/**
	 * 网格单元ID
	 */
	@Getter
	@Setter
	private Long mmeFid;

	/**
	 * 网格单元名称
	 */
	@Getter
	@Setter
	private String gridName;
	
	/**
	 * 区域名称
	 */
	@Getter
	@Setter
	private String areaName;
	
	/**
	 * 子区域名称
	 */
	@Getter
	@Setter
	private String subareaName;

	/**
	 * 组织树标识
	 */
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 关联类型
	 */
	@Getter
	@Setter
	private String ralaCd;

	/**
	 * 员工排序
	 */
	@Getter
	@Setter
	private Long staffSort;

	/**
	 * 数据权限：区域
	 */
	@Getter
	@Setter
	private TelcomRegion permissionTelcomRegion;

	public String getRalaCdName() {
		if (!StrUtil.isNullOrEmpty(this.getRalaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"StaffOrganizationTran", "ralaCd", this.getRalaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public Long getRalaCdNumber() {
		if (!StrUtil.isNullOrEmpty(this.getRalaCd())) {
			List<AttrExtValue> list = UomClassProvider
					.jdbcGetAttrExtValueByValue("StaffOrganizationTran",
							"ralaCd", this.getRalaCd(),
							BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				if (IdcardValidator.isDigital(list.get(0).getAttrExtValue())) {
					return Long.parseLong(list.get(0).getAttrExtValue());
				}
			}
		}
		return null;
	}

	/**
	 * 获取员工
	 * 
	 * @return
	 */
	public Staff getStaff() {
		if (this.getStaffId() != null) {
			Object object = Staff.repository().getObject(Staff.class,
					this.getStaffId());
			if (object != null) {
				return (Staff) object;
			}
		}
		return null;
	}

	/**
	 * 获取员工姓名
	 * 
	 * @return
	 */
	public String getStaffName() {
		Staff staff = this.getStaff();
		if (staff != null) {
			return staff.getStaffName();
		}
		return "";
	}

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.orgId != null) {
			return (Organization) Organization.repository().getObject(
					Organization.class, this.orgId);
		}
		return null;
	}

	/**
	 * 获取网格单元组织
	 * 
	 * @return
	 */
	public GridUnit getGridUnit() {
		if (this.getOrgId() != null) {
			String sql = "SELECT * FROM GRID_UNIT B WHERE B.STATUS_CD = ? AND B.GRID_UNIT_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgId());
			List<GridUnit> gridUnitList = this.repository().jdbcFindList(sql,
					params, GridUnit.class);
			if (gridUnitList != null && gridUnitList.size() > 0) {
				return gridUnitList.get(0);
			}
		}
		return null;
	}

	// /**
	// * 获取组织名称
	// *
	// * @return
	// */
	// public String getGridName() {
	// GridUnit gridUnit = this.getGridUnit();
	// if (gridUnit != null) {
	// return gridUnit.getGridName();
	// }
	// return "";
	// }

	// /**
	// * 获取全息网格标识
	// *
	// * @return
	// */
	// public Long getMmeFid() {
	// GridUnit gridUnit = this.getGridUnit();
	// if (gridUnit != null) {
	// return gridUnit.getMmeFid();
	// }
	// return null;
	// }

	/**
	 * 获取组织名称
	 * 
	 * @return
	 */
	public String getOrganizationName() {
		Organization org = this.getOrganization();
		if (org != null) {
			if (!StrUtil.isEmpty(org.getOrgFullName())) {
				return org.getOrgFullName();
			} else {
				return org.getOrgName();
			}
		}
		return "";
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrganizationDao repository() {
		return (OrganizationDao) ApplicationContextUtil
				.getBean("organizationDao");
	}
}
