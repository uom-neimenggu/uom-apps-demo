package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRela;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.OrganizationRelation;

@Service("treeOrgRelaManager")
@Scope("prototype")
public class TreeOrgRelaManagerImpl implements TreeOrgRelaManager {

	private Logger logger = Logger.getLogger(this.getClass());

	public void batchInsert(final List<TreeOrgRela> list) {
		final Date now = DateUtil.getNowSqlDate();
		String sql = "insert into TREE_ORG_RELA values (SEQ_TREE_ORG_RELA_ID.nextVal,?,?,?,?,to_date(? ,'yyyyMMddHH24miss'),to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'))";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgRela treeOrgRela = (TreeOrgRela) list.get(i);
						preparedstatement.setLong(1, treeOrgRela.getOrgTreeId());
						preparedstatement.setLong(2, treeOrgRela.getOrgId());
						preparedstatement.setLong(3, treeOrgRela.getRelaOrgId());
						preparedstatement.setString(4, treeOrgRela.getRelaCd());
						preparedstatement.setString(5, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getEffDate()));
						preparedstatement
								.setString(6, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(7,
								BaseUnitConstants.ENTT_STATE_ACTIVE);
						preparedstatement.setString(8, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getStatusDate()));
						preparedstatement.setString(9, "");
						preparedstatement.setString(10,
								DateUtil.getYYYYMMDDHHmmss(now));
						preparedstatement.setString(11, "");
						preparedstatement.setString(12, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getUpdateDate()));
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	private void batchInsert(final List<OrganizationRelation> list,
			final Long orgTreeId, final java.util.Date thisDate) {
		String sql = "INSERT INTO TREE_ORG_RELA(TREE_ORG_RELA_ID,ORG_RELA_ID,ORG_TREE_ID,ORG_ID,RELA_ORG_ID,RELA_CD,EFF_DATE,EXP_DATE,STATUS_CD,STATUS_DATE,CREATE_DATE,UPDATE_DATE)VALUES(SEQ_TREE_ORG_RELA_ID.nextVal,?,?,?,?,?,to_date(?, 'yyyyMMddHH24miss'),TO_DATE(?, 'yyyyMMddHH24miss'), ?,TO_DATE(?, 'yyyyMMddHH24miss'),TO_DATE(?, 'yyyyMMddHH24miss'),TO_DATE(?, 'yyyyMMddHH24miss'))";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						OrganizationRelation or = list.get(i);
						ps.setLong(1, or.getOrgRelId());
						ps.setLong(2, orgTreeId);
						ps.setLong(3, or.getOrgId());
						ps.setLong(4, or.getRelaOrgId());
						ps.setString(5, or.getRelaCd());
						ps.setString(6, DateUtil.getYYYYMMDDHHmmss(thisDate));
						ps.setString(7, Constants.EXP_DATE_VALUE);
						ps.setString(8, or.getStatusCd());
						ps.setString(9, DateUtil.getYYYYMMDDHHmmss(thisDate));
						ps.setString(10, DateUtil.getYYYYMMDDHHmmss(thisDate));
						ps.setString(11, DateUtil.getYYYYMMDDHHmmss(thisDate));
					}

					@Override
					public int getBatchSize() {
						return list.size();
					}
				});
	}

	public void batchUpdate(final List<TreeOrgRela> list) {

		String baksql = "insert into TREE_ORG_RELA_HIS select SEQ_TREE_ORG_RELA_HIS_ID.nextVal,TREE_ORG_RELA_ID,ORG_TREE_ID,ORG_ID,RELA_ORG_ID,RELA_CD,EFF_DATE,to_date(? ,'yyyyMMddHH24miss'),STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,ORG_RELA_ID from TREE_ORG_RELA where TREE_ORG_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(baksql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgRela treeOrgRela = (TreeOrgRela) list.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getEffDate()));
						preparedstatement.setLong(2,
								treeOrgRela.getTreeOrgRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});

		String sql = "update TREE_ORG_RELA set EFF_DATE = to_date(? ,'yyyyMMddHH24miss'),EXP_DATE = to_date(? ,'yyyyMMddHH24miss'),STATUS_DATE = to_date(? ,'yyyyMMddHH24miss'),UPDATE_DATE = to_date(? ,'yyyyMMddHH24miss') where TREE_ORG_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgRela treeOrgRela = (TreeOrgRela) list.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getEffDate()));
						preparedstatement
								.setString(2, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(3, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getStatusDate()));
						preparedstatement.setString(4, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getUpdateDate()));
						preparedstatement.setLong(5,
								treeOrgRela.getTreeOrgRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	public void batchRemove(final List<TreeOrgRela> list) {
		String baksql = "insert into TREE_ORG_RELA_HIS select SEQ_TREE_ORG_RELA_HIS_ID.nextVal,TREE_ORG_RELA_ID,ORG_TREE_ID,ORG_ID,RELA_ORG_ID,RELA_CD,EFF_DATE,to_date(? ,'yyyyMMddHH24miss'),STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,ORG_RELA_ID from TREE_ORG_RELA where TREE_ORG_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(baksql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgRela treeOrgRela = (TreeOrgRela) list.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getEffDate()));
						preparedstatement.setLong(2,
								treeOrgRela.getTreeOrgRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});

		String sql = "update TREE_ORG_RELA set EFF_DATE = to_date(? ,'yyyyMMddHH24miss'),EXP_DATE = to_date(? ,'yyyyMMddHH24miss'),STATUS_CD = ?,STATUS_DATE = to_date(? ,'yyyyMMddHH24miss'),UPDATE_DATE = to_date(? ,'yyyyMMddHH24miss') where TREE_ORG_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgRela treeOrgRela = (TreeOrgRela) list.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getEffDate()));
						preparedstatement
								.setString(2, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(3,
								BaseUnitConstants.ENTT_STATE_INACTIVE);
						preparedstatement.setString(4, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getStatusDate()));
						preparedstatement.setString(5, DateUtil
								.getYYYYMMDDHHmmss(treeOrgRela.getUpdateDate()));
						preparedstatement.setLong(6,
								treeOrgRela.getTreeOrgRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	@Override
	public List<OrganizationRelation> getThisDateTreeOrgRelaList(
			Long orgTreeId, java.util.Date thisDate) {
		logger.info("1.1->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		String dateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		String sql = "SELECT * FROM (SELECT DISTINCT TOR.* FROM V_ORGANIZATION_RELATION TOR, TREE_ORG_RELA_TYPE_RULE TORR, V_ORG_TYPE TOT, TREE_ORG_TYPE_RULE TOTR WHERE TORR.STATUS_CD = '1000' AND TOTR.STATUS_CD = '1000' AND TOR.STATUS_CD = '1000' AND TOT.STATUS_CD = '1000' AND TOR.RELA_CD = TORR.REF_ORG_RELA_CD AND TOR.ORG_ID = TOT.ORG_ID AND TOR.ORG_ID = TOT.ORG_ID AND TOTR.REF_TYPE_VALUE = TOT.ORG_TYPE_CD AND TORR.ORG_TREE_ID = "
				+ +orgTreeId
				+ " AND TOTR.ORG_TREE_ID = "
				+ orgTreeId
				+ " AND TOR.EFF_DATE <=to_date('"
				+ dateStr
				+ "','yyyyMMddhh24miss') AND TOR.EXP_DATE > to_date('"
				+ dateStr
				+ "','yyyyMMddhh24miss')  AND TOT.EFF_DATE <= to_date('"
				+ dateStr
				+ "','yyyyMMddhh24miss')  AND TOT.EXP_DATE > to_date('"
				+ dateStr
				+ "','yyyyMMddhh24miss') ) WHERE RELA_ORG_ID != "
				+ 0
				+ " START WITH RELA_ORG_ID = "
				+ 0
				+ " CONNECT BY PRIOR ORG_ID = RELA_ORG_ID";

		List params = new ArrayList();
		return OrganizationRelation.repository().jdbcFindList(sql, params,
				OrganizationRelation.class);
	}

	@Override
	public Map<String, List<TreeOrgRela>> getNeedUpdateTreeOrgRelaDataMap(
			List<OrganizationRelation> list, Long orgTreeId,
			java.util.Date thisDate) {
		logger.info("1.2->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		String sql = "SELECT * FROM TREE_ORG_RELA WHERE STATUS_CD = ? AND ORG_TREE_ID = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgTreeId);
		// 中间表生效数据
		List<TreeOrgRela> treeOrgRelalist = DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, TreeOrgRela.class);
		List<TreeOrgRela> removeList = new ArrayList<TreeOrgRela>();
		List<TreeOrgRela> addList = new ArrayList<TreeOrgRela>();
		List<TreeOrgRela> updateList = new ArrayList<TreeOrgRela>();
		logger.info("orsize->" + list.size());
		logger.info("torsize->" + treeOrgRelalist.size());
		if (list != null && list.size() > 0) {
			for (OrganizationRelation or : list) {
				boolean isExist = false;
				for (int j = 0; j < treeOrgRelalist.size(); j++) {
					TreeOrgRela tor = treeOrgRelalist.get(j);
					if (or.getOrgId().equals(tor.getOrgId())
							&& or.getRelaOrgId().equals(tor.getRelaOrgId())
							&& or.getRelaCd().equals(tor.getRelaCd())) {
						isExist = true;
						// 存在中间表移出数据，最后剩下的就是失效的数据
						treeOrgRelalist.remove(j);
						// 关系虽然不变，但是存在更新的情况
						if (or.getEffDate().compareTo(tor.getEffDate()) > 0) {
							tor.setEffDate(thisDate);
							tor.setStatusDate(thisDate);
							tor.setUpdateDate(thisDate);
							updateList.add(tor);
						}
						break;
					}
				}
				// 不存在中间表的，生成新增数据
				if (!isExist) {
					TreeOrgRela tor = new TreeOrgRela();
					tor.setOrgTreeId(orgTreeId);
					tor.setOrgId(or.getOrgId());
					tor.setRelaOrgId(or.getRelaOrgId());
					tor.setRelaCd(or.getRelaCd());
					tor.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					tor.setEffDate(thisDate);
					tor.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
					tor.setStatusDate(thisDate);
					tor.setUpdateDate(thisDate);
					Long staffId = PlatformUtil.getCurrentUserId();
					if (staffId != null) {
						tor.setCreateStaff(staffId);
					}
					addList.add(tor);
				}
			}
		}
		// 剩余未移出的数据就是要失效的数据
		if (treeOrgRelalist != null && treeOrgRelalist.size() > 0) {
			for (TreeOrgRela tor : treeOrgRelalist) {
				tor.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
				tor.setEffDate(thisDate);
				tor.setStatusDate(thisDate);
				tor.setUpdateDate(thisDate);
				tor.setExpDate(thisDate);
				Long staffId = PlatformUtil.getCurrentUserId();
				if (staffId != null) {
					tor.setUpdateStaff(staffId);
				}
				removeList.add(tor);
			}
		}
		Map map = new HashMap<String, List<TreeOrgRela>>();
		map.put("addList", addList);
		map.put("removeList", removeList);
		map.put("updateList", updateList);

		return map;
	}

	@Override
	public void updateTreeOrgRelaData(Long orgTreeId, java.util.Date thisDate) {
		Map<String, List<TreeOrgRela>> map = this
				.getNeedUpdateTreeOrgRelaDataMap(
						this.getThisDateTreeOrgRelaList(orgTreeId, thisDate),
						orgTreeId, thisDate);
		logger.info("1.3->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		if (map != null) {
			List<TreeOrgRela> removeList = map.get("removeList");
			if (removeList != null && removeList.size() > 0) {
				this.batchRemove(removeList);
			}
			logger.info("1.4->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
			List<TreeOrgRela> addList = map.get("addList");
			if (addList != null && addList.size() > 0) {
				this.batchInsert(addList);
			}
			logger.info("1.5->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
			List<TreeOrgRela> updateList = map.get("updateList");
			if (updateList != null && updateList.size() > 0) {
				this.batchUpdate(updateList);
			}
			logger.info("1.6->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
		}
	}

	@Override
	public void updateTreeOrgRelaData(Long orgTreeId, java.util.Date preDate,
			java.util.Date thisDate) {
		List<OrganizationRelation> addList = this.getAddOrgRelaData(preDate,
				thisDate, orgTreeId);
		logger.info("1.1->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		List<TreeOrgRela> updateList = this.getUpdateOrgRelaData(preDate,
				thisDate, orgTreeId);
		logger.info("1.2->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		List<TreeOrgRela> delList = this.getDelOrgRelaData(preDate, thisDate,
				orgTreeId);
		logger.info("1.3->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		if (updateList != null && updateList.size() > 0) {
			for (TreeOrgRela tor : updateList) {
				tor.setEffDate(thisDate);
				tor.setStatusDate(thisDate);
				tor.setUpdateDate(thisDate);
			}
		}
		if (delList != null && delList.size() > 0) {
			for (TreeOrgRela tor : delList) {
				tor.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
				tor.setEffDate(thisDate);
				tor.setStatusDate(thisDate);
				tor.setUpdateDate(thisDate);
			}
		}
		if (addList != null && addList.size() > 0) {
			logger.info("addList size->" + addList.size());
			this.batchInsert(addList, orgTreeId, thisDate);

		}
		if (updateList != null && updateList.size() > 0) {
			logger.info("updateList size->" + updateList.size());
			this.batchUpdate(updateList);
		}
		if (delList != null && delList.size() > 0) {
			logger.info("delList size->" + delList.size());
			this.batchRemove(delList);
		}
	}

	private List<OrganizationRelation> getAddOrgRelaData(
			java.util.Date preDate, java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		String preDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				preDate);

		StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT * FROM (");
		sql.append("SELECT DISTINCT TOR.* FROM V_ORGANIZATION_RELATION TOR,TREE_ORG_RELA_TYPE_RULE TORR,V_ORG_TYPE TOT,TREE_ORG_TYPE_RULE TOTR");
		sql.append(" WHERE TORR.STATUS_CD = '1000' AND TOTR.STATUS_CD = '1000'  AND TOT.STATUS_CD = '1000'");
		sql.append(" AND TOR.RELA_CD = TORR.REF_ORG_RELA_CD");
		sql.append(" AND TOR.ORG_ID = TOT.ORG_ID");
		sql.append(" AND TOR.ORG_ID = TOT.ORG_ID");
		sql.append(" AND TOTR.REF_TYPE_VALUE = TOT.ORG_TYPE_CD");
		sql.append(" AND TORR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOTR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOR.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TOR.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TOT.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TOT.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(")WHERE RELA_ORG_ID != ").append(
				OrganizationConstant.ROOT_ORG_ID);
		sql.append(" START WITH RELA_ORG_ID = ")
				.append(OrganizationConstant.ROOT_ORG_ID)
				.append(" CONNECT BY PRIOR ORG_ID = RELA_ORG_ID) T1");
		sql.append(" WHERE NOT EXISTS (");
		sql.append("SELECT 1 FROM TREE_ORG_RELA O");
		sql.append(" WHERE O.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND STATUS_CD = '1000'");
		sql.append(" AND T1.ORG_ID = O.ORG_ID");
		sql.append(" AND T1.RELA_ORG_ID = O.RELA_ORG_ID");
		sql.append(" AND T1.RELA_CD = O.RELA_CD)");
		sql.append(" AND T1.EFF_DATE > ").append("to_date('")
				.append(preDateStr).append("','yyyyMMddhh24miss')");
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		// params.add(orgTreeId);
		// params.add(preDate);
		return OrganizationRelation.repository().jdbcFindList(sql.toString(),
				params, OrganizationRelation.class);
	}

	private List<TreeOrgRela> getUpdateOrgRelaData(java.util.Date preDate,
			java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		String preDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				preDate);

		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_ORG_RELA O");
		sql.append(" WHERE O.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND O.STATUS_CD = '1000'");
		sql.append(" AND EXISTS(");
		sql.append("SELECT 1 FROM(SELECT *FROM (");
		sql.append("SELECT DISTINCT TOR.* FROM V_ORGANIZATION_RELATION TOR, TREE_ORG_RELA_TYPE_RULE TORR, V_ORG_TYPE TOT, TREE_ORG_TYPE_RULE TOTR");
		sql.append(" WHERE TORR.STATUS_CD = '1000' AND TOTR.STATUS_CD = '1000' AND TOR.STATUS_CD = '1000' AND TOT.STATUS_CD = '1000'");
		sql.append(" AND TOR.RELA_CD = TORR.REF_ORG_RELA_CD AND TOR.ORG_ID = TOT.ORG_ID AND TOR.ORG_ID = TOT.ORG_ID AND TOTR.REF_TYPE_VALUE = TOT.ORG_TYPE_CD");
		sql.append(" AND TORR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOTR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOR.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOR.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOT.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOT.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(")");
		sql.append(" WHERE RELA_ORG_ID != ").append(
				OrganizationConstant.ROOT_ORG_ID);
		sql.append(" START WITH RELA_ORG_ID = ").append(
				OrganizationConstant.ROOT_ORG_ID);
		sql.append(" CONNECT BY PRIOR ORG_ID = RELA_ORG_ID) T1");
		sql.append(" WHERE T1.EFF_DATE > ").append("to_date('")
				.append(preDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND T1.ORG_ID = O.ORG_ID");
		sql.append(" AND T1.RELA_ORG_ID = O.RELA_ORG_ID");
		sql.append(" AND T1.RELA_CD = O.RELA_CD)");
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		// params.add(preDate);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),
				params, TreeOrgRela.class);
	}

	private List<TreeOrgRela> getDelOrgRelaData(java.util.Date preDate,
			java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		// String preDateStr=DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
		// preDate);

		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_ORG_RELA O");
		sql.append(" WHERE O.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND O.STATUS_CD = '1000'");
		sql.append(" AND NOT EXISTS(");
		sql.append("SELECT 1 FROM(SELECT *FROM (");
		sql.append("SELECT DISTINCT TOR.* FROM V_ORGANIZATION_RELATION TOR, TREE_ORG_RELA_TYPE_RULE TORR, V_ORG_TYPE TOT, TREE_ORG_TYPE_RULE TOTR");
		sql.append(" WHERE TORR.STATUS_CD = '1000' AND TOTR.STATUS_CD = '1000' AND TOR.STATUS_CD = '1000' AND TOT.STATUS_CD = '1000'");
		sql.append(" AND TOR.RELA_CD = TORR.REF_ORG_RELA_CD AND TOR.ORG_ID = TOT.ORG_ID AND TOR.ORG_ID = TOT.ORG_ID AND TOTR.REF_TYPE_VALUE = TOT.ORG_TYPE_CD");
		sql.append(" AND TORR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOTR.ORG_TREE_ID = ").append(orgTreeId);
		sql.append(" AND TOR.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOR.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOT.EFF_DATE <= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TOT.EXP_DATE > ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(")");
		sql.append(" WHERE RELA_ORG_ID != ").append(
				OrganizationConstant.ROOT_ORG_ID);
		sql.append(" START WITH RELA_ORG_ID = ").append(
				OrganizationConstant.ROOT_ORG_ID);
		sql.append(" CONNECT BY PRIOR ORG_ID = RELA_ORG_ID) T1");
		sql.append(" WHERE T1.ORG_ID = O.ORG_ID");
		sql.append(" AND T1.RELA_ORG_ID = O.RELA_ORG_ID");
		sql.append(" AND T1.RELA_CD = O.RELA_CD)");
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		// params.add(OrganizationConstant.ROOT_ORG_ID);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),
				params, TreeOrgRela.class);
	}
}
