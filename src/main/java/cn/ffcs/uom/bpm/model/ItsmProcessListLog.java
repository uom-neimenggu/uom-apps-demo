package cn.ffcs.uom.bpm.model;

import cn.ffcs.uom.common.model.UomEntity;

/**
 * ItsmProcessListLogId entity. @author MyEclipse Persistence Tools
 */

public class ItsmProcessListLog extends UomEntity implements java.io.Serializable {
    
    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    private String msgId;
    /**
     * 工单名称
     */
    private String listName;
    /**
     * 工单类型
     */
    private String listType;
    /**
     * 工单状态
     */
    private String listStatus;
    /**
     * 单据分类(1、分公司，2：业务系统)
     */
    private String classfyType;
    /**
     * 分类标识(分公司编码和系统编码)
     */
    private String classfyCode;
    /**
     * 接口调用结果
     */
    private String result;
    
    private String reqContent;
    private String resContent;
    
    public Long getItsmProcessListLogId() {
        return super.getId();
    }
    
    public void setItsmProcessListLogId(Long itsmProcessListLogId) {
        super.setId(itsmProcessListLogId);
    }
    
    public String getMsgId() {
        return this.msgId;
    }
    
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    
    public String getListName() {
        return this.listName;
    }
    
    public void setListName(String listName) {
        this.listName = listName;
    }
    
    public String getListType() {
        return this.listType;
    }
    
    public void setListType(String listType) {
        this.listType = listType;
    }
    
    public String getListStatus() {
        return this.listStatus;
    }
    
    public void setListStatus(String listStatus) {
        this.listStatus = listStatus;
    }
    
    public String getClassfyType() {
        return this.classfyType;
    }
    
    public void setClassfyType(String classfyType) {
        this.classfyType = classfyType;
    }
    
    public String getClassfyCode() {
        return this.classfyCode;
    }
    
    public void setClassfyCode(String classfyCode) {
        this.classfyCode = classfyCode;
    }
    
    public String getResult() {
        return this.result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }

    public String getResContent() {
        return resContent;
    }

    public void setResContent(String resContent) {
        this.resContent = resContent;
    }

    public String getReqContent() {
        return reqContent;
    }

    public void setReqContent(String reqContent) {
        this.reqContent = reqContent;
    }
    
}
