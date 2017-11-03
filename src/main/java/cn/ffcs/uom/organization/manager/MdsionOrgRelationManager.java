package cn.ffcs.uom.organization.manager;

import java.util.List;
import java.util.Map;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.organization.model.MdsionOrgRelType;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;

public interface MdsionOrgRelationManager {

	/**
	 * 获取组织关系
	 * 
	 * @param organizationRelation
	 * @return
	 */
	public MdsionOrgRelation queryMdsionOrgRelation(
			MdsionOrgRelation mdsionOrgRelation);

	/**
	 * 获取多级组织关系列表
	 * 
	 * @param mdsionOrgRelation
	 * @return
	 */
	public List<MdsionOrgRelation> queryMdsionOrgRelationList(
			MdsionOrgRelation mdsionOrgRelation);

	/**
	 * 保存记录
	 * 
	 * @param mdsionOrgRelation
	 */
	public void addMdsionOrgRelation(MdsionOrgRelation mdsionOrgRelation);

	/**
	 * 修改组织关系类型
	 * 
	 * @param mdsionOrgRelId
	 */
	public void updateMdsionOrgRelType(MdsionOrgRelation mdsionOrgRelation);

	/**
	 * 加载内部树
	 * 
	 * @param org_id
	 * @param orgRelaRootTreeitem
	 */
	public void loadPoliticalTree(final String org_id,
			final Treeitem orgRelaRootTreeitem,
			final Map<String, List<Checkbox>> checkboxMaps);

	/**
	 * 复制内部组织关系
	 * 
	 * @param parentTreeitem
	 * @param copyOrgIds
	 */
	public boolean copyPoliticalOrgRela(Treeitem parentTreeitem,
			String copyOrgIds);

	/**
	 * 删除记录
	 * 
	 * @param mdsionOrgRelation
	 */
	public void removeMdsionOrgRelation(MdsionOrgRelation mdsionOrgRelation);

	/**
	 * 加载推导树
	 * 
	 * @param orgTreeId
	 *            组织树ID
	 * @param parentOrgId
	 *            上级组织ID
	 * @param relaCd
	 *            关系类型
	 * @param parentTreeitem
	 *            上级节点
	 * @param orgTreeRootId
	 *            根节点
	 */
	public void loadDerivationTree(String orgTreeId, String parentOrgId,
			String relaCd, Treeitem parentTreeitem, String orgTreeRootId,
			boolean isShowAll, long[] roleIds) throws Exception;

	public List<MdsionOrgRelation> querySubTreeMdsionOrgRelationList(
			Long orgId, String relaCd);

	public MdsionOrgTree getMdsionOrgTree(MdsionOrgRelation mdsionOrgRelation);
}
