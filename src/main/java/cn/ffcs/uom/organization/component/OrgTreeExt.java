package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Pair;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelType;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.Organization;

public class OrgTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/org_tree_ext.zul";
	/**
	 * 组织树
	 */
	private MdsionOrgRelationTree mdsionOrgRelationTree;
	/**
	 * manager
	 */
	private MdsionOrgRelationManager mdsionOrgRelationManager = (MdsionOrgRelationManager) ApplicationContextUtil
			.getBean("mdsionOrgRelationManager");
	/**
	 * manager
	 */
	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");
	/**
	 * 选中的组织关系
	 */
	private MdsionOrgRelation mdsionOrgRelation;
	/**
	 * 选中的组织TreeItem
	 */
	private Treeitem selectedTreeitem;
	/**
	 * 选中的组织
	 */
	private Organization organization;
	/**
	 * 选择树类型
	 */
	@Getter
	@Setter
	private Listbox treeSelect;
	/**
	 * 增加根节点按钮
	 */
	/*@Getter
	@Setter
	private Toolbarbutton addRootButton;*/
	/**
	 * 复制内部节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton copyChildButton;
	/**
	 * 增加孩子节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton addChildButton;
	/**
	 * 删除节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton delChildButton;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 父节点类型
	 */
	private String parentNodeRelaCd;
	
	/**
	 * 父节点Id
	 */
	private Long parentNodeId;
	
	
	public OrgTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 组织名称修改要改组织树节点名称
		 */
		this.addForward(OrganizationConstant.ON_SAVE_ORGANIZATION_INFO,OrgTreeExt.this, "onSaveOrganiazationInfoResponse");
		this.addForward(OrganizationConstant.ON_SELECT_TREE_ROOT,OrgTreeExt.this, "onSelTreeRootResponse");
		
		this.setButtonValid(false, false, false);
		this.getCopyChildButton().setVisible(false);
	}

	/**
	 * 监听组织根节点选择事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelTreeRootResponse(ForwardEvent event)throws Exception {
		if (event.getOrigin().getData() != null) {
			Map arg = (Map)event.getOrigin().getData();
			parentNodeRelaCd = arg.get("rela").toString();
			parentNodeId = Long.valueOf(arg.get("rootId").toString());
			loadOrgRelationTree();
		}
	}

	
	public void loadOrgRelationTree(){
		MdsionOrgRelation orgRel = new MdsionOrgRelation();
		orgRel.setIsRoot(true);
		orgRel.setRootId(null);
		orgRel.setPerPageDataPermissionRootOrgId(parentNodeId);
		orgRel.setRelaCdStr(parentNodeRelaCd);
		orgRel.setIsAgent(false);
		orgRel.setIsIbe(false);
		final TreeNodeImpl<MdsionOrgRelation> treeNode = new TreeNodeImpl<MdsionOrgRelation>(orgRel);
		treeNode.readChildren();
		mdsionOrgRelationTree.setModel(new BaseTreeModel(treeNode));
		mdsionOrgRelationTree.setTreeitemRenderer(new BaseTreeitemRenderer());
		mdsionOrgRelationTree.setLableStyle();
		this.setButtonValid(false, false, false);
	}
	
	

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	public void onAddChildNode() throws Exception {
		/**
		 * 操作数据权限
		 */
		/*if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}*/
		if (this.mdsionOrgRelationTree.getSelectedItem() != null) {
			String opType = "addOrgTreeChildNode";
			this.openAddNodeWindow(opType);
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}
	/**
	 * 复制内部节点
	 * 
	 * @throws Exception
	 */
	public void onCopyChildNode() throws Exception {
		/**
		 * 操作数据权限
		 */
		/*if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}*/
		final Treeitem treeitem = this.mdsionOrgRelationTree.getSelectedItem();
		if (treeitem != null) {
			Window win = (Window) Executions.createComponents("/pages/organization/political_tree_sel_ext.zul", this, null);
			win.doModal();
			win.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String ids = (String)event.getData();
						if(mdsionOrgRelationManager.copyPoliticalOrgRela(treeitem, ids)){
							//重新加载树
							loadOrgRelationTree();
							ZkUtil.showError("复制子节点成功！", "提示信息");
						}
					}
				}
			});
		} else {
			ZkUtil.showError("请选择节点!", "提示信息");
		}
	}

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	private void onAddChildNodeResponse(
			MdsionOrgRelation mdsionOrgRelation) throws Exception {
		Treechildren treechildren = this.mdsionOrgRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.mdsionOrgRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.mdsionOrgRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(mdsionOrgRelation.getLabel());
			/**
			 * 单位label样式
			 */
			if (mdsionOrgRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					mdsionOrgRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild
					.setParent(this.mdsionOrgRelationTree
							.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(mdsionOrgRelation.getLabel());
			/**
			 * 单位类label样式
			 */
			if (mdsionOrgRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					mdsionOrgRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(treechildren);
		}
	}

	/**
	 * 打开界面
	 * 
	 * @param opType
	 */
	private void openAddNodeWindow(String opType) throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("organization", organization);
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_tree_node_edit.zul", this,
				map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map dataMap = (Map) event.getData();
				MdsionOrgRelation mdsionOrgRelation = MdsionOrgRelation.newInstance();
				String orgId = (String) dataMap.get("orgId");
				if (StrUtil.isEmpty(orgId)) {
					ZkUtil.showError("获取不到组织ID", "提示信息");
					return;
				}
				if (StrUtil.isEmpty(parentNodeRelaCd)) {
					ZkUtil.showError("默认关联关系不能为空", "提示信息");
					return;
				}
				mdsionOrgRelation.setOrgId(Long.valueOf(orgId));
				mdsionOrgRelation.setRelaOrgId(organization.getOrgId());
				mdsionOrgRelation.setRelaCd(parentNodeRelaCd);
				List<MdsionOrgRelation> queryMdsionOrgRelationList = mdsionOrgRelationManager.queryMdsionOrgRelationList(mdsionOrgRelation);
				if (queryMdsionOrgRelationList != null && queryMdsionOrgRelationList.size() > 0) {
					ZkUtil.showError("该组织已存在该关系", "提示信息");
					return;
				}
				mdsionOrgRelationManager.addMdsionOrgRelation(mdsionOrgRelation);
			
				
				//--------------插入其它关联关系
				List<Pair<String, String>> relaResultArr = (List<Pair<String, String>>) dataMap.get("relaResultArr");
				for (Pair pair : relaResultArr) {
					mdsionOrgRelation.setRelaCd((String) pair.getLeft());
					queryMdsionOrgRelationList = mdsionOrgRelationManager.queryMdsionOrgRelationList(mdsionOrgRelation);
					if (queryMdsionOrgRelationList != null && queryMdsionOrgRelationList.size() > 0) {
						continue;
					}
					MdsionOrgRelType mort = new MdsionOrgRelType();
					mort.setMdsionOrgRelId(mdsionOrgRelation.getId());
					mort.setMdsionOrgRelTypeCd((String) pair.getLeft());
					mort.add();
					//mdsionOrgRelation.add();
				}
				//--------------
				
				
				Treechildren treechildren = mdsionOrgRelationTree.getSelectedItem().getTreechildren();
				
				if (treechildren == null) {// 没有下级
					TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) mdsionOrgRelationTree.getSelectedItem().getValue();
					parentTreeNodeImpl.setChildren(null);
					mdsionOrgRelationTree.getSelectedItem().setValue(parentTreeNodeImpl);
					Treechildren tchild = new Treechildren();
					Treeitem titem = new Treeitem();
					Treerow trow = new Treerow();
					Treecell tcell = new Treecell(mdsionOrgRelation.getOrganization().getOrgName());
					//单位label样式
					if (mdsionOrgRelation.isCompany()) {
						tcell.setStyle("color:blue");
					}
					tcell.setParent(trow);
					trow.setParent(titem);
					TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(mdsionOrgRelation);
					titem.setValue(treeNodeImpl);
					titem.setParent(tchild);
					tchild.setParent(mdsionOrgRelationTree.getSelectedItem());
				} else {// 已存在下级
					Treeitem titem = new Treeitem();
					Treerow trow = new Treerow();
					Treecell tcell = new Treecell(mdsionOrgRelation.getOrganization().getOrgName());
					//单位label样式
					if (mdsionOrgRelation.isCompany()) {
						tcell.setStyle("color:blue");
					}
					tcell.setParent(trow);
					trow.setParent(titem);
					TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(mdsionOrgRelation);
					titem.setValue(treeNodeImpl);
					titem.setParent(treechildren);
				}
			}
		});
	}

	/**
	 * 删除节点
	 * 
	 * @throws Exception
	 */
	public void onDelNode() throws Exception {
	/*	if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}*/
		if (this.mdsionOrgRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.mdsionOrgRelationTree
					.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点,不能删除", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
//						mdsionOrgRelation.remove();
						mdsionOrgRelation.setRelaCd(parentNodeRelaCd);
						mdsionOrgRelationManager.removeMdsionOrgRelation(mdsionOrgRelation);
						Treechildren treechildren = (Treechildren) mdsionOrgRelationTree.getSelectedItem().getParent();
						treechildren.removeChild(mdsionOrgRelationTree.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						mdsionOrgRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(OrganizationConstant.ON_DEL_NODE_OK,OrgTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}

	/**
	 * 查询节点
	 * 
	 * @throws Exception
	 */
	public void onSearchNode() throws Exception {
		/**
		 * 数据权
		 */
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (selectedTreeitem == null) {
			ZkUtil.showError("请选择你要查询节点", "提示信息");
			return;
		}
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_tree_node_search.zul", this,
				null);
		win.setTitle("查找");
		win.doOverlapped();
		win.setLeft("30%");
		win.setTop("30%");
		win.addEventListener("onSearchOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map dataMap = (Map) event.getData();
				String orgName = (String) dataMap.get("orgName");
				searchNext(orgName);
			}
		});
	}

	/**
	 * 点击时选择树
	 * 
	 * @throws Exception
	 */
	public void onSelect$mdsionOrgRelationTree() throws Exception {
		if (this.mdsionOrgRelationTree != null && this.mdsionOrgRelationTree.getSelectedItem() != null) {
			mdsionOrgRelation = (MdsionOrgRelation) ((TreeNodeImpl) this.mdsionOrgRelationTree.getSelectedItem().getValue()).getEntity();
			if (mdsionOrgRelation != null && mdsionOrgRelation.getOrgId() != null) {
				organization = mdsionOrgRelation.getOrganization();
			}
			this.setButtonValid(true, true, true);
			Events.postEvent(OrganizationConstant.ON_SELECT_MARKETING_ORGANIZATION_TREE_REQUEST,this, mdsionOrgRelation);
		}
	}

	/**
	 * 查找时选择树
	 * 
	 * @throws Exception
	 */
	private void onSelectMdsionOrgRelationTree() {
		mdsionOrgRelation = (MdsionOrgRelation) ((TreeNodeImpl) this.mdsionOrgRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (mdsionOrgRelation != null
				&& mdsionOrgRelation.getOrgId() != null) {
			organization = mdsionOrgRelation.getOrganization();
		}
		Events
				.postEvent(
						OrganizationConstant.ON_SELECT_MARKETING_ORGANIZATION_TREE_REQUEST,
						this, mdsionOrgRelation);
	}

	/**
	 * 查找时：选中查找的组织,并支持下一个
	 * 
	 * @param orgName
	 */
	private void searchNext(String orgName) {
		/**
		 * 选中要查询下级的节点
		 */
		selectedTreeitem.setOpen(true);
		Treechildren tc = selectedTreeitem.getTreechildren();
		/**
		 * 当前选中的节点
		 */
		Treeitem currentSelectTreeItem = mdsionOrgRelationTree
				.getSelectedItem();
		if (tc != null) {
			Collection<Treeitem> collection = tc.getItems();
			if (collection != null && collection.size() > 0) {
				int count = 0;
				for (Treeitem ti : collection) {
					if (ti != null) {
						if (ti.getLabel().contains(orgName)) {
							count++;
						}
					}
				}
				if (count == 0) {
					ZkUtil.showInformation("该节点下没有该组织", "提示信息");
					return;
				} else if (count == 1) {
					if (!selectedTreeitem.equals(currentSelectTreeItem)
							&& currentSelectTreeItem.getLabel().contains(
									orgName)) {
						ZkUtil.showInformation("该节点下没有其他符合条件的组织", "提示信息");
						return;
					}
					for (Treeitem ti : collection) {
						if (ti != null) {
							if (ti.getLabel().contains(orgName)) {
								ti.setSelected(true);
								ti.focus();
								onSelectMdsionOrgRelationTree();
								return;
							}
						}
					}
				} else {
					/**
					 * 多个组织符合条件：第一次查询
					 */
					if (selectedTreeitem.equals(currentSelectTreeItem)) {
						for (Treeitem ti : collection) {
							if (ti != null) {
								if (ti.getLabel().contains(orgName)) {
									ti.setSelected(true);
									ti.focus();
									onSelectMdsionOrgRelationTree();
									return;
								}
							}
						}
					} else {
						/**
						 * 多个组织符合条件:继续往下查询
						 */
						/**
						 * 用来判断是否到当前的匹配记录了
						 */
						boolean flag = false;
						/**
						 * 用来判断第几条
						 */
						int falgCount = 0;

						for (Treeitem ti : collection) {
							if (ti != null) {
								if (flag) {
									if (ti.getLabel().contains(orgName)) {
										ti.setSelected(true);
										ti.focus();
										onSelectMdsionOrgRelationTree();
										return;
									} else {
										continue;
									}
								}
								/**
								 * 查询到当条了
								 */
								if (ti.equals(currentSelectTreeItem)) {
									flag = true;
									if (ti.getLabel().contains(orgName)) {
										falgCount++;
									}
								} else {
									if (ti.getLabel().contains(orgName)) {
										falgCount++;
									}
								}
								/**
								 * 已经是最后一条了:从第一条查起
								 */
								if (falgCount == count) {
									for (Treeitem temp : collection) {
										if (temp != null) {
											if (temp.getLabel().contains(
													orgName)) {
												temp.setSelected(true);
												ti.focus();
												onSelectMdsionOrgRelationTree();
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				ZkUtil.showInformation("该节点为末级节点,请重新选择你要查询的节点", "提示信息");
				return;
			}
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAddRoot
	 * @param canAddChild
	 * @param canDel
	 */
	private void setButtonValid(boolean canCopyChild, boolean canAddChild,boolean canDel) {
		this.copyChildButton.setDisabled(!canCopyChild);
		this.addChildButton.setDisabled(!canAddChild);
		this.delChildButton.setDisabled(!canDel);
	}

	/**
	 * 供外层主动获取控件选择的组织
	 */
	public MdsionOrgRelation getSelectOrganizationOrganization() {
		return mdsionOrgRelation;
	}

	/**
	 * 判断名称是否要修改
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveOrganiazationInfoResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			Organization org = (Organization) event.getOrigin().getData();
			/**
			 * 修改名称
			 */
			this.mdsionOrgRelationTree.getSelectedItem().setLabel(
					org.getOrgName());
			/**
			 * 修改排序
			 */
			if (org.getOrgPriority() != null
					&& !org.getOrgPriority().equals(
							organization.getOrgPriority())) {
				organization = org;
				this.changeOrgSeq();
			}
		}
	}

	/**
	 * 修改组织排序
	 * 
	 * @param event
	 * @throws Exception
	 */
	private void changeOrgSeq() throws Exception {
		Treeitem selectItem = this.mdsionOrgRelationTree
				.getSelectedItem();
		Treechildren tc = (Treechildren) selectItem.getParent();
		if (tc != null) {
			Collection<Treeitem> tiList = tc.getItems();
			Object[] array = tiList.toArray();
			Treeitem pti = (Treeitem) tc.getParent();
			ArrayList<TreeNodeEntity> list = ((TreeNodeImpl) pti.getValue())
					.getEntity().getChildren();
			if (pti != null) {
				pti.removeChild(tc);
				Treechildren tchild = new Treechildren();
				if (list != null && list.size() > 0) {
					for (TreeNodeEntity tne : list) {
						MdsionOrgRelation dbor = (MdsionOrgRelation) tne;
						if (dbor != null && dbor.getOrgId() != null) {
							for (Object o : array) {
								Treeitem ti = (Treeitem) o;
								MdsionOrgRelation or = (MdsionOrgRelation) ((TreeNodeImpl) ti
										.getValue()).getEntity();
								if (or != null
										&& dbor.getOrgId()
												.equals(or.getOrgId())) {
									ti.setParent(tchild);
									break;
								}
							}
						}
					}
				}
				tchild.setParent(pti);
			}
		}
	}
	
	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException 
	 * @throws Exception 
	 */
	public void setPagePosition(String page) throws Exception {
	/*	boolean canCopyRoot = false;
		boolean canAddChild = false;
		boolean candelChild = false;
		
		if (PlatformUtil.isAdmin()) {
			canCopyRoot = true;
			canAddChild = true;
			candelChild = true;
		} else if ("marketingTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_TREE_ORG_ADD_ROOT)) {
				canCopyRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_TREE_ORG_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_TREE_ORG_DEL)) {
				candelChild = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);*/
	}

}
