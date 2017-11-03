/**
 * 
 */
package cn.ffcs.uom.information.vo;


/**
 * @author 曾臻
 */
public class AttachmentVo {
	/**
	 * 操作类型：add/remove
	 */
	private String operation;
	private long attachmentId;
	private String name;
	private long creationDate;
	private int length;
	private String alfId;
	
	
	public String getAlfId() {
		return alfId;
	}
	public void setAlfId(String alfId) {
		this.alfId = alfId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public long getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(long attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
}
