package cn.ffcs.uom.audit.manager;

import java.util.List;

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
public interface OrgAuditManager {

	/**
	 * 分页查询组织稽核
	 * 
	 * @param orgAudit
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgAudit(OrgAudit orgAudit, int currentPage,
			int pageSize);

	public List<OrgAudit> queryOrgAuditList(OrgAudit orgAudit);

	public List<OrgAuditBill> queryOrgAuditBillList(OrgAuditBill orgAuditBill);

	public List<NodeVo> queryOrgAuditNodes();

}
