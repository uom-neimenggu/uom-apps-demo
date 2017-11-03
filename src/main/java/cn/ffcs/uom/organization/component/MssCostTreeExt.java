package cn.ffcs.uom.organization.component;

import java.util.Collection;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.JtMssCostRelation;

public class MssCostTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/msscost_tree_ext.zul";
	/**
	 * 组织树
	 */
	private JtMssCostRelationTree jtMssCostRelationTree;

	/**
	 * 选中的组织关系
	 */
	private JtMssCostRelation jtMssCostRelation;
	/**
	 * 选中的组织TreeItem
	 */
	private Treeitem selectedTreeitem;
	/**
	 * 选择树类型
	 */
	@Getter
	@Setter
	private Listbox treeSelect;
	/**
	 * 增加根节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton addRootButton;
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

	public MssCostTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 组织名称修改要改组织树节点名称
		 */
		MssCostTreeExt.this.addForward(
				OrganizationConstant.ON_SAVE_ORGANIZATION_INFO,
				MssCostTreeExt.this, "onSaveOrganiazationInfoResponse");
		
		this.setButtonValid(false, false, false);
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
	public void onSelect$jtMssCostRelationTree() throws Exception {
		selectedTreeitem = this.jtMssCostRelationTree.getSelectedItem();
		jtMssCostRelation = (JtMssCostRelation) ((TreeNodeImpl) this.jtMssCostRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (jtMssCostRelation != null
				&& jtMssCostRelation.getSubSetName() != null) {
		}
		this.setButtonValid(false, false, false);
		Events.postEvent(
						OrganizationConstant.ON_SELECT_COST_ORGANIZATION_TREE_REQUEST,
						this, jtMssCostRelation);
	}

	/**
	 * 查找时选择树
	 * 
	 * @throws Exception
	 */
	private void onSelectOrganizationRelationTree2() {
	    jtMssCostRelation = (JtMssCostRelation) ((TreeNodeImpl) this.jtMssCostRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (jtMssCostRelation != null
				&& jtMssCostRelation.getSubSetName() != null) {
		}
		Events.postEvent(
						OrganizationConstant.ON_SELECT_COST_ORGANIZATION_TREE_REQUEST,
						this, jtMssCostRelation);
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
		Treeitem currentSelectTreeItem = jtMssCostRelationTree
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
								onSelectOrganizationRelationTree2();
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
									onSelectOrganizationRelationTree2();
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
										onSelectOrganizationRelationTree2();
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
												onSelectOrganizationRelationTree2();
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
	private void setButtonValid(boolean canAddRoot, boolean canAddChild,
			boolean canDel) {
		this.addRootButton.setDisabled(!canAddRoot);
		this.addChildButton.setDisabled(!canAddChild);
		this.delChildButton.setDisabled(!canDel);
	}

	/**
	 * 供外层主动获取控件选择的组织
	 */
	public JtMssCostRelation getSelectOrganizationOrganization() {
		return jtMssCostRelation;
	}
	
	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException 
	 * @throws Exception 
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAddRoot = false;
		boolean canAddChild = false;
		boolean candelChild = false;
		
		if (PlatformUtil.isAdmin()) {
			canAddRoot = false;
			canAddChild = false;
			candelChild = false;
		} else if ("mssCostTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_TREE_ORG_ADD_ROOT)) {
				canAddRoot = false;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_TREE_ORG_ADD_CHILD)) {
				canAddChild = false;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_TREE_ORG_DEL)) {
				candelChild = false;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

}
