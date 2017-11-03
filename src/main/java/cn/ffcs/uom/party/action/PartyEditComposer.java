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
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.api.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.InputFieldUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.Md5Util;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.action.bean.PartyEditBean;
import cn.ffcs.uom.party.constants.PartyCertificationConstant;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

/**
 * 参与人信息编辑 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-30
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked" })
public class PartyEditComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	PartyEditBean bean = new PartyEditBean();

	@Resource
	private StaticParameter staticParameter;

	@Resource
	private Md5Util md5Util;

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private IdentityCardConfigManager identityCardConfigManager = (IdentityCardConfigManager) ApplicationContextUtil
			.getBean("identityCardConfigManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private GroupManager groupManager = (GroupManager) ApplicationContextUtil
			.getBean("groupManager");

	private Party party;

	private PartyContactInfo pCInfo;

	private PartyCertification pCertif;

	private PartyCertification partyCertification;
	/**
	 * 修改时旧的party;
	 */
	private Party oldParty;
	/**
	 * 操作类型.
	 */
	private String opType = null;// 操作类型
	/**
	 * 是否是组织树维护界面
	 */
	private Boolean isTreeMainPage = false;
	/**
	 * 是否是推导树
	 */
	@Getter
	@Setter
	private Boolean isDuceTree = false;

	private Staff staff;

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
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		/**
		 * 树页面选择组织节点
		 */
		this.self.addForward(OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
				this.self,
				OrganizationConstant.ON_SELECT_TREE_ORGANIZATION_RESPONSE);
		/**
		 * 树页面初始化
		 */
		this.self.addForward(
				OrganizationConstant.ORGANIZATION_TREE_MAIN_INIT_REQUEST,
				this.self,
				OrganizationConstant.ORGANIZATION_TREE_MAIN_INIT_RESPONSE);
		/**
		 * 是否是推到树响应
		 */
		this.self.addForward("onSetTreeTypeToRightTab", this.self,
				"onSetTreeTypeResponse");

		/**
		 * 对用工性质进行监听
		 */
		bean.getWorkProp().addForward("onChooseOK", this.self,
				"onChooseOKResponse");

	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$partyEditWindow() throws Exception {
		variablePageLocation = StrUtil.strnull(arg.get("variablePageLocation"));
		portletInfoProvider = (IPortletInfoProvider) arg
				.get("portletInfoProvider");
		setPageLocation(variablePageLocation);
		bindCombobox();
		bindBean();
		bindEvent();
	}

	/**
	 * 绑定下拉框. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-30 Wong
	 */
	private void bindCombobox() throws Exception {
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
	 * 页面初始化.
	 * 
	 * @throws
	 * @author
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if (SffOrPtyCtants.ADD.equals(opType)) {
			bean.getPartyEditWindow().setTitle("参与人新增");
			/**
			 * 新增参与人-参与人联系人-参与人类型默认：个人
			 */
			ListboxUtils.selectByCodeValue(bean.getPartyType(),
					PartyConstant.ATTR_VALUE_PARTYTYPE_PERSONAL);
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
		} else if ("bindParty".equals(opType)) {
			bean.getPartyEditWindow().setTitle("参与人新增");
			ListboxUtils.selectByCodeValue(bean.getPartyType(),
					PartyConstant.ATTR_VALUE_PARTYTYPE_ORGANIZATION);
			bean.getPartyType().setDisabled(true);
			bean.getOrgs().setVisible(true);
			bean.getPel().setVisible(false);
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
		} else if (SffOrPtyCtants.MOD.equals(opType)
				|| SffOrPtyCtants.MOD_INDIVIDUAL.equals(opType)
				|| SffOrPtyCtants.VIEW.equals(opType) || isTreeMainPage) {
			bean.getPartyEditWindow().setTitle("参与人修改");
			if (SffOrPtyCtants.MOD_INDIVIDUAL.equals(opType)) {
				bean.getPartyEditWindow().setTitle("个人信息修改");
				// bean.getPartyInfo().setVisible(false);
			}
			bean.getPanPartyCer().setVisible(false);
			bean.getPanPartyConInfo().setVisible(false);
			party = (Party) arg.get("party");
			if (null != party && null != party.getPartyRoleId()
					&& !isTreeMainPage) {
				staff = staffManager.getStaffByPartyRoleId(Long.parseLong(party
						.getPartyRoleId()));
				if (null != staff) {
					ListboxUtils.selectByCodeValue(bean.getAddStaffInfo(),
							SffOrPtyCtants.CONST_ADD_STAFFINFO);
					bean.getStaffInfo().setVisible(true);
					if (SffOrPtyCtants.MOD_INDIVIDUAL.equals(opType)) {
						bean.getPartyName().setDisabled(true);
						bean.getPartyType().setDisabled(true);
						bean.getRoleType().setDisabled(true);
						bean.getAddStaffInfo().setDisabled(true);
						bean.getStaffInfo().setVisible(false);
					}
					PubUtil.fillBeanFromPo(staff, bean);
					PoliticalLocation pl = PoliticalLocation
							.getPoliticalLocation(staff.getLocationId());
					if (null != pl) {
						staff.setLocationName(pl.getLocationName());
						bean.getLocationId().setPoliticalLocation(pl);
						bean.getLocationId().getPoliticalLocation()
								.setLocationName(pl.getLocationName());
					}
					List<String> workPropList = new ArrayList<String>();
					workPropList.add(staff.getWorkProp());
					bean.getWorkProp().setInitialValue(workPropList);
					ListboxUtils.selectByCodeValue(bean.getStaffPosition(),
							staff.getStaffPosition());
					StaffAccount sa = staffManager.getStaffAccount(null,
							staff.getStaffId());
					if (null != sa) {
						bean.getStaffAcct().setValue(sa.getStaffAccount());
						bean.getStaffPassword().setValue(sa.getStaffPassword());
					}
					List<StaffExtendAttr> staffExtendAttrList = staffManager
							.getStaffExtendAttr(staff.getStaffId());
					if (null != staffExtendAttrList
							&& staffExtendAttrList.size() > 0) {
						bean.getStaffExtendAttrExt().setExtendValue(
								staffExtendAttrList);
					}
					bean.getAddStaffInfoLab().setValue("是否修改员工");
				} else {
					bean.getAddStaffInfo().setVisible(false);
					bean.getAddStaffInfoLab().setVisible(false);
					bean.getStaffInfo().setVisible(false);
				}
			}
			/**
			 * 组织树维护界面
			 */
			if (isTreeMainPage) {
				bean.getPartyEditWindow().setTitle("");
				bean.getAddStaffInfoLab().setVisible(false);
				bean.getAddStaffInfo().setVisible(false);
				bean.getStaffInfo().setVisible(false);
				ListboxUtils.selectByCodeValue(bean.getAddStaffInfo(),
						SffOrPtyCtants.CONST_NO_ADD_STAFFINFO);
			}
			PubUtil.fillBeanFromPo(party, bean);
			String mg = null;
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(party.getPartyType())) {
				Individual indivi = partyManager.getIndividual(party
						.getPartyId());
				if (null != indivi) {
					mg = staticParameter.handling(new String[] { "Individual",
							"maritalStatus", indivi.getMarriageStatus() });
					if (null != mg) {
						bean.getMarriageStatus().setValue(mg);
					}
					mg = staticParameter.handling(new String[] { "Individual",
							"nationality", indivi.getNationality() });
					if (null != mg) {
						bean.getNationality().setValue(mg);
					}
					PubUtil.fillBeanFromPo(indivi, bean);
				}
				bean.getPel().setVisible(true);
				bean.getOrgs().setVisible(false);
			} else {
				/**
				 * partyId不能为null
				 */
				if (party.getPartyId() != null) {
					PartyOrganization partyOrg = partyManager.getPartyOrg(party
							.getPartyId());
					if (null != partyOrg) {
						PubUtil.fillBeanFromPo(partyOrg, bean);
					}
					bean.getOrgs().setVisible(true);
					bean.getPel().setVisible(false);
				}
			}
			if (party.getPartyId() != null) {
				List<PartyRole> liPr = partyManager.getPartyRoleByPtId(party
						.getPartyId());
				if (null != liPr && liPr.size() > 0) {
					List<String> roleTypeList = new ArrayList<String>();
					roleTypeList.add(liPr.get(0).getRoleType());
					bean.getRoleType().setInitialValue(roleTypeList);
				}
			}
		}
	}

	/**
	 * 点击OK确定事件 .
	 * 
	 * @author Wong 2013-5-30 Wong
	 * @throws Exception
	 */
	public void onOk() throws Exception {
		try {
			String msg = checkPartyData();
			if (null != msg) {
				ZkUtil.showError(msg, "提示信息");
				return;
			}
			msg = null;
			String passWord = bean.getStaffPassword().getValue();
			/**
			 * 获取MD5加密后的密码，统一认证接入，系统上线前把生产数据库员工密码字段置为空，程序新增员工的时候，密码设置为空字符串。
			 */
			// passWord = md5Util.getMD5(passWord);
			passWord = "";

			List<PartyContactInfo> partyContactInfoList = null;

			if (!StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
				PartyContactInfo partyContactInfo = new PartyContactInfo();
				partyContactInfo.setHeadFlag(SffOrPtyCtants.HEADFLAG);
				partyContactInfo.setGrpUnEmail(bean.getGrpUnEmail().getValue());
				partyContactInfoList = partyManager
						.queryDefaultPartyContactInfo(partyContactInfo);
			}

			if (SffOrPtyCtants.ADD.equals(opType) || "bindParty".equals(opType)) {
				if (partyContactInfoList != null
						&& partyContactInfoList.size() > 0) {
					ZkUtil.showInformation("集团统一邮箱在主数据中已经存在,请修改！", "提示信息");
					return;
				}
				String roleType = bean.getRoleType().getAttrValue();
				/*
				 * if (partyManager.checkPartyNameExits(bean.getPartyName()
				 * .getValue().trim())) { ZkUtil.showError("参与人名字被使用", "提示信息");
				 * }
				 */
				party = Party.newInstance();
				PubUtil.fillPoFromBean(bean, party);
				PartyRole partyRole = PartyRole.newInstance(); // 参与人角色
				partyRole.setRoleType(roleType);
				party.setPartyRole(partyRole);
				Listitem selLst = bean.getPartyType().getSelectedItem();
				if (null != selLst) {
					String resul = (String) selLst.getValue();
					if (!StrUtil.isNullOrEmpty(resul)) { // 个人员工用户
						if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) { // 参与人个人信息
							Individual indivi = Individual.newInstance();
							PubUtil.fillPoFromBean(this.bean, indivi);
							indivi.setMarriageStatus(bean.getMarriageStatus()
									.getAttrValue());
							indivi.setNationality(bean.getNationality()
									.getAttrValue());
							party.setIndividual(indivi);
						} else { // 参与人组织
							PartyOrganization ptyOrg = new PartyOrganization();
							PubUtil.fillPoFromBean(this.bean, ptyOrg);
							party.setPartyOrganization(ptyOrg);
						}
					}
				}
				Listitem isAddStaffInfo = bean.getAddStaffInfo()
						.getSelectedItem();
				if (null != isAddStaffInfo) {
					String resul = (String) isAddStaffInfo.getValue();
					if (!StrUtil.isNullOrEmpty(resul)) {
						if (SffOrPtyCtants.CONST_ADD_STAFFINFO.equals(resul)) {
							staff = new Staff();
							PubUtil.fillPoFromBean(this.bean, staff);
							msg = checkStaffData();
							if (null != msg) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}
							msg = null;
							String workProp = bean.getWorkProp().getAttrValue();
							staff.setWorkProp(workProp);
							staff.setLocationId(bean.getLocationId()
									.getPoliticalLocation().getLocationId());
							staff.setUuid(StrUtil.getUUID());
							List<StaffExtendAttr> listValu = this
									.getStaffExtendAttrList(staff);
							if (null != listValu && listValu.size() > 0) {
								staff.setStaffExtendAttr(listValu);
							}
							staff.setStaffCode(staffManager
									.gennerateStaffCode());
							staff.setStaffNbr(staffManager
									.gennerateStaffNumber(workProp));
							StaffAccount sa = new StaffAccount();
							sa.setStaffPassword(passWord);
							sa.setStaffAccount(staffManager
									.gennerateStaffAccount(staff));
							staff.setObjStaffAccount(sa);
							party.setStaff(staff);
						}
					}
				}

				this.getPartyCerficaByBean();

				if (null == partyCertification) {
					return;
				}

				boolean certIsNotExist = partyManager
						.checkIsExistCertificate(partyCertification
								.getCertNumber());
				if (!certIsNotExist) { // 证件已达到使用上限
					String partyCertificationUsedMax = UomClassProvider
							.getSystemConfig("partyCertificationUsedMax");
					String fieldErrorCertAlreadyUseStr = PartyCertificationConstant.FIELD_ERROR_CERT_ALREADY_USE_STR
							.replace("%", partyCertificationUsedMax);
					String staffAccListStr = staffManager
							.queryStaffAccountListStrByCertNum(partyCertification
									.getCertNumber());
					StringBuilder showErrorInfoSb = new StringBuilder();
					showErrorInfoSb.append(fieldErrorCertAlreadyUseStr);
					showErrorInfoSb.append(" 存在以下工号使用该身份证号：");
					showErrorInfoSb.append(staffAccListStr);
					ZkUtil.showError(showErrorInfoSb.toString(), "提示信息");
					return;
				}

				partyCertification
						.setIsRealName(PartyConstant.PARTY_CERTIFICATION_IS_REAL_NAME_Y);
				party.setPartyCertification(partyCertification);

				party.setPartyContactInfo(getContactInfoByBean());

				if (null != isAddStaffInfo) {

					String resul = (String) isAddStaffInfo.getValue();

					if (!StrUtil.isNullOrEmpty(resul)) {

						if (SffOrPtyCtants.CONST_ADD_STAFFINFO.equals(resul)) {

							msg = partyManager.checkStaffAccountNew(
									partyCertification, party.getPartyName());

							if (!StrUtil.isNullOrEmpty(msg)) {

								if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL
										.equals(msg)) {

									msg = SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL_STR;
								}

								if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO
										.equals(msg)) {
									msg = SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO_STR;
								}

								if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE
										.equals(msg)) {
									msg = SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE_STR;
								}

								if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM
										.equals(msg)) {
									msg = SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM_STR;
								}
							}

							if (null != msg) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}

							Group group = new Group();
							group.setUserName(party.getPartyName());
							group.setCtIdentityNumber(partyCertification
									.getCertNumber());

							List<Group> groupList = null;

							groupList = groupManager.queryGroupList(group);

							if ((groupList == null || groupList.size() == 0)
									&& partyCertification.getCertNumber()
											.length() == 15) {

								group.setCtIdentityNumber(IdcardValidator
										.convertIdcarBy15bit(partyCertification
												.getCertNumber()));

								groupList = groupManager.queryGroupList(group);

							}

							if (groupList != null && groupList.size() > 0) {

								group = groupList.get(0);
								party.setGroup(group);

								String staffNbr = group.getCtHrUserCode();

								if (!StrUtil.isNullOrEmpty(staffNbr)) {

									staffNbr = staffNbr.trim();
									staff.setStaffNbr(staffNbr);
									staff.getObjStaffAccount().setStaffAccount(
											staffNbr);

									if (staffNbr.startsWith("34")) {

										String staffAccStr = staffNbr
												.substring(2, staffNbr.length());

										staff.getObjStaffAccount()
												.setStaffAccount(staffAccStr);

									} else if (staffNbr.startsWith("W34")
											|| staffNbr.startsWith("w34")) {

										String staffAccStr = "W9"
												+ staffNbr.substring(3,
														staffNbr.length());

										staff.getObjStaffAccount()
												.setStaffAccount(staffAccStr);

									}

								}
							}
						}
					}
				}

				partyManager.addParty(party);

			} else if (SffOrPtyCtants.MOD.equals(opType)
					|| SffOrPtyCtants.MOD_INDIVIDUAL.equals(opType)
					|| isTreeMainPage) { // 修改
				if (partyContactInfoList != null
						&& partyContactInfoList.size() > 0) {
					for (PartyContactInfo partyContactInfo : partyContactInfoList) {
						if (!partyContactInfo.getPartyId().equals(
								party.getPartyId())) {
							ZkUtil.showInformation("集团统一邮箱在主数据中已经存在,请修改！",
									"提示信息");
							return;
						}
					}
				}
				Long partyId = party.getPartyId();
				String partyName = party.getPartyName();
				PubUtil.fillPoFromBean(bean, party);
				party.setPartyId(partyId);
				List<PartyRole> liPr = partyManager.getPartyRoleByPtId(party
						.getPartyId());
				PartyRole pr = null;
				if (null != liPr && liPr.size() > 0) {
					pr = liPr.get(0);
				}
				if (null == pr) {
					pr = new PartyRole();
					pr.setPartyId(party.getPartyId());
				}
				pr.setRoleType(bean.getRoleType().getAttrValue());
				party.setPartyRole(pr);
				Individual indivi = partyManager.getIndividual(party
						.getPartyId());
				PartyOrganization partyOrg = partyManager.getPartyOrg(party
						.getPartyId());
				String resul = bean.getPartyType().getSelectedItem().getValue()
						.toString();
				if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) { // 参与人个人信息
					if (null != partyOrg) {
						partyManager.remove(partyOrg);
					}
					if (indivi == null) {
						indivi = new Individual();
					}
					String maSu = indivi.getMarriageStatus();
					String naly = indivi.getNationality();
					PubUtil.fillPoFromBean(bean, indivi);
					indivi.setPartyId(party.getPartyId());
					if (null != bean.getMarriageStatus().getAttrValue()) {
						indivi.setMarriageStatus(bean.getMarriageStatus()
								.getAttrValue());
					} else {
						if (!StrUtil.isNullOrEmpty(maSu)) {
							indivi.setMarriageStatus(maSu);
						}
					}
					if (null != bean.getNationality().getAttrValue()) {
						indivi.setNationality(bean.getNationality()
								.getAttrValue());
					} else {
						if (!StrUtil.isNullOrEmpty(naly)) {
							indivi.setNationality(naly);
						}
					}
					party.setIndividual(indivi);
				} else {
					if (null != indivi) {
						partyManager.remove(indivi);
					}
					if (null == partyOrg) {
						partyOrg = new PartyOrganization();
					}
					PubUtil.fillPoFromBean(bean, partyOrg);
					party.setPartyOrganization(partyOrg);
				}
				if (!partyName.equals(party.getPartyName())) {
					Messagebox.show("您已修改了参与人名称，是否选择同步修改对应的员工?", "提示信息",
							Messagebox.OK | Messagebox.CANCEL,
							Messagebox.INFORMATION, new EventListener() {
								public void onEvent(Event event)
										throws Exception {
									Integer result = (Integer) event.getData();
									if (result == Messagebox.OK) {
										sysStaffNameForPtyName(party);
										Messagebox.show("同步修改员工名称成功！");
									}
								}
							});
				}
				Listitem isAddStaffInfo = bean.getAddStaffInfo()
						.getSelectedItem();
				if (null != isAddStaffInfo) {
					resul = (String) isAddStaffInfo.getValue();
					if (!StrUtil.isNullOrEmpty(resul)) {
						if (SffOrPtyCtants.CONST_ADD_STAFFINFO.equals(resul)
								&& !SffOrPtyCtants.MOD_INDIVIDUAL
										.equals(opType)) {
							PubUtil.fillPoFromBean(this.bean, staff);
							msg = checkStaffData();
							if (null != msg) {
								ZkUtil.showError(msg, "提示信息");
								return;
							}
							msg = null;
							String workProp = bean.getWorkProp().getAttrValue();
							staff.setWorkProp(workProp);
							staff.setLocationId(bean.getLocationId()
									.getPoliticalLocation().getLocationId());
							// staff.setUuid(StrUtil.getUUID());
							List<StaffExtendAttr> listValu = this
									.getStaffExtendAttrList(staff);
							if (null != listValu && listValu.size() > 0) {
								staff.setStaffExtendAttr(listValu);
							}
							// staff.setStaffCode(staffManager.gennerateStaffCode());
							// staff.setStaffNbr(staffManager.gennerateStaffNumber(workProp));
							StaffAccount sa = staffManager.getStaffAccount(
									null, staff.getStaffId());
							if (null == sa) {
								sa = new StaffAccount();
								// sa.setStaffPassword(passWord);
								sa.setStaffAccount(staffManager
										.gennerateStaffAccount(staff));
							}
							staff.setObjStaffAccount(sa);
							Long locId = bean.getLocationId()
									.getPoliticalLocation().getLocationId();
							staff.setLocationId(locId);
							party.setStaff(staff);
						}
					}
				}
				partyManager.updateParty(party);
			}
			Events.postEvent(SffOrPtyCtants.ON_OK, bean.getPartyEditWindow(),
					party);
			/**
			 * 组织树维护界面保存成功不关闭窗口
			 */
			if (!isTreeMainPage) {
				bean.getPartyEditWindow().onClose();
			}
		} catch (WrongValueException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 员工扩展属性
	 */
	public List<StaffExtendAttr> getStaffExtendAttrList(Staff staff) {

		List<StaffExtendAttr> extendAttrList = staff.getStaffExtendAttrList();
		List<StaffExtendAttr> beanList = this.bean.getStaffExtendAttrExt()
				.getExtendValueList();
		if (extendAttrList == null || extendAttrList.size() <= 0) {
			staff.setStaffExtendAttr(beanList);
		} else {
			for (StaffExtendAttr staffExtendAttr : beanList) {
				for (StaffExtendAttr dbStaffExtendAttr : extendAttrList) {
					if (staffExtendAttr.getStaffAttrSpecId().equals(
							dbStaffExtendAttr.getStaffAttrSpecId())) {
						String staffAttrVlue = staffExtendAttr
								.getStaffAttrValue();
						BeanUtils.copyProperties(staffExtendAttr,
								dbStaffExtendAttr);
						staffExtendAttr.setStaffAttrValue(staffAttrVlue);
					}
				}
			}
		}

		return beanList;
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-25 Wong
	 */
	private void bindEvent() throws Exception {
		PartyEditComposer.this.bean.getPartyEditWindow().addEventListener(
				"onPartyChange", new EventListener() {
					public void onEvent(final Event event) throws Exception {
						if (!StrUtil.isNullOrEmpty(event.getData())) {
							PartyEditComposer.this.arg = (HashMap) event
									.getData();
							bindBean();
						}
					}
				});
	}

	/**
	 * 取消事件 .
	 * 
	 * @author Wong 2013-5-30 Wong
	 */
	public void onCancel() {
		bean.getPartyEditWindow().onClose();
	}

	/**
	 * 参与人类型选择，组织或者个人 .
	 * 
	 * @author Wong 2013-5-27 Wong
	 */
	public void onSelect$partyType() {
		String resul = (String) bean.getPartyType().getSelectedItem()
				.getValue();
		if (!StrUtil.isNullOrEmpty(resul)) {
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {
				bean.getPel().setVisible(true);
				bean.getOrgs().setVisible(false);
			} else {
				bean.getPel().setVisible(false);
				bean.getOrgs().setVisible(true);
			}
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
				bean.getStaffInfo().setVisible(true);
			} else {
				bean.getStaffInfo().setVisible(false);
			}
		} else {
			bean.getStaffInfo().setVisible(false);
		}
	}

	/**
	 * 根据18或15位身份证取出生年月日
	 */
	private void getBirthday() {
		String certNum = (String) this.bean.getCertNumber().getValue();
		String certType = (String) this.bean.getCertType().getSelectedItem()
				.getValue();
		if (!StrUtil.isNullOrEmpty(certNum)
				&& PartyConstant.ATTR_VALUE_IDNO.equals(certType)) {
			if (PartyConstant.ATTR_VALUE_IDNO15 == certNum.length()
					|| PartyConstant.ATTR_VALUE_IDNO18 == certNum.length()) {
				this.bean.getBirthday().setValue(
						CertUtil.getBirthFromCard(certNum));
			}
		}
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

	/**
	 * 新增或修改员工时，集团统一邮箱=员工姓名全拼【重复的后面加数据区分】+".ah@chinatelecom.cn"
	 */
	// public void onChange$partyName() {
	// if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())
	// && (bean.getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
	// .getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
	// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
	// .getSelectedItem().getValue())) {
	// if (StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
	// bean.getGrpUnEmail().setValue(
	// ChineseSpellUtil.converterToSpell(bean.getPartyName()
	// .getValue()) + GroupMailConstant.GRP_UN_EMAIL);
	// }
	// } else {
	// bean.getGrpUnEmail().setValue(null);
	// }
	// }

	public void onChooseOKResponse(ForwardEvent event) throws Exception {

		String attrName = (String) event.getOrigin().getData();

		// if (!StrUtil.isEmpty(attrName) && attrName.equals("workProp")) {
		// if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())
		// && (bean.getWorkProp().getAttrValue()
		// .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
		// .getWorkProp().getAttrValue()
		// .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
		// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
		// .getSelectedItem().getValue())) {
		// if (StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
		// bean.getGrpUnEmail().setValue(
		// ChineseSpellUtil.converterToSpell(bean
		// .getPartyName().getValue())
		// + GroupMailConstant.GRP_UN_EMAIL);
		// }
		// } else {
		// bean.getGrpUnEmail().setValue(null);
		// }
		// }
	}

	// public void onSelect$headFlag() {
	// if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())
	// && (bean.getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
	// .getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
	// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
	// .getSelectedItem().getValue())) {
	// if (StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
	// bean.getGrpUnEmail().setValue(
	// ChineseSpellUtil.converterToSpell(bean.getPartyName()
	// .getValue()) + GroupMailConstant.GRP_UN_EMAIL);
	// }
	// } else {
	// bean.getGrpUnEmail().setValue(null);
	// }
	//
	// }

	public void onChange$certNumber() {
		getBirthday();
	}

	public void onSelect$certType() {
		getBirthday();
	}

	/**
	 * .
	 * 
	 * @param event
	 * @author Wong 2013-6-6 Wong
	 */
	public void onViewPartyContactInfoResponse(final ForwardEvent event) {
		pCInfo = (PartyContactInfo) event.getOrigin().getData();
		PubUtil.fillEditorBeanFromPo(pCInfo, bean);
	}

	public void onViewPartyCertificationResponse(final ForwardEvent event) {
		pCertif = (PartyCertification) event.getOrigin().getData();
		PubUtil.fillEditorBeanFromPo(pCertif, bean);
	}

	/**
	 * 选择
	 * 
	 * @throws Exception
	 */
	public void onSelectTreeOrganizationResponse(ForwardEvent event)
			throws Exception {
		Party party = (Party) event.getOrigin().getData();
		this.setButtonValid(true, false, false);
		if (party != null) {
			oldParty = party;
			arg.put("party", party);
			this.bindCombobox();
			this.bindBean();
		} else {
			this.bindBean();
		}
	}

	/**
	 * 初始化
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrganizationTreeManInitResponse() throws Exception {
		isTreeMainPage = true;
		this.bean.getPartyEditWindow().setWidth("100%");
		this.bean.getSaveStaff().setVisible(false);
		this.bean.getCancelStaff().setVisible(false);
		this.bean.getSaveButton().setVisible(true);
		this.bean.getEditButton().setVisible(true);
		this.bean.getRecoverButton().setVisible(true);
	}

	/**
	 * 编辑按钮
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		this.setButtonValid(false, true, true);
	}

	/**
	 * 保存按钮
	 * 
	 * @throws Exception
	 */
	public void onSave() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		this.setButtonValid(true, false, false);
		this.onOk();
	}

	/**
	 * 恢复按钮
	 * 
	 * @throws Exception
	 */
	public void onRecover() throws Exception {
		this.setButtonValid(true, false, false);
		arg.put("party", oldParty);
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 设置按钮状态
	 */
	private void setButtonValid(boolean canEdit, boolean canSave,
			boolean canvalid) {
		/**
		 * 推导树统一按钮不让编辑
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getEditButton().setDisabled(!canEdit);
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canvalid);
	}

	/**
	 * 检查参与人必填数据 .
	 * 
	 * @return
	 * @author Wong 2013-5-25 Wong
	 */
	private String checkPartyData() {
		if (StrUtil.isNullOrEmpty(bean.getPartyName().getValue())) {
			return "请填写参与人名称";
		}
		if (StrUtil.checkBlank(bean.getPartyName().getValue())) {
			return "参与人名称中含有空格";
		}
		Listitem selLst = bean.getPartyType().getSelectedItem();
		if (StrUtil.isNullOrEmpty(selLst) || StrUtil.isNullOrEmpty(selLst.getValue())) {
			return "请选择参与人类型";
		}
		if (null != selLst) {
			String resul = (String) selLst.getValue();
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {// 个人员工用户
				if (StrUtil.isNullOrEmpty(bean.getBirthday().getValue())) {
					return "请选择出生日期";
				}
				if (StrUtil.isNullOrEmpty(bean.getGender().getSelectedItem()
						.getValue())) {
					return "请选择性别";
				}
			} else {
				if (StrUtil.isNullOrEmpty(bean.getOrgType().getSelectedItem()
						.getValue())) {
					return "请选择组织类型";
				}
			}
		}
		if (StrUtil.isNullOrEmpty(bean.getRoleType().getAttrValue())) {
			return "请选择参与人角色类型";
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem()
					.getValue())) {
				return "请选择证件类型";
			}

			if (StrUtil.isNullOrEmpty(bean.getIdentityCardId()
					.getSelectedItem())
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
			if (StrUtil.isNullOrEmpty(bean.getCertSort().getSelectedItem()
					.getValue())) {
				return "请选择证件种类";
			}

			if (StrUtil.isNullOrEmpty(bean.getCertName().getValue())) {
				return "请填写证件名";
			}
			if (StrUtil.checkBlank(bean.getCertName().getValue())) {
				return "证件名中含有空格";
			}

			String certType = bean.getCertType().getSelectedItem().getValue()
					.toString();
			String certNum = bean.getCertNumber().getValue();

			String certName = bean.getCertName().getValue();

			if (PartyConstant.ATTR_VALUE_IDNO.equals(certType)) {
				try {
					boolean isRealName = CertUtil
							.checkIdCard(certNum, certName);
					if (isRealName) {
						return null;
					} else {
						return "实名认证未通过";
					}
				} catch (Exception e) {
					return "调用国政通接口失败";
				}
			}

			if (!StrUtil.isNullOrEmpty(certType)
					&& !StrUtil.isNullOrEmpty(certNum)) {
				PartyCertification queryPartyCertification = new PartyCertification();
				queryPartyCertification.setCertType(certType);
				queryPartyCertification.setCertNumber(certNum);
				PartyRole partyRole = partyManager
						.getPartyRoleByPartyCertification(queryPartyCertification);
				if (partyRole != null && partyRole.getPartyRoleId() != null) {
					StringBuffer sbMsg = new StringBuffer("该参与人的证件信息已被使用");
					Staff existStaff = staffManager
							.getStaffByPartyRoleId(partyRole.getPartyRoleId());
					if (existStaff != null) {
						sbMsg.append(",员工工号：" + existStaff.getStaffNbr());
						StaffOrganization staffOrganization = existStaff
								.getStaffOrganization();
						if (staffOrganization != null) {
							Organization org = staffOrganization
									.getOrganization();
							if (org != null) {
								sbMsg.append(",该员工归属组织为：" + org.getOrgName()
										+ ",组织编码：" + org.getOrgCode());
							}
						}
					}
					return sbMsg.toString();
				}
			}

			if (StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
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
				return "联系人名称中有空格";
			}
			if (StrUtil.isNullOrEmpty(bean.getContactGender().getSelectedItem()
					.getValue())) {
				return "请填写联系人性别";
			}
			if (StrUtil.isNullOrEmpty(bean.getMobilePhone().getValue())) {
				return "请填写移动电话";
			}
			/**
			 * 参与人-新增、修改-增加手机号排重
			 */
			PartyContactInfo partyContactInfo = getContactInfoByBean();
			if (partyManager.isExistsMobilePhone(partyContactInfo)) {
				return "移动电话有重复";
			}
			if (!InputFieldUtil.checkPhone(bean.getMobilePhone().getValue())) {
				return "移动电话格式有误";
			}
			if (!StrUtil.isNullOrEmpty(bean.getEmail().getValue())
					&& !InputFieldUtil.checkEmail(bean.getEmail().getValue())) {
				return "邮箱格式有误";
			}
			if (!StrUtil.isNullOrEmpty(bean.getPostCode().getValue())
					&& !InputFieldUtil.checkPost(bean.getPostCode().getValue())) {
				return "邮政编码有误";
			}

			/*
			 * if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue()) &&
			 * SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
			 * .getSelectedItem().getValue())) { if
			 * (bean.getWorkProp().getAttrValue()
			 * .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) ||
			 * bean.getWorkProp().getAttrValue()
			 * .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE)) { if
			 * (StrUtil.isNullOrEmpty(bean.getGrpUnEmail().getValue())) { return
			 * "用工性质为内部_合同制或内部派遣制时，集团统一邮箱不能为空"; } else if
			 * (!InputFieldUtil.isGrpUnEmail(bean .getGrpUnEmail().getValue()))
			 * { return "集团统一邮箱格式错误，正确格式应为姓名全拼【小写字母】加数字"; } } }
			 */

		}
		return null;
	}

	public void onCertTypeSelect() {
		if (bean.getCertType() != null
				&& bean.getCertType().getSelectedItem() != null
				&& bean.getCertType().getSelectedItem().getValue() != null) {
			bean.getCertNumber().setDisabled(false);
			bean.getIdentityCardId().setDisabled(false);
			bean.getIdentityCardId().selectItem(null);
		}
	}

	public void onIdentityCardIdSelect() {
		if (bean.getIdentityCardId() != null
				&& bean.getIdentityCardId().getSelectedItem() != null
				&& bean.getIdentityCardId().getSelectedItem().getValue() != null) {
			bean.getCertNumber().setDisabled(false);
		}
	}

	/**
	 * 检查员工必填项
	 * 
	 * @return
	 */
	private String checkStaffData() {
		if (StrUtil.isNullOrEmpty(bean.getLocationId().getValue())) {
			return "请填写行政区域标识";
		}
		if (StrUtil.isNullOrEmpty(bean.getStaffName().getValue())) {
			return "请填写员工名称";
		}
		if (StrUtil.checkBlank(bean.getStaffName().getValue())) {
			return "员工名称中含有空格";
		}
		if (StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())) {
			return "请选择用工性质";
		}
		if (StrUtil.isNullOrEmpty(bean.getStaffPosition().getSelectedItem()
				.getValue())) {
			return "请选择员工职位";
		}
		return null;
	}

	/**
	 * 选择组织类型响应
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSetTreeTypeResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			isDuceTree = (Boolean) event.getOrigin().getData();
		}
		if (isDuceTree) {
			this.setButtonValid(false, false, false);
		}
	}

	/**
	 * 新增参与人参与人证件信息必填
	 * 
	 * @return
	 * @author Zhu Lintao 2014-8-25
	 */
	private void getPartyCerficaByBean() {
		if (SffOrPtyCtants.ADD.equals(opType) || "bindParty".equals(opType)) {
			partyCertification = new PartyCertification();
			PubUtil.fillPoFromBean(bean, partyCertification);
		}
	}

	/**
	 * 新增参与人参与人联系信息必填
	 * 
	 * @return
	 * @author Wangy 2013-7-8
	 */
	private PartyContactInfo getContactInfoByBean() {
		PartyContactInfo partyContactInfo = new PartyContactInfo();
		PubUtil.fillPoFromBean(bean, partyContactInfo);
		partyContactInfo.setContactType(bean.getContactType().getAttrValue());
		return partyContactInfo;
	}

	/**
	 * 修改参与人名称同步员工名称
	 * 
	 * @param party
	 * @author Wangy 2013-7-8
	 */
	public void sysStaffNameForPtyName(Party party) {
		partyManager.modStaffNameForPtyName(party);
	}

	public void onAutogeneration() {
		if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())
				&& (bean.getWorkProp().getAttrValue()
						.startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
						.getWorkProp().getAttrValue()
						.startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
				&& !StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
						.getValue())
				&& SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
						.getSelectedItem().getValue())) {

			Party party = new Party();
			Staff staff = new Staff();

			party.setPartyName(bean.getPartyName().getValue());
			staff.setWorkProp(bean.getWorkProp().getAttrValue());
			party.setStaff(staff);

			bean.getGrpUnEmail().setValue(
					partyManager.generateGrpUnEmail(party, null, null));
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
					ActionKeys.PARTY_ADD_GRPUNEMAIL)) {
				canGrpUnEmail = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_ADD_AUTOGENERATION)) {
				canAutogeneration = true;
			}
		}

		bean.getGrpUnEmail().setDisabled(!canGrpUnEmail);
		bean.getAutogenerationBtn().setDisabled(!canAutogeneration);
	}
}
