package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.action.bean.OrganizationListboxBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpChannelOperatorsRela;
import cn.ffcs.uom.restservices.model.GrpChannelRela;
import cn.ffcs.uom.restservices.model.GrpOperators;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 组织管理.
 * 
 * @author OUZHF
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class OrganizationListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	@Getter
	private OrganizationListboxBean bean = new OrganizationListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("groupMailManager")
	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager) ApplicationContextUtil
			.getBean("orgTypeManager");

	@Autowired
	@Qualifier("partyManager")
	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	/**
	 * 日志服务队列
	 */
	private LogService logService = (LogService) ApplicationContextUtil
			.getBean("logService");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/comp/organization_listbox.zul";

	/**
	 * 当前选择的organization
	 */
	private Organization organization;

	/**
	 * 查询organization.
	 */
	private Organization qryOrganization;
	private Party partyQu;
	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;

	/**
	 * bandbox使用是否是查询代理商组织
	 */
	private Boolean isAgent = false;
	/**
	 * bandbox使用是否是查询内部经营实体组织
	 */
	private Boolean isIbe = false;
	/**
	 * bandbox使用是否是查询代理商根组织
	 */
	private Boolean isChooseAgentRoot = false;
	/**
	 * bandbox使用是否是查询内部经营实体根组织
	 */
	private Boolean isChooseIbeRoot = false;
	/**
	 * bandbox使用是否包含营业网点
	 */
	private Boolean isContainSalesNetwork = false;
	/**
	 * 是否包含内部经营实体营业网点
	 */
	@Getter
	@Setter
	private Boolean isContainIbeSalesNetwork = false;
	/**
	 * bandbox是否排除代理商
	 */
	private Boolean isExcluseAgent = false;
	/**
	 * 是否排除内部经营实体组织
	 */
	@Getter
	@Setter
	private Boolean isExcluseIbe = false;
	/**
	 * listbox是否只有营销单元组织
	 */
	@Getter
	@Setter
	private Boolean isCustmsListbox = false;
	/**
	 * listbox是否只有新营销单元组织2015
	 */
	@Getter
	@Setter
	private Boolean isMarketingListbox = false;
	/**
	 * listbox是否只有新营销单元组织2016
	 */
	@Getter
	@Setter
	private Boolean isNewMarketingListbox = false;

	/**
	 * listbox是否只有新营销单元组织2017
	 */
	@Getter
	@Setter
	private Boolean isNewSeventeenMarketingListbox = false;

	/**
	 * 是否是营销2016第五级，包区组织
	 */
	@Setter
	@Getter
	private Boolean isPackArea = false;

	/**
	 * 代理商网点
	 */
	@Setter
	@Getter
	private Boolean isAgentChannel = false;
	/**
	 * 组织管理区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;
	/**
	 * listbox是否只有成本树组织
	 */
	@Getter
	@Setter
	private Boolean isCostListbox = false;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

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
	 * 排除的组织id列表
	 */
	private List<String> queruExcludeOrgIdList;

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	/**
	 * 专业树根节点
	 * 
	 * @throws Exception
	 */
	private String orgTreeRootId;

	public OrganizationListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 代理商组织树页面设置组织类型
		 */
		this.addForward(OrganizationConstant.ON_SET_AGENT_ORGANIZATION_REQUEST,
				this, "onSetAgentOrganizationTypeResponse");
		/**
		 * 内部经营实体组织树页面设置组织类型
		 */
		this.addForward(OrganizationConstant.ON_SET_IBE_ORGANIZATION_REQUEST,
				this, "onSetIbeOrganizationTypeResponse");

		/**
		 * 代理商组织树页面选择代理商根节点
		 */
		this.addForward(
				OrganizationConstant.ON_CHOOSE_AGENT_ORGANIZATION_ROOT_REQUEST,
				this, "onChooseAgentOrganizationRootResponse");
		/**
		 * 代理商组织树页面选择内部经营实体根节点
		 */
		this.addForward(
				OrganizationConstant.ON_CHOOSE_IBE_ORGANIZATION_ROOT_REQUEST,
				this, "onChooseIbeOrganizationRootResponse");

		/**
		 * 代理商组织树页面添加下级节点，包含营业网点（代理商+营业网点）
		 */
		this.addForward(
				OrganizationConstant.ON_SET_AGENT_CONTAIN_SALESNETWORK_REQUEST,
				this, "onSetAgentContainSalesNetworkResponse");

		/**
		 * 内部经营实体组织树页面添加下级节点，包含营业网点（内部经营实体+营业网点）
		 */
		this.addForward(
				OrganizationConstant.ON_SET_IBE_CONTAIN_SALESNETWORK_REQUEST,
				this, "onSetIbeContainSalesNetworkResponse");

		/**
		 * 全部树页面排除代理商组织
		 */
		this.addForward(
				OrganizationConstant.ON_EXCLUDE_AGENT_ORGANIZATION_REQUEST,
				this, "onExcludeAgentOrganizationResponse");
		/**
		 * 全部树页面排除内部经营实体组织
		 */
		this.addForward(
				OrganizationConstant.ON_EXCLUDE_IBE_ORGANIZATION_REQUEST, this,
				"onExcludeIbeOrganizationResponse");
		/**
		 * 设定组织类型列表
		 */
		this.addForward(OrganizationConstant.ON_SET_ORGTYPE_REQUEST, this,
				"onSetOrgTypeListResponse");

		/**
		 * 公开页面忽略数据权（电信管理区域）
		 */
		this.addForward(OrganizationConstant.ON_SET_CONFIG_PAGE_REQUEST, this,
				"onSetConfgiPageResponse");
		/**
		 * 公开页面忽略数据权（电信管理区域）
		 */
		this.addForward(
				OrganizationConstant.ON_MULTIDIMENSIONAL_TREE_ORGANIZATION_QUERY,
				this, "onMultidimensionalTreeOrganizationResponse");

		/**
		 * 包区组织页面
		 * 
		 */
		this.addForward(OrganizationConstant.ON_PACKARE_ORGANIZATION_REQUEST,
				this, "onPackareOrganizationResponse");

		/**
		 * 代理商网点
		 * 
		 */
		this.addForward(
				OrganizationConstant.ON_AGENT_CHANNNEL_ORGANIZATION_REQUEST,
				this, "onAgentChannelOrganizationResponse");

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

			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());

				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
				bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
			}
		}
		this.bindCombobox();
		this.setOrganizationButtonValid(true, false, false, false, false,
				false, false);
		bean.getOrganizationListBox().setPageSize(25);
		// 去除默认查询 if (!isBandbox)
		// 去除默认查询 onQueryOrganization();
	}

	/**
	 * 绑定combobox.
	 * 
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"Organization", "orgType");
		ListboxUtils.rendererForQuery(bean.getOrgType(), orgTypeList);

		List<NodeVo> existTypeList = UomClassProvider.getValuesList(
				"Organization", "existType");
		ListboxUtils.rendererForQuery(this.bean.getExistType(), existTypeList);
	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onQueryOrganization() throws Exception {
		setOrganizationButtonValid(true, false, false, false, false, false,
				false);
		qryOrganization = new Organization();

		PubUtil.fillPoFromBean(bean, qryOrganization);
		if (isAgent || isChooseAgentRoot) {
			orgType = new OrgType();
			queryOrgTypeList = new ArrayList<OrgType>();
			qryOrganization.setIsAgent(isAgent);
			orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
			queryOrgTypeList.add(orgType);
			// 添加查询内部经营实体组织
			OrgType orgType1 = new OrgType();
			orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0903000000);
			queryOrgTypeList.add(orgType1);
		}
		if (isIbe || isChooseIbeRoot) {
			orgType = new OrgType();
			queryOrgTypeList = new ArrayList<OrgType>();
			qryOrganization.setIsIbe(isIbe);
			orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0903000000);
			queryOrgTypeList.add(orgType);
		}
		if (isChooseAgentRoot) {
			qryOrganization.setIsChooseAgentRoot(isChooseAgentRoot);
		}
		if (isChooseIbeRoot) {
			qryOrganization.setIsChooseIbeRoot(isChooseIbeRoot);
		}

		if (isContainSalesNetwork) {
			orgType = new OrgType();
			queryOrgTypeList = new ArrayList<OrgType>();
			qryOrganization.setIsContainSalesNetwork(isContainSalesNetwork);
			orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
			queryOrgTypeList.add(orgType);
		}
		if (isContainSalesNetwork && isChooseAgentRoot) {
			orgType = new OrgType();
			queryOrgTypeList = new ArrayList<OrgType>();
			qryOrganization.setIsContainSalesNetwork(isContainSalesNetwork);
			orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
			queryOrgTypeList.add(orgType);
			qryOrganization.setIsAgent(isAgent);
			// 添加查询内部经营实体组织
			OrgType orgType1 = new OrgType();
			orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0903000000);
			queryOrgTypeList.add(orgType1);
		}
		if (isContainIbeSalesNetwork) {
			orgType = new OrgType();
			queryOrgTypeList = new ArrayList<OrgType>();
			qryOrganization
					.setIsContainIbeSalesNetwork(isContainIbeSalesNetwork);
			orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0903000000);
			queryOrgTypeList.add(orgType);
		}
		if (isExcluseAgent) {
			qryOrganization.setIsExcluseAgent(isExcluseAgent);
		}
		if (isExcluseIbe) {
			qryOrganization.setIsExcluseIbe(isExcluseIbe);
		}
		if (this.queryOrgTypeList != null && this.queryOrgTypeList.size() > 0) {
			qryOrganization.setQueryOrgTypeList(queryOrgTypeList);
		}
		if (isCustmsListbox) {
			TreeOrgTypeRule totr = new TreeOrgTypeRule();
			totr.setOrgTreeId(OrganizationConstant.CUSTMS_TREE_ID);
			queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
			qryOrganization.setQueryOrgTypeList(queryOrgTypeList);
		}
		if (isMarketingListbox || isNewMarketingListbox
				|| isNewSeventeenMarketingListbox) {
			TreeOrgTypeRule totr = new TreeOrgTypeRule();
			totr.setOrgTreeId(OrganizationConstant.MARKETING_TREE_ID);
			queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
			qryOrganization.setQueryOrgTypeList(queryOrgTypeList);
		}
		if (isCostListbox) {
			TreeOrgTypeRule totr = new TreeOrgTypeRule();
			totr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
			queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
			qryOrganization.setQueryOrgTypeList(queryOrgTypeList);
		}

		if (this.queruExcludeOrgIdList != null
				&& this.queruExcludeOrgIdList.size() > 0) {
			qryOrganization.setQueryOrgIdList(queruExcludeOrgIdList);
		}

		/**
		 * 电信管理查询条件
		 */
		if (this.bean.getTelcomRegion().getTelcomRegion() != null
				&& this.bean.getTelcomRegion().getTelcomRegion()
						.getTelcomRegionId() != null) {
			qryOrganization.setTelcomRegionId(this.bean.getTelcomRegion()
					.getTelcomRegion().getTelcomRegionId());
		} else {
			/**
			 * 页面没选择默认数据权最大电信管理区域
			 */
			qryOrganization
					.setTelcomRegionId(permissionTelcomRegion != null ? permissionTelcomRegion
							.getTelcomRegionId() : null);
		}
		/**
		 * 数据权限：上级
		 */
		if (this.permissionOrganizationList != null
				&& permissionOrganizationList.size() != 0) {
			qryOrganization
					.setPermissionOrganizationList(permissionOrganizationList);
		}
		this.bean.getOrganizationListPaging().setActivePage(0);
		if (isPackArea) // 查出包区组织
			this.queryPackAreaOrganization();
		else if (isAgentChannel) // 代理商网点
			this.queryAgentChannelOrganization();
		else
			this.queryOrganization();
		/**
		 * 抛出组织查询事件
		 */
		Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY, this, null);
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationSelect() throws Exception {
		if (bean.getOrganizationListBox().getSelectedCount() > 0) {
			organization = (Organization) bean.getOrganizationListBox()
					.getSelectedItem().getValue();
			if (!StrUtil.isEmpty(variablePagePosition)) {
				this.setPagePosition(variablePagePosition);
			}
			this.setOrganizationButtonValid(true, true, true, true, true, true,
					true);
			// this.setOrganizationButtonValid(false, false, false, false,
			// false, false,
			// false);
			/**
			 * 抛出组织选择事件
			 */
			Events.postEvent(OrganizationConstant.ON_SELECT_ORGANIZATION, this,
					organization);
		}
	}

	/**
	 * 新增组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openOrganizationEditWin("add");
	}

	public void onAddParty() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openPartyEditWin("bindParty");
	}

	/**
	 * 修改组织 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openOrganizationEditWin("mod");
	}

	/**
	 * 删除组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (this.organization != null) {

			/**
			 * 使重新查库
			 */
			organization.setSubOrganizationList(null);
			List<Organization> subOrgList = organization
					.getSubOrganizationList();
			if (subOrgList != null && subOrgList.size() > 0) {
				ZkUtil.showError("存在关联的组织,不能删除", "提示信息");
				return;
			}

			/**
			 * 使重新查库
			 */
			organization.setPositionList(null);
			List<Position> positionList = organization.getPositionList();
			if (positionList != null && positionList.size() > 0) {
				ZkUtil.showError("存在关联的岗位,不能删除", "提示信息");
				return;
			}

			/**
			 * 使重新查库
			 */
			organization.setStaffList(null);
			List<Staff> staffList = organization.getStaffList();
			if (staffList != null && staffList.size() > 0) {
				ZkUtil.showError("存在员工,不能删除", "提示信息");
				return;
			}

			/**
			 * 使重新查库
			 */
			organization.setOrganizationTranListByOrgId(null);
			List<OrganizationTran> organizationTranListByOrgId = organization
					.getOrganizationTranListByOrgId();
			// if (organizationTranListByOrgId != null
			// && organizationTranListByOrgId.size() > 0) {
			// ZkUtil.showError("存在域内组织业务关系,不能删除", "提示信息");
			// return;
			// }

			/**
			 * 使重新查库
			 */
			organization.setOrganizationTranListByTranOrgId(null);
			List<OrganizationTran> organizationTranListByTranOrgId = organization
					.getOrganizationTranListByTranOrgId();
			// if (organizationTranListByTranOrgId != null
			// && organizationTranListByTranOrgId.size() > 0) {
			// ZkUtil.showError("存在域内组织业务关系,不能删除", "提示信息");
			// return;
			// }

			/**
			 * 使重新查库
			 */
			organization.setUomGroupOrgTranList(null);
			List<UomGroupOrgTran> uomGroupOrgTranList = organization
					.getUomGroupOrgTranList();
			// if (uomGroupOrgTranList != null && uomGroupOrgTranList.size() >
			// 0) {
			// ZkUtil.showError("存在域外组织业务关系,不能删除", "提示信息");
			// return;
			// }

			// if (isMarketingListbox) {
			if (true) {

				List<OrgType> orgTypeList = organization.getOrgTypeList();

				boolean isGrid = organization.isGrid(orgTypeList);

				// 调用全息网格接口进行校验
				if (isGrid) {

					if (isNewSeventeenMarketingListbox) {
						// 添加一个2017年的校验权限网格开关
						boolean gridInterFaceSwitch2017 = UomClassProvider
								.isOpenSwitch("gridInterFaceSwitch2017");// 校验开关

						// 17年调用接口
						if (gridInterFaceSwitch2017) {// 营销2015和营销2016都调用接口
							String msg = organizationManager.getGridValid(
									organization, null, null,
									OrganizationConstant.OPT_TYPE_5);
							if (!StrUtil.isEmpty(msg)) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}
						}
					} else {
						boolean gridInterFaceSwitch = UomClassProvider
								.isOpenSwitch("gridInterFaceSwitch");// 校验开关
						// if (gridInterFaceSwitch && isMarketingListbox)
						// {//只有营销2015调用接口
						if (gridInterFaceSwitch) {// 营销2015和营销2016都调用接口
							String msg = organizationManager.getGridValid(
									organization, null, null,
									OrganizationConstant.OPT_TYPE_5);
							if (!StrUtil.isEmpty(msg)) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}
						}
					}
				}
			}

			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String reason = (String) event.getData();
						/**
						 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
						 */
						SysLog log = new SysLog();
						log.startLog(new Date(), SysLogConstrants.ORG);
						// 获取当前操作用户
						// log.setUser(PlatformUtil.getCurrentUser());
						OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgId(organization.getOrgId());
						organizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

						organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(organizationExtendAttr);

						if ((organizationExtendAttr != null && !StrUtil
								.isEmpty(organizationExtendAttr
										.getOrgAttrValue()))
								|| organization
										.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {

							boolean groupMailInterFaceSwitch = UomClassProvider
									.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

							if (groupMailInterFaceSwitch
									&& organization.isUploadGroupMail()) {
								String msg = groupMailManager
										.groupMailPackageInfo(
												GroupMailConstant.GROUP_MAIL_BIZ_ID_17,
												null, organization);
								/*
								 * if (!StrUtil.isEmpty(msg)) {
								 * ZkUtil.showError(msg, "集团邮箱提示信息"); return; }
								 */
							}
						}
						
						organization.setReason(reason);
						organizationManager.removeOrganization(organization);
						/**
						 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
						 */
						Class clazz[] = { Organization.class };
						log.endLog(logService, clazz, SysLogConstrants.DEL,
								SysLogConstrants.INFO, "组织删除记录日志");
						cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
								bean.getOrganizationListBox(), organization,
								"del");
						organization = null;
						setOrganizationButtonValid(true, false, false, false,
								false, false, false);
						Events.postEvent(
								OrganizationConstant.ON_DEL_ORGANIZAITON,
								OrganizationListboxComposer.this, null);
						
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除组织", "提示信息");
			return;
		}
	}

	/**
	 * 打开组织编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openOrganizationEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("mod".equals(opType)) {
			arg.put("oldOrganization", organization);
		} else if ("show".equals(opType)) {
			arg.put("oldOrganization", organization);
		}
		if (isCustmsListbox || isMarketingListbox || isNewMarketingListbox
				|| isNewSeventeenMarketingListbox || isCostListbox) {// 营销单元或聚合营销单元或成本树管理
			arg.put("marketingUnit", true);
			arg.put("marketingListbox", isMarketingListbox);
			arg.put("newMarketingListbox", isNewMarketingListbox);
			arg.put("newSeventeenMarketingListbox",
					isNewSeventeenMarketingListbox);
			arg.put("costUnit", isCostListbox);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					if (event.getData() != null) {
						setOrganizationButtonValid(true, false, false, false,
								false, false, false);
						if ("add".equals(opType)) {
							cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
									bean.getOrganizationListBox(),
									(Organization) event.getData(), "add");
							qryOrganization = (Organization) event.getData();

							// 新增组织后，用于显示该条记录
							bean.getTelcomRegion().setTelcomRegion(
									qryOrganization.getTelcomRegion());
							bean.getOrgCode().setValue(
									qryOrganization.getOrgCode());
							bean.getOrgName().setValue(
									qryOrganization.getOrgName());

							onQueryOrganization();

						} else if ("mod".equals(opType)) {
							ListModelList model = (ListModelList) bean
									.getOrganizationListBox().getModel();
							for (int i = 0; i < model.getSize(); i++) {
								Organization org = (Organization) model.get(i);
								if (org.getOrgId().equals(
										((Organization) event.getData())
												.getOrgId())) {
									model.set(i, org);
									ListModel dataList = new BindingListModelList(
											model, true);
									bean.getOrganizationListBox().setModel(
											dataList);
								}
							}
						}

					}
				}
			}
		});
	}

	/**
	 * 设置组织按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canShow
	 *            查看按钮
	 */
	private void setOrganizationButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete,
			final Boolean canUpdate, final Boolean canShow,
			final Boolean canAddParty, final Boolean canUpdateOrgRela) {
		this.bean.getAddOrganizationButton().setDisabled(!canAdd);
		this.bean.getEditOrganizationButton().setDisabled(!canEdit);
		this.bean.getDelOrganizationButton().setDisabled(!canDelete);
		this.bean.getUpdateOrganizationButton().setDisabled(!canUpdate);
		this.bean.getShowOrganizationButton().setDisabled(!canShow);
		this.bean.getAddPartyButton().setDisabled(!canAddParty);
		this.bean.getUpdateOrganizationRelaButton().setDisabled(
				!canUpdateOrgRela);
		// this.bean.getImportOrganizationButton().setDisabled(!canImport);
	}

	/**
	 * 查询组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryOrganization() throws Exception {
		if (this.qryOrganization != null) {
			ListboxUtils.clearListbox(bean.getOrganizationListBox());
			PageInfo pageInfo = organizationManager
					.queryPageInfoByOrganization(qryOrganization, this.bean
							.getOrganizationListPaging().getActivePage() + 1,
							this.bean.getOrganizationListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrganizationListBox().setModel(dataList);
			this.bean.getOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
			organization = null;
		}
	}

	/**
	 * 查询营销第五级包区组织 .
	 * 
	 * @author xiaof 2016年9月21日 xiaof
	 */
	private void queryPackAreaOrganization() throws Exception {
		if (this.qryOrganization != null) {
			ListboxUtils.clearListbox(bean.getOrganizationListBox());
			PageInfo pageInfo = organizationManager
					.queryPageInfoByPackAreaOrganization(qryOrganization,
							this.bean.getOrganizationListPaging()
									.getActivePage() + 1, this.bean
									.getOrganizationListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrganizationListBox().setModel(dataList);
			this.bean.getOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
			organization = null;
		}
	}

	/**
	 * 代理商网点 .
	 * 
	 * @throws Exception
	 * @author xiaof 2016年9月21日 xiaof
	 */
	private void queryAgentChannelOrganization() throws Exception {
		if (this.qryOrganization != null) {
			ListboxUtils.clearListbox(bean.getOrganizationListBox());
			PageInfo pageInfo = organizationManager
					.queryPageInfoByAgentChannelOrganization(qryOrganization,
							this.bean.getOrganizationListPaging()
									.getActivePage() + 1, this.bean
									.getOrganizationListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrganizationListBox().setModel(dataList);
			this.bean.getOrganizationListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
			organization = null;
		}
	}

	/**
	 * 接受bandbox传过来的组织类型参数
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetAgentOrganizationTypeResponse(ForwardEvent event)
			throws Exception {
		isAgent = (Boolean) event.getOrigin().getData();
		if (isAgent) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来的组织类型参数
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetIbeOrganizationTypeResponse(ForwardEvent event)
			throws Exception {
		isIbe = (Boolean) event.getOrigin().getData();
		if (isIbe) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来的是否是选择根节点的参数
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onChooseAgentOrganizationRootResponse(ForwardEvent event)
			throws Exception {
		isChooseAgentRoot = (Boolean) event.getOrigin().getData();
		if (isChooseAgentRoot) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来是否是包区组织 .
	 * 
	 * @param event
	 * @throws Exception
	 * @author xiaof 2016年9月21日 xiaof
	 */
	public void onPackareOrganizationResponse(ForwardEvent event)
			throws Exception {
		isPackArea = (Boolean) event.getOrigin().getData();
		if (isPackArea) {
			if (!isBandbox) {
				/**
				 * 查询营销第5级包区组织
				 */
				this.bean.getOrganizationListPaging().setActivePage(0);
				this.queryPackAreaOrganization();

			}
		}
	}

	/**
	 * 代理商网点 .
	 * 
	 * @param event
	 * @throws Exception
	 * @author xiaof 2016年9月21日 xiaof
	 */
	public void onAgentChannelOrganizationResponse(ForwardEvent event)
			throws Exception {
		isAgentChannel = (Boolean) event.getOrigin().getData();

		if (isAgentChannel) {
			if (!isBandbox) {
				/**
				 * 查询代理商网点
				 */
				this.bean.getOrganizationListPaging().setActivePage(0);
				this.queryAgentChannelOrganization();

			}
		}
	}

	/**
	 * 接受bandbox传过来的是否是选择根节点的参数
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onChooseIbeOrganizationRootResponse(ForwardEvent event)
			throws Exception {
		isChooseIbeRoot = (Boolean) event.getOrigin().getData();
		if (isChooseIbeRoot) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来的是否是代理商且包含营业网点
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetAgentContainSalesNetworkResponse(ForwardEvent event)
			throws Exception {
		isContainSalesNetwork = (Boolean) event.getOrigin().getData();
		if (isContainSalesNetwork) {
			isAgent = isContainSalesNetwork;
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来的是否是内部经营实体且包含营业网点
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetIbeContainSalesNetworkResponse(ForwardEvent event)
			throws Exception {
		isContainIbeSalesNetwork = (Boolean) event.getOrigin().getData();
		if (isContainIbeSalesNetwork) {
			isIbe = isContainIbeSalesNetwork;
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	public void onExcludeAgentOrganizationResponse(ForwardEvent event)
			throws Exception {
		isExcluseAgent = (Boolean) event.getOrigin().getData();
		if (isExcluseAgent) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	public void onExcludeIbeOrganizationResponse(ForwardEvent event)
			throws Exception {
		isExcluseIbe = (Boolean) event.getOrigin().getData();
		if (isExcluseIbe) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 接受bandbox传过来组织类型列表范围
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetOrgTypeListResponse(ForwardEvent event) throws Exception {
		Map map = (Map) event.getOrigin().getData();
		String orgTypeStr = (String) map.get("orgTypeList");
		String excludeOrgIdStr = (String) map.get("excludeOrgIdList");
		if (!StrUtil.isEmpty(orgTypeStr)) {
			this.queryOrgTypeList = new ArrayList<OrgType>();
			for (String str : orgTypeStr.split(",")) {
				if (!StrUtil.isEmpty(str)) {
					OrgType orgType = new OrgType();
					orgType.setOrgTypeCd(str);
					queryOrgTypeList.add(orgType);
				}
			}
		}
		if (!StrUtil.isEmpty(excludeOrgIdStr)) {
			this.queruExcludeOrgIdList = new ArrayList<String>();
			for (String str : excludeOrgIdStr.split(",")) {
				if (!StrUtil.isEmpty(str)) {
					queruExcludeOrgIdList.add(str);
				}
			}
		}
		if (queryOrgTypeList.size() > 0 || queruExcludeOrgIdList.size() > 0) {
			if (!isBandbox)
				onQueryOrganization();
		}
	}

	/**
	 * 分页
	 * 
	 * @throws Exception
	 */
	public void onOrganizationListboxPaging() throws Exception {
		if (isPackArea)
			this.queryPackAreaOrganization();
		else if (isAgentChannel)
			this.queryAgentChannelOrganization();
		else
			this.queryOrganization();
	}

	/**
	 * 双击组织列表
	 * 
	 * @throws Exception
	 */
	// public void onDoubleClick$organizationListBox()
	// throws Exception {
	// if (organization != null) {
	// /**
	// * 查看详情
	// */
	// this.openOrganizationEditWin("show");
	// }
	// }

	/**
	 * 更新选择的组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationUpdate() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要更新组织吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (organization == null || organization.getOrgId() == null) {
						ZkUtil.showError("请选择你要更新的组织", "提示信息");
						return;
					} else {
						/**
						 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
						 */
						SysLog log = new SysLog();
						log.startLog(new Date(), SysLogConstrants.ORG);
						// 获取当前操作用户,对于公司封装的这个方法，我只想呵呵
						// log.setUser(PlatformUtil.getCurrentUser());
						String batchNumber = OperateLog.gennerateBatchNumber();

						organization.setBatchNumber(batchNumber);
						organization.update();

						List<OrgType> orgTypeList = organization
								.getOrgTypeList();
						if (orgTypeList != null && orgTypeList.size() > 0) {
							for (OrgType orgType : orgTypeList) {
								orgType.setBatchNumber(batchNumber);
								orgType.update();
							}
						}

						List<OrganizationRelation> organizationRelationList = organization
								.getOrganizationRelationList();
						if (organizationRelationList != null
								&& organizationRelationList.size() > 0) {
							for (OrganizationRelation organizationRelation : organizationRelationList) {
								organizationRelation
										.setBatchNumber(batchNumber);
								organizationRelation.update();
							}
						}

						List<OrgContactInfo> orgContactInfoList = organization
								.getOrganizationContactInfoList();
						if (orgContactInfoList != null
								&& orgContactInfoList.size() > 0) {
							for (OrgContactInfo orgContactInfo : orgContactInfoList) {
								orgContactInfo.setBatchNumber(batchNumber);
								orgContactInfo.update();
							}
						}

						List<OrganizationExtendAttr> organizationExtendAttrList = organization
								.getOrganizationExtendAttrList();
						if (organizationExtendAttrList != null
								&& organizationExtendAttrList.size() > 0) {
							for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList) {
								organizationExtendAttr
										.setBatchNumber(batchNumber);
								organizationExtendAttr.update();
							}
						}

						Long partyId = organization.getPartyId();

						if (!StrUtil.isNullOrEmpty(partyId)) {

							PartyRole partyRole = partyManager
									.getPartyRoleByPartyId(partyId);
							if (partyRole != null) {
								partyRole.setBatchNumber(batchNumber);
								partyRole.update();
							}

							PartyOrganization partyOrganization = partyManager
									.getPartyOrg(partyId);
							if (partyOrganization != null) {
								partyOrganization.setBatchNumber(batchNumber);
								partyOrganization.update();
							}

							Party party = partyManager.queryParty(partyId);
							if (party != null) {
								party.setBatchNumber(batchNumber);
								party.update();
							}

							List<PartyCertification> partyCertificationList = partyManager
									.getPartyCerfion(partyId);
							if (partyCertificationList != null
									&& partyCertificationList.size() > 0) {
								for (PartyCertification partyCertification : partyCertificationList) {
									partyCertification
											.setBatchNumber(batchNumber);
									partyCertification.update();
								}
							}

							List<PartyContactInfo> partyContactInfoList = partyManager
									.getPartyContInfo(partyId);
							if (partyContactInfoList != null
									&& partyContactInfoList.size() > 0) {
								for (PartyContactInfo partyContactInfo : partyContactInfoList) {
									partyContactInfo
											.setBatchNumber(batchNumber);
									partyContactInfo.update();
								}
							}
						}

						setOrganizationButtonValid(true, false, false, false,
								false, false, false);
						Messagebox.show("更新组织成功！");
						/**
						 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
						 */
						Class clazz[] = { Organization.class };
						log.endLog(logService, clazz, SysLogConstrants.UPDATE,
								SysLogConstrants.INFO, "组织更新记录日志");

						OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgId(organization.getOrgId());
						organizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

						organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(organizationExtendAttr);

						List<Organization> list = organization
								.getRelacd0101SubOrganizationList();

						if (organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null
								|| (organizationExtendAttr != null && !StrUtil
										.isEmpty(organizationExtendAttr
												.getOrgAttrValue()))
								|| (list != null && list.size() > 0)) {

							boolean groupMailInterFaceSwitch = UomClassProvider
									.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

							if (groupMailInterFaceSwitch
									&& organization.isUploadGroupMail()) {
								String msg = groupMailManager
										.groupMailPackageInfo(
												GroupMailConstant.GROUP_MAIL_BIZ_ID_16,
												null, organization);
								if (!StrUtil.isEmpty(msg)) {
									// ZkUtil.showError(msg, "提示信息");
									// return;
								}
							}
						}

						queryOrganization();

					}
				}
			}
		});

	}

	/**
	 * 更新选择的组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationRelaUpdate() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要更新组织关系吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (organization == null || organization.getOrgId() == null) {
						ZkUtil.showError("请选择你要更新的组织", "提示信息");
						return;
					} else {

						String batchNumber = OperateLog.gennerateBatchNumber();

						List<OrgType> orgTypeList = organization
								.getOrgTypeList();

						if (orgTypeList != null && orgTypeList.size() > 0) {

							OrgType orgType1 = new OrgType();
							OrgType orgType2 = new OrgType();
							OrgType orgType3 = new OrgType();
							OrgType orgType4 = new OrgType();
							OrgType orgType5 = new OrgType();
							OrgType orgType6 = new OrgType();
							OrgType orgType7 = new OrgType();
							orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
							orgType2.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202010000);
							orgType3.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202020000);
							orgType4.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202030000);
							orgType5.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202040000);
							orgType6.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202050000);
							orgType7.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202060000);

							if (orgTypeList.contains(orgType1)
									|| orgTypeList.contains(orgType2)
									|| orgTypeList.contains(orgType3)
									|| orgTypeList.contains(orgType4)
									|| orgTypeList.contains(orgType5)
									|| orgTypeList.contains(orgType6)
									|| orgTypeList.contains(orgType7)) {

								OrganizationExtendAttr queryOrganizationExtendAttr = new OrganizationExtendAttr();
								queryOrganizationExtendAttr
										.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
								queryOrganizationExtendAttr
										.setOrgId(organization.getOrgId());

								OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
										.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

								if (organizationExtendAttr != null
										&& !StrUtil
												.isEmpty(organizationExtendAttr
														.getOrgAttrValue())) {

									OrganizationRelation organizationRelation0102 = organization
											.getOrganizationRelationByRelaCd(OrganizationConstant.RELA_CD_EXTER);

									OrganizationRelation organizationRelation0201 = organization
											.getOrganizationRelationByRelaCd(OrganizationConstant.RELA_CD_JZ);

									if (orgTypeList
											.contains(OrganizationConstant.ORG_TYPE_AGENT)) {

										GrpOperators grpOperators = new GrpOperators();
										grpOperators
												.setOperatorsNbr(organizationExtendAttr
														.getOrgAttrValue());

										List<GrpOperators> grpOperatorsList = channelInfoManager
												.queryGrpOperatorsList(grpOperators);

										if (grpOperatorsList != null
												&& grpOperatorsList.size() > 0) {
											grpOperators = grpOperatorsList
													.get(0);
											if (!StrUtil.isEmpty(grpOperators
													.getParentOperNbr())) {

												queryOrganizationExtendAttr = new OrganizationExtendAttr();
												queryOrganizationExtendAttr
														.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
												queryOrganizationExtendAttr
														.setOrgAttrValue(grpOperators
																.getParentOperNbr());

												organizationExtendAttr = organizationExtendAttrManager
														.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

												if (organizationExtendAttr != null
														&& organizationExtendAttr
																.getOrgId() != null
														&& (organizationRelation0102 == null || !organizationExtendAttr
																.getOrgId()
																.equals(organizationRelation0102
																		.getRelaOrgId()))) {

													OrganizationRelation newOrganizationRelation = new OrganizationRelation();
													newOrganizationRelation
															.setOrgId(organization
																	.getOrgId());
													newOrganizationRelation
															.setRelaOrgId(organizationExtendAttr
																	.getOrgId());
													newOrganizationRelation
															.setRelaCd(OrganizationConstant.RELA_CD_EXTER);

													newOrganizationRelation
															.add();

													if (organizationRelation0102 != null) {
														organizationRelation0102
																.remove();
													}

												}
											} else if (organizationRelation0102 == null
													|| !OrganizationConstant.ROOT_AGENT_ORG_ID
															.equals(organizationRelation0102
																	.getRelaOrgId())) {

												OrganizationRelation newOrganizationRelation = new OrganizationRelation();
												newOrganizationRelation
														.setOrgId(organization
																.getOrgId());
												newOrganizationRelation
														.setRelaOrgId(OrganizationConstant.ROOT_AGENT_ORG_ID);
												newOrganizationRelation
														.setRelaCd(OrganizationConstant.RELA_CD_EXTER);

												newOrganizationRelation.add();

												if (organizationRelation0102 != null) {
													organizationRelation0102
															.remove();
												}

											}
										}

									} else {

										GrpChannelOperatorsRela grpChannelOperatorsRela = new GrpChannelOperatorsRela();
										GrpChannelRela grpChannelRela = new GrpChannelRela();

										grpChannelOperatorsRela
												.setChannelNbr(organizationExtendAttr
														.getOrgAttrValue());
										grpChannelRela
												.setChannelNbr(organizationExtendAttr
														.getOrgAttrValue());

										List<GrpChannelOperatorsRela> grpChannelOperatorsRelaList = channelInfoManager
												.queryGrpChannelOperatorsRelaList(grpChannelOperatorsRela);
										List<GrpChannelRela> grpChannelRelaList = channelInfoManager
												.queryGrpChannelRelaList(grpChannelRela);

										if (grpChannelOperatorsRelaList != null
												&& grpChannelOperatorsRelaList
														.size() > 0) {
											grpChannelOperatorsRela = grpChannelOperatorsRelaList
													.get(0);
											if (!StrUtil
													.isEmpty(grpChannelOperatorsRela
															.getOperatorsNbr())) {
												queryOrganizationExtendAttr = new OrganizationExtendAttr();
												queryOrganizationExtendAttr
														.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
												queryOrganizationExtendAttr
														.setOrgAttrValue(grpChannelOperatorsRela
																.getOperatorsNbr());

												organizationExtendAttr = organizationExtendAttrManager
														.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

												if (organizationExtendAttr != null
														&& organizationExtendAttr
																.getOrgId() != null
														&& (organizationRelation0102 == null || !organizationExtendAttr
																.getOrgId()
																.equals(organizationRelation0102
																		.getRelaOrgId()))) {

													OrganizationRelation newOrganizationRelation = new OrganizationRelation();
													newOrganizationRelation
															.setOrgId(organization
																	.getOrgId());
													newOrganizationRelation
															.setRelaOrgId(organizationExtendAttr
																	.getOrgId());
													newOrganizationRelation
															.setRelaCd(OrganizationConstant.RELA_CD_EXTER);

													newOrganizationRelation
															.add();

													if (organizationRelation0102 != null) {
														organizationRelation0102
																.remove();
													}

												}
											}
										}

										if (grpChannelRelaList != null
												&& grpChannelRelaList.size() > 0) {
											grpChannelRela = grpChannelRelaList
													.get(0);
											if (!StrUtil.isEmpty(grpChannelRela
													.getRelaChannelNbr())) {
												queryOrganizationExtendAttr = new OrganizationExtendAttr();
												queryOrganizationExtendAttr
														.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
												queryOrganizationExtendAttr
														.setOrgAttrValue(grpChannelRela
																.getRelaChannelNbr());

												organizationExtendAttr = organizationExtendAttrManager
														.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

												if (organizationExtendAttr != null
														&& organizationExtendAttr
																.getOrgId() != null
														&& (organizationRelation0201 == null || !organizationExtendAttr
																.getOrgId()
																.equals(organizationRelation0201
																		.getRelaOrgId()))) {

													OrganizationRelation newOrganizationRelation = new OrganizationRelation();
													newOrganizationRelation
															.setOrgId(organization
																	.getOrgId());
													newOrganizationRelation
															.setRelaOrgId(organizationExtendAttr
																	.getOrgId());
													newOrganizationRelation
															.setRelaCd(OrganizationConstant.RELA_CD_JZ);

													newOrganizationRelation
															.add();

													if (organizationRelation0201 != null) {
														organizationRelation0201
																.remove();
													}

												}
											}
										}

									}

								}

							} else {
								Messagebox.show("非代理商和网点组织类型，无法更新组织关系！");
								return;
							}
						} else {
							Messagebox.show("非代理商和网点组织类型，无法更新组织关系！");
							return;
						}

						setOrganizationButtonValid(true, false, false, false,
								false, false, false);

						Messagebox.show("更新组织关系成功！");

						queryOrganization();

					}
				}
			}
		});

	}

	/**
	 * 查看按钮
	 */
	public void onOrganizationShow() throws Exception {
		if (organization != null) {
			/**
			 * 查看详情
			 */
			this.openOrganizationEditWin("show");
		}
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetOrganization() throws Exception {
		PubUtil.fillBeanFromPo(new Organization(), this.bean);
		this.bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
	}

	/**
	 * 分配组织数据权限时设置数据区域
	 * 
	 * @param permissionTelcomRegion
	 */
	// public void setPermissionTelcomRegion(TelcomRegion
	// permissionTelcomRegion) {
	// if (permissionTelcomRegion != null) {
	// bean.getTelcomRegion().setTelcomRegion(permissionTelcomRegion);
	// this.permissionTelcomRegion = permissionTelcomRegion;
	// bean.getTelcomRegion().setDisabled(true);
	// }
	// }

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canUpdateOrgRela = false;
		boolean canDelete = false;
		boolean canEdit = false;
		boolean canView = false;
		boolean canAddBatch = false;
		boolean canAddParty = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canUpdateOrgRela = true;
			canDelete = true;
			canEdit = true;
			canView = true;
			canAddBatch = true;
			canAddParty = true;

		} else if ("organizationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_UPDATE_ORG_RELA)) {
				canUpdateOrgRela = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_ADD_BATCH)) {
				canAddBatch = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_DEL)) {
				canDelete = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORGANIZATION_ADD_PARTY)) {
				canAddParty = true;
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
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD_BATCH)) {
					canAddBatch = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_DEL)) {
					canDelete = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_EDIT)) {
					canEdit = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_VIEW)) {
					canView = true;
				}

			}

		} else if ("newMarketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

			// 营销管理员只有导入权限
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD_BATCH)) {
				canAddBatch = true;
			}

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD_BATCH)) {
					canAddBatch = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_DEL)) {
					canDelete = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_EDIT)) {
					canEdit = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_VIEW)) {
					canView = true;
				}

			}

		} else if ("newSeventeenMarketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);

			// 营销管理员只有导入权限
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD_BATCH)) {
				canAddBatch = true;
			}

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_ADD_BATCH)) {
					canAddBatch = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_DEL)) {
					canDelete = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_EDIT)) {
					canEdit = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORGANIZATION_VIEW)) {
					canView = true;
				}

			}

		} else if ("costUnitPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_ADD_BATCH)) {
				canAddBatch = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_DEL)) {
				canDelete = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORGANIZATION_VIEW)) {
				canView = true;
			}
		} else if ("financialCenterPage".equals(page)) {
			canAdd = false;
			canUpdate = false;
			canDelete = false;
			canEdit = false;
			canView = false;
			canAddBatch = false;
		}
		this.bean.getAddOrganizationButton().setVisible(canAdd);
		this.bean.getUpdateOrganizationButton().setVisible(canUpdate);
		this.bean.getUpdateOrganizationRelaButton()
				.setVisible(canUpdateOrgRela);
		this.bean.getImportOrganizationButton().setVisible(canAddBatch);
		this.bean.getDelOrganizationButton().setVisible(canDelete);
		this.bean.getEditOrganizationButton().setVisible(canEdit);
		this.bean.getShowOrganizationButton().setVisible(canView);
		this.bean.getAddPartyButton().setVisible(canAddParty);
	}

	/**
	 * 公开页面设置
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetConfgiPageResponse(ForwardEvent event) throws Exception {
		Map map = (Map) event.getOrigin().getData();
		if (map != null) {
			TelcomRegion telcomRegion = (TelcomRegion) map
					.get("configTelcomRegion");
			if (telcomRegion != null) {
				this.bean.getTelcomRegion().setIsConfigPage(true);
				this.bean.getTelcomRegion().onCreate();
				this.permissionTelcomRegion = telcomRegion;
				if (!isBandbox)
					onQueryOrganization();
			}
		}
	}

	/**
	 * 多维树组织查询
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onMultidimensionalTreeOrganizationResponse(ForwardEvent event)
			throws Exception {
		Map map = (Map) event.getOrigin().getData();
		orgTreeRootId = (String) map.get("orgTreeRootId");
	}

	/**
	 * 打开导入页面
	 */
	public void onOrganizationImport() throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		if (isNewMarketingListbox) {
			arg.put("marketing", "NewMarketing");
		} else if (isNewSeventeenMarketingListbox) {
			arg.put("marketing", "NewSeventeenMarketing");
		}
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_import.zul", null, arg);
		win.doModal();
	}

	/**
	 * 参与人编辑页面
	 * 
	 * @param opType
	 * @author fangy 2015年11月29日
	 */
	private void openPartyEditWin(String opType) {
		try {
			if (organization.getPartyId() != null
					&& (partyManager.queryParty(organization.getPartyId()) != null)) {
				Messagebox.show("该组织已经关联参与人,无法修改！");
				return;
			}
			List<OrgType> orgTypes = organization.getOrgTypeList();
			if (orgTypes != null && orgTypes.size() > 0) {
				OrgType orgType1 = new OrgType();
				OrgType orgType2 = new OrgType();
				OrgType orgType3 = new OrgType();
				OrgType orgType4 = new OrgType();
				orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
				orgType2.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0902000000);
				orgType3.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0903000000);
				orgType4.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0904000000);
				if (!orgTypes.contains(orgType1)
						&& !orgTypes.contains(orgType2)
						&& !orgTypes.contains(orgType3)
						&& !orgTypes.contains(orgType4)) {
					Messagebox.show("该组织类型无法关联参与人！");
					return;
				}
			} else {
				Messagebox.show("该组织类型无法关联参与人！");
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			if (opType.equals(SffOrPtyCtants.MOD)
					|| opType.equals(SffOrPtyCtants.VIEW)) {
				arg.put("organizaiton", organization);
			}
			String zul = SffOrPtyCtants.PARTY_EDIT_ZUL;
			Window win = (Window) Executions.createComponents(zul, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					// this.setOrganizationButtonValid(true, false, false,
					// false, false,false, false);
					if (event.getData() != null) {
						partyQu = (Party) event.getData();
						// PubUtil.reDisplayListbox(bean.getPartyListbox(),(Party)
						// event.getData(), type);
						organization.setPartyId(partyQu.getPartyId());
						organization.update();
					}
				}
			});
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
