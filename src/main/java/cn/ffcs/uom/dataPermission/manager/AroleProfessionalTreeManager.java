package cn.ffcs.uom.dataPermission.manager;

import java.util.List;

import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;

public interface AroleProfessionalTreeManager {
	/**
	 * 分页取类信息
	 * 
	 * @param aroleProfessionalTree
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public PageInfo queryPageInfoByRoleProfessionalTree(AroleProfessionalTree aroleProfessionalTree,int currentPage, int pageSize) throws Exception;
	
	/**
	 * 取角色组织列表
	 * 
	 * @param aroleProfessionalTree
	 * @return
	 * @throws Exception
	 */
	public List<AroleProfessionalTree> queryRoleProfessionalTreeList(AroleProfessionalTree aroleProfessionalTree) throws Exception;
	
	/**
	 * 获取专业树某个节点下的角色权限列表
	 * @param aroleProfessionalTree
	 * @return
	 * @throws Exception
	 */
	public List<AroleProfessionalTree> findProfessionalTreeAuthByNode(AroleProfessionalTree aroleProfessionalTree) throws Exception;
	
	/**
	 * 删除记录
	 * 
	 * @param aroleProfessionalTree
	 */
	public void removeRoleProfessionalTree(AroleProfessionalTree aroleProfessionalTree);

	/**
	 * 保存记录
	 * 
	 * @param aroleProfessionalTree
	 */
	public void addRoleProfessionalTree(AroleProfessionalTree aroleProfessionalTree);
	
	/**
	 * 保存专业树权限
	 * @param orgTreeRootNode
	 * @param aroleProfessionalTree
	 * @param aroleProfessionalTreeList
	 */
	public void saveRoleProfessionalTree(Tree orgTreeRootNode,AroleProfessionalTree aroleProfessionalTree,List<AroleProfessionalTree> aroleProfessionalTreeList);
	
	
	
}
