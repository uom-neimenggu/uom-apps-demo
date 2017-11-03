/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.ftpinform;

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
     * 批量文件列表
     */
    private cn.ffcs.uom.webservices.bean.ftpinform.FileList _fileList;

    /**
     * 同步类型：1：全量，0：增量
     */
    private java.lang.String _syncType;

    /**
     * 文件路径：/系统/日期/批次号
     */
    private java.lang.String _filePath;

    /**
     * 数据时间：YYYYMMDDHH24MISS
     */
    private java.lang.String _dataDate;


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
     * Returns the value of field 'dataDate'. The field 'dataDate'
     * has the following description: 数据时间：YYYYMMDDHH24MISS
     * 
     * @return the value of field 'DataDate'.
     */
    public java.lang.String getDataDate(
    ) {
        return this._dataDate;
    }

    /**
     * Returns the value of field 'fileList'. The field 'fileList'
     * has the following description: 批量文件列表
     * 
     * @return the value of field 'FileList'.
     */
    public cn.ffcs.uom.webservices.bean.ftpinform.FileList getFileList(
    ) {
        return this._fileList;
    }

    /**
     * Returns the value of field 'filePath'. The field 'filePath'
     * has the following description: 文件路径：/系统/日期/批次号
     * 
     * @return the value of field 'FilePath'.
     */
    public java.lang.String getFilePath(
    ) {
        return this._filePath;
    }

    /**
     * Returns the value of field 'syncType'. The field 'syncType'
     * has the following description: 同步类型：1：全量，0：增量
     * 
     * @return the value of field 'SyncType'.
     */
    public java.lang.String getSyncType(
    ) {
        return this._syncType;
    }

    /**
     * Sets the value of field 'dataDate'. The field 'dataDate' has
     * the following description: 数据时间：YYYYMMDDHH24MISS
     * 
     * @param dataDate the value of field 'dataDate'.
     */
    public void setDataDate(
            final java.lang.String dataDate) {
        this._dataDate = dataDate;
    }

    /**
     * Sets the value of field 'fileList'. The field 'fileList' has
     * the following description: 批量文件列表
     * 
     * @param fileList the value of field 'fileList'.
     */
    public void setFileList(
            final cn.ffcs.uom.webservices.bean.ftpinform.FileList fileList) {
        this._fileList = fileList;
    }

    /**
     * Sets the value of field 'filePath'. The field 'filePath' has
     * the following description: 文件路径：/系统/日期/批次号
     * 
     * @param filePath the value of field 'filePath'.
     */
    public void setFilePath(
            final java.lang.String filePath) {
        this._filePath = filePath;
    }

    /**
     * Sets the value of field 'syncType'. The field 'syncType' has
     * the following description: 同步类型：1：全量，0：增量
     * 
     * @param syncType the value of field 'syncType'.
     */
    public void setSyncType(
            final java.lang.String syncType) {
        this._syncType = syncType;
    }

}
