package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;

/**
 * 汇总员工：员工类型
 * 
 * @author ZhaoF
 * 
 */
public interface TreeStaffSftRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeStaffSftRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo queryTreeStaffSftRulePageInfoByTreeStaffSftRule(
			TreeStaffSftRule queryTreeStaffSftRule, int currentPage,
			int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeStaffSftRule
	 */
	void removeTreeStaffSftRule(TreeStaffSftRule treeStaffSftRule);

	/**
	 * 新增
	 * 
	 * @param treeStaffSftRuleList
	 */
	void addTreeStaffSftRule(List<TreeStaffSftRule> treeStaffSftRuleList);

}
