package cn.ffcs.uom.group.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.dao.GroupUomDao;
import cn.ffcs.uom.group.model.GroupUomOrg;

@Repository("groupUomDao")
public class GroupUomDaoImpl extends BaseDaoImpl implements GroupUomDao {

	@Override
	public PageInfo queryPageInfoByGroupUomOrg(GroupUomOrg groupUomOrg,
			int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT M.ID,M.KTEXT,ORG.ORG_ID,ORG.ORG_NAME,ORG.ORG_CODE,SGP.RES_ID,SGP.RES_TYPE,SGP.DATA_TYPE,SGP.EFF_DATE,SGP.EXP_DATE");
		sb.append(" FROM MATC5 M");
		sb.append(" LEFT JOIN (SELECT * FROM SYNC_GROUP_PRV WHERE STATUS_CD = ?) SGP ON M.ID = SGP.GROUP_ID");
		sb.append(" LEFT JOIN (SELECT * FROM ORGANIZATION WHERE STATUS_CD = ?) ORG ON SGP.PRV_ID = ORG.ORG_ID");
		sb.append(" WHERE M.DATA_TYPE = ?");

		params.add("1000");
		params.add("1000");
		params.add(1);

		if (groupUomOrg != null) {

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getId())) {
				sb.append(" AND M.ID = ?");
				params.add(groupUomOrg.getId());
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getKtext())) {
				sb.append(" AND M.KTEXT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupUomOrg.getKtext()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getOrgId())) {
				sb.append(" AND ORG.ORG_ID = ?");
				params.add(groupUomOrg.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getResType())) {
				sb.append(" AND SGP.RES_TYPE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getResType()));
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getDataType())) {
				sb.append(" AND SGP.DATA_TYPE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getDataType()));
			}
		}

		sb.append(" ORDER BY M.ID ASC");

		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, GroupUomOrg.class);

	}

	@Override
	public List<GroupUomOrg> queryGroupUomOrgList(GroupUomOrg groupUomOrg) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT M.ID,M.KTEXT,ORG.ORG_ID,ORG.ORG_NAME,ORG.ORG_CODE,SGP.RES_ID,SGP.RES_TYPE,SGP.DATA_TYPE,SGP.EFF_DATE,SGP.EXP_DATE");
		sb.append(" FROM MATC5 M");
		sb.append(" LEFT JOIN (SELECT * FROM SYNC_GROUP_PRV WHERE STATUS_CD = ?) SGP ON M.ID = SGP.GROUP_ID");
		sb.append(" LEFT JOIN (SELECT * FROM ORGANIZATION WHERE STATUS_CD = ?) ORG ON SGP.PRV_ID = ORG.ORG_ID");
		sb.append(" WHERE M.DATA_TYPE = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(1);

		if (groupUomOrg != null) {

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getId())) {
				sb.append(" AND M.ID = ?");
				params.add(groupUomOrg.getId());
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getKtext())) {
				sb.append(" AND M.KTEXT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(groupUomOrg.getKtext()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getOrgId())) {
				sb.append(" AND ORG.ORG_ID = ?");
				params.add(groupUomOrg.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getResType())) {
				sb.append(" AND SGP.RES_TYPE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getResType()));
			}

			if (!StrUtil.isNullOrEmpty(groupUomOrg.getDataType())) {
				sb.append(" AND SGP.DATA_TYPE = ?");
				params.add(StringEscapeUtils.escapeSql(groupUomOrg.getDataType()));
			}
		}

		sb.append(" ORDER BY M.ID ASC");

		return super.jdbcFindList(sb.toString(), params, GroupUomOrg.class);

	}

	@Override
	public void updateGroupUomOrg(GroupUomOrg groupUomOrg) {

		// this.getJdbcTemplate().update(sql, args);

		if (groupUomOrg != null) {

			StringBuilder sb = new StringBuilder("UPDATE CT_USER SET");

			sb.append(" CREATE_DATE = SYSDATE");
			// sb.append(" ,RESERV_COL2 = '").append(
			// groupUomOrg.getReserv_Col2() + "'");
			sb.append(" WHERE 1=1");

			// if (!StrUtil.isNullOrEmpty(groupUomOrg.getUserName())) {
			// sb.append(" AND USERNAME = '").append(
			// groupUomOrg.getUserName() + "'");
			// }
			//
			// if (!StrUtil.isNullOrEmpty(groupUomOrg.getCtHrUserCode())) {
			// sb.append(" AND CTHRUSERCODE = '").append(
			// groupUomOrg.getCtHrUserCode() + "'");
			// }
			//
			// if (!StrUtil.isNullOrEmpty(groupUomOrg.getLoginName())) {
			// sb.append(" AND LOGINNAME = '").append(
			// groupUomOrg.getLoginName() + "'");
			// }
			//
			// if (!StrUtil.isNullOrEmpty(groupUomOrg.getCtIdentityNumber())) {
			// sb.append(" AND CTIDENTITYNUMBER = '").append(
			// groupUomOrg.getCtIdentityNumber() + "'");
			// }

			getJdbcTemplate().execute(sb.toString());

		}
	}

	@Override
	public void updateGroupUomOrgIsNull(GroupUomOrg groupUomOrg) {

		// this.getJdbcTemplate().update(sql, args);

		if (groupUomOrg != null) {

			StringBuilder sb = new StringBuilder("UPDATE CT_USER SET");

			sb.append(" CREATE_DATE = SYSDATE");
			sb.append(" ,RESERV_COL2 = ''");
			sb.append(" WHERE");

			// if (!StrUtil.isNullOrEmpty(groupUomOrg.getReserv_Col2())) {
			// sb.append(" RESERV_COL2 = '").append(
			// groupUomOrg.getReserv_Col2() + "'");
			// getJdbcTemplate().execute(sb.toString());
			// }

		}
	}
}
