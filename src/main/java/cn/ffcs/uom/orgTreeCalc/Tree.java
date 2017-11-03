package cn.ffcs.uom.orgTreeCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.vo.OrganizationRelationNameVo;

public class Tree {
	@Getter
	private TreeNode root;
	@Getter
	private HashMap<Long, TreeNode> allNodes = new HashMap<Long, TreeNode>();
	
	/**
	 * 访问节点列表，以orgId为value,用于判断树中是否存在循环
	 */
	private Set<Long> accessNodeList = new HashSet<Long>();

	/*
	 * SELECT b.org_name, a.org_id, a.rela_org_id, a.org_rel_id FROM
	 * organization_relation a, organization b WHERE a.org_id = b.org_id START
	 * WITH a.rela_org_id = 0 CONNECT BY PRIOR a.org_id = a.rela_org_id
	 */
	/**
	 * 
	 * @param data 经过CONNECT BY语句一次排序后的组织关系列表，能提高执行速度
	 */
	public Tree(List<OrganizationRelation> data) {
		root = new TreeNode();
		root.setValue("_root");
		try {
			root.setChildNodes(buildTree(data.get(0).getRelaOrgId(), data, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构建并返回orgId的下级节点
	 * @param orgId
	 * @param list 经过CONNECT BY语句一次排序后的组织关系列表，能提高执行速度
	 * @param idx 标记list当前访问位置
	 * @return 以关系类型为key对TreeNode分组
	 * @throws Exception
	 */
	private HashMap<String, List<TreeNode>> buildTree(Long orgId,
			List<OrganizationRelation> list, List<Integer> idx)
			throws Exception {
		if (accessNodeList.contains(orgId)) {
			throw new Exception("存在循环节点");
		}
		accessNodeList.add(orgId);

		HashMap<String, List<TreeNode>> childrens = new HashMap<String, List<TreeNode>>();
		//初始化idx
		if (idx == null) {
			idx = new ArrayList<Integer>();
			idx.add(0);
		}
		while (list.size() > idx.get(0)) {
			OrganizationRelation item = list.get(idx.get(0));
			String relaId = item.getRelaCd();
			/*
			 * 基于CONNECT BY的排序结果
			 * 判断item的关联组织即父级节点是否是orgId，如果不是则结束本轮递归
			 */
			if (item.getRelaOrgId().equals(orgId)) {
				idx.set(0, idx.get(0) + 1);
				TreeNode node = allNodes.get(item.getOrgId());
				if (node == null) {
					node = new TreeNode();
					allNodes.put(item.getOrgId(), node);

					node.setValue(StrUtil.strnull(item.getOrgId()));
				}
				node.getChildNodes().putAll(
						buildTree(item.getOrgId(), list, idx));
				List<TreeNode> childList = childrens.get(relaId);
				if (childList == null) {
					childList = new ArrayList<TreeNode>();
					childrens.put(relaId, childList);
				}
				if (!childList.contains(node)) {
					childList.add(node);
				}
			} else {
				break;
			}
		}

		accessNodeList.remove(orgId);
		return childrens;
	}
}
