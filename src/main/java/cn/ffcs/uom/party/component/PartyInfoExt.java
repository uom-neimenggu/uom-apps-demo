package cn.ffcs.uom.party.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.api.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.InputFieldUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.component.bean.PartyInfoExtBean;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

public class PartyInfoExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String zul = "/pages/party/comp/party_info_ext.zul";
	/**
	 * 页面bean
	 */
	@Getter
	private PartyInfoExtBean bean = new PartyInfoExtBean();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private IdentityCardConfigManager identityCardConfigManager = (IdentityCardConfigManager) ApplicationContextUtil
			.getBean("identityCardConfigManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	@Resource
	private StaticParameter staticParameter;

	/**
	 * 操作类型区分新增还是修改
	 */
	@Setter
	private String opType;
	/**
	 * 修改的party
	 */
	private Party party;
	/**
	 * 供外部使用party
	 */
	@Getter
	@Setter
	private Party outsideParty;

	/**
	 * 初始化
	 */
	public PartyInfoExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 下拉框绑定
	 */
	private void bindCombobox() {
		List<NodeVo> liTp = UomClassProvider
				.getValuesList("Party", "partyType");
		ListboxUtils.rendererForEdit(bean.getPartyType(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "gender");
		ListboxUtils.rendererForEdit(bean.getGender(), liTp);

		liTp = UomClassProvider.getValuesList("PartyOrganization", "orgType");
		ListboxUtils.rendererForEdit(bean.getOrgType(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "politicsStatus");
		ListboxUtils.rendererForEdit(bean.getPoliticsStatus(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "educationLevel");
		ListboxUtils.rendererForEdit(bean.getEducationLevel(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "religion");
		ListboxUtils.rendererForEdit(bean.getReligion(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "nation");
		ListboxUtils.rendererForEdit(bean.getNation(), liTp);

		liTp = UomClassProvider.getValuesList("Organization", "orgScale");
		ListboxUtils.rendererForEdit(bean.getOrgScale(), liTp);

		liTp = UomClassProvider.getValuesList("PartyCertification", "certType");
		ListboxUtils.rendererForEdit(bean.getCertType(), liTp);

		liTp = UomClassProvider.getValuesList("PartyCertification", "certSort");
		ListboxUtils.rendererForEdit(bean.getCertSort(), liTp);

		List<NodeVo> identityCardIdList = identityCardConfigManager
				.getValuesList();
		ListboxUtils.rendererForEdit(bean.getIdentityCardId(),
				identityCardIdList);

		liTp = UomClassProvider.getValuesList("PartyContactInfo", "headFlag");
		ListboxUtils.rendererForEdit(bean.getHeadFlag(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "gender");
		ListboxUtils.rendererForEdit(bean.getContactGender(), liTp);

		liTp = UomClassProvider.getValuesList("Staff", "parttime");
		ListboxUtils.rendererForEdit(bean.getPartTime(), liTp);

		liTp = UomClassProvider.getValuesList("Staff", "staffPosition");
		ListboxUtils.rendererForEdit(bean.getStaffPosition(), liTp);
	}

	/**
	 * 页面bean设置
	 */
	private void bindBean() {

	}

	/**
	 * 获取party
	 * 
	 * @return
	 */
	// public Party getParty() {
	public void getParty() {
		if (!"mod".equals(opType)
				&& partyManager.checkPartyNameExits(bean.getPartyName()
						.getValue().trim())) {
			ZkUtil.showError("参与人名字已存在", "提示信息");
		}
		Party party = null;
		PartyRole partyRole = null;
		Individual indivi = null;
		PartyOrganization ptyOrg = null;
		PartyCertification partyCertification = null;
		PartyContactInfo partyContactInfo = null;
		if ("mod".equals(opType)) {
			party = this.party;
			/**
			 * 修改的时候不会修改参与人角色和参与人证件，联系方式，因为是多个
			 */
			if (party != null && party.getPartyId() != null) {
				partyRole = partyManager.getPartyRole(party.getPartyId());
				indivi = partyManager.getIndividual(party.getPartyId());
				ptyOrg = partyManager.getPartyOrg(party.getPartyId());
				if (null == ptyOrg) {
					ptyOrg = new PartyOrganization();
				}
			}
		} else {
			/**
			 * 其他情况都当新增处理
			 */
			party = Party.newInstance();
			partyRole = PartyRole.newInstance();
			indivi = Individual.newInstance();
			ptyOrg = new PartyOrganization();
			partyCertification = new PartyCertification();
			partyContactInfo = new PartyContactInfo();
		}
		if (party != null) {
			PubUtil.fillPoFromBean(bean, party);
		}
		// 参与人角色
		if (bean.getRoleType() != null
				&& !StrUtil.isEmpty(bean.getRoleType().getAttrValue())) {
			partyRole.setRoleType(bean.getRoleType().getAttrValue());
			party.setPartyRole(partyRole);
		}
		Listitem selLst = bean.getPartyType().getSelectedItem();
		if (null != selLst) {
			String resul = (String) selLst.getValue();
			if (!StrUtil.isNullOrEmpty(resul)) { // 个人员工用户
				if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {
					if (indivi != null) {
						PubUtil.fillPoFromBean(this.bean, indivi);
						indivi.setMarriageStatus(bean.getMarriageStatus()
								.getAttrValue());
						indivi.setNationality(bean.getNationality()
								.getAttrValue());
						party.setIndividual(indivi);
					}
				} else { // 参与人组织
					if (ptyOrg != null) {
						PubUtil.fillPoFromBean(this.bean, ptyOrg);
						party.setPartyOrganization(ptyOrg);
					}
				}
			}
		}
		if (partyCertification != null) {
			// PubUtil.fillPoFromBean(bean, partyCertification);

			if ("mod".equals(opType)) {

				partyCertification = new PartyCertification();
				PubUtil.fillPoFromBean(bean, partyCertification);

				IdentityCardConfig identityCardConfig = null;
				Long identityCardId = null;

				PartyCertification queryPartyCertification = new PartyCertification();
				queryPartyCertification.setCertType(partyCertification
						.getCertType());
				queryPartyCertification.setCertNumber(partyCertification
						.getCertNumber());

				if (PartyConstant.ATTR_VALUE_IDNO.equals(partyCertification
						.getCertType())) {
					// 如果身份证号是临时18位号，则换前三个字母，后15们不变
					if (IdcardValidator.is18TempIdcard(partyCertification
							.getCertNumber())) {

						identityCardId = Long.valueOf((String) bean
								.getIdentityCardId().getSelectedItem()
								.getValue());

						identityCardConfig = identityCardConfigManager
								.getIdentityCardConfig(identityCardId);

						queryPartyCertification
								.setCertNumber(identityCardConfig
										.getIdentityCardPrefix()
										+ partyCertification.getCertNumber()
												.substring(3));

					}
				}

				partyCertification.setCertNumber(queryPartyCertification
						.getCertNumber());
				party.setPartyCertification(partyCertification);

			} else {
				partyCertification = new PartyCertification();
				PubUtil.fillPoFromBean(bean, partyCertification);
			}
			
			party.setPartyCertification(partyCertification);
		}
		if (partyContactInfo != null) {
			PubUtil.fillPoFromBean(bean, partyContactInfo);
			partyContactInfo.setContactType(bean.getContactType()
					.getAttrValue());
			party.setPartyContactInfo(partyContactInfo);
		}
		outsideParty = party;
	}

	/**
	 * 设置参与人
	 * 
	 * @param party
	 * @param opType
	 *            操作类型
	 */
	public void setParty(Party party, String opType) {
		this.party = party;
		this.opType = opType;
		/**
		 * 不填默认新增
		 */
		if (StrUtil.isEmpty(opType)) {
			this.opType = "add";
		} else if ("addAgentRootNode".equals(opType)
				|| "addIbeRootNode".equals(opType)) {
			/**
			 * 设置参与人类型为组织时，个人信息隐藏，组织或法人信息展示
			 */
			bean.getIndividualGroupbox().setVisible(false);
			bean.getPartyOrgGroupbox().setVisible(true);
			/**
			 * 设置参与人证件信息默认值 证件类型为身份证 1 证件种类为默认值 1
			 */
			ListboxUtils.selectByCodeValue(this.bean.getCertType(), "1");
			ListboxUtils.selectByCodeValue(this.bean.getCertSort(), "1");

			/**
			 * 设置参与人联系信息默认值 首选联系人为是 1 联系人类型为企业法人代表 1
			 */
			ListboxUtils.selectByCodeValue(this.bean.getHeadFlag(), "1");
			List<String> orgTypeCdList = new ArrayList<String>();
			orgTypeCdList.add("0201");
			this.bean.getContactType().setInitialValue(orgTypeCdList);

		}

		/**
		 * 证件和联系人支持多个所以没法维护
		 */
		if (!("addAgentRootNode".equals(opType))
				&& !("addIbeRootNode".equals(opType))) {
			bean.getPartyCertGroupbox().setVisible(false);
			bean.getPartyContactGroupbox().setVisible(false);
		}
		if (null != party) {
			PubUtil.fillBeanFromPo(party, bean);
			if ("addAgentRootNode".equals(opType)
					|| "addIbeRootNode".equals(opType)) {
				/**
				 * 设置参与人证件信息默认值 证件类型为身份证 1
				 */
				ListboxUtils.selectByCodeValue(this.bean.getCertType(), "1");
			}
			String mg = null;
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(party.getPartyType())) {
				Individual indivi = partyManager.getIndividual(party
						.getPartyId());
				if (null != indivi) {
					List marriageStatusCodeList = new ArrayList();
					marriageStatusCodeList.add(indivi.getMarriageStatus());
					bean.getMarriageStatus().setInitialValue(
							marriageStatusCodeList);

					List nationalityCodeList = new ArrayList();
					nationalityCodeList.add(indivi.getNationality());
					bean.getNationality().setInitialValue(nationalityCodeList);

					PubUtil.fillBeanFromPo(indivi, bean);
				}
				bean.getPartyOrgGroupbox().setVisible(false);
				bean.getIndividualGroupbox().setVisible(true);
			} else {
				if (party.getPartyId() != null) {
					PartyOrganization partyOrg = partyManager.getPartyOrg(party
							.getPartyId());
					if (null != partyOrg) {
						PubUtil.fillBeanFromPo(partyOrg, bean);
					}
					bean.getPartyOrgGroupbox().setVisible(true);
					bean.getIndividualGroupbox().setVisible(false);
				} else if ("addAgentRootNode".equals(opType)
						|| "addIbeRootNode".equals(opType)) {
					/**
					 * 设置组织或法人信息-组织类型默认值 组织类型为外部组织 1100
					 */
					ListboxUtils.selectByCodeValue(this.bean.getOrgType(),
							"1100");
				}
			}
			if (party.getPartyId() != null) {
				List<PartyRole> liPr = partyManager.getPartyRoleByPtId(party
						.getPartyId());
				if (null != liPr && liPr.size() > 0) {
					PartyRole pr = liPr.get(0);
					if (pr != null) {
						List<String> roleTypeList = new ArrayList<String>();
						roleTypeList.add(pr.getRoleType());
						bean.getRoleType().setInitialValue(roleTypeList);
					}
				}
			}
		}
	}

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	public String getDoValidPartyInfo() {
		if (StrUtil.isNullOrEmpty(bean.getPartyName().getValue())) {
			return "请填写参与人名称";
		}
		if (StrUtil.checkBlank(bean.getPartyName().getValue())) {
			return "参与人名称中含有空格";
		}
		Listitem selLst = bean.getPartyType().getSelectedItem();
		if (selLst == null || StrUtil.isNullOrEmpty((String) selLst.getValue())) {
			return "请选择参与人类型";
		}
		String resul = (String) selLst.getValue();
		if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {// 个人员工用户
			if (StrUtil.isNullOrEmpty(bean.getBirthday().getValue())) {
				return "请选择出生日期";
			}
			if (bean.getGender().getSelectedItem() == null
					|| StrUtil.isNullOrEmpty(bean.getGender().getSelectedItem()
							.getValue())) {
				return "请选择性别";
			}
		} else {
			if (bean.getOrgType().getSelectedItem() == null
					|| StrUtil.isNullOrEmpty(bean.getOrgType()
							.getSelectedItem().getValue())) {
				return "请选择组织类型";
			}
		}

		if (StrUtil.isNullOrEmpty(bean.getRoleType().getAttrValue())) {
			return "请选择参与人角色类型";
		}
		if (bean.getCertType().getSelectedItem() == null
				|| StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem()
						.getValue())) {
			return "请选择证件类型";
		}

		if (StrUtil.isNullOrEmpty(bean.getIdentityCardId().getSelectedItem())
				|| StrUtil.isNullOrEmpty(bean.getIdentityCardId()
						.getSelectedItem().getValue())) {
			return "请选择类型";
		} else if (StrUtil.isNullOrEmpty(bean.getCertNumber().getValue())) {
			return "请填写证件号码";
		}

		if (StrUtil.checkLowerCase(bean.getCertNumber().getValue())) {
			return "请将证件号码中小写字母改成大写字母";
		}
		if (PartyConstant.ATTR_VALUE_IDNO.equals(bean.getCertType()
				.getSelectedItem().getValue())) {
			if (!IdcardValidator.isValidatedAllIdcard(bean.getCertNumber()
					.getValue().trim())) {
				return "身份证格式不正确，请填写真实身份证信息!";
			}
		}
		if (bean.getCertSort().getSelectedItem() == null
				|| StrUtil.isNullOrEmpty(bean.getCertSort().getSelectedItem()
						.getValue())) {
			return "请选择证件种类";
		}
		String certType = bean.getCertType().getSelectedItem().getValue()
				.toString();
		// String certNum = bean.getCertNumber().getValue();

		if (PartyConstant.ATTR_VALUE_IDNO.equals(certType)) {
			return null;
		}

		/*
		 * if (!StrUtil.isNullOrEmpty(certType) &&
		 * !StrUtil.isNullOrEmpty(certNum)) { // 证件已使用 if
		 * (!partyManager.checkIsExistCertificate(certType, certNum)) { return
		 * "该参与人的证件信息已被使用"; } }
		 */
		if (bean.getHeadFlag().getSelectedItem() == null
				|| StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
						.getValue())) {
			return "请填写首选联系人";
		}
		if (StrUtil.isNullOrEmpty(bean.getContactType().getAttrValue())) {
			return "请选择联系人类型";
		}
		if (StrUtil.isNullOrEmpty(bean.getContactName().getValue())) {
			return "请填写联系人名称";
		}
		if (StrUtil.checkBlank(bean.getContactName().getValue())) {
			return "联系人名称中含有空格";
		}
		if (bean.getContactGender().getSelectedItem() == null
				|| StrUtil.isNullOrEmpty(bean.getContactGender()
						.getSelectedItem().getValue())) {
			return "请填写联系人性别";
		}
		if (StrUtil.isNullOrEmpty(bean.getMobilePhone().getValue())) {
			return "请填写移动电话";
		}
		if (!InputFieldUtil.checkPhone(bean.getMobilePhone().getValue())) {
			return "移动电话格式有误";
		}
		/**
		 * 参与人-新增、修改-增加手机号排重
		 */
		PartyContactInfo partyContactInfo = new PartyContactInfo();
		partyContactInfo.setMobilePhone(bean.getMobilePhone().getValue());
		if (partyManager.isExistsMobilePhone(partyContactInfo)) {
			return "移动电话重复";
		}
		if (!StrUtil.isNullOrEmpty(bean.getEmail().getValue())
				&& !InputFieldUtil.checkEmail(bean.getEmail().getValue())) {
			return "邮箱格式有误";
		}
		if (!StrUtil.isNullOrEmpty(bean.getPostCode().getValue())
				&& !InputFieldUtil.checkPost(bean.getPostCode().getValue())) {
			return "邮政编码有误";
		}
		return null;
	}

	public void onCertTypeSelect() {
		bean.getCertNumber().setDisabled(false);
		bean.getIdentityCardId().setDisabled(false);
		bean.getIdentityCardId().selectItem(null);
	}

	public void onIdentityCardIdSelect() {
		if (bean.getIdentityCardId() != null
				&& bean.getIdentityCardId().getSelectedItem() != null
				&& bean.getIdentityCardId().getSelectedItem().getValue() != null) {
			bean.getCertNumber().setDisabled(false);
		}
	}

	/**
	 * 选择参与人类型
	 */
	public void onSelect$partyType() {
		String resul = (String) bean.getPartyType().getSelectedItem()
				.getValue();
		Party party = new Party();
		if (!StrUtil.isNullOrEmpty(resul)) {
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {
				bean.getIndividualGroupbox().setVisible(true);
				bean.getPartyOrgGroupbox().setVisible(false);
				PartyOrganization partyOrg = new PartyOrganization();
				PubUtil.fillBeanFromPo(partyOrg, bean);
				PartyCertification partyCertification = new PartyCertification();
				PubUtil.fillBeanFromPo(partyCertification, bean);
				PartyContactInfo partyContactInfo = new PartyContactInfo();
				PubUtil.fillBeanFromPo(partyContactInfo, bean);
				this.bean.getContactType().setInitialValue(
						new ArrayList<String>());
				if ("addAgentRootNode".equals(opType)
						|| "addIbeRootNode".equals(opType)) {
					party.setPartyName("");
					Events.postEvent("onUpdateOrgRequest", this, party);
				}
			} else {
				bean.getIndividualGroupbox().setVisible(false);
				bean.getPartyOrgGroupbox().setVisible(true);
				/**
				 * 设置组织或法人信息-组织类型默认值 组织类型为外部组织 1100
				 */
				PartyOrganization partyOrg = new PartyOrganization();
				/**
				 * 设置参与人证件信息默认值 证件类型为身份证 1 证件种类为默认值 1
				 */
				PartyCertification partyCertification = new PartyCertification();
				/**
				 * 设置参与人联系信息默认值 首选联系人为是 1 联系人类型为企业法人代表 1
				 */
				PartyContactInfo partyContactInfo = new PartyContactInfo();
				if (("addAgentRootNode".equals(opType) || "addIbeRootNode"
						.equals(opType)) && resul.equals("2")) {
					partyOrg.setOrgType("1100");
					partyCertification.setCertType("1");
					partyCertification.setCertSort("1");
					partyContactInfo.setHeadFlag("1");
				}
				PubUtil.fillBeanFromPo(partyOrg, bean);
				PubUtil.fillBeanFromPo(partyCertification, bean);
				PubUtil.fillBeanFromPo(partyContactInfo, bean);
				if (("addAgentRootNode".equals(opType) || "addIbeRootNode"
						.equals(opType)) && resul.equals("2")) {
					List<String> orgTypeCdList = new ArrayList<String>();
					orgTypeCdList.add("0201");
					this.bean.getContactType().setInitialValue(orgTypeCdList);
					party.setPartyName(bean.getPartyName().getValue());
					Events.postEvent("onUpdateOrgRequest", this, party);
				} else if (("addAgentRootNode".equals(opType) || "addIbeRootNode"
						.equals(opType)) && resul.equals("3")) {
					this.bean.getContactType().setInitialValue(
							new ArrayList<String>());
					party.setPartyName("");
					Events.postEvent("onUpdateOrgRequest", this, party);
				}

			}
		} else {
			bean.getIndividualGroupbox().setVisible(false);
			bean.getPartyOrgGroupbox().setVisible(false);
		}
	}

	/**
	 * 是否添加员工信息
	 * 
	 */
	public void onSelect$addStaffInfo() {
		String resul = (String) this.bean.getAddStaffInfo().getSelectedItem()
				.getValue();
		if (!StrUtil.isNullOrEmpty(resul)) {
			if (SffOrPtyCtants.CONST_ADD_STAFFINFO.equals(resul)) {
				bean.getStaffAttrGroupbox().setVisible(true);
			} else {
				bean.getStaffAttrGroupbox().setVisible(false);
			}
		} else {
			bean.getStaffAttrGroupbox().setVisible(false);
		}
	}

	/**
	 * 代理商添加根节点时，组织名称和组织简称的值取参与人的名称
	 * 
	 */
	public void onChange$partyName() {
		String resul = (String) bean.getPartyType().getSelectedItem()
				.getValue();
		Party party = new Party();
		if ("addAgentRootNode".equals(opType)
				|| "addIbeRootNode".equals(opType)) {
			if (!StrUtil.isNullOrEmpty(resul)) {
				if (resul.equals("2")) {
					String partyName = bean.getPartyName().getValue();
					if (!StrUtil.isEmpty(partyName)) {
						party.setPartyName(partyName);
						Events.postEvent("onUpdateOrgRequest", this, party);
					}
				}
			}
		}
	}
}
