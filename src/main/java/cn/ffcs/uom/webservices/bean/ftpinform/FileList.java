/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package cn.ffcs.uom.webservices.bean.ftpinform;

/**
 * 批量文件列表
 * 
 * @version $Revision$ $Date$
 */
public class FileList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fileInfoList.
     */
    private java.util.Vector _fileInfoList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FileList() {
        super();
        this._fileInfoList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vFileInfo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFileInfo(
            final cn.ffcs.uom.webservices.bean.ftpinform.FileInfo vFileInfo)
    throws java.lang.IndexOutOfBoundsException {
        this._fileInfoList.addElement(vFileInfo);
    }

    /**
     * 
     * 
     * @param index
     * @param vFileInfo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFileInfo(
            final int index,
            final cn.ffcs.uom.webservices.bean.ftpinform.FileInfo vFileInfo)
    throws java.lang.IndexOutOfBoundsException {
        this._fileInfoList.add(index, vFileInfo);
    }

    /**
     * Method enumerateFileInfo.
     * 
     * @return an Enumeration over all
     * cn.ffcs.uom.webservices.bean.ftpinform.FileInfo elements
     */
    public java.util.Enumeration enumerateFileInfo(
    ) {
        return this._fileInfoList.elements();
    }

    /**
     * Method getFileInfo.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * cn.ffcs.uom.webservices.bean.ftpinform.FileInfo at the given
     * index
     */
    public cn.ffcs.uom.webservices.bean.ftpinform.FileInfo getFileInfo(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fileInfoList.size()) {
            throw new IndexOutOfBoundsException("getFileInfo: Index value '" + index + "' not in range [0.." + (this._fileInfoList.size() - 1) + "]");
        }
        
        return (cn.ffcs.uom.webservices.bean.ftpinform.FileInfo) _fileInfoList.get(index);
    }

    /**
     * Method getFileInfo.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public cn.ffcs.uom.webservices.bean.ftpinform.FileInfo[] getFileInfo(
    ) {
        cn.ffcs.uom.webservices.bean.ftpinform.FileInfo[] array = new cn.ffcs.uom.webservices.bean.ftpinform.FileInfo[0];
        return (cn.ffcs.uom.webservices.bean.ftpinform.FileInfo[]) this._fileInfoList.toArray(array);
    }

    /**
     * Method getFileInfoCount.
     * 
     * @return the size of this collection
     */
    public int getFileInfoCount(
    ) {
        return this._fileInfoList.size();
    }

    /**
     */
    public void removeAllFileInfo(
    ) {
        this._fileInfoList.clear();
    }

    /**
     * Method removeFileInfo.
     * 
     * @param vFileInfo
     * @return true if the object was removed from the collection.
     */
    public boolean removeFileInfo(
            final cn.ffcs.uom.webservices.bean.ftpinform.FileInfo vFileInfo) {
        boolean removed = _fileInfoList.remove(vFileInfo);
        return removed;
    }

    /**
     * Method removeFileInfoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public cn.ffcs.uom.webservices.bean.ftpinform.FileInfo removeFileInfoAt(
            final int index) {
        java.lang.Object obj = this._fileInfoList.remove(index);
        return (cn.ffcs.uom.webservices.bean.ftpinform.FileInfo) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vFileInfo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setFileInfo(
            final int index,
            final cn.ffcs.uom.webservices.bean.ftpinform.FileInfo vFileInfo)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fileInfoList.size()) {
            throw new IndexOutOfBoundsException("setFileInfo: Index value '" + index + "' not in range [0.." + (this._fileInfoList.size() - 1) + "]");
        }
        
        this._fileInfoList.set(index, vFileInfo);
    }

    /**
     * 
     * 
     * @param vFileInfoArray
     */
    public void setFileInfo(
            final cn.ffcs.uom.webservices.bean.ftpinform.FileInfo[] vFileInfoArray) {
        //-- copy array
        _fileInfoList.clear();
        
        for (int i = 0; i < vFileInfoArray.length; i++) {
                this._fileInfoList.add(vFileInfoArray[i]);
        }
    }

}
