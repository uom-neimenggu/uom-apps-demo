/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.ftpinform;

/**
 * Class FileInfo.
 * 
 * @version $Revision$ $Date$
 */
public class FileInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 文件名称
     */
    private java.lang.String _fileName;

    /**
     * 数据数
     */
    private java.lang.String _dataCount;


      //----------------/
     //- Constructors -/
    //----------------/

    public FileInfo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dataCount'. The field
     * 'dataCount' has the following description: 数据数
     * 
     * @return the value of field 'DataCount'.
     */
    public java.lang.String getDataCount(
    ) {
        return this._dataCount;
    }

    /**
     * Returns the value of field 'fileName'. The field 'fileName'
     * has the following description: 文件名称
     * 
     * @return the value of field 'FileName'.
     */
    public java.lang.String getFileName(
    ) {
        return this._fileName;
    }

    /**
     * Sets the value of field 'dataCount'. The field 'dataCount'
     * has the following description: 数据数
     * 
     * @param dataCount the value of field 'dataCount'.
     */
    public void setDataCount(
            final java.lang.String dataCount) {
        this._dataCount = dataCount;
    }

    /**
     * Sets the value of field 'fileName'. The field 'fileName' has
     * the following description: 文件名称
     * 
     * @param fileName the value of field 'fileName'.
     */
    public void setFileName(
            final java.lang.String fileName) {
        this._fileName = fileName;
    }

}
