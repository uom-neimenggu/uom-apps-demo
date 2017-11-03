package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.dataPermission.manager.AroleProfessionalTreeManager;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.orgTreeCalc.manager.TreeBindingRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class MultidimensionalTreeExt extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/multidimensional_tree_ext.zul";
	/**
	 * 组织树
	 */
	private Tree mdsionOrgRelationTree;
	@Getter
	@Setter
	private Treecol treecol;
	/**
	 * 组织
	 */
	private Organization organization;
	/**
	 * 选中的组织
	 */
	private MdsionOrgRelation mdsionOrgRelation;
	/**
	 * 选中的组织根节点ID
	 */
	private String selOrgTreeRootId;
	/**
	 * 组织树名称
	 */
	@Getter
	@Setter
	private Listbox orgTreeNameListbox;
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

	@Resource(name = "treeBindingRuleManager")
	private TreeBindingRuleManager treeBindingRuleManager;

	@Resource(name = "mdsionOrgRelationManager")
	private MdsionOrgRelationManager mdsionOrgRelationManager;

	@Resource(name = "organizationManager")
	private OrganizationManager organizationManager;

	@Autowired
	@Qualifier("operateLogManager")
	private OperateLogManager operateLogManager = (OperateLogManager) ApplicationContextUtil
			.getBean("operateLogManager");

	@Resource(name = "aroleProfessionalTreeManager")
	private AroleProfessionalTreeManager aroleProfessionalTreeManager;

	public MultidimensionalTreeExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		this.setButtonValid(false, false);

		mdsionOrgRelationTree.addEventListener("onSelect", new EventListener() {
			public void onEvent(final Event e) throws Exception {
				Treeitem item = mdsionOrgRelationTree.getSelectedItem();
				selOrgTreeRootId = (String) item.getAttribute("orgTreeRootId");
				mdsionOrgRelation = (MdsionOrgRelation) ((TreeNodeImpl) item
						.getValue()).getEntity();
				onSelectOrganizationRelationTree(mdsionOrgRelation);
			}
		});

	}

	public void onCreate() {
		this.mdsionOrgRelationTree.setVisible(false);
		List<NodeVo> orgTreeNameList = operateLogManager.getValuesListDw();
		ListboxUtils.rendererForEdit(orgTreeNameListbox, orgTreeNameList);
	}

	/**
	 * 加载专业树顶层节点
	 * 
	 * @throws Exception
	 */
	public void loadProfessionTreeRoot(String orgTreeId) throws Exception {
		if (!StrUtil.isEmpty(orgTreeId)) {
			String sql = "select * from mdsion_org_tree t where t.status_cd = ? and t.mdsion_org_tree_id=? and t.isshow=1";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(orgTreeId);
			List<MdsionOrgTree> mdsionOrgTreeList = MdsionOrgTree.repository()
					.jdbcFindList(sql, params, MdsionOrgTree.class);
			if (mdsionOrgTreeList.size() > 0) {
				Treechildren treechildren = this.mdsionOrgRelationTree
						.getTreechildren();
				final long[] roleIds = PlatformUtil.getCurrentUser()
						.getRoleIds();
				for (final MdsionOrgTree mdsionOrgTree : mdsionOrgTreeList) {
					MdsionOrgRelation orgRelaRoot = MdsionOrgRelation
							.newInstance();
					orgRelaRoot.setOrgId(mdsionOrgTree.getOrgId());
					orgRelaRoot
							.setRelaCd(mdsionOrgTree.getMdsionOrgRelTypeCd());
					orgRelaRoot
							.setRelaOrgId(OrganizationConstant.ROOT_TREE_ORG_ID);
					boolean isShow = false, isShowAll = false;// 默认不展示
					if (PlatformUtil.isAdmin()) {// 管理员
						isShow = true;
						isShowAll = true;
					} else {
						AroleProfessionalTree apt = new AroleProfessionalTree();
						apt.setOrgId(mdsionOrgTree.getOrgId());
						apt.setOrgTreeId(Long.valueOf(orgTreeId));
						apt.setOrgRela(mdsionOrgTree.getMdsionOrgRelTypeCd());
						apt.setProfessionalTreeId(mdsionOrgTree.getOrgId());
						for (long roleId : roleIds) {
							apt.setAroleId(roleId);
							List<AroleProfessionalTree> aptList = aroleProfessionalTreeManager
									.findProfessionalTreeAuthByNode(apt);
							if (aptList != null) {
								if (aptList.size() > 0)// 当前权限还有子权限节点
									isShow = true;
								if (aptList.size() == 1)// 当前权限是最后的权限节点
									isShowAll = true;
							}
						}
					}

					if (isShow) {
						final boolean isShowAllFinal = isShowAll;
						final Treeitem titem = new Treeitem();
						titem.setAttribute("orgTreeRootId",
								String.valueOf(mdsionOrgTree.getOrgId()));
						Treerow trow = new Treerow();
						Treecell tcell = new Treecell(
								mdsionOrgTree.getOrgTreeName());
						tcell.setParent(trow);
						trow.setParent(titem);
						TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
								orgRelaRoot);
						titem.setValue(treeNodeImpl);
						titem.setParent(treechildren);
						titem.addEventListener("onOpen", new EventListener() {
							public void onEvent(final Event e) throws Exception {
								Treechildren tc = titem.getTreechildren();
								if (tc.getChildren().isEmpty()) {
									mdsionOrgRelationManager.loadDerivationTree(
											String.valueOf(mdsionOrgTree
													.getMdsionOrgTreeId()),
											String.valueOf(mdsionOrgTree
													.getOrgId()), mdsionOrgTree
													.getMdsionOrgRelTypeCd(),
											titem, String.valueOf(mdsionOrgTree
													.getOrgId()),
											isShowAllFinal, roleIds);
								}
							}
						});
						new Treechildren().setParent(titem);
						titem.setOpen(false);
					}
				}
			} else {
				ZkUtil.showError("未绑定专业树", "提示信息");
			}
		} else {
			ZkUtil.showError("加载专业树失败", "提示信息");
		}
	}

	/**
	 * 选择组织树
	 * 
	 * @throws Exception
	 */
	public void onSelect$orgTreeNameListbox() throws Exception {
		this.setButtonValid(false, false);
		Listitem listitem = orgTreeNameListbox.getSelectedItem();
		if (listitem.getValue() != null) {
			mdsionOrgRelationTree.clear();
			treecol.setLabel(listitem.getLabel());
			loadProfessionTreeRoot(listitem.getValue().toString());
			mdsionOrgRelationTree.setVisible(true);
		} else {
			mdsionOrgRelationTree.setVisible(false);
		}
		mdsionOrgRelation = null;

	}

	/**
	 * 选择组织事件
	 * 
	 * @throws Exception
	 */
	public void onSelectOrganizationRelationTree(MdsionOrgRelation orgRela)
			throws Exception {
		if (orgRela != null && orgRela.getOrgId() != null) {
			organization = orgRela.getOrganization();
			Events.postEvent(
					OrganizationConstant.ON_SELECT_PROFESSION_ORGANIZATION_TREE_REQUEST,
					this, orgRela);
		}
		this.setButtonValid(true, true);
	}

	/**
	 * 供外层主动获取控件选择的组织
	 */
	public MdsionOrgRelation getSelectMdsionOrgRelation() {
		return mdsionOrgRelation;
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
		if (this.mdsionOrgRelationTree.getSelectedItem() != null) {
			String opType = "addMultidimensionalTreeChildNode";
			this.openAddNodeWindow(opType);
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
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
		map.put("orgTreeRootId", selOrgTreeRootId);
		Window win = (Window) Executions.createComponents(
				"/pages/organization/multidimensional_tree_node_edit.zul",
				this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map dataMap = (Map) event.getData();
				Organization org = (Organization) dataMap.get("organization");
				System.out.println("-------org.getOrgId()="+org.getOrgId());
				MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
				orgRela.setOrgId(org.getOrgId());
//				orgRela.setRelaOrgId(organization.getOrgId());
				orgRela.setRelaCd(treeBindingRuleManager
						.getOrgRelaByRefRootId(Long.valueOf(selOrgTreeRootId)));
				List<MdsionOrgRelation> ors = mdsionOrgRelationManager
						.queryMdsionOrgRelationList(orgRela);
				if (ors != null && ors.size()>0) {
				    for (int i = 0; i < ors.size(); i++) {
                        if(organization.getOrgId().equals(ors.get(i).getOrgId())){
                            ZkUtil.showError("该关系已经存在", "提示信息");
                            return;                            
                        }
                        if(++i == ors.size()){
                            ZkUtil.showError("已经存在此类关系", "提示信息");
                            return;                               
                        }
                    }

				}
				
				List<MdsionOrgRelation> subMdsionOrgRelationList = mdsionOrgRelationManager
						.querySubTreeMdsionOrgRelationList(orgRela.getOrgId(),orgRela.getRelaCd());
				if (subMdsionOrgRelationList != null) {
					for (MdsionOrgRelation subMdsionOrgRelation : subMdsionOrgRelationList) {
					    System.out.println("-------subMdsionOrgRelationList="+subMdsionOrgRelation.getOrgId());
						if (mdsionOrgRelation.getOrgId().equals(
								subMdsionOrgRelation.getOrgId())) {
							ZkUtil.showError("存在环不可添加", "提示信息");
							return;
						}
					}
				}
				mdsionOrgRelationManager.addMdsionOrgRelation(orgRela);
				// orgRela.addOnly();
				Treeitem selTreeitem = mdsionOrgRelationTree.getSelectedItem();
				if (selTreeitem.isOpen()) {// 打开的时候才加载
					Treechildren treechildren = selTreeitem.getTreechildren();
					Treeitem titem = new Treeitem();
					Treerow trow = new Treerow();
					Treecell title = new Treecell(org.getOrgName());
					title.setParent(trow);
					trow.setParent(titem);
					TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
							orgRela);
					titem.setValue(treeNodeImpl);
					if (treechildren != null) {
						titem.setParent(treechildren);
					} else {
						treechildren = new Treechildren();
						titem.setParent(treechildren);
						treechildren.setParent(selTreeitem);
					}
				}
		        Listitem listitem = orgTreeNameListbox.getSelectedItem();
		        if (listitem.getValue() != null) {
		            mdsionOrgRelationTree.clear();
		            treecol.setLabel(listitem.getLabel());
		            loadProfessionTreeRoot(listitem.getValue().toString());
		            mdsionOrgRelationTree.setVisible(true);
		        } else {
		            mdsionOrgRelationTree.setVisible(false);
		        }
		        mdsionOrgRelation = null;
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
						MdsionOrgRelation or = mdsionOrgRelationManager
								.queryMdsionOrgRelation(mdsionOrgRelation);
						if (or != null) {
				            String orgRela = treeBindingRuleManager
			                        .getOrgRelaByRefRootId(Long.valueOf(selOrgTreeRootId));
				            or.setRelaCd(orgRela);
							mdsionOrgRelationManager
									.removeMdsionOrgRelation(or);
							Treechildren treechildren = (Treechildren) mdsionOrgRelationTree
									.getSelectedItem().getParent();
							treechildren.removeChild(mdsionOrgRelationTree
									.getSelectedItem());
							if (treechildren.getChildren().size() == 0) {
								treechildren.getParent().removeChild(
										treechildren);
							}
							mdsionOrgRelation = null;
							/**
							 * 抛出删除节点成功事件
							 */
							Events.postEvent(
									OrganizationConstant.ON_DEL_NODE_OK,
									MultidimensionalTreeExt.this, null);
							setButtonValid(false, false);
						}
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAddRoot
	 * @param canAddChild
	 * @param canDel
	 */
	private void setButtonValid(boolean canAddChild, boolean canDel) {
		this.addChildButton.setDisabled(!canAddChild);
		this.delChildButton.setDisabled(!canDel);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAddChild = false;
		boolean candelChild = false;

		if (PlatformUtil.isAdmin()) {
			canAddChild = true;
			candelChild = true;
		} else if ("agentTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.AGENT_TREE_ORG_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.AGENT_TREE_ORG_DEL)) {
				candelChild = true;
			}
		}
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(candelChild);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_CHILD_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_CHILD_DEL)) {
					canDel = true;
				}
			}
		}
		this.getAddChildButton().setVisible(canAdd);
		this.getDelChildButton().setVisible(canDel);
	}
}
