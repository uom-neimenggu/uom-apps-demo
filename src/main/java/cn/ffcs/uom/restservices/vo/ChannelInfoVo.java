package cn.ffcs.uom.restservices.vo;

import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpIntfLog;

public class ChannelInfoVo {
    private ContractRootInParam rootIn;
    private ContractRootOutParam rootOutParam;
    private GrpIntfLog grpIntfLog;
    private String xml;
    public ContractRootInParam getRootIn() {
        return rootIn;
    }
    public void setRootIn(ContractRootInParam rootIn) {
        this.rootIn = rootIn;
    }
    public ContractRootOutParam getRootOutParam() {
        return rootOutParam;
    }
    public void setRootOutParam(ContractRootOutParam rootOutParam) {
        this.rootOutParam = rootOutParam;
    }
    public GrpIntfLog getGrpIntfLog() {
        return grpIntfLog;
    }
    public void setGrpIntfLog(GrpIntfLog grpIntfLog) {
        this.grpIntfLog = grpIntfLog;
    }
    public String getXml() {
        return xml;
    }
    public void setXml(String xml) {
        this.xml = xml;
    }
}
