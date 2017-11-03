package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.UomGridCountyLog;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;

@Repository("organizationDao")
public class OrganizationDaoImpl extends BaseDaoImpl implements OrganizationDao {
	/**
	 * 根据组织编码查找组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgCode(Organization organization) {
		if (null != organization) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM ORGANIZATION T WHERE T.STATUS_CD=? AND T.ORG_CODE=?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
			Organization organ = jdbcFindObject(sql, params, Organization.class);
			return organ;
		}
		return null;
	}

	/**
	 * 根据组织编码查找组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgCode(String orgCode) {
		if (!StrUtil.isNullOrEmpty(orgCode)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM ORGANIZATION T WHERE T.STATUS_CD=? AND T.ORG_CODE=?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(orgCode));
			Organization organ = jdbcFindObject(sql, params, Organization.class);
			return organ;
		}
		return null;
	}

	/**
	 * 根据参与人ID查找组织
	 * 
	 * @param partyId
	 */
	@Override
	public Organization queryOrganizationByPartyId(Long partyId) {
		if (!StrUtil.isNullOrEmpty(partyId)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM ORGANIZATION T WHERE T.STATUS_CD = ? AND T.PARTY_ID = ?";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(partyId);
			Organization organ = jdbcFindObject(sql, params, Organization.class);
			return organ;
		}
		return null;
	}

	/**
	 * 查询组织信息
	 * 
	 * @param organization
	 * @return
	 */
	@Override
	public List<Organization> quertyOrganizationList(Organization organization) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORGANIZATION WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);

		if (organization != null) {

			if (!StrUtil.isNullOrEmpty(organization.getOrgType())) {
				sql.append(" AND ORG_TYPE = ?");
				params.add(organization.getOrgType());
			}

			if (!StrUtil.isNullOrEmpty(organization.getExistType())) {
				sql.append(" AND EXIST_TYPE= ? ");
				params.add(organization.getExistType());
			}

			if (!StrUtil.isNullOrEmpty(organization.getOrgName())) {
				sql.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(organization.getOrgCode())) {
				sql.append(" AND ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(organization.getPartyId())) {
				sql.append(" AND PARTY_ID = ?");
				params.add(organization.getPartyId());
			}

		}
		sql.append(" ORDER BY ORG_ID");

		return super.jdbcFindList(sql.toString(), params, Organization.class);

	}

	/**
	 * 根据组织名和组织类型称查找代理商组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgNameAndOrgType(String orgName,
			String orgType) {
		if (!StrUtil.isNullOrEmpty(orgName)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM ORGANIZATION T WHERE T.STATUS_CD=? AND T.ORG_NAME=? AND EXISTS(SELECT OT.ORG_ID FROM ORG_TYPE OT WHERE ORG_TYPE_CD = ? AND OT.ORG_ID =T.ORG_ID)";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(orgName));
			params.add(orgType);
			List<Organization> organList = jdbcFindList(sql, params,
					Organization.class);
			if (organList.size() > 0) {
				return organList.get(0);
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 根据组织名称和组织类型查找非代理商组织
	 * 
	 * @param organization
	 */
	public Organization queryOrganizationByOrgNameAndNotSearchOrgType(
			String orgName, String notSearchOrgType) {
		if (!StrUtil.isNullOrEmpty(orgName)
				&& !StrUtil.isNullOrEmpty(notSearchOrgType)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM ORGANIZATION T WHERE T.STATUS_CD=? AND T.ORG_NAME=? AND NOT EXISTS(SELECT OT.ORG_ID FROM ORG_TYPE OT WHERE ORG_TYPE_CD = ? AND OT.ORG_ID =T.ORG_ID)";
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(orgName));
			params.add(notSearchOrgType);
			List<Organization> organList = jdbcFindList(sql, params,
					Organization.class);
			if (organList.size() > 0) {
				return organList.get(0);
			} else {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Organization getById(Long id) {
		if (!StrUtil.isNullOrEmpty(id)) {
			String hql = " From Organization where statusCd=? and orgId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(id);
			List<Organization> list = Organization.repository()
					.findListByHQLAndParams(hql, params);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Organization getByIdStatusCd1100(Long id) {
		if (!StrUtil.isNullOrEmpty(id)) {
			String hql = " From Organization where statusCd=? and orgId=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(id);
			List<Organization> list = Organization.repository()
					.findListByHQLAndParams(hql, params);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	public Organization getAgentByOrgCode(String orgCode) {
		if (!StrUtil.isNullOrEmpty(orgCode)) {
			String sql = " select o.* from organization o left join org_type ot on ot.org_id = o.org_id where o.status_cd=? and o.org_code =? and ot.org_type_cd = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(orgCode));
			params.add(OrganizationConstant.ORG_TYPE_AGENT);
			Organization organ = jdbcFindObject(sql, params, Organization.class);
			return organ;
		}
		return null;
	}

	public Organization getIbeByOrgCode(String orgCode) {
		if (!StrUtil.isNullOrEmpty(orgCode)) {
			String sql = " select o.* from organization o left join org_type ot on ot.org_id = o.org_id where o.status_cd=? and o.org_code =? and ot.org_type_cd = ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(StringEscapeUtils.escapeSql(orgCode));
			params.add(OrganizationConstant.ORG_TYPE_N0903000000);
			Organization organ = jdbcFindObject(sql, params, Organization.class);
			return organ;
		}
		return null;
	}

	/**
	 * 分页查询失效组织信息
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo forQuertyOrganizationActivation(Organization organization,
			int currentPage, int pageSize) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORGANIZATION  WHERE STATUS_CD=?");
		/**
		 * 代理商页面添加下级节点除了代理商外还要包含营业网点uinon增加头部分
		 */
		if (organization.getIsContainSalesNetwork()) {
			sql = new StringBuffer("SELECT * FROM (").append(sql);
		}
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		if (organization != null) {
			/**
			 * 是否是只查询挂在代理商根节点的组织(包含查询代理商类型)
			 */
			if (organization.getIsChooseAgentRoot()) {
				sql.append(" AND PARTY_ID IN (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD=? AND ROLE_TYPE=?)");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationConstant.ROLE_TYPE_AGENT);
				sql.append(" AND ORG_ID IN (SELECT ORG_ID FROM ORGANIZATION_RELATION WHERE STATUS_CD=? AND RELA_ORG_ID=?)");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationConstant.ROOT_AGENT_ORG_ID);
			} else {
				/**
				 * 是否是只查询代理商
				 */
				if (organization.getIsAgent()) {
					sql.append(" AND PARTY_ID IN (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD=? AND ROLE_TYPE=?)");
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(OrganizationConstant.ROLE_TYPE_AGENT);
				}
			}
			/**
			 * 代理商页面添加下级节点除了代理商外还要包含营业网点
			 */
			if (organization.getIsContainSalesNetwork()) {
				sql.append(" UNION SELECT A.* FROM ORGANIZATION A, ORG_TYPE C WHERE A.STATUS_CD=? AND C.STATUS_CD=? AND A.ORG_ID = C.ORG_ID AND C.ORG_TYPE_CD LIKE ?) WHERE 1=1");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationConstant.SALES_NETWORK_PRE + "%");
			}
			/**
			 * 全部树页面排除代理商组织
			 */
			if (organization.getIsExcluseAgent()) {
				sql.append("AND ORG_ID NOT IN (SELECT ORG_ID FROM ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID IN (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD=? AND ROLE_TYPE=?))");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(OrganizationConstant.ROLE_TYPE_AGENT);
			}
			/**
			 * 数据权：管理区域
			 */
			if (organization.getTelcomRegionId() != null) {
				sql.append(" AND TELCOM_REGION_ID IN (SELECT TELCOM_REGION_ID FROM TELCOM_REGION WHERE STATUS_CD = ? START WITH TELCOM_REGION_ID = ? CONNECT BY PRIOR TELCOM_REGION_ID = UP_REGION_ID)");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organization.getTelcomRegionId());
			} else {
				// 未配置管理区域不显示数据
				sql.append(" AND 1=2");
			}
			if (!StrUtil.isNullOrEmpty(organization.getOrgType())) {
				sql.append(" AND ORG_TYPE=?");
				params.add(organization.getOrgType());
			}
			if (!StrUtil.isNullOrEmpty(organization.getExistType())) {
				sql.append(" AND EXIST_TYPE=?");
				params.add(organization.getExistType());
			}
			if (!StrUtil.isNullOrEmpty(organization.getOrgName())) {
				sql.append(" AND ORG_NAME like ?");
				params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName()) + "%");
			}
			if (!StrUtil.isNullOrEmpty(organization.getOrgCode())) {
				sql.append(" AND ORG_CODE=?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
			}
			/**
			 * 数据权：帐号组织
			 */
			/*
			 * 组织不控制 if (organization.getOrgId() != null) { sql.append(
			 * "AND ORG_ID IN (SELECT ORG_ID  FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH ORG_ID = ? CONNECT BY PRIOR ORG_ID=RELA_ORG_ID)"
			 * ); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			 * params.add(organization.getOrgId()); }
			 */
		}
		sql.append(" ORDER BY ORG_ID");
		return organization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, Organization.class);

	}

	/**
	 * 激活失效的组织
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public String updateOrganizationList(List<Organization> organizationList) {

		String msg = "";

		try {

			for (Organization organization : organizationList) {

				if (null != organization.getOrgId()) {

					String batchNumber = OperateLog.gennerateBatchNumber();

					@SuppressWarnings("unchecked")
					List<OrgContactInfo> orgContactInfoList = (List<OrgContactInfo>) getActivationObj(
							OrgContactInfo.class, organization.getOrgId());

					for (OrgContactInfo orgContactInfo : orgContactInfoList) {
						if (orgContactInfo.getStatusCd().equals(
								BaseUnitConstants.ENTT_STATE_INACTIVE)) {
							orgContactInfo
									.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
							orgContactInfo.setBatchNumber(batchNumber);
							orgContactInfo.update();
						}
					}

					@SuppressWarnings("unchecked")
					List<OrgType> orgTypeList = (List<OrgType>) getActivationObj(
							OrgType.class, organization.getOrgId());

					for (OrgType orgType : orgTypeList) {
						if (orgType.getStatusCd().equals(
								BaseUnitConstants.ENTT_STATE_INACTIVE)) {
							orgType.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
							orgType.setBatchNumber(batchNumber);
							orgType.update();
						}
					}

					@SuppressWarnings("unchecked")
					List<OrganizationExtendAttr> organizationExtendAttrList = (List<OrganizationExtendAttr>) getActivationObj(
							OrganizationExtendAttr.class,
							organization.getOrgId());

					for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList) {
						if (organizationExtendAttr.getStatusCd().equals(
								BaseUnitConstants.ENTT_STATE_INACTIVE)) {
							organizationExtendAttr
									.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
							organizationExtendAttr.setBatchNumber(batchNumber);
							organizationExtendAttr.update();
						}
					}

					organization
							.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					organization.setBatchNumber(batchNumber);
					organization.update();

					if (null != organization.getPartyId()) {

						@SuppressWarnings("unchecked")
						List<Party> partyList = (List<Party>) getActivationObjByPartyId(
								Party.class, organization.getPartyId());

						for (Party party : partyList) {
							if (party.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								party.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								party.setBatchNumber(batchNumber);
								party.update();
							}
						}

						@SuppressWarnings("unchecked")
						List<PartyRole> partyRoleList = (List<PartyRole>) getActivationObjByPartyId(
								PartyRole.class, organization.getPartyId());

						for (PartyRole partyRole : partyRoleList) {
							if (partyRole.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								partyRole
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								partyRole.setBatchNumber(batchNumber);
								partyRole.update();
							}
						}

						@SuppressWarnings("unchecked")
						List<PartyContactInfo> partyContactInfoList = (List<PartyContactInfo>) getActivationObjByPartyId(
								PartyContactInfo.class,
								organization.getPartyId());

						for (PartyContactInfo partyContactInfo : partyContactInfoList) {
							if (partyContactInfo.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								partyContactInfo
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								partyContactInfo.setBatchNumber(batchNumber);
								partyContactInfo.update();
							}
						}

						@SuppressWarnings("unchecked")
						List<PartyCertification> partyCertificationList = (List<PartyCertification>) getActivationObjByPartyId(
								PartyCertification.class,
								organization.getPartyId());

						for (PartyCertification partyCertification : partyCertificationList) {
							if (partyCertification.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								partyCertification
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								partyCertification.setBatchNumber(batchNumber);
								partyCertification.update();
							}
						}

						@SuppressWarnings("unchecked")
						List<PartyOrganization> partyOrganizationList = (List<PartyOrganization>) getActivationObjByPartyId(
								PartyOrganization.class,
								organization.getPartyId());

						for (PartyOrganization partyOrganization : partyOrganizationList) {
							if (partyOrganization.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								partyOrganization
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								partyOrganization.setBatchNumber(batchNumber);
								partyOrganization.update();
							}
						}

					}

				}
			}
			return msg;
		} catch (Exception e) {
			msg = e.getMessage();
			return msg;

		}
	}

	private List<?> getActivationObj(Class<?> clazz, Long orgId) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
				.append("T.orgId=").append(orgId).append(" AND T.statusCd=")
				.append(BaseUnitConstants.ENTT_STATE_INACTIVE);
		return getHibernateTemplate().find(sBhql.toString());
	}

	private List<?> getActivationObjByPartyId(Class<?> clazz, Long partyId) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
				.append("T.partyId=").append(partyId)
				.append(" AND T.statusCd=")
				.append(BaseUnitConstants.ENTT_STATE_INACTIVE);
		return getHibernateTemplate().find(sBhql.toString());
	}

	@Override
	public Long getSeqOrgFixId() {
		return Long.parseLong(this.getSeqNextval("SEQ_ORGANIZATION_FIX_ID"));
	}

	@Override
	public List<UomGridCountyLog> queryUomGridCountyLogByOrgIdList(
			UomGridCountyLog uomGridCountyLog) {

		if (uomGridCountyLog != null && uomGridCountyLog.getOrgId() != null) {

			List params = new ArrayList();
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM UOM_GRID_COUNTY_LOG WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(uomGridCountyLog.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgAttrSpecId())) {
				sb.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(uomGridCountyLog.getOrgAttrSpecId());
			}

			sb.append(" ORDER BY UOM_GRID_COUNTY_ATTR_ID ASC");

			return super.jdbcFindList(sb.toString(), params,
					UomGridCountyLog.class);
		}

		return null;

	}

	@Override
	public List<UomGridCountyLog> queryUomGridCountyLogList(
			UomGridCountyLog uomGridCountyLog) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM UOM_GRID_COUNTY_LOG WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (uomGridCountyLog != null) {

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog
					.getUomGridCountyAttrId())) {
				sb.append(" AND UOM_GRID_COUNTY_ATTR_ID = ?");
				params.add(uomGridCountyLog.getUomGridCountyAttrId());
			}

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(uomGridCountyLog.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgAttrSpecId())) {
				sb.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(uomGridCountyLog.getOrgAttrSpecId());
			}

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgAttrCd())) {
				sb.append(" AND ORG_ATTR_CD = ?");
				params.add(uomGridCountyLog.getOrgAttrCd());
			}

			if (!StrUtil.isNullOrEmpty(uomGridCountyLog.getOrgAttrValue())) {
				sb.append(" AND ORG_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(uomGridCountyLog.getOrgAttrValue()));
			}

			sb.append(" ORDER BY UOM_GRID_COUNTY_ATTR_ID ASC");

		}

		return super
				.jdbcFindList(sb.toString(), params, UomGridCountyLog.class);

	}

}
