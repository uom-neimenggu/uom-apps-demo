/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.querystaff;

/**
 * 返回参数
 * 
 * @version $Revision$ $Date$
 */
public class OutParam implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 处理结果
     * 1：成功 
     * 0：失败
     */
    private java.lang.String _result;

    /**
     * 错误信息
     */
    private cn.ffcs.uom.webservices.bean.querystaff.Error _error;

    /**
     * 员工信息
     */
    private cn.ffcs.uom.webservices.bean.querystaff.StaffInfo _staffInfo;


      //----------------/
     //- Constructors -/
    //----------------/

    public OutParam() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'error'. The field 'error' has
     * the following description: 错误信息
     * 
     * @return the value of field 'Error'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.Error getError(
    ) {
        return this._error;
    }

    /**
     * Returns the value of field 'result'. The field 'result' has
     * the following description: 处理结果
     * 1：成功 
     * 0：失败
     * 
     * @return the value of field 'Result'.
     */
    public java.lang.String getResult(
    ) {
        return this._result;
    }

    /**
     * Returns the value of field 'staffInfo'. The field
     * 'staffInfo' has the following description: 员工信息
     * 
     * @return the value of field 'StaffInfo'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.StaffInfo getStaffInfo(
    ) {
        return this._staffInfo;
    }

    /**
     * Sets the value of field 'error'. The field 'error' has the
     * following description: 错误信息
     * 
     * @param error the value of field 'error'.
     */
    public void setError(
            final cn.ffcs.uom.webservices.bean.querystaff.Error error) {
        this._error = error;
    }

    /**
     * Sets the value of field 'result'. The field 'result' has the
     * following description: 处理结果
     * 1：成功 
     * 0：失败
     * 
     * @param result the value of field 'result'.
     */
    public void setResult(
            final java.lang.String result) {
        this._result = result;
    }

    /**
     * Sets the value of field 'staffInfo'. The field 'staffInfo'
     * has the following description: 员工信息
     * 
     * @param staffInfo the value of field 'staffInfo'.
     */
    public void setStaffInfo(
            final cn.ffcs.uom.webservices.bean.querystaff.StaffInfo staffInfo) {
        this._staffInfo = staffInfo;
    }

}
