package cn.ffcs.uom.bpm.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.bpm.dao.BmpDao;
import cn.ffcs.uom.bpm.model.ItsmProcessListLog;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.bpm.model.QaUnOpsPpass;
import cn.ffcs.uom.bpm.model.VOrgAuditBill;
import cn.ffcs.uom.bpm.model.VQaAudit;
import cn.ffcs.uom.bpm.model.VStaffAuditBill;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

@Repository("bmpDao")
public class BmpDaoImpl extends BaseDaoImpl implements BmpDao {
	// 当前稽核
	@Override
	public PageInfo getBmpInfos(String regionName, Long execSctIdenti,
			int currentPage, int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from qa_un_ops_ppass t where t.exec_kid_identi=? ");
		if (execSctIdenti != null) {
			params.add(execSctIdenti);
			if (regionName != null) {
				sb.append("and t.unit = ? ");
				params.add(regionName);
			}
			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, QaUnOpsPpass.class);
		} else {
			return null;
		}
	}

	// 历史稽核
	@Override
	public PageInfo getHisBmpInfos(String regionName, Long execSctIdenti,
			int currentPage, int pageSize, Date beginDate, Date endDate) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String beginDateStr = sdf.format(beginDate);
		String endDateStr = sdf.format(endDate);
		sb.append("select * from qa_un_ops_ppass_his t where t.exec_kid_identi=? and to_char(t.create_date,'YYYY/MM/DD') between ? and ? ");
		params.add(execSctIdenti);
		params.add(beginDateStr);
		params.add(endDateStr);

		if (regionName != null) {
			sb.append(" and t.unit = ? ");
			params.add(regionName);
		}

		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, QaUnOpsPpass.class);
	}

	@Override
	public List<QaUnOppExecScript> getList(QaUnOppExecScript qaUnOppExecScript) {
		StringBuilder sql = new StringBuilder(
				"select * from QA_UN_OPP_EXEC_SCRIPT t where t.status_cd=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (qaUnOppExecScript != null) {
			if (!StrUtil.isNullOrEmpty(qaUnOppExecScript.getCheckType())) {
				sql.append(" AND t.check_type = ?");
				params.add(qaUnOppExecScript.getCheckType());
			}
			
			if (!StrUtil.isNullOrEmpty(qaUnOppExecScript.getExecSctIdenti())) {
				sql.append(" AND t.EXEC_SCT_IDENTI = ?");
				params.add(qaUnOppExecScript.getExecSctIdenti());
			}
		}
		sql.append(" ORDER BY t.exec_kid_identi");

		return this.jdbcFindList(sql.toString(), params,
				QaUnOppExecScript.class);
	}

	@Override
	public List<VStaffAuditBill> getBmpStaffList(Long monitorId,
			String monitorNames) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from V_STAFF_AUDIT_DETALIST t where t.monitor_id=? and t.names=?");
		params.add(monitorId);
		params.add(monitorNames);
		return this.jdbcFindList(sb.toString(), params, VStaffAuditBill.class);
	}

	@Override
	public List<VOrgAuditBill> getBmpOrgList(Long monitorId, String monitorNames) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from V_ORG_AUDIT_DETALIST t where t.monitor_id=? and t.names=?");
		params.add(monitorId);
		params.add(monitorNames);
		return this.jdbcFindList(sb.toString(), params, VOrgAuditBill.class);
	}

	@Override
	public List<Map<String, Object>> getBmpOrgMap(Long qaUnOpsPpassId,
			String unit) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.TEL_REGION_ID 区域,t.resul_1 组织ID,t.resul_2 组织编码,"
				+ "t.NOTES 异常详情,to_char(t.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') 稽核时间  "
				+ " from QA_UN_EXEC_RESULT t where t.EXEC_KID_IDENTI=? and t.tel_region_id=?");
		params.add(qaUnOpsPpassId);
		params.add(unit);
		return this.getJdbcTemplate().queryForList(sb.toString(),
				params.toArray());
	}

	public List<Map<String, Object>> getBmpStaffMap(Long qaUnOpsPpassId,
			String unit) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.TEL_REGION_ID 区域,t.resul_1 员工ID,t.resul_2 员工编码,"
				+ "t.detail 员工帐号,t.RECD_IDENTI 对应系统编码,t.NOTES 异常详情,"
				+ "to_char(t.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') 稽核时间  "
				+ " from QA_UN_EXEC_RESULT t where t.EXEC_KID_IDENTI=? and t.tel_region_id=?");
		params.add(qaUnOpsPpassId);
		params.add(unit);
		return this.getJdbcTemplate().queryForList(sb.toString(),
				params.toArray());
	}

	@Override
	public String callBmpProcedure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VQaAudit> getBmpInfos(VQaAudit qaAudit) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from V_QA_AUDIT t where 1=1");
		String monitorId = qaAudit.getMonitorId();
		if (!StrUtil.isNullOrEmpty(monitorId)) {
			sb.append(" AND t.MONITOR_ID = ?");
			params.add(monitorId);
		}
		String monitorName = qaAudit.getMonitorName();
		if (!StrUtil.isNullOrEmpty(monitorName)) {
			sb.append(" AND t.MONITOR_NAME = ?");
			params.add(monitorName);
		}
		String names = qaAudit.getNames();
		if (!StrUtil.isNullOrEmpty(names)) {
			sb.append(" AND t.names = ?");
			params.add(names);
		}
		String qualifiedYn = qaAudit.getQualifiedYn();
		if (!StrUtil.isNullOrEmpty(qualifiedYn)) {
			sb.append(" AND t.QUALIFIED_YN = ?");
			params.add(qualifiedYn);
		}
		String types = qaAudit.getTypes();
		if (!StrUtil.isNullOrEmpty(types)) {
			sb.append(" AND t.types = ?");
			params.add(types);
		}
		String codes = qaAudit.getCodes();
		if (!StrUtil.isNullOrEmpty(codes)) {
			sb.append(" AND t.codes = ?");
			params.add(codes);
		}
		return super.jdbcFindList(sb.toString(), params, VQaAudit.class);
	}

	@Override
	public void addBmpLog(ItsmProcessListLog itsmProcessListLog) {
		try {
			String sql = "insert into ITSM_PROCESS_LIST_LOG values (SEQ_ITSM_PROCESS_LIST_LOG.nextval,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			this.getJdbcTemplate().update(
					sql,
					new Object[] { itsmProcessListLog.getMsgId(),
							itsmProcessListLog.getListName(),
							itsmProcessListLog.getListType(),
							itsmProcessListLog.getListStatus(),
							itsmProcessListLog.getClassfyType(),
							itsmProcessListLog.getClassfyCode(),
							itsmProcessListLog.getResult(),
							itsmProcessListLog.getReqContent(),
							itsmProcessListLog.getResContent(),
							itsmProcessListLog.getCreateDate(),
							itsmProcessListLog.getUpdateDate() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ItsmProcessListLog> getLogByMsgId(String msgId) {
		// TODO Auto-generated method stub
		String sql = "select * from ITSM_PROCESS_LIST_LOG t where t.msg_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(msgId);
		return this.jdbcFindList(sql, params, ItsmProcessListLog.class);
	}

	@Override
	public List<Map<String, Object>> getBmpMap(Long qaUnOpsPpassId, String unit) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.TEL_REGION_ID 区域,t.RECD_IDENTI ID,t.NOTES 异常详情,t.CREATE_DATE 稽核时间  from QA_UN_EXEC_RESULT2 t where t.EXEC_SCT_IDENTI=? and t.tel_region_id=?");
		params.add(qaUnOpsPpassId);
		params.add(unit);
		return this.getJdbcTemplate().queryForList(sb.toString(),
				params.toArray());
	}
}
