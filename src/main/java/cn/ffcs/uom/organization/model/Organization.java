package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.vo.OrganizationRelationNameVo;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.manager.impl.FilterConfigManagerImpl;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 组织实体.
 * 
 * @author
 * 
 **/
public class Organization extends UomEntity implements TreeNodeEntity,
		Serializable {

	/**
	 * 组织规格ID
	 */
	public Long getOrgId() {
		return super.getId();
	}

	public void setOrgId(Long orgId) {
		super.setId(orgId);
	}

	/**
	 * 关联组织标识.
	 **/
	@Getter
	@Setter
	private Long relaOrgId;

	/**
	 * 行政区域标识.
	 **/
	@Getter
	@Setter
	private Long locationId;
	/**
	 * 参与人角色标识.
	 **/
	@Getter
	@Setter
	private Long partyId;
	/**
	 * 组织编码.
	 **/
	@Getter
	@Setter
	private String orgCode;
	// /**
	// * 集团渠道编码.
	// **/
	// @Getter
	// @Setter
	// private String groupChannelCode;
	/**
	 * EDA编码.
	 **/
	@Getter
	@Setter
	private String edaCode;
	/**
	 * 组织名称.
	 **/
	@Getter
	private String orgName;

	public void setOrgName(String orgName) {
		FilterConfigManager fcm = new FilterConfigManagerImpl();
		this.orgName = fcm.filterAllByActive(orgName);
	}

	/**
	 * 组织类型.
	 **/
	@Getter
	@Setter
	private String orgType;
	/**
	 * 组织级别.
	 **/
	@Getter
	@Setter
	private String orgLeave;
	/**
	 * 存在类型.
	 **/
	@Getter
	@Setter
	private String existType;
	/**
	 * 组织英文名称.
	 **/
	@Getter
	@Setter
	private String orgNameEn;
	/**
	 * 组织简介.
	 **/
	@Getter
	@Setter
	private String orgContent;
	/**
	 * 负责人信息.
	 **/
	@Getter
	@Setter
	private String principal;
	/**
	 * 组织规模.
	 **/
	@Getter
	@Setter
	private String orgScale;
	/**
	 * 备注.
	 **/
	@Getter
	@Setter
	private String remark;
	/**
	 * 全称.
	 **/
	@Getter
	@Setter
	private String orgFullName;
	/**
	 * 简称.
	 **/
	@Getter
	@Setter
	private String orgShortName;
	/**
	 * 集团编码.
	 **/
	@Getter
	@Setter
	private String orgGroupCode;
	/**
	 * 业务编码.
	 **/
	@Getter
	@Setter
	private String orgBusinessCode;
	/**
	 * 业务编码.
	 **/
	@Getter
	@Setter
	private String areaId;
	/**
	 * 全局标识码.
	 **/
	@Getter
	@Setter
	private String uuid;
	/**
	 * 电信管理区域标识
	 */
	@Getter
	@Setter
	private Long telcomRegionId;
	/**
	 * 组织排序
	 */
	@Getter
	@Setter
	private Long orgPriority;
	/**
	 * EDA组织排序
	 */
	@Getter
	@Setter
	private Long edaSort;
	/**
	 * 区域编码
	 */
	@Getter
	@Setter
	private Long areaCodeId;
	/**
	 * 组织集团编码级别
	 */
	@Getter
	@Setter
	private String orgGroupCodeLevel;
	/**
	 * 组织集团简码
	 */
	@Getter
	@Setter
	private String orgGroupCodeSalevelCode;
	/**
	 * 城镇标识
	 */
	@Getter
	@Setter
	private String cityTown;
	/**
	 * 组织修复ID
	 */
	@Getter
	@Setter
	private Long orgFixId;
	
	/**
	 * 原因
	 */
	@Setter
	@Getter
	private String reason;
	
	// /**
	// * 财务组织编码
	// */
	// @Setter
	// @Getter
	// private String financeOrgCode;
	// /**
	// * 财务组织组编码
	// */
	// @Setter
	// @Getter
	// private String financeOrgGroupCode;
	// /**
	// * 财务组织长名称
	// */
	// @Setter
	// @Getter
	// private String financeOrgExtentName;
	// /**
	// * 财务组织短名称
	// */
	// @Setter
	// @Getter
	// private String financeOrgShortName;
	// /**
	// * 财务组织预留字段1
	// */
	// @Setter
	// @Getter
	// private String reserveOne;
	// /**
	// * 财务组织预留字段2
	// */
	// @Setter
	// @Getter
	// private String reserveTwo;
	/**
	 * 组织联系
	 */
	@Setter
	private OrgContactInfo organizationContactInfo;
	/**
	 * 下级关联组织
	 */
	@Setter
	private List<Organization> subOrganizationList;
	/**
	 * 下级归属关联组织
	 */
	@Setter
	private List<Organization> subRelaCd0101OrganizationList;
	/**
	 * 岗位
	 */
	@Setter
	private List<Position> positionList;
	/**
	 * 员工
	 */
	@Setter
	private List<Staff> staffList;
	/**
	 * 组织业务关系-域内
	 */
	@Setter
	private List<OrganizationTran> organizationTranListByOrgId;
	/**
	 * 组织业务关系-域内
	 */
	@Setter
	private List<OrganizationTran> organizationTranListByTranOrgId;
	/**
	 * 组织业务关系-域外
	 */
	@Setter
	private List<UomGroupOrgTran> uomGroupOrgTranList;
	/**
	 * 是否是根节点
	 */
	@Getter
	@Setter
	private Boolean isRoot = false;
	/**
	 * 树类型
	 */
	@Getter
	@Setter
	private String orgTreeId;
	/**
	 * 根节点
	 */
	@Getter
	@Setter
	private Long rootOrgId = 0l;
	/**
	 * 是否是代理商,查询使用
	 */
	@Getter
	@Setter
	private Boolean isAgent = false;
	/**
	 * 是否是内部经营实体,查询使用
	 */
	@Getter
	@Setter
	private Boolean isIbe = false;
	/**
	 * 是否是选择代理商根节点,查询使用
	 */
	@Getter
	@Setter
	private Boolean isChooseAgentRoot = false;
	/**
	 * 是否是选择内部经营实体根节点,查询使用
	 */
	@Getter
	@Setter
	private Boolean isChooseIbeRoot = false;
	
	/**
	 * 是否是营销包区第五级组织
	 */
	@Setter
	@Getter
	private Boolean isPackArea = false;
	/**
	 * 是否要包含营业网点,查询使用
	 */
	@Getter
	@Setter
	private Boolean isContainSalesNetwork = false;
	/**
	 * 是否包含内部经营实体营业网点
	 */
	@Getter
	@Setter
	private Boolean isContainIbeSalesNetwork = false;

	/**
	 * 是否排除代理商,查询使用
	 */
	@Getter
	@Setter
	private Boolean isExcluseAgent = false;
	/**
	 * 是否排除内部经营实体组织
	 */
	@Getter
	@Setter
	private Boolean isExcluseIbe = false;
	/**
	 * 组织扩展属性
	 */
	@Setter
	private List<OrganizationExtendAttr> organizationExtendAttrList;
	/**
	 * 组织类型
	 */
	@Setter
	private List<OrgType> orgTypeList;
	/**
	 * 查询使用的组织类型
	 */
	@Setter
	@Getter
	private List<OrgType> queryOrgTypeList;
	/**
	 * 组织类型
	 */
	@Setter
	@Getter
	private List<OrgType> addOrgTypeList;
	/**
	 * 组织类型
	 */
	@Setter
	@Getter
	private List<OrgType> delOrgTypeList;
	/**
	 * 组织树上级
	 */
	@Setter
	private List<Organization> treeParentOrgList;

	/**
	 * 上级组织
	 */
	@Setter
	private List<Organization> parentOrgList;

	/**
	 * 组织关系
	 */
	@Setter
	private List<OrganizationRelation> organizationRelationList;

	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private List<OrganizationRelation> DelOrganizationRelationList;
	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private List<OrganizationRelation> addOrganizationRelationList;

	/**
	 * 代理商新增组织添加
	 */
	@Setter
	@Getter
	private Party agentAddParty;

	/**
	 * 是否改变组织名称
	 */
	@Setter
	@Getter
	private Boolean isChangeOrgName = false;

	/**
	 * 查询排除的orgid
	 */
	@Setter
	@Getter
	private List<String> queryOrgIdList;

	/**
	 * 数据权限：组织
	 */
	@Setter
	@Getter
	private List<Organization> permissionOrganizationList;

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT * FROM ORGANIZATION T1 WHERE T1.STATUS_CD = ? AND T1.ORG_ID IN (SELECT A.ORG_ID FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? AND A.RELA_ORG_ID = ?) ORDER BY T1.ORG_PRIORITY";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getOrgId());
		List<Organization> list = Organization.repository().jdbcFindList(sql,
				params, Organization.class);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (Organization org : list) {
				org.setOrgTreeId(this.orgTreeId);
				treeNodelist.add(org);
			}
		}
		return treeNodelist;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.orgName)) {
			return this.orgName;
		}
		return "";
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		String sql = "SELECT * FROM ORGANIZATION T1 WHERE T1.STATUS_CD = ? AND T1.ORG_ID IN (SELECT A.ORG_ID FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? AND A.RELA_ORG_ID = ?) ORDER BY T1.ORG_PRIORITY";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.rootOrgId);
		List<Organization> list = Organization.repository().jdbcFindList(sql,
				params, Organization.class);
		ArrayList<TreeNodeEntity> treeNodelist = new ArrayList<TreeNodeEntity>();
		if (list != null) {
			for (Organization org : list) {
				org.setOrgTreeId(this.orgTreeId);
				treeNodelist.add(org);
			}
		}
		return treeNodelist;
	}

	@Override
	public boolean isGetRoot() {
		return isRoot;
	}

	/**
	 * 构造方法
	 */
	public Organization() {
		super();
	}

	public Organization(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(this.getClass());
			if (!StrUtil.isEmpty(seqName)) {
				this.setOrgId(repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return TreeOrgRelaTypeRule
	 */
	public static Organization newInstance() {
		return new Organization();
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

	/**
	 * 获取联系信息
	 * 
	 * @return
	 */
	public OrgContactInfo getOrganizationContactInfo() {
		if (this.organizationContactInfo != null) {
			return organizationContactInfo;
		}
		List<OrgContactInfo> organizationContactInfoList = null;
		if (this.getOrgId() != null) {
			List params = new ArrayList();
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM ORG_CONTACT_INFO WHERE STATUS_CD = ? AND ORG_ID = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgId());
			organizationContactInfoList = this.repository().jdbcFindList(
					sql.toString(), params, OrgContactInfo.class);
			if (organizationContactInfoList != null
					&& organizationContactInfoList.size() > 0) {
				organizationContactInfo = organizationContactInfoList.get(0);
			}
		}
		return organizationContactInfo;
	}

	/**
	 * 获取联系信息
	 * 
	 * @return
	 */
	public List<OrgContactInfo> getOrganizationContactInfoList() {
		List<OrgContactInfo> organizationContactInfoList = null;
		if (this.getOrgId() != null) {
			List params = new ArrayList();
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM ORG_CONTACT_INFO WHERE STATUS_CD = ? AND ORG_ID = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgId());
			organizationContactInfoList = this.repository().jdbcFindList(
					sql.toString(), params, OrgContactInfo.class);
		}
		return organizationContactInfoList;
	}

	/**
	 * 获取组织参与人
	 * 
	 * @return
	 */
	public Party getParty() {
		if (this.getPartyId() != null) {
			return (Party) Organization.repository().getObject(Party.class,
					this.getPartyId());
		}
		return null;
	}

	/**
	 * 获取组织参与人
	 * 
	 * @return
	 */
	public List<PartyOrganization> getPartyOrganizationList(Long partyId) {
		List<PartyOrganization> partyOrganizationList = null;
		if (partyId != null) {
			List params = new ArrayList();
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM PARTY_ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(partyId);
			partyOrganizationList = this.repository().jdbcFindList(
					sql.toString(), params, PartyOrganization.class);
		}
		return partyOrganizationList;
	}

	/**
	 * 获取级别名称
	 * 
	 * @return
	 */
	public String getOrgLeaveName() {
		if (!StrUtil.isEmpty(this.getOrgLeave())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Organization", "orgLeave", this.getOrgLeave(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取组织规模名称
	 * 
	 * @return
	 */
	public String getOrgScaleName() {
		if (!StrUtil.isEmpty(this.getOrgScale())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Organization", "orgScale", this.getOrgScale(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取行政区域
	 * 
	 * @return
	 */
	public PoliticalLocation getPoliticalLocation() {
		if (this.getLocationId() != null) {
			return (PoliticalLocation) repository().getObject(
					PoliticalLocation.class, this.getLocationId());
		}
		return null;
	}

	/**
	 * 获取下级组织
	 * 
	 * @return
	 */
	public List<Organization> getSubOrganizationList() {
		if (subOrganizationList == null || subOrganizationList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT A.* FROM ORGANIZATION A,ORGANIZATION_RELATION B WHERE A.STATUS_CD=? AND A.ORG_ID=B.ORG_ID AND B.STATUS_CD=? AND B.RELA_ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				subOrganizationList = Organization.repository().jdbcFindList(
						sql, params, Organization.class);
			}
		}
		return subOrganizationList;
	}

	/**
	 * 获取归属下级组织
	 * 
	 * @return
	 */
	public List<Organization> getRelacd0101SubOrganizationList() {
		if (subRelaCd0101OrganizationList == null
				|| subRelaCd0101OrganizationList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT A.* FROM ORGANIZATION A,ORGANIZATION_RELATION B WHERE A.STATUS_CD=? AND A.ORG_ID=B.ORG_ID AND B.RELA_CD=? AND B.STATUS_CD=? AND B.RELA_ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				subRelaCd0101OrganizationList = Organization.repository()
						.jdbcFindList(sql, params, Organization.class);
			}
		}
		return subRelaCd0101OrganizationList;
	}

	/**
	 * 获取岗位
	 * 
	 * @return
	 */
	public List<Position> getPositionList() {
		if (positionList == null || positionList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT A.* FROM POSITION A,ORG_POSITION B WHERE A.STATUS_CD=? AND A.POSITION_ID=B.POSITION_ID AND B.STATUS_CD=? AND B.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				positionList = Organization.repository().jdbcFindList(sql,
						params, Position.class);
			}
		}
		return positionList;
	}

	/**
	 * 获取员工
	 * 
	 * @return
	 */
	public List<Staff> getStaffList() {
		if (staffList == null || staffList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM STAFF_ORGANIZATION A,STAFF B WHERE B.STATUS_CD=? AND B.STAFF_ID=A.STAFF_ID AND A.STATUS_CD=? AND A.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				staffList = Organization.repository().jdbcFindList(sql, params,
						Staff.class);
			}
		}
		return staffList;
	}

	/**
	 * 获取业务组织关系-域内
	 * 
	 * @return
	 */
	public List<OrganizationTran> getOrganizationTranListByOrgId() {
		if (organizationTranListByOrgId == null
				|| organizationTranListByOrgId.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORGANIZATION_TRAN A WHERE A.STATUS_CD = ? AND A.ORG_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				organizationTranListByOrgId = OrganizationTran.repository()
						.jdbcFindList(sql, params, OrganizationTran.class);
			}
		}
		return organizationTranListByOrgId;
	}

	/**
	 * 获取业务组织关系-域内
	 * 
	 * @return
	 */
	public List<OrganizationTran> getOrganizationTranListByTranOrgId() {
		if (organizationTranListByTranOrgId == null
				|| organizationTranListByTranOrgId.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORGANIZATION_TRAN A WHERE A.STATUS_CD = ? AND A.TRAN_ORG_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				organizationTranListByTranOrgId = OrganizationTran.repository()
						.jdbcFindList(sql, params, OrganizationTran.class);
			}
		}
		return organizationTranListByTranOrgId;
	}

	/**
	 * 获取业务组织关系-域外
	 * 
	 * @return
	 */
	public List<UomGroupOrgTran> getUomGroupOrgTranList() {
		if (uomGroupOrgTranList == null || uomGroupOrgTranList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM UOM_GROUP_ORGANIZATION_TRAN A WHERE A.STATUS_CD = ? AND A.ORG_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				uomGroupOrgTranList = UomGroupOrgTran.repository()
						.jdbcFindList(sql, params, UomGroupOrgTran.class);
			}
		}
		return uomGroupOrgTranList;
	}

	/**
	 * 获取电信区域
	 * 
	 * @return
	 */
	public TelcomRegion getTelcomRegion() {
		if (this.telcomRegionId != null) {
			return (TelcomRegion) repository().getObject(TelcomRegion.class,
					this.telcomRegionId);
		}
		return null;
	}

	/**
	 * 获取电信区域
	 * 
	 * @return
	 */
	public TelcomRegion getTelcomRegionByRegionCode(String regionCode) {
		if (!StrUtil.isEmpty(regionCode)) {
			String sql = "SELECT * FROM TELCOM_REGION A WHERE A.STATUS_CD = ? AND A.REGION_CODE = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(regionCode);
			return repository().jdbcFindObject(sql, params, TelcomRegion.class);
		}
		return null;
	}

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	public List<OrganizationExtendAttr> getOrganizationExtendAttrList() {
		if (this.organizationExtendAttrList == null
				|| organizationExtendAttrList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORGANIZATION_EXTEND_ATTR A WHERE A.STATUS_CD=? AND A.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				organizationExtendAttrList = this.repository().jdbcFindList(
						sql, params, OrganizationExtendAttr.class);
			}
		}
		return organizationExtendAttrList;
	}

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	public List<OrganizationExtendAttr> getOldOrganizationExtendAttrList() {

		List<OrganizationExtendAttr> oldOrganizationExtendAttrList = null;

		if (this.getOrgId() != null) {
			String sql = "SELECT * FROM ORGANIZATION_EXTEND_ATTR A WHERE A.STATUS_CD=? AND A.ORG_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgId());
			oldOrganizationExtendAttrList = this.repository().jdbcFindList(sql,
					params, OrganizationExtendAttr.class);
		}

		return oldOrganizationExtendAttrList;

	}

	/**
	 * 获取组织类型
	 * 
	 * @return
	 */
	public List<OrgType> getOrgTypeList() {
		if (this.orgTypeList == null || this.orgTypeList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORG_TYPE A WHERE A.STATUS_CD=? AND A.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				orgTypeList = this.repository().jdbcFindList(sql, params,
						OrgType.class);
			}
		}
		return orgTypeList;
	}

	/**
	 * 获取组织类型
	 * 
	 * @return
	 */
	public List<OrgType> getCurrOrgTypeList() {
		return this.orgTypeList;
	}

	/**
	 * 获取组织类型名称
	 * 
	 * @return
	 */
	public String getOrganizationTypeName() {
		List<OrgType> list = this.getOrgTypeList();
		String name = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				OrgType orgType = list.get(i);
				String temp = orgType.getOrgTypeCdName();
				if (!StrUtil.isEmpty(temp)) {
					if (i == list.size() - 1) {
						name += orgType.getOrgTypeCdName();
					} else {
						name += orgType.getOrgTypeCdName() + ",";
					}
				}
			}
			return name;
		}
		return "";
	}

	/**
	 * 是否是行政
	 * 
	 * @return
	 */
	public boolean isAdministrative() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.ADMINISTRATIVE_PRE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否是单位
	 * 
	 * @return
	 */
	public boolean isCompany() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.COMPANY_PRE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否是非单位
	 * 
	 * @return
	 */
	public boolean isNotCompany() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.COMPANY_PRE)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 是否是部门
	 * 
	 * @return
	 */
	public boolean isDepartment() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.DEPARTMENT_PRE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否是团队
	 * 
	 * @return
	 */
	public boolean isTeam() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.DEPARTMENT_TEAM)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public String getMaxSubOrgGroupCodeSalevelCode() {// REGEXP_REPLACE解决取最大值中字母替换问题
		if (this.getOrgId() != null) {
			String sql = "SELECT NVL(MAX(TO_NUMBER(REGEXP_REPLACE(B.ORG_GROUP_CODE_SALEVEL_CODE,'[[:alpha:]]',0))),0)+1 SEQ FROM ORGANIZATION_RELATION A, ORGANIZATION B WHERE A.ORG_ID = B.ORG_ID AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RELA_CD = ? AND A.RELA_ORG_ID = ?";
			List list = this
					.repository()
					.getJdbcTemplate()
					.queryForList(sql, BaseUnitConstants.ENTT_STATE_ACTIVE,
							BaseUnitConstants.ENTT_STATE_ACTIVE,
							OrganizationConstant.RELA_CD_INNER, this.getOrgId());
			if (list != null && list.size() > 0) {
				Map map = (Map) list.get(0);
				if (map != null) {
					return map.get("SEQ") + "";
				}
			}
		}
		return "";
	}

	/**
	 * 树查询获取根节点
	 * 
	 * @return
	 */
	public List<Organization> getTreeParentOrgList() {
		if (treeParentOrgList == null || treeParentOrgList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT T1.* FROM ORGANIZATION T1,(SELECT ROWNUM SEQ, T.* FROM (SELECT * FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ? ) START WITH ORG_ID = ? CONNECT BY PRIOR RELA_ORG_ID = ORG_ID) T) T2 WHERE T1.ORG_ID=T2.RELA_ORG_ID AND T1.STATUS_CD= ? ORDER BY T2.SEQ DESC";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationConstant.RELA_CD_INNER);
				params.add(this.getOrgId());
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				treeParentOrgList = this.repository().jdbcFindList(sql, params,
						Organization.class);
			}
		}
		return treeParentOrgList;
	}

	/**
	 * 获取上级
	 * 
	 * @return
	 */
	public List<Organization> getParentOrgList(
			OrganizationRelation organizationRelation) {
		if (parentOrgList == null || parentOrgList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORGANIZATION A WHERE A.STATUS_CD=? AND A.ORG_ID IN (SELECT B.RELA_ORG_ID FROM ORGANIZATION_RELATION B WHERE B.STATUS_CD=? AND B.ORG_ID=? AND B.RELA_CD=?) ORDER BY A.ORG_PRIORITY";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organizationRelation.getOrgId());
				params.add(organizationRelation.getRelaCd());// 修复营销树无法添加子节点的问题
				parentOrgList = this.repository().jdbcFindList(sql, params,
						Organization.class);
			}
		}
		return parentOrgList;
	}

	/**
	 * 自动生成组织编码
	 * 
	 * @return
	 */
	public String retriveOrgCode() {
		Long seq = this.repository().jdbcGetSeqNextval(
				OrganizationConstant.SEQ_ORG_CODE);
		String orgCode = StrUtil.strnull(seq);
		int len = OrganizationConstant.LENGTH_OF_ORG_CODE;
		while (orgCode.length() < len) {
			orgCode = "0" + orgCode;
		}
		return OrganizationConstant.PREFIX_OF_ORG_CODE
				+ orgCode.substring(0, len);
	}

	/**
	 * 20130903新的组织编码生成规则
	 * 
	 * @return
	 */
	public String generateOrgCode() {
		/**
		 * 新增的时候
		 */
		TelcomRegion tr = this.getTelcomRegion();
		if (StrUtil.isEmpty(this.orgCode)) {
			Long seq = this.repository().jdbcGetSeqNextval(
					OrganizationConstant.SEQ_ORG_CODE);
			DecimalFormat decimalFormat = new DecimalFormat("000000");
			String orgCodeEnd = decimalFormat.format(seq);
			if (tr != null && !StrUtil.isEmpty(tr.getPreOrgCode())
					&& !StrUtil.isEmpty(orgCodeEnd)
					&& !StrUtil.isEmpty(this.getOrgType())) {
				if (OrganizationConstant.ORG_TYPE_N.equals(this.getOrgType())) {
					return OrganizationConstant.ORG_CODE_N + tr.getPreOrgCode()
							+ orgCodeEnd;
				} else if (OrganizationConstant.ORG_TYPE_W.equals(this
						.getOrgType())) {
					return OrganizationConstant.ORG_CODE_W + tr.getPreOrgCode()
							+ orgCodeEnd;
				}
			}
		} else {
			/**
			 * 修改的时候
			 */
			if (tr != null && !StrUtil.isEmpty(tr.getPreOrgCode())
					&& !StrUtil.isEmpty(this.getOrgType())
					&& this.orgCode.length() == 13) {
				if (OrganizationConstant.ORG_TYPE_N.equals(this.getOrgType())) {
					return OrganizationConstant.ORG_CODE_N + tr.getPreOrgCode()
							+ this.orgCode.substring(7, 13);
				} else if (OrganizationConstant.ORG_TYPE_W.equals(this
						.getOrgType())) {
					return OrganizationConstant.ORG_CODE_W + tr.getPreOrgCode()
							+ this.orgCode.substring(7, 13);
				}
			}
		}
		return "";
	}

	/**
	 * 获取组织的组织关系
	 * 
	 * @return
	 */
	public List<OrganizationRelation> getOrganizationRelationList() {
		if (organizationRelationList == null
				|| organizationRelationList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORGANIZATION_RELATION B WHERE B.STATUS_CD=? AND B.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				organizationRelationList = this.repository().jdbcFindList(sql,
						params, OrganizationRelation.class);
			}
		}
		return organizationRelationList;
	}

	/**
	 * 获取上级组织关系
	 * 
	 * @return
	 */
	public OrganizationRelation getOrganizationRelationByRelaCd(String relaCd) {
		if (this.getOrgId() != null) {
			String sql = "SELECT A.* FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? AND RELA_CD = ? AND A.ORG_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(relaCd);
			params.add(this.getOrgId());

			List<OrganizationRelation> organizationRelationList = this
					.repository().jdbcFindList(sql, params,
							OrganizationRelation.class);

			if (organizationRelationList != null
					&& organizationRelationList.size() > 0) {
				return organizationRelationList.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取组织的组织关系
	 * 
	 * @return
	 */
	public List<OrganizationRelation> getCurrOrganizationRelationList() {
		return this.organizationRelationList;
	}

	/**
	 * 获取组织性质名称
	 * 
	 * @return
	 */
	public String getOrgTypeName() {
		if (!StrUtil.isEmpty(this.getOrgType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Organization", "orgType", this.getOrgType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 新增ORG_FIX_ID字段，在这里赋值
	 */
	public void add() {
		this.orgFixId = repository().getSeqOrgFixId();
		super.add();
	}

	/**
	 * 获取组织关系-省公司及市公司
	 * 
	 * @return
	 */
	public OrganizationRelation getOrganizationRelation(String relaCd) {

		if (this.getOrgId() != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT T.* FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?) T");
			sql.append(" START WITH T.ORG_ID = ? CONNECT BY PRIOR T.RELA_ORG_ID = T.ORG_ID");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(relaCd);
			params.add(this.getOrgId());

			List<OrganizationRelation> organizationRelationList = this
					.repository().jdbcFindList(sql.toString(), params,
							OrganizationRelation.class);

			if (!StrUtil.isNullOrEmpty(organizationRelationList)
					&& organizationRelationList.size() > 2) {
				return organizationRelationList.get(organizationRelationList
						.size() - 3);
			}

		}

		return null;
	}

	/**
	 * 获取该组织子孙节点
	 * 
	 * @return
	 */
	public List<Organization> getChildrenOrganization(String relaCd) {

		if (this.getOrgId() != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM ORGANIZATION WHERE ORG_ID IN (SELECT T.ORG_ID FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?) T");
			sql.append(" START WITH T.RELA_ORG_ID = ? CONNECT BY NOCYCLE PRIOR T.ORG_ID = T.RELA_ORG_ID)");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(relaCd);
			params.add(this.getOrgId());

			return this.repository().jdbcFindList(sql.toString(), params,
					Organization.class);
		}

		return null;
	}

	/**
	 * 获取组织层级
	 * 
	 * @return
	 */
	public int getOrganizationLevel(String relaCd) {

		if (this.getOrgId() != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT T.* FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?) T");
			sql.append(" START WITH T.ORG_ID = ? CONNECT BY NOCYCLE PRIOR T.RELA_ORG_ID = T.ORG_ID");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(relaCd);
			params.add(this.getOrgId());

			List<OrganizationRelation> organizationRelationList = this
					.repository().jdbcFindList(sql.toString(), params,
							OrganizationRelation.class);

			if (!StrUtil.isNullOrEmpty(organizationRelationList)
					|| organizationRelationList.size() > 0) {
				return organizationRelationList.size() - 1;
			}

		}

		return 0;
	}

	/**
	 * 20150306 EDA组织编码生成规则 组织架构表： edaCode字段生成规则： 第一层：0
	 * 第二层：3位本地网ID(如：551，564……),除16个本地网外的，与本地网平级的二层编号：200+ltrim(to_char(
	 * SEQ_SYS_ORGCODE_LEVEL2.nextval,'000000'))
	 * 第三层：300+本地网ID+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL3.nextval,'000000'))
	 * 例：'300'||latn_id||
	 * ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL3.nextval,'000000'))
	 * 第四层：400+本地网ID+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL4.nextval,'000000'))
	 * 例：'400'||latn_id||
	 * ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL4.nextval,'000000'))
	 * 第五层：500+本地网ID+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL5.nextval,'000000'))
	 * 第六层：600+本地网ID+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL6.nextval,'000000'))
	 * 第七层：700+本地网ID+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL7.nextval,'000000'))
	 * 如果是网格或者发展人，就是700+ltrim(to_char(SEQ_SYS_ORGCODE_LEVEL7.nextval,'000000'))
	 * 
	 * @return
	 */
	public String generateEdaCode(int orgLevel, boolean isGrid) {

		TelcomRegion tr = this.getTelcomRegion();
		String areaCode = tr.getAreaCode();
		String edaCodePre = "";
		String edaCodeEnd = "";
		Long seq = 0L;

		// if (StrUtil.isEmpty(this.edaCode)) {

		DecimalFormat decimalFormat = new DecimalFormat("0000000000");
		DecimalFormat decimalFormatGrid = new DecimalFormat("0000000000");

		switch (orgLevel) {
		case 1:
			return edaCodePre + seq;
		case 2:
			if (StrUtil.isEmpty(areaCode)) {
				edaCodePre = "200";
				seq = this.repository().jdbcGetSeqNextval(
						OrganizationConstant.SEQ_EDA_CODE_LEVEL2);
			} else {
				return areaCode;
			}
			break;
		case 3:
			edaCodePre = "300";
			seq = this.repository().jdbcGetSeqNextval(
					OrganizationConstant.SEQ_EDA_CODE_LEVEL3);
			break;
		case 4:
			edaCodePre = "400";
			seq = this.repository().jdbcGetSeqNextval(
					OrganizationConstant.SEQ_EDA_CODE_LEVEL4);
			break;
		case 5:
			edaCodePre = "500";
			seq = this.repository().jdbcGetSeqNextval(
					OrganizationConstant.SEQ_EDA_CODE_LEVEL5);
			break;
		case 6:
			edaCodePre = "600";
			seq = this.repository().jdbcGetSeqNextval(
					OrganizationConstant.SEQ_EDA_CODE_LEVEL6);
			break;
		case 7:
			edaCodePre = "700";
			if (isGrid) {
				seq = this.repository().jdbcGetSeqNextval(
						OrganizationConstant.SEQ_EDA_CODE_LEVEL7_GRID);
			} else {
				seq = this.repository().jdbcGetSeqNextval(
						OrganizationConstant.SEQ_EDA_CODE_LEVEL7);
			}
			break;
		}

		edaCodeEnd = seq.toString();

		if (isGrid) {
			// edaCodeEnd = decimalFormatGrid.format(seq);
			return edaCodeEnd;
		} else {
			// edaCodeEnd = decimalFormat.format(seq);
			return edaCodePre + areaCode + edaCodeEnd;
		}

		// }

		// return null;
	}

	/**
	 * 组织类型中是否包含网格类型
	 * 
	 * @param orgTypeList
	 * @return
	 */
	public boolean isGrid(List<OrgType> orgTypeList) {
		for (OrgType orgType : orgTypeList) {
			if (OrganizationConstant.ORG_TYPE_N1101050000.equals(orgType
					.getOrgTypeCd())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取电信管理区域
	 * 
	 * @return
	 */
	public TelcomRegion getTelcomRegion(String regionCode) {
		if (!StrUtil.isEmpty(regionCode)) {
			String sql = "SELECT * FROM TELCOM_REGION B WHERE B.STATUS_CD = ? AND B.REGION_CODE = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(regionCode);
			return this.repository().jdbcFindObject(sql, params,
					TelcomRegion.class);
		}
		return null;
	}

	/**
	 * 获取行政管理区域
	 * 
	 * @return
	 */
	public PoliticalLocation getPoliticalLocation(String locationCode) {
		if (!StrUtil.isEmpty(locationCode)) {
			String sql = "SELECT * FROM POLITICAL_LOCATION B WHERE B.STATUS_CD = ? AND B.LOCATION_CODE = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(locationCode);
			return this.repository().jdbcFindObject(sql, params,
					PoliticalLocation.class);
		}
		return null;
	}

	/**
	 * 获取行政区域
	 * 
	 * @return
	 */
	public PoliticalLocation getPoliticalLocationByLocationCode(
			String locationCode) {
		if (!StrUtil.isEmpty(locationCode)) {
			String sql = "SELECT * FROM POLITICAL_LOCATION A WHERE A.STATUS_CD = ? AND A.LOCATION_CODE = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(locationCode);
			return repository().jdbcFindObject(sql, params,
					PoliticalLocation.class);
		}
		return null;
	}

	public String getGroupMailOrgCode(String relaCd) {

		if (this.getOrgId() != null) {
			// #号分隔存在问题   20170612 zhanglu,step 1
			String sql = "SELECT * FROM ( SELECT B.ORG_NAME,A.ORG_ID,A.RELA_ORG_ID,A.ORG_REL_ID,"
					+ "SYS_CONNECT_BY_PATH(B.ORG_NAME,'#') AS ORG_FULL_NAME FROM (SELECT * FROM ORGANIZATION_RELATION"
					+ " WHERE STATUS_CD = ? AND RELA_CD = ? ) A,( SELECT * FROM ORGANIZATION WHERE STATUS_CD = ? ) B"
					+ " WHERE A.ORG_ID = B.ORG_ID START WITH A.ORG_ID = ? CONNECT BY PRIOR A.RELA_ORG_ID = A.ORG_ID )"
					+ " WHERE RELA_ORG_ID = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(relaCd);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.getOrgId());
			params.add(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
			List<OrganizationRelationNameVo> list = OrganizationRelationNameVo
					.repository().jdbcFindList(sql, params,
							OrganizationRelationNameVo.class);

			if (list != null && list.size() > 0) {

				StringBuffer sb = new StringBuffer();

				String[] strArray = null;
				
				// // #号分隔存在问题   20170612 zhanglu,step 2
				if (list.get(0).getOrgFullName().indexOf("#", 1) == -1) {
					return null;
				} else {
					// #号分隔存在问题   20170612 zhanglu,step end
					strArray = list
							.get(0)
							.getOrgFullName()
							.substring(
									1,
									list.get(0).getOrgFullName()
											.lastIndexOf("#")).split("#");
				}

				if (strArray.length > 0) {

					for (int i = strArray.length - 1; i >= 0; --i) {

						if (i == strArray.length - 1) {
							sb.append(GroupMailConstant.GROUP_MAIL_AH_ORG_CODE);
						} else {
							sb.append(strArray[i]);
						}

						if (i != 0) {
							sb.append("#");
						}

					}

					for (int i = 0; i < 5 - strArray.length; i++) {
						sb.append("#000");
					}

					return sb.toString();
				}
			}

		}
		return null;
	}

	/**
	 * 是否上传给集团统一邮箱
	 * 
	 * @return
	 */
	public boolean isUploadGroupMail() {
		List<Organization> list = null;
		if (this.getOrgId() != null) {
			StringBuffer sb = new StringBuffer(
					"SELECT DISTINCT ORG.* FROM ORGANIZATION ORG, ORG_TYPE OT WHERE ORG.STATUS_CD = ?");
			sb.append(" AND OT.STATUS_CD = ? AND ORG.ORG_ID = OT.ORG_ID AND EXISTS (SELECT TR.REF_TYPE_VALUE");
			sb.append(" FROM TREE_ORG_TYPE_RULE TR WHERE TR.STATUS_CD = ? AND TR.ORG_TREE_ID = ?");
			sb.append(" AND OT.ORG_TYPE_CD = TR.REF_TYPE_VALUE) AND OT.ORG_ID = ?");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(OrganizationConstant.ORG_TREE_ID_3);
			params.add(this.getOrgId());
			list = this.repository().jdbcFindList(sb.toString(), params,
					Organization.class);

			if (list != null && list.size() > 0) {
				return true;
			}
		}
		return false;
	}

}
