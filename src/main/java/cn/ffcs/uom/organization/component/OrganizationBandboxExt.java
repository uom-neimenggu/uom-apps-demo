package cn.ffcs.uom.organization.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.OrganizationListboxComposer;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class OrganizationBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/organization_bandbox_ext.zul";
	/**
	 * 选择的组织
	 */
	@Getter
	private Organization organization;
	/**
	 * 组织列表
	 */
	@Getter
	@Setter
	private OrganizationListboxComposer organizationListboxExt;
	/**
	 * 是否是代理商
	 */
	@Getter
	@Setter
	private Boolean isAgent = false;
	/**
	 * 是否是内部经营实体
	 */
	@Getter
	@Setter
	private Boolean isIbe = false;
	/**
	 * 是否是选代理商根节点
	 */
	@Getter
	@Setter
	private Boolean isChooseAgentRoot = false;
	/**
	 * 是否是选内部经营实体根节点
	 */
	@Getter
	@Setter
	private Boolean isChooseIbeRoot = false;
	/**
	 * 是否包含营业网点
	 */
	@Getter
	@Setter
	private Boolean isContainSalesNetwork = false;
	/**
	 * 是否包含内部经营实体营业网点
	 */
	@Getter
	@Setter
	private Boolean isContainIbeSalesNetwork = false;
	/**
	 * 是否排除代理商组织
	 */
	@Getter
	@Setter
	private Boolean isExcluseAgent = false;
	/**
	 * 是否排除内部经营实体组织
	 */
	@Getter
	@Setter
	private Boolean isExcluseIbe = false;
	
	/**
	 * 是否是包区组织
	 */
	@Setter
	@Getter
	private Boolean isPackArea = false;
	
	/**
	 * 是否是代理商渠道网点
	 * 
	 */
	@Setter
    @Getter
	private Boolean isAgentChannel = false;

	/**
	 * 查找的组织类型列表
	 */
	@Getter
	@Setter
	private String orgTypeList;

	/**
	 * 排除的组织id列表
	 */
	@Getter
	@Setter
	private String excludeOrgIdList;
	/**
	 * 是否是配置等页面（忽略数据权，电信管理区域默认中国）
	 */
	@Getter
	@Setter
	private Boolean isConfigPage = false;

	private boolean isLoaded = false;

	/**
	 * 构造函数
	 */
	public OrganizationBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		organizationListboxExt.getBean().getOrganizationListPaging()
				.setPageSize(10);
		organizationListboxExt.getBean().getOrganizationWindowDiv()
				.setVisible(false);
		organizationListboxExt.getBean().getOrganizationBandboxDiv()
				.setVisible(true);
//		if(isPackArea)
//		{
//		    //如果是渠道包区页面，展示一个下拉框进行选择是营销2017还是2016
//		    organizationListboxExt.getBean().getYear().setVisible(true);
//		}
		

		/**
		 * 监听事件
		 */
		this.organizationListboxExt.addForward(
				OrganizationConstant.ON_SELECT_ORGANIZATION, this,
				"onSelectOrganizationResponse");
		this.organizationListboxExt.addForward(
				OrganizationConstant.ON_CLEAN_ORGANIZATION, this,
				"onCleanOrganizationResponse");
		this.organizationListboxExt.addForward(
				OrganizationConstant.ON_CLOSE_ORGANIZATION, this,
				"onCloseOrganizationResponse");
		/**
		 * 数据权限组织配置区域控制
		 */
		// this.addForward("onConfigOrgDataRequest", this,
		// "onConfigOrgDataResponse");
		/**
		 * 添加点击查询事件
		 */
		this.addEventListener("onOpen", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (!isLoaded) {
					// 去除默认查询organizationListboxExt.onQueryOrganization();
				}
				isLoaded = true;
			}
		});

	}

	/**
	 * 创建接受参数,设置组织类型
	 * @throws Exception 
	 */
	public void onCreate() throws Exception {
        if (isChooseAgentRoot) {
            Events.postEvent(OrganizationConstant.ON_CHOOSE_AGENT_ORGANIZATION_ROOT_REQUEST,
                this.organizationListboxExt, isChooseAgentRoot);
        } else if (isChooseIbeRoot) {
            Events.postEvent(OrganizationConstant.ON_CHOOSE_IBE_ORGANIZATION_ROOT_REQUEST,
                this.organizationListboxExt, isChooseIbeRoot);
        } else if (isPackArea) {
            Events.postEvent(OrganizationConstant.ON_PACKARE_ORGANIZATION_REQUEST,
                this.organizationListboxExt, isPackArea);
        } else if(isAgentChannel) {
            Events.postEvent(OrganizationConstant.ON_AGENT_CHANNNEL_ORGANIZATION_REQUEST,
                this.organizationListboxExt, isAgentChannel);
        }
        else {
            /**
             * 选择根节点包含选择代理商
             */
            if (isAgent) {
                Events.postEvent(OrganizationConstant.ON_SET_AGENT_ORGANIZATION_REQUEST,
                    this.organizationListboxExt, isAgent);
            }
            /**
             * 选择根节点包含选择内部经营实体
             */
            if (isIbe) {
                Events.postEvent(OrganizationConstant.ON_SET_IBE_ORGANIZATION_REQUEST,
                    this.organizationListboxExt, isIbe);
            }
        }
		/**
		 * 代理商且包含营业网点
		 */
		if (isContainSalesNetwork) {
			Events.postEvent(
					OrganizationConstant.ON_SET_AGENT_CONTAIN_SALESNETWORK_REQUEST,
					this.organizationListboxExt, isContainSalesNetwork);
		}
		/**
		 * 内部经营实体且包含营业网点
		 */
		if (isContainIbeSalesNetwork) {
			Events.postEvent(
					OrganizationConstant.ON_SET_IBE_CONTAIN_SALESNETWORK_REQUEST,
					this.organizationListboxExt, isContainIbeSalesNetwork);
		}
		/**
		 * 组织类型范围
		 */
		if (!StrUtil.isEmpty(this.orgTypeList)
				|| !StrUtil.isEmpty(this.excludeOrgIdList)) {
			Map map = new HashMap();
			map.put("orgTypeList", this.orgTypeList);
			map.put("excludeOrgIdList", this.excludeOrgIdList);
			Events.postEvent(OrganizationConstant.ON_SET_ORGTYPE_REQUEST,
					this.organizationListboxExt, map);
		}
		/**
		 * 公开页面（数据权忽略电信管理区域）
		 */
		if (isConfigPage) {
			Map map = new HashMap();
			TelcomRegion permissionTelcomRegion = new TelcomRegion();
			permissionTelcomRegion
					.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			map.put("configTelcomRegion", permissionTelcomRegion);
			Events.postEvent(OrganizationConstant.ON_SET_CONFIG_PAGE_REQUEST,
					this.organizationListboxExt, map);
		}
	}

	public Object getAssignObject() {
		return getOrganization();
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof Organization) {
			setOrganization((Organization) assignObject);
		}
	}

	public void setOrganization(Organization organization) {
		this.setValue(organization == null ? "" : organization.getOrgName());
		this.organization = organization;
	}

	/**
	 * 选择组织
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectOrganizationResponse(final ForwardEvent event)
			throws Exception {
		organization = (Organization) event.getOrigin().getData();
		if (organization != null) {
			this.setValue(organization.getOrgName());
		}
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanOrganizationResponse(final ForwardEvent event)
			throws Exception {
		this.setOrganization(null);
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 关闭窗口
	 * 
	 * @param eventt
	 * @throws Exception
	 */
	public void onCloseOrganizationResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

	/**
	 * 数据权限配置组织权限时设置组织列表查询区域
	 * 
	 * @param event
	 * @throws Exception
	 */
	// public void onConfigOrgDataResponse(final ForwardEvent event)
	// throws Exception {
	// if (event.getOrigin() != null && event.getOrigin().getData() != null) {
	// Long aroleId = (Long) event.getOrigin().getData();
	// if (aroleId != null) {
	// TelcomRegion permissionTelcomRegion = PermissionUtil
	// .getPermissionTelcomRegion(new long[] { aroleId });
	// if (permissionTelcomRegion != null) {
	// this.organizationListboxExt.setPermissionTelcomRegion(permissionTelcomRegion);
	// }
	// }
	// }
	// }

	/**
	 * 设置代理商初始化
	 * 
	 * @param isAgent
	 */
	public void setAgentInit(boolean isAgent) {
		this.isAgent = isAgent;
		Events.postEvent(
				OrganizationConstant.ON_SET_AGENT_ORGANIZATION_REQUEST,
				this.organizationListboxExt, isAgent);
	}

	/**
	 * 设置内部经营实体初始化
	 * 
	 * @param isIbe
	 */
	public void setIbeInit(boolean isIbe) {
		this.isIbe = isIbe;
		Events.postEvent(OrganizationConstant.ON_SET_IBE_ORGANIZATION_REQUEST,
				this.organizationListboxExt, isIbe);
	}

	/**
	 * 设置代理商且包含营业网点初始化
	 * 
	 * @param isContainSalesNetwork
	 */
	public void setContainSalesNetworkInit(boolean isContainSalesNetwork) {
		this.isContainSalesNetwork = isContainSalesNetwork;
		if (isContainSalesNetwork) {
			this.isAgent = isContainSalesNetwork;
		}
		Events.postEvent(
				OrganizationConstant.ON_SET_AGENT_CONTAIN_SALESNETWORK_REQUEST,
				this.organizationListboxExt, isContainSalesNetwork);
	}

	/**
	 * 设置内部经营实体且包含营业网点初始化
	 * 
	 * @param isContainIbeSalesNetwork
	 */
	public void setContainIbeSalesNetworkInit(boolean isContainIbeSalesNetwork) {
		this.isContainIbeSalesNetwork = isContainIbeSalesNetwork;
		if (isContainIbeSalesNetwork) {
			this.isIbe = isContainIbeSalesNetwork;
		}
		Events.postEvent(
				OrganizationConstant.ON_SET_IBE_CONTAIN_SALESNETWORK_REQUEST,
				this.organizationListboxExt, isContainIbeSalesNetwork);
	}

	/**
	 * 组织树页面添加下级节点：不包含代理商组织
	 * 
	 * @param isAgent
	 */
	public void setExcluseAgentInit(boolean isExcluseAgent) {
		this.isExcluseAgent = isExcluseAgent;
		Events.postEvent(
				OrganizationConstant.ON_EXCLUDE_AGENT_ORGANIZATION_REQUEST,
				this.organizationListboxExt, isExcluseAgent);
	}

	/**
	 * 组织树页面添加下级节点：不包含内部经营实体组织
	 * 
	 * @param isExcluseIbe
	 */
	public void setExcluseIbeInit(boolean isExcluseIbe) {
		this.isExcluseIbe = isExcluseIbe;
		Events.postEvent(
				OrganizationConstant.ON_EXCLUDE_IBE_ORGANIZATION_REQUEST,
				this.organizationListboxExt, isExcluseIbe);
	}
	
	/**
	 * 
	 * @param isExcluseIbe
	 */
	public void setMultidimensionalTreeInit(String orgTreeRootId) {
		final Map map = new HashMap();
		map.put("orgTreeRootId", orgTreeRootId);
		Events.postEvent(OrganizationConstant.ON_MULTIDIMENSIONAL_TREE_ORGANIZATION_QUERY,this.organizationListboxExt, map);
	}

	/**
	 * 设置组织类型列表
	 * 
	 * @param orgTypeStr
	 */
	public void setSupplierOrgTypeList(Map map) {
		/**
		 * 组织类型范围
		 */
		Events.postEvent(OrganizationConstant.ON_SET_ORGTYPE_REQUEST,
				this.organizationListboxExt, map);
	}
}
