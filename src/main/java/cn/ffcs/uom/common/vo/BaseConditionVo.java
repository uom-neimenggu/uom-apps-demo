/**
 * 
 */
package cn.ffcs.uom.common.vo;

/**
 * 查询条件基类。
 * 
 * @author wuzhb
 * 
 */
public class BaseConditionVo {
	private int currentPageIndex;
	private int itemsPerPage;
	private int totalCount;

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

}
