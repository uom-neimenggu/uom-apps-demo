package cn.ffcs.uom.bpm.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.bpm.dao.BmpDao;
import cn.ffcs.uom.bpm.manager.BmpManager;
import cn.ffcs.uom.bpm.model.FlowPersonalInfo;
import cn.ffcs.uom.bpm.model.ItsmProcessList;
import cn.ffcs.uom.bpm.model.ItsmProcessListLog;
import cn.ffcs.uom.bpm.model.ProcessInformReq;
import cn.ffcs.uom.bpm.model.ProcessInformRes;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.bpm.model.VOrgAuditBill;
import cn.ffcs.uom.bpm.model.VQaAudit;
import cn.ffcs.uom.bpm.model.VStaffAuditBill;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;
import cn.ffcs.uom.webservices.util.WsClientUtil;

@Service("bmpManager")
public class BmpManagerImpl implements BmpManager{

    @Autowired
    private BmpDao bmpDao;
    @Override
    public List<NodeVo> getValuesList() {
    	QaUnOppExecScript qaUnOppExecScript = new QaUnOppExecScript();
    	qaUnOppExecScript.setCheckType("1");
    	qaUnOppExecScript.setExecSctIdenti(10L);
        List<QaUnOppExecScript> bmpItemList = bmpDao.getList(qaUnOppExecScript);
        List<NodeVo> retAttrValues = new ArrayList<NodeVo>();
        if (bmpItemList != null) {
            for (QaUnOppExecScript bmpItem : bmpItemList) {
                if (bmpItem != null) {
                    NodeVo vo = new NodeVo();
                    vo.setId(bmpItem.getExecKidIdenti());
                    vo.setName(bmpItem.getExecKidName());
                    retAttrValues.add(vo);
                }
            }
        }
        return retAttrValues;
    }
    @Override
    public PageInfo getBmpInfos(String regionName, Long execSctIdenti, int currentPage, int pageSize) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpInfos(regionName, execSctIdenti, currentPage, pageSize);
    }
	@Override
	public PageInfo getHisBmpInfos(String regionName, Long execSctIdenti, int currentPage, int pageSize,
		Date beginDate, Date endDate) {
		// TODO Auto-generated method stub
		return bmpDao.getHisBmpInfos(regionName, execSctIdenti, currentPage, pageSize, beginDate, endDate);
	}
    @Override
    public List<VStaffAuditBill> getBmpStaffList(Long monitorId,String monitorNames) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpStaffList(monitorId,monitorNames);
    }
    @Override
    public List<VOrgAuditBill> getBmpOrgList(Long monitorId,String monitorNames) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpOrgList(monitorId,monitorNames);
    }
    @Override
    public List<Map<String, Object>> getBmpStaffMap(Long monitorId,String monitorNames) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpStaffMap(monitorId,monitorNames);
    }
    @Override
    public List<Map<String, Object>> getBmpOrgMap(Long monitorId,String monitorNames) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpOrgMap(monitorId,monitorNames);
    }
    @Override
    public void checkAllBmps() {
        // TODO Auto-generated method stub
        VQaAudit qaAudit = new VQaAudit();
        qaAudit.setQualifiedYn("不合格");
        List<VQaAudit> list = bmpDao.getBmpInfos(qaAudit);
        if (list != null && list.size()>0) {
            for (VQaAudit vQaAudit : list) {
                String msgId = EsbHeadUtil.getEAMMsgId(WsConstants.OIP_SENDER);
                String listName = "主数据稽核告警："+vQaAudit.getNames()+"在稽核指标【"+vQaAudit.getMonitorName()+"】上不合格，标准合格率："+vQaAudit.getStaQualifiedRate()+",实际合格率："+vQaAudit.getPercents()+",异常数量："+vQaAudit.getExceNumber()+"。";
                ItsmProcessList itsmProcessList = new ItsmProcessList();
                itsmProcessList.setType("10");
                itsmProcessList.setMsgId(msgId);
                itsmProcessList.setSheetTitle(listName);
                itsmProcessList.setApplyOrgId("B7EE37BCD5D940D4A29119FA16AB9DB9");
                itsmProcessList.setUserName("134976");
                itsmProcessList.setLinkPhone("15305515083");
                FlowPersonalInfo flowPersonalInfo1 = new FlowPersonalInfo();
                flowPersonalInfo1.setFieldName("CLASSFY_TYPE");
                flowPersonalInfo1.setFieldValue(vQaAudit.getTypes());
                FlowPersonalInfo flowPersonalInfo2 = new FlowPersonalInfo();
                flowPersonalInfo2.setFieldName("CLASSFY_CODE");
                flowPersonalInfo2.setFieldValue(vQaAudit.getTypes());
                FlowPersonalInfo flowPersonalInfo3 = new FlowPersonalInfo();
                flowPersonalInfo3.setFieldName("URG_DEGREE");
                flowPersonalInfo3.setFieldValue("1");
                List<FlowPersonalInfo> flowPersonalInfos = new ArrayList<FlowPersonalInfo>();
                flowPersonalInfos.add(flowPersonalInfo1);
                flowPersonalInfos.add(flowPersonalInfo2);
                flowPersonalInfos.add(flowPersonalInfo3);
                itsmProcessList.setFlowPersonalInfos(flowPersonalInfos);
                String esbHead = EsbHeadUtil.getEsbHead(WsConstants.OIP_SENDER,
                    WsConstants.STSM_OIP_SERVICE_CODE, msgId, "");
                try {
                    String inJson = JacksonUtil.Object2JSon(itsmProcessList);
                    String outJson = WsClientUtil.wsCallOnOip(esbHead, inJson, "", "", "");
                    String result = JacksonUtil.getValue(outJson, "RESULT");
                    ItsmProcessListLog itsmProcessListLog  = new ItsmProcessListLog();
                    itsmProcessListLog.setMsgId(msgId);
                    itsmProcessListLog.setListName(listName);
                    itsmProcessListLog.setListType(BaseUnitConstants.ITSM_PROCESS_LOG_TYPE_1);
                    itsmProcessListLog.setListStatus(BaseUnitConstants.ITSM_PROCESS_STATUS_1);
                    itsmProcessListLog.setClassfyType(vQaAudit.getCodes());
                    itsmProcessListLog.setResult(result);
                    itsmProcessListLog.setReqContent(inJson);
                    itsmProcessListLog.setResContent(outJson);
                    itsmProcessListLog.addOnly();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public ProcessInformRes saveItsmInform(ProcessInformReq req) {
        ProcessInformRes res = new ProcessInformRes();
        res.setResult("1");
        res.setMsg("处理成功");
        try {
            ItsmProcessListLog itsmProcessListLog = new ItsmProcessListLog();
            itsmProcessListLog.setMsgId(req.getSerial());
            itsmProcessListLog.setListType(BaseUnitConstants.ITSM_PROCESS_LOG_TYPE_2);
            itsmProcessListLog.setListStatus(req.getStatus());
            itsmProcessListLog.setResult("0");
            itsmProcessListLog.setReqContent(JacksonUtil.Object2JSon(req));
            itsmProcessListLog.setResContent(JacksonUtil.Object2JSon(res));
            itsmProcessListLog.addOnly();
            
            List<ItsmProcessListLog> logs = bmpDao.getLogByMsgId(req.getSerial());
            if (logs != null && logs.size()>0) {
                ItsmProcessListLog log = logs.get(0);
                if (BaseUnitConstants.ITSM_PROCESS_STATUS_0.equals(req.getStatus())) {
                    log.setListStatus(BaseUnitConstants.ITSM_PROCESS_STATUS_0);
                }else {
                }
                itsmProcessListLog.updateOnly();
            }
            
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }
    @Override
    public List<Map<String, Object>> getBmpMap(Long qaUnOpsPpassId,String unit) {
        // TODO Auto-generated method stub
        return bmpDao.getBmpMap(qaUnOpsPpassId,unit);
    }
}
