package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;

/**
 * 树绑定配置
 * 
 * @author ZhaoF
 * 
 */
public interface TreeBindingRuleManager {
	/**
	 * 分页查询
	 * 
	 * @param queryTreeOrgOrRule
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryTreeBindingRulePageInfo(TreeBindingRule queryTreeBinding, int currentPage,int pageSize);

	/**
	 * 删除
	 * 
	 * @param treeOrgRelaTypeRule
	 */
	public void removeTreeBindingRule(TreeBindingRule treeBindingRule);

	/**
	 * 新增
	 * 
	 */
	public void addTreeBindingRule(TreeBindingRule treeBindingRule);
	
	/**
	 * 验证绑定树配置是否存在 
	 * @param treeBindingRule
	 * @return
	 */
	public boolean checkTreeBindingRuleIsExist(TreeBindingRule treeBindingRule);
	
	/**
	 * 根据业务树ID获取绑定的专业树配置
	 * @param orgTreeId
	 * @return
	 */
	public List<TreeBindingRule> findTreeBindingRule(Long orgTreeId);
	
	/**
	 * 根据组织树ID获取组织关系
	 * @param refTreeId
	 * @return
	 */
	public String getOrgRelaByRefTreeId(Long refTreeId);
    /**
     * 根据组织树根节点ID获取组织关系
     * @param refTreeId
     * @return
     */
    public String getOrgRelaByRefRootId(Long rootId);
	/**
	 * 根据配置
	 * @param refTreeId
	 * @return
	 */
	public TreeBindingRule getTreeBindingRuleByRefTreeId(Long refTreeId);

}
