package cn.ffcs.uom.party.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ChineseSpellUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.InputFieldUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.party.action.bean.PartyContactInfoEditBean;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class PartyContactInfoEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private Party party;
	private String opType;
	private String oldMobilePhone;
	private PartyContactInfo partyContactInfo;
	private PartyContactInfoEditBean bean = new PartyContactInfoEditBean();
	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	@Resource
	private StaticParameter staticParameter;

	private List<PartyRole> partyRoleList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面定位
	 */
	@Getter
	@Setter
	private String variablePageLocation;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		bean.getContactType().addForward(SffOrPtyCtants.ON_EVENT_CONTACT_TYPE,
				comp, SffOrPtyCtants.ON_EVENT_CONTACT_TYPE_RESP);
	}

	public void onCreate$partyContactInfoEditWin() throws Exception {
		variablePageLocation = StrUtil.strnull(arg.get("variablePageLocation"));
		portletInfoProvider = (IPortletInfoProvider) arg
				.get("portletInfoProvider");
		setPageLocation(variablePageLocation);
		bindCombobox();
		bindEvent();
		bindBean();
	}

	/**
	 * 绑定下拉框. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-30 Wong
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> liTp = UomClassProvider.getValuesList("PartyContactInfo",
				"headFlag");
		ListboxUtils.rendererForEdit(bean.getHeadFlag(), liTp);
		liTp = UomClassProvider.getValuesList("Individual", "gender");
		ListboxUtils.rendererForEdit(bean.getContactGender(), liTp);
	}

	/**
	 * 页面初始化.
	 * 
	 * @throws
	 * @author
	 */
	public void bindBean() throws Exception {
		party = (Party) arg.get("party");
		opType = StrUtil.strnull(arg.get("opType"));
		if (SffOrPtyCtants.ADD.equals(opType)) {
			bean.getPartyContactInfoEditWin().setTitle("联系人新增");
			/**
			 * 新增参与人-参与人联系人-首选联系人默认：是
			 */
			ListboxUtils.selectByCodeValue(bean.getHeadFlag(),
					PartyConstant.ATTR_VALUE_HEADFLAG_YES);
			/**
			 * 新增参与人-参与人联系人-联系人类型默认：本人
			 */
			List<String> contactTypeList = new ArrayList<String>();
			contactTypeList.add(PartyConstant.ATTR_VALUE_CONTACTTYPE_SELF);
			bean.getContactType().setInitialValue(contactTypeList);

		} else if (SffOrPtyCtants.MOD.equals(opType)) {
			bean.getPartyContactInfoEditWin().setTitle("联系人修改");
			partyContactInfo = (PartyContactInfo) arg.get("partyContactInfo");
			String conNm = staticParameter.handling(
					SffOrPtyCtants.SYS_JAVA_CODE_PARTY_CON_INFO,
					SffOrPtyCtants.SPC_JAVA_CODE_CONTACTTYPE,
					partyContactInfo.getContactType());
			if (null != conNm) {
				bean.getContactType().setValue(conNm);
			}
			if (null != partyContactInfo) {
				oldMobilePhone = partyContactInfo.getMobilePhone();
			}
			PubUtil.fillBeanFromPo(partyContactInfo, bean);
		}

		// if (party != null && party.getPartyId() != null) {
		//
		// Staff staff = staffManager.queryStaffByPartyId(party.getPartyId());
		//
		// if (staff != null
		// && !StrUtil.isNullOrEmpty(staff.getWorkProp())
		// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
		// .getSelectedItem().getValue())) {
		// if (staff.getWorkProp().startsWith(
		// SffOrPtyCtants.WORKPROP_N_H_PRE)
		// || staff.getWorkProp().startsWith(
		// SffOrPtyCtants.WORKPROP_N_P_PRE)) {
		// PartyContactInfo partyContactInfo = partyManager
		// .getDefaultPartyContactInfo(party.getPartyId());
		// if (partyContactInfo != null
		// && !StrUtil.isEmpty(partyContactInfo
		// .getGrpUnEmail())) {
		// bean.getGrpUnEmail().setValue(
		// partyContactInfo.getGrpUnEmail());
		// } else {
		// bean.getGrpUnEmail().setValue(
		// ChineseSpellUtil.converterToSpell(party
		// .getPartyName())
		// + GroupMailConstant.GRP_UN_EMAIL);
		// }
		//
		// }
		// }
		// }
	}

	public void onOk() {

		if (null == party) {
			ZkUtil.showInformation("请选择相应的参与人", "提示信息");
			return;
		}
		String msg = checkPartyConInfoData();
		if (null != msg) {
			ZkUtil.showInformation(msg, "提示信息");
			return;
		}

		List<PartyContactInfo> partyContactInfoList = null;

		if (!StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
			PartyContactInfo partyContactInfo = new PartyContactInfo();
			partyContactInfo.setHeadFlag(SffOrPtyCtants.HEADFLAG);
			partyContactInfo.setGrpUnEmail(bean.getGrpUnEmail().getValue());
			partyContactInfoList = partyManager
					.queryDefaultPartyContactInfo(partyContactInfo);
		}

		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (partyContactInfoList != null && partyContactInfoList.size() > 0) {
				ZkUtil.showInformation("集团统一邮箱在主数据中已经存在,请修改！", "提示信息");
				return;
			}
			partyContactInfo = new PartyContactInfo();
			partyContactInfo.setPartyId(party.getPartyId());
			PubUtil.fillPoFromBean(bean, partyContactInfo);
			partyContactInfo.setContactType(bean.getContactType()
					.getAttrValue());
			partyManager.save(partyContactInfo);
		} else if (SffOrPtyCtants.MOD.equals(opType)) { // 修改
			if (partyContactInfoList != null && partyContactInfoList.size() > 0) {
				for (PartyContactInfo partyContactInfo : partyContactInfoList) {
					if (!partyContactInfo.getPartyId().equals(
							party.getPartyId())) {
						ZkUtil.showInformation("集团统一邮箱在主数据中已经存在,请修改！", "提示信息");
						return;
					}
				}
			}
			String contactType = partyContactInfo.getContactType();
			PubUtil.fillPoFromBean(bean, partyContactInfo);
			String cTpe = bean.getContactType().getAttrValue();
			if (!StrUtil.isNullOrEmpty(cTpe)) {
				partyContactInfo.setContactType(cTpe);
			} else {
				if (!StrUtil.isNullOrEmpty(contactType)) {
					partyContactInfo.setContactType(contactType);
				}
			}
			partyManager.update(partyContactInfo);
		}
		// 修改参与人联系信息时，同时更新参与人和参与人角色
		Long partyId = party.getPartyId();
		if (partyId != null) {
			partyRoleList = partyManager.getPartyRoleByPtId(partyId);
		}
		if (partyRoleList != null && partyRoleList.size() > 0) {
			for (PartyRole oldPartyRole : partyRoleList) {
				partyManager.updatePartyRole(oldPartyRole);
			}
		}
		partyManager.updateParty(party);
		Events.postEvent(SffOrPtyCtants.ON_OK,
				bean.getPartyContactInfoEditWin(), partyContactInfo);
		bean.getPartyContactInfoEditWin().onClose();
	}

	public void onCancel() {
		bean.getPartyContactInfoEditWin().onClose();
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-25 Wong
	 */
	private void bindEvent() throws Exception {
		PartyContactInfoEditComposer.this.bean.getPartyContactInfoEditWin()
				.addEventListener("onPartyContactInfoChange",
						new EventListener() {
							@SuppressWarnings("unchecked")
							public void onEvent(final Event event)
									throws Exception {
								if (!StrUtil.isNullOrEmpty(event.getData())) {
									PartyContactInfoEditComposer.this.arg = (HashMap) event
											.getData();
									bindBean();
								}
							}
						});
	}

	public void onEventContactTypeRespons(final ForwardEvent event) {
		bean.getContactName().setValue(party.getPartyName());
	}

	/**
	 * 新增员工时，内部邮箱=手机号码+"@anhuitelecom.com"
	 */
	public void onChange$mobilePhone() {
		if (SffOrPtyCtants.ADD.equals(opType)) {
			bean.getInnerEmail().setValue(
					bean.getMobilePhone().getValue() + "@anhuitelecom.com");
		}
	}

	// public void onSelect$headFlag() {
	//
	// if (SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag().getSelectedItem()
	// .getValue())) {
	//
	// if (party != null && party.getPartyId() != null) {
	//
	// Staff staff = staffManager.queryStaffByPartyId(party
	// .getPartyId());
	//
	// if (staff != null
	// && !StrUtil.isNullOrEmpty(staff.getWorkProp())) {
	// if (staff.getWorkProp().startsWith(
	// SffOrPtyCtants.WORKPROP_N_H_PRE)
	// || staff.getWorkProp().startsWith(
	// SffOrPtyCtants.WORKPROP_N_P_PRE)) {
	// PartyContactInfo partyContactInfo = partyManager
	// .getDefaultPartyContactInfo(party.getPartyId());
	// if (partyContactInfo != null
	// && !StrUtil.isEmpty(partyContactInfo
	// .getGrpUnEmail())) {
	// bean.getGrpUnEmail().setValue(
	// partyContactInfo.getGrpUnEmail());
	// } else {
	// bean.getGrpUnEmail().setValue(
	// ChineseSpellUtil.converterToSpell(party
	// .getPartyName())
	// + GroupMailConstant.GRP_UN_EMAIL);
	// }
	//
	// }
	// }
	// }
	// } else {
	// bean.getGrpUnEmail().setValue(null);
	// }
	// }

	/**
	 * .
	 * 
	 * @return
	 * @author Wong 2013-5-25 Wong
	 */
	private String checkPartyConInfoData() {
		if (StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
				.getValue())) {
			return "请填写首选联系人";
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (StrUtil.isNullOrEmpty(bean.getContactType().getAttrValue())) {
				return "请选择联系人类型";
			}
		}
		if (StrUtil.isNullOrEmpty(bean.getContactName().getValue())) {
			return "请填写联系人名称";
		}
		if (StrUtil.checkBlank(bean.getContactName().getValue())) {
			return "联系人名称中有空格";
		}
		if (StrUtil.isNullOrEmpty(bean.getContactGender().getSelectedItem()
				.getValue())) {
			return "请填写联系人性别";
		}
		if (!StrUtil.isNullOrEmpty(bean.getHomePhone().getValue())) {
			if (!InputFieldUtil.checkPhone(bean.getHomePhone().getValue())) {
				return "只能填写一个家庭电话或家庭电话格式有误";
			}
		}
		if (!StrUtil.isNullOrEmpty(bean.getOfficePhone().getValue())) {
			if (!InputFieldUtil.checkPhone(bean.getOfficePhone().getValue())) {
				return "只能填写一个办公电话或办公电话格式有误";
			}
		}
		if (StrUtil.isNullOrEmpty(bean.getMobilePhone().getValue())) {
			return "请填写移动电话";
		}
		if (!StrUtil.checkTelephoneNumber(bean.getMobilePhone().getValue())) {
			return "移动电话格式不对或者不存在此号码段，请重新输入11位数的手机号。";
		}
		if (!StrUtil.isNullOrEmpty(bean.getMobilePhoneSpare().getValue())) {
			if (!StrUtil.checkTelephoneNumber(bean.getMobilePhoneSpare()
					.getValue())) {
				return "备用移动电话格式不对或者不存在此号码段，请重新输入11位数的手机号。";
			}
		}
		if (!StrUtil.isNullOrEmpty(bean.getEmail().getValue())
				&& !InputFieldUtil.checkEmail(bean.getEmail().getValue())) {
			return "邮箱格式有误";
		}
		if (!StrUtil.isNullOrEmpty(bean.getPostCode().getValue())
				&& !InputFieldUtil.checkPost(bean.getPostCode().getValue())) {
			return "邮政编码有误";
		}

		/*if (party != null && party.getPartyId() != null) {

			Staff staff = staffManager.queryStaffByPartyId(party.getPartyId());

			if (staff != null
					&& !StrUtil.isNullOrEmpty(staff.getWorkProp())
					&& SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
							.getSelectedItem().getValue())) {
				if (staff.getWorkProp().startsWith(
						SffOrPtyCtants.WORKPROP_N_H_PRE)
						|| staff.getWorkProp().startsWith(
								SffOrPtyCtants.WORKPROP_N_P_PRE)) {
					if (StrUtil.isNullOrEmpty(bean.getGrpUnEmail().getValue())) {
						return "用工性质为内部_合同制或内部派遣制时，集团统一邮箱不能为空";
					} else if (!InputFieldUtil.isGrpUnEmail(bean
							.getGrpUnEmail().getValue())) {
						return "集团统一邮箱格式错误，正确格式应为姓名全拼【小写字母】加数字";
					}
				}
			}
		}*/
		
		/**
		 * 参与人-新增、修改-增加手机号排重
		 */
		PartyContactInfo pci = new PartyContactInfo();
		String newMobilePhone = bean.getMobilePhone().getValue();
		pci.setMobilePhone(newMobilePhone);
		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (partyManager.isExistsMobilePhone(pci)) {
				return "移动电话有重复";
			}
		} else if (SffOrPtyCtants.MOD.equals(opType)) {
			if (null != newMobilePhone && newMobilePhone.equals(oldMobilePhone)) {

			} else {
				if (partyManager.isExistsMobilePhone(pci)) {
					return "移动电话有重复";
				}
			}
		}
		
		if (StrUtil.isNullOrEmpty(bean.getReason().getValue())) {
			return "请填写变更原因";
		}

		return null;
	}

	public void onAutogeneration() {

		if (!StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
				.getValue())
				&& SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
						.getSelectedItem().getValue())) {

			if (party != null && party.getPartyId() != null) {

				Staff staff = staffManager.queryStaffByPartyId(party
						.getPartyId());

				if (staff != null
						&& !StrUtil.isNullOrEmpty(staff.getWorkProp())) {

					if (staff.getWorkProp().startsWith(
							SffOrPtyCtants.WORKPROP_N_H_PRE)
							|| staff.getWorkProp().startsWith(
									SffOrPtyCtants.WORKPROP_N_P_PRE)) {

						Party newParty = new Party();

						newParty.setPartyId(party.getPartyId());
						newParty.setPartyName(staff.getStaffName());
						newParty.setStaff(staff);

						bean.getGrpUnEmail().setValue(
								partyManager.generateGrpUnEmail(newParty, null,
										null));

					}
				}
			}
		} else {
			bean.getGrpUnEmail().setValue(null);
		}

	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPageLocation(String pageLocation) throws Exception {
		boolean canGrpUnEmail = false;
		boolean canAutogeneration = false;

		if (PlatformUtil.isAdmin()) {
			canGrpUnEmail = true;
			canAutogeneration = true;
		} else if ("partyPage".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("staffPage".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("marketingStaffPage".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("costStaffPage".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("politicalTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("agentTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("ibeTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("supplierTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("ossTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("edwTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("marketingTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("newMarketingTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

        } else if ("newSeventeenMarketingTab".equals(pageLocation)) {
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_GRPUNEMAIL)) {
                canGrpUnEmail = true;
            }
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_AUTOGENERATION)) {
                canAutogeneration = true;
            }
            
        } else if ("costTab".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_COST_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_COST_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("politicalTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("agentTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("ibeTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("supplierTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("ossTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("edwTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("marketingTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("newMarketingTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

        } else if ("newSeventeenMarketingTab_orgParty".equals(pageLocation)) {
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
                canGrpUnEmail = true;
            }
            
            if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_AUTOGENERATION)) {
                canAutogeneration = true;
            }
            
        } else if ("costTab_orgParty".equals(pageLocation)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		} else if ("multidimensionalTab".equals(pageLocation)) {

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CONTACT_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CONTACT_AUTOGENERATION)) {
				canAutogeneration = true;
			}

		}

		bean.getGrpUnEmail().setDisabled(!canGrpUnEmail);
		bean.getAutogenerationBtn().setDisabled(!canAutogeneration);
	}
}
