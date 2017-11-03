package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.OrgTree;

public interface OrgTreeManager {
	/**
	 * 新增
	 * 
	 * @param orgTree
	 * @return
	 */
	public void addOrgTree(OrgTree orgTree);
	
	/**
	 * 更新
	 * 
	 * @param orgTree
	 * @return
	 */
	public void updateOrgTree(OrgTree orgTree);

	/**
	 * 获取组织树列表
	 * 
	 * @param orgTree
	 * @return
	 */
	public List<OrgTree> queryOrgTreeList(OrgTree orgTree);

	/**
	 * 获取组织树
	 * @param orgTreeId
	 * @return
	 */
	public OrgTree getOrgTreeByOrgTreeId(Long orgTreeId);

	/**
	 * 分页查询组织树信息
	 * 
	 * @param queryOrgTree
	 * @param i
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryOrgTreePageInfoByOrgTree(OrgTree queryOrgTree, int i,
			int pageSize);

	/**
	 * 删除组织树信息
	 * 
	 * @param orgTree
	 */
	public void removeOrgTree(OrgTree orgTree);
}
