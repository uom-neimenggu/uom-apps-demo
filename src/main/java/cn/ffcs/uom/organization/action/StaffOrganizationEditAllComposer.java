package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.ChineseSpellUtil;
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
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationEditAllBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.constants.PartyCertificationConstant;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyCertificationRuleManager;
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
import cn.ffcs.uom.staffrole.manager.RoleRuleManager;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.RoleRule;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

/**
 * 组织员工关系编辑Composer.
 * 
 * @author OUZHF
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class StaffOrganizationEditAllComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffOrganizationEditAllBean bean = new StaffOrganizationEditAllBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	@Resource
	private PartyManager partyManager;

	@Resource
	private StaffOrganizationManager staffOrganizationManager;

	private IdentityCardConfigManager identityCardConfigManager = (IdentityCardConfigManager) ApplicationContextUtil
			.getBean("identityCardConfigManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	@Resource(name = "partyCertificationRuleManager")
	private PartyCertificationRuleManager partyCertificationRuleManager;

	@Resource
	private StaticParameter staticParameter;

	@Resource
	private Md5Util md5Util;

	@Resource
	private StaffManager staffManager;

	/**
	 * 修改的组织关系
	 */
	private StaffOrganization oldStaffOrganization;

	/**
	 * 修改的员工
	 */
	private Staff staff;

	/**
	 * 修改的员工账号
	 */
	private StaffAccount sa;

	/**
	 * 修改的参与人信息
	 */
	private Party party;

	/**
	 * 获取参与人证件
	 */
	private PartyCertification partyCertification;

	/**
	 * 参与人个人信息
	 */
	private Individual indivi;

	/**
	 * 参与人组织信息
	 */
	private PartyOrganization partyOrg;
	/**
	 * 是否是组织树页面
	 */
	private Boolean isOrgTreePage;
	@Autowired
	private RoleRuleManager roleRuleManager;
	/**
	 * 是否是组织树内部组织页面
	 */
	private Boolean isPoliticalTab;
	/**
	 * 是否是组织树代理商页面
	 */
	private Boolean isAgentTab;
	/**
	 * 是否是组织树内部经营实体页面
	 */
	private Boolean isIbeTab;

	/**
	 * 用于刷新的组织关系
	 */
	private StaffOrganization refreshStaffOrganization;

	/**
	 * 修改之前的关联关系
	 */
	private String oldRalaCd;
	@Autowired
	private StaffRoleManager staffRoleManager;

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
		/**
		 * 对用工性质进行监听
		 */
		bean.getWorkProp().addForward("onChooseOK", this.self,
				"onChooseOKResponse");

		/**
		 * 对人员属性进行监听
		 */
		bean.getStaffProperty().addForward("onChooseOK", this.self,
				"onChooseOKResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffOrganizationEditAllWindow() throws Exception {
		variablePageLocation = StrUtil.strnull(arg.get("variablePageLocation"));
		portletInfoProvider = (IPortletInfoProvider) arg
				.get("portletInfoProvider");
		setPageLocation(variablePageLocation);
		bindEvent();
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 监听事件
	 * 
	 * @throws Exception
	 */
	private void bindEvent() throws Exception {
		StaffOrganizationEditAllComposer.this.bean.getWorkProp().getOkButton()
				.addEventListener("onClick", new EventListener() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						// partyCertificationRuleManager
						// .reloadStaffTypeListboxItems(
						// StaffOrganizationEditAllComposer.this.bean
						// .getStaffType(),
						// StaffOrganizationEditAllComposer.this.bean
						// .getWorkProp().getAttrValue());
					}
				});
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> ralaCdList = UomClassProvider.getValuesList(
				"StaffOrganization", "ralaCd");
		ListboxUtils.rendererForEdit(this.bean.getRalaCd(), ralaCdList);

		List<NodeVo> liTp = UomClassProvider.getValuesList("Staff", "parttime");
		ListboxUtils.rendererForEdit(this.bean.getPartTime(), liTp);

		liTp = UomClassProvider.getValuesList("Staff", "staffPosition");
		ListboxUtils.rendererForEdit(this.bean.getStaffPosition(), liTp);

		liTp = UomClassProvider.getValuesList("Party", "partyType");
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

		// List<NodeVo> staffTypeList = UomClassProvider.getValuesList("Staff",
		// "staffType");
		// ListboxUtils.rendererForEdit(this.bean.getStaffType(),
		// staffTypeList);
		// this.bean.getStaffType().setSelectedIndex(1);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldStaffOrganization = (StaffOrganization) arg
				.get("oldStaffOrganization");
		/**
		 * 是否是组织树页面，是的话组织不可选
		 */
		isOrgTreePage = (Boolean) arg.get("isOrgTreePage");
		if (isOrgTreePage != null && isOrgTreePage) {
			this.bean.getOrg().setDisabled(true);
		}
		if ("add".equals(opType)) {
			this.bean.getStaffOrganizationEditAllWindow().setTitle("组织员工关系新增");
			/**
			 * 新增默认排序号200
			 */
			this.bean.getStaffSeq().setValue(200);
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
			this.bean.getContactType().setInitialValue(contactTypeList);
			/**
			 * 在组织树界面的 “内部组织管理界面－>组织员工－>新增员工”，用工性质不能为“外部-代理商员工”
			 * 在组织树界面的“内部经营实体管理界面－>组织员工－>新增员工”，用工性质不能为“外部-代理商员工”
			 */
			isPoliticalTab = (Boolean) arg.get("isPoliticalTab");
			isIbeTab = (Boolean) arg.get("isIbeTab");
			if (isPoliticalTab || isIbeTab) {
				List<String> workPropList = new ArrayList<String>();
				workPropList.add(SffOrPtyCtants.WORKPROP_W_AGENT);
				this.bean.getWorkProp().setDisabledOptionNodes(workPropList);
			}
			/**
			 * 在组织树界面的
			 * “代理商管理界面－>组织员工－>新增员工”，用工性质固定为“外部-代理商员工”，参与人角色类型固定为“合作伙伴-代理商”
			 */
			isAgentTab = (Boolean) arg.get("isAgentTab");
			if (isAgentTab) {
				List<String> workPropList = new ArrayList<String>();
				workPropList.add(SffOrPtyCtants.WORKPROP_W_AGENT);
				this.bean.getWorkProp().setInitialValue(workPropList);
				this.bean.getWorkProp().setOptionNodes(workPropList);
				List<String> roleTypeList = new ArrayList<String>();
				roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_TELE);
				roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_WCOMP);
				this.bean.getRoleType().setOptionNodes(roleTypeList);
				// partyCertificationRuleManager.reloadStaffTypeListboxItems(
				// this.bean.getStaffType(),
				// SffOrPtyCtants.WORKPROP_W_AGENT);
			}
		} else if ("mod".equals(opType)) {
			this.bean.getStaffOrganizationEditAllWindow().setTitle("组织员工关系修改");
			this.bean.getPanPartyCer().setVisible(false);
			this.bean.getPanPartyConInfo().setVisible(false);
			oldStaffOrganization = (StaffOrganization) arg
					.get("updateStaffOrganization");
			this.bean.getOrg().setDisabled(true);
			/**
			 * 在组织树界面的 “内部组织管理界面－>组织员工－>新增员工”，用工性质不能为“外部-代理商员工”
			 * 在组织树界面的“内部经营实体管理界面－>组织员工－>新增员工”，用工性质不能为“外部-代理商员工”
			 */
			isPoliticalTab = (Boolean) arg.get("isPoliticalTab");
			isIbeTab = (Boolean) arg.get("isIbeTab");
			if (isPoliticalTab || isIbeTab) {
				List<String> workPropList = new ArrayList<String>();
				workPropList.add(SffOrPtyCtants.WORKPROP_W_AGENT);
				this.bean.getWorkProp().setDisabledOptionNodes(workPropList);
			}
			/**
			 * 在组织树界面的
			 * “代理商管理界面－>组织员工－>新增员工”，用工性质固定为“外部-代理商员工”，参与人角色类型固定为“合作伙伴-代理商”
			 */
			isAgentTab = (Boolean) arg.get("isAgentTab");
			if (isAgentTab) {
				List<String> workPropList = new ArrayList<String>();
				workPropList.add(SffOrPtyCtants.WORKPROP_W_AGENT);
				this.bean.getWorkProp().setInitialValue(workPropList);
				this.bean.getWorkProp().setOptionNodes(workPropList);
				List<String> roleTypeList = new ArrayList<String>();
				roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_TELE);
				roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_WCOMP);
				this.bean.getRoleType().setOptionNodes(roleTypeList);
			}
			if (oldStaffOrganization != null) {
				oldRalaCd = oldStaffOrganization.getRalaCd();
				oldStaffOrganization.setReason("");
				PubUtil.fillBeanFromPo(oldStaffOrganization, this.bean);
				Long staffId = oldStaffOrganization.getStaffId();
				if (null != staffId) {
					staff = staffManager.queryStaff(staffId);
					sa = staffManager.getStaffAccount(null, staffId);
					if (null != sa) {
						staff.setStaffAccount(sa.getStaffAccount());
						staff.setStaffPassword(sa.getStaffPassword());
						staff.setObjStaffAccount(sa);
					}
					PubUtil.fillBeanFromPo(staff, this.bean);
					this.bean.getStaffAccount().setValue(
							staff.getStaffAccount());
					PoliticalLocation pl = PoliticalLocation
							.getPoliticalLocation(staff.getLocationId());
					if (null != pl) {
						staff.setLocationName(pl.getLocationName());
						bean.getLocationId().setPoliticalLocation(pl);
						bean.getLocationId().getPoliticalLocation()
								.setLocationName(pl.getLocationName());
					}
					String conNm = staticParameter.handling("Staff",
							"workProp", staff.getWorkProp());
					if (null != conNm) {
						bean.getWorkProp().setValue(conNm);
					}
					String staffConNm = staticParameter.handling("Staff",
							"staffProperty", staff.getStaffProperty());
					if (null != staffConNm) {
						bean.getStaffProperty().setValue(staffConNm);
					}
					Long party_role_id = staff.getPartyRoleId();
					PartyRole partyRole = partyManager
							.getPartyRole(party_role_id);
					if (null != partyRole) {
						party = partyManager.queryParty(partyRole.getPartyId());
						party.setPartyRole(partyRole);
						PubUtil.fillBeanFromPo(party, this.bean);
						String mg = null;
						if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(party
								.getPartyType())) {
							bean.getPel().setVisible(true);
							bean.getOrgs().setVisible(false);
							indivi = partyManager.getIndividual(party
									.getPartyId());
							if (null != indivi) {
								mg = staticParameter.handling(new String[] {
										"Individual", "maritalStatus",
										indivi.getMarriageStatus() });
								if (null != mg) {
									bean.getMarriageStatus().setValue(mg);
								}
								mg = staticParameter.handling(new String[] {
										"Individual", "nationality",
										indivi.getNationality() });
								if (null != mg) {
									bean.getNationality().setValue(mg);
								}
								PubUtil.fillBeanFromPo(indivi, bean);
								party.setIndividual(indivi);
							}
						} else {
							bean.getOrgs().setVisible(true);
							bean.getPel().setVisible(false);
							if (party.getPartyId() != null) {
								partyOrg = partyManager.getPartyOrg(party
										.getPartyId());
								if (null != partyOrg) {
									PubUtil.fillBeanFromPo(partyOrg, bean);
								}
								party.setPartyOrganization(partyOrg);
							}
						}
						if (party.getPartyId() != null) {
							List<PartyRole> liPr = partyManager
									.getPartyRoleByPtId(party.getPartyId());
							if (null != liPr && liPr.size() > 0) {
								mg = staticParameter.handling(new String[] {
										"PartyRole", "roleType",
										liPr.get(0).getRoleType() });
								bean.getRoleType().setValue(mg);
							}
						}
					}
					List<StaffExtendAttr> staffExtendAttrList = staffManager
							.getStaffExtendAttr(staff.getStaffId());
					if (null != staffExtendAttrList
							&& staffExtendAttrList.size() > 0) {
						bean.getStaffExtendAttrExt().setExtendValue(
								staffExtendAttrList);
						staff.setStaffExtendAttr(staffExtendAttrList);
					}
				}

				if (oldStaffOrganization.getStaffSeq() != null) {
					this.bean.getStaffSeq()
							.setValue(
									new Integer(oldStaffOrganization
											.getStaffSeq() + ""));
				}
				this.bean.getStaffAccount().setValue(staff.getStaffAccount());
			}

			if (staff.getWorkProp().equals(SffOrPtyCtants.WORKPROP_W_AGENT)) {
				// partyCertificationRuleManager.reloadStaffTypeListboxItems(
				// bean.getStaffType(), staff.getWorkProp());
			}
			List<StaffRole> staffRoles = staffRoleManager
					.queryStaffaRoles(staff);
			bean.getStaffRoleBandboxExt().setInitialValue(staffRoles);
		}
		if (oldStaffOrganization != null
				&& oldStaffOrganization.getOrgId() != null) {
			Organization org = organizationManager.getById(oldStaffOrganization
					.getOrgId());
			bean.getOrg().setOrganization(org);
		}
	}

	/**
	 * 保存.
	 */
	@SuppressWarnings("unchecked")
	public void onOk() throws Exception {
		StaffOrganization staffOrganization = null;
		if ("add".equals(opType)) {
			staffOrganization = new StaffOrganization();
		} else if ("mod".equals(opType)) {
			// staffOrganization = oldStaffOrganization;
			/**
			 * 20131024修改的逻辑（修改员工，组织，关系类型3个有修改一个：先删除旧的，新增一条新的保持旧的数据的记录）
			 */
			staffOrganization = new StaffOrganization();
			BeanUtils.copyProperties(staffOrganization, oldStaffOrganization);
		}
		List<StaffRole> staffRoles = bean.getStaffRoleBandboxExt()
				.getStaffRoles();
		String msg2 = checkStaffRoles(staffRoles);
		if (msg2 != null) {
			Messagebox.show(msg2);
			return;
		}
		// 填充对象
		PubUtil.fillPoFromBean(bean, staffOrganization);
		Organization org = bean.getOrg().getOrganization();
		if (org != null) {
			staffOrganization.setOrgId(org.getOrgId());
		}
		if (this.bean.getStaffSeq().getValue() != null) {
			staffOrganization.setStaffSeq(new Long(this.bean.getStaffSeq()
					.getValue() + ""));
		}
		String msg = this.doValidate(staffOrganization);
		if (!StrUtil.isNullOrEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		msg = this.checkPartyData();
		if (!StrUtil.isNullOrEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		msg = this.checkStaffData();
		if (!StrUtil.isNullOrEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		String passWord = bean.getStaffPassword().getValue();
		/**
		 * 获取MD5加密后的密码，统一认证接入，系统上线前把生产数据库员工密码字段置为空，程序新增员工的时候，密码设置为空字符串。
		 */
		// passWord = md5Util.getMD5(passWord);
		passWord = "";
		if ("add".equals(opType)) {
			/*** 添加参与人信息 ***/
			String roleType = bean.getRoleType().getAttrValue();
			if (StrUtil.isNullOrEmpty(roleType)) {
				ZkUtil.showError("请选择相应的参与人角色", "提示信息");
				return;
			}
			Listitem mlPyTy = bean.getPartyType().getSelectedItem();
			if (null == mlPyTy) {
				ZkUtil.showError("请选择相应参与人类型", "提示信息");
				return;
			}
			/*
			 * if
			 * (partyManager.checkPartyNameExits(bean.getPartyName().getValue()
			 * .trim())) { ZkUtil.showError("参与人名字被使用", "提示信息"); }
			 */
			Party party = Party.newInstance();
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
			this.getPartyCerficaByBean();
			if (null == partyCertification) {
				return;
			}

			boolean certIsNotExist = partyManager
					.checkIsExistCertificate(partyCertification.getCertNumber());
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

			if (!StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
				PartyContactInfo partyContactInfo = new PartyContactInfo();
				partyContactInfo.setHeadFlag(SffOrPtyCtants.HEADFLAG);
				partyContactInfo.setGrpUnEmail(bean.getGrpUnEmail().getValue());
				List<PartyContactInfo> partyContactInfoList = partyManager
						.queryDefaultPartyContactInfo(partyContactInfo);
				if (partyContactInfoList != null
						&& partyContactInfoList.size() > 0) {
					ZkUtil.showInformation("集团统一邮箱在主数据中已经存在,请修改！", "提示信息");
					return;
				}
			}

			staffOrganization.setPartyObj(party);
			/*** 添加参与人信息结束 ***/

			/*** 添加员工信息 ***/
			Staff staff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, staff);
			staff.setLocationId(bean.getLocationId().getPoliticalLocation()
					.getLocationId());
			staff.setUuid(StrUtil.getUUID());
			String staffCd = staffManager.gennerateStaffCode();
			if (null == staffCd) {
				Messagebox.show("员工编码生成异常");
				return;
			}
			List<StaffExtendAttr> listValu = this.getStaffExtendAttrList(staff);
			if (null != listValu && listValu.size() > 0) {
				staff.setStaffExtendAttr(listValu);
			}
			staff.setWorkProp(bean.getWorkProp().getAttrValue());
			staff.setStaffProperty(bean.getStaffProperty().getAttrValue());
			staff.setStaffCode(staffCd);
			staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
					.getWorkProp()));
			StaffAccount staffAcc = new StaffAccount();
			staff.setObjStaffAccount(staffAcc);
			staffAcc.setStaffPassword(passWord);
			String sffAcc = bean.getStaffAccount().getValue().trim();
			if (StrUtil.isNullOrEmpty(sffAcc)) {
				sffAcc = staffManager.gennerateStaffAccount(staff);
			} else {
				StaffAccount sa = staffManager
						.getStaffAccountByStaffAccount(sffAcc);
				if (null != sa) {
					Messagebox.show("员工账号有重复");
					return;
				}
			}
			staffAcc.setStaffAccount(sffAcc); // 员工账号
			staffOrganization.setStaffObj(staff);
			/*** 添加员工信息结束 ***/

			/*** 添加组织员工信息 ***/
			/**
			 * 新增需要判断是否该关系已经存在
			 */
			if (staffOrganizationManager.isExitsUserCode(staffOrganization
					.getUserCode())) {
				ZkUtil.showError("该员工账号已存在，请使用其他账号登陆", "提示信息");
				return;
			}
			/**
			 * 20130910新增员工组织关系：因为是新增所以肯定是是唯一关系，唯一关系设置为为归属关系
			 */
			staffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);

			staffOrganization.setUserCode(staffOrganizationManager
					.getOrgUserCode());

			// 添加员工组织关系规则校验 zhulintao
			if (true) {
				String msgStr = staffOrganizationManager.doStaffOrgRelRule(
						staff, null, org);
				if (!StrUtil.isNullOrEmpty(msgStr)) {
					ZkUtil.showError(msgStr, "提示信息");
					return;
				}
			}
			if (staffRoles != null && staffRoles.size() > 0) {
				staffOrganization.setAddStaffRoles(staffRoles);
			}
			staffOrganizationManager
					.addStaffOrganizationWithParty(staffOrganization);
			Events.postEvent(Events.ON_OK,
					bean.getStaffOrganizationEditAllWindow(), staffOrganization);
			bean.getStaffOrganizationEditAllWindow().onClose();
		} else if ("mod".equals(opType)) {
			// 更新员工信息
			List<StaffRole> ownStaffRoleList = staffRoleManager
					.queryStaffaRoles(staff);
			String workProp = staff.getWorkProp();
			String staffProperty = staff.getStaffProperty();
			String strSffAcc = bean.getStaffAccount().getValue().trim();
			if (StrUtil.isNullOrEmpty(strSffAcc)) {
				Messagebox.show("员工账号不可为空");
				return;
			}
			StaffAccount sffAcc = staff.getObjStaffAccount();
			if (null == sffAcc) {
				sffAcc = new StaffAccount();
			} else {
				String oldStaffAccountStr = sffAcc.getStaffAccount();
				String newStaffAccountStr = strSffAcc;
				// if(!newStaffAccountStr.equals(oldStaffAccountStr)){
				// StaffAccount sa =
				// staffManager.getStaffAccountByStaffAccount(newStaffAccountStr);
				// if(null != sa){
				// Messagebox.show("员工账号有重复");
				// return;
				// }
				// }
			}
			PubUtil.fillPoFromBean(bean, staff);
			sa.setStaffAccount(strSffAcc);
			sa.setStaffId(staff.getStaffId());
			// sa.setStaffPassword(passWord);
			staff.setObjStaffAccount(sa);
			// staff.setStaffNbr(strSffAcc);
			staff.setStaffAccount(strSffAcc);
			Long locId = bean.getLocationId().getPoliticalLocation()
					.getLocationId();
			staff.setLocationId(locId);
			List<StaffExtendAttr> listValu = this.getStaffExtendAttrList(staff);
			if (null != listValu && listValu.size() > 0) {
				staff.setStaffExtendAttr(listValu);
			}
			if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())) {
				staff.setWorkProp(bean.getWorkProp().getAttrValue());
			} else {
				if (!StrUtil.isNullOrEmpty(workProp)) {
					staff.setWorkProp(workProp);
				}
			}
			if (!StrUtil.isNullOrEmpty(bean.getStaffProperty().getAttrValue())) {
				staff.setStaffProperty(bean.getStaffProperty().getAttrValue());
			} else {
				if (!StrUtil.isNullOrEmpty(staffProperty)) {
					staff.setStaffProperty(staffProperty);
				}
			}
			staffOrganization.setStaffObj(staff);
			// 更新参与人信息
			String partyName = party.getPartyName();
			PubUtil.fillPoFromBean(bean, party);
			List<PartyRole> liPr = partyManager.getPartyRoleByPtId(party
					.getPartyId());
			PartyRole pr = null;
			if (null != liPr && liPr.size() > 0) {
				pr = liPr.get(0);
			}
			if (null != pr) {
				if (!StrUtil.isNullOrEmpty(bean.getRoleType().getAttrValue())) {
					pr.setRoleType(bean.getRoleType().getAttrValue());
				}
			} else {
				pr = new PartyRole();
				pr.setPartyId(party.getPartyId());
				pr.setRoleType(bean.getRoleType().getAttrValue());
			}
			party.setPartyRole(pr);
			indivi = partyManager.getIndividual(party.getPartyId());
			partyOrg = partyManager.getPartyOrg(party.getPartyId());
			String resul = bean.getPartyType().getSelectedItem().getValue()
					.toString();
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) { // 参与人个人信息
				String maSu = null;
				String naly = null;
				if (indivi == null) {
					indivi = new Individual();
				} else {
					maSu = indivi.getMarriageStatus();
					naly = indivi.getNationality();
				}
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
					indivi.setNationality(bean.getNationality().getAttrValue());
				} else {
					if (!StrUtil.isNullOrEmpty(naly)) {
						indivi.setNationality(naly);
					}
				}
				party.setIndividual(indivi);
			} else {
				if (null == partyOrg) {
					partyOrg = new PartyOrganization();
				}
				PubUtil.fillPoFromBean(bean, partyOrg);
				party.setPartyOrganization(partyOrg);
			}
			staffOrganization.setPartyObj(party);
			// 参与人证件号码
			PartyCertification pac = partyManager
					.getDefaultPartyCertification(party.getPartyId());

			String userCode = staffOrganization.getUserCode();
			if (StrUtil.isNullOrEmpty(userCode)) {
				staffOrganization.setUserCode(staffOrganizationManager
						.getOrgUserCode());
			}

			// 添加员工组织关系规则校验 zhulintao
			if (false) {
				String msgStr = staffOrganizationManager.doStaffOrgRelRule(
						staff, null, staffOrganization.getOrganization());
				if (!StrUtil.isNullOrEmpty(msgStr)) {
					ZkUtil.showError(msgStr, "提示信息");
					return;
				}
			}
			/*
			 * if (staffRoles!=null && staffRoles.size()>0) {
			 * staffOrganization.setStaffRoles(staffRoles); }
			 */
			List<StaffRole> addStaffRoleList = new ArrayList<StaffRole>();
			if (staffRoles != null && staffRoles.size() > 0) {
				if (ownStaffRoleList != null && ownStaffRoleList.size() > 0) {
					for (StaffRole staffRole : staffRoles) {
						if (!ownStaffRoleList.contains(staffRole)) {
							addStaffRoleList.add(staffRole);
						}
					}
				} else {
					addStaffRoleList = staffRoles;
				}

			}
			List<StaffRole> delStaffRoleList = new ArrayList<StaffRole>();
			if (ownStaffRoleList != null && ownStaffRoleList.size() > 0) {
				if (staffRoles != null && staffRoles.size() > 0) {
					for (StaffRole staffRole : ownStaffRoleList) {
						if (!staffRoles.contains(staffRole)) {
							delStaffRoleList.add(staffRole);
						}
					}
				} else {
					delStaffRoleList = staffRoles;
				}

			}
			staffOrganization.setAddStaffRoles(addStaffRoleList);
			staffOrganization.setDelStaffRoles(delStaffRoleList);
			this.staffOrganizationManager
					.updateStaffOrganizationWithParty(staffOrganization);

			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {
				if (null != partyOrg) {
					partyManager.remove(partyOrg);
				}
			} else {
				if (null != indivi) {
					partyManager.remove(indivi);
				}
			}
			if (!partyName.equals(party.getPartyName())) {
				Messagebox.show("您已修改了参与人名称，是否选择同步修改对应的员工?", "提示信息",
						Messagebox.OK | Messagebox.CANCEL,
						Messagebox.INFORMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								Integer result = (Integer) event.getData();
								if (result == Messagebox.OK) {
									sysStaffNameForPtyName(party);
									Messagebox.show("同步修改员工名称成功！");
								}
							}
						});
			}

			/**
			 * 如果员工，组织，关联关系都没变直接做更新
			 */
			if (staffOrganization.getOrgId().equals(
					oldStaffOrganization.getOrgId())
					&& staffOrganization.getStaffId().equals(
							oldStaffOrganization.getStaffId())
					&& staffOrganization.getRalaCd().equals(
							oldStaffOrganization.getRalaCd())) {
				/**
				 * 其实就是只改了序号和备注
				 */
				oldStaffOrganization.setStaffSeq(staffOrganization
						.getStaffSeq());
				oldStaffOrganization.setNote(staffOrganization.getNote());
				if (StrUtil.isEmpty(oldStaffOrganization.getUserCode())) {
					oldStaffOrganization.setUserCode(staffOrganizationManager
							.getOrgUserCode());
				}
				this.staffOrganizationManager
						.updateStaffOrganization(oldStaffOrganization);
				Events.postEvent(Events.ON_OK,
						bean.getStaffOrganizationEditAllWindow(),
						staffOrganization);
				bean.getStaffOrganizationEditAllWindow().onClose();
			} else {
				/**
				 * 
				 * 20130911修改：如果非归属关系改为非归属关系，则修改；如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
				 * 如果将归属关系修改为非归属关系，则判断弹出一个对话框
				 * 
				 */
				StaffOrganization queryStaffOrganization = new StaffOrganization();
				queryStaffOrganization.setStaffId(staffOrganization
						.getStaffId());
				List<StaffOrganization> listStaffOrganization = staffOrganizationManager
						.queryStaffOrganizationList(queryStaffOrganization);
				if (null != listStaffOrganization
						&& listStaffOrganization.size() == 1) {
					// 如果没有其他关系则强制修改为归属关系
					if (!BaseUnitConstants.RALA_CD_1
							.equals(oldStaffOrganization.getRalaCd())) {
						// 如果没有其他关系则不让进行修改操作
						staffOrganization
								.setRalaCd(BaseUnitConstants.RALA_CD_1);
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
					}
					ZkUtil.showInformation("该员工只有一条员工组织记录强制为归属关系", "提示信息");
					Events.postEvent(Events.ON_OK,
							bean.getStaffOrganizationEditAllWindow(),
							staffOrganization);
					bean.getStaffOrganizationEditAllWindow().onClose();
				} else if (null != listStaffOrganization
						&& listStaffOrganization.size() > 1) {
					if (BaseUnitConstants.RALA_CD_1.equals(staffOrganization
							.getRalaCd())) {
						// 如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
						StaffOrganization querySo = new StaffOrganization();
						querySo.setStaffId(staffOrganization.getStaffId());
						querySo.setRalaCd(BaseUnitConstants.RALA_CD_1);
						/**
						 * 原来归属改非归属
						 */
						List<StaffOrganization> needUpdateToRela3List = this.staffOrganizationManager
								.queryStaffOrganizationList(querySo);
						staffOrganization
								.setNeedUpdateToRela3List(needUpdateToRela3List);
						/**
						 * 删除旧的 关系，复制新增归属关系
						 */
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
						Events.postEvent(Events.ON_OK,
								bean.getStaffOrganizationEditAllWindow(),
								staffOrganization);
						bean.getStaffOrganizationEditAllWindow().onClose();
					} else if (!BaseUnitConstants.RALA_CD_1.equals(oldRalaCd)
							&& !BaseUnitConstants.RALA_CD_1
									.equals(staffOrganization.getRalaCd())) {
						// 非归属关系修改为非归属关系
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
						Events.postEvent(Events.ON_OK,
								bean.getStaffOrganizationEditAllWindow(),
								staffOrganization);
						bean.getStaffOrganizationEditAllWindow().onClose();
					} else {
						// 如果将归属关系修改为非归属关系，则判断弹出一个对话框
						final StaffOrganization innerStaffOrganization = staffOrganization;
						Map arg = new HashMap();
						arg.put("oldStaffOrganization", oldStaffOrganization);
						Window win = (Window) Executions
								.createComponents(
										"/pages/organization/staff_organization_relation_listbox.zul",
										this.self, arg);
						win.doModal();
						win.addEventListener(Events.ON_OK, new EventListener() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (null != event.getData()) {
									StaffOrganization chooseSo = (StaffOrganization) event
											.getData();
									if (chooseSo != null) {
										List<StaffOrganization> needUpdateToRela1List = new ArrayList<StaffOrganization>();
										needUpdateToRela1List.add(chooseSo);
										innerStaffOrganization
												.setNeedUpdateToRela1List(needUpdateToRela1List);
										List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
										needRemoveList
												.add(oldStaffOrganization);
										innerStaffOrganization
												.setNeedRemoveList(needRemoveList);
										staffOrganizationManager
												.updateStaffOrganizationRelation(innerStaffOrganization);
										Events.postEvent(
												Events.ON_OK,
												bean.getStaffOrganizationEditAllWindow(),
												innerStaffOrganization);
										bean.getStaffOrganizationEditAllWindow()
												.onClose();
									}
								}
							}
						});
					}
				}
			}
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

	public void refreshStaffOrganizationListbox() {
		Events.postEvent(Events.ON_OK,
				bean.getStaffOrganizationEditAllWindow(),
				refreshStaffOrganization);
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getStaffOrganizationEditAllWindow().onClose();
	}

	/**
	 * 验证组织员工数据
	 * 
	 * @param staffOrganization
	 * @return
	 */
	private String doValidate(StaffOrganization staffOrganization) {
		if (staffOrganization.getOrgId() == null) {
			return "组织不能为空！";
		}
		if (staffOrganization.getRalaCd() == null) {
			return "关联类型不能为空！";
		}
		if (StrUtil.isNullOrEmpty(staffOrganization.getReason())) {
			return "变更原因不能为空！";
		}

		return null;
	}

	/**
	 * 验证参与人数据
	 * 
	 * @param staffOrganization
	 * @return
	 */
	private String checkPartyData() {
		if (StrUtil.isNullOrEmpty(bean.getPartyName().getValue())) {
			return "请填写参与人名称";
		}
		if (StrUtil.checkBlank(bean.getPartyName().getValue())) {
			return "参与人名称中含有空格";
		}
		if (StrUtil.isNullOrEmpty(bean.getPartyType().getSelectedItem())) {
			return "请选择参与人类型";
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (StrUtil.isNullOrEmpty(bean.getRoleType().getAttrValue())) {
				return "请选择参与人角色类型";
			}
		}
		Listitem selLst = bean.getPartyType().getSelectedItem();
		if (null != selLst) {
			String resul = (String) selLst.getValue();
			if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(resul)) {// 个人员工用户
				if (StrUtil.isNullOrEmpty(bean.getBirthday().getValue())) {
					return "请选择出生日期";
				}
				if (StrUtil.isNullOrEmpty(bean.getGender())) {
					return "请选择性别";
				}
			} else {
				if (StrUtil.isNullOrEmpty(bean.getOrgType())) {
					return "请选择组织类型";
				}
			}
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
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
				return "联系人名称中含有空格";
			}
			if (StrUtil.isNullOrEmpty(bean.getContactGender().getSelectedItem()
					.getValue())) {
				return "请填写联系人性别";
			}
			if (StrUtil.isNullOrEmpty(bean.getMobilePhone().getValue())) {
				return "请填写移动电话";
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
			/**
			 * 参与人-新增、修改-增加手机号排重
			 */
			PartyContactInfo partyContactInfo = getContactInfoByBean();
			if (partyManager.isExistsMobilePhone(partyContactInfo)) {
				return "移动电话有重复";
			}
			if (StrUtil.isNullOrEmpty(bean.getCertType().getSelectedItem())) {
				return "请填写证件类型";
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
			 * 
			 * if
			 * (!StrUtil.isNullOrEmpty(bean.getStaffProperty().getAttrValue())
			 * && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
			 * .getSelectedItem().getValue())) { if
			 * (bean.getStaffProperty().getAttrValue()
			 * .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) ||
			 * bean.getStaffProperty().getAttrValue()
			 * .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE)) { if
			 * (StrUtil.isNullOrEmpty(bean.getGrpUnEmail().getValue())) { return
			 * "用工性质为合同制或派遣制时，集团统一邮箱不能为空"; } else if
			 * (!InputFieldUtil.isGrpUnEmail(bean .getGrpUnEmail().getValue()))
			 * { return "集团统一邮箱格式错误，正确格式应为姓名全拼【小写字母】加数字"; } } }
			 */

			if (StrUtil.isNullOrEmpty(bean.getIdentityCardId()
					.getSelectedItem())
					|| StrUtil.isNullOrEmpty(bean.getIdentityCardId()
							.getSelectedItem().getValue())) {
				return "请选择类型";
			} else if (StrUtil.isNullOrEmpty(bean.getCertNumber().getValue())) {
				return "请填写证件号码";
			}

			if (StrUtil.isNullOrEmpty(bean.getCertName().getValue())) {
				return "请填写证件名";
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
			if (StrUtil.isNullOrEmpty(bean.getCertSort().getSelectedItem())) {
				return "请选择证件种类";
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
				List<PartyCertification> pcList = partyManager
						.getPartyCertificationList(queryPartyCertification);
				if (pcList != null & pcList.size() > 0 & 1 > 2) {
					PartyRole partyRole = partyManager
							.getPartyRoleByPartyCertification(queryPartyCertification);
					if (partyRole != null && partyRole.getPartyRoleId() != null) {
						StringBuffer sbMsg = new StringBuffer("该参与人的证件信息已被使用");
						Staff existStaff = staffManager
								.getStaffByPartyRoleId(partyRole
										.getPartyRoleId());
						if (existStaff != null) {
							sbMsg.append(",员工工号：" + existStaff.getStaffNbr());
							StaffOrganization staffOrganization = existStaff
									.getStaffOrganization();
							if (staffOrganization != null) {
								Organization org = staffOrganization
										.getOrganization();
								if (org != null) {
									sbMsg.append(",该员工归属组织为："
											+ org.getOrgName() + ",组织编码："
											+ org.getOrgCode());
								}
							}
						}
						return sbMsg.toString();
					}
				}
			}
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
	 * 验证员工信息数据
	 * 
	 * @param staffOrganization
	 * @return
	 */
	private String checkStaffData() {
		if (StrUtil.isNullOrEmpty(bean.getStaffName().getValue())) {
			return "请填写请填写员工名称";
		}
		if (StrUtil.checkBlank(bean.getStaffName().getValue())) {
			return "员工名称中含有空格";
		}
		if (StrUtil.isNullOrEmpty(bean.getLocationId().getValue())) {
			return "请填写行政区域标识";
		}
		if (StrUtil.isNullOrEmpty(bean.getStaffPosition().getSelectedItem()
				.getValue())) {
			return "请选择员工职位";
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
			if (StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())) {
				return "请选择员工性质";
			}
			if (StrUtil.isNullOrEmpty(bean.getStaffProperty().getAttrValue())) {
				return "请选择人员属性";
			}
		}
		return null;
	}

	/**
	 * 新增参与人参与人证件信息必填
	 * 
	 * @return
	 * @author ZhuLintao 2013-7-8
	 */
	private void getPartyCerficaByBean() {

		IdentityCardConfig identityCardConfig = null;
		Long identityCardId = null;

		if (SffOrPtyCtants.ADD.equals(opType)) {

			String certType = bean.getCertType().getSelectedItem().getValue()
					.toString();
			String certNum = bean.getCertNumber().getValue();
			if (!StrUtil.isNullOrEmpty(certType)) {

				if (StrUtil.isNullOrEmpty(certNum)) {
					if (PartyConstant.ATTR_VALUE_IDNO.equals(bean.getCertType()
							.getSelectedItem().getValue())
							&& !bean.getIdentityCardId().getSelectedItem()
									.getValue()
									.equals(PartyConstant.IDENTITY_CARD_TMP)) {
						identityCardId = Long.valueOf((String) bean
								.getIdentityCardId().getSelectedItem()
								.getValue());

						identityCardConfig = identityCardConfigManager
								.getIdentityCardConfig(identityCardId);

						certNum = DefaultDaoFactory.getDefaultDao().genTransId(
								identityCardConfig.getIdentityCardPrefix(), 18,
								"SEQ_TEMP_IDENTITY_CARD_ID");

						bean.getCertNumber().setValue(certNum);
					}
				}

				PartyCertification queryPartyCertification = new PartyCertification();
				queryPartyCertification.setCertType(certType);
				queryPartyCertification.setCertNumber(certNum);
			}
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

	public void onChange$certNumber() {
		getBirthday();
	}

	public void onSelect$certType() {
		getBirthday();
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
	// public void onChange$staffName() {
	// if (!StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())
	// && (bean.getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
	// .getWorkProp().getAttrValue()
	// .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
	// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
	// .getSelectedItem().getValue())) {
	// if (StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
	// bean.getGrpUnEmail().setValue(
	// ChineseSpellUtil.converterToSpell(bean.getStaffName()
	// .getValue()) + GroupMailConstant.GRP_UN_EMAIL);
	// }
	// } else {
	// bean.getGrpUnEmail().setValue(null);
	// }
	// }

	public void onChooseOKResponse(ForwardEvent event) throws Exception {

		String attrName = (String) event.getOrigin().getData();

		// if (!StrUtil.isEmpty(attrName) && attrName.equals("workProp")) {
		// if (!StrUtil.isEmpty(bean.getWorkProp().getAttrValue())
		// && (bean.getWorkProp().getAttrValue()
		// .startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
		// .getWorkProp().getAttrValue()
		// .startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
		// && SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
		// .getSelectedItem().getValue())) {
		// if (StrUtil.isEmpty(bean.getGrpUnEmail().getValue())) {
		// bean.getGrpUnEmail().setValue(
		// ChineseSpellUtil.converterToSpell(bean
		// .getStaffName().getValue())
		// + GroupMailConstant.GRP_UN_EMAIL);
		// }
		// } else {
		// bean.getGrpUnEmail().setValue(null);
		// }
		//
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

	/**
	 * 参与人类型选择，组织或者个人；角色类型做相应修改个人对应员工（电信员工，外公司员工）、组织对应合作伙伴（代理商，供应商）
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
				if (isAgentTab) {
					List<String> roleTypeList = new ArrayList<String>();
					roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_TELE);
					roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_WCOMP);
					this.bean.getRoleType().setOptionNodes(roleTypeList);
				}
			} else {
				bean.getPel().setVisible(false);
				bean.getOrgs().setVisible(true);
				if (isAgentTab) {
					List<String> roleTypeList = new ArrayList<String>();
					roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_AGENT);
					roleTypeList.add(PartyConstant.PARTY_ROLE_TYPE_SUPPLIER);
					this.bean.getRoleType().setOptionNodes(roleTypeList);
				}
			}
		}
	}

	private String checkStaffRoles(List<StaffRole> staffRoles) {
		if (staffRoles != null && staffRoles.size() > 0) {
			List<RoleRule> roleRules = roleRuleManager.getAllRules();
			HashMap<RoleRule, Integer> onlyRoleMap = new HashMap<RoleRule, Integer>();
			if (roleRules != null && roleRules.size() > 0) {
				for (RoleRule roleRule : roleRules) {
					if ("1".equals(roleRule.getRuleType())) {
						// 单选角色
						onlyRoleMap.put(roleRule, 0);
					}
				}
			}
			for (int i = 0; i < staffRoles.size(); i++) {
				StaffRole staffRole = staffRoles.get(i);
				for (Entry<RoleRule, Integer> entry : onlyRoleMap.entrySet()) {
					if (entry.getKey().getRole() == staffRole.getRoleParentId()) {
						entry.setValue(entry.getValue() + 1);
						if (entry.getValue() > 1) {
							return entry.getKey().getMessage();
						}
					}
				}
			}
		}
		return null;
	}

	public void onAutogeneration() {
		if (!StrUtil.isNullOrEmpty(bean.getStaffProperty().getAttrValue())
				&& (bean.getStaffProperty().getAttrValue()
						.startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || bean
						.getStaffProperty().getAttrValue()
						.startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))
				&& !StrUtil.isNullOrEmpty(bean.getHeadFlag().getSelectedItem()
						.getValue())
				&& SffOrPtyCtants.HEADFLAG.equals(bean.getHeadFlag()
						.getSelectedItem().getValue())) {

			Party party = new Party();
			Staff staff = new Staff();

			party.setPartyName(bean.getPartyName().getValue());
			staff.setStaffProperty(bean.getStaffProperty().getAttrValue());
			party.setStaff(staff);

			bean.getGrpUnEmail().setValue(
					partyManager.generateGrpUnEmail(party, null, null));

		} else {
			bean.getGrpUnEmail().setValue(null);
		}
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param pageLocation
	 */
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
		} else if (!StrUtil.isNullOrEmpty(pageLocation)) {

			if ("politicalTab".equals(pageLocation)) {

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("agentTab".equals(pageLocation)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_AGENT_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("ibeTab".equals(pageLocation)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_IBE_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("supplierTab".equals(pageLocation)) {

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("ossTab".equals(pageLocation)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_OSS_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("edwTab".equals(pageLocation)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_EDW_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("marketingTab".equals(pageLocation)) {

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("newMarketingTab".equals(pageLocation)) {

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("newSeventeenMarketingTab".equals(pageLocation)) {

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			} else if ("costTab".equals(pageLocation)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_ADDALL_GRPUNEMAIL)) {
					canGrpUnEmail = true;
				}

				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_COST_STAFF_ORG_ADDALL_AUTOGENERATION)) {
					canAutogeneration = true;
				}

			}

		}

		this.bean.getGrpUnEmail().setDisabled(!canGrpUnEmail);
		this.bean.getAutogenerationBtn().setDisabled(!canAutogeneration);
	}
}
