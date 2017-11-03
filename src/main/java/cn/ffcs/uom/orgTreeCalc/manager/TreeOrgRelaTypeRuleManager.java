package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;

/**
 * 推导组织：组织关系
 * 
 * @author ZhaoF
 * 
 */
public interface TreeOrgRelaTypeRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeOrgOrRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryTreeOrgOrgRelaRulePageInfo(
			TreeOrgRelaTypeRule queryTreeOrgOrRule, int currentPage,
			int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeOrgRelaTypeRule
	 */
	public void removeTreeOrgRelaTypeRule(
			TreeOrgRelaTypeRule treeOrgRelaTypeRule);

	/**
	 * 新增
	 * 
	 * @param list
	 */
	public void addTreeOrgRelaTypeRuleList(List<TreeOrgRelaTypeRule> list);

}
