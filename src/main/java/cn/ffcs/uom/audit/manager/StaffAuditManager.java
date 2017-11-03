package cn.ffcs.uom.audit.manager;

import java.util.List;

import cn.ffcs.uom.audit.model.StaffAudit;
import cn.ffcs.uom.audit.model.StaffAuditBill;
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
public interface StaffAuditManager {

	/**
	 * 分页查询员工稽核
	 * 
	 * @param staffAudit
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffAudit(StaffAudit staffAudit,
			int currentPage, int pageSize);

	public List<StaffAudit> queryStaffAuditList(StaffAudit staffAudit);

	public List<StaffAuditBill> queryStaffAuditBillList(
			StaffAuditBill staffAuditBill);
	public List<NodeVo> queryStaffAuditNodes();

}
