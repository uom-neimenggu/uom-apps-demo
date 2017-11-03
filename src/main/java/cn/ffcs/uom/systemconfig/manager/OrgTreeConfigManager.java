package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;



public interface OrgTreeConfigManager {
	/**
	 * 加载组织树
	 * @param orgTree
	 */
	public void loadOrgTreeRootNode(final Listbox listbox);
	/**
	 * 加载组织树根节点
	 * @param orgTree
	 */
	public void loadOrgTreeRootNode(Tree orgTreeRootNode);
	/**
	 * 加载专业多选树根节点
	 * @param professionalTree
	 * @param orgTreeId
	 * @param aroleProfessionalTreeList
	 */
	public void loadProfessionalCheckTreeRoot(Tree professionalTree,String orgTreeId,List<AroleProfessionalTree> aroleProfessionalTreeList);
    /**
     * 加载多维专业多选树根节点
     * @param professionalTree
     * @param orgTreeId
     * @param aroleProfessionalTreeList
     */
    public void loadProfessionalCheckTreeRootDw(Tree professionalTree,String orgTreeId,List<AroleProfessionalTree> aroleProfessionalTreeList);	
	/**
	 * 获取组织树关联的组织类型
	 * @param orgId
	 */
	public List<OrgTreeConfig> findOrgType(Long orgId);
	
	/**
     * 获取组织树关联的组织类型
     * @param orgId
     */
    public PageInfo loadOrgTreeRootNodeDw(int currentPage, int pageSize);
}
