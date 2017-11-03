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
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.action.bean.UomGroupOrgTranListboxExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.dao.impl.UomGroupOrgTranDaoImpl;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.manager.UomGroupOrgTranManager;
import cn.ffcs.uom.organization.model.GroupOrganization;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.systemconfig.constants.AttrValueConstant;
import cn.ffcs.uom.systemconfig.manager.AttrValueManager;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 跨域内外组织业务关系
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-14
 * @功能说明：
 * 
 */
public class UomGroupOrgTranListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private OrganizationTranManager organizationTranManager = (OrganizationTranManager) ApplicationContextUtil
			.getBean("organizationTranManager");

	private GroupOrganizationManager groupOrganizationManager = (GroupOrganizationManager) ApplicationContextUtil
			.getBean("groupOrganizationManager");

	@Autowired
	@Qualifier("attrValueManager")
	private AttrValueManager attrValueManager = (AttrValueManager) ApplicationContextUtil
			.getBean("attrValueManager");

	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	
    private UomGroupOrgTranManager uomGroupOrgTranManager = (UomGroupOrgTranManager) ApplicationContextUtil
                                                                            .getBean("uomGroupOrgTranManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager) ApplicationContextUtil
			.getBean("orgTypeManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Getter
	@Setter
	private UomGroupOrgTranListboxExtBean bean = new UomGroupOrgTranListboxExtBean();

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	/**
	 * 组织类型列表
	 */
	private List<OrgType> queryOrgTypeList;
	/**
	 * 业务组织类型列表
	 */
	private List<String> queryTranOrgTypeList;

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;
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
	 * 是否是组织页面tab
	 */
	@Getter
	@Setter
	private Boolean isOrganizationPage = false;

	/**
	 * 页面标志
	 */
	@Getter
	private String pagePosition;
	/**
	 * 判断组织树TAB
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
	private UomGroupOrgTran uomGroupOrgTran;

	/**
	 * 组织树中当前选择的organization
	 */
	private Organization organization;

	// 查询
	private UomGroupOrgTran queryUomGroupOrgTran;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setUomGroupOrgTranButtonValid(false, false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	public UomGroupOrgTranListboxExt() {
		Executions.createComponents(
				"/pages/organization/uom_group_org_tran_listbox_ext.zul", this,
				null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 查询组织业务关系
		 */
		this.addForward(OrganizationTranConstant.ON_UOM_GROUP_ORG_TRAN_QUERY,
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

		this.setUomGroupOrgTranButtonValid(false, false, false);

	}

	/**
	 * 初始化页面数据
	 */
	public void initData() {
		/**
		 * 组织业务关系-可选项
		 */
		List<String> optionAttrValueList = new ArrayList<String>();

		if ("costTab".equals(variableOrgTreeTabName)) {
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE);
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE);
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE);
		} else if ("agentTab".equals(variableOrgTreeTabName)
				|| "ibeTab".equals(variableOrgTreeTabName)) {
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE);
		} else if ("marketingTab".equals(variableOrgTreeTabName)
				|| "newMarketingTab".equals(variableOrgTreeTabName)
				|| "newSeventeenMarketingTab".equals(variableOrgTreeTabName)) {
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
		} else if ("otherTab".equals(variableOrgTreeTabName)) {
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
			// optionAttrValueList
			// .add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
		} else {

			optionAttrValueList
					.add(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
			optionAttrValueList
					.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE);
		}

		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_ONE_TO_ONE);

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

		uomGroupOrgTran = (UomGroupOrgTran) event.getOrigin().getData();

		if (!StrUtil.isNullOrEmpty(uomGroupOrgTran)) {

			organization = new Organization();

			if ("costTab".equals(variableOrgTreeTabName)
					|| "marketingTab".equals(variableOrgTreeTabName)
					|| "newMarketingTab".equals(variableOrgTreeTabName)
					|| "newSeventeenMarketingTab".equals(variableOrgTreeTabName)
					|| "agentTab".equals(variableOrgTreeTabName)
					|| "ibeTab".equals(variableOrgTreeTabName)
					|| "organization".equals(variableOrgTreeTabName)) {

				organization.setOrgId(uomGroupOrgTran.getOrgId());

			}
		}

		if (organization != null && organization.getOrgId() != null) {

			this.onQueryUomGroupOrgTran();

		} else {

			this.setUomGroupOrgTranButtonValid(false, false, false);

			/**
			 * 组织树未选择组织清理数据
			 */
			ListboxUtils.clearListbox(this.bean.getUomGroupOrgTranListbox());
		}

		if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
			this.setOrgTreeTabName(variableOrgTreeTabName);
		}

		if (!StrUtil.isEmpty(variablePagePosition)) {
			this.setPagePosition(variablePagePosition);
		}

	}

	public void onSelectUomGroupOrgTranRequest() {

		if (bean.getUomGroupOrgTranListbox().getSelectedCount() > 0) {

			uomGroupOrgTran = null;

			uomGroupOrgTran = (UomGroupOrgTran) bean
					.getUomGroupOrgTranListbox().getSelectedItem().getValue();

			if (uomGroupOrgTran != null
					&& !StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

				if (OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE
						.equals(uomGroupOrgTran.getTranRelaType())) {
					bean.getShowSupplierInfoButton().setDisabled(false);
				} else {
					bean.getShowSupplierInfoButton().setDisabled(true);
				}

			} else {
				bean.getShowSupplierInfoButton().setDisabled(true);
			}

			if (uomGroupOrgTran.getOrgTranId() != null) {

				if (!StrUtil.isEmpty(variableOrgTreeTabName)
						&& !StrUtil.isNullOrEmpty(organization)) {// 针对组织树上的新增按钮进行处理
					this.setUomGroupOrgTranButtonValid(true, true, true);
				} else {
					this.setUomGroupOrgTranButtonValid(false, true, true);
				}

			} else {

				if (!StrUtil.isNullOrEmpty(bean.getTranRelaType())
						&& !StrUtil.isEmpty(bean.getTranRelaType()
								.getAttrValue())) {
					uomGroupOrgTran.setTranRelaType(bean.getTranRelaType()
							.getAttrValue());
				}

				this.setUomGroupOrgTranButtonValid(true, false, false);
			}
		}
	}

	/**
	 * 清空选中的组织业务关系 .
	 * 
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onCleaningUomGroupOrgTran() throws Exception {
		ListboxUtils.clearListbox(bean.getUomGroupOrgTranListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author 朱林涛
	 */
	public void onUomGroupOrgTranListboxPaging() {
		queryUomGroupOrgTranListboxForPaging();
	}

	public void queryUomGroupOrgTranListboxForPaging() {
		try {

			if (queryUomGroupOrgTran != null) {
				Paging paging = bean.getUomGroupOrgTranListboxPaging();
				PageInfo pageInfo = organizationTranManager
						.queryPageInfoByUomGroupOrgTran(queryUomGroupOrgTran,
								paging.getActivePage() + 1,
								paging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getUomGroupOrgTranListbox().setModel(list);
				bean.getUomGroupOrgTranListboxPaging().setTotalSize(
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
	public void onQueryUomGroupOrgTran() {

		this.setUomGroupOrgTranButtonValid(false, false, false);

		queryUomGroupOrgTran = new UomGroupOrgTran();

		PubUtil.fillPoFromBean(bean, queryUomGroupOrgTran);

		String msg = this.getDoValidUomGroupOrgTran();

		if (StrUtil.isNullOrEmpty(organization) && !StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		
		//如果是组织页面的tab，没有得到组织页面传递过来的信号，不给查询
		if(isOrganizationPage && (uomGroupOrgTran == null || uomGroupOrgTran.getOrgId() == null))
		{
		    ZkUtil.showError("未选中组织", "提示信息");
		    return;
		}

		if (!StrUtil.isNullOrEmpty(bean.getTranRelaType())
				&& !StrUtil.isEmpty(bean.getTranRelaType().getAttrValue())) {
			queryUomGroupOrgTran.setTranRelaType(bean.getTranRelaType()
					.getAttrValue());
		}

		if (!StrUtil.isNullOrEmpty(organization)) {

			this.setUomGroupOrgTranButtonValid(true, false, false);

			if ("costTab".equals(variableOrgTreeTabName)
					|| "marketingTab".equals(variableOrgTreeTabName)
					|| "newMarketingTab".equals(variableOrgTreeTabName)
					|| "newSeventeenMarketingTab".equals(variableOrgTreeTabName)
					|| "agentTab".equals(variableOrgTreeTabName)
					|| "ibeTab".equals(variableOrgTreeTabName)
					|| "organization".equals(variableOrgTreeTabName)) {

				queryUomGroupOrgTran.setOrgId(organization.getOrgId());

				if (StrUtil.isEmpty(queryUomGroupOrgTran.getTranRelaType())) {
					queryUomGroupOrgTran
							.setTranRelaType(OrganizationTranConstant.UOM_GROUP_MAIN_DATA_PRE);
				}

			}

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
			queryUomGroupOrgTran.setTelcomRegionId(this.bean.getTelcomRegion()
					.getTelcomRegion().getTelcomRegionId());
		} else {
			/**
			 * 页面没选择默认数据权最大电信管理区域
			 */
			queryUomGroupOrgTran
					.setTelcomRegionId(permissionTelcomRegion != null ? permissionTelcomRegion
							.getTelcomRegionId() : null);
		}
		/**
		 * 数据权限：上级
		 */
		if (this.permissionOrganizationList != null
				&& permissionOrganizationList.size() != 0) {
			queryUomGroupOrgTran
					.setPermissionOrganizationList(permissionOrganizationList);
		}

		this.bean.getUomGroupOrgTranListboxPaging().setActivePage(0);
		this.queryUomGroupOrgTranListboxForPaging();

	}

	/**
	 * 设置查询的组织类型
	 */
	public void setOrgTypeList() {

		if (!StrUtil.isEmpty(queryUomGroupOrgTran.getTranRelaType())
				|| !StrUtil.isEmpty(variableOrgTreeTabName)) {

			String tranRelaType = queryUomGroupOrgTran.getTranRelaType();

			if (!StrUtil.isEmpty(tranRelaType)) {

				if (tranRelaType
						.equals(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE)
						|| "costTab".equals(variableOrgTreeTabName)) {
					/**
					 * 财务组织类型列表
					 */
					TreeOrgTypeRule totr = new TreeOrgTypeRule();
					totr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
					queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
					queryUomGroupOrgTran.setQueryOrgTypeList(queryOrgTypeList);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE)
						|| "agentTab".equals(variableOrgTreeTabName)
						|| "ibeTab".equals(variableOrgTreeTabName)) {// 网点与财务组织关系

					/**
					 * 营业网点组织类型列表
					 */
					AttrValue attrValue = new AttrValue();
					attrValue.setAttrId(AttrValueConstant.ORG_TYPE_ATTR_ID);
					attrValue
							.setAttrValue(OrganizationConstant.SALES_NETWORK_PRE);

					queryOrgTypeList = new ArrayList<OrgType>();

					List<AttrValue> attrValueList = attrValueManager
							.queryAttrValueList(attrValue, "R");

					if (attrValueList != null && attrValueList.size() > 0) {

						for (AttrValue newAttrValue : attrValueList) {
							OrgType orgType = new OrgType();
							orgType.setOrgTypeCd(newAttrValue.getAttrValue());
							queryOrgTypeList.add(orgType);
						}

					}

					queryUomGroupOrgTran.setQueryOrgTypeList(queryOrgTypeList);

				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE)
						|| "marketingTab".equals(variableOrgTreeTabName)
						|| "newMarketingTab".equals(variableOrgTreeTabName)
						|| "newSeventeenMarketingTab".equals(variableOrgTreeTabName)) {// 营销组织与集团成本中心组织关系

					/**
					 * 营销组织类型列表
					 */
					AttrValue attrValue = new AttrValue();
					attrValue.setAttrId(AttrValueConstant.ORG_TYPE_ATTR_ID);
					attrValue
							.setAttrValue(OrganizationConstant.MARKETING_ORG_PRE);

					queryOrgTypeList = new ArrayList<OrgType>();

					List<AttrValue> attrValueList = attrValueManager
							.queryAttrValueList(attrValue, "R");

					if (attrValueList != null && attrValueList.size() > 0) {

						for (AttrValue newAttrValue : attrValueList) {
							OrgType orgType = new OrgType();
							orgType.setOrgTypeCd(newAttrValue.getAttrValue());
							queryOrgTypeList.add(orgType);
						}

					}

					queryUomGroupOrgTran.setQueryOrgTypeList(queryOrgTypeList);

				}
			}

			/**
			 * 集团主数据组织类型列表
			 */

			queryTranOrgTypeList = new ArrayList<String>();

			if (!StrUtil.isEmpty(tranRelaType)) {

				if (tranRelaType
						.equals(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE)) {// 统一用户财务公司组织与集团公司代码对应关系[一对一]集团公司代码组织类型
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_COMPANY_CODE_ONE_TO_ONE);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE)) {// 统一用户财务利润组织与集团利润中心对应关系[一对一]集团利润中心组织类型
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_PROFIT_CENTER_ONE_TO_ONE);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE)
						|| tranRelaType
								.equals(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE)) {// 统一用户财务成本组织与集团成本中心对应关系[一对一]或统一用户网点组织与集团成本中心对应关系[多对一]
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_COST_CENTER_ONE_TO_ONE);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE)) {// 统一用户网点组织与集团供应商对应关系[多对一]集团供应商组织类型
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_SUPPLIER_ONE_TO_ONE);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE)) {// 统一用户与集团统一目录组织对应关系[一对一]集团統一目录组织类型
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_DIRECTORY_ONE_TO_ONE);
				} else if (tranRelaType
						.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE)) {// 统一用户网点组织与集团供应商付款方银行账号对应关系[多对一]供应商付款方银行账号
					queryTranOrgTypeList
							.add(OrganizationTranConstant.GROUP_SUPPLIER_BANK_ONE_TO_ONE);
				}
			}

			if (queryTranOrgTypeList != null && queryTranOrgTypeList.size() > 0) {
				queryUomGroupOrgTran
						.setQueryTranOrgTypeList(queryTranOrgTypeList);
			}

		}
	}

	public String getDoValidUomGroupOrgTran() {
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
	public void onResetUomGroupOrgTran() throws Exception {
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
	public void onAddUomGroupOrgTran() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (!StrUtil.isNullOrEmpty(organization)) {// 对组织树新增组织业务关系的处理，防止组织业务关系选择事件干扰

			uomGroupOrgTran = new UomGroupOrgTran();

			if (!StrUtil.isNullOrEmpty(bean.getTranRelaType())
					&& !StrUtil.isEmpty(bean.getTranRelaType().getAttrValue())) {
				uomGroupOrgTran.setTranRelaType(bean.getTranRelaType()
						.getAttrValue());
			}

			if ("costTab".equals(variableOrgTreeTabName)
					|| "marketingTab".equals(variableOrgTreeTabName)
					|| "newMarketingTab".equals(variableOrgTreeTabName)
					|| "newSeventeenMarketingTab".equals(variableOrgTreeTabName)
					|| "agentTab".equals(variableOrgTreeTabName)
					|| "ibeTab".equals(variableOrgTreeTabName)
					|| "organization".equals(variableOrgTreeTabName)) {

				uomGroupOrgTran.setOrgId(organization.getOrgId());

			}
		}

		this.openEditUomGroupOrgTranWin("add");

	}

	/**
	 * 修改 .
	 * 
	 * @author 朱林涛
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onEditUomGroupOrgTran() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (!StrUtil.isNullOrEmpty(organization)) {// 对组织树修改组织业务关系的处理，防止组织业务关系新增事件干扰

			uomGroupOrgTran = (UomGroupOrgTran) bean
					.getUomGroupOrgTranListbox().getSelectedItem().getValue();
		}

		this.openEditUomGroupOrgTranWin("mod");
	}

	private void openEditUomGroupOrgTranWin(String opType) {
		try {
		    //如果是组织页面传过来的，打开页面之前要进行组织类型的校验
            if ("organization".equals(variableOrgTreeTabName)) {
                // 校验是否符合对应的组织类型
                String msg = checkOrgType(uomGroupOrgTran);
                if (!StrUtil.isNullOrEmpty(msg)) {
                    ZkUtil.showError(msg, "提示信息");
                    return;
                }
            }
		    
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("uomGroupOrgTran", uomGroupOrgTran);
			arg.put("variableOrgTreeTabName", variableOrgTreeTabName);
			Window win = (Window) Executions.createComponents(
					"/pages/organization/uom_group_org_tran_edit.zul", this,
					arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {

					if (event.getData() != null) {

						UomGroupOrgTran uomGroupOrgTran = (UomGroupOrgTran) event
								.getData();

						if (uomGroupOrgTran != null
								&& uomGroupOrgTran.getOrgId() != null
								&& uomGroupOrgTran.getTranOrgId() != null) {

							List<String> tranRelaTypeList = new ArrayList<String>();

							if (!StrUtil.isEmpty(uomGroupOrgTran
									.getTranRelaType())) {
								tranRelaTypeList.add(uomGroupOrgTran
										.getTranRelaType());
								bean.getTranRelaType().setInitialValue(
										tranRelaTypeList);
							}

							Organization organization = (Organization) Organization
									.repository().getObject(Organization.class,
											uomGroupOrgTran.getOrgId());

							GroupOrganization groupOrganization = new GroupOrganization();
							groupOrganization.setOrgCode(uomGroupOrgTran
									.getTranOrgId());

							List<GroupOrganization> groupOrganizationList = groupOrganizationManager
									.queryGroupOrganizationList(groupOrganization);

							GroupOrganization tranOrganization = null;

							if (groupOrganizationList != null
									&& groupOrganizationList.size() > 0) {

								tranOrganization = groupOrganizationList.get(0);

							}

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

						onQueryUomGroupOrgTran();
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

	public void onDelUomGroupOrgTran() {
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

									setUomGroupOrgTranButtonValid(true, false,
											false);
								} else {
									setUomGroupOrgTranButtonValid(false, false,
											false);
								}
								organizationTranManager
										.removeUomGroupOrgTran(uomGroupOrgTran);
								Messagebox.show("跨域内外业务关系删除成功！");
								PubUtil.reDisplayListbox(
										bean.getUomGroupOrgTranListbox(),
										uomGroupOrgTran, "del");
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

		if (uomGroupOrgTran != null) {

			List<OrganizationRelation> organizationRelationList = new ArrayList<OrganizationRelation>();

			List<NodeVo> relaCdList = UomClassProvider.getValuesList(
					"OrganizationRelation", "relaCd");

			if (uomGroupOrgTran.getOrgId() != null) {

				Organization org = organizationManager.getById(uomGroupOrgTran
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
				groupOrganization.setOrgCode(uomGroupOrgTran.getOrgId()
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

			if (!StrUtil.isEmpty(uomGroupOrgTran.getTranOrgId())) {

				GroupOrganization groupOrganization = new GroupOrganization();
				groupOrganization.setOrgCode(uomGroupOrgTran.getTranOrgId());

				List<GroupOrganization> groupOrganizationList = groupOrganizationManager
						.queryGroupOrganizationList(groupOrganization);

				if (groupOrganizationList != null
						&& groupOrganizationList.size() > 0) {
					OrganizationRelation organizationRelation = new OrganizationRelation();
					organizationRelation
							.setGroupOrganization(groupOrganizationList.get(0));
					organizationRelationList.add(organizationRelation);
				}

				if (IdcardValidator.isDigital(uomGroupOrgTran.getTranOrgId())) {
					Organization tranOrg = organizationManager.getById(Long
							.parseLong(uomGroupOrgTran.getTranOrgId()));

					if (tranOrg != null) {
						for (NodeVo nodeVo : relaCdList) {
							OrganizationRelation organizationRelation = new OrganizationRelation();
							organizationRelation.setOrgId(tranOrg.getOrgId());
							organizationRelation.setRelaCd(nodeVo.getId());
							organizationRelationList.add(organizationRelation);
						}
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
	 * 发布日志请求事件.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSupplierInfoShow() throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("uomGroupOrgTran", uomGroupOrgTran);
		Window win = (Window) Executions.createComponents(
				"/pages/organization/supplier_info_main.zul", this, arg);
		win.doModal();
	}

	/**
	 * 设置按钮的状态 .
	 * 
	 * @param add
	 * @param del
	 * @param edit
	 * @author 朱林涛
	 */
	private void setUomGroupOrgTranButtonValid(boolean add, boolean edit,
			boolean del) {
		bean.getAddUomGroupOrgTranButton().setDisabled(!add);
		bean.getEditUomGroupOrgTranButton().setDisabled(!edit);
		bean.getDelUomGroupOrgTranButton().setDisabled(!del);
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
		} else if ("uomGroupTranPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UOM_GROUP_ORG_TRAN_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UOM_GROUP_ORG_TRAN_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UOM_GROUP_ORG_TRAN_DEL)) {
				canDel = true;
			}

		} else if ("organizationPage".equals(page)) {
		    
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_UOM_GROUP_ORG_TRAN_ADD)) {
                canAdd = true;
            }
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_UOM_GROUP_ORG_TRAN_EDIT)) {
                canEdit = true;
            }
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_UOM_GROUP_ORG_TRAN_DEL)) {
                canDel = true;
            }
		    
		}

		this.bean.getAddUomGroupOrgTranButton().setVisible(canAdd);
		this.bean.getEditUomGroupOrgTranButton().setVisible(canEdit);
		this.bean.getDelUomGroupOrgTranButton().setVisible(canDel);
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
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_POLITICAL_UOM_GROUP_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_POLITICAL_UOM_GROUP_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_POLITICAL_UOM_GROUP_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_UOM_GROUP_ORG_TRAN_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_UOM_GROUP_ORG_TRAN_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_UOM_GROUP_ORG_TRAN_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_UOM_GROUP_ORG_TRAN_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_UOM_GROUP_ORG_TRAN_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_UOM_GROUP_ORG_TRAN_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_UOM_GROUP_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_UOM_GROUP_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_SUPPLIER_UOM_GROUP_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("ossTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_UOM_GROUP_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_UOM_GROUP_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_OSS_UOM_GROUP_ORG_TRAN_DEL)) {
				// canDel = true;
				// }
			} else if ("edwTab".equals(orgTreeTabName)) {
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_UOM_GROUP_ORG_TRAN_ADD)) {
				// canAdd = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_UOM_GROUP_ORG_TRAN_EDIT)) {
				// canEdit = true;
				// }
				// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
				// ActionKeys.ORG_TREE_EDW_UOM_GROUP_ORG_TRAN_DEL)) {
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

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_DEL)) {
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

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_DEL)) {
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
                        ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_ADD)) {
                        canAdd = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_EDIT)) {
                        canEdit = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_UOM_GROUP_ORG_TRAN_DEL)) {
                        canDel = true;
                    }
                    
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_UOM_GROUP_ORG_TRAN_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_UOM_GROUP_ORG_TRAN_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_UOM_GROUP_ORG_TRAN_DEL)) {
					canDel = true;
				}
			}
		}
		this.bean.getAddUomGroupOrgTranButton().setVisible(canAdd);
		this.bean.getEditUomGroupOrgTranButton().setVisible(canEdit);
		this.bean.getDelUomGroupOrgTranButton().setVisible(canDel);
	}
	
    public String checkOrgType(UomGroupOrgTran uomGroupOrgTran) {
        String msg = "";
        // 1、选中的组织不为空
        if (uomGroupOrgTran.getOrgId() == null) {
            return "未选中对应组织";
        }
        // 2、选中的关联关系不为空
        if (uomGroupOrgTran.getTranRelaType() == null) {
            return "未选中相应的关联关系";
        }
        // 3、选中对应的关联关系之后对应的组织的组织类型匹配
        // 3、1 获取选中组织的组织类型
        List<OrgType> orgTypeList = uomGroupOrgTran.getOrganizationOrgTypeList();
        // 3、2 根据选中的关联关系进行判断是否匹配相应的组织类型
        // 3、2、1 组织页面有4种关系
        // (1) 统一目录 （2） 网点与成本中心 （3） 网点与供应商 （4） 营销与成本中心
        String tranRelaType = uomGroupOrgTran.getTranRelaType();
        
        List<AttrValue> typeList = null;
        if (tranRelaType.equals(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE)) {
            // 统一目录
            String regexp = "^N01.{3}[^4]";
            typeList = uomGroupOrgTranManager.queryOrgTypeByRegexp(regexp);
        } else if (tranRelaType
            .equals(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE)
            || tranRelaType.equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE)) {
            // 关联是的供应商和成本中心的正则
            String regexp = "^N02";
            typeList = uomGroupOrgTranManager.queryOrgTypeByRegexp(regexp);
        } else if (tranRelaType
            .equals(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE)) {
            String regexp = "^N11";
            typeList = uomGroupOrgTranManager.queryOrgTypeByRegexp(regexp);
        }
        
        // 判断当前选中的组织类型和提取出来的组织类型相匹配
        // 遍历提取出来的类型，和当前组织类型比对
        boolean findOk = false;
        for (OrgType orgType : orgTypeList) {
            if (findOk == true) {
                break;
            }
            // 判断的当前组织类型是否和后面对应的组织类型匹配
            for (AttrValue attrValue : typeList) {
                if (orgType.getOrgTypeCd().equals(attrValue.getAttrValue())) {
                    // 这里匹配成功
                    findOk = true;
                    break;
                }
            }
        }
        
        // 如果是false标识没有匹配成功
        if (findOk == false)
            msg = "所选组织无法添加相应的跨域关系";
        
        return msg;
    }
}
