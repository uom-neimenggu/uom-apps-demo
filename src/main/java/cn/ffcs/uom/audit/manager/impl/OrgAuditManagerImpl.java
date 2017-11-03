package cn.ffcs.uom.audit.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.audit.dao.OrgAuditDao;
import cn.ffcs.uom.audit.manager.OrgAuditManager;
import cn.ffcs.uom.audit.model.OrgAudit;
import cn.ffcs.uom.audit.model.OrgAuditBill;
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
@Service("orgAuditManager")
@Scope("prototype")
public class OrgAuditManagerImpl implements OrgAuditManager {

	@Resource(name = "orgAuditDao")
	private OrgAuditDao orgAuditDao;

	/**
	 * 分页查询组织稽核
	 * 
	 * @param orgAudit
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByOrgAudit(OrgAudit orgAudit, int currentPage,
			int pageSize) {
		return orgAuditDao.queryPageInfoByOrgAudit(orgAudit, currentPage,
				pageSize);
	}

	@Override
	public List<OrgAudit> queryOrgAuditList(OrgAudit orgAudit) {
		return orgAuditDao.queryOrgAuditList(orgAudit);
	}

	@Override
	public List<OrgAuditBill> queryOrgAuditBillList(OrgAuditBill orgAuditBill) {
		return orgAuditDao.queryOrgAuditBillList(orgAuditBill);
	}

	@Override
	public List<NodeVo> queryOrgAuditNodes() {
		// TODO Auto-generated method stub
		return orgAuditDao.queryOrgAuditNodes();
	}

}
