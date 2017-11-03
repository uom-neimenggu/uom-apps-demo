package cn.ffcs.uom.systemconfig.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.systemconfig.action.bean.OrgTreeConfigMainBean;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;

@Controller
@Scope("prototype")
public class OrgTreeConfigMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private OrgTreeConfigMainBean bean = new OrgTreeConfigMainBean();

	/**
	 * 选中的组织
	 */
	private MdsionOrgRelation mdsionOrgRelation;
	private MdsionOrgTree mdsionOrgTree;

	private OrgTreeConfigManager orgTreeConfigManager = (OrgTreeConfigManager) ApplicationContextUtil
			.getBean("orgTreeConfigManager");

	@Resource
	private MdsionOrgRelationManager mdsionOrgRelationManager;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);

		bean.getOrgTreeRootNode().addEventListener("onSelect",
				new EventListener() {
					public void onEvent(final Event e) throws Exception {
						mdsionOrgTree = (MdsionOrgTree) bean
								.getOrgTreeRootNode().getSelectedItem()
								.getValue();
						mdsionOrgRelation = MdsionOrgRelation.newInstance();
						mdsionOrgRelation.setMdsionOrgRelId(0L);
						mdsionOrgRelation.setOrgId(mdsionOrgTree.getOrgId());
						// onSelectMdsionOrgRelationTree(mdsionOrgRelation);
						onSelectMdsionOrgTree(mdsionOrgTree);
					}
				});
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$orgTreeConfigMainWin() throws Exception {

		// orgTreeConfigManager.loadOrgTreeRootNode(bean.getOrgTreeRootNode());
		onOrgTreeRootNodeListPaging();
		setOrgTreeButtonValid(true, false, false);
	}

	/**
	 * 点击tab
	 * 
	 * @throws Exception
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setSelectTab(clickTab);
				callTab();
			}
		}
	}

	public void onOrgTreeRootNodeListPaging() {
		PageInfo pageInfo = this.orgTreeConfigManager.loadOrgTreeRootNodeDw(
				this.bean.getOrgTreeRootNodeListPaging().getActivePage() + 1,
				this.bean.getOrgTreeRootNodeListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgTreeRootNode().setModel(dataList);
		this.bean.getOrgTreeRootNodeListPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabBox().getSelectedTab());
		}
		if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
		}

	}

	/**
	 * 选择组织事件
	 * 
	 * @throws Exception
	 */
	public void onSelectMdsionOrgRelationTree(MdsionOrgRelation mor)
			throws Exception {
		if (mor != null && mor.getOrgId() != null) {
			mor.setRelaOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
			mdsionOrgRelation = mdsionOrgRelationManager
					.queryMdsionOrgRelation(mor);
			Organization organization = mdsionOrgRelation.getOrganization();
			if (organization != null) {
				if (this.bean.getSelectTab() == null
						|| "orgRelaTab"
								.equals(this.bean.getSelectTab().getId())) {
					Map arg = new HashMap();
					arg.put("rootId", mor.getOrgId());
					arg.put("rela", mor.getRelaCd());
					Events.postEvent(OrganizationConstant.ON_SELECT_TREE_ROOT,
							this.bean.getOrgTreeExt(), arg);
				}
			}
		}
		setOrgTreeButtonValid(true, true, true);
	}

	public void onSelectMdsionOrgTree(MdsionOrgTree mor) throws Exception {
		if (mor != null) {
			// mor.setRelaOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
			// mdsionOrgRelation =
			// mdsionOrgRelationManager.queryMdsionOrgRelation(mor);
			Organization organization = mdsionOrgTree.getOrganization();
			if (organization != null) {
				if (this.bean.getSelectTab() == null
						|| "orgRelaTab"
								.equals(this.bean.getSelectTab().getId())) {
					Map arg = new HashMap();
					arg.put("rootId", mor.getOrgId());
					arg.put("rela", mor.getMdsionOrgRelTypeCd());
					Events.postEvent(OrganizationConstant.ON_SELECT_TREE_ROOT,
							this.bean.getOrgTreeExt(), arg);
				}
			}
		}
		setOrgTreeButtonValid(true, true, true);
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onAddOrgNode() throws Exception {
		final Map map = new HashMap();
		map.put("opType", "addOrgRootNode");
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_tree_node_edit.zul", null,
				map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map dataMap = (Map) event.getData();
				MdsionOrgRelation mdsionOrgRelation = (MdsionOrgRelation) dataMap
						.get("mdsionOrgRelation");
				long orgTreeId = (Long) dataMap.get("orgTreeId");
				/*
				 * Treechildren treechildren =
				 * bean.getOrgTreeRootNode().getTreechildren(); Treeitem titem =
				 * new Treeitem(); Treerow trow = new Treerow(); Treecell title
				 * = new
				 * Treecell(mdsionOrgRelation.getOrganization().getOrgName());
				 * Treecell rela = new Treecell(mdsionOrgRelation.getRelaCd());
				 * title.setParent(trow); rela.setParent(trow);
				 * trow.setParent(titem); TreeNodeImpl treeNodeImpl = new
				 * TreeNodeImpl<TreeNodeEntity>(mdsionOrgRelation);
				 * titem.setValue(treeNodeImpl); titem.setParent(treechildren);
				 */

				// --------------插入组织类型
				List<Pair<String, String>> relaResultArr = (List<Pair<String, String>>) dataMap
						.get("relaResultArr");
				for (Pair pair : relaResultArr) {
					OrgTreeConfig otc = new OrgTreeConfig();
					otc.setOrgTreeId(orgTreeId);
					otc.setOrgTypeCd((String) pair.getLeft());
					otc.addOnly();
				}
				// --------------
			}
		});
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onEditOrgNode() throws Exception {
		if (mdsionOrgTree != null) {
			Map arg = new HashMap();
			arg.put("orgRela", mdsionOrgRelation);
			Window win = (Window) Executions.createComponents(
					"/pages/system_config/org_tree_config_edit.zul", null, arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					Map dataMap = (Map) event.getData();
					MdsionOrgRelation orgRela = (MdsionOrgRelation) dataMap
							.get("orgRela");
					if (orgRela != null && orgRela.getOrgId() != null
							&& !StrUtil.isEmpty(orgRela.getRelaCd())) {
						// bean.getOrgTreeRootNode().clear();
						// 重新加载树根节点
						onOrgTreeRootNodeListPaging();
						/*
						 * orgTreeConfigManager.loadOrgTreeRootNode(bean.
						 * getOrgTreeRootNode()); //选中组织树 Collection<Treeitem>
						 * treeitem =
						 * bean.getOrgTreeRootNode().getItems();//.setSelectedItem
						 * (titem);//setValue(treeNodeImpl); for (Treeitem ti :
						 * treeitem) { MdsionOrgRelation or =
						 * (MdsionOrgRelation) ((TreeNodeImpl)
						 * ti.getValue()).getEntity(); if
						 * (or.getOrgId()==orgRela.getOrgId()){
						 * ti.setSelected(true); } } //重新加载树 Map arg = new
						 * HashMap(); arg.put("rootId", orgRela.getOrgId());
						 * arg.put("rela", orgRela.getRelaCd());
						 * Events.postEvent
						 * (OrganizationConstant.ON_SELECT_TREE_ROOT
						 * ,bean.getOrgTreeExt(), arg);
						 */

						setOrgTreeButtonValid(true, true, true);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要修改的组织树!", "提示信息");
			return;
		}
	}

	/**
	 * 删除
	 */
	public void onDelOrgNode() throws Exception {
		if (mdsionOrgTree != null) {
			ZkUtil.showQuestion("你确定要删除组织树吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						// mdsionOrgRelation.remove();
						// long orgTreeId =
						// mdsionOrgRelation.getOrgTreeIdByRoot(mdsionOrgRelation.getOrgId());
						// OrgTreeConfig.
						// mdsionOrgRelationManager.removeMdsionOrgRelation(mdsionOrgRelation);
						mdsionOrgTree.remove();
						/*
						 * Treechildren treechildren = (Treechildren)
						 * bean.getOrgTreeRootNode
						 * ().getSelectedItem().getParent();
						 * treechildren.removeChild
						 * (bean.getOrgTreeRootNode().getSelectedItem()); if
						 * (treechildren.getChildren().size() == 0) {
						 * treechildren.getParent().removeChild(treechildren); }
						 */
						onOrgTreeRootNodeListPaging();
						setOrgTreeButtonValid(true, false, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的组织树!", "提示信息");
			return;
		}
	}

	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setOrgTreeButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete) {
		this.bean.getAddOrgButton().setDisabled(!canAdd);
		this.bean.getEditOrgButton().setDisabled(!canEdit);
		this.bean.getDelOrgButton().setDisabled(!canDelete);
	}
}
