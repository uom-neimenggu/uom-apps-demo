package cn.ffcs.uom.audit.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.audit.dao.StaffAuditDao;
import cn.ffcs.uom.audit.manager.StaffAuditManager;
import cn.ffcs.uom.audit.model.OrgAuditBill;
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
@Service("staffAuditManager")
@Scope("prototype")
public class StaffAuditManagerImpl implements StaffAuditManager {

	@Resource(name = "staffAuditDao")
	private StaffAuditDao staffAuditDao;

	/**
	 * 分页查询员工稽核
	 * 
	 * @param staffAudit
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByStaffAudit(StaffAudit staffAudit,
			int currentPage, int pageSize) {
		return staffAuditDao.queryPageInfoByStaffAudit(staffAudit, currentPage,
				pageSize);
	}

	@Override
	public List<StaffAudit> queryStaffAuditList(StaffAudit staffAudit) {
		return staffAuditDao.queryStaffAuditList(staffAudit);
	}

	@Override
	public List<StaffAuditBill> queryStaffAuditBillList(
			StaffAuditBill staffAuditBill) {
		return staffAuditDao.queryStaffAuditBillList(staffAuditBill);
	}

	@Override
	public List<NodeVo> queryStaffAuditNodes() {
		// TODO Auto-generated method stub
		return staffAuditDao.queryStaffAuditNodes();
	}

}
