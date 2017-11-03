package cn.ffcs.uom.roleauth.component;

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
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

public class AuthorityTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private final String zul = "/pages/role_auth/comp/role_auth_tree_ext.zul";
	
	@Setter
	@Getter
	private StaffAuthority authority;

	@Setter
	@Getter
	private AuthorityTree authorityTree;
	
	@Autowired
	private AuthorityManager authorityManager;
	
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

	public AuthorityTreeExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		this.addForward(RoleAuthConstants.ON_CHANGE_TREE_STYLE, this, "onChangeTreeStyleResponse");
		this.addForward(RoleAuthConstants.ON_SAVE_AUTHN, this, "onSaveAuth");
	}
	
	public void onSaveAuth(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			StaffAuthority authority = (StaffAuthority) event.getOrigin().getData();
			this.authorityTree.getSelectedItem().setLabel(authority.getAuthorityName());
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
		if (this.authorityTree.getSelectedItem() != null) {
			String opType = "addChildNode";
			//子节点不能新增节点
			if (authority == null || !RoleAuthConstants.IS_PARENT.equals(authority.getIsParent())) {
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
		if (this.authorityTree.getSelectedItem() != null) {
			Treechildren treechildren = this.authorityTree.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点，不能删除。", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						authorityManager.removeAuthority(authority);
						Treechildren treechildren = (Treechildren) authorityTree.getSelectedItem().getParent();
						treechildren.removeChild(authorityTree.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						authority = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(RoleAuthConstants.ON_DEL_NODE_OK, AuthorityTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onSelect$authorityTree() throws Exception {
		authority = (StaffAuthority) ((TreeNodeImpl) this.authorityTree.getSelectedItem().getValue()).getEntity();
		Events.postEvent(RoleAuthConstants.ON_SELECT_TREE_AUTH, this, authority);
	}
	
	@SuppressWarnings("rawtypes")
	public void onChangeTreeStyleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Boolean isChange = (Boolean) event.getOrigin().getData();
			if (isChange != null) {
				Treeitem ti = this.authorityTree.getSelectedItem();
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
		map.put("authority", authority);
		Window win = (Window) Executions.createComponents("/pages/role_auth/auth_edit.zul", this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				StaffAuthority authority = (StaffAuthority) event.getData();
				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(authority);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(authority);
				}
			}
		});
	}
	
	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	private void onAddRootNodeResponse(StaffAuthority authority) throws Exception {
		if(null == authority){
			return;
		}
		Treechildren treechildren = this.authorityTree.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(authority.getAuthorityName());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(authority);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onAddChildNodeResponse(StaffAuthority authority) throws Exception {
		if(null == authority){
			return;
		}
		Treechildren treechildren = this.authorityTree.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.authorityTree.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.authorityTree.getSelectedItem().setValue(parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(authority.getLabel());
			/**
			 * 单位label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(authority);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.authorityTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(authority.getLabel());
			/**
			 * 单位类label样式
			 */
			// if (organizationRelation.isCompany()) {
			// tcell.setStyle("color:blue");
			// }
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(authority);
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
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.AUTHORITY_TREE_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.AUTHORITY_TREE_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.AUTHORITY_TREE_DEL)) {
				candelChild = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

}
