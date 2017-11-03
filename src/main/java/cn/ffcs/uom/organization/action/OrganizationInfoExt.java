package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Pair;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Div;

import cn.ffcs.uom.areacode.manager.AreaCodeManager;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.OrganizationInfoExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.party.model.Party;

public class OrganizationInfoExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8024563184709088381L;

	private final String zul = "/pages/organization/organization_info_ext.zul";

	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * 页面bean
	 */
	@Getter
	private OrganizationInfoExtBean bean = new OrganizationInfoExtBean();
	/**
	 * 组织
	 */
	private Organization organization;
	/**
	 * 是否是聚合营销2015tab
	 */
	@Getter
	@Setter
	private Boolean isMarketingTab = false;
	/**
	 * 是否是聚合营销2016tab
	 */
	@Getter
	@Setter
	private Boolean isNewMarketingTab = false;

	/**
	 * 是否是聚合营销2017tab
	 */
	@Getter
	@Setter
	private Boolean isNewSeventeenMarketingTab = false;

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
	 * 营销单元
	 */
	@Getter
	@Setter
	public boolean marketingUnit;
	/**
	 * 营销单元2015管理界面
	 */
	@Getter
	@Setter
	public boolean marketingListbox;
	/**
	 * 营销单元2016管理界面
	 */
	@Getter
	@Setter
	public boolean newMarketingListbox;
	/**
	 * 营销单元2017管理界面 newSeventeenMarketingListbox
	 */
	@Getter
	@Setter
	public boolean newSeventeenMarketingListbox;
	/**
	 * 成本单元
	 */
	@Getter
	@Setter
	public boolean costUnit;

	/**
	 * 旧的组织
	 */
	private Organization oldOrganization;

	/**
	 * 组织联系信息
	 */
	private OrgContactInfo organizationContactInfo;

	/**
	 * 组织扩展属性
	 */
	private List<OrganizationExtendAttr> organizationExtendAttrList;

	/**
	 * 操作类型
	 */
	@Setter
	private String opType;

	/**
	 * manager
	 */
	private AreaCodeManager areaCodeManager = (AreaCodeManager) ApplicationContextUtil
			.getBean("areaCodeManager");

	/**
	 * 初始化
	 */
	public OrganizationInfoExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 监听组织类型确定事件
		 */
		this.bean.getOrgTypeCd().addForward("onChooseOK", this,
				"onChooseOrgTypeCd");
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		this.bindCombobox();
		/**
		 * 组织级别不然填写
		 */
		this.bean.getOrgLeave().setDisabled(true);
		/**
		 * 组织扩展属性
		 */
		this.bean.getOrganizationExtendAttrExt().setAllDisable(true);
		/**
		 * 组织类型单位初始化
		 */
		List list = new ArrayList();
		list.add(OrganizationConstant.MANAGER_PRE);
		this.bean.getOrgTypeCd().setSpecialNodeStyle(list, null);

		// Groupbox gb = this.bean.getFinanceIndividualizationGroupbox();

		// if (null != gb) {
		// gb.setVisible(false);
		// }

		if (costUnit) {// 成本单元
			List<Node> listNodes = this.bean.getOrgTypeCd().getAllNodes();
			List<String> optionAttrValueList = new ArrayList<String>();
			for (Node node : listNodes) {
				if (node.getAttrValue().startsWith(
						OrganizationConstant.FINANCE_ORG_PRE)) {
					optionAttrValueList.add(node.getAttrValue());
				}
			}
			this.bean.getOrgTypeCd().setOptionNodes(optionAttrValueList);
			/*
			 * gb.setVisible(true); Panel panel = this.bean.getOrgInfoPanel();
			 * panel.setHeight((panel.getHeight()+100)+"px");
			 */
		}
	}

	/**
	 * 绑定下拉框
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"Organization", "orgType");
		ListboxUtils.rendererForEdit(this.bean.getOrgType(), orgTypeList);

		List<NodeVo> existTypeList = UomClassProvider.getValuesList(
				"Organization", "existType");
		ListboxUtils.rendererForEdit(this.bean.getExistType(), existTypeList);

		List<NodeVo> orgLevelList = UomClassProvider.getValuesList(
				"Organization", "orgLeave");
		ListboxUtils.rendererForEdit(bean.getOrgLeave(), orgLevelList);

		List<NodeVo> orgScaleList = UomClassProvider.getValuesList(
				"Organization", "orgScale");
		ListboxUtils.rendererForEdit(bean.getOrgScale(), orgScaleList);

		List<NodeVo> cityTownList = UomClassProvider.getValuesList(
				"Organization", "cityTown");
		ListboxUtils.rendererForEdit(bean.getCityTown(), cityTownList);

	}

	/**
	 * 获取组织信息
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if ("add".equals(opType)) {
			organization = new Organization();
		} else if ("mod".equals(opType)) {
			oldOrganization.setReason("");
			organization = oldOrganization;
		} else {
			organization = new Organization();
		}
		String oldOrgFullName = organization.getOrgFullName();
		String oldOrgName = organization.getOrgName();

		/**
		 * 填充值
		 */
		this.fillPoFromBean(bean, organization);

		String orgName = organization.getOrgName();
		String orgFullName = organization.getOrgFullName();
		if ("add".equals(opType)) {
			/**
			 * 新增的时候组织全称设置为组织名
			 */
			orgFullName = orgName;
		} else if ("mod".equals(opType)) {
			if (!StrUtil.isEmpty(oldOrgFullName)
					&& oldOrgFullName.endsWith(oldOrgName)) {
				int index = oldOrgFullName.indexOf(oldOrgName);
				String preOrgFullName = "";
				if (index != -1 && oldOrgFullName.endsWith(oldOrgName)) {
					preOrgFullName = orgFullName.substring(0, index);
					orgFullName = preOrgFullName + orgName;
				}
			} else {
				// 如果后面名称对不上就查库重新生成组织全称,生成为空即还没上挂组织树用组织名
				String temp = this.organizationRelationManager
						.getOrgFullName(organization.getOrgId());
				if (!StrUtil.isEmpty(temp)) {
					orgFullName = temp;
				} else {
					orgFullName = orgName;
				}
			}
		}
		/**
		 * 组织全称变更 去除组织名称和组织全称中的空格
		 */
		orgName = StrUtil.removeTabAndEnter(orgName);
		orgFullName = StrUtil.removeTabAndEnter(orgFullName);
		organization.setOrgFullName(orgName);
		bean.getOrgFullName().setValue(orgFullName);
		organization.setOrgFullName(orgFullName);
		if (!StrUtil.isEmpty(oldOrgName) && !StrUtil.isEmpty(orgName)
				&& !oldOrgName.equals(orgName)) {
			organization.setIsChangeOrgName(true);
		}

		OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
		organizationExtendAttr.setOrgId(organization.getOrgId());
		organizationExtendAttr
				.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

		if (organization.getOrgId() != null) {
			organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(organizationExtendAttr);
		}

		List<Organization> list = organization
				.getChildrenOrganization(OrganizationConstant.RELA_CD_INNER);

		// 如果没有挂到内部组织树上，或者有内部关系且根节点不为0，则不修改集团统一邮箱集团编码
		if (organization
				.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null
				|| (organizationExtendAttr != null && !StrUtil
						.isEmpty(organizationExtendAttr.getOrgAttrValue()))
				|| (list != null && list.size() > 0)) {
			if (organization
					.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {
				bean.getOrganizationExtendAttrExt().setExtendValue(
						OrganizationConstant.ORG_ATTR_SPEC_ID_19,
						organization.getGroupMailOrgCode(
								OrganizationConstant.RELA_CD_INNER)
								.replaceFirst(oldOrgName, orgName));
			} else if (organizationExtendAttr != null
					&& !StrUtil.isEmpty(organizationExtendAttr
							.getOrgAttrValue())) {
				bean.getOrganizationExtendAttrExt().setExtendValue(
						OrganizationConstant.ORG_ATTR_SPEC_ID_19,
						organizationExtendAttr.getOrgAttrValue().replaceFirst(
								oldOrgName, orgName));
			}
		}

		List<OrganizationExtendAttr> organizationExtendAttrList = organization
				.getOrganizationExtendAttrList();
		if (organizationExtendAttrList != null
				&& organizationExtendAttrList.size() > 0) {
			for (OrganizationExtendAttr orgExtendAttr : organizationExtendAttrList) {
				if (OrganizationConstant.ORG_ATTR_SPEC_ID_19
						.equals(orgExtendAttr.getOrgAttrSpecId())) {
					orgExtendAttr.setOrgAttrValue(bean
							.getOrganizationExtendAttrExt().getExtendValue(
									OrganizationConstant.ORG_ATTR_SPEC_ID_19));
				}
			}
		}

		return organization;
	}

	/**
	 * 设置组织信息
	 * 
	 * @param organization
	 */
	public void setOrganization(Organization organization) {
		this.oldOrganization = organization;
		/**
		 * 重新绑定，防止组织树组织变了bandbox出现上一次选择的情况
		 */
		this.bean.getPoliticalLocationTreeBandbox()
				.politicalLocationTreeBindTree();
		this.bean.getTelcomRegionTreeBandbox()
				.telcomRegionTreeBandboxBindTree();
		if ("mod".equals(opType)) {
			if (isMarketingTab
					|| (marketingListbox && organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE) > 0)) {// 营销单元2015管理

				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);

				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				this.fillBeanFromPo(organization, this.bean);

				if (!isMarketingTab) {
					this.bean
							.getOrganizationExtendAttrExt()
							.orgLevelControlDisable(
									organization,
									false,
									OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
				}

			} else if (isNewMarketingTab
					|| (newMarketingListbox && organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403) > 0)) {// 营销单元2016管理

				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);

				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				this.fillBeanFromPo(organization, this.bean);

				if (!isNewMarketingTab) {
					this.bean
							.getOrganizationExtendAttrExt()
							.orgLevelControlDisable(
									organization,
									false,
									OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
				}

			} else if (isNewSeventeenMarketingTab
					|| (newSeventeenMarketingListbox && organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404) > 0)) {// 营销单元2016管理

				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);

				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);

				this.fillBeanFromPo(organization, this.bean);

				if (!isNewSeventeenMarketingTab) {
					this.bean
							.getOrganizationExtendAttrExt()
							.orgLevelControlDisable(
									organization,
									false,
									OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
				}

			} else if (isAgentTab
					|| isIbeTab
					|| organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_EXTER) > 0) {// 代理商管理或经营实体管理

				if (isAgentTab) {
					this.bean.getOrganizationExtendAttrExt()
							.setIsAgentTab(true);
				} else if (isIbeTab) {
					this.bean.getOrganizationExtendAttrExt().setIsIbeTab(true);
				} else {
					this.bean.getOrganizationExtendAttrExt()
							.setIsAgentTab(true);
					this.bean.getOrganizationExtendAttrExt().setIsIbeTab(true);
				}

				this.bean.getOrganizationExtendAttrExt()
						.orgChannelControlVisible(organization, false);

				this.fillBeanFromPo(organization, this.bean);

				if (!isAgentTab && !isIbeTab) {
					this.bean.getOrganizationExtendAttrExt()
							.orgChannelControlDisable(organization, false);
				}

			} else {
				this.bean.getOrganizationExtendAttrExt().setIsAgentTab(false);
				this.bean.getOrganizationExtendAttrExt().setIsIbeTab(false);
				this.bean.getOrganizationExtendAttrExt()
						.orgChannelControlVisible(organization, false);
				this.fillBeanFromPo(organization, this.bean);
			}
		} else if ("show".equals(opType)) {
			if (marketingListbox
					&& organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE) > 0) {// 营销单元2015管理
				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);
				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
				// this.bean.getOrganizationExtendAttrExt()
				// .orgLevelControlDisable(organization, false);
			} else if (newMarketingListbox
					&& organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403) > 0) {// 营销单元2016管理
				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);
				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
				// this.bean.getOrganizationExtendAttrExt()
				// .orgLevelControlDisable(organization, false);
			} else if (newSeventeenMarketingListbox
					&& organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404) > 0) {// 营销单元2017管理
				// this.bean.getOrganizationExtendAttrExt()
				// .setIsMarketingTab(true);
				this.bean
						.getOrganizationExtendAttrExt()
						.orgLevelControlVisible(
								organization,
								false,
								OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
				// this.bean.getOrganizationExtendAttrExt()
				// .orgLevelControlDisable(organization, false);
			} else if (isAgentTab
					|| isIbeTab
					|| organization
							.getOrganizationLevel(OrganizationConstant.RELA_CD_EXTER) > 0) {// 代理商管理或经营实体管理

				if (isAgentTab) {
					this.bean.getOrganizationExtendAttrExt()
							.setIsAgentTab(true);
				} else if (isIbeTab) {
					this.bean.getOrganizationExtendAttrExt().setIsIbeTab(true);
				} else {
					this.bean.getOrganizationExtendAttrExt()
							.setIsAgentTab(true);
					this.bean.getOrganizationExtendAttrExt().setIsIbeTab(true);
				}

				this.bean.getOrganizationExtendAttrExt()
						.orgChannelControlVisible(organization, false);
			} else {
				this.bean.getOrganizationExtendAttrExt().setIsAgentTab(false);
				this.bean.getOrganizationExtendAttrExt().setIsIbeTab(false);
				this.bean.getOrganizationExtendAttrExt()
						.orgChannelControlVisible(organization, false);
			}

			this.fillBeanFromPo(organization, this.bean);
		} else if ("clear".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addAgentRootNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addAgentChildNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addIbeRootNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addIbeChildNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addMarketingRootNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addMarketingChildNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addNewMarketingRootNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addNewMarketingChildNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addNewSeventeenMarketingRootNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		} else if ("addNewSeventeenMarketingChildNode".equals(opType)) {
			this.fillBeanFromPo(organization, this.bean);
		}
	}

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	public String getDoValidOrganizationInfo() {
		if (this.bean.getOrgName() == null
				|| StrUtil.isEmpty(this.bean.getOrgName().getValue())) {
			return "组织名称不能为空";
		} else {
			if (StrUtil.checkBlank(this.bean.getOrgName().getValue())) {
				return "组织名称中含有空格";
			}
		}
		if (this.bean.getOrgType() == null
				|| this.bean.getOrgType().getSelectedItem() == null
				|| this.bean.getOrgType().getSelectedItem().getValue() == null
				|| StrUtil.isEmpty(this.bean.getOrgType().getSelectedItem()
						.getValue().toString())) {
			return "组织性质不能为空";
		}
		if (this.bean.getExistType() == null
				|| this.bean.getExistType().getSelectedItem() == null
				|| this.bean.getExistType().getSelectedItem().getValue() == null
				|| StrUtil.isEmpty(this.bean.getExistType().getSelectedItem()
						.getValue().toString())) {
			return "存在类型称不能为空";
		}
		if (this.bean.getOrgTypeCd() == null
				|| this.bean.getOrgTypeCd().getResultArr() == null
				|| this.bean.getOrgTypeCd().getResultArr().size() <= 0) {
			return "组织类型不能为空";
		}
		/**
		 * 20130918问题汇总城镇标识改不是必填
		 */
		// if (this.bean.getCityTown() == null
		// || this.bean.getCityTown().getSelectedItem() == null
		// || this.bean.getCityTown().getSelectedItem().getValue() == null
		// || StrUtil.isEmpty(this.bean.getCityTown().getSelectedItem()
		// .getValue().toString())) {
		// return "城镇标识必填";
		// }
		if (this.bean.getOrgPriority() == null
				|| this.bean.getOrgPriority().getValue() == null) {
			return "组织排序号不能为空";
		}
		/**
		 * 可编辑情况下必须填写
		 */
		if (this.bean.getAreaCodeId() != null
				&& !this.bean.getAreaCodeId().isDisabled()) {
			if (this.bean.getAreaCodeId().getValue() == null) {
				return "区域编码不能为空";
			} else {
				/**
				 * 区域编码不存在提醒
				 */
				List areaCodeList = areaCodeManager
						.getAreaCodeListByAreaCode(""
								+ this.bean.getAreaCodeId().getValue());
				if (areaCodeList == null || areaCodeList.size() <= 0) {
					return "该区域编码不存在,请确认";
				}
			}
		}
		if (this.bean.getTelcomRegionTreeBandbox() == null
				|| this.bean.getTelcomRegionTreeBandbox().getTelcomRegion() == null) {
			return "电信管理区域不能为空";
		}
		if (this.bean.getPoliticalLocationTreeBandbox() == null
				|| this.bean.getPoliticalLocationTreeBandbox()
						.getPoliticalLocation() == null) {
			return "行政区域不能为空";
		}

		if (this.bean.getReason() == null
				|| StrUtil.isEmpty(this.bean.getReason().getValue())) {
			return "变更原因不能为空";
		}
		/*
		 * if (this.bean.getAddress() == null ||
		 * StrUtil.isNullOrEmpty(this.bean.getAddress().getValue())) { return
		 * OrganizationConstant.ORG_CONTRACTINFO_ADDRESS_ERROR; }
		 */
		return "";
	}

	/**
	 * 填充po
	 */
	private void fillPoFromBean(OrganizationInfoExtBean bean,
			Organization organization) {
		PubUtil.fillPoFromBean(bean, organization);
		if (this.bean.getOrgParty().getParty() != null
				&& this.bean.getOrgParty().getParty().getPartyId() != null) {
			organization.setPartyId(this.bean.getOrgParty().getParty()
					.getPartyId());
		}
		if (this.bean.getOrgPriority().getValue() != null) {
			organization.setOrgPriority(this.bean.getOrgPriority().getValue());
		}
		/**
		 * 行政管理区域
		 */
		if (this.bean.getPoliticalLocationTreeBandbox().getPoliticalLocation() != null) {
			organization.setLocationId(this.bean
					.getPoliticalLocationTreeBandbox().getPoliticalLocation()
					.getLocationId());
		}
		if (this.bean.getTelcomRegionTreeBandbox().getTelcomRegion() != null) {
			organization.setTelcomRegionId(this.bean
					.getTelcomRegionTreeBandbox().getTelcomRegion()
					.getTelcomRegionId());
		}
		/**
		 * 组织类型
		 */
		List<Pair<String, String>> orgTypeCdList = this.bean.getOrgTypeCd()
				.getResultArr();
		/**
		 * 查询前先查询库
		 */
		organization.setOrgTypeList(null);
		/**
		 * 已经存在的组织类型列表
		 */
		List<OrgType> orgTypeList = organization.getOrgTypeList();
		/**
		 * 新增的组织类型
		 */
		List<OrgType> addOrgTypeList = new ArrayList<OrgType>();
		/**
		 * 删除的组织类型
		 */
		List<OrgType> delOrgTypeList = new ArrayList<OrgType>();
		/**
		 * 修改
		 */
		if ((orgTypeList != null && orgTypeList.size() > 0)
				&& (orgTypeCdList != null && orgTypeCdList.size() > 0)) {
			/**
			 * 新增情况
			 */
			for (Pair pair : orgTypeCdList) {
				boolean isExist = false;
				for (OrgType exist : orgTypeList) {
					if (exist.getOrgTypeCd().equals(pair.getLeft())) {
						isExist = true;
					}
				}
				if (!isExist) {
					OrgType orgType = new OrgType();
					orgType.setOrgTypeCd((String) pair.getLeft());
					addOrgTypeList.add(orgType);
				}
			}
			/**
			 * 删除的情况
			 */
			for (OrgType exist : orgTypeList) {
				boolean isExist = false;
				for (Pair pair : orgTypeCdList) {
					if (exist.getOrgTypeCd().equals(pair.getLeft())) {
						isExist = true;
					}
				}
				if (!isExist) {
					delOrgTypeList.add(exist);
				}
			}
			organization.setAddOrgTypeList(addOrgTypeList);
			organization.setDelOrgTypeList(delOrgTypeList);
		} else {
			/**
			 * 新增
			 */
			if (orgTypeCdList != null && orgTypeCdList.size() > 0) {
				for (Pair pair : orgTypeCdList) {
					OrgType orgType = new OrgType();
					orgType.setOrgTypeCd((String) pair.getLeft());
					addOrgTypeList.add(orgType);
				}
			}
		}
		organization.setAddOrgTypeList(addOrgTypeList);

		OrgContactInfo organizationContactInfo = organization
				.getOrganizationContactInfo();

		if (organizationContactInfo == null) {
			organizationContactInfo = new OrgContactInfo();
		}
		/**
		 * 组织联系信息
		 */
		PubUtil.fillPoFromBean(bean, organizationContactInfo);
		organization.setOrganizationContactInfo(organizationContactInfo);

		/**
		 * 扩展属性
		 */
		List<OrganizationExtendAttr> extendAttrList = organization
				.getOrganizationExtendAttrList();
		List<OrganizationExtendAttr> beanList = this.bean
				.getOrganizationExtendAttrExt().getExtendValueList();
		if (extendAttrList == null || extendAttrList.size() <= 0) {
			organization.setOrganizationExtendAttrList(beanList);
		} else {
			for (OrganizationExtendAttr organizationExtendAttr : beanList) {
				for (OrganizationExtendAttr dbOrganizationExtendAttr : extendAttrList) {
					if (organizationExtendAttr.getOrgAttrSpecId().equals(
							dbOrganizationExtendAttr.getOrgAttrSpecId())) {
						String orgAttrVlue = organizationExtendAttr
								.getOrgAttrValue();
						BeanUtils.copyProperties(organizationExtendAttr,
								dbOrganizationExtendAttr);
						organizationExtendAttr.setOrgAttrValue(orgAttrVlue);
					}
				}
			}
		}
		organization.setOrganizationExtendAttrList(beanList);
	}

	/**
	 * 填充bean
	 */
	private void fillBeanFromPo(Organization organization,
			OrganizationInfoExtBean bean) {
		if (organization != null) {
			PubUtil.fillBeanFromPo(organization, bean);
			Party party = organization.getParty();
			/**
			 * 组织参与人
			 */
			this.bean.getOrgParty().setParty(party);
			bean.getTelcomRegionTreeBandbox().setTelcomRegion(
					organization.getTelcomRegion());
			bean.getPoliticalLocationTreeBandbox().setPoliticalLocation(
					organization.getPoliticalLocation());
			ListboxUtils.selectByCodeValue(this.bean.getOrgType(),
					organization.getOrgType());
			ListboxUtils.selectByCodeValue(this.bean.getExistType(),
					organization.getExistType());
			ListboxUtils.selectByCodeValue(this.bean.getOrgLeave(),
					organization.getOrgLeave());
			ListboxUtils.selectByCodeValue(this.bean.getOrgScale(),
					organization.getOrgScale());
			/**
			 * 联系信息
			 */
			OrgContactInfo organizationContactInfo = organization
					.getOrganizationContactInfo();
			if (organizationContactInfo != null) {
				PubUtil.fillBeanFromPo(organizationContactInfo, bean);
			} else {// 修复没有组织联系信息记录时，组织树地址不改变的BUG
				PubUtil.fillBeanFromPo(new OrgContactInfo(), bean);
			}
			/**
			 * 扩展属性
			 */
			List<OrganizationExtendAttr> extendAttrList = organization
					.getOrganizationExtendAttrList();
			this.bean.getOrganizationExtendAttrExt().setExtendValueList(
					extendAttrList);
			/**
			 * 组织类型
			 */
			List<OrgType> orgTypeList = organization.getOrgTypeList();
			/**
			 * 组织类型
			 */
			List<String> orgTypeCdList = new ArrayList<String>();
			/**
			 * 是否可以编辑区域编码
			 */
			boolean canEditAreaCode = false;
			if (orgTypeList != null && orgTypeList.size() > 0) {
				for (OrgType orgType : orgTypeList) {
					orgTypeCdList.add(orgType.getOrgTypeCd());
					if (orgType.getOrgTypeCd() != null
							&& orgType.getOrgTypeCd().startsWith(
									OrganizationConstant.COMPANY_PRE)) {
						/**
						 * 单位可以编辑
						 */
						this.bean.getAreaCodeId().setDisabled(false);
						canEditAreaCode = true;
					}
				}
			}
			if (!canEditAreaCode) {
				this.bean.getAreaCodeId().setDisabled(true);
			}
			this.bean.getOrgTypeCd().setInitialValue(orgTypeCdList);
		}
	}

	/**
	 * 选择组织类型时
	 */
	public void onChooseOrgTypeCd() throws Exception {
		List<Pair<String, String>> orgTypeCdList = this.bean.getOrgTypeCd()
				.getResultArr();
		boolean isCompany = false;
		if (orgTypeCdList != null && orgTypeCdList.size() > 0) {
			for (Pair pair : orgTypeCdList) {
				String orgTypeCd = (String) pair.getLeft();
				if (orgTypeCd != null
						&& orgTypeCd
								.startsWith(OrganizationConstant.COMPANY_PRE)) {
					isCompany = true;
					break;
				}
			}
		}
		if (isCompany) {
			/**
			 * 单位可以编辑
			 */
			this.bean.getAreaCodeId().setDisabled(false);
		} else {
			this.bean.getAreaCodeId().setDisabled(true);
		}

		// financeTypeChange(orgTypeCdList);
	}

	// 财务类型
	// public void financeTypeChange(List<Pair<String, String>> orgTypeCdList) {
	// boolean isFinanceType = false;// 是否财务类型
	// Groupbox gb = this.bean.getFinanceIndividualizationGroupbox();
	// if (null != gb) {
	// List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
	// "OrgType", "orgTypeCd");
	// Map<String, String> map = new HashMap<String, String>();
	// for (NodeVo nv : orgTypeList) {
	// if (nv.getId().startsWith(OrganizationConstant.FINANCE_ORG_PRE)) {
	// map.put(nv.getId(), nv.getName());
	// }
	// }
	// if (orgTypeCdList != null && orgTypeCdList.size() > 0) {
	// for (Pair pair : orgTypeCdList) {
	// String orgTypeCd = (String) pair.getLeft();
	// if (orgTypeCd != null && map.get(orgTypeCd) != null) {
	// isFinanceType = true;
	// break;
	// }
	// }
	// }
	// gb.setVisible(isFinanceType);
	// if (isFinanceType) {
	// Panel panel = this.bean.getOrgInfoPanel();
	// panel.setHeight((panel.getHeight() + 100) + "px");
	// }
	// }
	// }

	public void setUpdateOrganization(Organization organization) {
		if (organization != null) {
			bean.getOrgName().setValue(organization.getOrgName());
			bean.getOrgShortName().setValue(organization.getOrgShortName());
		}
	}
}
