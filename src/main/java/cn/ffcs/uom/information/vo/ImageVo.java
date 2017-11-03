/**
 * 
 */
package cn.ffcs.uom.information.vo;


/**
 * @author 曾臻
 * @dater 2013-01-14
 *
 */
public class ImageVo {
	/**
	 * 操作类型：add/remove
	 */
	private String operation;
	private long imageId;
	private String type;
	private int length;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public long getImageId() {
		return imageId;
	}
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
