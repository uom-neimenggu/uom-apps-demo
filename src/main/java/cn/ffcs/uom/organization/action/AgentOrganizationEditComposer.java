package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.action.bean.AgentOrganizationEditBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;

/**
 * 组织树新增组织
 * 
 * @author ZhaoF
 * 
 */
@Controller
@Scope("prototype")
public class AgentOrganizationEditComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private AgentOrganizationEditBean bean = new AgentOrganizationEditBean();
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 选中的组织
	 */
	private Organization selectOrganization;

	/**
	 * manager
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	/**
	 * manager
	 */
	private OrgTreeConfigManager orgTreeConfigManager = (OrgTreeConfigManager) ApplicationContextUtil
			.getBean("orgTreeConfigManager");
	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager) ApplicationContextUtil
			.getBean("orgTypeManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getPartyInfoExt().addForward("onUpdateOrgRequest", this.self,
				"onUpdateOrgResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$agentOrganizationEditWindow() throws Exception {
		this.bindBean();
	}

	/**
	 * 页面bean
	 */
	private void bindBean() {
		opType = (String) arg.get("opType");
		selectOrganization = (Organization) arg.get("organization");
		this.bean.getOrganizationInfoExt().setOpType("add");
		if ("addAgentRootNode".equals(opType)
				|| "addAgentChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增代理商组织");
			/**
			 * 代理商或网点标识
			 */
			this.bean.getOrganizationInfoExt().setOpType(opType);
			/**
			 * 设置默认值 组织性质为外部组织 1100 存在类型为实体组织 1 电信管理区域为上级电信管理区域 行政管理区域为上级行政管理区域
			 */

			Organization organization = new Organization();
			organization.setOrgType("1100");
			organization.setExistType("1");

			if (selectOrganization != null) {
				if (selectOrganization.getTelcomRegionId() != null) {
					organization.setTelcomRegionId(selectOrganization
							.getTelcomRegionId());
				}

				if (selectOrganization.getLocationId() != null) {
					organization.setLocationId(selectOrganization
							.getLocationId());
				}
			}

			/**
			 * 是否新增员工隐藏
			 */
			this.bean.getPartyInfoExt().getBean().getIsAddStaffRow()
					.setVisible(false);
			/**
			 * 代理商角色
			 */
			List partyRoleTypeList = new ArrayList();
			partyRoleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_AGENT);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setInitialValue(partyRoleTypeList);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);

			/**
			 * 组织参与人bandbox
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgParty()
					.setDisabled(true);
			if ("addAgentRootNode".equals(opType)) {

				/**
				 * 选择新增内部经营实体为默认选项
				 */
				ListboxUtils.selectByCodeValue(this.bean.getAddType(), "AG");

				/**
				 * 组织类型
				 */
				List<String> orgTypeCd = new ArrayList();
				orgTypeCd.add(OrganizationConstant.ORG_TYPE_AGENT);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCd);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setDisabled(true);
				/**
				 * 代理商或网点标识
				 */
				this.bean.getPartyInfoExt().setOpType(opType);
				/**
				 * 设置默认值 参与人类型为组织 2
				 */
				Party party = new Party();
				party.setPartyType("2");
				this.bean.getPartyInfoExt().setParty(party, opType);

			} else if ("addAgentChildNode".equals(opType)) {
				/**
				 * 选择代理商按钮展示
				 */
				this.bean.getAddTypeGorupbox().setVisible(true);

				List<String> addTypeList = new ArrayList<String>();
				addTypeList.add("NW");
				addTypeList.add("AG");
				ListboxUtils.isVisibleByCodeValue(this.bean.getAddType(),
						addTypeList);

				/**
				 * 选择新增营业网点为默认选项
				 */
				ListboxUtils.selectByCodeValue(this.bean.getAddType(), "NW");

				/**
				 * 添加子节点是初始化新增代理商的组织类型
				 */
				if (this.bean.getAddType() != null
						&& this.bean.getAddType().getSelectedItem() != null) {
					if ("AG".equals(this.bean.getAddType().getSelectedItem()
							.getValue())) {
						this.bean.getPartyInfoExt().setVisible(true);
						/**
						 * 将组织类型置空
						 */
						organization.setOrgTypeList(null);
					} else {
						this.bean.getAgentOrganizationEditWindow().setTitle(
								"新增营业网点组织");
						this.bean.getPartyInfoExt().setVisible(false);
						/**
						 * 设置默认值 组织类型为社会实体渠道 N0202020000
						 */
						List<OrgType> orgTypeList = new ArrayList<OrgType>();
						OrgType orgType = new OrgType();
						orgType.setOrgTypeCd("N0202020000");
						orgTypeList.add(orgType);
						organization.setOrgTypeList(orgTypeList);
					}
				}
			}
			/**
			 * 设置默认值
			 */
			this.bean.getOrganizationInfoExt().setOrganization(organization);
			if ("addAgentRootNode".equals(opType)) {
				/**
				 * 组织类型设置成代理商 防止上面一条语句的覆盖
				 */
				List<String> orgTypeCd = new ArrayList();
				orgTypeCd.add(OrganizationConstant.ORG_TYPE_AGENT);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCd);
			}

			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(true);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
			/**
			 * 组织层级控制--编辑
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlDisable(selectOrganization, true);

		} else if ("addIbeRootNode".equals(opType)
				|| "addIbeChildNode".equals(opType)) {

			this.bean.getAgentOrganizationEditWindow().setTitle("新增内部经营实体");
			/**
			 * 内部经营实体或网点标识
			 */
			this.bean.getOrganizationInfoExt().setOpType(opType);
			/**
			 * 设置默认值 组织性质为外部组织 1100 存在类型为实体组织 1 电信管理区域为上级电信管理区域 行政管理区域为上级行政管理区域
			 */

			Organization organization = new Organization();
			organization.setOrgType("1100");
			organization.setExistType("1");

			if (selectOrganization != null) {
				if (selectOrganization.getTelcomRegionId() != null) {
					organization.setTelcomRegionId(selectOrganization
							.getTelcomRegionId());
				}

				if (selectOrganization.getLocationId() != null) {
					organization.setLocationId(selectOrganization
							.getLocationId());
				}
			}

			/**
			 * 是否新增员工隐藏
			 */
			this.bean.getPartyInfoExt().getBean().getIsAddStaffRow()
					.setVisible(false);
			/**
			 * 代理商角色
			 */
			List partyRoleTypeList = new ArrayList();
			partyRoleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_AGENT);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setInitialValue(partyRoleTypeList);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);

			/**
			 * 组织参与人bandbox
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgParty()
					.setDisabled(true);
			if ("addIbeRootNode".equals(opType)) {

				/**
				 * 选择新增内部经营实体为默认选项
				 */
				ListboxUtils.selectByCodeValue(this.bean.getAddType(), "IBE");

				/**
				 * 组织类型
				 */
				List<String> orgTypeCd = new ArrayList();
				orgTypeCd.add(OrganizationConstant.ORG_TYPE_N0903000000);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCd);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setDisabled(true);
				/**
				 * 内部经营实体或网点标识
				 */
				this.bean.getPartyInfoExt().setOpType(opType);
				/**
				 * 设置默认值 参与人类型为组织 2
				 */
				Party party = new Party();
				party.setPartyType("2");
				this.bean.getPartyInfoExt().setParty(party, opType);

			} else if ("addIbeChildNode".equals(opType)) {
				/**
				 * 选择内部经营实体按钮展示
				 */
				this.bean.getAddTypeGorupbox().setVisible(true);

				List<String> addTypeList = new ArrayList<String>();
				addTypeList.add("IBENW");
				addTypeList.add("IBE");
				ListboxUtils.isVisibleByCodeValue(this.bean.getAddType(),
						addTypeList);

				/**
				 * 选择新增营业网点为默认选项
				 */
				ListboxUtils.selectByCodeValue(this.bean.getAddType(), "IBENW");

				/**
				 * 添加子节点是初始化新增内部经营实体的组织类型
				 */
				if (this.bean.getAddType() != null
						&& this.bean.getAddType().getSelectedItem() != null) {
					if ("IBE".equals(this.bean.getAddType().getSelectedItem()
							.getValue())) {
						this.bean.getPartyInfoExt().setVisible(true);
						/**
						 * 将组织类型置空
						 */
						organization.setOrgTypeList(null);
					} else {
						this.bean.getAgentOrganizationEditWindow().setTitle(
								"新增营业网点组织");
						this.bean.getPartyInfoExt().setVisible(false);
						/**
						 * 设置默认值 组织类型为自营实体渠道 N0202010000
						 */
						List<OrgType> orgTypeList = new ArrayList<OrgType>();
						OrgType orgType = new OrgType();
						orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202010000);
						orgTypeList.add(orgType);
						organization.setOrgTypeList(orgTypeList);
					}
				}
			}
			/**
			 * 设置默认值
			 */
			this.bean.getOrganizationInfoExt().setOrganization(organization);
			if ("addIbeRootNode".equals(opType)) {
				/**
				 * 组织类型设置成内部经营实体 防止上面一条语句的覆盖
				 */
				List<String> orgTypeCd = new ArrayList();
				orgTypeCd.add(OrganizationConstant.ORG_TYPE_N0903000000);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCd);
			}

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(true);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
			/**
			 * 组织层级控制--编辑
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlDisable(selectOrganization, true);

		} else if ("addRootNode".equals(opType)
				|| "addChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);

		} else if ("addSupplierChildNode".equals(opType)
				|| "addSupplierRootNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增供应商组织");
			/**
			 * 是否新增员工隐藏
			 */
			this.bean.getPartyInfoExt().getBean().getIsAddStaffRow()
					.setVisible(false);
			/**
			 * 供应商参与人角色
			 */
			List partyRoleTypeList = new ArrayList();
			partyRoleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_SUPPLIER);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setInitialValue(partyRoleTypeList);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);
			/**
			 * 供应商维护逻辑： 1、顶级只能增加N0902000000属性类型的组织，同时需要设定为供应商的参与人；
			 * 2、非顶级节点，按正常可以设置的任意一种： N1001010100 集团(外部) N1001010200 公司(外部)
			 * N1001020100 部门(外部) N1001040100 班组(外部)
			 */
			List<String> optionAttrValueList = new ArrayList();
			if ("addSupplierChildNode".equals(opType)) {
				optionAttrValueList
						.add(OrganizationConstant.ORG_TYPE_N1001010100);
				optionAttrValueList
						.add(OrganizationConstant.ORG_TYPE_N1001010200);
				optionAttrValueList
						.add(OrganizationConstant.ORG_TYPE_N1001020100);
				optionAttrValueList
						.add(OrganizationConstant.ORG_TYPE_N1001040100);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setOptionNodes(optionAttrValueList);
			} else if ("addSupplierRootNode".equals(opType)) {
				List<String> codeList = new ArrayList<String>();
				codeList.add(OrganizationConstant.ORG_TYPE_N0902000000);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(codeList);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setDisabled(true);
			}
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
		} else if ("addOssChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增中通服组织");
			/**
			 * 是否新增员工隐藏
			 */
			this.bean.getPartyInfoExt().getBean().getIsAddStaffRow()
					.setVisible(false);
			/**
			 * 实业公司参与人角色
			 */
			List partyRoleTypeList = new ArrayList();
			partyRoleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_INDUSTRIES);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setInitialValue(partyRoleTypeList);
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
		} else if ("addEdwRootNode".equals(opType)
				|| "addEdwChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			List<String> optionAttrValueList = new ArrayList<String>();
			TreeOrgTypeRule totr = new TreeOrgTypeRule();
			totr.setOrgTreeId(OrganizationConstant.CUSTMS_TREE_ID);
			List<OrgType> queryOrgTypeList = orgTypeManager
					.getOrgTypeList(totr);
			if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
				for (OrgType orgType : queryOrgTypeList) {
					optionAttrValueList.add(orgType.getOrgTypeCd());
				}
			}
			/**
			 * 营销树页面，若新增组织，地址为非必填，组织类型为营销类型
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
					.setOptionNodes(optionAttrValueList);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
		} else if ("addMarketingRootNode".equals(opType)
				|| "addMarketingChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			/**
			 * 组织扩展属性
			 */
			// this.bean.getOrganizationInfoExt().getBean()
			// .getOrganizationExtendAttrExt().setAllDisable(false);
			/**
			 * 营销树2015Tab页面
			 */
			// this.bean.getOrganizationInfoExt().getBean()
			// .getOrganizationExtendAttrExt().setIsMarketingTab(true);

			/**
			 * 组织层级控制--显示
			 */
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlVisible(selectOrganization, true,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
			/**
			 * 组织层级控制--编辑
			 */
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlDisable(selectOrganization, true,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

			List<String> optionAttrValueList = new ArrayList<String>();
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101040000);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101050000);

			// TreeOrgTypeRule totr = new TreeOrgTypeRule();
			// totr.setOrgTreeId(OrganizationConstant.MARKETING_TREE_ID);
			// List<OrgType> queryOrgTypeList = orgTypeManager
			// .getOrgTypeList(totr);
			// if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
			// for (OrgType orgType : queryOrgTypeList) {
			// optionAttrValueList.add(orgType.getOrgTypeCd());
			// }
			// }

			/**
			 * 营销树页面，若新增组织，地址为非必填，组织类型为营销类型
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
					.setOptionNodes(optionAttrValueList);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
		} else if ("addNewMarketingRootNode".equals(opType)
				|| "addNewMarketingChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			/**
			 * 组织扩展属性
			 */
			// this.bean.getOrganizationInfoExt().getBean()
			// .getOrganizationExtendAttrExt().setAllDisable(false);
			/**
			 * 营销树2015Tab页面
			 */
			// this.bean.getOrganizationInfoExt().getBean()
			// .getOrganizationExtendAttrExt().setIsMarketingTab(true);

			/**
			 * 组织层级控制--显示
			 */
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlVisible(
							selectOrganization,
							true,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
			/**
			 * 组织层级控制--编辑
			 */
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlDisable(
							selectOrganization,
							true,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

			List<String> optionAttrValueList = new ArrayList<String>();
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101040000);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101050000);

			// TreeOrgTypeRule totr = new TreeOrgTypeRule();
			// totr.setOrgTreeId(OrganizationConstant.MARKETING_TREE_ID);
			// List<OrgType> queryOrgTypeList = orgTypeManager
			// .getOrgTypeList(totr);
			// if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
			// for (OrgType orgType : queryOrgTypeList) {
			// optionAttrValueList.add(orgType.getOrgTypeCd());
			// }
			// }

			/**
			 * 营销树页面，若新增组织，地址为非必填，组织类型为营销类型
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
					.setOptionNodes(optionAttrValueList);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
        } else if ("addNewSeventeenMarketingRootNode".equals(opType)
            || "addNewSeventeenMarketingChildNode".equals(opType)) {
            this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
            this.bean.getPartyInfoExt().setVisible(false);
            /**
             * 组织扩展属性
             */
            // this.bean.getOrganizationInfoExt().getBean()
            // .getOrganizationExtendAttrExt().setAllDisable(false);
            /**
             * 营销树2015Tab页面
             */
            // this.bean.getOrganizationInfoExt().getBean()
            // .getOrganizationExtendAttrExt().setIsMarketingTab(true);
            
            /**
             * 组织层级控制--显示
             */
            this.bean
                .getOrganizationInfoExt()
                .getBean()
                .getOrganizationExtendAttrExt()
                .orgLevelControlVisible(selectOrganization, true,
                    OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
            /**
             * 组织层级控制--编辑
             */
            this.bean
                .getOrganizationInfoExt()
                .getBean()
                .getOrganizationExtendAttrExt()
                .orgLevelControlDisable(selectOrganization, true,
                    OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
            
            List<String> optionAttrValueList = new ArrayList<String>();
            optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101040000);
            optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101050000);
            
            // TreeOrgTypeRule totr = new TreeOrgTypeRule();
            // totr.setOrgTreeId(OrganizationConstant.MARKETING_TREE_ID);
            // List<OrgType> queryOrgTypeList = orgTypeManager
            // .getOrgTypeList(totr);
            // if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
            // for (OrgType orgType : queryOrgTypeList) {
            // optionAttrValueList.add(orgType.getOrgTypeCd());
            // }
            // }
            
            /**
             * 营销树页面，若新增组织，地址为非必填，组织类型为营销类型
             */
            this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
                .setOptionNodes(optionAttrValueList);
            setOrganizationInfoExtStyle();
            /**
             * 是否是代理商tab
             */
            this.bean.getOrganizationInfoExt().getBean().getOrganizationExtendAttrExt()
                .setIsAgentTab(false);
            
            /**
             * 是否是经营实体tab
             */
            this.bean.getOrganizationInfoExt().getBean().getOrganizationExtendAttrExt()
                .setIsIbeTab(false);
            
            /**
             * 组织层级控制--显示
             */
            this.bean.getOrganizationInfoExt().getBean().getOrganizationExtendAttrExt()
                .orgChannelControlVisible(selectOrganization, true);
        } else if ("addCostRootNode".equals(opType)
				|| "addCostChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			List<String> optionAttrValueList = new ArrayList<String>();
			TreeOrgTypeRule totr = new TreeOrgTypeRule();
			totr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
			List<OrgType> queryOrgTypeList = orgTypeManager
					.getOrgTypeList(totr);
			if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
				for (OrgType orgType : queryOrgTypeList) {
					optionAttrValueList.add(orgType.getOrgTypeCd());
				}
			}
			/**
			 * 营销树页面，若新增组织，地址为非必填，组织类型为营销类型
			 */
			this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
					.setOptionNodes(optionAttrValueList);
			setOrganizationInfoExtStyle();
			/**
			 * 是否是代理商tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsAgentTab(false);

			/**
			 * 是否是经营实体tab
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt().setIsIbeTab(false);

			/**
			 * 组织层级控制--显示
			 */
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlVisible(selectOrganization, true);
		} else if ("addMultidimensionalTreeChildNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("新增组织");
			this.bean.getPartyInfoExt().setVisible(false);
			String orgTreeRootId = (String) arg.get("orgTreeRootId");
			List<OrgTreeConfig> orgTreeConfigList = orgTreeConfigManager
					.findOrgType(Long.valueOf(orgTreeRootId));
			List<String> optionAttrValueList = new ArrayList<String>();
			if (null != orgTreeConfigList && orgTreeConfigList.size() > 0) {
				for (OrgTreeConfig orgTreeConfig : orgTreeConfigList) {
					optionAttrValueList.add(orgTreeConfig.getOrgTypeCd());
				}
			}
			this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
					.setOptionNodes(optionAttrValueList);
			setOrganizationInfoExtStyle();
		} else if ("addOrgRootNode".equals(opType)) {
			this.bean.getAgentOrganizationEditWindow().setTitle("添加组织树");
			this.bean.getPartyInfoExt().setVisible(false);
			setOrganizationInfoExtStyle();
		}
	}

	/**
	 * 功能说明:设置组织信息控件样式 创建人:俸安琪 创建时间:2014-7-4 上午10:43:40 void
	 */
	private void setOrganizationInfoExtStyle() {
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setClass("");
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.removeAttribute("class");
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setValue("地址");
	}

	/**
	 * 点击保存
	 */
	public void onOk() {
		String msg = null;
		Party party = null;
		boolean validPartyInfo = false;
		if ("addAgentRootNode".equals(opType)) {
			msg = this.bean.getPartyInfoExt().getDoValidPartyInfo();
			validPartyInfo = true;
			if (!StrUtil.isEmpty(msg)) {
				ZkUtil.showError(msg, "提示信息");
				return;
			}
			/**
			 * 参与人
			 */
			this.bean.getPartyInfoExt().getParty();
			party = this.bean.getPartyInfoExt().getOutsideParty();
		} else if ("addAgentChildNode".equals(opType)) {
			if (this.bean.getAddType() != null
					&& this.bean.getAddType().getSelectedItem() != null) {
				if ("AG".equals(this.bean.getAddType().getSelectedItem()
						.getValue())) {
					/**
					 * 新增代理商才有参与人，新增网点没有参与人
					 */
					msg = this.bean.getPartyInfoExt().getDoValidPartyInfo();
					validPartyInfo = true;
					if (!StrUtil.isEmpty(msg)) {
						ZkUtil.showError(msg, "提示信息");
						return;
					}
					/**
					 * 参与人
					 */

					this.bean.getPartyInfoExt().getParty();
					party = this.bean.getPartyInfoExt().getOutsideParty();
				}
			}
		} else if ("addIbeRootNode".equals(opType)) {
			msg = this.bean.getPartyInfoExt().getDoValidPartyInfo();
			validPartyInfo = true;
			if (!StrUtil.isEmpty(msg)) {
				ZkUtil.showError(msg, "提示信息");
				return;
			}
			/**
			 * 参与人
			 */
			this.bean.getPartyInfoExt().getParty();
			party = this.bean.getPartyInfoExt().getOutsideParty();
		} else if ("addIbeChildNode".equals(opType)) {
			if (this.bean.getAddType() != null
					&& this.bean.getAddType().getSelectedItem() != null) {
				if ("IBE".equals(this.bean.getAddType().getSelectedItem()
						.getValue())) {
					/**
					 * 新增内部经营实体才有参与人，新增网点没有参与人
					 */
					msg = this.bean.getPartyInfoExt().getDoValidPartyInfo();
					validPartyInfo = true;
					if (!StrUtil.isEmpty(msg)) {
						ZkUtil.showError(msg, "提示信息");
						return;
					}
					/**
					 * 参与人
					 */

					this.bean.getPartyInfoExt().getParty();
					party = this.bean.getPartyInfoExt().getOutsideParty();
				}
			}
		} else if ("addSupplierRootNode".equals(opType)
				|| "addSupplierChildNode".equals(opType)
				|| "addOssChildNode".equals(opType)
				|| "addOssRootNode".equals(opType)) {
			/**
			 * 参与人角色类型在初始化的已经限制，所以判断参与人角色类型了
			 */
			msg = this.bean.getPartyInfoExt().getDoValidPartyInfo();
			validPartyInfo = true;
			if (!StrUtil.isEmpty(msg)) {
				ZkUtil.showError(msg, "提示信息");
				return;
			}
			/**
			 * 参与人
			 */

			this.bean.getPartyInfoExt().getParty();
			party = this.bean.getPartyInfoExt().getOutsideParty();
		}
		msg = this.bean.getOrganizationInfoExt().getDoValidOrganizationInfo();

		/**
		 * 非代理商树页面，若新增修改组织，地址若未填让其通过
		 */
		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)
				|| "addEdwChildNode".equals(opType)
				|| "addEdwRootNode".equals(opType)
				|| "addMarketingChildNode".equals(opType)
				|| "addMarketingRootNode".equals(opType)
				|| "addNewMarketingChildNode".equals(opType)
				|| "addNewMarketingRootNode".equals(opType)
				|| "addNewSeventeenMarketingChildNode".equals(opType)
				|| "addNewSeventeenMarketingRootNode".equals(opType)
				|| "addCostChildNode".equals(opType)
				|| "addCostRootNode".equals(opType)
				|| "addSupplierRootNode".equals(opType)
				|| "addSupplierChildNode".equals(opType)
				|| "addOssChildNode".equals(opType)
				|| "addOssRootNode".equals(opType)) {
			if (!OrganizationConstant.ORG_CONTRACTINFO_ADDRESS_ERROR
					.equals(msg)) {
				if (!StrUtil.isEmpty(msg)) {
					ZkUtil.showError(msg, "提示信息");
					return;
				}
			}
		} else {
			if (!StrUtil.isEmpty(msg)) {
				ZkUtil.showError(msg, "提示信息");
				return;
			}
		}
		/**
		 * 组织
		 */
		Organization organization = this.bean.getOrganizationInfoExt()
				.getOrganization();
		/**
		 * 供应商，代理商，实业公司都要填组织参与人
		 */
		if ("addAgentRootNode".equals(opType)
				|| "addAgentChildNode".equals(opType)
				|| "addIbeRootNode".equals(opType)
				|| "addIbeChildNode".equals(opType)
				|| "addSupplierRootNode".equals(opType)
				|| "addSupplierChildNode".equals(opType)
				|| "addOssChildNode".equals(opType)
				|| "addOssRootNode".equals(opType)) {
			organization.setAgentAddParty(party);
		}

		if ("addAgentChildNode".equals(opType)) {
			/**
			 * 新增组织类型必须是网点和代理商
			 */
			List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
			for (OrgType ot : addOrgTypeList) {

				// 区分内部网点和外部网点
				// if (ot.getOrgTypeCd() != null
				// && !(ot.getOrgTypeCd().equals(
				// OrganizationConstant.ORG_TYPE_AGENT)
				// || ot.getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202020000)
				// || ot.getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202050000) || ot
				// .getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202060000))) {
				// ZkUtil.showError("代理商下级节点组织类型只能是代理商和营业网点", "提示信息");
				// return;
				// }

				// 不区分内部网点和外部网点
				if (ot.getOrgTypeCd() != null
						&& !(ot.getOrgTypeCd().equals(
								OrganizationConstant.ORG_TYPE_AGENT) || ot
								.getOrgTypeCd().startsWith(
										OrganizationConstant.SALES_NETWORK_PRE))) {
					ZkUtil.showError("代理商下级节点组织类型只能是代理商和营业网点", "提示信息");
					return;
				}
			}

			if (this.bean.getAddType() != null
					&& this.bean.getAddType().getSelectedItem() != null) {
				if ("AG".equals(this.bean.getAddType().getSelectedItem()
						.getValue())) {
					/**
					 * 选择添加代理商组织类型必须有代理商
					 */
					boolean flag = false;
					for (OrgType ot : addOrgTypeList) {
						if (ot.getOrgTypeCd() != null
								&& (ot.getOrgTypeCd()
										.equals(OrganizationConstant.ORG_TYPE_AGENT))) {
							flag = true;
						}
					}
					if (!flag) {
						ZkUtil.showError("请选择代理商组织类型", "提示信息");
						return;
					}
				} else if ("NW".equals(this.bean.getAddType().getSelectedItem()
						.getValue())) {
					/**
					 * 选择添加网点组织类型不能有代理商
					 */
					boolean flag = false;
					for (OrgType ot : addOrgTypeList) {
						if (ot.getOrgTypeCd() != null
								&& (ot.getOrgTypeCd()
										.equals(OrganizationConstant.ORG_TYPE_AGENT))) {
							flag = true;
						}
					}
					if (flag) {
						ZkUtil.showError("选择添加营业网点，不能选择代理商", "提示信息");
						return;
					}
				}
			}
		} else if ("addIbeChildNode".equals(opType)) {
			/**
			 * 新增组织类型必须是内部营业网点和内部经营实体
			 */
			List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
			for (OrgType ot : addOrgTypeList) {
				// 区分内部网点和外部网点
				// if (ot.getOrgTypeCd() != null
				// && !(ot.getOrgTypeCd().equals(
				// OrganizationConstant.ORG_TYPE_N0903000000)
				// || ot.getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202010000)
				// || ot.getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202030000) || ot
				// .getOrgTypeCd()
				// .equals(OrganizationConstant.ORG_TYPE_N0202040000))) {
				// ZkUtil.showError("内部经营实体下级节点组织类型只能是内部经营实体和内部营业网点", "提示信息");
				// return;
				// }

				// 不区分内部网点和外部网点
				if (ot.getOrgTypeCd() != null
						&& !(ot.getOrgTypeCd().equals(
								OrganizationConstant.ORG_TYPE_N0903000000) || ot
								.getOrgTypeCd().startsWith(
										OrganizationConstant.SALES_NETWORK_PRE))) {
					ZkUtil.showError("内部经营实体下级节点组织类型只能是内部经营实体和内部营业网点", "提示信息");
					return;
				}
			}

			if (this.bean.getAddType() != null
					&& this.bean.getAddType().getSelectedItem() != null) {
				if ("IBE".equals(this.bean.getAddType().getSelectedItem()
						.getValue())) {
					/**
					 * 选择添加内部经营实体组织类型必须有内部经营实体
					 */
					boolean flag = false;
					for (OrgType ot : addOrgTypeList) {
						if (ot.getOrgTypeCd() != null
								&& (ot.getOrgTypeCd()
										.equals(OrganizationConstant.ORG_TYPE_N0903000000))) {
							flag = true;
						}
					}
					if (!flag) {
						ZkUtil.showError("请选择内部经营实体组织类型", "提示信息");
						return;
					}
				} else if ("IBENW".equals(this.bean.getAddType()
						.getSelectedItem().getValue())) {
					/**
					 * 选择添加网点组织类型不能有内部经营实体
					 */
					boolean flag = false;
					for (OrgType ot : addOrgTypeList) {
						if (ot.getOrgTypeCd() != null
								&& (ot.getOrgTypeCd()
										.equals(OrganizationConstant.ORG_TYPE_N0903000000))) {
							flag = true;
						}
					}
					if (flag) {
						ZkUtil.showError("选择添加的内部营业网点，不能选择内部经营实体", "提示信息");
						return;
					}
				}
			}
		} else if ("addSupplierChildNode".equals(opType)
				|| "addSupplierRootNode".equals(opType)) {
			// 组织类型只能是供应商类型
			List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
			/**
			 * 供应商组织类型
			 */
			List<String> optionAttrValueList = new ArrayList();
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N0902000000);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1001010100);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1001010200);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1001020100);
			optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1001040100);
			boolean flag = false;
			for (OrgType ot : addOrgTypeList) {
				if (ot.getOrgTypeCd() != null
						&& (!optionAttrValueList.contains(ot.getOrgTypeCd()))) {
					flag = true;
				}
			}
			if (flag) {
				ZkUtil.showError("请选则供应商正确的组织类型", "提示信息");
				return;
			}
			setAddressAsAString(organization);
		} else if ("addEdwChildNode".equals(opType)
				|| "addEdwRootNode".equals(opType)) {
			setAddressAsAString(organization);
		} else if ("addMarketingChildNode".equals(opType)
				|| "addMarketingRootNode".equals(opType)) {

			boolean isGrid = organization.isGrid(organization
					.getAddOrgTypeList());

			int orgLevel = selectOrganization
					.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE) + 1;

			if (isGrid && orgLevel == 7) {

				msg = organizationManager
						.getDoValidOrganizationExtendAttrGrid(organization);

				if (!StrUtil.isEmpty(msg)) {
					ZkUtil.showError(msg, "提示信息");
					return;
				}

			}

			setAddressAsAString(organization);
		} else if ("addNewMarketingChildNode".equals(opType)
				|| "addNewMarketingRootNode".equals(opType)) {

			boolean isGrid = organization.isGrid(organization
					.getAddOrgTypeList());

			int orgLevel = selectOrganization
					.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403) + 1;

			if (isGrid && orgLevel == 7) {

				msg = organizationManager
						.getDoValidOrganizationExtendAttrGrid(organization);

				if (!StrUtil.isEmpty(msg)) {
					ZkUtil.showError(msg, "提示信息");
					return;
				}

			}

			setAddressAsAString(organization);
        } else if ("addNewSeventeenMarketingChildNode".equals(opType)
            || "addNewSeventeenMarketingRootNode".equals(opType)) {
            
            boolean isGrid = organization.isGrid(organization.getAddOrgTypeList());
            
            int orgLevel = selectOrganization
                .getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404) + 1;
            
            if (isGrid && orgLevel == 7) {
                
                msg = organizationManager.getDoValidOrganizationExtendAttrGrid(organization);
                
                if (!StrUtil.isEmpty(msg)) {
                    ZkUtil.showError(msg, "提示信息");
                    return;
                }
                
            }
            
            setAddressAsAString(organization);
        } else if ("addCostChildNode".equals(opType)
				|| "addCostRootNode".equals(opType)) {
			setAddressAsAString(organization);
		} else if ("addOssChildNode".equals(opType)
				|| "addOssRootNode".equals(opType)) {
			setAddressAsAString(organization);
		} else if ("addChildNode".equals(opType)
				|| "addRootNode".equals(opType)) {
			setAddressAsAString(organization);
		}

		if (validPartyInfo && null == party) {
			return;
		}

		/**
		 * 供应商和实业公司组织类型其实都在初始化的时候哦做了限制可以不判断组织类型
		 */
		organizationManager.addOrganization(organization);
		this.bean.getAgentOrganizationEditWindow().onClose();
		/**
		 * 抛出成功事件
		 */
		Events.postEvent("onOK", this.self, organization);
	}

	/**
	 * 功能说明:若组织联系信息的地址为空串，则让其等于组织名称 创建人:俸安琪 创建时间:2014-7-4 下午2:28:06
	 * 
	 * @param organization
	 *            void
	 */
	private void setAddressAsAString(Organization organization) {
		OrgContactInfo oci = organization.getOrganizationContactInfo();
		if (oci != null) {
			String address = oci.getAddress();
			if (StrUtil.isNullOrEmpty(address)) {// 若组织联系信息的地址为空串，则让其等于组织名称
				oci.setAddress(organization.getOrgName());
			}
		}
	}

	/**
	 * 点击取消
	 */
	public void onCancel() {
		this.bean.getAgentOrganizationEditWindow().onClose();
	}

	/**
	 * 选择添加类型
	 */
	public void onSelect$addType(ForwardEvent event) throws Exception {
		if (this.bean.getAddType() != null
				&& this.bean.getAddType().getSelectedItem() != null) {
			if ("NW".equals(this.bean.getAddType().getSelectedItem().getValue())) {
				this.bean.getAgentOrganizationEditWindow().setTitle("新增营业网点组织");
				this.bean.getPartyInfoExt().setVisible(false);
				/**
				 * 设置默认值 组织类型为社会实体渠道 N0202020000
				 */
				List<String> orgTypeCdList = new ArrayList<String>();
				orgTypeCdList.add("N0202020000");
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCdList);
			} else if ("AG".equals(this.bean.getAddType().getSelectedItem()
					.getValue())) {
				this.bean.getAgentOrganizationEditWindow().setTitle("新增代理商组织");
				this.bean.getPartyInfoExt().setVisible(true);
				/**
				 * 将组织类型置空
				 */
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(new ArrayList<String>());
			} else if ("IBENW".equals(this.bean.getAddType().getSelectedItem()
					.getValue())) {
				this.bean.getAgentOrganizationEditWindow().setTitle("新增营业网点组织");
				this.bean.getPartyInfoExt().setVisible(false);
				/**
				 * 设置默认值 组织类型为自营实体渠道 N0202010000
				 */
				List<String> orgTypeCdList = new ArrayList<String>();
				orgTypeCdList.add(OrganizationConstant.ORG_TYPE_N0202010000);
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(orgTypeCdList);
			} else if ("IBE".equals(this.bean.getAddType().getSelectedItem()
					.getValue())) {
				this.bean.getAgentOrganizationEditWindow().setTitle("新增内部经营实体");
				this.bean.getPartyInfoExt().setVisible(true);
				/**
				 * 将组织类型置空
				 */
				this.bean.getOrganizationInfoExt().getBean().getOrgTypeCd()
						.setInitialValue(new ArrayList<String>());
			}
		}
	}

	public void onUpdateOrgResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Party party = (Party) event.getOrigin().getData();
			if (party != null) {
				Organization organization = new Organization();
				organization.setOrgName(party.getPartyName());
				organization.setOrgShortName(party.getPartyName());
				this.bean.getOrganizationInfoExt().setUpdateOrganization(
						organization);
			}
		}
	}
}
