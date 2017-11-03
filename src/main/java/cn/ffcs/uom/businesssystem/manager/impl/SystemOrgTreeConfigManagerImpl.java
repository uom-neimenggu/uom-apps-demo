package cn.ffcs.uom.businesssystem.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.dao.SystemOrgTreeConfigDao;
import cn.ffcs.uom.businesssystem.manager.SystemOrgTreeConfigManager;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.businesssystem.model.SystemOrgTreeConfig;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.staff.model.Staff;

@Service("systemOrgTreeConfigManagerImpl")
@Scope("prototype")
public class SystemOrgTreeConfigManagerImpl implements
		SystemOrgTreeConfigManager {

	@Resource
	private SystemOrgTreeConfigDao systemOrgTreeConfigDao;

	@Override
	public List<BusinessSystem> queryBusinessSystemListByTreeId(Long treeId) {
		String sql = "SELECT A.* FROM BUSINESS_SYSTEM A ,SYSTEM_ORG_TREE_CONFIG B WHERE A.STATUS_CD=? AND B.STATUS_CD=? AND A.BUSINESS_SYSTEM_ID=B.BUSINESS_SYSTEM_ID AND B.ORG_TREE_ID=? AND B.GENERATION_SWITCH = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(treeId);
		params.add(BaseUnitConstants.SWITCH_OPEN);
		return this.systemOrgTreeConfigDao.jdbcFindList(sql, params,
				BusinessSystem.class);
	}

	/**
	 * 查询业务系统组织树配置
	 */
	@Override
	public List<SystemOrgTreeConfig> querySystemOrgTreeConfigList(
			SystemOrgTreeConfig systemOrgTreeConfig) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_ORG_TREE_CONFIG WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemOrgTreeConfig.getBusinessSystemId() != null) {
			sb.append(" AND BUSINESS_SYSTEM_ID = ?");
			params.add(systemOrgTreeConfig.getBusinessSystemId());
		}

		if (systemOrgTreeConfig.getOrgTreeId() != null) {
			sb.append(" AND ORG_TREE_ID = ?");
			params.add(systemOrgTreeConfig.getOrgTreeId());
		}

		if (systemOrgTreeConfig.getGenerationSwitch() != null) {
			sb.append(" AND GENERATION_SWITCH = ?");
			params.add(StringEscapeUtils.escapeSql(systemOrgTreeConfig.getGenerationSwitch()));
		}

		return SystemOrgTreeConfig.repository().jdbcFindList(sb.toString(),
				params, SystemOrgTreeConfig.class);
	}

	/**
	 * 分页查询业务系统组织树配置
	 * 
	 * @param systemOrgTreeConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo querySystemOrgTreeConfigPageInfo(
			SystemOrgTreeConfig systemOrgTreeConfig, int currentPage,
			int pageSize) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_ORG_TREE_CONFIG WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemOrgTreeConfig != null) {

			Long businessSystemId = systemOrgTreeConfig.getBusinessSystemId();
			if (!StrUtil.isNullOrEmpty(businessSystemId)) {
				sb.append(" AND BUSINESS_SYSTEM_ID = ?");
				params.add(businessSystemId);
			}

		}

		sb.append(" ORDER BY SYSTEM_ORG_TREE_CONFIG_ID");

		return this.systemOrgTreeConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemOrgTreeConfig.class);
	}

	/**
	 * 业务系统组织树配置新增功能
	 * 
	 * @param systemOrgTreeConfig
	 */
	@Override
	public void addSystemOrgTreeConfig(SystemOrgTreeConfig systemOrgTreeConfig) {
		if (systemOrgTreeConfig != null) {
			systemOrgTreeConfig.addOnly();
		}
	}

	/**
	 * 业务系统组织树配置修改功能
	 * 
	 * @param systemOrgTreeConfig
	 */
	@Override
	public void updateSystemOrgTreeConfig(
			SystemOrgTreeConfig systemOrgTreeConfig) {
		if (systemOrgTreeConfig != null) {
			systemOrgTreeConfig.updateOnly();
		}
	}

	/**
	 * 业务系统组织树删除功能
	 * 
	 * @param systemOrgTreeConfig
	 */
	@Override
	public void removeSystemOrgTreeConfig(
			SystemOrgTreeConfig systemOrgTreeConfig) {
		if (systemOrgTreeConfig != null
				&& systemOrgTreeConfig.getSystemOrgTreeConfigId() != null) {
			systemOrgTreeConfig.removeOnly();
		}
	}

	@Override
	public List<NodeVo> getOrgTreeListbox() {

		List params = new ArrayList();
		String sql = "SELECT * FROM ORG_TREE WHERE STATUS_CD = ? AND IS_CALC = ?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.IS_CALC_ACTIVE);

		List<OrgTree> orgTreeList = OrgTree.repository().jdbcFindList(sql,
				params, OrgTree.class);
		List<NodeVo> retAttrValues = new ArrayList();
		if (orgTreeList != null) {
			for (OrgTree orgTree : orgTreeList) {
				if (orgTree != null) {
					NodeVo vo = new NodeVo();
					vo.setId(orgTree.getOrgTreeId().toString());
					vo.setName(orgTree.getOrgTreeName().trim());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@Override
	public List<NodeVo> getBusinessSystemListbox() {

		List params = new ArrayList();
		String sql = "SELECT * FROM BUSINESS_SYSTEM WHERE STATUS_CD = ?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		List<BusinessSystem> businessSystemist = BusinessSystem.repository()
				.jdbcFindList(sql, params, BusinessSystem.class);
		List<NodeVo> retAttrValues = new ArrayList();
		if (businessSystemist != null) {
			for (BusinessSystem businessSystem : businessSystemist) {
				if (businessSystem != null) {
					NodeVo vo = new NodeVo();
					vo.setId(businessSystem.getBusinessSystemId().toString());
					vo.setName(businessSystem.getSystemName().trim());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@Override
	public PageInfo queryStaffIssued(Staff staff, int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT T2.* "
						+ "FROM SYSTEM_ORG_TREE_CONFIG T2, STAFF T6 "
						+ "WHERE T6.STAFF_ID = ? AND T6.STATUS_CD=1000 AND T2.STATUS_CD=1000 "
						+ "AND T2.ORG_TREE_ID IN "
						+ "(SELECT T3.ORG_TREE_ID "
						+ "FROM TREE_ORG_TYPE_RULE T3 "
						+ "WHERE T3.STATUS_CD=1000 AND T3.REF_TYPE_VALUE IN "
						+ "(SELECT T4.ORG_TYPE_CD "
						+ "FROM ORG_TYPE T4 "
						+ "WHERE T4.STATUS_CD=1000 AND T4.ORG_ID IN "
						+ "(SELECT T5.ORG_ID "
						+ "FROM STAFF_ORGANIZATION T5 "
						+ "WHERE T5.STATUS_CD=1000 AND T5.STAFF_ID = T6.STAFF_ID))) "
						+ "AND T2.ORG_TREE_ID IN "
						+ "(SELECT T7.ORG_TREE_ID "
						+ "FROM TREE_STAFF_SFT_RULE T7 "
						+ "WHERE T7.STATUS_CD=1000 AND T7.REF_STAFF_TYPE_CD = T6.WORK_PROP) "
						+ "AND T2.ORG_TREE_ID IN "
						+ "(SELECT T8.ORG_TREE_ID "
						+ "FROM TREE_ORG_RELA_TYPE_RULE T8 "
						+ "WHERE T8.STATUS_CD=1000 AND T8.REF_ORG_RELA_CD IN "
						+ "(SELECT T9.RELA_CD "
						+ "FROM ORGANIZATION_RELATION T9 "
						+ "WHERE T9.STATUS_CD=1000 AND T9.ORG_ID IN "
						+ "(SELECT T5.ORG_ID "
						+ "FROM STAFF_ORGANIZATION T5 "
						+ "WHERE T5.STATUS_CD=1000 AND T5.STAFF_ID = T6.STAFF_ID)))");
		List params = new ArrayList();
		if (staff != null) {
			if (!StrUtil.isNullOrEmpty(staff.getStaffId())) {
				params.add(staff.getStaffId());
			}
		}
		return this.systemOrgTreeConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemOrgTreeConfig.class);
	}

	@Override
	public PageInfo queryOrganizationIssued(Organization organization,
			int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer("SELECT T2.*"
				+ " FROM SYSTEM_ORG_TREE_CONFIG T2, ORGANIZATION T6"
				+ " WHERE T6.ORG_ID = ?" + " AND T6.STATUS_CD = 1000"
				+ " AND T2.STATUS_CD = 1000" + " AND T2.ORG_TREE_ID IN"
				+ " (SELECT T3.ORG_TREE_ID" + " FROM TREE_ORG_TYPE_RULE T3"
				+ " WHERE T3.STATUS_CD = 1000" + " AND T3.REF_TYPE_VALUE IN"
				+ " (SELECT T4.ORG_TYPE_CD" + " FROM ORG_TYPE T4"
				+ " WHERE T4.STATUS_CD = 1000" + " AND T4.ORG_ID = T6.ORG_ID))"
				+ " AND T2.ORG_TREE_ID IN" + " (SELECT T8.ORG_TREE_ID"
				+ " FROM TREE_ORG_RELA_TYPE_RULE T8"
				+ " WHERE T8.STATUS_CD = 1000" + " AND T8.REF_ORG_RELA_CD IN"
				+ " (SELECT T9.RELA_CD" + " FROM ORGANIZATION_RELATION T9"
				+ " WHERE T9.STATUS_CD = 1000" + " AND T9.ORG_ID = T6.ORG_ID))");
		List params = new ArrayList();
		if (organization != null) {
			if (!StrUtil.isNullOrEmpty(organization.getOrgId())) {
				params.add(organization.getOrgId());
			}
		}
		return this.systemOrgTreeConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemOrgTreeConfig.class);
	}
}
