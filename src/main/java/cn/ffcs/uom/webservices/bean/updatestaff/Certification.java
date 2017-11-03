/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.updatestaff;

/**
 * 证件
 * 
 * @version $Revision$ $Date$
 */
public class Certification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 证件类型
     */
    private java.lang.String _certType;

    /**
     * 证件号码
     */
    private java.lang.String _certNbr;


      //----------------/
     //- Constructors -/
    //----------------/

    public Certification() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'certNbr'. The field 'certNbr'
     * has the following description: 证件号码
     * 
     * @return the value of field 'CertNbr'.
     */
    public java.lang.String getCertNbr(
    ) {
        return this._certNbr;
    }

    /**
     * Returns the value of field 'certType'. The field 'certType'
     * has the following description: 证件类型
     * 
     * @return the value of field 'CertType'.
     */
    public java.lang.String getCertType(
    ) {
        return this._certType;
    }

    /**
     * Sets the value of field 'certNbr'. The field 'certNbr' has
     * the following description: 证件号码
     * 
     * @param certNbr the value of field 'certNbr'.
     */
    public void setCertNbr(
            final java.lang.String certNbr) {
        this._certNbr = certNbr;
    }

    /**
     * Sets the value of field 'certType'. The field 'certType' has
     * the following description: 证件类型
     * 
     * @param certType the value of field 'certType'.
     */
    public void setCertType(
            final java.lang.String certType) {
        this._certType = certType;
    }

}
