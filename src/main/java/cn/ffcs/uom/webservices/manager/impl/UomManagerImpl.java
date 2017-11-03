package cn.ffcs.uom.webservices.manager.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.exception.RtManagerException;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.SystemUpdateStaffScopeManager;
import cn.ffcs.uom.webservices.manager.UomManager;
import cn.ffcs.uom.webservices.model.SystemUpdateStaffScope;
import cn.ffcs.uom.webservices.util.CastorParser;
import cn.ffcs.uom.webservices.util.RootBuilder;
import cn.ffcs.uom.webservices.util.WsUtil;

@Service("uomManager")
@Scope("prototype")
public class UomManagerImpl implements UomManager {

	@Resource(name = "staffManager")
	private StaffManager staffManager;

	@Resource(name = "partyManager")
	private PartyManager partyManager;

	@Resource(name = "systemUpdateStaffScopeManager")
	private SystemUpdateStaffScopeManager systemUpdateStaffScopeManager;

	@Override
	public String updateStaff(String inXml) throws Exception {
		String outXml = "";
		cn.ffcs.uom.webservices.bean.updatestaff.Root root = null;
		cn.ffcs.uom.webservices.bean.updatestaff.Root result = new cn.ffcs.uom.webservices.bean.updatestaff.Root();
		try {
			root = (cn.ffcs.uom.webservices.bean.updatestaff.Root) CastorParser
					.toObject(inXml,
							cn.ffcs.uom.webservices.bean.updatestaff.Root.class);
			cn.ffcs.uom.webservices.bean.comm.MsgHead msgHead = root
					.getMsgHead();
			Long updateStaff = new Long(msgHead.getFrom());
			if (root != null) {
				cn.ffcs.uom.webservices.bean.updatestaff.MsgBody msgBody = root
						.getMsgBody();
				cn.ffcs.uom.webservices.bean.updatestaff.InParam inParam = msgBody
						.getInParam();
				String staffAccount = inParam.getStaffAccount();
				if (StrUtil.isEmpty(staffAccount)) {
					throw new Exception("staffAccount为必填值");
				}
				Staff queryStaff = new Staff();
				queryStaff.setStaffAccount(staffAccount);
				List<Staff> list = staffManager.queryStaffList(queryStaff);
				if (list == null || list.size() != 1) {
					throw new Exception("staffAccount值错误");
				}
				Staff staff = list.get(0);
				PartyRole partyRole = staffManager.getPartyRole(staff
						.getPartyRoleId());
				if (partyRole == null || partyRole.getPartyId() == null) {
					throw new Exception("数据错误:员工参与人角色id值错误");
				}
				Party party = staffManager.getParty(partyRole.getPartyId());
				if (party == null || party.getPartyId() == null) {
					throw new Exception("数据错误:参与人角色party_id值错误");
				}
				String systemCode = msgHead.getFrom();
				if (StrUtil.isEmpty(systemCode)) {
					throw new Exception("from系统编码错误");
				}
				SystemUpdateStaffScope querySystemUpdateStaffScope = new SystemUpdateStaffScope();
				querySystemUpdateStaffScope.setSystemCode(systemCode);
				List<SystemUpdateStaffScope> scopeList = systemUpdateStaffScopeManager
						.querySystemUpdateStaffScopeList(querySystemUpdateStaffScope);

				if (scopeList == null || scopeList.size() <= 0) {
					throw new Exception("该系统没有修改员工信息的权限，请配置");
				}

				cn.ffcs.uom.webservices.bean.updatestaff.UpdateStaffInfo updateStaffInfo = inParam
						.getUpdateStaffInfo();

				String staffName = updateStaffInfo.getName();
				if (!StrUtil.isNullOrEmpty(staffName)) {
					if (SystemUpdateStaffScope.includes(scopeList, "STAFF",
							"STAFF_NAME")) {
						staff.setStaffName(staffName);
						staff.setIsNeedUpdate(true);
						staff.setUpdateStaff(updateStaff);
					}
					if (SystemUpdateStaffScope.includes(scopeList, "PARTY",
							"PARTY_NAME")) {
						party.setPartyName(staffName);
						party.setIsNeedUpdate(true);
						party.setUpdateStaff(updateStaff);
					}
				}
				PartyCertification queryPartyCertification = new PartyCertification();
				queryPartyCertification.setPartyId(party.getPartyId());
				queryPartyCertification
						.setCertSort(PartyConstant.DEFAULT_CERT_SORT);

				List<PartyCertification> partyCertList = partyManager
						.getPartyCertificationList(queryPartyCertification);
				PartyCertification partyCertification = null;
				if (partyCertList != null && partyCertList.size() > 0) {
					partyCertification = partyCertList.get(0);
				}

				PartyContactInfo queryContactInfo = new PartyContactInfo();
				queryContactInfo.setPartyId(party.getPartyId());
				List<PartyContactInfo> partyContactList = partyManager
						.getPartyContactInfo(queryContactInfo);
				// List<PartyContactInfo> partyContactList = partyManager
				// .getPartyContInfoList(queryContactInfo);
				PartyContactInfo partyContactInfo = null;
				if (partyContactList != null && partyContactList.size() > 0) {
					partyContactInfo = partyContactList.get(0);
				}

				if (partyCertification == null) {
					partyCertification = new PartyCertification();
					partyCertification.setCreateStaff(updateStaff);
					partyCertification
							.setCertSort(PartyConstant.DEFAULT_CERT_SORT);
				}
				if (partyContactInfo == null) {
					partyContactInfo = new PartyContactInfo();
					partyContactInfo.setCreateStaff(updateStaff);
				}

				cn.ffcs.uom.webservices.bean.updatestaff.Certification certification = updateStaffInfo
						.getCertification();
				if (!StrUtil.isNullOrEmpty(certification)) {
					if (!StrUtil.isNullOrEmpty(certification.getCertType())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CERTIFICATION", "CERT_TYPE")) {
							partyCertification.setCertType(certification
									.getCertType());
						}
					}
					if (!StrUtil.isNullOrEmpty(certification.getCertNbr())) {
						/**
						 * 验证证件号码是否已存在
						 */
						PartyCertification queryPC = new PartyCertification();
						queryPC.setCertNumber(certification.getCertNbr());

						List<PartyCertification> pCList = partyManager
								.getPartyCertificationList(queryPC);
						if (pCList != null && pCList.size() > 0) {
							// 排除自身问题
							for (PartyCertification partyCert : pCList) {
								if (!(partyRole.getPartyId().equals(partyCert
										.getPartyId()))) {
									throw new Exception("证件号码已存在，请确认");
								}
							}
						}
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CERTIFICATION", "CERT_NUMBER")) {
							partyCertification.setCertNumber(certification
									.getCertNbr());
							partyCertification.setIsNeedUpdate(true);
						}
					}
					partyCertification.setUpdateStaff(updateStaff);
					party.setPartyCertification(partyCertification);
				}
				cn.ffcs.uom.webservices.bean.updatestaff.ContactInfo contactInfo = updateStaffInfo
						.getContactInfo();
				if (!StrUtil.isNullOrEmpty(contactInfo)) {
					if (!StrUtil.isNullOrEmpty(contactInfo.getContactName())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "CONTACT_NAME")) {
							partyContactInfo.setContactName(contactInfo
									.getContactName());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getContactType())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "CONTACT_TYPE")) {
							partyContactInfo.setContactType(contactInfo
									.getContactType());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getContactGender())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "CONTACT_GENDER")) {
							partyContactInfo.setContactGender(contactInfo
									.getContactGender());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getContactAddress())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "CONTACT_ADDRESS")) {
							partyContactInfo.setContactAddress(contactInfo
									.getContactAddress());
						}
					}
					if (!StrUtil
							.isNullOrEmpty(contactInfo.getContactEmployer())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "CONTACT_EMPLOYER")) {
							partyContactInfo.setContactEmployer(contactInfo
									.getContactEmployer());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getHomePhone())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "HOME_PHONE")) {
							partyContactInfo.setHomePhone(contactInfo
									.getHomePhone());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getOfficPhone())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "OFFICE_PHONE")) {
							partyContactInfo.setOfficePhone(contactInfo
									.getOfficPhone());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getMobilePhone())) {
						PartyContactInfo queryCI = new PartyContactInfo();
						queryCI.setMobilePhone(contactInfo.getMobilePhone());
						List<PartyContactInfo> pCList = partyManager
								.getPartyContInfoList(queryCI);
						if (pCList != null && pCList.size() > 0) {
							// 排除自身问题
							for (PartyContactInfo partyCont : pCList) {
								if (!(partyRole.getPartyId().equals(partyCont
										.getPartyId()))) {
									throw new Exception("手机号码已存在，请确认");
								}
							}
						}
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "MOBILE_PHONE")) {
							partyContactInfo.setMobilePhone(contactInfo
									.getMobilePhone());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getEmail())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "E_MAIL")) {
							partyContactInfo.setEmail(contactInfo.getEmail());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getPostCode())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "POSTCODE")) {
							partyContactInfo.setPostCode(contactInfo
									.getPostCode());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getPostAddress())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "POST_ADDRESS")) {
							partyContactInfo.setPostAddress(contactInfo
									.getPostAddress());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getFax())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "FAX")) {
							partyContactInfo.setFax(contactInfo.getFax());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo.getQqNumber())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "QQ_NUMBER")) {
							partyContactInfo.setQqNumber(contactInfo
									.getQqNumber());
						}
					}
					if (!StrUtil.isNullOrEmpty(contactInfo
							.getMobilePhoneSpare())) {
						if (SystemUpdateStaffScope.includes(scopeList,
								"PARTY_CONTACT_INFO", "MOBILE_PHONE_SPARE")) {
							partyContactInfo.setMobilePhoneSpare(contactInfo
									.getMobilePhoneSpare());
						}
					}
					partyContactInfo.setIsNeedUpdate(true);
					partyContactInfo.setUpdateStaff(updateStaff);
					party.setPartyContactInfo(partyContactInfo);
				}
				staff.setParty(party);
				staffManager.updateStaffByIntf(staff);
				try {
					staffManager.updateStaffToLdap(staffAccount);
				} catch (Exception e) {
				}
				RootBuilder.getResponseMsgHead(msgHead, new Date());
				result.setMsgHead(msgHead);

				cn.ffcs.uom.webservices.bean.updatestaff.OutParam outParam = new cn.ffcs.uom.webservices.bean.updatestaff.OutParam();
				outParam.setResult(WsConstants.SUCCESS);

				cn.ffcs.uom.webservices.bean.updatestaff.MsgBody outMsgBody = new cn.ffcs.uom.webservices.bean.updatestaff.MsgBody();
				outMsgBody.setOutParam(outParam);

				result.setMsgBody(outMsgBody);

				outXml = CastorParser.toXML(result);
				/**
				 * 删除命名空间
				 */
				outXml = WsUtil.delNameSpace(outXml);
				return outXml;
			}
		} catch (Exception e) {
			throw new RtManagerException(e.getMessage());
		}
		return outXml;
	}

	@Override
	public String queryStaff(String inXml) throws Exception {
		String outXml = "";
		cn.ffcs.uom.webservices.bean.querystaff.Root root = null;
		cn.ffcs.uom.webservices.bean.querystaff.Root result = new cn.ffcs.uom.webservices.bean.querystaff.Root();
		try {
			root = (cn.ffcs.uom.webservices.bean.querystaff.Root) CastorParser
					.toObject(inXml,
							cn.ffcs.uom.webservices.bean.querystaff.Root.class);
			cn.ffcs.uom.webservices.bean.comm.MsgHead msgHead = root
					.getMsgHead();
			if (root != null) {
				cn.ffcs.uom.webservices.bean.querystaff.MsgBody msgBody = root
						.getMsgBody();
				cn.ffcs.uom.webservices.bean.querystaff.InParam inParam = msgBody
						.getInParam();
				String staffAccount = inParam.getStaffAccount();
				if (StrUtil.isEmpty(staffAccount)) {
					throw new Exception("staffAccount为必填值");
				}
				Staff queryStaff = new Staff();
				queryStaff.setStaffAccount(staffAccount);
				List<Staff> list = staffManager.queryStaffList(queryStaff);
				if (list == null || list.size() != 1) {
					throw new Exception("staffAccount值错误");
				}

				String systemCode = msgHead.getFrom();
				if (StrUtil.isEmpty(systemCode)) {
					throw new Exception("from系统编码错误");
				}
				Staff staff = list.get(0);
				PartyRole partyRole = staffManager.getPartyRole(staff
						.getPartyRoleId());
				if (partyRole == null || partyRole.getPartyId() == null) {
					throw new Exception("数据错误:员工参与人角色id值错误");
				}
				Party party = staffManager.getParty(partyRole.getPartyId());
				if (party == null || party.getPartyId() == null) {
					throw new Exception("数据错误:参与人角色party_id值错误");
				}

				PartyCertification queryPartyCertification = new PartyCertification();
				queryPartyCertification.setPartyId(party.getPartyId());
				queryPartyCertification
						.setCertSort(PartyConstant.DEFAULT_CERT_SORT);

				List<PartyCertification> partyCertList = partyManager
						.getPartyCertificationList(queryPartyCertification);
				PartyCertification partyCertification = null;
				if (partyCertList != null && partyCertList.size() > 0) {
					partyCertification = partyCertList.get(0);
				}

				PartyContactInfo queryContactInfo = new PartyContactInfo();
				queryContactInfo.setPartyId(party.getPartyId());
				List<PartyContactInfo> partyContactList = partyManager
						.getPartyContactInfo(queryContactInfo);
				// List<PartyContactInfo> partyContactList = partyManager
				// .getPartyContInfoList(queryContactInfo);
				PartyContactInfo partyContactInfo = null;
				if (partyContactList != null && partyContactList.size() > 0) {
					partyContactInfo = partyContactList.get(0);
				}

				RootBuilder.getResponseMsgHead(msgHead, new Date());
				result.setMsgHead(msgHead);

				cn.ffcs.uom.webservices.bean.querystaff.OutParam outParam = new cn.ffcs.uom.webservices.bean.querystaff.OutParam();
				outParam.setResult(WsConstants.SUCCESS);

				cn.ffcs.uom.webservices.bean.querystaff.StaffInfo staffInfo = new cn.ffcs.uom.webservices.bean.querystaff.StaffInfo();
				staffInfo.setName(staff.getStaffName());

				cn.ffcs.uom.webservices.bean.querystaff.Certification certification = new cn.ffcs.uom.webservices.bean.querystaff.Certification();
				if (partyCertification != null) {
					certification
							.setCertNbr(partyCertification.getCertNumber());
					certification.setCertType(partyCertification.getCertType());
				}
				cn.ffcs.uom.webservices.bean.querystaff.ContactInfo contactInfo = new cn.ffcs.uom.webservices.bean.querystaff.ContactInfo();
				if (partyContactInfo != null) {
					contactInfo.setContactAddress(partyContactInfo
							.getContactAddress());
					contactInfo.setContactEmployer(partyContactInfo
							.getContactEmployer());
					contactInfo.setContactGender(partyContactInfo
							.getContactGender());
					contactInfo.setContactName(partyContactInfo
							.getContactName());
					contactInfo.setContactType(partyContactInfo
							.getContactType());
					contactInfo.setEmail(partyContactInfo.getEmail());
					contactInfo.setFax(partyContactInfo.getFax());
					contactInfo.setHomePhone(partyContactInfo.getHomePhone());
					contactInfo.setMobilePhone(partyContactInfo
							.getMobilePhone());
					contactInfo.setMobilePhoneSpare(partyContactInfo
							.getMobilePhoneSpare());
					contactInfo
							.setOfficPhone(partyContactInfo.getOfficePhone());
					contactInfo.setPostAddress(partyContactInfo
							.getPostAddress());
					contactInfo.setPostCode(partyContactInfo.getPostCode());
					contactInfo.setQqNumber(partyContactInfo.getQqNumber());
				}

				staffInfo.setCertification(certification);
				staffInfo.setContactInfo(contactInfo);

				outParam.setStaffInfo(staffInfo);
				cn.ffcs.uom.webservices.bean.querystaff.MsgBody outMsgBody = new cn.ffcs.uom.webservices.bean.querystaff.MsgBody();

				outMsgBody.setOutParam(outParam);
				result.setMsgBody(outMsgBody);
				outXml = CastorParser.toXML(result);
				/**
				 * 删除命名空间
				 */
				outXml = WsUtil.delNameSpace(outXml);
				return outXml;
			}
		} catch (Exception e) {
			throw new RtManagerException(e.getMessage());
		}
		return outXml;
	}
}
