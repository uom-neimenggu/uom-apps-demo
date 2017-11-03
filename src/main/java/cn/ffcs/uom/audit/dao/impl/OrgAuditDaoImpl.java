/**
 * 
 */
package cn.ffcs.uom.audit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.audit.dao.OrgAuditDao;
import cn.ffcs.uom.audit.model.OrgAudit;
import cn.ffcs.uom.audit.model.OrgAuditBill;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * 组织稽核查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-07-30
 * @功能说明：
 * 
 */
@Repository("orgAuditDao")
@Transactional
public class OrgAuditDaoImpl extends BaseDaoImpl implements OrgAuditDao {

	@Override
	public PageInfo queryPageInfoByOrgAudit(OrgAudit orgAudit, int currentPage,
			int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_ORG_AUDIT WHERE 1=1");

		if (orgAudit != null) {

			if (!StrUtil.isNullOrEmpty(orgAudit.getSystemDomainId())) {
				sb.append(" AND SYSTEM_DOMAIN_ID = ?");
				params.add(orgAudit.getSystemDomainId());
			}

			if (!StrUtil.isNullOrEmpty(orgAudit.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(orgAudit.getUpdateDate()));
			}

		}
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, OrgAudit.class);
	}

	@Override
	public List<OrgAudit> queryOrgAuditList(OrgAudit orgAudit) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_ORG_AUDIT WHERE 1=1");

		if (orgAudit != null) {

			// if (!StrUtil.isNullOrEmpty(orgAudit.getSystemDomainId())) {
			// sb.append(" AND SYSTEM_DOMAIN_ID = ?");
			// params.add(orgAudit.getSystemDomainId());
			// }

			if (!StrUtil.isNullOrEmpty(orgAudit.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(orgAudit.getUpdateDate()));
			}

		}
		return this.jdbcFindList(sb.toString(), params, OrgAudit.class);
	}

	@Override
	public List<OrgAuditBill> queryOrgAuditBillList(OrgAuditBill orgAuditBill) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM V_ORG_AUDIT_BILL WHERE 1=1");

		if (orgAuditBill != null) {

			if (!StrUtil.isNullOrEmpty(orgAuditBill.getSystemDomainId())) {
				sb.append(" AND SYSTEM_DOMAIN_ID = ?");
				params.add(orgAuditBill.getSystemDomainId());
			}

			if (!StrUtil.isNullOrEmpty(orgAuditBill.getUpdateDate())) {
				sb.append(" AND TO_CHAR(UPDATE_DATE,'YYYY-MM-DD') = ?");
				params.add(DateUtil.getShortDateStr(orgAuditBill
						.getUpdateDate()));
			}

		}
		return this.jdbcFindList(sb.toString(), params, OrgAuditBill.class);
	}

	@Override
	public List<NodeVo> queryOrgAuditNodes() {
		// TODO Auto-generated method stub
		String sql = "select distinct t.SYSTEM_NAME name,t.SYSTEM_DOMAIN_ID id from v_org_audit t where t.SYSTEM_DOMAIN_ID!=0";
		List<NodeVo> orgAuditList = this.jdbcFindList(sql, new ArrayList(), NodeVo.class);
		return orgAuditList;
	}
}
