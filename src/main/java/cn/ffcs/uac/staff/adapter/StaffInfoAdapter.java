package cn.ffcs.uac.staff.adapter;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uac.staff.constant.EnumUacStaffInfo;
import cn.ffcs.uac.staff.dao.UacCertDao;
import cn.ffcs.uac.staff.dao.UacContactDao;
import cn.ffcs.uac.staff.dao.UacStaffUomRelationDao;
import cn.ffcs.uac.staff.manager.UacStaffManager;
import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.model.UacStaffUomRelation;
import cn.ffcs.uac.staff.util.ModelPropertyCopyUtil;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

@Service("staffInfoAdapter")
@Scope("prototype")
@Transactional
public class StaffInfoAdapter implements UacStaffManager {
	@Resource
	private UacStaffManager uacStaffManager;
	@Resource
	private UacCertDao uacCertDao;
	@Resource
	private UacContactDao uacContactDao;
	@Resource
	private UacStaffUomRelationDao uacStaffUomRelationDao;

	@Override
	public String addUacStaffAllInfo(UacStaff uacStaff) {
		/********* add uac begin ***********/
		String info = null;

		UacCert uacCert = uacStaff.getUacCert();
		UacContact uacContact = uacStaff.getUacContact();

		// 根据规则生成必须值
		uacStaff.setUuid(StrUtil.getUUID());
		uacStaff.setEcode(generateEcode());
		// 生成账号，检查账号是否生成成功
		EnumUacStaffInfo returnEnum = generateAccount(uacStaff.getStaffName(),
				uacStaff.getProperty(), uacCert.getCertNumber());
		boolean isAccSuccess = Boolean.parseBoolean(returnEnum.name());

		if (isAccSuccess) {
			uacStaff.setAccount(returnEnum.getValue());
		} else {
			info = returnEnum.getValue();
			return info;
		}

		Group group = getGroup(uacStaff.getStaffName(), uacCert.getCertNumber());
		if (group != null) {
			uacStaff.setGuid(group.getUniCode());
			uacStaff.setHrCode(group.getCtHrUserCode());
		}

		// 新增UacCert,如果有,用已经存在的cert，没有则新增
		if (uacCertDao.queryUacCert(uacCert) != null) {
			uacCert = uacCertDao.queryUacCert(uacCert);
		} else {
			if (EnumUacStaffInfo.CERT_TYPE_IDCARD.getValue().equals(
					uacCert.getCertType())) {
				uacCert.setIsReal(EnumUacStaffInfo.REAL_NAME_YES.getValue());
			} else {
				uacCert.setIsReal(EnumUacStaffInfo.REAL_NAME_NO.getValue());
			}

			uacCert.addOnly();
		}

		uacStaff.setCertId(uacCert.getId());

		// 新增uacContact,如果有,用已经存在的uacContact，没有则新增
		if (uacContactDao.queryUacContact(uacContact) != null) {
			uacContact = uacContactDao.queryUacContact(uacContact);
		} else {
			uacContact.addOnly();
		}

		uacStaff.setContactId(uacContact.getId());

		uacStaff.addOnly();

		UacAttachedInfo uacAttachedInfo = uacStaff.getUacAttachedInfo();
		uacAttachedInfo.setStaffId(uacStaff.getId());
		uacAttachedInfo.addOnly();

		/********* add uac end ***********/

		/******** add uom begin *********/
		String batchNumber = OperateLog.gennerateBatchNumber();

		Party party = Party.newInstance();
		ModelPropertyCopyUtil.copy(party, uacStaff);
		party.setBatchNumber(batchNumber);
		party.setPartyType("1"); // 默认个人
		party.add();

		PartyRole partyRole = PartyRole.newInstance();
		partyRole.setPartyId(party.getPartyId());
		partyRole.setRoleType("1210"); // 默认电信员工类型
		partyRole.setBatchNumber(batchNumber);
		partyRole.add();

		Individual individual = Individual.newInstance();
		ModelPropertyCopyUtil.copy(individual, uacAttachedInfo);
		individual.setPartyId(party.getPartyId());
		individual.setBatchNumber(batchNumber);
		individual.add();

		PartyContactInfo partyContactInfo = PartyContactInfo.newInstance();
		ModelPropertyCopyUtil.copy(partyContactInfo, uacContact);
		partyContactInfo.setPartyId(party.getPartyId());
		partyContactInfo.setBatchNumber(batchNumber);
		partyContactInfo.add();

		PartyCertification partyCertification = PartyCertification
				.newInstance();
		ModelPropertyCopyUtil.copy(partyCertification, uacCert);
		partyCertification.setPartyId(party.getPartyId());
		partyCertification.setBatchNumber(batchNumber);
		partyCertification.setIdentityCardId(uacStaff.getType()); // 设置类型
		partyCertification.add();

		Staff staff = Staff.newInstance();
		ModelPropertyCopyUtil.copy(staff, uacStaff);
		staff.setPartyRoleId(partyRole.getPartyRoleId());
		staff.setBatchNumber(batchNumber);
		staff.add();

		StaffAccount staffAccount = StaffAccount.newInstance();
		ModelPropertyCopyUtil.copy(staffAccount, uacStaff);
		staffAccount.setStaffId(staff.getStaffId());
		staffAccount.setBatchNumber(batchNumber);
		staffAccount.add();

		/******** add uom end ***********/

		/******** add uac staff uom relation begin ********/

		UacStaffUomRelation uacStaffUomRelation = UacStaffUomRelation
				.newInstance();
		uacStaffUomRelation.setUacStaffId(uacStaff.getId());
		uacStaffUomRelation.setUacCertId(uacCert.getId());
		uacStaffUomRelation.setUacContactId(uacContact.getId());
		uacStaffUomRelation.setAttachedInfoId(uacAttachedInfo.getId());

		uacStaffUomRelation.setStaffId(staff.getId());
		uacStaffUomRelation.setStaffAccountId(staffAccount.getId());
		uacStaffUomRelation.setPartyId(party.getId());
		uacStaffUomRelation.setPartyRoleId(partyRole.getId());
		uacStaffUomRelation.setContactId(partyContactInfo.getId());
		uacStaffUomRelation.setPartyCertId(partyCertification.getId());
		uacStaffUomRelation.setIndividualId(individual.getId());

		uacStaffUomRelation.addOnly();

		/******** add uac staff uom relation end ********/

		return info;
	}

	@Override
	public EnumUacStaffInfo delUacStaffAllInfo(UacStaff uacStaff) {
		// 失效uom staff相关信息
		UacStaffUomRelation uacStaffUomRelation = UacStaffUomRelation
				.newInstance();
		uacStaffUomRelation.setUacStaffId(uacStaff.getId());
		uacStaffUomRelation = uacStaffUomRelationDao
				.queryUacStaffUomRelation(uacStaffUomRelation);
		HibernateTemplate hbTpt = uacStaffUomRelationDao.getHibernateTplt();
		String batchNumber = OperateLog.gennerateBatchNumber();
		Staff staff = hbTpt.get(Staff.class, uacStaffUomRelation.getStaffId());
		if (staff != null) {
			staff.setBatchNumber(batchNumber);
			staff.remove();
		}

		StaffAccount staffAccount = hbTpt.get(StaffAccount.class,
				uacStaffUomRelation.getStaffAccountId());
		if (staffAccount != null) {
			staffAccount.setBatchNumber(batchNumber);
			staffAccount.remove();
		}

		Party party = hbTpt.get(Party.class, uacStaffUomRelation.getPartyId());
		if (party != null) {
			party.setBatchNumber(batchNumber);
			party.remove();
		}

		PartyRole partyRole = hbTpt.get(PartyRole.class,
				uacStaffUomRelation.getPartyRoleId());
		if (partyRole != null) {
			partyRole.setBatchNumber(batchNumber);
			partyRole.remove();
		}

		PartyContactInfo partyContactInfo = hbTpt.get(PartyContactInfo.class,
				uacStaffUomRelation.getContactId());
		if (partyContactInfo != null) {
			partyContactInfo.setBatchNumber(batchNumber);
			partyContactInfo.remove();
		}

		PartyCertification partyCertification = hbTpt.get(
				PartyCertification.class, uacStaffUomRelation.getPartyCertId());
		if (partyCertification != null) {
			partyCertification.setBatchNumber(batchNumber);
			partyCertification.remove();
		}

		Individual individual = hbTpt.get(Individual.class,
				uacStaffUomRelation.getIndividualId());
		if (individual != null) {
			individual.setBatchNumber(batchNumber);
			individual.remove();
		}

		uacStaffUomRelation.removeOnly();

		return uacStaffManager.delUacStaffAllInfo(uacStaff);
	}

	@Override
	public void updateUacStaffAllInfo(UacStaff uacStaff) {
		// 更新uacStaffAllInfo
		uacStaffManager.updateUacStaffAllInfo(uacStaff);
		
		// 更新uom staff相关信息
		UacStaffUomRelation uacStaffUomRelation = UacStaffUomRelation
				.newInstance();
		uacStaffUomRelation.setUacStaffId(uacStaff.getId());
		uacStaffUomRelation = uacStaffUomRelationDao
				.queryUacStaffUomRelation(uacStaffUomRelation);
		HibernateTemplate hbTpt = uacStaffUomRelationDao.getHibernateTplt();
		String batchNumber = OperateLog.gennerateBatchNumber();
		Staff staff = hbTpt.get(Staff.class, uacStaffUomRelation.getStaffId());
		if (staff != null) {
			ModelPropertyCopyUtil.copy(staff, uacStaff);
			staff.setBatchNumber(batchNumber);
			staff.update();
		}

		StaffAccount staffAccount = hbTpt.get(StaffAccount.class,
				uacStaffUomRelation.getStaffAccountId());
		if (staffAccount != null) {
			ModelPropertyCopyUtil.copy(staffAccount, uacStaff);
			staffAccount.setBatchNumber(batchNumber);
			staffAccount.update();
		}

		Party party = hbTpt.get(Party.class, uacStaffUomRelation.getPartyId());
		if (party != null) {
			ModelPropertyCopyUtil.copy(party, uacStaff);
			party.setBatchNumber(batchNumber);
			party.update();
		}

		PartyContactInfo partyContactInfo = hbTpt.get(PartyContactInfo.class,
				uacStaffUomRelation.getContactId());
		if (partyContactInfo != null) {
			ModelPropertyCopyUtil.copy(partyContactInfo,
					uacStaff.getUacContact());
			partyContactInfo.setBatchNumber(batchNumber);
			partyContactInfo.update();
		}

		PartyCertification partyCertification = hbTpt.get(
				PartyCertification.class, uacStaffUomRelation.getPartyCertId());
		if (partyCertification != null) {
			ModelPropertyCopyUtil.copy(partyCertification,
					uacStaff.getUacCert());
			partyCertification.setBatchNumber(batchNumber);
			partyCertification.update();
		}

		Individual individual = hbTpt.get(Individual.class,
				uacStaffUomRelation.getIndividualId());
		if (individual != null) {
			ModelPropertyCopyUtil.copy(individual,
					uacStaff.getUacAttachedInfo());
			individual.setBatchNumber(batchNumber);
			individual.update();
		}
	}

	@Override
	public PageInfo queryUacStaffPage(UacStaff uacStaff, int currentPage,
			int pageSize) {
		return uacStaffManager.queryUacStaffPage(uacStaff, currentPage,
				pageSize);
	}
	@Override
	public PageInfo queryDemoStaffPage(DemoStaff demoStaff, int currentPage,
			int pageSize) {
		return uacStaffManager.queryDemoStaffPage(demoStaff, currentPage,
				pageSize);
	}

	@Override
	public String generateEcode() {
		return uacStaffManager.generateEcode();
	}

	@Override
	public String genneratePropertyAccNumber(String property) {
		return uacStaffManager.genneratePropertyAccNumber(property);
	}

	@Override
	public Group getGroup(String staffName, String certNumber) {
		return uacStaffManager.getGroup(staffName, certNumber);
	}

	@Override
	public EnumUacStaffInfo generateAccount(String staffName, String property,
			String certNumber) {
		return uacStaffManager.generateAccount(staffName, property, certNumber);
	}

	@Override
	public void saveRemindInfo(String msg) {
		uacStaffManager.saveRemindInfo(msg);
	}

}
