/**
 * 
 */
package cn.ffcs.uom.information.vo;


/**
 * @author 曾臻
 * @date 2013-02-02
 *
 */
public class RollingItemVo {
	private long articleId;
	private String type;//"title","content"
	private String text;
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
