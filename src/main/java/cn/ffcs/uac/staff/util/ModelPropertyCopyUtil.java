package cn.ffcs.uac.staff.util;

import cn.ffcs.uac.staff.model.UacAttachedInfo;
import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

public class ModelPropertyCopyUtil {
	/**
	 * 将UacStaff origin的对应值复制给Staff destination
	 * 
	 * @param destination
	 * @param origin
	 */
	public static void copy(Staff destination, UacStaff origin) {
		destination.setStaffProperty(origin.getProperty());
		destination.setStaffNbr(origin.getAccount());
		destination.setStaffCode(origin.getEcode());
		destination.setStaffName(origin.getStaffName());
		destination.setUuid(origin.getUuid());
		destination.setReason(origin.getTransactionRecord());
		destination.setStaffDesc(origin.getEmpDesc());
	}

	/**
	 * 将UacStaff origin的对应值复制给StaffAccount destination
	 * 
	 * @param destination
	 * @param origin
	 */
	public static void copy(StaffAccount destination, UacStaff origin) {
		destination.setStaffAccount(origin.getAccount());
		destination.setGuid(origin.getGuid());
	}
	
	public static void copy(Party destination, UacStaff origin) {
		destination.setPartyName(origin.getStaffName());
	}
	
	/**
	 * 将UacCert origin的对应值复制给PartyCertification destination
	 * @param destination
	 * @param origin
	 */
	public static void copy(PartyCertification destination, UacCert origin) {
		destination.setCertNumber(origin.getCertNumber());
		destination.setIsRealName(origin.getIsReal());
		destination.setCertType(origin.getCertType());
		destination.setCertName(origin.getCertName());
		destination.setCertOrg(origin.getCertName());
		destination.setCertAddress(origin.getCertAddr());
	}
	
	/**
	 * 将UacContact origin的对应值复制给PartyContactInfo destination
	 * @param destination
	 * @param origin
	 */
	public static void copy(PartyContactInfo destination, UacContact origin) {
		destination.setMobilePhone(origin.getMobilePhone());
		destination.setMobilePhoneSpare(origin.getMobilePhone());
		destination.setOfficePhone(origin.getTelephone());
		destination.setInnerEmail(origin.getInnerEmail());
		destination.setEmail(origin.getEmail());
		destination.setGrpUnEmail(origin.getUnifiedEmail());
		destination.setContactAddress(origin.getAddress());
		destination.setFax(origin.getFax());
		destination.setQqNumber(origin.getQq());
	}
	
	/**
	 * 将UacAttachedInfo origin的对应值复制给Individual destination
	 * @param destination
	 * @param origin
	 */
	public static void copy(Individual destination, UacAttachedInfo origin) {
		destination.setMarriageStatus(origin.getMaritalStatus());
		destination.setGender(origin.getGender());
		destination.setNation(origin.getNation());
		destination.setReligion(origin.getReligion());
		destination.setEducationLevel(origin.getEducationLevel());
	}
}
