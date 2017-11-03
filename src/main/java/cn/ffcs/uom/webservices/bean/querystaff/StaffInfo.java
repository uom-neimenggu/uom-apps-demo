/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.querystaff;

/**
 * 员工信息
 * 
 * @version $Revision$ $Date$
 */
public class StaffInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 员工名称
     */
    private java.lang.String _name;

    /**
     * 证件
     */
    private cn.ffcs.uom.webservices.bean.querystaff.Certification _certification;

    /**
     * 联系信息
     */
    private cn.ffcs.uom.webservices.bean.querystaff.ContactInfo _contactInfo;


      //----------------/
     //- Constructors -/
    //----------------/

    public StaffInfo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'certification'. The field
     * 'certification' has the following description: 证件
     * 
     * @return the value of field 'Certification'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.Certification getCertification(
    ) {
        return this._certification;
    }

    /**
     * Returns the value of field 'contactInfo'. The field
     * 'contactInfo' has the following description: 联系信息
     * 
     * @return the value of field 'ContactInfo'.
     */
    public cn.ffcs.uom.webservices.bean.querystaff.ContactInfo getContactInfo(
    ) {
        return this._contactInfo;
    }

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: 员工名称
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Sets the value of field 'certification'. The field
     * 'certification' has the following description: 证件
     * 
     * @param certification the value of field 'certification'.
     */
    public void setCertification(
            final cn.ffcs.uom.webservices.bean.querystaff.Certification certification) {
        this._certification = certification;
    }

    /**
     * Sets the value of field 'contactInfo'. The field
     * 'contactInfo' has the following description: 联系信息
     * 
     * @param contactInfo the value of field 'contactInfo'.
     */
    public void setContactInfo(
            final cn.ffcs.uom.webservices.bean.querystaff.ContactInfo contactInfo) {
        this._contactInfo = contactInfo;
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: 员工名称
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

}
