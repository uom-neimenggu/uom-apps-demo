package cn.ffcs.uom.bpm.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.bpm.model.ProcessInformReq;
import cn.ffcs.uom.bpm.model.ProcessInformRes;
import cn.ffcs.uom.bpm.model.VOrgAuditBill;
import cn.ffcs.uom.bpm.model.VStaffAuditBill;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

public interface BmpManager {
    
    public List<NodeVo> getValuesList();
    
    public PageInfo getBmpInfos(String regionName, Long execSctIdenti,int currentPage,
        int pageSize);
    public PageInfo getHisBmpInfos(String regionName, Long execSctIdenti,int currentPage,int pageSize,Date beginDate,Date endDate);
    public List<VStaffAuditBill> getBmpStaffList(Long monitorId,String monitorNames);
    public List<VOrgAuditBill> getBmpOrgList(Long monitorId,String monitorNames);
    public List<Map<String, Object>> getBmpStaffMap(Long monitorId,String monitorNames);
    public List<Map<String, Object>> getBmpOrgMap(Long monitorId,String monitorNames);
    public List<Map<String, Object>> getBmpMap(Long qaUnOpsPpassId,String unit);
    public void checkAllBmps();
    public ProcessInformRes saveItsmInform(ProcessInformReq req);
    
}
