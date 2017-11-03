package cn.ffcs.uom.bpm.model;

/**
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年5月27日
 * @功能说明：UOM提供给itsm的流程通知接口bean
 *
 */
public class ItsmProcessInform {
    
    /**
     * 单据号（ITM返回的单号）
     */
    private String serial;
    //单据状态 0-归档；1-处理中
    private String status;
    //描述
    private String desc;
    //扩展1
    private String expa1;
    //扩展2
    private String expa2;
    
    public String getSerial() {
        return serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getExpa1() {
        return expa1;
    }
    public void setExpa1(String expa1) {
        this.expa1 = expa1;
    }
    public String getExpa2() {
        return expa2;
    }
    public void setExpa2(String expa2) {
        this.expa2 = expa2;
    }
    
}
