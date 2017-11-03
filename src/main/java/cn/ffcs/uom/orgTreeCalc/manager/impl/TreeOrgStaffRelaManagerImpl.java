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
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgStaffRelaManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgStaffRela;
import cn.ffcs.uom.organization.model.StaffOrganization;

@Service("treeOrgStaffRelaManager")
@Scope("prototype")
public class TreeOrgStaffRelaManagerImpl implements TreeOrgStaffRelaManager {

	private Logger logger = Logger.getLogger(this.getClass());

	public void batchInsert(final List<TreeOrgStaffRela> list) {
		final Date now = DateUtil.getNowSqlDate();
		String sql = "insert into TREE_ORG_STAFF_RELA (TREE_ORG_STAFF_RELA_ID, ORG_TREE_ID, ORG_ID, STAFF_ID, ORI_ORG_ID, RELA_CD, EFF_DATE, EXP_DATE, STATUS_CD, STATUS_DATE, CREATE_STAFF, CREATE_DATE, UPDATE_STAFF, UPDATE_DATE, STAFF_SEQ, USERCODE, NOTE) values (SEQ_TREE_ORG_STAFF_RELA_ID.nextVal,?,?,?,?,?,to_date(? ,'yyyyMMddHH24miss'),to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,?,?)";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgStaffRela treeOrgStaffRela = (TreeOrgStaffRela) list
								.get(i);
						preparedstatement.setLong(1,
								treeOrgStaffRela.getOrgTreeId());
						preparedstatement.setLong(2,
								treeOrgStaffRela.getOrgId());
						preparedstatement.setLong(3,
								treeOrgStaffRela.getStaffId());
						preparedstatement.setLong(4,
								treeOrgStaffRela.getOriOrgId());
						preparedstatement.setString(5,
								treeOrgStaffRela.getRelaCd());
						preparedstatement.setString(6, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getEffDate()));
						preparedstatement
								.setString(7, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(8,
								BaseUnitConstants.ENTT_STATE_ACTIVE);
						preparedstatement.setString(9, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getStatusDate()));
						preparedstatement.setString(10, "");
						preparedstatement.setString(11,
								DateUtil.getYYYYMMDDHHmmss(now));
						preparedstatement.setString(12, "");
						preparedstatement.setString(13, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getUpdateDate()));
						preparedstatement.setLong(14,
								treeOrgStaffRela.getStaffSeq());
						preparedstatement.setString(15,
								treeOrgStaffRela.getUserCode());
						preparedstatement.setString(16,
								treeOrgStaffRela.getNote());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	private void batchInsert(final List<StaffOrganization> list,
			final Long orgTreeId, final java.util.Date thisDate) {
		String sql = "insert into TREE_ORG_STAFF_RELA (TREE_ORG_STAFF_RELA_ID,STAFF_ORG_RELA_ID , ORG_TREE_ID, ORG_ID, STAFF_ID, ORI_ORG_ID, RELA_CD, EFF_DATE, EXP_DATE, STATUS_CD, STATUS_DATE, CREATE_STAFF, CREATE_DATE, UPDATE_STAFF, UPDATE_DATE, STAFF_SEQ, USERCODE, NOTE) values (SEQ_TREE_ORG_STAFF_RELA_ID.nextVal,?,?,?,?,?,?,to_date(? ,'yyyyMMddHH24miss'),to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,to_date(? ,'yyyyMMddHH24miss'),?,?,?)";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						StaffOrganization staffOrganization = (StaffOrganization) list
								.get(i);
						preparedstatement.setLong(1,
								staffOrganization.getStaffOrgId());
						preparedstatement.setLong(2, orgTreeId);
						preparedstatement.setLong(3,
								staffOrganization.getOrgId());
						preparedstatement.setLong(4,
								staffOrganization.getStaffId());
						preparedstatement.setLong(5,
								staffOrganization.getOrgId());
						preparedstatement.setString(6,
								staffOrganization.getRalaCd());
						preparedstatement.setString(7,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement
								.setString(8, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(9,
								BaseUnitConstants.ENTT_STATE_ACTIVE);
						preparedstatement.setString(10,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setString(11, "");
						preparedstatement.setString(12,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setString(13, "");
						preparedstatement.setString(14,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setLong(15,
								staffOrganization.getStaffSeq());
						preparedstatement.setString(16,
								staffOrganization.getUserCode());
						preparedstatement.setString(17,
								staffOrganization.getNote());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	private void batchUpdate(final List<StaffOrganization> list,
			final Long orgTreeId, final java.util.Date thisDate) {
		String baksql = "INSERT INTO TREE_ORG_STAFF_RELA_HIS(HIS_ID,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,EXP_DATE,STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE,STAFF_ORG_RELA_ID ) SELECT SEQ_TREE_ORG_STAFF_RELA_HIS_ID.NEXTVAL,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,TO_DATE(?, 'yyyyMMddHH24miss'),STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE,staff_org_rela_id FROM TREE_ORG_STAFF_RELA WHERE STATUS_CD= '1000' AND ORG_TREE_ID= ? AND ORG_ID = ? AND STAFF_ID = ? AND RELA_CD = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(baksql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						StaffOrganization staffOrganization = (StaffOrganization) list
								.get(i);
						preparedstatement.setString(1,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setLong(2, orgTreeId);
						preparedstatement.setLong(3,
								staffOrganization.getOrgId());
						preparedstatement.setLong(4,
								staffOrganization.getStaffId());
						preparedstatement.setString(5,
								staffOrganization.getRalaCd());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
		String sql = "update TREE_ORG_STAFF_RELA set EFF_DATE = to_date(? ,'yyyyMMddHH24miss'),EXP_DATE = to_date(? ,'yyyyMMddHH24miss'),STATUS_DATE = to_date(? ,'yyyyMMddHH24miss'),UPDATE_DATE = to_date(? ,'yyyyMMddHH24miss'),STAFF_SEQ=?,USERCODE=?,NOTE=? WHERE STATUS_CD= '1000' AND ORG_TREE_ID= ? AND ORG_ID = ? AND STAFF_ID = ? AND RELA_CD = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						StaffOrganization staffOrganization = (StaffOrganization) list
								.get(i);
						preparedstatement.setString(1,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement
								.setString(2, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(3,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setString(4,
								DateUtil.getYYYYMMDDHHmmss(thisDate));
						preparedstatement.setLong(5,
								staffOrganization.getStaffSeq());
						preparedstatement.setString(6,
								staffOrganization.getUserCode());
						preparedstatement.setString(7,
								staffOrganization.getNote());
						preparedstatement.setLong(8, orgTreeId);
						preparedstatement.setLong(9,
								staffOrganization.getOrgId());
						preparedstatement.setLong(10,
								staffOrganization.getStaffId());
						preparedstatement.setString(11,
								staffOrganization.getRalaCd());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	public void batchUpdate(final List<TreeOrgStaffRela> list) {
		String baksql = "INSERT INTO TREE_ORG_STAFF_RELA_HIS(HIS_ID,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,EXP_DATE,STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE) SELECT SEQ_TREE_ORG_STAFF_RELA_HIS_ID.NEXTVAL,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,TO_DATE(?, 'yyyyMMddHH24miss'),STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE FROM TREE_ORG_STAFF_RELA WHERE TREE_ORG_STAFF_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(baksql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgStaffRela treeOrgStaffRela = (TreeOrgStaffRela) list
								.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getEffDate()));
						preparedstatement.setLong(2,
								treeOrgStaffRela.getTreeOrgStaffRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
		String sql = "update TREE_ORG_STAFF_RELA set EFF_DATE = to_date(? ,'yyyyMMddHH24miss'),EXP_DATE = to_date(? ,'yyyyMMddHH24miss'),STATUS_DATE = to_date(? ,'yyyyMMddHH24miss'),UPDATE_DATE = to_date(? ,'yyyyMMddHH24miss'),STAFF_SEQ=?,USERCODE=?,NOTE=? WHERE TREE_ORG_STAFF_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgStaffRela treeOrgStaffRela = (TreeOrgStaffRela) list
								.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getEffDate()));
						preparedstatement
								.setString(2, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(3, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getStatusDate()));
						preparedstatement.setString(4, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getUpdateDate()));
						preparedstatement.setLong(5,
								treeOrgStaffRela.getStaffSeq());
						preparedstatement.setString(6,
								treeOrgStaffRela.getUserCode());
						preparedstatement.setString(7,
								treeOrgStaffRela.getNote());
						preparedstatement.setLong(8,
								treeOrgStaffRela.getTreeOrgStaffRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	public void batchRemove(final List<TreeOrgStaffRela> list) {
		String baksql = "INSERT INTO TREE_ORG_STAFF_RELA_HIS(HIS_ID,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,EXP_DATE,STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE,STAFF_ORG_RELA_ID) SELECT SEQ_TREE_ORG_STAFF_RELA_HIS_ID.NEXTVAL,TREE_ORG_STAFF_RELA_ID,ORG_TREE_ID,ORG_ID,STAFF_ID,ORI_ORG_ID,RELA_CD,EFF_DATE,TO_DATE(?, 'yyyyMMddHH24miss'),STATUS_CD,STATUS_DATE,CREATE_STAFF,CREATE_DATE,UPDATE_STAFF,UPDATE_DATE,STAFF_SEQ,USERCODE,NOTE,staff_org_rela_id FROM TREE_ORG_STAFF_RELA WHERE TREE_ORG_STAFF_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(baksql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgStaffRela treeOrgStaffRela = (TreeOrgStaffRela) list
								.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getExpDate()));
						preparedstatement.setLong(2,
								treeOrgStaffRela.getTreeOrgStaffRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
		String sql = "update TREE_ORG_STAFF_RELA set EFF_DATE = to_date(? ,'yyyyMMddHH24miss'),EXP_DATE = to_date(? ,'yyyyMMddHH24miss'),STATUS_CD = ?,STATUS_DATE = to_date(? ,'yyyyMMddHH24miss'),UPDATE_DATE = to_date(? ,'yyyyMMddHH24miss') where TREE_ORG_STAFF_RELA_ID = ?";
		DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement preparedstatement,
							int i) throws SQLException {
						TreeOrgStaffRela treeOrgStaffRela = (TreeOrgStaffRela) list
								.get(i);
						preparedstatement.setString(1, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getEffDate()));
						preparedstatement
								.setString(2, Constants.EXP_DATE_VALUE);
						preparedstatement.setString(3,
								BaseUnitConstants.ENTT_STATE_INACTIVE);
						preparedstatement.setString(4, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getStatusDate()));
						preparedstatement.setString(5, DateUtil
								.getYYYYMMDDHHmmss(treeOrgStaffRela
										.getUpdateDate()));
						preparedstatement.setLong(6,
								treeOrgStaffRela.getTreeOrgStaffRelaId());
					}

					public int getBatchSize() {
						return list.size();
					}
				});
	}

	@Override
	public Map<String, List<TreeOrgStaffRela>> getNeedUpdateTreeOrgStaffRelaDataMap(
			List<StaffOrganization> list, Long orgTreeId,
			java.util.Date thisDate) {
		logger.info("2.2->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		String sql = "SELECT * FROM TREE_ORG_STAFF_RELA A WHERE A.STATUS_CD = ? AND A.ORG_TREE_ID = ?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgTreeId);
		// 中间表生效数据
		List<TreeOrgStaffRela> treeOrgStaffRelalist = DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql, params,
						TreeOrgStaffRela.class);
		List<TreeOrgStaffRela> removeList = new ArrayList<TreeOrgStaffRela>();
		List<TreeOrgStaffRela> addList = new ArrayList<TreeOrgStaffRela>();
		List<TreeOrgStaffRela> updateList = new ArrayList<TreeOrgStaffRela>();
		logger.info("sfsize->" + list.size());
		logger.info("tsfrsize->" + treeOrgStaffRelalist.size());
		if (list != null && list.size() > 0) {
			for (StaffOrganization so : list) {
				boolean isExist = false;
				for (int j = 0; j < treeOrgStaffRelalist.size(); j++) {
					TreeOrgStaffRela tosr = treeOrgStaffRelalist.get(j);
					if (so.getStaffId().equals(tosr.getStaffId())
							&& so.getOrgId().equals(tosr.getOrgId())
							&& so.getRalaCd().equals(tosr.getRelaCd())) {
						isExist = true;
						// 存在中间表移出数据，最后剩下的就是失效的数据
						treeOrgStaffRelalist.remove(j);
						// 关系虽然不变，但是存在更新的情况
						if (so.getEffDate().compareTo(tosr.getEffDate()) > 0) {
							tosr.setEffDate(thisDate);
							tosr.setStatusDate(thisDate);
							tosr.setUpdateDate(thisDate);
							tosr.setStaffSeq(so.getStaffSeq());
							tosr.setUserCode(so.getUserCode());
							tosr.setNote(so.getNote());
							updateList.add(tosr);
						}
						break;
					}
				}
				// 不存在中间表的，生成新增数据
				if (!isExist) {
					TreeOrgStaffRela tosr = new TreeOrgStaffRela();
					tosr.setOrgTreeId(orgTreeId);
					tosr.setOrgId(so.getOrgId());
					tosr.setStaffId(so.getStaffId());
					tosr.setRelaCd(so.getRalaCd());
					tosr.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					tosr.setEffDate(thisDate);
					tosr.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
					tosr.setStatusDate(thisDate);
					tosr.setUpdateDate(thisDate);
					tosr.setOriOrgId(so.getOrgId());
					tosr.setStaffSeq(so.getStaffSeq());
					tosr.setUserCode(so.getUserCode());
					tosr.setNote(so.getNote());
					Long staffId = PlatformUtil.getCurrentUserId();
					if (staffId != null) {
						tosr.setCreateStaff(staffId);
					}
					addList.add(tosr);
				}
			}
		}
		// 剩余未移出的数据就是要失效的数据
		if (treeOrgStaffRelalist != null && treeOrgStaffRelalist.size() > 0) {
			for (TreeOrgStaffRela tosr : treeOrgStaffRelalist) {
				tosr.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
				tosr.setEffDate(thisDate);
				tosr.setStatusDate(thisDate);
				tosr.setUpdateDate(thisDate);
				tosr.setExpDate(thisDate);
				Long staffId = PlatformUtil.getCurrentUserId();
				if (staffId != null) {
					tosr.setUpdateStaff(staffId);
				}
				removeList.add(tosr);
			}
		}
		Map map = new HashMap<String, List<TreeOrgStaffRela>>();
		map.put("addList", addList);
		map.put("removeList", removeList);
		map.put("updateList", updateList);
		return map;
	}

	@Override
	public List<StaffOrganization> getThisDateTreeStaffOrgList(Long orgTreeId,
			java.util.Date thisDate) {
		logger.info("2.1->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		String sql = "SELECT DISTINCT TSO.* FROM V_STAFF_ORGANIZATION TSO, V_STAFF TS, TREE_STAFF_SFT_RULE TSTR,(SELECT ORG_ID FROM TREE_ORG_RELA WHERE STATUS_CD = ? AND ORG_TREE_ID = ? UNION SELECT RELA_ORG_ID ORG_ID FROM TREE_ORG_RELA WHERE STATUS_CD = ? AND ORG_TREE_ID = ?) TORG WHERE TSO.STATUS_CD = ? AND TS.STATUS_CD = ? AND TSTR.STATUS_CD = ? AND TSO.EFF_DATE<= ? AND TSO.EXP_DATE> ? AND TS.EFF_DATE<=? AND TS.EXP_DATE> ? AND TS.STAFF_ID = TSO.STAFF_ID AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD AND TSTR.ORG_TREE_ID = ? AND TSO.ORG_ID = TORG.ORG_ID";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgTreeId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(thisDate);
		params.add(thisDate);
		params.add(thisDate);
		params.add(thisDate);
		params.add(orgTreeId);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql, params,
				StaffOrganization.class);
	}

	@Override
	public void updateTreeOrgStaffRelaData(Long orgTreeId,
			java.util.Date thisDate) {
		Map<String, List<TreeOrgStaffRela>> map = this
				.getNeedUpdateTreeOrgStaffRelaDataMap(
						this.getThisDateTreeStaffOrgList(orgTreeId, thisDate),
						orgTreeId, thisDate);
		logger.info("2.3->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		if (map != null) {
			List<TreeOrgStaffRela> removeList = map.get("removeList");
			if (removeList != null && removeList.size() > 0) {
				this.batchRemove(removeList);
			}
			logger.info("2.4->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
			List<TreeOrgStaffRela> addList = map.get("addList");
			if (addList != null && addList.size() > 0) {
				this.batchInsert(addList);
			}
			logger.info("2.5->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
			List<TreeOrgStaffRela> updateList = map.get("updateList");
			if (updateList != null && updateList.size() > 0) {
				this.batchUpdate(updateList);
			}
			logger.info("2.6->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new java.util.Date()));
		}
	}

	private List<StaffOrganization> getAddOrgStaffRelaData(
			java.util.Date preDate, java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		String preDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				preDate);
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT TSO.* FROM V_STAFF_ORGANIZATION TSO, V_STAFF TS, TREE_STAFF_SFT_RULE TSTR,");
		sql.append("(SELECT ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = ")
				.append(orgTreeId)
				.append(" UNION SELECT RELA_ORG_ID ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = ")
				.append(orgTreeId).append(" ) TORG ");
		sql.append(" WHERE TSO.STATUS_CD = '1000' AND TS.STATUS_CD = '1000' AND TSTR.STATUS_CD = '1000'");
		sql.append(" AND TSO.EFF_DATE<= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TSO.EXP_DATE> ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TS.EFF_DATE<=").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TS.EXP_DATE> ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TS.STAFF_ID = TSO.STAFF_ID AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD");
		sql.append(" AND TSTR.ORG_TREE_ID = ").append(orgTreeId)
				.append(" AND TSO.ORG_ID = TORG.ORG_ID");
		sql.append(" AND NOT EXISTS (SELECT 1 FROM TREE_ORG_STAFF_RELA O");
		sql.append(" WHERE O.ORG_TREE_ID =").append(orgTreeId);
		sql.append(" AND O.STATUS_CD = '1000'");
		sql.append(" AND TSO.STAFF_ID = O.STAFF_ID AND TSO.ORG_ID = O.ORG_ID AND TSO.RALA_CD = O.RELA_CD)");
		sql.append(" AND TSO.EFF_DATE >").append("to_date('")
				.append(preDateStr).append("','yyyyMMddhh24miss')");
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(preDate);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),
				params, StaffOrganization.class);
	}

	private List<StaffOrganization> getUpdateOrgStaffRelaData(
			java.util.Date preDate, java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		String preDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				preDate);
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT TSO.* FROM V_STAFF_ORGANIZATION TSO, V_STAFF TS, TREE_STAFF_SFT_RULE TSTR,");
		sql.append("(SELECT ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = ")
				.append(orgTreeId)
				.append(" UNION SELECT RELA_ORG_ID ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = ")
				.append(orgTreeId).append(") TORG ");
		sql.append(" WHERE TSO.STATUS_CD = '1000' AND TS.STATUS_CD = '1000' AND TSTR.STATUS_CD = '1000'");
		sql.append(" AND TSO.EFF_DATE<= ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TSO.EXP_DATE> ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TS.EFF_DATE<=").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')")
				.append(" AND TS.EXP_DATE> ").append("to_date('")
				.append(thisDateStr).append("','yyyyMMddhh24miss')");
		sql.append(" AND TS.STAFF_ID = TSO.STAFF_ID AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD");
		sql.append(" AND TSTR.ORG_TREE_ID = ").append(orgTreeId)
				.append(" AND TSO.ORG_ID = TORG.ORG_ID");
		sql.append(" AND EXISTS (SELECT 1 FROM TREE_ORG_STAFF_RELA O");
		sql.append(" WHERE O.ORG_TREE_ID =").append(orgTreeId);
		sql.append(" AND O.STATUS_CD = '1000'");
		sql.append(" AND TSO.STAFF_ID = O.STAFF_ID AND TSO.ORG_ID = O.ORG_ID AND TSO.RALA_CD = O.RELA_CD)");
		sql.append(" AND TSO.EFF_DATE >").append("to_date('")
				.append(preDateStr).append("','yyyyMMddhh24miss')");
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(preDate);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),
				params, StaffOrganization.class);
	}

	private List<TreeOrgStaffRela> getDelOrgStaffRelaData(
			java.util.Date preDate, java.util.Date thisDate, Long orgTreeId) {
		String thisDateStr = DateUtil.getDateByDateFormat("yyyyMMddHHmmss",
				thisDate);
		StringBuffer sql = new StringBuffer("select tosr.* from (");
		sql.append(
				" SELECT O.ORG_ID,O.STAFF_ID,O.RELA_CD FROM TREE_ORG_STAFF_RELA O  WHERE  O.ORG_TREE_ID =")
				.append(orgTreeId).append("  AND O.STATUS_CD = '1000'");
		sql.append("  minus");
		sql.append(" SELECT DISTINCT TSO.ORG_ID, TSO.STAFF_ID ,  TSO.RALA_CD FROM ");
		sql.append("   V_STAFF_ORGANIZATION TSO, ");
		sql.append("  V_STAFF TS, ");
		sql.append("   TREE_STAFF_SFT_RULE TSTR,");
		sql.append("  (");
		sql.append("  SELECT DISTINCT DECODE(n,1,ORG_ID,RELA_ORG_ID) org_id  FROM (");
		sql.append(
				" SELECT ORG_ID,RELA_ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID =")
				.append(orgTreeId);
		sql.append("  ) ,");
		sql.append("  (SELECT LEVEL n FROM DUAL CONNECT BY LEVEL<=2)");
		sql.append(" ) TORG  ");
		sql.append(" WHERE        ");
		sql.append(" TSO.STATUS_CD = '1000' ");
		sql.append(" AND TS.STATUS_CD = '1000' ");
		sql.append(" AND TSTR.STATUS_CD = '1000' ");
		sql.append(" AND TSO.EFF_DATE<= to_date('").append(thisDateStr)
				.append("','yyyyMMddhh24miss') ");
		sql.append(" AND TSO.EXP_DATE> to_date('").append(thisDateStr)
				.append("','yyyyMMddhh24miss') ");
		sql.append(" AND TS.EFF_DATE<=to_date('").append(thisDateStr)
				.append("','yyyyMMddhh24miss') ");
		sql.append(" AND TS.EXP_DATE> to_date('").append(thisDateStr)
				.append("','yyyyMMddhh24miss') ");
		sql.append(" AND TS.STAFF_ID = TSO.STAFF_ID ");
		sql.append(" AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD ");
		sql.append("  AND TSTR.ORG_TREE_ID =").append(orgTreeId);
		sql.append(" AND TSO.ORG_ID = TORG.ORG_ID ");
		sql.append("  )  tosrt");
		sql.append(" left join");
		sql.append(" TREE_ORG_STAFF_RELA tosr on (tosrt.Org_Id = tosr.org_id and tosrt.STAFF_ID = tosr.staff_id and tosrt.RELA_CD = tosr.RELA_CD)");
		sql.append(" WHERE  tosr.ORG_TREE_ID = ").append(orgTreeId)
				.append("   AND tosr.STATUS_CD = '1000' ");

		/*
		 * 
		 * StringBuffer sql = new StringBuffer(
		 * "SELECT * FROM TREE_ORG_STAFF_RELA O  WHERE O.ORG_TREE_ID = "
		 * ).append(orgTreeId); sql.append(" AND O.STATUS_CD = '1000'");
		 * sql.append(" AND NOT EXISTS (SELECT 1 FROM ("); sql.append(
		 * "SELECT DISTINCT TSO.* FROM V_STAFF_ORGANIZATION TSO, V_STAFF TS, TREE_STAFF_SFT_RULE TSTR,"
		 * );
		 * sql.append("(SELECT ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = "
		 * ).append(orgTreeId).append(
		 * " UNION SELECT RELA_ORG_ID ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = "
		 * ).append(orgTreeId).append(") TORG "); sql.append(
		 * " WHERE TSO.STATUS_CD = '1000' AND TS.STATUS_CD = '1000' AND TSTR.STATUS_CD = '1000'"
		 * );
		 * sql.append(" AND TSO.EFF_DATE<= ").append("to_date('").append(thisDateStr
		 * )
		 * .append("','yyyyMMddhh24miss')").append(" AND TSO.EXP_DATE> ").append
		 * ("to_date('").append(thisDateStr).append("','yyyyMMddhh24miss')");
		 * sql
		 * .append(" AND TS.EFF_DATE<=").append("to_date('").append(thisDateStr
		 * ).
		 * append("','yyyyMMddhh24miss')").append(" AND TS.EXP_DATE> ").append(
		 * "to_date('").append(thisDateStr).append("','yyyyMMddhh24miss')");
		 * sql.append(
		 * " AND TS.STAFF_ID = TSO.STAFF_ID AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD"
		 * ); sql.append(" AND TSTR.ORG_TREE_ID = ").append(orgTreeId).append(
		 * " AND TSO.ORG_ID = TORG.ORG_ID )T1");
		 * sql.append(" WHERE O.ORG_ID = T1.ORG_ID");
		 * sql.append(" AND O.STAFF_ID = T1.STAFF_ID");
		 * sql.append(" AND O.RELA_CD = T1.RALA_CD)");
		 */
		List params = new ArrayList();
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(orgTreeId);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(thisDate);
		// params.add(orgTreeId);
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(),
				params, TreeOrgStaffRela.class);
	}

	/*
	 * private List<TreeOrgStaffRela> getDelOrgStaffRelaData(java.util.Date
	 * preDate, java.util.Date thisDate, Long orgTreeId) { String
	 * thisDateStr=DateUtil.getDateByDateFormat("yyyyMMddHHmmss", thisDate);
	 * StringBuffer sql = new
	 * StringBuffer("SELECT * FROM TREE_ORG_STAFF_RELA O  WHERE O.ORG_TREE_ID = "
	 * ).append(orgTreeId); sql.append(" AND O.STATUS_CD = '1000'");
	 * sql.append(" AND NOT EXISTS (SELECT 1 FROM ("); sql.append(
	 * "SELECT DISTINCT TSO.* FROM V_STAFF_ORGANIZATION TSO, V_STAFF TS, TREE_STAFF_SFT_RULE TSTR,"
	 * );
	 * sql.append("(SELECT ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = ").append
	 * (orgTreeId).append(
	 * " UNION SELECT RELA_ORG_ID ORG_ID FROM TREE_ORG_RELA WHERE ORG_TREE_ID = "
	 * ).append(orgTreeId).append(") TORG "); sql.append(
	 * " WHERE TSO.STATUS_CD = '1000' AND TS.STATUS_CD = '1000' AND TSTR.STATUS_CD = '1000'"
	 * );
	 * sql.append(" AND TSO.EFF_DATE<= ").append("to_date('").append(thisDateStr
	 * ).append("','yyyyMMddhh24miss')").append(" AND TSO.EXP_DATE> ").append(
	 * "to_date('").append(thisDateStr).append("','yyyyMMddhh24miss')");
	 * sql.append
	 * (" AND TS.EFF_DATE<=").append("to_date('").append(thisDateStr).append
	 * ("','yyyyMMddhh24miss')"
	 * ).append(" AND TS.EXP_DATE> ").append("to_date('")
	 * .append(thisDateStr).append("','yyyyMMddhh24miss')"); sql.append(
	 * " AND TS.STAFF_ID = TSO.STAFF_ID AND TS.WORK_PROP = TSTR.REF_STAFF_TYPE_CD"
	 * ); sql.append(" AND TSTR.ORG_TREE_ID = ").append(orgTreeId).append(
	 * " AND TSO.ORG_ID = TORG.ORG_ID )T1");
	 * sql.append(" WHERE O.ORG_ID = T1.ORG_ID");
	 * sql.append(" AND O.STAFF_ID = T1.STAFF_ID");
	 * sql.append(" AND O.RELA_CD = T1.RALA_CD)"); List params = new
	 * ArrayList(); // params.add(orgTreeId); // params.add(orgTreeId); //
	 * params.add(orgTreeId); // params.add(thisDate); // params.add(thisDate);
	 * // params.add(thisDate); // params.add(thisDate); //
	 * params.add(orgTreeId); return
	 * DefaultDaoFactory.getDefaultDao().jdbcFindList(sql.toString(), params,
	 * TreeOrgStaffRela.class); }
	 */

	public void updateTreeOrgStaffRelaData(Long orgTreeId,
			java.util.Date preDate, java.util.Date thisDate) {
		List<StaffOrganization> addList = this.getAddOrgStaffRelaData(preDate,
				thisDate, orgTreeId);
		logger.info("2.1->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		List<StaffOrganization> updateList = this.getUpdateOrgStaffRelaData(
				preDate, thisDate, orgTreeId);
		logger.info("2.2->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		List<TreeOrgStaffRela> delList = this.getDelOrgStaffRelaData(preDate,
				thisDate, orgTreeId);
		logger.info("2.3->"
				+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
						new java.util.Date()));
		if (delList != null && delList.size() > 0) {
			for (TreeOrgStaffRela tosr : delList) {
				tosr.setEffDate(thisDate);
				tosr.setStatusDate(thisDate);
				tosr.setUpdateDate(thisDate);
				tosr.setExpDate(thisDate);
			}
		}

		if (addList != null && addList.size() > 0) {
			logger.info("addList size->" + addList.size());
			this.batchInsert(addList, orgTreeId, thisDate);
		}
		if (updateList != null && updateList.size() > 0) {
			logger.info("updateList size->" + updateList.size());
			this.batchUpdate(updateList, orgTreeId, thisDate);
		}
		if (delList != null && delList.size() > 0) {
			logger.info("delList size->" + delList.size());
			this.batchRemove(delList);
		}
	}
}
