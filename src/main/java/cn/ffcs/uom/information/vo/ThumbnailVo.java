/**
 * 
 */
package cn.ffcs.uom.information.vo;


/**
 * @author 曾臻
 * @dater 2013-01-14
 *
 */
public class ThumbnailVo {
	private long thumbnailId;
	private int length;
	private String brief;
	
	
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public long getThumbnailId() {
		return thumbnailId;
	}
	public void setThumbnailId(long thumbnailId) {
		this.thumbnailId = thumbnailId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
