/**
 * 
 */
package cn.ffcs.uom.audit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.audit.dao.StaffAuditDao;
import cn.ffcs.uom.audit.model.StaffAudit;
import cn.ffcs.uom.audit.model.StaffAuditBill;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * 员工稽核查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-07-30
 * @功能说明：
 * 
 */
@Repository("staffAuditDao")
@Transactional
public class StaffAuditDaoImpl extends BaseDaoImpl implements StaffAuditDao {

	@Override
	public PageInfo queryPageInfoByStaffAudit(StaffAudit staffAudit,
			int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_STAFF_AUDIT WHERE 1=1");

		if (staffAudit != null) {

			if (!StrUtil.isNullOrEmpty(staffAudit.getSystemDomainId())) {
				sb.append(" AND SYSTEM_DOMAIN_ID = ?");
				params.add(staffAudit.getSystemDomainId());
			}

			if (!StrUtil.isNullOrEmpty(staffAudit.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(staffAudit.getUpdateDate()));
			}

		}
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, StaffAudit.class);
	}

	@Override
	public List<StaffAudit> queryStaffAuditList(StaffAudit staffAudit) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_STAFF_AUDIT WHERE 1=1");

		if (staffAudit != null) {

			// if (!StrUtil.isNullOrEmpty(staffAudit.getSystemDomainId())) {
			// sb.append(" AND SYSTEM_DOMAIN_ID = ?");
			// params.add(staffAudit.getSystemDomainId());
			// }

			if (!StrUtil.isNullOrEmpty(staffAudit.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(staffAudit.getUpdateDate()));
			}

		}
		return this.jdbcFindList(sb.toString(), params, StaffAudit.class);
	}

	@Override
	public List<StaffAuditBill> queryStaffAuditBillList(
			StaffAuditBill staffAuditBill) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_STAFF_AUDIT_BILL WHERE 1=1");

		if (staffAuditBill != null) {

			if (!StrUtil.isNullOrEmpty(staffAuditBill.getSystemDomainId())) {
				sb.append(" AND SYSTEM_DOMAIN_ID = ?");
				params.add(staffAuditBill.getSystemDomainId());
			}

			if (!StrUtil.isNullOrEmpty(staffAuditBill.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(staffAuditBill
						.getUpdateDate()));
			}

		}
		return this.jdbcFindList(sb.toString(), params, StaffAuditBill.class);
	}

	@Override
	public List<NodeVo> queryStaffAuditNodes() {
		// TODO Auto-generated method stub
		String sql = "select distinct t.SYSTEM_NAME name,t.SYSTEM_DOMAIN_ID id from v_staff_audit t where t.SYSTEM_DOMAIN_ID!=0";
		List<NodeVo> staffAuditList = this.jdbcFindList(sql, new ArrayList(), NodeVo.class);
		return staffAuditList;
	}
}
