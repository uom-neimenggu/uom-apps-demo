package cn.ffcs.uom.organization.component;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class MdsionOrgRelationTree extends Tree implements IdSpace {
	/**
	 * 树类型
	 */
	@Getter
	@Setter
	private String orgTreeId;
	/**
	 * 指定的根组织父id
	 */
	@Getter
	@Setter
	private Long rootId;

	/**
	 * 数据权：各个页面的可配置根id是不一样的：内部是999999999以下的组织，中通服是9999999996以下的组织；
	 */
	@Getter
	@Setter
	private Long perPageDataPermissionRootOrgId;
	/**
	 * 关系类型值，从页面传入，内部-0101，营销-0401.
	 **/
	@Getter
	@Setter
	private String relaCdStr;

	/**
	 * 是否代理商页面 解决 组织树组织关系新增时 代理商出现两颗树问题
	 */
	@Getter
	@Setter
	private Boolean isAgent = false;
	/**
	 * 是否内部经营实体页面 解决 组织树组织关系新增时 内部经营实体出现两颗树问题
	 */
	@Getter
	@Setter
	public boolean isIbe = false;

	/**
	 * 构造函数.
	 */
	public MdsionOrgRelationTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}
	
	/**
	 * 创建
	 */
	public void onCreate() {
		this.setSclass("zt-tree-scroll");
        bindTree();
	}
	/**
	 * 绑定推导树
	 * 
	 * @throws Exception
	 */
	public void bindTree(Long orgTreeId) throws Exception {
		MdsionOrgRelation orgRel = new MdsionOrgRelation();
		treeCalcAction tca = new treeCalcAction();
		tca.createRealTimeTree(OrganizationConstant.ROOT_TREE_ORG_ID, orgTreeId);
		orgRel.setIsRoot(true);
		orgRel.setOrgTreeId(this.getOrgTreeId());
		orgRel.setRootId(rootId);
		orgRel.setTreeCalcVo(tca);
		orgRel.setIsAgent(isAgent);
		orgRel.setIsIbe(isIbe);
		final TreeNodeImpl<MdsionOrgRelation> treeNode = new TreeNodeImpl<MdsionOrgRelation>(
				orgRel);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}

	/**
	 * 非推导树
	 */
	public void bindTree() {
		MdsionOrgRelation orgRel = new MdsionOrgRelation();
		orgRel.setIsRoot(true);
		orgRel.setRootId(rootId);
		orgRel.setPerPageDataPermissionRootOrgId(perPageDataPermissionRootOrgId);
		orgRel.setRelaCdStr(relaCdStr);
		orgRel.setIsAgent(isAgent);
		orgRel.setIsIbe(isIbe);
		final TreeNodeImpl<MdsionOrgRelation> treeNode = new TreeNodeImpl<MdsionOrgRelation>(
				orgRel);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
		this.setLableStyle();
	}

	public void setLableStyle() {
		Collection<Treeitem> tis = this.getItems();
		for (Treeitem ti : tis) {
			MdsionOrgRelation mdsionOrgRelation = (MdsionOrgRelation) ((TreeNodeImpl) ti
					.getValue()).getEntity();
			if (mdsionOrgRelation != null) {
				Treecell cell = null;
				List list = ti.getTreerow().getChildren();
				if (list != null) {
					cell = (Treecell) list.get(0);
				}
				if (mdsionOrgRelation.isCompany()) {
					if (cell != null) {
						cell.setStyle("color:blue");
					}
				}
			}
		}
	}
}
