package cn.ffcs.uom.accconfig.component;

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
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;

public class AccConfigTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private final String zul = "/pages/accconfig/comp/accconfig_tree_ext.zul";
	
	@Setter
	@Getter
	private AccConfig accConfig;

	@Setter
	@Getter
	private AccConfigTree accConfigTree;
	
	@Autowired
	private AccConfigManager accConfigManager;
	
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

	public AccConfigTreeExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		this.addForward(AccConfigConstants.ON_CHANGE_TREE_STYLE, this, "onChangeTreeStyleResponse");
		this.addForward(AccConfigConstants.ON_SAVE_ACCCONFIGN, this, "onSaveAccConfig");
	}
	
	public void onSaveAccConfig(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			AccConfig accConfig = (AccConfig) event.getOrigin().getData();
			this.accConfigTree.getSelectedItem().setLabel(accConfig.getAccName());
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
		if (this.accConfigTree.getSelectedItem() != null) {
			String opType = "addChildNode";
			//子节点不能新增节点
			if (accConfig == null || !AccConfigConstants.IS_PARENT.equals(accConfig.getIsParent())) {
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
		if (this.accConfigTree.getSelectedItem() != null) {
			Treechildren treechildren = this.accConfigTree.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点，不能删除。", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						accConfigManager.removeAccConfig(accConfig);
						Treechildren treechildren = (Treechildren) accConfigTree.getSelectedItem().getParent();
						treechildren.removeChild(accConfigTree.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						accConfig = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(AccConfigConstants.ON_DEL_NODE_OK, AccConfigTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onSelect$accConfigTree() throws Exception {
		accConfig = (AccConfig) ((TreeNodeImpl) this.accConfigTree.getSelectedItem().getValue()).getEntity();
		Events.postEvent(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, this, accConfig);
	}
	
	@SuppressWarnings("rawtypes")
	public void onChangeTreeStyleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Boolean isChange = (Boolean) event.getOrigin().getData();
			if (isChange != null) {
				Treeitem ti = this.accConfigTree.getSelectedItem();
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
		map.put("accConfig", accConfig);
		Window win = (Window) Executions.createComponents("/pages/accconfig/acc_edit.zul", this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				AccConfig accConfig = (AccConfig) event.getData();
				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(accConfig);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(accConfig);
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
	private void onAddRootNodeResponse(AccConfig accConfig) throws Exception {
		if(null == accConfig){
			return;
		}
		Treechildren treechildren = this.accConfigTree.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(accConfig.getAccName());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(accConfig);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onAddChildNodeResponse(AccConfig accConfig) throws Exception {
		if(null == accConfig){
			return;
		}
		Treechildren treechildren = this.accConfigTree.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.accConfigTree.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.accConfigTree.getSelectedItem().setValue(parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(accConfig.getLabel());
			/**
			 * 单位label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(accConfig);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.accConfigTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(accConfig.getLabel());
			/**
			 * 单位类label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(accConfig);
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
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_TREE_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_TREE_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_TREE_DEL)) {
				candelChild = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

}
