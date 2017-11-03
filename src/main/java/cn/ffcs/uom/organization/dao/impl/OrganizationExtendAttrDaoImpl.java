package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrganizationExtendAttrDao;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

@Repository("organizationExtendAttrDao")
public class OrganizationExtendAttrDaoImpl extends BaseDaoImpl implements
		OrganizationExtendAttrDao {

	@Override
	public OrganizationExtendAttr queryOrganizationExtendAttr(
			OrganizationExtendAttr organizationExtendAttr) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (organizationExtendAttr != null) {

			sb.append("SELECT * FROM ORGANIZATION_EXTEND_ATTR WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgExtendAttrId())) {
				sb.append(" AND ORG_EXTEND_ATTR_ID = ?");
				params.add(organizationExtendAttr.getOrgExtendAttrId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(organizationExtendAttr.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgAttrSpecId())) {
				sb.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(organizationExtendAttr.getOrgAttrSpecId());
			}

			if (!StrUtil
					.isNullOrEmpty(organizationExtendAttr.getOrgAttrValue())) {
				sb.append(" AND ORG_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(organizationExtendAttr.getOrgAttrValue()));
			}

			sb.append(" ORDER BY ORG_EXTEND_ATTR_ID ASC");

			List<OrganizationExtendAttr> organizationExtendAttrList = super
					.jdbcFindList(sb.toString(), params,
							OrganizationExtendAttr.class);

			if (organizationExtendAttrList != null
					&& organizationExtendAttrList.size() > 0) {
				return organizationExtendAttrList.get(0);
			} else {
				return null;
			}

		}

		return null;

	}

	@Override
	public OrganizationExtendAttr queryOrganizationExtendAttrStatusCd1100(
			OrganizationExtendAttr organizationExtendAttr) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (organizationExtendAttr != null) {

			sb.append("SELECT * FROM ORGANIZATION_EXTEND_ATTR WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgExtendAttrId())) {
				sb.append(" AND ORG_EXTEND_ATTR_ID = ?");
				params.add(organizationExtendAttr.getOrgExtendAttrId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(organizationExtendAttr.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgAttrSpecId())) {
				sb.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(organizationExtendAttr.getOrgAttrSpecId());
			}

			if (!StrUtil
					.isNullOrEmpty(organizationExtendAttr.getOrgAttrValue())) {
				sb.append(" AND ORG_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(organizationExtendAttr.getOrgAttrValue()));
			}

			sb.append(" ORDER BY ORG_EXTEND_ATTR_ID ASC");

			List<OrganizationExtendAttr> organizationExtendAttrList = super
					.jdbcFindList(sb.toString(), params,
							OrganizationExtendAttr.class);

			if (organizationExtendAttrList != null
					&& organizationExtendAttrList.size() > 0) {
				return organizationExtendAttrList.get(0);
			} else {
				return null;
			}

		}

		return null;

	}

	/**
	 * 跨域内外业务关系查询
	 */
	@Override
	public List<OrganizationExtendAttr> queryOrganizationExtendAttrList(
			OrganizationExtendAttr organizationExtendAttr) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (organizationExtendAttr != null) {

			sb.append("SELECT * FROM ORGANIZATION_EXTEND_ATTR WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgExtendAttrId())) {
				sb.append(" AND ORG_EXTEND_ATTR_ID = ?");
				params.add(organizationExtendAttr.getOrgExtendAttrId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(organizationExtendAttr.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationExtendAttr
					.getOrgAttrSpecId())) {
				sb.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(organizationExtendAttr.getOrgAttrSpecId());
			}

			if (!StrUtil
					.isNullOrEmpty(organizationExtendAttr.getOrgAttrValue())) {
				sb.append(" AND ORG_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(organizationExtendAttr.getOrgAttrValue()));
			}

			sb.append(" ORDER BY ORG_EXTEND_ATTR_ID ASC");

			return super.jdbcFindList(sb.toString(), params,
					OrganizationExtendAttr.class);

		}

		return null;

	}

}
