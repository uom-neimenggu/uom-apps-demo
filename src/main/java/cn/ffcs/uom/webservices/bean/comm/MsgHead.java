/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.comm;

/**
 * Class MsgHead.
 * 
 * @version $Revision$ $Date$
 */
public class MsgHead implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 发送消息包时间
     * YYYYMMDDHH24MISS
     */
    private java.lang.String _time;

    /**
     * 消息类型，填接口调用的方法名称
     */
    private java.lang.String _msgType;

    /**
     * 发送系统，标识消息包来源如：UAM(14000)
     */
    private java.lang.String _from;

    /**
     * 发送系统，标识消息包来源如：CRM(14107)
     */
    private java.lang.String _to;

    /**
     * 流水号，消息包唯一标识，反馈包体中的流水号应该对应于请求包体中的流水号
     */
    private java.lang.String _serial;

    /**
     * 发起系统签名
     */
    private java.lang.String _sysSign;


      //----------------/
     //- Constructors -/
    //----------------/

    public MsgHead() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'from'. The field 'from' has the
     * following description: 发送系统，标识消息包来源如：UAM(14000)
     * 
     * @return the value of field 'From'.
     */
    public java.lang.String getFrom(
    ) {
        return this._from;
    }

    /**
     * Returns the value of field 'msgType'. The field 'msgType'
     * has the following description: 消息类型，填接口调用的方法名称
     * 
     * @return the value of field 'MsgType'.
     */
    public java.lang.String getMsgType(
    ) {
        return this._msgType;
    }

    /**
     * Returns the value of field 'serial'. The field 'serial' has
     * the following description:
     * 流水号，消息包唯一标识，反馈包体中的流水号应该对应于请求包体中的流水号
     * 
     * @return the value of field 'Serial'.
     */
    public java.lang.String getSerial(
    ) {
        return this._serial;
    }

    /**
     * Returns the value of field 'sysSign'. The field 'sysSign'
     * has the following description: 发起系统签名
     * 
     * @return the value of field 'SysSign'.
     */
    public java.lang.String getSysSign(
    ) {
        return this._sysSign;
    }

    /**
     * Returns the value of field 'time'. The field 'time' has the
     * following description: 发送消息包时间
     * YYYYMMDDHH24MISS
     * 
     * 
     * @return the value of field 'Time'.
     */
    public java.lang.String getTime(
    ) {
        return this._time;
    }

    /**
     * Returns the value of field 'to'. The field 'to' has the
     * following description: 发送系统，标识消息包来源如：CRM(14107)
     * 
     * @return the value of field 'To'.
     */
    public java.lang.String getTo(
    ) {
        return this._to;
    }

    /**
     * Sets the value of field 'from'. The field 'from' has the
     * following description: 发送系统，标识消息包来源如：UAM(14000)
     * 
     * @param from the value of field 'from'.
     */
    public void setFrom(
            final java.lang.String from) {
        this._from = from;
    }

    /**
     * Sets the value of field 'msgType'. The field 'msgType' has
     * the following description: 消息类型，填接口调用的方法名称
     * 
     * @param msgType the value of field 'msgType'.
     */
    public void setMsgType(
            final java.lang.String msgType) {
        this._msgType = msgType;
    }

    /**
     * Sets the value of field 'serial'. The field 'serial' has the
     * following description: 流水号，消息包唯一标识，反馈包体中的流水号应该对应于请求包体中的流水号
     * 
     * @param serial the value of field 'serial'.
     */
    public void setSerial(
            final java.lang.String serial) {
        this._serial = serial;
    }

    /**
     * Sets the value of field 'sysSign'. The field 'sysSign' has
     * the following description: 发起系统签名
     * 
     * @param sysSign the value of field 'sysSign'.
     */
    public void setSysSign(
            final java.lang.String sysSign) {
        this._sysSign = sysSign;
    }

    /**
     * Sets the value of field 'time'. The field 'time' has the
     * following description: 发送消息包时间
     * YYYYMMDDHH24MISS
     * 
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(
            final java.lang.String time) {
        this._time = time;
    }

    /**
     * Sets the value of field 'to'. The field 'to' has the
     * following description: 发送系统，标识消息包来源如：CRM(14107)
     * 
     * @param to the value of field 'to'.
     */
    public void setTo(
            final java.lang.String to) {
        this._to = to;
    }

}
