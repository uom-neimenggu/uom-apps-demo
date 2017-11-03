package cn.ffcs.uom.syslist.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;
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
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.SysList;

public class SysListTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private final String zul = "/pages/syslist/comp/syslist_tree_ext.zul";
	
	@Setter
	@Getter
	private SysList sysList;

	@Setter
	@Getter
	private SysListTree sysListTree;
	
	@Autowired
	private SysListManager sysListManager;
	
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

	public SysListTreeExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		this.addForward(SysListConstants.ON_CHANGE_TREE_STYLE, this, "onChangeTreeStyleResponse");
		this.addForward(SysListConstants.ON_SAVE_SYSLISTN, this, "onSaveSysList");
	}
	
	public void onSaveSysList(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			SysList sysList = (SysList) event.getOrigin().getData();
			this.sysListTree.getSelectedItem().setLabel(sysList.getSysName());
		}
	}
	
	/**
	 * 增加父节点
	 * 
	 * @throws Exception
	 */
	public void onAddRootNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		String opType = "addRootNode";
		this.openAddNodeWindow(opType);
	}
	
	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	public void onAddChildNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if (this.sysListTree.getSelectedItem() != null) {
			String opType = "addChildNode";
			//子节点不能新增节点
			if (sysList == null || !SysListConstants.IS_PARENT.equals(sysList.getIsParent())) {
				ZkUtil.showError("末级节点上面不能添加子节点,请重新选择节点!", "提示信息");
			} else {
				this.openAddNodeWindow(opType);
			}
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}
	
	public void onDelNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if (this.sysListTree.getSelectedItem() != null) {
			Treechildren treechildren = this.sysListTree.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点，不能删除。", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						sysListManager.removeSysList(sysList);
						Treechildren treechildren = (Treechildren) sysListTree.getSelectedItem().getParent();
						treechildren.removeChild(sysListTree.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						sysList = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(SysListConstants.ON_DEL_NODE_OK, SysListTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onSelect$sysListTree() throws Exception {
		sysList = (SysList) ((TreeNodeImpl) this.sysListTree.getSelectedItem().getValue()).getEntity();
		Events.postEvent(SysListConstants.ON_SELECT_TREE_SYSLIST, this, sysList);
	}
	
	@SuppressWarnings("rawtypes")
	public void onChangeTreeStyleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Boolean isChange = (Boolean) event.getOrigin().getData();
			if (isChange != null) {
				Treeitem ti = this.sysListTree.getSelectedItem();
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
	 * 打开界面
	 * 
	 * @param opType
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void openAddNodeWindow(String opType) throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("sysList", sysList);
		Window win = (Window) Executions.createComponents("/pages/syslist/syslist_edit.zul", this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				SysList sysList = (SysList) event.getData();
				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(sysList);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(sysList);
				}
			}
		});
	}
	
	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void onAddRootNodeResponse(SysList sysList) throws Exception {
		if(null == sysList){
			return;
		}
		Treechildren treechildren = this.sysListTree.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(sysList.getSysName());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(sysList);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onAddChildNodeResponse(SysList sysList) throws Exception {
		if(null == sysList){
			return;
		}
		Treechildren treechildren = this.sysListTree.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.sysListTree.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.sysListTree.getSelectedItem().setValue(parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(sysList.getLabel());
			/**
			 * 单位label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(sysList);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.sysListTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(sysList.getLabel());
			/**
			 * 单位类label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(sysList);
			titem.setValue(treeNodeImpl);
			titem.setParent(treechildren);
		}
	}
	
	/**
	 * 设置页面坐标
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition()  throws Exception {
		boolean canAddRoot = false;
		boolean canAddChild = false;
		boolean candelChild = false;
		
		if (PlatformUtil.isAdmin()) {
			canAddRoot = true;
			canAddChild = true;
			candelChild = true;
		} else {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_TREE_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_TREE_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_TREE_DEL)) {
				candelChild = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

}
