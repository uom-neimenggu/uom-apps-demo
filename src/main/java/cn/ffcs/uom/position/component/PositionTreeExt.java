package cn.ffcs.uom.position.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

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
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.Position;

public class PositionTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/position/comp/position_tree_ext.zul";
	/**
	 * 岗位树
	 */
	private PositionTree positionTree;
	/**
	 * manager
	 */
	private PositionManager positionManager = (PositionManager) ApplicationContextUtil
			.getBean("positionManager");
	/**
	 * 选中的岗位
	 */
	private Position position;
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

	public PositionTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 是否改变样式
		 */
		PositionTreeExt.this.addForward(PositionConstant.ON_CHANGE_TREE_STYLE,
				PositionTreeExt.this, "onChangeTreeStyleResponse");
		/**
		 * 岗位名称修改要改岗位树节点名称
		 */
		PositionTreeExt.this.addForward(PositionConstant.ON_SAVE_POSITION_NAME,
				PositionTreeExt.this, "onSavePosition");
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	public void onAddRootNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		Treechildren treechildren = this.positionTree.getTreechildren();
		/*
		 * if (treechildren != null && treechildren.getChildren() != null &&
		 * treechildren.getChildren().size() > 0) {
		 * ZkUtil.showError("存在根节点,不能添加", "提示信息"); return; }
		 */
		String opType = "addRootNode";
		this.openAddNodeWindow(opType);
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	private void onAddRootNodeResponse(Position position) throws Exception {
		Treechildren treechildren = this.positionTree.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(position.getPositionName());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(position);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	public void onAddChildNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (this.positionTree.getSelectedItem() != null) {
			String opType = "addChildNode";

			// 用于判断是否在第三级岗位上新增子节点
			if (position != null
					&& !StrUtil.isEmpty(position.getPositionCode())) {
				if (!(position.getPositionCode().trim().substring(4, 6)
						.equals("00"))) {
					ZkUtil.showError("第三级岗位上不能添加子节点,请重新选择节点!", "提示信息");
				} else {
					this.openAddNodeWindow(opType);
				}
			}

		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	private void onAddChildNodeResponse(Position position) throws Exception {
		Treechildren treechildren = this.positionTree.getSelectedItem()
				.getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.positionTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.positionTree.getSelectedItem().setValue(parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(position.getLabel());
			/**
			 * 单位label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					position);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.positionTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(position.getLabel());
			/**
			 * 单位类label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					position);
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
		map.put("position", position);
		Window win = (Window) Executions.createComponents(
				"/pages/position/position_edit.zul", this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Position position = (Position) event.getData();
				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(position);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(position);
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
				ActionKeys.DATA_OPERATING))
			return;

		if (this.positionTree.getSelectedItem() != null) {
			Treechildren treechildren = this.positionTree.getSelectedItem()
					.getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点,不能删除", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						positionManager.removePosition(position);
						Treechildren treechildren = (Treechildren) positionTree
								.getSelectedItem().getParent();
						treechildren.removeChild(positionTree.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						position = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(PositionConstant.ON_DEL_NODE_OK,
								PositionTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}

	/**
	 * 选择树
	 * 
	 * @throws Exception
	 */
	public void onSelect$positionTree() throws Exception {
		position = (Position) ((TreeNodeImpl) this.positionTree
				.getSelectedItem().getValue()).getEntity();
		Events.postEvent(PositionConstant.ON_SELECT_POSITION, this, position);
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
	 * 供外层主动获取控件选择的岗位
	 */
	public Position getSelectPosition() {
		return position;
	}

	/**
	 * 供外层使用是否是推导树
	 * 
	 * @return
	 */
	/*
	 * public boolean isDuceTree() { if (StrUtil.isEmpty((String)
	 * getTreeSelect().getSelectedItem() .getValue())) { return false; } return
	 * true; }
	 */

	/**
	 * 改变样式
	 * 
	 * @param event
	 */
	public void onChangeTreeStyleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Boolean isChange = (Boolean) event.getOrigin().getData();
			if (isChange != null) {
				Treeitem ti = this.positionTree.getSelectedItem();
				List list = ti.getTreerow().getChildren();
				if (list != null && list.size() > 0) {
					Treecell tc = (Treecell) list.get(0);
					if (tc != null) {
						if (isChange) {
							tc.setStyle("color:blue");
						} else {
							tc.setStyle(null);
						}
					}
				}
			}
		}
	}

	/**
	 * 判断名称是否要修改
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSavePosition(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Position position = (Position) event.getOrigin().getData();
			/**
			 * 修改名称
			 */
			this.positionTree.getSelectedItem().setLabel(
					position.getPositionName());
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
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAddRoot = true;
			canAddChild = true;
			canDel = true;
		} else if ("positionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_DEL)) {
				canDel = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(canDel);
	}
}
