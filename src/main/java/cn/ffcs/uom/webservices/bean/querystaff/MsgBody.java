/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.querystaff;

/**
 * Class MsgBody.
 * 
 * @version $Revision$ $Date$
 */
public class MsgBody implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * 请求参数
     */
    private cn.ffcs.uom.webservices.bean.querystaff.InParam _inParam;

    /**
     * 返回参数
     */
    private cn.ffcs.uom.webservices.bean.querystaff.OutParam _outParam;


      //----------------/
     //- Constructors -/
    //----------------/

    public MsgBody() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'inParam'. The field 'inParam'
     * has the following description: 请求参数
     * 
     * @return the value of field 'InParam'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.InParam getInParam(
    ) {
        return this._inParam;
    }

    /**
     * Returns the value of field 'outParam'. The field 'outParam'
     * has the following description: 返回参数
     * 
     * @return the value of field 'OutParam'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.OutParam getOutParam(
    ) {
        return this._outParam;
    }

    /**
     * Sets the value of field 'inParam'. The field 'inParam' has
     * the following description: 请求参数
     * 
     * @param inParam the value of field 'inParam'.
     */
    public void setInParam(
            final cn.ffcs.uom.webservices.bean.querystaff.InParam inParam) {
        this._inParam = inParam;
        this._choiceValue = inParam;
    }

    /**
     * Sets the value of field 'outParam'. The field 'outParam' has
     * the following description: 返回参数
     * 
     * @param outParam the value of field 'outParam'.
     */
    public void setOutParam(
            final cn.ffcs.uom.webservices.bean.querystaff.OutParam outParam) {
        this._outParam = outParam;
        this._choiceValue = outParam;
    }

}
