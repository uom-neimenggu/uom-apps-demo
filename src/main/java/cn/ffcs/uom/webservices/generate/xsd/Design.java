package cn.ffcs.uom.webservices.generate.xsd;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;

/**
 * Comment describing your root element.
 * 
 * @version $Revision$ $Date$
 */
public class Design implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/

	/**
	 * Field _fileList.
	 */
	private java.util.Vector _fileList;

	// ----------------/
	// - Constructors -/
	// ----------------/

	public Design() {
		super();
		_fileList = new Vector();
	} // -- com.cn.ffcs.uom.webservices.generate.xsd.Design()

	// -----------/
	// - Methods -/
	// -----------/

	/**
	 * Method addFile.
	 * 
	 * 
	 * 
	 * @param vFile
	 */
	public void addFile(cn.ffcs.uom.webservices.generate.xsd.File vFile)
			throws java.lang.IndexOutOfBoundsException {
		_fileList.addElement(vFile);
	} // -- void addFile(com.cn.ffcs.uom.webservices.generate.xsd.File)

	/**
	 * Method addFile
	 * 
	 * 
	 * 
	 * @param index
	 * @param vFile
	 */
	public void addFile(int index,
			cn.ffcs.uom.webservices.generate.xsd.File vFile)
			throws java.lang.IndexOutOfBoundsException {
		_fileList.insertElementAt(vFile, index);
	} // -- void addFile(int, com.cn.ffcs.uom.webservices.generate.xsd.File)

	/**
	 * Method enumerateFile
	 * 
	 * 
	 * 
	 * @return Enumeration
	 */
	public java.util.Enumeration enumerateFile() {
		return _fileList.elements();
	} // -- java.util.Enumeration enumerateFile()

	/**
	 * Method getFile
	 * 
	 * 
	 * 
	 * @param index
	 * @return File
	 */
	public cn.ffcs.uom.webservices.generate.xsd.File getFile(int index)
			throws java.lang.IndexOutOfBoundsException {
		// -- check bounds for index
		if ((index < 0) || (index > _fileList.size())) {
			throw new IndexOutOfBoundsException("getFile: Index value '"
					+ index + "' not in range [0.." + _fileList.size() + "]");
		}

		return (cn.ffcs.uom.webservices.generate.xsd.File) _fileList
				.elementAt(index);
	} // -- com.cn.ffcs.uom.webservices.generate.xsd.File getFile(int)

	/**
	 * Method getFile
	 * 
	 * 
	 * 
	 * @return File
	 */
	public cn.ffcs.uom.webservices.generate.xsd.File[] getFile() {
		int size = _fileList.size();
		cn.ffcs.uom.webservices.generate.xsd.File[] mArray = new cn.ffcs.uom.webservices.generate.xsd.File[size];
		for (int index = 0; index < size; index++) {
			mArray[index] = (cn.ffcs.uom.webservices.generate.xsd.File) _fileList
					.elementAt(index);
		}
		return mArray;
	} // -- com.cn.ffcs.uom.webservices.generate.xsd.File[] getFile()

	/**
	 * Method getFileCount
	 * 
	 * 
	 * 
	 * @return int
	 */
	public int getFileCount() {
		return _fileList.size();
	} // -- int getFileCount()

	/**
	 * Method removeAllFile
	 * 
	 */
	public void removeAllFile() {
		_fileList.removeAllElements();
	} // -- void removeAllFile()

	/**
	 * Method removeFile
	 * 
	 * 
	 * 
	 * @param index
	 * @return File
	 */
	public cn.ffcs.uom.webservices.generate.xsd.File removeFile(int index) {
		java.lang.Object obj = _fileList.elementAt(index);
		_fileList.removeElementAt(index);
		return (cn.ffcs.uom.webservices.generate.xsd.File) obj;
	} // -- com.cn.ffcs.uom.webservices.generate.xsd.File removeFile(int)

	/**
	 * Method setFile
	 * 
	 * 
	 * 
	 * @param index
	 * @param vFile
	 */
	public void setFile(int index,
			cn.ffcs.uom.webservices.generate.xsd.File vFile)
			throws java.lang.IndexOutOfBoundsException {
		// -- check bounds for index
		if ((index < 0) || (index > _fileList.size())) {
			throw new IndexOutOfBoundsException("setFile: Index value '"
					+ index + "' not in range [0.." + _fileList.size() + "]");
		}
		_fileList.setElementAt(vFile, index);
	} // -- void setFile(int, com.cn.ffcs.uom.webservices.generate.xsd.File)

	/**
	 * Method setFile
	 * 
	 * 
	 * 
	 * @param fileArray
	 */
	public void setFile(cn.ffcs.uom.webservices.generate.xsd.File[] fileArray) {
		// -- copy array
		_fileList.removeAllElements();
		for (int i = 0; i < fileArray.length; i++) {
			_fileList.addElement(fileArray[i]);
		}
	} // -- void setFile(com.cn.ffcs.uom.webservices.generate.xsd.File)

}
