package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.action.bean.OrganizationRelationListboxBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.manager.DwOrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

/**
 * 组织关系管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class DwOrganizationRelationListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrganizationRelationListboxBean bean = new OrganizationRelationListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("dwOrganizationRelationManager")
	private DwOrganizationRelationManager dwOrganizationRelationManager = (DwOrganizationRelationManager) ApplicationContextUtil
			.getBean("dwOrganizationRelationManager");

	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager) ApplicationContextUtil
			.getBean("orgTypeManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/dwOrganization_relation_listbox.zul";

	/**
	 * 组织树中当前选择的organization
	 */
	private Organization organization;
	/**
	 * 当前选择的organizationRelation
	 */
	private OrganizationRelation organizationRelation;

	/**
	 * 查询organization.
	 */
	private Organization qryOrganization;
	private OrganizationRelation qryOrganizationRelation;
	private OrgType orgType;
	private List<OrgType> orgTypeList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是推导树
	 */
	@Getter
	private Boolean isDuceTree = false;
	/**
	 * 是否是组织树页面
	 */
	@Getter
	@Setter
	private Boolean isOrgTreePage = false;
	/**
	 * 是否是代理商tab
	 */
	@Getter
	@Setter
	private Boolean isAgentTab = false;
	/**
	 * 是否是内部经营实体tab
	 */
	@Getter
	@Setter
	private Boolean isIbeTab = false;
	/**
	 * 是否是代理商组织
	 */
	@Getter
	@Setter
	private Boolean isAgentOrgRelation = false;
	/**
	 * 是否是内部经营实体组织
	 */
	@Getter
	@Setter
	private Boolean isIbeOrgRelation = false;

	/**
	 * 是否显示新增删除按钮
	 */
	private Boolean isVisible = false;
	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;
	/**
	 * 是否是聚合营销2015tab
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;

	/**
	 * 组织管理区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setOrganizationRelationButtonValid(false, false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	public DwOrganizationRelationListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受组织列表抛出的组织关系查询时间
		this.addEventListener(
				OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						OrganizationRelation organizationRelation = (OrganizationRelation) event
								.getData();
						if (!StrUtil.isNullOrEmpty(organizationRelation)) {
							organization = new Organization();
							organization.setOrgId(organizationRelation
									.getOrgId());
						}

						if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
							setOrgTreeTabName(variableOrgTreeTabName);
						}

						if (!StrUtil.isEmpty(variablePagePosition)) {
							setPagePosition(variablePagePosition);
						}

						queryOrganizationRelationResponse(organizationRelation);
					}
				});

		/**
		 * 组织列表查询事件
		 */
		this.addForward(OrganizationConstant.ON_ORGANIZATION_QUERY, this,
				"onOrganiztionQueryResponse");

		/**
		 * 组织列表删除组织事件
		 */
		this.addForward(OrganizationConstant.ON_DEL_ORGANIZAITON, this,
				"onDelOrganiztionResponse");
		/**
		 * 是否显示新增删除按钮
		 */
		this.addForward(OrganizationConstant.ON_IS_VISIBLE, this,
				"onIsVisibleInit");
	}

	/**
	 * 创建初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {

		this.setOrganizationRelationButtonValid(false, false, false);
		/**
		 * 非组织树页面提供删除组织关系
		 */
		if (!isOrgTreePage) {
			this.bean.getDelOrganizationRelationButton().setVisible(true);
			this.setPagePosition(pagePosition);
		}
	}

	/**
	 * 组织列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onOrganiztionQueryResponse() throws Exception {
		this.qryOrganizationRelation = null;
		PubUtil.clearListbox(this.bean.getOrganizationRelationListBox());
		this.setOrganizationRelationButtonValid(false, false, false);
	}

	/**
	 * 选择组织列表的组织,响应查询处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void queryOrganizationRelationResponse(
			OrganizationRelation qryOrganizationRelation) throws Exception {
		this.bean.getOrgCode().setValue("");
		this.bean.getOrgName().setValue("");
		this.qryOrganizationRelation = qryOrganizationRelation;
		this.queryOrganizationRelation();
	}

	/**
	 * 组织关系选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSelectOrganizationRelation() throws Exception {
		if (bean.getOrganizationRelationListBox().getSelectedCount() > 0) {
			organizationRelation = (OrganizationRelation) bean
					.getOrganizationRelationListBox().getSelectedItem()
					.getValue();
			if (isAgentTab || isIbeTab) {
				if (!StrUtil.isNullOrEmpty(organizationRelation)
						&& !StrUtil.isNullOrEmpty(organizationRelation
								.getOrgId())) {

					orgType = new OrgType();
					orgTypeList = new ArrayList<OrgType>();

					orgType.setOrgId(organizationRelation.getOrgId());
					orgTypeList = orgTypeManager.queryOrgTypeList(orgType);

					// 在代理商TAB或内部经营实体中，如果该组织是代理商或内部经营实体，其组织关系TAB页面中，不可使用新增按钮。
					if (orgTypeList != null && orgTypeList.size() > 0) {
						for (OrgType oldOrgType : orgTypeList) {
							if (OrganizationConstant.ORG_TYPE_AGENT
									.equals(oldOrgType.getOrgTypeCd())) {
								isAgentOrgRelation = true;
							}
							if (OrganizationConstant.ORG_TYPE_N0903000000
									.equals(oldOrgType.getOrgTypeCd())) {
								isIbeOrgRelation = true;
							}
						}
					}

					if (isAgentOrgRelation) {
						isAgentOrgRelation = false;
						this.setOrganizationRelationButtonValid(false, true,
								true);
					} else if (isIbeOrgRelation) {
						isIbeOrgRelation = false;
						this.setOrganizationRelationButtonValid(false, true,
								true);
					} else {
						this.setOrganizationRelationButtonValid(true, true,
								true);
					}

				}
			} else {
				this.setOrganizationRelationButtonValid(true, true, true);
			}
		}
	}

	/**
	 * 新增组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationRelationAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openOrganizationRelationEditWin("add");
	}

	/**
	 * 删除组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationRelationDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		/**
		 * 页面ListItems列表为实时数据，无需重新查库
		 */
		List list = this.bean.getOrganizationRelationListBox().getItems();
		/**
		 * 存在2条以上直接删除选中组织关系，存在一条判断是否有下级节点
		 */
		if (list != null && list.size() <= 1) {
			/**
			 * 组织存在下级组织不可删除
			 */
			Organization org = organizationRelation.getOrganization();
			org.setSubOrganizationList(null);
			if (org != null && org.getSubOrganizationList() != null
					&& org.getSubOrganizationList().size() > 0) {
				ZkUtil.showQuestion("该组织存在下级节点，不能删除该唯一关系", "提示信息");
				return;
			}
		}
		ZkUtil.showQuestion("你确定要删除该组织关系吗?", "提示信息", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
				    dwOrganizationRelationManager
							.removeOrganizationRelation(organizationRelation);
					setOrganizationRelationButtonValid(true, false, false);
					cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
							bean.getOrganizationRelationListBox(),
							organizationRelation, "del");
					organizationRelation = null;
				}
			}
		});
	}

	/**
	 * 打开组织关系编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openOrganizationRelationEditWin(String opType)
			throws Exception {
		if (qryOrganizationRelation != null
				&& qryOrganizationRelation.getOrgId() != null) {
			Map arg = new HashMap();
			arg.put("opType", opType);
			arg.put("organizationRelation", qryOrganizationRelation);
			Window win = (Window) Executions.createComponents(
					"/pages/organization/organization_relation_edit.zul", this,
					arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						if (event.getData() != null) {
							setOrganizationRelationButtonValid(true, false,
									false);

							OrganizationRelation organizationRelation = (OrganizationRelation) event
									.getData();
							organizationRelation.setOrgId(organizationRelation
									.getRelaOrgId());
							Organization organization = organizationRelation
									.getOrganization();

							if (organization != null) {
								// 新增组织关系后，用于显示该条记录
								bean.getOrgCode().setValue(
										organization.getOrgCode());
								bean.getOrgName().setValue(
										organization.getOrgName());
							}

							queryOrganizationRelation();
						}
					}
				}
			});
		}
	}

	/**
	 * 设置组织按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setOrganizationRelationButtonValid(final Boolean canAdd,
			final Boolean canDelete, final Boolean canShow) {
		/**
		 * 推导树默认不让编辑且不让修改
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getAddOrganizationRelationButton().setDisabled(!canAdd);
		this.bean.getDelOrganizationRelationButton().setDisabled(!canDelete);
		this.bean.getShowOrganizationPathButton().setDisabled(!canShow);
	}

	/**
	 * 查询组织关系. 为组织关系添加搜索条件
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryOrganizationRelation() throws Exception {
		organizationRelation = null;
		qryOrganization = new Organization();
		qryOrganization.setOrgCode(this.bean.getOrgCode().getValue());
		qryOrganization.setOrgName(this.bean.getOrgName().getValue());

		if (this.qryOrganizationRelation != null) {
			if (isAgentTab || isIbeTab) {
				if (!StrUtil.isNullOrEmpty(qryOrganizationRelation)
						&& !StrUtil.isNullOrEmpty(qryOrganizationRelation
								.getOrgId())) {

					orgType = new OrgType();
					orgTypeList = new ArrayList<OrgType>();

					orgType.setOrgId(qryOrganizationRelation.getOrgId());
					orgTypeList = orgTypeManager.queryOrgTypeList(orgType);

					// 在代理商TAB中，如果该组织是代理商或内部经营实体，其组织关系TAB页面中，不可使用新增按钮。
					if (orgTypeList != null && orgTypeList.size() > 0) {
						for (OrgType oldOrgType : orgTypeList) {
							if (OrganizationConstant.ORG_TYPE_AGENT
									.equals(oldOrgType.getOrgTypeCd())) {
								isAgentOrgRelation = true;
							}
							if (OrganizationConstant.ORG_TYPE_N0903000000
									.equals(oldOrgType.getOrgTypeCd())) {
								isIbeOrgRelation = true;
							}
						}
					}

					if (isAgentOrgRelation) {
						isAgentOrgRelation = false;
						this.setOrganizationRelationButtonValid(false, false,
								false);
					} else if (isIbeOrgRelation) {
						isIbeOrgRelation = false;
						this.setOrganizationRelationButtonValid(false, false,
								false);
					} else {
						this.setOrganizationRelationButtonValid(true, false,
								false);
					}

				}
			} else {
				this.setOrganizationRelationButtonValid(true, false, false);
			}
			ListboxUtils.clearListbox(bean.getOrganizationRelationListBox());
			PageInfo pageInfo = dwOrganizationRelationManager
					.queryPageInfoByOrganizationRelation(qryOrganization,
							qryOrganizationRelation, this.bean
									.getOrganizationRelationListPaging()
									.getActivePage() + 1, this.bean
									.getOrganizationRelationListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrganizationRelationListBox().setModel(dataList);
			this.bean.getOrganizationRelationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			/**
			 * 组织树未选择组织时添加清理操作
			 */
			ListboxUtils.clearListbox(bean.getOrganizationRelationListBox());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganization() throws Exception {
		this.bean.getOrganizationRelationListPaging().setActivePage(0);
		this.queryOrganizationRelation();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetOrganization() throws Exception {

		this.bean.getOrgCode().setValue("");

		this.bean.getOrgName().setValue("");

	}

	/**
	 * 组织路径查看
	 * 
	 * @throws Exception
	 */
	public void onOrganizationPathShow() throws Exception {
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			List<OrganizationRelation> organizationRelationList = new ArrayList<OrganizationRelation>();
			organizationRelationList.add(organizationRelation);
			Map arg = new HashMap();
			arg.put("organizationRelationList", organizationRelationList);
			Window win = (Window) Executions.createComponents(
					"/pages/organization/organization_path.zul", this, arg);
			win.doModal();
		}
	}

	/**
	 * 组织列表删除组织事件响应
	 * 
	 * @throws Exception
	 */
	public void onDelOrganiztionResponse() throws Exception {
		/**
		 * 清空组织关系
		 */
		PubUtil.clearListbox(this.bean.getOrganizationRelationListBox());
	}

	/**
	 * 是否显示新增删除按钮
	 * 
	 * @throws Exception
	 */
	public void onIsVisibleInit(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			isVisible = (Boolean) event.getOrigin().getData();
			this.bean.getDelOrganizationRelationButton().setVisible(isVisible);
			this.bean.getAddOrganizationRelationButton().setVisible(isVisible);
			this.setPagePosition(pagePosition);
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		// boolean canAdd = false;
		boolean canDel = false;
		boolean canShow = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;
		pagePosition = page;

		if (PlatformUtil.isAdmin()) {
			canDel = true;
			canShow = true;
		} else if ("orgTreePage".equals(page)) {
			// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
			// ActionKeys.ORG_TREE_ORG_RELA_ADD)) {
			// canAdd = true;
			// }
			// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
			// ActionKeys.ORG_TREE_ORG_RELA_DEL)) {
			// canDel = true;
			// }
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_RELA_SHOW)) {
				canShow = true;
			}
		} else if ("organizationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_RELA_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_RELA_SHOW)) {
				canShow = true;
			}
		} else if ("marketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_RELA_DEL)) {
					canDel = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_RELA_SHOW)) {
					canShow = true;
				}

			}

		} else if ("costUnitPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORG_RELA_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORG_RELA_SHOW)) {
				canShow = true;
			}
		} else if ("financialCenterPage".equals(page)) {
			canDel = false;
			canShow = false;
		}
		this.bean.getDelOrganizationRelationButton().setVisible(canDel);
		this.bean.getShowOrganizationPathButton().setVisible(canShow);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		boolean canShow = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
			canShow = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_RELA_SHOW)) {
					canShow = true;
				}
			} else if ("marketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_RELA_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_RELA_DEL)) {
						canDel = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_RELA_SHOW)) {
						canShow = true;
					}

				}

			} else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_RELA_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_RELA_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_RELA_SHOW)) {
					canShow = true;
				}
			}
		}
		this.bean.getAddOrganizationRelationButton().setVisible(canAdd);
		this.bean.getDelOrganizationRelationButton().setVisible(canDel);
		this.bean.getShowOrganizationPathButton().setVisible(canShow);
	}
}
