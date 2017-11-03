package cn.ffcs.uom.staff.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.Md5Util;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.comparehr.manager.OperateHrManager;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.manager.PartyCertificationRuleManager;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.staff.action.bean.StaffEditBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staffrole.manager.RoleRuleManager;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.RoleRule;
import cn.ffcs.uom.staffrole.model.StaffRole;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private StaffEditBean bean = new StaffEditBean();

	private String opType;

	@Resource
	private Md5Util md5Util;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");
	private StaffRoleManager staffRoleManager = (StaffRoleManager) ApplicationContextUtil
			.getBean("staffRoleManager");

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	private OperateHrManager operateHrManager = (OperateHrManager) ApplicationContextUtil
			.getBean("operateHrManager");

	private GroupManager groupManager = (GroupManager) ApplicationContextUtil
			.getBean("groupManager");

	@Resource(name = "partyCertificationRuleManager")
	private PartyCertificationRuleManager partyCertificationRuleManager;
	@Autowired
	private RoleRuleManager roleRuleManager;
	
	/**
     * 日志服务队列
     */
    private LogService logService = (LogService) ApplicationContextUtil.getBean("logService");

	Staff staff;

	/**
	 * 修改的参与人信息
	 */
	private Party party;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		bean.getPartyBandboxExt().addForward("onBandChang", comp,
				"onBandChangChanging");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffEditComposer() throws Exception {
		bindEvent();
		bindCombobox();
		bindBean();
	}

	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> liTp = UomClassProvider.getValuesList("Staff", "parttime");
		ListboxUtils.rendererForEdit(bean.getPartTime(), liTp);
		liTp = UomClassProvider.getValuesList("Staff", "staffPosition");
		ListboxUtils.rendererForEdit(bean.getStaffPosition(), liTp);

		List<NodeVo> staffTypeList = UomClassProvider.getValuesList("Staff",
				"staffType");
		// ListboxUtils.rendererForEdit(this.bean.getStaffType(),
		// staffTypeList);
		// this.bean.getStaffType().setSelectedIndex(1);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if (SffOrPtyCtants.ADD.equals(opType)) {
			bean.getStaffEditComposer().setTitle("员工新增");
		} else if (SffOrPtyCtants.MOD.equals(opType)
				|| SffOrPtyCtants.VIEW.equals(opType)) {
			staff = (Staff) arg.get("staff");
			if (SffOrPtyCtants.VIEW.equals(opType)) {
				bean.getStaffEditComposer().setTitle("员工查看");
				bean.getBtnToolBar().setVisible(false);
				bean.getViewBtnTB().setVisible(true);
			} else {
				bean.getStaffEditComposer().setTitle("员工修改");
				List<StaffRole> staffRoles = staffRoleManager
						.queryStaffaRoles(staff);
				bean.getStaffRoleBandboxExt().setInitialValue(staffRoles);
			}

			PoliticalLocation pl = PoliticalLocation.getPoliticalLocation(staff
					.getLocationId());
			if (null != pl) {
				staff.setLocationName(pl.getLocationName());
			}
			bean.getPartyBandboxExt().setValue(
					staffManager.getPartyNameByStaffId(staff.getStaffId()));
			List<StaffExtendAttr> staffExtendAttrList = staffManager
					.getStaffExtendAttr(staff.getStaffId());
			if (null != staffExtendAttrList && staffExtendAttrList.size() > 0) {
				bean.getStaffExtendAttrExt()
						.setExtendValue(staffExtendAttrList);
			}
			List<String> workPropList = new ArrayList<String>();
			workPropList.add(staff.getWorkProp());
			bean.getWorkProp().setInitialValue(workPropList);
			
			List<String> staffPropertyList = new ArrayList<String>();
			staffPropertyList.add(staff.getStaffProperty());
			bean.getStaffProperty().setInitialValue(staffPropertyList);
			
			ListboxUtils.selectByCodeValue(bean.getStaffPosition(),
					staff.getStaffPosition());
			PubUtil.fillBeanFromPo(staff, bean);
			if (staff.getWorkProp().equals(SffOrPtyCtants.WORKPROP_W_AGENT)) {
				// partyCertificationRuleManager.reloadStaffTypeListboxItems(bean.getStaffType(),
				// staff.getWorkProp());
			}
		}
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-25 Wong
	 */
	private void bindEvent() throws Exception {
		StaffEditComposer.this.bean.getStaffEditComposer().addEventListener(
				"onStaffChange", new EventListener() {
					@SuppressWarnings("unchecked")
					public void onEvent(final Event event) throws Exception {
						if (!StrUtil.isNullOrEmpty(event.getData())) {
							StaffEditComposer.this.arg = (HashMap) event
									.getData();
							bindBean();
						}
					}
				});

		StaffEditComposer.this.bean.getWorkProp().getOkButton()
				.addEventListener("onClick", new EventListener() {
					public void onEvent(Event arg0) throws Exception {
						// partyCertificationRuleManager.reloadStaffTypeListboxItems(StaffEditComposer.this.bean.getStaffType(),
						// StaffEditComposer.this.bean.getWorkProp().getAttrValue());
					}
				});
	}

	/**
	 * 保存.
	 * 
	 * @throws Exception
	 */
	public void onOk() throws Exception {
		/*
		 * if (null == bean.getLocationId().getPoliticalLocation()) {
		 * Messagebox.show("未选择行政区域"); return; }
		 */
		String msg = checkStaffData(); // 检验员工信息格式内容是否完整
		if (msg != null) {
			Messagebox.show(msg);
			return;
		}
		String passWord = bean.getStaffPassword().getValue();
		/**
		 * 获取MD5加密后的密码，统一认证接入，系统上线前把生产数据库员工密码字段置为空，程序新增员工的时候，密码设置为空字符串。
		 */
		// passWord = md5Util.getMD5(passWord);
		passWord = "";
		List<StaffRole> staffRoles = bean.getStaffRoleBandboxExt()
				.getStaffRoles();
		String msg2 = checkStaffRoles(staffRoles);
		if (msg2 != null) {
			Messagebox.show(msg2);
			return;
		}
		if (SffOrPtyCtants.ADD.equals(opType)) {
		    /**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
		    SysLog log = new SysLog();
		    log.startLog(new Date(), SysLogConstrants.STAFF);
		    
			Party par = bean.getPartyBandboxExt().getParty();
			if (null == par || null == par.getPartyRoleId()) {
				Messagebox.show("请选择相应的参与人信息");
				return;
			}
			Staff tempStaff = staffManager.getStaffByPartyRoleId(Long
					.parseLong(par.getPartyRoleId()));
			if (!StrUtil.isNullOrEmpty(tempStaff)) {
				StringBuffer sbMsg = new StringBuffer("每个参与人只能对应一个员工,已存在员工工号为：");
				sbMsg.append(tempStaff.getStaffNbr());
				/**
				 * 提示员工归属组织
				 */
				StaffOrganization staffOrganization = tempStaff
						.getStaffOrganization();
				if (staffOrganization != null) {
					Organization org = staffOrganization.getOrganization();
					if (org != null) {
						sbMsg.append(",该员工归属组织为：" + org.getOrgName() + ",组织编码："
								+ org.getOrgCode());
					}
				}
				Messagebox.show(sbMsg.toString());
				return;
			}

			staff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, staff);
			
			staff.setPartyRoleId(Long.parseLong(par.getPartyRoleId()));
			staff.setUuid(StrUtil.getUUID());

			List<StaffExtendAttr> listValu = this.getStaffExtendAttrList(staff);
			if (null != listValu && listValu.size() > 0) {
				staff.setStaffExtendAttr(listValu);
			}

			staff.setWorkProp(bean.getWorkProp().getAttrValue());
			staff.setStaffProperty(bean.getStaffProperty().getAttrValue());
			String staffCd = staffManager.gennerateStaffCode();
			if (null == staffCd) {
				Messagebox.show("员工编码生成异常");
				return;
			}
			staff.setStaffCode(staffCd);
			StaffAccount sfAcc = new StaffAccount();
			sfAcc.setStaffPassword(passWord);
			// 获取参与人默认证件信息
			PartyCertification pc = partyManager
					.getDefaultPartyCertification(par.getPartyId());
			PartyCertification queryPartyCertification = new PartyCertification();
			queryPartyCertification.setCertNumber(pc.getCertNumber());
			List pcList = partyManager.getPartyCertificationList(queryPartyCertification);
			Group group = new Group();

			if (!StrUtil.isNullOrEmpty(pc) && pcList.size() == 1) {

				group.setUserName(staff.getStaffName());

				group.setCtIdentityNumber(pc.getCertNumber());

				List<Group> groupList = null;

				groupList = groupManager.queryGroupList(group);

				if ((groupList == null || groupList.size() == 0)
						&& pc.getCertNumber().length() == 15) {

					group.setCtIdentityNumber(IdcardValidator
							.convertIdcarBy15bit(pc.getCertNumber()));

					groupList = groupManager.queryGroupList(group);

				}

				if (groupList != null && groupList.size() > 0) {

					group = groupList.get(0);

					Messagebox
							.show("侦测到存在该参与人的身份证对应的人力信息，系统已自动更新员工工号、员工账号和员工GUID。");

					String staffNbr = group.getCtHrUserCode();

					if (!StrUtil.isNullOrEmpty(staffNbr)) {

						staffNbr = staffNbr.trim();
						staff.setStaffNbr(staffNbr);
						sfAcc.setStaffAccount(staffNbr);

						if (staffNbr.startsWith("34")) {

							if (staffNbr.length() < 2) {
								Messagebox.show("从人力中间表获取的工号长度小于2位。");
								return;
							}
							String staffAccStr = staffNbr.substring(2,
									staffNbr.length());

							sfAcc.setStaffAccount(staffAccStr);

						} else if (staffNbr.startsWith("W34")
								|| staffNbr.startsWith("w34")) {

							if (staffNbr.length() < 3) {
								Messagebox.show("从人力中间表获取的工号长度小于3位。");
								return;
							}

							String staffAccStr = "W9"
									+ staffNbr.substring(3, staffNbr.length());

							sfAcc.setStaffAccount(staffAccStr);
						}

						if (staffManager.getStaffAccountList(sfAcc).size() > 0) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在;
							Messagebox
									.show(SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM_STR);
							return;
						}

					} else {
						Messagebox.show("从人力中间表获取的工号为空。");
						return;
					}

					par.setPartyName(group.getUserName());
					par.update();
					PartyContactInfo pci = partyManager
							.getDefaultPartyContactInfo(par.getPartyId());
					// if (null != pci) {
					// pci.setContactGender(operateHr.getSex());
					// pci.update();
					// }
					// Individual individual = partyManager.getIndividual(par
					// .getPartyId());
					// if (null != individual) {
					// individual.setGender(operateHr.getSex());
					// individual.setBirthday(operateHr.getBirthday());
					// individual.update();
					// }

				} else {
					staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
							.getWorkProp()));
					sfAcc.setStaffAccount(staffManager
							.gennerateStaffAccount(staff)); // 员工账号
					if (staffManager.getStaffAccountList(sfAcc).size() > 0) {
						Messagebox.show("账号" + sfAcc.getStaffAccount()
								+ "在主数据中已经存在");
						return;
					}
				}

				// //////////////////下面是老的人力工号较正///////////////

				// OperateHr operateHr = operateHrManager
				// .queryOperateHrByCertNum(pc.getCertNumber());

				// if (!StrUtil.isNullOrEmpty(operateHr)) {
				// try {
				// Messagebox
				// .show("侦测到存在该参与人的身份证对应的人力信息，系统已自动更新参与人的姓名、身份证、性别、生日、员工的工号、员工的账号、用户性质。");
				// staff.setWorkProp(operateHr.getWorkPop());
				// String staffNbr = operateHr.getPsnCode();
				// staff.setStaffNbr(staffNbr);
				// if (SffOrPtyCtants.WORKPROP_N_H.equals(operateHr
				// .getWorkPop())
				// || SffOrPtyCtants.WORKPROP_P_SRS
				// .equals(operateHr.getWorkPop())
				// || SffOrPtyCtants.WORKPROP_P_LW
				// .equals(operateHr.getWorkPop())
				// || SffOrPtyCtants.WORKPROP_P_QRS
				// .equals(operateHr.getWorkPop())
				// || SffOrPtyCtants.WORKPROP_P_QLW
				// .equals(operateHr.getWorkPop())) {
				// if (!StrUtil.isNullOrEmpty(staffNbr)) {
				// staffNbr = staffNbr.trim();
				// if (staffNbr.length() < 2) {
				// Messagebox.show("从人力中间表获取的工号长度小于2位。");
				// return;
				// }
				// String staffAccStr = staffNbr.substring(2,
				// staffNbr.length());
				// sfAcc.setStaffAccount(staffAccStr);
				// } else {
				// Messagebox.show("从人力中间表获取的工号为空。");
				// return;
				// }
				// } else if (SffOrPtyCtants.WORKPROP_N_W.equals(operateHr
				// .getWorkPop())) {
				// if (!StrUtil.isNullOrEmpty(staffNbr)) {
				// staffNbr = staffNbr.trim();
				// if (staffNbr.length() < 3) {
				// Messagebox.show("从人力中间表获取的工号长度小于3位。");
				// return;
				// }
				// String staffAccStr = "W9"
				// + staffNbr.substring(3,
				// staffNbr.length());
				// sfAcc.setStaffAccount(staffAccStr);
				// } else {
				// Messagebox.show("从人力中间表获取的工号为空。");
				// return;
				// }
				// }
				// par.setPartyName(operateHr.getPsnName());
				// par.update();
				// PartyContactInfo pci = partyManager
				// .getDefaultPartyContactInfo(par.getPartyId());
				// if (null != pci) {
				// pci.setContactGender(operateHr.getSex());
				// pci.update();
				// }
				// Individual individual = partyManager.getIndividual(par
				// .getPartyId());
				// if (null != individual) {
				// individual.setGender(operateHr.getSex());
				// individual.setBirthday(operateHr.getBirthday());
				// individual.update();
				// }
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// } else {
				// staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
				// .getWorkProp()));
				// sfAcc.setStaffAccount(staffManager
				// .gennerateStaffAccount(staff)); // 员工账号
				// }

				// if(pc.getCertType().equals(PartyConstant.ATTR_VALUE_IDNO)){
				// String certNumber = pc.getCertNumber();
				// if(staff!=null&&staff.getStaffType()!=null){
				// if(partyManager.checkCertNumber(pc.getCertNumber())){//正常身份证号
				// certNumber =
				// partyCertificationRuleManager.genCertNumber(Integer.valueOf(bean.getStaffType().getSelectedItem().getValue().toString()),pc.getCertNumber());
				// }else{//TMPXXXXXXXXXXXX
				// certNumber =
				// partyCertificationRuleManager.reGenCertNumber(Integer.valueOf(bean.getStaffType().getSelectedItem().getValue().toString()),
				// pc.getCertNumber());
				// }
				// }else{//默认TMP
				// certNumber =
				// partyCertificationRuleManager.genCertNumber(certNumber);
				// }
				// if(!StrUtil.isNullOrEmpty(certNumber)){
				// pc.setCertNumber(certNumber);
				// partyManager.update(pc);
				// }
				// }
			} else {
				staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
						.getWorkProp()));
				sfAcc.setStaffAccount(staffManager.gennerateStaffAccount(staff)); // 员工账号
			}
			staff.setObjStaffAccount(sfAcc);
			//保存员工
			staffManager.addStaff(staff);
			/**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
			Class clazz[] = {Staff.class};
			log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "员工添加记录日志");
			
			List<Staff> staffList = new ArrayList<Staff>();
			staffList.add(staff);
			if (staffRoles != null && staffRoles.size() > 0) {
				staffRoleManager.saveStaffRoleRela(staffRoles, staffList);
			}
			groupManager.updateGroupProofread(group, staff);

		} else if (SffOrPtyCtants.MOD.equals(opType)) {
		    /**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
		    SysLog log = new SysLog();
		    log.startLog(new Date(), SysLogConstrants.STAFF);
		    
			List<StaffRole> ownStaffRoleList = staffRoleManager
					.queryStaffaRoles(staff);

			String workProp = staff.getWorkProp();
			String staffProperty = staff.getStaffProperty();
			Long partyRoleId = staff.getPartyRoleId();
			PartyRole partyRole = partyManager.getPartyRole(partyRoleId);
			if (null != partyRole) {
				party = partyManager.queryParty(partyRole.getPartyId());
				String partyName = bean.getPartyBandboxExt().getValue();
				if (!StrUtil.isEmpty(partyName)) {
					party.setPartyName(partyName);
				}
			}
			// 参与人证件号码
			/*PartyCertification pc = partyManager
					.getDefaultPartyCertification(party.getPartyId());
			if (pc.getCertType().equals(PartyConstant.ATTR_VALUE_IDNO)) {
				String certNumber = pc.getCertNumber();*/
				/*
				 * String workPropValue = bean.getWorkProp().getAttrValue();
				 * if(StrUtil.isNullOrEmpty(workPropValue)){ workPropValue =
				 * workProp;//用户未选择用工性质的修改 ，默认库中的用工性质 }
				 */

				// if(staff!=null&&staff.getStaffType()!=null){
				// if(partyManager.checkCertNumber(pc.getCertNumber())){//正常身份证号
				// certNumber =
				// partyCertificationRuleManager.genCertNumber(Integer.valueOf(bean.getStaffType().getSelectedItem().getValue().toString()),pc.getCertNumber());
				// }else{//TMPXXXXXXXXXXXX
				// certNumber =
				// partyCertificationRuleManager.reGenCertNumber(Integer.valueOf(bean.getStaffType().getSelectedItem().getValue().toString()),
				// pc.getCertNumber());
				// }
				// }else{//默认TMP
				// certNumber =
				// partyCertificationRuleManager.genCertNumber(certNumber);
				// }

				/*
				 * if(!workPropValue.equals(SffOrPtyCtants.WORKPROP_W_AGENT)){
				 * if(partyManager.checkCertNumber(pc.getCertNumber())){//正常身份证号
				 * certNumber =
				 * partyCertificationRuleManager.genCertNumber(Integer
				 * .valueOf(bean
				 * .getStaffType().getSelectedItem().getValue().toString
				 * ()),pc.getCertNumber()); }else{//TMPXXXXXXXXXXXX certNumber =
				 * partyCertificationRuleManager
				 * .reGenCertNumber(Integer.valueOf(
				 * bean.getStaffType().getSelectedItem().getValue().toString()),
				 * pc.getCertNumber()); } }else{//代理商
				 * if(partyManager.checkCertNumber(pc.getCertNumber())){//正常身份证号
				 * certNumber =
				 * partyCertificationRuleManager.genCertNumber(6,pc.
				 * getCertNumber()); }else{//TMPXXXXXXXXXXXX certNumber =
				 * partyCertificationRuleManager.reGenCertNumber(6,
				 * pc.getCertNumber()); } }
				 */

			/*	if (!StrUtil.isNullOrEmpty(certNumber)) {
					pc.setCertNumber(certNumber);
					partyManager.update(pc);
				}
			}*/

			PubUtil.fillPoFromBean(bean, staff);
			
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
			if (StrUtil.isNullOrEmpty(staff.getStaffNbr())) {
				staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
						.getWorkProp()));
			}
			String strSffAcc = bean.getStaffAccount().getValue().trim();
			if (StrUtil.isNullOrEmpty(strSffAcc)) {
				strSffAcc = staffManager.gennerateStaffAccount(staff);
			}
			StaffAccount sffAcc = staffManager.getStaffAccount(null,
					staff.getStaffId());
			if (null == sffAcc) {
				sffAcc = new StaffAccount();
				sffAcc.setStaffAccount(strSffAcc);
			} else {
				String oldStaffAccountStr = sffAcc.getStaffAccount();
				String newStaffAccountStr = strSffAcc;
				if (!newStaffAccountStr.equals(oldStaffAccountStr)) {
					StaffAccount sa = staffManager
							.getStaffAccountByStaffAccount(newStaffAccountStr);
					if (null != sa) {
						Messagebox.show("员工账号有重复");
						return;
					}
				}
			}
			sffAcc.setStaffId(staff.getStaffId());
			// sffAcc.setStaffPassword(passWord);
			staff.setObjStaffAccount(sffAcc);
			if (StrUtil.isEmpty(staff.getStaffCode())) {
				Messagebox.show("员工编码不能为空！");
				return;
			}

			if (StrUtil.isEmpty(staff.getStaffNbr())) {
				Messagebox.show("员工工号不能为空！");
				return;
			}

			if (StrUtil.isEmpty(staff.getStaffName())) {
				Messagebox.show("员工姓名不能为空！");
				return;
			}
			List<Staff> staffList = new ArrayList<Staff>();
			staffList.add(staff);
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

			if (addStaffRoleList != null && addStaffRoleList.size() > 0) {
				staffRoleManager.saveStaffRoleRela(addStaffRoleList, staffList);
			}
			if (delStaffRoleList != null && delStaffRoleList.size() > 0) {
				staffRoleManager.removeStaffRoleRela(delStaffRoleList, staff);
			}
			staffManager.updateStaff(staff);
			partyManager.updateParty(party);
			/**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
			Class clazz[] = {Staff.class};
			log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "员工修改记录日志");

		}
		// 绑定员工角色
		// List<StaffRole> staffRoles =
		// bean.getStaffRoleBandboxExt().getStaffRoles();
		// Messagebox.show(staffRoles.get(0).getRoleName());
		// Events.postEvent("onOK", bean.getStaffEditComposer(), staff);
		bean.getStaffEditComposer().onClose();
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
	 * 
	 * .
	 * 
	 * @author Wong 2013-6-4 Wong
	 */
	public void onBandChangChanging() {
		Party p = bean.getPartyBandboxExt().getParty();
		Individual indiv = partyManager.getIndividual(p.getPartyId());
		String vlu = p.getPartyName();
		if (null != indiv) {
			String strSeNeCo = indiv.getSameNameCode();
			if (!StrUtil.isNullOrEmpty(strSeNeCo)) {
				vlu += indiv.getSameNameCode();
			}
		}
		bean.getStaffName().setValue(vlu);
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getStaffEditComposer().onClose();
	}

	/**
	 * .
	 * 
	 * @return
	 * @author Wong 2013-5-25 Wong
	 */
	private String checkStaffData() {
		if (StrUtil.isNullOrEmpty(bean.getStaffName().getValue())) {
			return "请填写员工名称";
		}
		if (StrUtil.checkBlank(bean.getStaffName().getValue())) {
			return "员工名称中有空格";
		}
		/*
		 * if (StrUtil.isNullOrEmpty(bean.getLocationId().getValue())) { return
		 * "请填写行政区域标识"; }
		 */
		if (StrUtil.isNullOrEmpty(bean.getStaffPosition().getSelectedItem()
				.getValue())) {
			return "请选择员工职位";
		}
		if (StrUtil.isNullOrEmpty(bean.getWorkProp().getAttrValue())) {
			return "请选择员工性质";
		}
		if (StrUtil.isNullOrEmpty(bean.getStaffProperty().getAttrValue())) {
			return "请选择人员属性";
		}
		if (StrUtil.isNullOrEmpty(bean.getReason().getValue())) {
			return "请填写变更原因";
		}
		// List<StaffRole> staffRoles =
		// bean.getStaffRoleBandboxExt().getStaffRoles();
		return null;
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
}
