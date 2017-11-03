package cn.ffcs.uom.organization.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.organization.action.bean.OrganizationInfoEditExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class OrganizationInfoEditExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3695647753612258247L;
	/**
	 * 页面
	 */
	private final String zul = "/pages/organization/organization_info_edit_ext.zul";
	/**
	 * 页面bean
	 */
	@Getter
	@Setter
	private OrganizationInfoEditExtBean bean = new OrganizationInfoEditExtBean();

	/**
	 * 选中的组织
	 */
	private Organization oldOrganization;

	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	/**
	 * Manager.
	 */
	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是推导树
	 */
	@Getter
	private Boolean isDuceTree = false;

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
	 * 是否是聚合营销2015tab
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;
	/**
	 * 是否是成本树管理tab
	 */
	@Getter
	@Setter
	private Boolean isCostTab = false;
	/**
	 * 是否是营销树tab
	 */
	@Getter
	@Setter
	private Boolean isCustmsTab = false;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setButtonValid(false, false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	/**
	 * 构造方法
	 */
	public OrganizationInfoEditExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(OrganizationConstant.ON_SELECT_TREE_ORGANIZATION, this,
				"onSelectTreeOrganizationResponse");
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false, false);
		/**
		 * 非代理商树且非内部经营实体树，若新增或修改组织，地址为非必填
		 */
		if (!isAgentTab && !isIbeTab) {
			setOrganizationInfoExtStyle("class", "地址");
		}

	}

	/**
	 * 功能说明:设置组织信息控件样式 创建人:俸安琪 创建时间:2014-7-4 上午10:43:40 void
	 */
	public void setOrganizationInfoExtStyle(String attrName, String labelName) {
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setClass("");
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.removeAttribute(attrName);
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setValue(labelName);
	}

	/**
	 * 功能说明:修复组织信息控件样式 创建人:俸安琪 创建时间:2014-7-4 上午10:43:40 void
	 */
	public void restoreOrganizationInfoExtStyle(String attrName,
			String attrValue, String labelName) {
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setAttribute(attrName, attrValue);
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setClass(attrValue);
		this.bean.getOrganizationInfoExt().getBean().getAddressLab()
				.setValue(labelName);
	}

	/**
	 * 组织树选择组织
	 * 
	 * @throws Exception
	 */
	public void onSelectTreeOrganizationResponse(ForwardEvent event)
			throws Exception {

		oldOrganization = (Organization) event.getOrigin().getData();

		if (oldOrganization != null && oldOrganization.getOrgId() != null) {

			if (isMarketingTab || isNewMarketingTab || isNewSeventeenMarketingTab) {
				this.bean.getOrganizationInfoExt().setIsMarketingTab(
						isMarketingTab);
				this.bean.getOrganizationInfoExt().setIsNewMarketingTab(
						isNewMarketingTab);
				this.bean.getOrganizationInfoExt().setIsNewSeventeenMarketingTab(isNewSeventeenMarketingTab);
				// this.bean.getOrganizationInfoExt().getBean()
				// .getOrganizationExtendAttrExt()
				// .setIsMarketingTab(isMarketingTab);
				this.bean.getOrganizationInfoExt().setIsAgentTab(isAgentTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt()
						.setIsAgentTab(isAgentTab);
				this.bean.getOrganizationInfoExt().setIsIbeTab(isIbeTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt().setIsIbeTab(isIbeTab);
				// this.bean.getOrganizationInfoExt().getBean()
				// .getOrganizationExtendAttrExt()
				// .orgLevelControlVisible(oldOrganization, false);

			} else if (isAgentTab) {
				this.bean.getOrganizationInfoExt().setIsAgentTab(isAgentTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt()
						.setIsAgentTab(isAgentTab);
			} else if (isIbeTab) {
				this.bean.getOrganizationInfoExt().setIsIbeTab(isIbeTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt().setIsIbeTab(isIbeTab);
			} else {
				this.bean.getOrganizationInfoExt().setIsAgentTab(isAgentTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt()
						.setIsAgentTab(isAgentTab);
				this.bean.getOrganizationInfoExt().setIsIbeTab(isIbeTab);
				this.bean.getOrganizationInfoExt().getBean()
						.getOrganizationExtendAttrExt().setIsIbeTab(isIbeTab);
			}

			this.bean.getOrganizationInfoExt().setOpType("mod");
			this.bean.getOrganizationInfoExt().setOrganization(oldOrganization);
			this.setButtonValid(true, false, false);

		} else {
			/**
			 * 未选择组织树清理已存在的数据
			 */
			this.bean.getOrganizationInfoExt().setOpType("clear");
			this.bean.getOrganizationInfoExt().setOrganization(
					new Organization());
		}

		if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
			this.setOrgTreeTabName(variableOrgTreeTabName);
		}

		this.bean.getOrganizationInfoExt().onChooseOrgTypeCd();
	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.setButtonValid(false, true, true);
		
		this.bean.getOrganizationInfoExt().getBean().getReason().setValue("");
		
		if (isMarketingTab) {
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlDisable(oldOrganization, false,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
		} else if (isNewMarketingTab) {
			this.bean
					.getOrganizationInfoExt()
					.getBean()
					.getOrganizationExtendAttrExt()
					.orgLevelControlDisable(
							oldOrganization,
							false,
							OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
        } else if (isNewSeventeenMarketingTab) {
            this.bean
                .getOrganizationInfoExt()
                .getBean()
                .getOrganizationExtendAttrExt()
                .orgLevelControlDisable(oldOrganization, false,
                    OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
        } else if (isAgentTab || isIbeTab) {
			this.bean.getOrganizationInfoExt().getBean()
					.getOrganizationExtendAttrExt()
					.orgChannelControlDisable(oldOrganization, false);
		}
	}

	/**
	 * 点击保存
	 * 
	 * @throws Exception
	 */
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		String msg = this.bean.getOrganizationInfoExt()
				.getDoValidOrganizationInfo();
		/**
		 * 非代理商树且非内部经营实体树，若新增或修改组织，地址若未填让其通过
		 */
		if (!isAgentTab && !isIbeTab) {
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
		Organization organization = this.bean.getOrganizationInfoExt()
				.getOrganization();
		if (organization.getIsChangeOrgName()) {
			List<OrganizationRelation> orgRelaList = organization
					.getOrganizationRelationList();
			boolean isInnerOrg = false;
			for (OrganizationRelation orgRela : orgRelaList) {
				if (OrganizationConstant.RELA_CD_INNER.equals(orgRela
						.getRelaCd())) {
					isInnerOrg = true;
					break;
				}
			}
			if (!isInnerOrg) {
				organization.setIsChangeOrgName(false);
			} else {
				if (!isMarketingTab && !isNewMarketingTab && !isNewSeventeenMarketingTab && !isCustmsTab
						&& !isCostTab)// 不是营销树和聚合营销树
					ZkUtil.showInformation("组织名称修改，该节点下的内部组织全称会全部修改请耐心等待...",
							"提示信息");
			}
		}
		if (!isAgentTab && !isIbeTab) {
			OrgContactInfo oci = organization.getOrganizationContactInfo();
			if (oci != null) {
				String address = oci.getAddress();
				if (StrUtil.isNullOrEmpty(address)) {// 若组织联系信息的地址为空串，则让其等于组织名称
					oci.setAddress(organization.getOrgName());
				}
			}
		}

		if (isMarketingTab || isNewMarketingTab || isNewSeventeenMarketingTab) {// 生成EDA_CODE
			// 获取本级组织的层级
			int orgLevel = 0;

			if (isMarketingTab) {
				orgLevel = organization
						.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);
			} else if (isNewMarketingTab) {
				orgLevel = organization
						.getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);
            } else if (isNewSeventeenMarketingTab) {
                orgLevel = organization
                    .getOrganizationLevel(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
            }

			boolean isGrid = organization.isGrid(organization
					.getAddOrgTypeList());

			if (!isGrid) {

				List<OrgType> orgTypeList = organization.getOrgTypeList();

				orgTypeList.removeAll(organization.getDelOrgTypeList());// 删除界面已经去除的组织类型

				isGrid = organization.isGrid(orgTypeList);

			}

			if (StrUtil.isEmpty(organization.getEdaCode())) {
				organization.setEdaCode(organization.generateEdaCode(orgLevel,
						isGrid));
			} else {
				if (!isGrid) {
					if (!(organization.getEdaCode().substring(0, 1)
							.equals(orgLevel + ""))) {
						organization.setEdaCode(organization.generateEdaCode(
								orgLevel, isGrid));
					} else if (!(organization.getTelcomRegion().getAreaCode()
							.equals(oldOrganization.getTelcomRegion()
									.getAreaCode()))) {
						organization.setEdaCode(organization.getEdaCode()
								.replaceFirst(
										oldOrganization.getTelcomRegion()
												.getAreaCode(),
										organization.getTelcomRegion()
												.getAreaCode()));
					}
				}
			}

			// 调用全息网格接口进行校验
			if (isGrid && orgLevel == 7) {

				msg = organizationManager
						.getDoValidOrganizationExtendAttrGrid(organization);

				if (!StrUtil.isEmpty(msg)) {
					ZkUtil.showError(msg, "提示信息");
					return;
				}

				//判断是16还是17
				if(isNewSeventeenMarketingTab)
				{
			        //添加一个2017年的校验权限网格开关
                    boolean gridInterFaceSwitch2017 = UomClassProvider
                        .isOpenSwitch("gridInterFaceSwitch2017");// 校验开关
	                
                    //17年调用接口
                    if (gridInterFaceSwitch2017) {// 营销2015和营销2016都调用接口
                        msg = organizationManager.getGridValid(organization,
                                oldOrganization, null,
                                OrganizationConstant.OPT_TYPE_3);
                        if (!StrUtil.isEmpty(msg)) {
                            ZkUtil.showError(msg, "提示信息");
                            return;
                        }
                    }
				}
				else
				{//不是17营销树，就走下面这个
				    boolean gridInterFaceSwitch = UomClassProvider
                        .isOpenSwitch("gridInterFaceSwitch");// 校验开关
				    //16年或者以前的校验接口
				    if (gridInterFaceSwitch) {// 营销2015和营销2016都调用接口
                        msg = organizationManager.getGridValid(organization,
                                oldOrganization, null,
                                OrganizationConstant.OPT_TYPE_3);
                        if (!StrUtil.isEmpty(msg)) {
                            ZkUtil.showError(msg, "提示信息");
                            return;
                        }
                    }
				}
			}

		}

		OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
		organizationExtendAttr.setOrgId(organization.getOrgId());
		organizationExtendAttr
				.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

		organizationExtendAttr = organizationExtendAttrManager
				.queryOrganizationExtendAttr(organizationExtendAttr);

		List<Organization> list = organization
				.getChildrenOrganization(OrganizationConstant.RELA_CD_INNER);

		if ("politicalTab".equals(variableOrgTreeTabName)
				|| organization
						.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null
				|| (organizationExtendAttr != null && !StrUtil
						.isEmpty(organizationExtendAttr.getOrgAttrValue()))
				|| (list != null && list.size() > 0)) {

			boolean groupMailInterFaceSwitch = UomClassProvider
					.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

			if (groupMailInterFaceSwitch && organization.isUploadGroupMail()) {
				msg = groupMailManager.groupMailPackageInfo(
						GroupMailConstant.GROUP_MAIL_BIZ_ID_16, null,
						organization);
/*				if (!StrUtil.isEmpty(msg)) {
					ZkUtil.showError(msg, "提示信息");
					return;
				}*/

				if (organization.getIsChangeOrgName()) {

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						OrganizationRelation orgRela = new OrganizationRelation();
						orgRela.setOrgId(organization.getOrgId());
						Organization org = orgRela.getOrganization();
						organizationExtendAttr
								.setOrgAttrValue(organizationExtendAttr
										.getOrgAttrValue().replaceFirst(
												org.getOrgName(),
												organization.getOrgName()));
						organizationExtendAttr.update();
					} else if (organization
							.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {
						organizationManager
								.updateOrganizationExtendAttr(organization);
					}

					if (list != null && list.size() > 0) {
						for (Organization o : list) {
							organizationExtendAttr = new OrganizationExtendAttr();
							organizationExtendAttr.setOrgId(o.getOrgId());
							organizationExtendAttr
									.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

							organizationExtendAttr = organizationExtendAttrManager
									.queryOrganizationExtendAttr(organizationExtendAttr);
							if (organizationExtendAttr != null
									&& !StrUtil.isEmpty(organizationExtendAttr
											.getOrgAttrValue())) {
								OrganizationRelation orgRela = new OrganizationRelation();
								orgRela.setOrgId(organization.getOrgId());
								Organization org = orgRela.getOrganization();
								organizationExtendAttr
										.setOrgAttrValue(organizationExtendAttr
												.getOrgAttrValue()
												.replaceFirst(
														org.getOrgName(),
														organization
																.getOrgName()));
								organizationExtendAttr.update();
							} else if (o
									.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {
								organizationManager
										.updateOrganizationExtendAttr(o);
							}
						}
					}
				}
			}
		}

		organizationManager.updateOrganization(organization);
		// 取消集团统一邮箱编码设置
		// organizationManager.updateOrganizationExtendAttr(organization);
		this.oldOrganization = organization;
		this.setButtonValid(true, false, false);
		/**
		 * 抛出保存事件，用来判断组织类型是否改变（管理类）
		 */
		Events.postEvent(OrganizationConstant.ON_SAVE_ORGANIZATION_INFO, this,
				organization);
	}

	/**
	 * 点击恢复
	 * 
	 * @throws Exception
	 */
	public void onRecover() throws Exception {
		this.setButtonValid(true, false, false);
		this.bean.getOrganizationInfoExt().setOrganization(oldOrganization);
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canEdit
	 * @param canSave
	 * @param canRecover
	 */
	private void setButtonValid(boolean canEdit, boolean canSave,
			boolean canRecover) {
		/**
		 * 推导树默认不让编辑且不然修改
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getEditButton().setDisabled(!canEdit);
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			if (!StrUtil.isEmpty(orgTreeTabName)
					&& orgTreeTabName.equals("gridUnitTreeTab")) {
				canEdit = false;
				canSave = false;
				canRecover = false;
			} else {
				canEdit = true;
				canSave = true;
				canRecover = true;
			}
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("marketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(oldOrganization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, oldOrganization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_SAVE)) {
						canSave = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_RECOVER)) {
						canRecover = true;
					}

				}

			} else if ("newMarketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				if (!StrUtil.isNullOrEmpty(oldOrganization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, oldOrganization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_SAVE)) {
						canSave = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_INFO_RECOVER)) {
						canRecover = true;
					}

				}

            } else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {
                
                aroleOrganizationLevel = new AroleOrganizationLevel();
                aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
                aroleOrganizationLevel
                    .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                
                if (!StrUtil.isNullOrEmpty(oldOrganization)
                    && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                        aroleOrganizationLevel, oldOrganization)) {
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_INFO_EDIT)) {
                        canEdit = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_INFO_SAVE)) {
                        canSave = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_INFO_RECOVER)) {
                        canRecover = true;
                    }
                    
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_INFO_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_INFO_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_INFO_RECOVER)) {
					canRecover = true;
				}
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}
}
