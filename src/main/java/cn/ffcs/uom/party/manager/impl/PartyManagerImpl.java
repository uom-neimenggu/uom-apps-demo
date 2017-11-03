package cn.ffcs.uom.party.manager.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.zkoss.zul.Messagebox;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.ChineseSpellUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.comparehr.manager.OperateHrManager;
import cn.ffcs.uom.comparehr.model.OperateHr;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.dao.PartyDao;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.CertIdVarInfo;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;

/**
 * 参与人的管理实现类 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-29
 * @功能说明：
 * 
 */
@Service("partyManager")
@Scope("prototype")
public class PartyManagerImpl implements PartyManager {

	@Resource
	private PartyDao partyDao;
	
	@Resource
	private StaffDao staffDao;
	
	@Resource
	private OperateHrManager operateHrManager;

	private GroupManager groupManager = (GroupManager) ApplicationContextUtil
			.getBean("groupManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private StaffRoleManager staffRoleManager = (StaffRoleManager) ApplicationContextUtil
			.getBean("staffRoleManager");

	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 返回新增参与人角色的主键ID {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.party.manager.PartyManager
	 * @author Wong 2013-6-3 Wong
	 * @throws Exception
	 */
	@Override
	public void addParty(Party party) throws Exception {
		// party = this.changePartyFieldFromOperateHrWhileAdding(party);
		partyDao.addParty(party);
	}

	/**
	 * 修改参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public void updateParty(Party party) {
		partyDao.updateParty(party);
	}

	@Override
	public void delParty(Long id) {
		partyDao.delParty(id);
	}

	@Override
	public void delParty(Party party) {
		partyDao.delParty(party);
	}

	@Override
	public Party queryParty(Long partyId) {
		return partyDao.queryParty(partyId);
	}

	/**
	 * 检查参与人的名字是否被使用 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public boolean checkPartyNameExits(String partyName) {
		return partyDao.checkPartyNameExits(partyName);
	}

	/**
	 * 根据参与人标识获取参与人证件实体 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	@Override
	public List<PartyCertification> getPartyCerfion(Long partyId) {
		return partyDao.getPartyCerfion(partyId);
	}
	
	@Override
	public PartyCertification getDefaultPartyCertification(Long partyId) {
		return partyDao.getDefaultPartyCertification(partyId);
	}
	
	@Override
	public CertIdVarInfo getCertIdVarInfo(String identity,String name) {
		return partyDao.getCertIdVarInfo(identity,name);
	}

	@Override
	public List<PartyContactInfo> queryDefaultPartyContactInfo(
			PartyContactInfo partyContactInfo) {
		return partyDao.queryDefaultPartyContactInfo(partyContactInfo);
	}

	/**
	 * 根据员工标识获取参与人默认证件实体
	 * 
	 * @param staffId
	 * @return
	 * @author 朱林涛
	 */
	@Override
	public List<PartyCertification> queryPartyCertificationList(Staff staff,
			PartyCertification pc) {
		return partyDao.queryPartyCertificationList(staff, pc);
	}
	
	@Override
	public PartyContactInfo getDefaultPartyContactInfo(Long partyId) {
		return partyDao.getDefaultPartyContactInfo(partyId);
	}

	/**
	 * 根据参与人标识获取参与人联系方式 .
	 * 
	 * @param id
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	@Override
	public List<PartyContactInfo> getPartyContInfo(Long partyId) {
		return partyDao.getPartyContInfo(partyId);
	}

	/**
	 * 根据证件类型和证件号码判断该证件是否被使用超过最大上限 . 没有超过最大上限则返回true
	 * 
	 * @param certType
	 *            证件类型
	 * @param certNumber
	 *            证件号码
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	@Override
	public boolean checkIsExistCertificate(String certNumber) {
		return partyDao.checkIsExistCertificate(certNumber);
	}

	@Override
	public PageInfo forQuertyParty(Party party, int currentPage, int pageSize) {
		return partyDao.forQuertyParty(party, currentPage, pageSize);
	}

	/**
	 * 根据参与人标识获取个人信息 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public Individual getIndividual(Long partyId) {
		return partyDao.getIndividual(partyId);
	}

	/**
	 * 获取参与人组织信息 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-6-3 Wong
	 */
	@Override
	public PartyOrganization getPartyOrg(Long partyId) {
		return partyDao.getPartyOrg(partyId);
	}

	/**
	 * 获取所有参与人 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	@Override
	public PageInfo getPartyContactInfos(PartyContactInfo pInfo,
			int activePage, int pageSize) {
		return partyDao.getPartyContactInfos(pInfo, activePage, pageSize);
	}

	/**
	 * 获取所有参与人证件 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	@Override
	public PageInfo getPartyCertifications(PartyCertification paCerInfo,
			int activePage, int pageSize) {
		return partyDao.getPartyCertifications(paCerInfo, activePage, pageSize);
	}

	@Override
	public void removePartyCertification(Serializable id) {
		partyDao.removePartyCertification(id);
	}

	/**
	 * 参与人证件删除 {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.party.manager.PartyManager#removePartyCertification(cn.ffcs.uom.party.model.PartyCertification)
	 * @author wangyong 2013-6-29 wangyong
	 */
	@Override
	public boolean removePartyCertification(
			PartyCertification partyCertification) {
		Long partyId = partyCertification.getPartyId();
		if (partyDao.isLastPartyCertification(partyId)) { // 证件是最后一个，不允许删除
			return true;
		}
		partyDao.removePartyCertification(partyCertification);
		
		List<PartyCertification> partyCertList = getPartyCerfion(partyId);
		if(!partyCertList.isEmpty() && partyCertList != null) {
			boolean isHaveDefault = false;
			
			for(PartyCertification pc : partyCertList) {
				if("1".equals(pc.getCertSort())) {
					isHaveDefault = true;
					break;
				}
			}
			
			if(!isHaveDefault) {
				PartyCertification partyCert = partyCertList.get(0);
				partyCert.setCertSort("1");
				update(partyCert);
			}
		}
		
		
		return false;
	}

	@Override
	public void removePartyContactInfo(Serializable id) {
		partyDao.removePartyContactInfo(id);
	}

	@Override
	public void removePartyContactInfo(PartyContactInfo partyContactInfo) {
		partyDao.removePartyContactInfo(partyContactInfo);
	}

	/**
	 * 员工组织关系 .
	 * 
	 * @param staffOrg
	 * @param activePag
	 * @param pageSize
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public PageInfo toQuertyStaffOrg(StaffOrganization staffOrg, int activePag,
			int pageSize) {
		return partyDao.toQuertyStaffOrg(staffOrg, activePag, pageSize);
	}

	/**
	 * 根据ClassName attrName获取AttrValue AttrValueName .
	 * 
	 * @param className
	 * @param attrName
	 * @return
	 * @author wangyong 2013-6-15 wangyong
	 */
	@Override
	public List<Map<String, String>> getListForAttrValue(String className,
			String attrName) {

		List<Map<String, String>> ltMps = null;
		List<Map<String, Object>> lMap = partyDao.getListForAttrValue(
				className, attrName);
		if (null != lMap) {
			Map<String, String> oms = new HashMap<String, String>();
			ltMps = new ArrayList<Map<String, String>>();
			for (Map<String, Object> m : lMap) {
				oms.put(m.get("ATTR_VALUE").toString(), m
						.get("ATTR_VALUE_NAME").toString());
			}
			ltMps.add(oms);
		}
		return ltMps;
	}

	@Override
	public void save(PartyCertification partyCerfi) {
		partyDao.save(partyCerfi);
	}

	@Override
	public void update(PartyCertification partyCerfi) {
		partyDao.update(partyCerfi);
	}

	@Override
	public void save(PartyContactInfo partyContactInfo) {
		partyDao.save(partyContactInfo);
	}

	@Override
	public void update(PartyContactInfo partyContactInfo) {
		partyDao.update(partyContactInfo);
	}

	@Override
	public void save(Individual indiv) {
		partyDao.save(indiv);
	}

	@Override
	public void update(Individual indiv) {
		partyDao.update(indiv);
	}

	@Override
	public List<PartyRole> getPartyRoleByPtId(Long staffId) {
		return partyDao.getPartyRoleByPtId(staffId);
	}

	@Override
	public void remove(UomEntity ue) {
		partyDao.remove(ue);
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void addPartyRole(PartyRole partyRole) {
		partyDao.addPartyRole(partyRole);
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void updatePartyRole(PartyRole partyRole) {
		partyDao.updatePartyRole(partyRole);
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyRole(PartyRole partyRole) {
		partyDao.removePartyRole(partyRole);
	}

	/**
	 * 
	 * .
	 * 
	 * @param partyRole
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author wangyong 2013-6-25 wangyong
	 */
	@Override
	public PageInfo getPartyRolePageInfo(Party party, int currentPage,
			int pageSize) {
		if (StrUtil.isNullOrEmpty(party.getPartyId())) {
			return null;
		}
		return partyDao.getPartyRolePageInfo(party, currentPage, pageSize);
	}

	@Override
	public void modStaffNameForPtyName(Party party) {
		partyDao.modStaffNameForPtyName(party);
	}

	@Override
	public boolean isExistsMobilePhone(PartyContactInfo partyContactInfo) {
		return partyDao.isExistsMobilePhone(partyContactInfo);
	}

	@Override
	public Party getPartyByStaff(Staff staff) {
		return partyDao.getPartyByStaff(staff);
	}
	
	@Override
	public Party getPartyByStaffAccount(String staffAccount) {
		return partyDao.getPartyByStaffAccount(staffAccount);
	}

	@Override
	public PartyRole getPartyRole(Long partyRoleId) {
		return partyDao.getPartyRole(partyRoleId);
	}

	@Override
	public PartyRole getPartyRoleByPartyId(Long partyId) {
		return partyDao.getPartyRoleByPartyId(partyId);
	}

	@Override
	public void addPartyStaffOrganizations(List<Party> partyList)
			throws Exception {
		if (null != partyList && partyList.size() > 0) {
			for (Party party : partyList) {
				if (null != party) {

					// party = this
					// .changePartyFieldFromOperateHrWhileAdding(party);

					party.setPartyType("1");
					party.add();
					Individual individual = party.getIndividual();
					PartyCertification ptyCerfic = party
							.getPartyCertification();
					PartyContactInfo ptyConInfo = party.getPartyContactInfo();
					individual.setPartyId(party.getPartyId());
					if (null != individual) {
						individual.setPartyId(party.getPartyId());
						individual.add();
					}
					if (null != ptyCerfic) {
						ptyCerfic.setPartyId(party.getPartyId());
						ptyCerfic.add();
					}
					if (null != ptyConInfo) {
						ptyConInfo.setPartyId(party.getPartyId());
						ptyConInfo.add();
					}
					PartyRole partyRole = party.getPartyRole();
					partyRole.setPartyId(party.getPartyId());
					partyRole.add();
					Staff staff = party.getStaff();
					if (null != staff) {
						staff.setPartyRoleId(partyRole.getPartyRoleId());
						staff.setUuid(StrUtil.getUUID());
						staff.add();
						StaffAccount sa = staff.getObjStaffAccount();
						if (sa != null) {
							sa.setStaffId(staff.getStaffId());
							sa.add();
						}
						StaffOrganization staffOrganization = party
								.getStaffOrganization();
						staffOrganization.setStaffId(staff.getStaffId());
						staffOrganization
								.setRalaCd(BaseUnitConstants.RALA_CD_1);// 关系类型--默认1
						staffOrganization.setStaffSeq(200L);
						staffOrganization.add();
						/**
						 * 员工添加角色
						 */
						staffRoleManager
								.updateBatchStaffRoleRela(staffOrganization);
						groupManager.updateGroupProofread(party.getGroup(),
								staff);
					}
				}
			}
		}
	}

	@Override
	public Party changePartyFieldFromOperateHrWhileAdding(Party party)
			throws Exception {
		if (null != party) {
			PartyCertification pcf = party.getPartyCertification();
			if (null != pcf) {
				OperateHr operateHr = null;
				OperateHr operateHr18 = null;
				OperateHr operateHr15 = null;
				if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
						&& null != pcf.getCertNumber()
						&& (PartyConstant.ATTR_VALUE_IDNO15 == pcf
								.getCertNumber().trim().length())) {
					operateHr = operateHrManager.queryOperateHrByCertNum(pcf
							.getCertNumber());
				}
				if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
						&& null != pcf.getCertNumber()
						&& (PartyConstant.ATTR_VALUE_IDNO18 == pcf
								.getCertNumber().trim().length())) {
					String certNum = CertUtil.getIDCard_15bit(pcf
							.getCertNumber());
					operateHr15 = operateHrManager
							.queryOperateHrByCertNum(certNum);
					operateHr18 = operateHrManager.queryOperateHrByCertNum(pcf
							.getCertNumber());
					if (null != operateHr18) {
						operateHr = operateHr18;
					} else if (null != operateHr15) {
						operateHr = operateHr15;
					}
				}
				if (null != operateHr) {
					try {
						Messagebox
								.show("侦测到存在该身份证对应的人力信息，系统已自动更新参与人的姓名、身份证、性别、生日、员工的工号、员工的账号、用户性质。");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					pcf.setCertNumber(operateHr.getLastCertNum());
					party.setPartyName(operateHr.getPsnName());
					PartyContactInfo pci = party.getPartyContactInfo();
					if (null != pci) {
						pci.setContactGender(operateHr.getSex());
					}
					Individual individual = party.getIndividual();
					if (null != individual) {
						individual.setGender(operateHr.getSex());
						individual.setBirthday(operateHr.getBirthday());
					}
					Staff staff = party.getStaff();
					if (null != staff) {
						staff.setWorkProp(operateHr.getWorkPop());
						String staffNbr = operateHr.getPsnCode();
						staff.setStaffNbr(staffNbr);
						StaffAccount sa = staff.getObjStaffAccount();
						if (null != sa) {
							String staffAccStr = "";
							if (SffOrPtyCtants.WORKPROP_N_H.equals(operateHr
									.getWorkPop())
									|| SffOrPtyCtants.WORKPROP_P_SRS
											.equals(operateHr.getWorkPop())
									|| SffOrPtyCtants.WORKPROP_P_LW
											.equals(operateHr.getWorkPop())
									|| SffOrPtyCtants.WORKPROP_P_QRS
											.equals(operateHr.getWorkPop())
									|| SffOrPtyCtants.WORKPROP_P_QLW
											.equals(operateHr.getWorkPop())) {
								if (!StrUtil.isNullOrEmpty(staffNbr)) {
									staffNbr = staffNbr.trim();
									if (staffNbr.length() < 2) {
										throw new Exception(
												"从人力中间表获取的工号长度小于2位。");
									}
									staffAccStr = staffNbr.substring(2,
											staffNbr.length());
									sa.setStaffAccount(staffAccStr);
									if (staffManager.getStaffAccountList(sa)
											.size() > 0) {
										throw new Exception(
												"身份证号为【"
														+ pcf.getCertNumber()
														+ "】的员工，从人力中间表获取的工号在主数据平台中已经存在。");
									}
								} else {
									throw new Exception("从人力中间表获取的工号为空。");
								}
							} else {
								if (!StrUtil.isNullOrEmpty(staffNbr)) {
									staffNbr = staffNbr.trim();
									if (staffNbr.length() < 3) {
										throw new Exception(
												"从人力中间表获取的工号长度小于3位。");
									}
									staffAccStr = "W9"
											+ staffNbr.substring(3,
													staffNbr.length());
									sa.setStaffAccount(staffAccStr);
									if (staffManager.getStaffAccountList(sa)
											.size() > 0) {
										throw new Exception(
												"身份证号为【"
														+ pcf.getCertNumber()
														+ "】的员工，从人力中间表获取的工号在主数据平台中已经存在。");
									}
								} else {
									throw new Exception("从人力中间表获取的工号为空。");
								}
							}
						}
					}
				}
			}
		}
		return party;
	}

	/**
	 * 侦测人力系统中的工号是否与主数据中的工号重复
	 * 
	 * @param pcf
	 * @return
	 */
	@Override
	public String checkStaffAccount(PartyCertification pcf) {
		if (null != pcf) {

			OperateHr operateHr = null;
			OperateHr operateHr18 = null;
			OperateHr operateHr15 = null;

			if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
					&& null != pcf.getCertNumber()
					&& (PartyConstant.ATTR_VALUE_IDNO15 == pcf.getCertNumber()
							.trim().length())) {
				operateHr = operateHrManager.queryOperateHrByCertNum(pcf
						.getCertNumber());
			}

			if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
					&& null != pcf.getCertNumber()
					&& (PartyConstant.ATTR_VALUE_IDNO18 == pcf.getCertNumber()
							.trim().length())) {

				String certNum = CertUtil.getIDCard_15bit(pcf.getCertNumber());

				operateHr15 = operateHrManager.queryOperateHrByCertNum(certNum);

				operateHr18 = operateHrManager.queryOperateHrByCertNum(pcf
						.getCertNumber());

				if (null != operateHr18) {
					operateHr = operateHr18;
				} else if (null != operateHr15) {
					operateHr = operateHr15;
				}

			}

			if (null != operateHr) {// 侦测到该身份证对应的人力信息

				StaffAccount sa = new StaffAccount();
				String staffNbr = operateHr.getPsnCode();
				String staffAccStr = "";

				if (SffOrPtyCtants.WORKPROP_N_H.equals(operateHr.getWorkPop())
						|| SffOrPtyCtants.WORKPROP_P_SRS.equals(operateHr
								.getWorkPop())
						|| SffOrPtyCtants.WORKPROP_P_LW.equals(operateHr
								.getWorkPop())
						|| SffOrPtyCtants.WORKPROP_P_QRS.equals(operateHr
								.getWorkPop())
						|| SffOrPtyCtants.WORKPROP_P_QLW.equals(operateHr
								.getWorkPop())) {
					if (!StrUtil.isNullOrEmpty(staffNbr)) {

						staffNbr = staffNbr.trim();

						if (staffNbr.length() < 2) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于2位;
							return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO;
						}

						staffAccStr = staffNbr.substring(2, staffNbr.length());
						sa.setStaffAccount(staffAccStr);

						if (staffManager.getStaffAccountList(sa).size() > 0) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在;
							return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM;
						}

					} else {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号为空;
						return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL;
					}

				} else {

					if (!StrUtil.isNullOrEmpty(staffNbr)) {

						staffNbr = staffNbr.trim();

						if (staffNbr.length() < 3) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于3位;

							return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE;

						}

						staffAccStr = "W9"
								+ staffNbr.substring(3, staffNbr.length());
						sa.setStaffAccount(staffAccStr);

						if (staffManager.getStaffAccountList(sa).size() > 0) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在;
							return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM;
						}

					} else {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号为空;

						return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL;

					}
				}
			}
		}
		return null;
	}

	/**
	 * 侦测人力系统中的工号是否与主数据中的工号重复
	 * 
	 * @param pcf
	 * @return
	 */
	@Override
	public String checkStaffAccountNew(PartyCertification pcf, String partyName) {

		if (null != pcf) {

			if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
					&& !StrUtil.isEmpty(pcf.getCertNumber())
					&& !StrUtil.isEmpty(partyName)) {

				Group group = new Group();
				group.setUserName(partyName);
				group.setCtIdentityNumber(pcf.getCertNumber());

				List<Group> groupList = null;

				groupList = groupManager.queryGroupList(group);

				if ((groupList == null || groupList.size() == 0)
						&& PartyConstant.ATTR_VALUE_IDNO15 == pcf
								.getCertNumber().trim().length()) {

					group.setCtIdentityNumber(IdcardValidator
							.convertIdcarBy15bit(pcf.getCertNumber()));

					groupList = groupManager.queryGroupList(group);

				}

				if (groupList != null && groupList.size() > 0) {// 侦测到该姓名加身份证对应的人力信息

					group = groupList.get(0);

					StaffAccount sa = new StaffAccount();
					String staffNbr = group.getCtHrUserCode();

					if (!StrUtil.isNullOrEmpty(staffNbr)) {

						staffNbr = staffNbr.trim();
						sa.setStaffAccount(staffNbr);

						if (staffNbr.startsWith("34")) {

							if (staffNbr.length() < 2) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于2位;
								return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO;
							}

							String staffAccStr = staffNbr.substring(2,
									staffNbr.length());

							sa.setStaffAccount(staffAccStr);

						} else if (staffNbr.startsWith("W34")
								|| staffNbr.startsWith("w34")) {

							if (staffNbr.length() < 3) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于3位;

								return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE;

							}

							String staffAccStr = "W9"
									+ staffNbr.substring(3, staffNbr.length());

							sa.setStaffAccount(staffAccStr);

						}

						if (staffManager.getStaffAccountList(sa).size() > 0) {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在;
							return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM;
						}

					} else {// 侦测到该身份证对应的人力信息,但人力中间表对应的工号为空;

						return SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL;

					}

				}
			}

		}

		return null;

	}

	@Override
	public void updateOrganizationParty(Party party) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		if (null != party) {
			party.update();
			Individual individual = party.getIndividual();
			if (individual != null) {
				individual.setBatchNumber(batchNumber);
				individual.update();
			}
			PartyOrganization po = party.getPartyOrganization();
			if (po != null) {
				if (StrUtil.isNullOrEmpty(po.getPratyOrganizationId())) {
					po.setPartyId(party.getPartyId());
					po.setBatchNumber(batchNumber);
					po.add();
				} else {
					po.setBatchNumber(batchNumber);
					po.update();
				}
			}
		}
	}

	@Override
	public List<PartyCertification> getPartyCertificationList(
			PartyCertification queryPartyCertification) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM PARTY_CERTIFICATION A WHERE A.STATUS_CD = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryPartyCertification != null) {
			if (!StrUtil.isEmpty(queryPartyCertification.getCertSort())) {
				sb.append(" AND A.CERT_SORT=?");
				params.add(queryPartyCertification.getCertSort());
			}
			if (!StrUtil.isEmpty(queryPartyCertification.getCertType())) {
				sb.append(" AND A.CERT_TYPE=?");
				params.add(queryPartyCertification.getCertType());
			}
			if (!StrUtil.isEmpty(queryPartyCertification.getCertNumber())) {
				sb.append(" AND A.CERT_NUMBER=?");
				params.add(StringEscapeUtils.escapeSql(queryPartyCertification.getCertNumber()));
			}
			if (queryPartyCertification.getPartyId() != null) {
				sb.append(" AND A.PARTY_ID=?");
				params.add(queryPartyCertification.getPartyId());
			}
		}
		return this.partyDao.jdbcFindList(sb.toString(), params,
				PartyCertification.class);
	}

	@Override
	public List<PartyContactInfo> getPartyContInfoList(
			PartyContactInfo queryContactInfo) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM PARTY_CONTACT_INFO A WHERE A.STATUS_CD = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryContactInfo != null) {
			if (!StrUtil.isEmpty(queryContactInfo.getMobilePhone())) {
				sb.append(" AND MOBILE_PHONE=?");
				params.add(StringEscapeUtils.escapeSql(queryContactInfo.getMobilePhone()));
			}
			if (queryContactInfo.getPartyId() != null) {
				sb.append(" AND A.PARTY_ID=?");
				params.add(queryContactInfo.getPartyId());
			}
		}
		return this.partyDao.jdbcFindList(sb.toString(), params,
				PartyContactInfo.class);
	}

	public List<PartyContactInfo> getPartyContactInfo(
			PartyContactInfo queryContactInfo) {
		return partyDao.getPartyContactInfo(queryContactInfo);
	}

	@Override
	public List<PartyRole> getPartyRoleListByPartyCertification(
			PartyCertification queryPartyCertification) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM PARTY_ROLE WHERE STATUS_CD = ? AND PARTY_ID IN(SELECT PARTY_ID FROM PARTY_CERTIFICATION A WHERE A.STATUS_CD = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryPartyCertification != null) {
			if (!StrUtil.isEmpty(queryPartyCertification.getCertSort())) {
				sb.append(" AND A.CERT_SORT=?");
				params.add(queryPartyCertification.getCertSort());
			}
			if (!StrUtil.isEmpty(queryPartyCertification.getCertType())) {
				sb.append(" AND A.CERT_TYPE=?");
				params.add(queryPartyCertification.getCertType());
			}
			if (!StrUtil.isEmpty(queryPartyCertification.getCertNumber())) {
				sb.append(" AND A.CERT_NUMBER=?");
				params.add(StringEscapeUtils.escapeSql(queryPartyCertification.getCertNumber()));
			}
			if (queryPartyCertification.getPartyId() != null) {
				sb.append(" AND A.PARTY_ID=?");
				params.add(queryPartyCertification.getPartyId());
			}
		}
		sb.append(")");
		return this.partyDao.jdbcFindList(sb.toString(), params,
				PartyRole.class);
	}

	@Override
	public PartyRole getPartyRoleByPartyCertification(
			PartyCertification queryPartyCertification) {
		List<PartyRole> list = this
				.getPartyRoleListByPartyCertification(queryPartyCertification);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 自定义身份证验证
	 * 
	 * 如： 350524199006252 （15位） 350524199006252-1（15位加自定义备注）
	 * 35052419900625259X（18位） 350524199006252592（18位）
	 * 350524199006252592-1（18位加自定义备注）
	 */
	@Override
	public boolean checkCertNumber(String certNumber) {
		String certNumberTemp = "";
		certNumber = certNumber.replace("－", "-").replace("x", "X");
		boolean result = true;
		if (certNumber.length() > 20) {// 身份证长度小等于20
			result = false;
		}

		if (result && certNumber.lastIndexOf("-") != -1) {// 带减号形式验证
			if (result
					&& (certNumber.length() - certNumber.lastIndexOf("-")) != 2)
				result = false;
			if (result)
				certNumberTemp = certNumber.substring(0,
						certNumber.lastIndexOf("-"));// 截取身份证
		} else {// 不带减号形式
			certNumberTemp = certNumber;
		}

		if (result
				&& !certNumberTemp.matches("([0-9]{17}([0-9]|X))|([0-9]{15})")) {// 验证身份证
																					// 18或15位
			result = false;
		}
		return result;
	}

	/**
	 * 验证参与人身份证是否存在并且参与人同名
	 * 
	 * @param certNumber
	 * @param partyNme
	 * @return
	 */
	@Override
	public boolean checkCertNumberAndParytNameIsExist(String certNumber,
			String partyNme) {
		String sql = "select nvl(max(p.party_id),'0') from party p inner join party_certification pc on(p.party_id = pc.party_id) where p.status_cd = 1000 and pc.status_cd = 1000 and p.party_name = '"
				+ partyNme
				+ "' and pc.CERT_TYPE = '"
				+ PartyConstant.ATTR_VALUE_IDNO
				+ "' and pc.cert_number = '"
				+ StringEscapeUtils.escapeSql(certNumber) + "'";
		if (jdbcTemplate.queryForLong(sql) > new Long(0)) {
			return true;
		} else {
			return false;
		}
	}

	// 用工性质为内部-合同制或内部-派遣制,自动生成集团统一邮箱
	@Override
	public String generateGrpUnEmail(Party party,
			List<Map<String, String>> mapList, Long tailNumber) {

		String grpUnEmail = null;

		if (!StrUtil.isEmpty(party.getStaff().getStaffProperty())
				&& (party.getStaff().getStaffProperty()
						.startsWith(SffOrPtyCtants.WORKPROP_N_H_PRE) || party
						.getStaff().getStaffProperty()
						.startsWith(SffOrPtyCtants.WORKPROP_N_P_PRE))) {

			boolean isExist = false;

			if (!StrUtil.isNullOrEmpty(party.getPartyName())) {

				if (StrUtil.isNullOrEmpty(tailNumber)) {
					grpUnEmail = ChineseSpellUtil.converterToSpell(party
							.getPartyName()) + GroupMailConstant.GRP_UN_EMAIL;
					tailNumber = new Long(1L);
				} else if (tailNumber < 10L) {
					grpUnEmail = ChineseSpellUtil.converterToSpell(party
							.getPartyName())
							+ "0"
							+ tailNumber
							+ GroupMailConstant.GRP_UN_EMAIL;
					tailNumber += 1L;
				} else {
					grpUnEmail = ChineseSpellUtil.converterToSpell(party
							.getPartyName())
							+ tailNumber
							+ GroupMailConstant.GRP_UN_EMAIL;
					tailNumber += 1L;
				}

				List<PartyContactInfo> partyContactInfoList = null;

				PartyContactInfo partyContactInfo = new PartyContactInfo();
				partyContactInfo.setHeadFlag(SffOrPtyCtants.HEADFLAG);
				partyContactInfo.setGrpUnEmail(grpUnEmail);
				partyContactInfoList = this
						.queryDefaultPartyContactInfo(partyContactInfo);

				if (partyContactInfoList != null
						&& partyContactInfoList.size() > 0) {

					if (party.getPartyId() != null) {

						for (PartyContactInfo pci : partyContactInfoList) {
							if (!pci.getPartyId().equals(party.getPartyId())) {
								isExist = true;
								break;
							}
						}

						if (isExist) {
							isExist = false;
							grpUnEmail = this.generateGrpUnEmail(party,
									mapList, tailNumber);
						} else {

							for (Map<String, String> map : mapList) {
								if (map != null
										&& !StrUtil.isNullOrEmpty(map
												.get(grpUnEmail))
										&& map.get(grpUnEmail).equals(
												grpUnEmail)) {
									isExist = true;
									break;
								}
							}

							if (isExist) {
								isExist = false;
								grpUnEmail = this.generateGrpUnEmail(party,
										mapList, tailNumber);
							}
						}

					} else {
						grpUnEmail = this.generateGrpUnEmail(party, mapList,
								tailNumber);
					}

				} else if (!StrUtil.isNullOrEmpty(mapList)
						&& mapList.size() > 0) {

					for (Map<String, String> map : mapList) {
						if (map != null
								&& !StrUtil.isNullOrEmpty(map.get(grpUnEmail))
								&& map.get(grpUnEmail).equals(grpUnEmail)) {
							isExist = true;
							break;
						}
					}

					if (isExist) {
						isExist = false;
						grpUnEmail = this.generateGrpUnEmail(party, mapList,
								tailNumber);
					}

				}

				boolean groupMailStaffQueryInterFaceSwitch = UomClassProvider
						.isOpenSwitch("groupMailStaffQueryInterFaceSwitch");// 集团统一邮箱员工账号查询开关

				if (groupMailStaffQueryInterFaceSwitch) {

					Party newParty = new Party();
					BeanUtils.copyProperties(newParty, party);
					PartyContactInfo pc = new PartyContactInfo();
					pc.setGrpUnEmail(grpUnEmail);
					newParty.setPartyContactInfo(pc);

					String msg = groupMailManager.groupMailPackageInfo(
							GroupMailConstant.GROUP_MAIL_BIZ_ID_2, newParty,
							null);

					if (!StrUtil.isEmpty(msg)) {
						if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE
								.equals(msg)) {
							grpUnEmail = this.generateGrpUnEmail(party,
									mapList, tailNumber);
						}
					}
				}
			}
		}

		return grpUnEmail;
	}
	
	@Override
    public void saveOrEditOrDelPartyCertification(List<PartyCertification> addPartyCertificationList,
    		List<PartyCertification> editPartyCertificationList, List<PartyCertification> delPartyCertificationList) {
		//新增
        for(PartyCertification addPartyCertification : addPartyCertificationList)
        {
        	save(addPartyCertification);
        }
		
		//修改
        for(PartyCertification editPartyCertification : editPartyCertificationList)
        {
        	update(editPartyCertification);
        }
        
        //删除
        for(PartyCertification delPartyCertification : delPartyCertificationList)
        {
        	removePartyCertification(delPartyCertification);
        }
    }

	@Override
	public void delPartyStaffList(List<Party> delPartyList,
			List<Staff> delStaffList) {
		for(Party delParty : delPartyList) {
			delParty(delParty);
		}
		
		for(Staff delStaff : delStaffList) {
			staffDao.delStaff(delStaff);
		}
	}

	@Override
	public void updatePartyStaffOrganizations(List<Party> partyList) throws Exception {
		// TODO Auto-generated method stub
		if (null != partyList && partyList.size() > 0) {
			for (Party party : partyList) {
				if (null != party) {
					Individual individual = party.getIndividual();
					PartyContactInfo ptyConInfo = party.getPartyContactInfo();
					Staff staff = party.getStaff();
					if (null != staff) {
						staff.update();
						party.update();
						if (individual != null) {
							individual.update();
						}
						ptyConInfo.update();
					}
				}
			}
		}		
	}
}
