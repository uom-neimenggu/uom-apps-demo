package cn.ffcs.uom.bpm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.bpm.model.ItsmProcessListLog;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.bpm.model.VOrgAuditBill;
import cn.ffcs.uom.bpm.model.VQaAudit;
import cn.ffcs.uom.bpm.model.VStaffAuditBill;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;

public interface BmpDao extends BaseDao{
    public PageInfo getBmpInfos(String regionName, Long execSctIdenti,int currentPage,int pageSize);    
    public PageInfo getHisBmpInfos(String regionName,Long execSctIdenti,int currentPage,int pageSize,Date beginDate,Date endDate);
    public List<VQaAudit> getBmpInfos(VQaAudit qaAudit);    
    public List<VStaffAuditBill> getBmpStaffList(Long monitorId,String monitorNames);
    public List<VOrgAuditBill> getBmpOrgList(Long monitorId,String monitorNames);
    public List<Map<String, Object>> getBmpStaffMap(Long qaUnOpsPpassId,String unit);
    public List<Map<String, Object>> getBmpOrgMap(Long qaUnOpsPpassId,String unit);
    public List<Map<String, Object>> getBmpMap(Long qaUnOpsPpassId,String unit);
    public String callBmpProcedure();
    
    public void addBmpLog(ItsmProcessListLog itsmProcessListLog);
    
    public List<ItsmProcessListLog> getLogByMsgId(String msgId);

	public List<QaUnOppExecScript> getList(QaUnOppExecScript qaUnOppExecScript);
    
}
