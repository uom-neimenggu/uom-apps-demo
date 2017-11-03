package cn.ffcs.uom.bpm.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年5月27日
 * @功能说明：
 *
 */
public class ItsmProcessList implements Serializable{
    private static final long serialVersionUID = 1L;
    @JsonProperty("TYPE")
    private String type;
    @JsonProperty("MSG_ID")
    private String msgId;
    @JsonProperty("SHEET_TITLE")
    private String sheetTitle;
    @JsonProperty("USER_NAME")
    private String userName;
    @JsonProperty("APPLY_ORG_ID")
    private String applyOrgId;
    @JsonProperty("LINK_PHONE")
    private String linkPhone;
    @JsonProperty("Flow_personal_info")
    private List<FlowPersonalInfo> flowPersonalInfos;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getSheetTitle() {
        return sheetTitle;
    }
    public void setSheetTitle(String sheetTitle) {
        this.sheetTitle = sheetTitle;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getApplyOrgId() {
        return applyOrgId;
    }
    public void setApplyOrgId(String applyOrgId) {
        this.applyOrgId = applyOrgId;
    }
    public String getLinkPhone() {
        return linkPhone;
    }
    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }
    public List<FlowPersonalInfo> getFlowPersonalInfos() {
        return flowPersonalInfos;
    }
    public void setFlowPersonalInfos(List<FlowPersonalInfo> flowPersonalInfos) {
        this.flowPersonalInfos = flowPersonalInfos;
    }

}
