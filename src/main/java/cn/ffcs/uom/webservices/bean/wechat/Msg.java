package cn.ffcs.uom.webservices.bean.wechat;

import java.io.Serializable;

public class Msg implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String transId;
    private String staffAccount;
    private String staffName;
    private String cardId;
    private String timeStr;
    private String sign;
    
    public String getTransId() {
        return transId;
    }
    public void setTransId(String transId) {
        this.transId = transId;
    }
    public String getStaffAccount() {
        return staffAccount;
    }
    public void setStaffAccount(String staffAccount) {
        this.staffAccount = staffAccount;
    }
    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getTimeStr() {
        return timeStr;
    }
    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    
}
