package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;

/**
 * 汇总员工：组织类型
 * 
 * @author ZhaoF
 * 
 */
public interface TreeStaffOtRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeStaffOtRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo queryTreeStaffOtRulePageInfo(TreeStaffOtRule queryTreeStaffOtRule,
			int currentPage, int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeStaffOtRule
	 */
	void removeTreeStaffOtRule(TreeStaffOtRule treeStaffOtRule);
	/**
	 * 新增
	 * @param treeStaffOtRuleList
	 */
	void addTreeStaffOtRuleList(List<TreeStaffOtRule> treeStaffOtRuleList);

}
