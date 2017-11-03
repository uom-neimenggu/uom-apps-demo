package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class OssTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/oss_tree_ext.zul";
	/**
	 * 组织树
	 */
	private OrganizationRelationTree ossOrganizationRelationTree;
	/**
	 * manager
	 */
	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	private MdsionOrgRelationManager mdsionOrgRelationManager = (MdsionOrgRelationManager) ApplicationContextUtil
			.getBean("mdsionOrgRelationManager");
	/**
	 * 选中的组织关系
	 */
	private OrganizationRelation organizationRelation;
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

	public OssTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 组织名称修改要改组织树节点名称
		 */
		OssTreeExt.this.addForward(
				OrganizationConstant.ON_SAVE_ORGANIZATION_INFO,
				OssTreeExt.this, "onSaveOrganiazationInfoResponse");

		this.setButtonValid(false, false, false);
		ossOrganizationRelationTree.bindTree();
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	public void onAddRootNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		Treechildren treechildren = this.ossOrganizationRelationTree
				.getTreechildren();
		if (treechildren != null && treechildren.getChildren() != null
				&& treechildren.getChildren().size() > 0) {
			ZkUtil.showError("存在根节点,不能添加", "提示信息");
			return;
		}
		String opType = "addOssRootNode";
		this.openAddNodeWindow(opType);
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	private void onAddRootNodeResponse(OrganizationRelation organizationRelation)
			throws Exception {
		Treechildren treechildren = this.ossOrganizationRelationTree
				.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treecell treecell = new Treecell("新增节点");
		Treerow treerow = new Treerow();
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
				organizationRelation);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
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
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (this.ossOrganizationRelationTree.getSelectedItem() != null) {
			String opType = "addOssChildNode";
			this.openAddNodeWindow(opType);
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	private void onAddChildNodeResponse(
			OrganizationRelation organizationRelation) throws Exception {
		Treechildren treechildren = this.ossOrganizationRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.ossOrganizationRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.ossOrganizationRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(organizationRelation.getLabel());
			/**
			 * 单位label样式
			 */
			if (organizationRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					organizationRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.ossOrganizationRelationTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(organizationRelation.getLabel());
			/**
			 * 单位类label样式
			 */
			if (organizationRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					organizationRelation);
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
				OrganizationRelation organizationRelation = (OrganizationRelation) dataMap
						.get("organizationRelation");
				if (map.get("opType").equals("addOssRootNode")) {
					onAddRootNodeResponse(organizationRelation);
				} else if (map.get("opType").equals("addOssChildNode")) {
					onAddChildNodeResponse(organizationRelation);
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
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		if (this.ossOrganizationRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.ossOrganizationRelationTree
					.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点,不能删除", "提示信息");
				return;
			}

			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String reason = (String) event.getData();

						organizationRelation.setReason(reason);

						organizationRelationManager
								.removeOrganizationRelation(organizationRelation);
						/**
						 * 删除多维组织关系
						 */
						MdsionOrgRelation orgRela = MdsionOrgRelation
								.newInstance();
						orgRela.setRelaCd(organizationRelation.getRelaCd());

						MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
								.getMdsionOrgTree(orgRela);

						if (mdsionOrgTree != null
								&& mdsionOrgTree.getMdsionOrgRelTypeCd()
										.equals(organizationRelation
												.getRelaCd())) {
							orgRela.setOrgId(organizationRelation.getOrgId());
							orgRela.setRelaOrgId(organizationRelation
									.getRelaOrgId());
							orgRela = mdsionOrgRelationManager
									.queryMdsionOrgRelation(orgRela);
							if (orgRela != null
									&& orgRela.getMdsionOrgRelId() != null) {
								mdsionOrgRelationManager
										.removeMdsionOrgRelation(orgRela);
							}
						}
						Treechildren treechildren = (Treechildren) ossOrganizationRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(ossOrganizationRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						organizationRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(OrganizationConstant.ON_DEL_NODE_OK,
								OssTreeExt.this, null);

						Messagebox.show("删除节点成功");
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
	public void onSelect$ossOrganizationRelationTree() throws Exception {
		selectedTreeitem = this.ossOrganizationRelationTree.getSelectedItem();
		organizationRelation = (OrganizationRelation) ((TreeNodeImpl) this.ossOrganizationRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		}
		this.setButtonValid(false, true, true);
		Events.postEvent(
				OrganizationConstant.ON_SELECT_ORGANIZATION_TREE_REQUEST, this,
				organizationRelation);
	}

	/**
	 * 查找时选择树
	 * 
	 * @throws Exception
	 */
	private void onSelectOrganizationRelationTree() {
		organizationRelation = (OrganizationRelation) ((TreeNodeImpl) this.ossOrganizationRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		}
		Events.postEvent(
				OrganizationConstant.ON_SELECT_POLITICAL_ORGANIZATION_TREE_REQUEST,
				this, organizationRelation);
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
		Treeitem currentSelectTreeItem = ossOrganizationRelationTree
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
								onSelectOrganizationRelationTree();
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
									onSelectOrganizationRelationTree();
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
										onSelectOrganizationRelationTree();
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
												onSelectOrganizationRelationTree();
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
	public OrganizationRelation getSelectOrganizationOrganization() {
		return organizationRelation;
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
			this.ossOrganizationRelationTree.getSelectedItem().setLabel(
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
		Treeitem selectItem = this.ossOrganizationRelationTree
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
						OrganizationRelation dbor = (OrganizationRelation) tne;
						if (dbor != null && dbor.getOrgId() != null) {
							for (Object o : array) {
								Treeitem ti = (Treeitem) o;
								OrganizationRelation or = (OrganizationRelation) ((TreeNodeImpl) ti
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
		boolean canAddRoot = false;
		boolean canAddChild = false;
		boolean candelChild = false;

		if (PlatformUtil.isAdmin()) {
			canAddRoot = true;
			canAddChild = true;
			candelChild = true;
		} else if ("ossTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OSS_TREE_ORG_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OSS_TREE_ORG_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OSS_TREE_ORG_DEL)) {
				candelChild = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

}
