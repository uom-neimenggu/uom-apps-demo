/**
 * 
 */
package cn.ffcs.uom.information.dao;

import cn.ffcs.uom.information.vo.ArticleVo;

/**
 * @author 曾臻
 * @date 2012-11-20
 *
 */
public interface ArticleDao {
	public ArticleVo loadArticle(long id);
	/**
	 * 分页查询
	 * @author 曾臻
	 * @date 2012-11-20
	 * @param condition
	 * @param page 页号（从1开始）
	 * @param size 每页记录数
	 * @param sort 排序字段名
	 * @param order 排序方向 取值为desc/asc
	 * @return
	 */
	public Object[] queryArticlesPaged(ArticleVo condition,int page,int size,String sort,String order);
	public Object[] queryArticlesPaged(ArticleVo condition,int page,int size);
	public long addArticle(ArticleVo obj);
	/**
	 * 可部分更新，不需要更新的字段填null或0
	 * @author 曾臻
	 * @date 2012-11-20
	 * @param obj
	 */
	public void updateArticle(ArticleVo obj);
	public void deleteArticle(long id);
}
