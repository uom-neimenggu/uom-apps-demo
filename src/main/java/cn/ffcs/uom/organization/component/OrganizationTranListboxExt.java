package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.action.bean.OrganizationTranListboxExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.GroupOrganization;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 省内组织业务关系
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-14
 * @功能说明：
 * 
 */
public class OrganizationTranListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private OrganizationTranManager organizationTranManager = (OrganizationTranManager) ApplicationContextUtil
			.getBean("organizationTranManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	private GroupOrganizationManager groupOrganizationManager = (GroupOrganizationManager) ApplicationContextUtil
			.getBean("groupOrganizationManager");

	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager) ApplicationContextUtil
			.getBean("orgTypeManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Getter
	@Setter
	private OrganizationTranListboxExtBean bean = new OrganizationTranListboxExtBean();

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	/**
	 * 组织类型
	 */
	private OrgType orgType;
	/**
	 * 组织类型列表
	 */
	private List<OrgType> queryOrgTypeList;
	/**
	 * 业务组织类型列表
	 */
	private List<OrgType> queryTranOrgTypeList;

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;
	/**
	 * listbox是否只有新营销单元组织
	 */
	@Getter
	@Setter
	private Boolean isMarketingListbox;
	/**
	 * listbox是否只有财务树组织
	 */
	@Getter
	@Setter
	private Boolean isFinancialListbox;
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
	private boolean isVisible = false;
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

	// 选中的组织业务关系
	private OrganizationTran organizationTran;

	/**
	 * 组织树中当前选择的organization
	 */
	private Organization organization;

	// 查询
	private OrganizationTran queryOrganizationTran;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setOrganizationTranButtonValid(false, false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	public OrganizationTranListboxExt() {
		Executions.createComponents(
				"/pages/organization/organization_tran_listbox_ext.zul", this,
				null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 查询组织业务关系
		 */
		this.addForward(OrganizationTranConstant.ON_ORGANIZATION_TRAN_QUERY,
				this, "onSelectOrganizationResponse");
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg
						.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);

				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
				permissionTelcomRegion.setRegionName("中国");

			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());

				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
			}

			bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);

		}

		this.initData();

		this.setOrganizationTranButtonValid(false, false, false);

	}

	/**
	 * 初始化页面数据
	 */
	public void initData() {
		/**
		 * 组织业务关系-可选项
		 */
		List<String> optionAttrValueList = new ArrayList<String>();

		// optionAttrValueList
		// .add(OrganizationTranConstant.MARKETING_FINACIAL_MANY_TO_ONE);

		if ("politicalTab".equals(variableOrgTreeTabName)) {
			optionAttrValueList
					.add(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE);
		} else if ("otherTab".equals(variableOrgTreeTabName)) {

		} else {

			optionAttrValueList
					.add(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE);
		}

		if ("otherTab".equals(variableOrgTreeTabName)) {

			this.bean.getTranRelaType().setDisabledOptionNodes(
					optionAttrValueList);

		} else {

			this.bean.getTranRelaType().setOptionNodes(optionAttrValueList);

		}
	}

	/**
	 * 选择组织响应事件
	 * 
	 * @param event
	 */
	public void onSelectOrganizationResponse(ForwardEvent event)
			throws Exception {

		this.initData();

		organizationTran = (OrganizationTran) event.getOrigin().getData();

		if (!StrUtil.isNullOrEmpty(organizationTran)) {

			organization = new Organization();

			if ("politicalTab".equals(variableOrgTreeTabName)) {

				organization.setOrgId(organizationTran.getOrgId());

			}

			// if ("marketingTab".equals(variableOrgTreeTabName)) {
			//
			// organization.setOrgId(organizationTran.getOrgId());
			//
			// } else if ("costTab".equals(variableOrgTreeTabName)) {
			//
			// organization.setOrgId(organizationTran.getOrgId());
			//
			// }
		}

		if (organization != null && organization.getOrgId() != null) {

			this.onQueryOrganizationTran();

		} else {

			this.setOrganizationTranButtonValid(false, false, false);

			/**
			 * 组织树未选择组织清理数据
			 */
			ListboxUtils.clearListbox(this.bean.getOrganizationTranListbox());
		}

		if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
			this.setOrgTreeTabName(variableOrgTreeTabName);
		}

		if (!StrUtil.isEmpty(variablePagePosition)) {
			this.setPagePosition(variablePagePosition);
		}

	}

	public void onSelectOrganizationTranRequest() {
		if (bean.getOrganizationTranListbox().getSelectedCount() > 0) {

			organizationTran = (OrganizationTran) bean
					.getOrganizationTranListbox().getSelectedItem().getValue();

			if (organizationTran.getOrgTranId() != null) {

				if (!StrUtil.isEmpty(variableOrgTreeTabName)
						&& !StrUtil.isNullOrEmpty(organization)) {// 指针组织树上的新增按钮进行处理
					this.setOrganizationTranButtonValid(true, true, true);
				} else {
					this.setOrganizationTranButtonValid(false, true, true);
				}

			} else {
				organizationTran.setTranRelaType(bean.getTranRelaType()
						.getAttrValue());
				this.setOrganizationTranButtonValid(true, false, false);
			}
		}
	}

	/**
	 * 清空选中的组织业务关系 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onCleaningOrganizationTran() throws Exception {
		ListboxUtils.clearListbox(bean.getOrganizationTranListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author 朱林涛
	 */
	public void onOrganizationTranListboxPaging() {
		queryOrganizationTranListboxForPaging();
	}

	public void queryOrganizationTranListboxForPaging() {
		try {

			if (queryOrganizationTran != null) {
				Paging paging = bean.getOrganizationTranListboxPaging();
				PageInfo pageInfo = organizationTranManager
						.queryPageInfoByOrganizationTran(queryOrganizationTran,
								paging.getActivePage() + 1,
								paging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getOrganizationTranListbox().setModel(list);
				bean.getOrganizationTranListboxPaging().setTotalSize(
						pageInfo.getTotalCount());
			}

		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganizationTran() {

		this.setOrganizationTranButtonValid(false, false, false);

		queryOrganizationTran = new OrganizationTran();

		PubUtil.fillPoFromBean(bean, queryOrganizationTran);

		String msg = this.getDoValidOrganizationTran();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		if (!StrUtil.isNullOrEmpty(bean.getTranRelaType())
				&& !StrUtil.isEmpty(bean.getTranRelaType().getAttrValue())) {
			queryOrganizationTran.setTranRelaType(bean.getTranRelaType()
					.getAttrValue());
		}

		if (!StrUtil.isNullOrEmpty(organization)) {

			this.setOrganizationTranButtonValid(true, false, false);

			if ("politicalTab".equals(variableOrgTreeTabName)) {

				queryOrganizationTran.setOrgId(organization.getOrgId());

			}

			// if ("marketingTab".equals(variableOrgTreeTabName)) {
			//
			// queryOrganizationTran.setOrgId(organization.getOrgId());
			//
			// } else if ("costTab".equals(variableOrgTreeTabName)) {
			//
			// queryOrganizationTran.setTranOrgId(organization.getOrgId());
			//
			// }

		} else if (!StrUtil.isEmpty(variableOrgTreeTabName)) {// 组织树上未选择组织时，不允许查询
			return;
		}

		this.setOrgTypeList();

		/**
		 * 电信管理查询条件
		 */
		if (this.bean.getTelcomRegion().getTelcomRegion() != null
				&& this.bean.getTelcomRegion().getTelcomRegion()
						.getTelcomRegionId() != null) {
			queryOrganizationTran.setTelcomRegionId(this.bean.getTelcomRegion()
					.getTelcomRegion().getTelcomRegionId());
		} else {
			/**
			 * 页面没选择默认数据权最大电信管理区域
			 */
			queryOrganizationTran
					.setTelcomRegionId(permissionTelcomRegion != null ? permissionTelcomRegion
							.getTelcomRegionId() : null);
		}
		/**
		 * 数据权限：上级
		 */
		if (this.permissionOrganizationList != null
				&& permissionOrganizationList.size() != 0) {
			queryOrganizationTran
					.setPermissionOrganizationList(permissionOrganizationList);
		}

		this.bean.getOrganizationTranListboxPaging().setActivePage(0);
		this.queryOrganizationTranListboxForPaging();

	}

	/**
	 * 设置查询的组织类型
	 */
	public void setOrgTypeList() {

		if (!StrUtil.isEmpty(queryOrganizationTran.getTranRelaType())
				|| !StrUtil.isEmpty(variableOrgTreeTabName)) {

			String tranRelaType = queryOrganizationTran.getTranRelaType();
			if (!StrUtil.isEmpty(tranRelaType)) {
				// if ("marketingTab".equals(variableOrgTreeTabName)
				// || "costTab".equals(variableOrgTreeTabName)
				// || queryOrganizationTran.getTranRelaType().startsWith(
				// OrganizationTranConstant.MARKETING_FINACIAL_PRE)) {//
				// 营销组织与财务组织关系[多对一]
				//
				// /**
				// * 营销组织类型列表
				// */
				// orgType = new OrgType();
				// queryOrgTypeList = new ArrayList<OrgType>();
				// // 收入单元(2015)组织类型
				// orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N1101040000);
				// queryOrgTypeList.add(orgType);
				// // 网格(2015)组织类型
				// OrgType orgType1 = new OrgType();
				// orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N1101050000);
				// queryOrgTypeList.add(orgType1);
				// queryOrganizationTran.setQueryOrgTypeList(queryOrgTypeList);
				//
				// /**
				// * 财务组织类型列表
				// */
				// TreeOrgTypeRule totr = new TreeOrgTypeRule();
				// totr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
				// queryTranOrgTypeList = orgTypeManager.getOrgTypeList(totr);
				// queryOrganizationTran
				// .setQueryTranOrgTypeList(queryTranOrgTypeList);
				//
				// } else
				if (tranRelaType
						.equals(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE)
						|| "politicalTab".equals(variableOrgTreeTabName)) {// 营业部与网运接入包区关系[多对一]

					/**
					 * 营业部组织类型列表
					 */
					orgType = new OrgType();
					queryOrgTypeList = new ArrayList<OrgType>();
					// 营业部
					orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0102010900);
					queryOrgTypeList.add(orgType);
					queryOrganizationTran.setQueryOrgTypeList(queryOrgTypeList);

					/**
					 * 网运接入包区组织类型列表
					 */
				}
			}
		}
	}

	public String getDoValidOrganizationTran() {
		if (this.bean.getTranRelaType() == null
				|| StrUtil.isEmpty(this.bean.getTranRelaType().getAttrValue())) {
			return "组织业务关系类型不能为空";
		}
		return "";
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetOrganizationTran() throws Exception {
		this.bean.getOrgName().setValue(null);
		this.bean.getOrgCode().setValue(null);
		this.bean.getTranOrgName().setValue(null);
		this.bean.getTranOrgCode().setValue(null);
	}

	/**
	 * 新增 .
	 * 
	 * @author 朱林涛
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onAddOrganizationTran() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (!StrUtil.isNullOrEmpty(organization)) {// 对组织树新增组织业务关系的处理，防止组织业务关系选择事件干扰

			organizationTran = new OrganizationTran();

			if (!StrUtil.isNullOrEmpty(bean.getTranRelaType())
					&& !StrUtil.isEmpty(bean.getTranRelaType().getAttrValue())) {

				organizationTran.setTranRelaType(bean.getTranRelaType()
						.getAttrValue());

			}

			// if ("marketingTab".equals(variableOrgTreeTabName)) {
			//
			// organizationTran.setOrgId(organization.getOrgId());
			//
			// } else if ("costTab".equals(variableOrgTreeTabName)) {
			//
			// organizationTran.setTranOrgId(organization.getOrgId());
			//
			// }

			if ("politicalTab".equals(variableOrgTreeTabName)) {

				organizationTran.setOrgId(organization.getOrgId());

			}

		}

		this.openEditOrganizationTranWin("add");

	}

	/**
	 * 修改 .
	 * 
	 * @author 朱林涛
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onEditOrganizationTran() throws PortalException,
			SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (!StrUtil.isNullOrEmpty(organization)) {// 对组织树修改组织业务关系的处理，防止组织业务关系新增事件干扰
			organizationTran = (OrganizationTran) bean
					.getOrganizationTranListbox().getSelectedItem().getValue();
		}

		this.openEditOrganizationTranWin("mod");
	}

	private void openEditOrganizationTranWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("organizationTran", organizationTran);
			arg.put("variableOrgTreeTabName", variableOrgTreeTabName);
			Window win = (Window) Executions
					.createComponents(
							"/pages/organization/organization_tran_edit.zul",
							this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {

					if (event.getData() != null) {

						OrganizationTran organizationTran = (OrganizationTran) event
								.getData();

						if (organizationTran != null
								&& organizationTran.getOrgId() != null
								&& organizationTran.getTranOrgId() != null) {

							List<String> tranRelaTypeList = new ArrayList<String>();

							if (!StrUtil.isEmpty(organizationTran
									.getTranRelaType())) {
								tranRelaTypeList.add(organizationTran
										.getTranRelaType());
								bean.getTranRelaType().setInitialValue(
										tranRelaTypeList);
							}

							Organization organization = (Organization) Organization
									.repository().getObject(Organization.class,
											organizationTran.getOrgId());

							Organization tranOrganization = (Organization) Organization
									.repository().getObject(Organization.class,
											organizationTran.getTranOrgId());

							if (organization != null) {
								bean.getOrgName().setValue(
										organization.getOrgName());
								bean.getOrgCode().setValue(
										organization.getOrgCode());
							}

							if (tranOrganization != null) {
								bean.getTranOrgName().setValue(
										tranOrganization.getOrgName());
								bean.getTranOrgCode().setValue(
										tranOrganization.getOrgCode());
							}

						}

						onQueryOrganizationTran();
					}
				}
			});
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception ec) {
			ec.printStackTrace();
		}
	}

	public void onDelOrganizationTran() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
			Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {

								if (!StrUtil.isNullOrEmpty(organization)) {

									setOrganizationTranButtonValid(true, false,
											false);
								} else {
									setOrganizationTranButtonValid(false,
											false, false);
								}
								organizationTranManager
										.removeOrganizationTran(organizationTran);
								Messagebox.show("域内业务关系删除成功！");
								PubUtil.reDisplayListbox(
										bean.getOrganizationTranListbox(),
										organizationTran, "del");
							}
						}
					});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 组织路径查看
	 * 
	 * @throws Exception
	 */
	public void onOrganizationPathShow() throws Exception {

		if (organizationTran != null) {

			List<OrganizationRelation> organizationRelationList = new ArrayList<OrganizationRelation>();

			List<NodeVo> relaCdList = UomClassProvider.getValuesList(
					"OrganizationRelation", "relaCd");

			if (organizationTran.getOrgId() != null) {

				Organization org = organizationManager.getById(organizationTran
						.getOrgId());

				if (org != null) {
					for (NodeVo nodeVo : relaCdList) {
						OrganizationRelation organizationRelation = new OrganizationRelation();
						organizationRelation.setOrgId(org.getOrgId());
						organizationRelation.setRelaCd(nodeVo.getId());
						organizationRelationList.add(organizationRelation);
					}
				}

				GroupOrganization groupOrganization = new GroupOrganization();
				groupOrganization.setOrgCode(organizationTran.getOrgId()
						.toString());

				List<GroupOrganization> groupOrganizationList = groupOrganizationManager
						.queryGroupOrganizationList(groupOrganization);

				if (groupOrganizationList != null
						&& groupOrganizationList.size() > 0) {
					OrganizationRelation organizationRelation = new OrganizationRelation();
					organizationRelation
							.setGroupOrganization(groupOrganizationList.get(0));
					organizationRelationList.add(organizationRelation);
				}

			}

			if (organizationTran.getTranOrgId() != null) {

				GroupOrganization groupOrganization = new GroupOrganization();
				groupOrganization.setOrgCode(organizationTran.getTranOrgId()
						.toString());

				List<GroupOrganization> groupOrganizationList = groupOrganizationManager
						.queryGroupOrganizationList(groupOrganization);

				if (groupOrganizationList != null
						&& groupOrganizationList.size() > 0) {
					OrganizationRelation organizationRelation = new OrganizationRelation();
					organizationRelation
							.setGroupOrganization(groupOrganizationList.get(0));
					organizationRelationList.add(organizationRelation);
				}

				Organization tranOrg = organizationManager
						.getById(organizationTran.getTranOrgId());

				if (tranOrg != null) {
					for (NodeVo nodeVo : relaCdList) {
						OrganizationRelation organizationRelation = new OrganizationRelation();
						organizationRelation.setOrgId(tranOrg.getOrgId());
						organizationRelation.setRelaCd(nodeVo.getId());
						organizationRelationList.add(organizationRelation);
					}
				}
			}

			Map arg = new HashMap();

			arg.put("organizationRelationList", organizationRelationList);

			Window win = (Window) Executions.createComponents(
					"/pages/organization/organization_path.zul", this, arg);
			win.doModal();

		}
	}

	/**
	 * 设置按钮的状态 .
	 * 
	 * @param add
	 * @param del
	 * @param edit
	 * @author 朱林涛
	 */
	private void setOrganizationTranButtonValid(boolean add, boolean edit,
			boolean del) {
		bean.getAddOrganizationTranButton().setDisabled(!add);
		bean.getEditOrganizationTranButton().setDisabled(!edit);
		bean.getDelOrganizationTranButton().setDisabled(!del);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
		} else if ("tranPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_TRAN_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_TRAN_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_TRAN_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddOrganizationTranButton().setVisible(canAdd);
		this.bean.getEditOrganizationTranButton().setVisible(canEdit);
		this.bean.getDelOrganizationTranButton().setVisible(canDel);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_TRAN_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_TRAN_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_TRAN_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_AGENT_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_AGENT_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_AGENT_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("ibeTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_IBE_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_IBE_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_IBE_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("supplierTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("ossTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("edwTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
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
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_DEL)) {
						canDel = true;
					}

				}

			} else if ("newMarketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_DEL)) {
						canDel = true;
					}

				}

            } else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {
                
                aroleOrganizationLevel = new AroleOrganizationLevel();
                aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
                aroleOrganizationLevel
                    .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                
                if (!StrUtil.isNullOrEmpty(organization)
                    && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                        aroleOrganizationLevel, organization)) {
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_ADD)) {
                        canAdd = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_EDIT)) {
                        canEdit = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_TRAN_DEL)) {
                        canDel = true;
                    }
                    
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_TRAN_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_TRAN_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_TRAN_DEL)) {
					canDel = true;
				}
			}
		}
		this.bean.getAddOrganizationTranButton().setVisible(canAdd);
		this.bean.getEditOrganizationTranButton().setVisible(canEdit);
		this.bean.getDelOrganizationTranButton().setVisible(canDel);
	}
}
