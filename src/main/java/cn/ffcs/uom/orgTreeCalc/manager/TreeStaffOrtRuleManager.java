package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;

/**
 * 汇总员工：组织关系类型
 * 
 * @author ZhaoF
 * 
 */
public interface TreeStaffOrtRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeStaffOrRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo queryTreeStaffOrRulePageInfo(
			TreeStaffOrtRule queryTreeStaffOrRule, int currentPage, int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeStaffOrRule
	 */
	void removeTreeStaffOrRule(TreeStaffOrtRule treeStaffOrRule);

	/**
	 * 新增
	 * 
	 * @param list
	 */
	void addTreeStaffOrRuleList(List<TreeStaffOrtRule> list);

}
