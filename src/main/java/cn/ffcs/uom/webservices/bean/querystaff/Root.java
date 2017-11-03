/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.querystaff;

/**
 * Class Root.
 * 
 * @version $Revision$ $Date$
 */
public class Root implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _msgHead.
     */
    private cn.ffcs.uom.webservices.bean.comm.MsgHead _msgHead;

    /**
     * Field _msgBody.
     */
    private cn.ffcs.uom.webservices.bean.querystaff.MsgBody _msgBody;


      //----------------/
     //- Constructors -/
    //----------------/

    public Root() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'msgBody'.
     * 
     * @return the value of field 'MsgBody'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.MsgBody getMsgBody(
    ) {
        return this._msgBody;
    }

    /**
     * Returns the value of field 'msgHead'.
     * 
     * @return the value of field 'MsgHead'.
     */
    public cn.ffcs.uom.webservices.bean.comm.MsgHead getMsgHead(
    ) {
        return this._msgHead;
    }

    /**
     * Sets the value of field 'msgBody'.
     * 
     * @param msgBody the value of field 'msgBody'.
     */
    public void setMsgBody(
            final cn.ffcs.uom.webservices.bean.querystaff.MsgBody msgBody) {
        this._msgBody = msgBody;
    }

    /**
     * Sets the value of field 'msgHead'.
     * 
     * @param msgHead the value of field 'msgHead'.
     */
    public void setMsgHead(
            final cn.ffcs.uom.webservices.bean.comm.MsgHead msgHead) {
        this._msgHead = msgHead;
    }

}
