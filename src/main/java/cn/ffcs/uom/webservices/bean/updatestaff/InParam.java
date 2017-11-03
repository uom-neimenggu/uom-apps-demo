/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.updatestaff;

/**
 * 请求参数
 * 
 * @version $Revision$ $Date$
 */
public class InParam implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _staffAccount.
     */
    private java.lang.String _staffAccount;

    /**
     * 要修改的信息
     */
    private cn.ffcs.uom.webservices.bean.updatestaff.UpdateStaffInfo _updateStaffInfo;


      //----------------/
     //- Constructors -/
    //----------------/

    public InParam() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'staffAccount'.
     * 
     * @return the value of field 'StaffAccount'.
     */
    public java.lang.String getStaffAccount(
    ) {
        return this._staffAccount;
    }

    /**
     * Returns the value of field 'updateStaffInfo'. The field
     * 'updateStaffInfo' has the following description: 要修改的信息
     * 
     * @return the value of field 'UpdateStaffInfo'.
     */
    public cn.ffcs.uom.webservices.bean.updatestaff.UpdateStaffInfo getUpdateStaffInfo(
    ) {
        return this._updateStaffInfo;
    }

    /**
     * Sets the value of field 'staffAccount'.
     * 
     * @param staffAccount the value of field 'staffAccount'.
     */
    public void setStaffAccount(
            final java.lang.String staffAccount) {
        this._staffAccount = staffAccount;
    }

    /**
     * Sets the value of field 'updateStaffInfo'. The field
     * 'updateStaffInfo' has the following description: 要修改的信息
     * 
     * @param updateStaffInfo the value of field 'updateStaffInfo'.
     */
    public void setUpdateStaffInfo(
            final cn.ffcs.uom.webservices.bean.updatestaff.UpdateStaffInfo updateStaffInfo) {
        this._updateStaffInfo = updateStaffInfo;
    }

}
