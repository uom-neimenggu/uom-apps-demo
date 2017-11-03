package cn.ffcs.uom.group.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.dao.GroupDao;
import cn.ffcs.uom.group.model.Group;

@Repository("groupDao")
public class GroupDaoImpl extends BaseDaoImpl implements GroupDao {

	@Override
	public PageInfo queryPageInfoByGroup(Group group, int currentPage,
			int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM CT_USER WHERE DEL_STATUS = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (group != null) {

			if (!StrUtil.isNullOrEmpty(group.getUserName())) {
				sb.append(" AND USERNAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(group.getUserName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(group.getCtHrUserCode())) {
				sb.append(" AND CTHRUSERCODE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(group.getCtHrUserCode()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(group.getLoginName())) {
				sb.append(" AND LOGINNAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(group.getLoginName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(group.getCtIdentityNumber())) {
				sb.append(" AND CTIDENTITYNUMBER LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(group.getCtIdentityNumber()) + "%");
			}

			// if (!StrUtil.isNullOrEmpty(group.getCtStatus())) {
			// if (group.getCtStatus().equals("3")) {
			// sb.append(" AND CTSTATUS IN ?");
			// params.add("(3," + "" + "," + null + ")");
			// } else {
			// sb.append(" AND CTSTATUS LIKE ?");
			// params.add(group.getCtStatus() + "%");
			// }
			// }

			sb.append(" ORDER BY ID ASC");

		}
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, Group.class);

	}

	@Override
	public List<Group> queryGroupList(Group group) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM CT_USER WHERE DEL_STATUS = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (group != null) {

			if (!StrUtil.isNullOrEmpty(group.getUserName())) {
				sb.append(" AND USERNAME = ?");
				params.add(StringEscapeUtils.escapeSql(group.getUserName()));
			}

			if (!StrUtil.isNullOrEmpty(group.getCtHrUserCode())) {
				sb.append(" AND CTHRUSERCODE = ?");
				params.add(StringEscapeUtils.escapeSql(group.getCtHrUserCode()));
			}

			if (!StrUtil.isNullOrEmpty(group.getLoginName())) {
				sb.append(" AND LOGINNAME = ?");
				params.add(StringEscapeUtils.escapeSql(group.getLoginName()));
			}

			if (!StrUtil.isNullOrEmpty(group.getCtIdentityNumber())) {
				sb.append(" AND CTIDENTITYNUMBER = ?");
				params.add(StringEscapeUtils.escapeSql(group.getCtIdentityNumber()));
			}

			if (!StrUtil.isNullOrEmpty(group.getReserv_Col2())) {
				sb.append(" AND RESERV_COL2 = ?");
				params.add(StringEscapeUtils.escapeSql(group.getReserv_Col2()));
			}

			// if (!StrUtil.isNullOrEmpty(group.getCtStatus())) {
			// if (group.getCtStatus().equals("3")) {
			// sb.append(" AND CTSTATUS IN ?");
			// params.add("(3," + "" + "," + null + ")");
			// } else {
			// sb.append(" AND CTSTATUS LIKE ?");
			// params.add(group.getCtStatus() + "%");
			// }
			// }

			sb.append(" ORDER BY ID ASC");

		}
		return super.jdbcFindList(sb.toString(), params, Group.class);

	}

	@Override
	public void updateGroup(Group group) {

		// this.getJdbcTemplate().update(sql, args);

		if (group != null) {

			StringBuilder sb = new StringBuilder("UPDATE CT_USER SET");

			sb.append(" CREATE_DATE = SYSDATE");
			sb.append(" ,RESERV_COL2 = '").append(StringEscapeUtils.escapeSql(group.getReserv_Col2()) + "'");
			sb.append(" WHERE 1=1");

			if (!StrUtil.isNullOrEmpty(group.getUserName())) {
				sb.append(" AND USERNAME = '")
						.append(StringEscapeUtils.escapeSql(group.getUserName()) + "'");
			}

			if (!StrUtil.isNullOrEmpty(group.getCtHrUserCode())) {
				sb.append(" AND CTHRUSERCODE = '").append(
						StringEscapeUtils.escapeSql(group.getCtHrUserCode()) + "'");
			}

			if (!StrUtil.isNullOrEmpty(group.getLoginName())) {
				sb.append(" AND LOGINNAME = '").append(
						StringEscapeUtils.escapeSql(group.getLoginName()) + "'");
			}

			if (!StrUtil.isNullOrEmpty(group.getCtIdentityNumber())) {
				sb.append(" AND CTIDENTITYNUMBER = '").append(
						StringEscapeUtils.escapeSql(group.getCtIdentityNumber()) + "'");
			}

			getJdbcTemplate().execute(sb.toString());

		}
	}

	@Override
	public void updateGroupIsNull(Group group) {

		// this.getJdbcTemplate().update(sql, args);

		if (group != null) {

			StringBuilder sb = new StringBuilder("UPDATE CT_USER SET");

			sb.append(" CREATE_DATE = SYSDATE");
			sb.append(" ,RESERV_COL2 = ''");
			sb.append(" WHERE");

			if (!StrUtil.isNullOrEmpty(group.getReserv_Col2())) {
				sb.append(" RESERV_COL2 = '").append(
						StringEscapeUtils.escapeSql(group.getReserv_Col2()) + "'");
				getJdbcTemplate().execute(sb.toString());
			}

		}
	}
}
