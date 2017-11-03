package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;

/**
 * 推导组织：组织类型
 * 
 * @author ZhaoF
 * 
 */
public interface TreeOrgTypeRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeOrgTypeRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo queryTreeOrgOrgTypeRulePageInfo(
			TreeOrgTypeRule queryTreeOrgTypeRule, int currentPage, int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeOrgTypeRule
	 */
	void removeTreeOrgTypeRule(TreeOrgTypeRule treeOrgTypeRule);

	/**
	 * 新增
	 * 
	 * @param treeOrgTypeRuleList
	 */
	void addTreeOrgTypeRuleList(List<TreeOrgTypeRule> treeOrgTypeRuleList);

}
