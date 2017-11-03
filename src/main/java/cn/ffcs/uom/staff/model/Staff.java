package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.manager.impl.FilterConfigManagerImpl;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 员工实体类封装 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
public class Staff extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工标识
	 */
	public Long getStaffId() {
		return super.getId();
	}

	public void setStaffId(Long staffId) {
		super.setId(staffId);
	}

	/**
	 * 行政区域标识
	 */
	@Setter
	@Getter
	private Long locationId;
	/**
	 * 参与人角色标识
	 */
	@Setter
	@Getter
	private Long partyRoleId;
	/**
	 * 员工编码
	 */
	@Setter
	@Getter
	private String staffCode;
	/**
	 * 员工工号
	 */
	@Setter
	@Getter
	private String staffNbr;
	/**
	 * 员工姓名
	 */
	@Getter
	private String staffName;

	public void setStaffName(String staffName) {
		/*
		 * FilterConfigManager fcm = new FilterConfigManagerImpl();
		 * List<FilterConfig> filterList = fcm.findAllByActive();
		 * for(FilterConfig fc : filterList) staffName =
		 * staffName.replaceAll(fc.getFilterChar(), "");
		 */
		// this.staffName = staffName;
		FilterConfigManager fcm = new FilterConfigManagerImpl();
		this.staffName = fcm.filterAllByActive(staffName);
	}

	/**
	 * 员工描述
	 */
	@Setter
	@Getter
	private String staffDesc;
	/**
	 * 员工职位
	 */
	@Setter
	@Getter
	private String staffPosition;

	/**
	 * 职务标注
	 */
	@Setter
	@Getter
	private String titleNote;

	/**
	 * 职务标注
	 */
	@Setter
	@Getter
	private Long orgId;
	/**
	 * 行政区域名
	 */
	@Setter
	@Getter
	private String locationName;

	/**
	 * 参与人角色类型
	 */
	@Setter
	@Getter
	private String roleType;

	/**
	 * mac地址
	 */
	@Setter
	@Getter
	private String mac;

	/**
	 * 兼职/全职
	 */
	@Setter
	@Getter
	private String partTime;

	/**
	 * 用工性质
	 */
	@Setter
	@Getter
	private String workProp;
	
	/**
	 * 人员属性
	 */
	@Setter
	@Getter
	private String staffProperty;
	
	/**
	 * 原因
	 */
	@Setter
	@Getter
	private String reason;

	/**
	 * 全局标识码
	 */
	@Setter
	@Getter
	private String uuid;

	/**
	 * 员工修复ID
	 */
	@Setter
	@Getter
	private Long staffFixId;
	
	public Staff() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return Staff
	 */
	public static Staff newInstance() {
		return new Staff();
	}

	/**
	 * 参与人
	 */
	@Setter
	@Getter
	private Party party;

	/**
	 * 员工账号
	 */
	@Setter
	@Getter
	private StaffAccount objStaffAccount;

	/**
	 * 员工扩展属性
	 */
	@Setter
	@Getter
	private List<StaffExtendAttr> staffExtendAttr;

	/**
	 * 员工账号
	 */
	@Setter
	@Getter
	private String staffAccount;
	/**
	 * 集团UID
	 */
	@Setter
	@Getter
	private String guid;

	@Setter
	@Getter
	private String staffPassword;

	/**************** 人力中间表开始 **************/
	/**
	 * 员工工号
	 */
	@Setter
	@Getter
	private String hrStaffNbr;

	/**
	 * 员工账号
	 */
	@Setter
	@Getter
	private String hrStaffAccount;

	/**
	 * 组织关系
	 */
	@Setter
	@Getter
	private OrganizationRelation organizationRelation;

	/**
	 * 组织关系列表
	 */
	@Setter
	@Getter
	private List<OrganizationRelation> organizationRelations;
	/**************** 人力中间表结束 **************/

	/**
	 * 当前状态
	 */
	@Setter
	@Getter
	private String currentStatus;
	/**
	 * 修改员工接口是否需要更新
	 */
	@Setter
	@Getter
	private Boolean isNeedUpdate = false;
	/**
	 * 数据权限：区域
	 */
	@Getter
	@Setter
	private TelcomRegion permissionTelcomRegion;

	/**
	 * 数据权限：组织
	 */
	@Setter
	@Getter
	private List<Organization> permissionOrganizationList;

	public StaffAccount getStaffAccountHis() {
		List<StaffAccount> staffList = null;

		if (this.getStaffId() != null) {
			List<Object> params = new ArrayList<Object>();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM STAFF_ACCOUNT WHERE STAFF_ID= ?");
			params.add(this.getStaffId());
			if (this.getEffDate() != null) {
				sql.append(" AND EFF_DATE <= ?");
				params.add(this.getEffDate());
			}
			staffList = (List<StaffAccount>) DefaultDaoFactory.getDefaultDao()
					.jdbcFindList(sql.toString(), params, StaffAccount.class);
			if (staffList != null && staffList.size() > 0) {
				return staffList.get(0);
			} else {
				StringBuffer sqlHis = new StringBuffer();
				List<Object> paramsHis = new ArrayList<Object>();
				sqlHis.append("SELECT * FROM STAFF_ACCOUNT_HIS WHERE STAFF_ID = ?");
				paramsHis.add(this.getStaffId());
				if (this.getEffDate() != null) {
					sqlHis.append(" AND EFF_DATE <= ?");
					paramsHis.add(this.getEffDate());
				}
				if (this.getExpDate() != null) {
					sqlHis.append("AND EXP_DATE > ?");
					paramsHis.add(this.getExpDate());
				}
				staffList = (List<StaffAccount>) DefaultDaoFactory
						.getDefaultDao().jdbcFindList(sqlHis.toString(),
								paramsHis, StaffAccount.class);
				if (staffList != null && staffList.size() > 0) {
					return staffList.get(0);
				}
			}

		}

		return null;
	}

	public static StaffDao repository() {
		return (StaffDao) ApplicationContextUtil.getBean("staffDao");
	}

	public static OrganizationDao repositoryOrg() {
		return (OrganizationDao) ApplicationContextUtil
				.getBean("organizationDao");
	}

	public String getPartTimeName() {
		if (!StrUtil.isNullOrEmpty(this.getPartTime())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Staff", "parttime", this.getPartTime(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getStaffPositionName() {
		if (!StrUtil.isNullOrEmpty(this.getStaffPosition())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Staff", "staffPosition", this.getStaffPosition(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getRoleTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getRoleType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"PartyRole", "roleType", this.getRoleType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getWorkPropName() {
		if (!StrUtil.isNullOrEmpty(this.getWorkProp())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Staff", "workProp", this.getWorkProp(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}
	
	public String getStaffPropertyName() {
		if (!StrUtil.isNullOrEmpty(this.getStaffProperty())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Staff", "staffProperty", this.getStaffProperty(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getStatusCdName() {
		if (!StrUtil.isNullOrEmpty(this.getStatusCd())) {
			if (BaseUnitConstants.ENTT_STATE_ACTIVE.equals(this.getStatusCd())) {
				return "生效";
			} else {
				return "失效";
			}
		}
		return "";
	}

	public Organization getOrganization() {
		if (!StrUtil.isNullOrEmpty(this.getOrgId())) {
			Organization organization = repositoryOrg()
					.getById(this.getOrgId());
			if (null != organization) {
				return organization;
			}
		}
		return null;
	}

	/**
	 * 新增STAFF_FIX_ID字段，在这里赋值
	 */
	public void add() {
		this.staffFixId = repository().getSeqStaffFixId();
		super.add();
	}

	/**
	 * 获取员工帐号
	 * 
	 * @return
	 */
	public StaffAccount getStaffAccountFromDB() {
		if (this.getStaffId() != null) {
			String sql = "SELECT * FROM STAFF_ACCOUNT WHERE STATUS_CD = ? AND STAFF_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getStaffId());
			List<StaffAccount> list = this.repository().jdbcFindList(sql,
					params, StaffAccount.class);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取员工组织关系
	 */
	public List<StaffOrganization> getStaffOrganizationList() {
		if (this.getStaffId() != null) {
			String sql = "SELECT * FROM STAFF_ORGANIZATION WHERE STATUS_CD = ? AND STAFF_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getStaffId());
			return this.repository().jdbcFindList(sql, params,
					StaffOrganization.class);
		}
		return null;
	}

	/**
	 * 获取员工组织关系(忽略status_cd)
	 */
	public List<StaffOrganization> getStaffOrganizationListNoStatusCd() {
		if (this.getStaffId() != null) {
			String sql = "SELECT * FROM STAFF_ORGANIZATION WHERE STAFF_ID = ?";
			List params = new ArrayList();
			params.add(this.getStaffId());
			return this.repository().jdbcFindList(sql, params,
					StaffOrganization.class);
		}
		return null;
	}

	/**
	 * 获取员工归属组织关系
	 */
	public StaffOrganization getStaffOrganization() {
		List<StaffOrganization> list = this.getStaffOrganizationList();
		if (list != null && list.size() > 0) {
			for (StaffOrganization so : list) {
				if (so != null
						&& BaseUnitConstants.RALA_CD_1.equals(so.getRalaCd())) {
					return so;
				}
			}
		}
		return null;
	}

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	public List<StaffExtendAttr> getStaffExtendAttrList() {
		if (this.staffExtendAttr == null || staffExtendAttr.size() <= 0) {
			if (this.getStaffId() != null) {
				String sql = "SELECT * FROM STAFF_EXTEND_ATTR A WHERE A.STATUS_CD = ? AND A.STAFF_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getStaffId());
				staffExtendAttr = this.repository().jdbcFindList(sql, params,
						StaffExtendAttr.class);
			}
		}
		return staffExtendAttr;
	}
}
