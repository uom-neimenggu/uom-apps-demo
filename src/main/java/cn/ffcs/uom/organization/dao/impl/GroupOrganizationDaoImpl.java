package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.GroupOrganizationDao;
import cn.ffcs.uom.organization.model.GroupOrganization;

@Repository("groupOrganizationDao")
public class GroupOrganizationDaoImpl extends BaseDaoImpl implements
		GroupOrganizationDao {

	@Override
	public PageInfo queryPageInfoByGroupOrganization(
			GroupOrganization groupOrganization, int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_GROUP_ORGANIZATION WHERE 1 = 1");

		if (groupOrganization != null) {

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgCode())) {
				sb.append(" AND ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(groupOrganization.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgName())) {
				sb.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgGroup())) {
				sb.append(" AND ORG_GROUP = ?");
				params.add(StringEscapeUtils.escapeSql(groupOrganization.getOrgGroup()));
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getSupplierCode())) {
				sb.append(" AND SUPPLIER_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(groupOrganization.getSupplierCode()));
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getSupplierName())) {
				sb.append(" AND SUPPLIER_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getSupplierName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getQueryOrgTypeList())
					&& groupOrganization.getQueryOrgTypeList().size() > 0) {

				sb.append(" AND ORG_TYPE IN ( ");

				for (int i = 0; i < groupOrganization.getQueryOrgTypeList()
						.size(); i++) {

					if (i != 0) {
						sb.append(",");
					}

					sb.append("'")
							.append(groupOrganization.getQueryOrgTypeList()
									.get(i)).append("'");
				}

				sb.append(" )");

			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgDesc())) {
				sb.append(" AND ORG_DESC LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getOrgDesc()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getTDesc())) {
				sb.append(" AND T_DESC LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getTDesc()) + "%");
			}

		}

		sb.append(" ORDER BY ORG_TYPE ASC");

		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, GroupOrganization.class);

	}

	@Override
	public List<GroupOrganization> queryGroupOrganizationList(
			GroupOrganization groupOrganization) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_GROUP_ORGANIZATION WHERE 1 = 1");

		if (groupOrganization != null) {

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgCode())) {
				sb.append(" AND ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(groupOrganization.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgName())) {
				sb.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgGroup())) {
				sb.append(" AND ORG_GROUP = ?");
				params.add(StringEscapeUtils.escapeSql(groupOrganization.getOrgGroup()));
			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getQueryOrgTypeList())
					&& groupOrganization.getQueryOrgTypeList().size() > 0) {

				sb.append(" AND ORG_TYPE IN ( ");

				for (int i = 0; i < groupOrganization.getQueryOrgTypeList()
						.size(); i++) {

					if (i != 0) {
						sb.append(",");
					}

					sb.append("'")
							.append(groupOrganization.getQueryOrgTypeList()
									.get(i)).append("'");
				}

				sb.append(" )");

			}

			if (!StrUtil.isNullOrEmpty(groupOrganization.getOrgDesc())) {
				sb.append(" AND ORG_DESC LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupOrganization.getOrgDesc()) + "%");
			}

		}

		sb.append(" ORDER BY ORG_TYPE ASC");

		return super.jdbcFindList(sb.toString(), params,
				GroupOrganization.class);

	}

}
