/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.ftpinform;

/**
 * 错误信息
 * 
 * @version $Revision$ $Date$
 */
public class Error implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 错误标识
     */
    private java.lang.String _id;

    /**
     * 错误信息
     */
    private java.lang.String _message;


      //----------------/
     //- Constructors -/
    //----------------/

    public Error() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: 错误标识
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'message'. The field 'message'
     * has the following description: 错误信息
     * 
     * @return the value of field 'Message'.
     */
    public java.lang.String getMessage(
    ) {
        return this._message;
    }

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: 错误标识
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'message'. The field 'message' has
     * the following description: 错误信息
     * 
     * @param message the value of field 'message'.
     */
    public void setMessage(
            final java.lang.String message) {
        this._message = message;
    }

}
