package cn.ffcs.uom.mail.manager.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.common.util.JsonValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.TripleDESUtil;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.mail.model.GroupMailBusinessParam;
import cn.ffcs.uom.mail.model.GroupMailRootInParam;
import cn.ffcs.uom.mail.model.GroupMailRootOutParam;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.restservices.model.OipError;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.IntfLogManager;
import cn.ffcs.uom.webservices.model.IntfLog;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service("groupMailManager")
@Scope("prototype")
public class GroupMailManagerImpl implements GroupMailManager {

	private IntfLogManager intfLogManager = (IntfLogManager) ApplicationContextUtil
			.getBean("intfLogManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	private CascadeRelationManager cascadeRelationManager = (CascadeRelationManager) ApplicationContextUtil
			.getBean("cascadeRelationManager");

	@Override
	public String groupMamilTimeStamp(String msgId) {

		String json = null;
		String encryJson = null;

		IntfLog intfLog = new IntfLog();
		intfLog.setMsgId(msgId);
		intfLog.setTransId(msgId);
		intfLog.setSystem(WsConstants.SYSTEM_CODE_GROUP_EMAIL);
		intfLog.setResult(WsConstants.TASK_FAILED);
		intfLog.setBeginDate(new Date());

		GroupMailRootInParam rootIn = new GroupMailRootInParam();
		rootIn.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);

		GroupMailBusinessParam biz = new GroupMailBusinessParam();
		biz.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);
		biz.setBizId(GroupMailConstant.GROUP_MAIL_BIZ_ID_0);
		biz.setAppKey(GroupMailConstant.GROUP_MAIL_APP_KEY);

		String oipServiceCode = UomClassProvider
				.getIntfUrl("oipServiceCodeGroupMail");

		if (StrUtil.isNullOrEmpty(oipServiceCode)) {
			intfLog.setErrMsg("oipServiceCodeGroupMail集团统一邮箱OIP服务编码没有配置");
			intfLogManager.saveIntfLog(intfLog);
			return "集团统一邮箱OIP服务编码没有配置";
		}

		String oipHttpUrl = UomClassProvider.getIntfUrl("oipRestUrlGroupMail");

		if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
			intfLog.setErrMsg("oipRestUrlGroupMail集团统一邮箱接口地址没有配置");
			intfLogManager.saveIntfLog(intfLog);
			return "集团统一邮箱接口地址没有配置";
		}

		try {
			json = JacksonUtil.Object2JSon(biz);
		} catch (JsonGenerationException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		} catch (JsonMappingException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		} catch (IOException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		}

		intfLog.setRequestContent(json);

		try {
			rootIn.setAppSignature(TripleDESUtil.bytesToHexString(TripleDESUtil
					.encryptMode(
							GroupMailConstant.GROUP_MAIL_ENCODE_KEY.getBytes(),
							json.getBytes(TripleDESUtil.ENCODING_GBK))));
		} catch (UnsupportedEncodingException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Json加密失败";
		}

		try {
			encryJson = JacksonUtil.Object2JSon(rootIn);
		} catch (JsonGenerationException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		} catch (JsonMappingException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		} catch (IOException e) {
			intfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
					: e.getMessage().substring(0, 500));
			intfLogManager.saveIntfLog(intfLog);
			return "Object转换Json失败";
		}

		Client client = Client.create();

		client.setConnectTimeout(30 * 1000);

		WebResource webResource = client.resource(oipHttpUrl);

		@SuppressWarnings("rawtypes")
		MultivaluedMap queryParams = new MultivaluedMapImpl();
		queryParams.add("isRest", "true");
		queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
		queryParams.add("servCode", oipServiceCode);
		queryParams.add("msgId", msgId);
		queryParams.add("transactionId", msgId);
		queryParams.add("sysId", rootIn.getSysId());
		queryParams.add("appSignature", rootIn.getAppSignature());

		ClientResponse response = webResource.queryParams(queryParams)
				.entity(encryJson, MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);

		String entity = response.getEntity(String.class);

		intfLog.setResponseContent(entity);

		if (!StrUtil.isEmpty(entity)) {

			if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
				try {
					if (entity.contains("errorCode")
							&& entity.contains("errorDesc")) {
						OipError oipError = (OipError) JacksonUtil.JSon2Object(
								entity, OipError.class);
						intfLog.setErrCode(oipError.getErrorCode());
						intfLog.setErrMsg(oipError.getErrorDesc());
						intfLogManager.saveIntfLog(intfLog);
						response.close();
						client.destroy();
						return oipError.getErrorDesc();
					} else {

						GroupMailRootOutParam rootOut = (GroupMailRootOutParam) JacksonUtil
								.JSon2Object(entity,
										GroupMailRootOutParam.class);

						if (rootOut != null) {
							if (rootOut.isRet()) {
								intfLog.setErrMsg(rootOut.getMsg());
								intfLog.setResult(WsConstants.TASK_SUCCESS);
								intfLogManager.saveIntfLog(intfLog);
								response.close();
								client.destroy();
								return rootOut.getTimeStamp();
							} else {
								intfLog.setErrMsg(rootOut.getErrorMsg());
								intfLogManager.saveIntfLog(intfLog);
								response.close();
								client.destroy();
								return rootOut.getErrorMsg();
							}
						} else {
							intfLog.setErrMsg("集团统一邮箱返回的应答信息为空");
							intfLogManager.saveIntfLog(intfLog);
							response.close();
							client.destroy();
							return "集团统一邮箱返回的应答信息为空";
						}

					}

				} catch (JsonGenerationException e) {

					intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
							.getMessage() : e.getMessage().substring(0, 500));
					intfLogManager.saveIntfLog(intfLog);
					response.close();
					client.destroy();
					return "Json转换Object失败";
				} catch (JsonMappingException e) {
					intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
							.getMessage() : e.getMessage().substring(0, 500));
					response.close();
					client.destroy();
					return "Json转换Object失败";
				} catch (IOException e) {
					intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
							.getMessage() : e.getMessage().substring(0, 500));
					intfLogManager.saveIntfLog(intfLog);
					response.close();
					client.destroy();
					return "Json转换Object失败";
				}

			} else {
				intfLog.setErrMsg("返回的报文[Json]格式不正确");
				intfLogManager.saveIntfLog(intfLog);
				response.close();
				client.destroy();
				return "集团统一邮箱未返回应答报文";
			}

		} else {

			intfLog.setErrMsg("集团统一邮箱未返回应答报文");
			intfLogManager.saveIntfLog(intfLog);
			response.close();
			client.destroy();
			return "集团统一邮箱未返回应答报文";

		}

	}

	@Override
	public String groupMailPackageInfo(String bizId, Party party,
			Organization organization) {

		String json = null;
		String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);
		String timeStamp = this.groupMamilTimeStamp(msgId);

		IntfLog intfLog = new IntfLog();
		intfLog.setMsgId(msgId);
		intfLog.setTransId(msgId);
		intfLog.setSystem(WsConstants.SYSTEM_CODE_GROUP_EMAIL);
		intfLog.setResult(WsConstants.TASK_FAILED);
		intfLog.setBeginDate(new Date());

		GroupMailBusinessParam biz = new GroupMailBusinessParam();
		biz.setBizId(bizId);
		biz.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);
		biz.setAppKey(GroupMailConstant.GROUP_MAIL_APP_KEY);

		if (IdcardValidator.isDigital(timeStamp)) {

			biz.setTimeStamp(timeStamp);

			if (GroupMailConstant.GROUP_MAIL_BIZ_ID_5.equals(bizId)) {
				if (party != null) {
					if (party.getPartyContactInfo() != null) {
						biz.setAccount(party.getPartyContactInfo()
								.getGrpUnEmail());
					}
				}
			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_13.equals(bizId)) {

				CascadeRelation cascadeRelation = new CascadeRelation();
				StaffOrganization staffOrganization = new StaffOrganization();
				cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_7);

				biz.setInvisible(true);
				biz.setStates(GroupMailConstant.GROUP_MAIL_AH);
				biz.setStatus(GroupMailConstant.GROUP_MAIL_STATUS_0);
				biz.setPositionNumber(GroupMailConstant.GROUP_MAIL_POSITION_NUMBER_9900);

				if (party != null) {

					biz.setCName(party.getPartyName());

					if (party.getPartyContactInfo() != null) {
						biz.setAccount(party.getPartyContactInfo()
								.getGrpUnEmail());
						biz.setGender(party.getPartyContactInfo()
								.getContactGender());
						biz.setMobilePhone(party.getPartyContactInfo()
								.getMobilePhone());
						biz.setCompanyPhone(party.getPartyContactInfo()
								.getOfficePhone());
					}

					if (party.getIndividual() != null) {
						biz.setBirthday(DateUtil.dateToStr(party
								.getIndividual().getBirthday()));

					}

					if (party.getStaffOrganization() != null
							&& party.getStaffOrganization().getStaffSeq() != null) {
						biz.setShowNum(party.getStaffOrganization()
								.getStaffSeq().toString());
					}

					if (party.getStaff() != null
							&& party.getStaff().getStaffPositionName() != null) {
						staffOrganization.setStaffId(party.getStaff()
								.getStaffId());
						biz.setOccupation(party.getStaff()
								.getStaffPositionName());
					}
				}

				if (organization != null && organization.getOrgId() != null) {

					staffOrganization.setOrgId(organization.getOrgId());

					CascadeRelation cascadeRelationDb = cascadeRelationManager
							.queryCascadeRelationByStaffOrganization(
									cascadeRelation, staffOrganization);

					if (cascadeRelationDb != null
							&& !StrUtil.isEmpty(cascadeRelationDb
									.getRelaCascadeValue())) {
						biz.setPositionNumber(cascadeRelationDb
								.getRelaCascadeValue());
					}

					if (organization.getOrgPriority() != null) {
						biz.setPartMentNum(organization.getOrgPriority()
								.toString());
					} else {
						biz.setPartMentNum("200");
					}

					OrganizationRelation organizationRelation = organization
							.getOrganizationRelation(OrganizationConstant.RELA_CD_INNER);
					if (organizationRelation != null
							&& organizationRelation.getOrgId() != null) {
						Organization org = organizationRelation
								.getOrganization();
						if (org != null && org.getOrgName().contains("市分公司")) {
							biz.setCity(org.getOrgName().substring(0,
									org.getOrgName().lastIndexOf("分公司")));
						}

					}

					OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
					organizationExtendAttr.setOrgId(organization.getOrgId());
					organizationExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(organizationExtendAttr);

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						biz.setPartment(organizationExtendAttr
								.getOrgAttrValue());
					} else {
						biz.setPartment(organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
					}
				}

			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_14.equals(bizId)) {

				CascadeRelation cascadeRelation = new CascadeRelation();
				StaffOrganization staffOrganization = new StaffOrganization();
				cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_7);

				biz.setInvisible(true);
				biz.setStates(GroupMailConstant.GROUP_MAIL_AH);
				biz.setStatus(GroupMailConstant.GROUP_MAIL_STATUS_0);
				biz.setPositionNumber(GroupMailConstant.GROUP_MAIL_POSITION_NUMBER_9900);

				if (party != null) {

					biz.setCName(party.getPartyName());

					if (party.getPartyContactInfo() != null) {
						biz.setAccount(party.getPartyContactInfo()
								.getGrpUnEmail());
						biz.setGender(party.getPartyContactInfo()
								.getContactGender());
						biz.setMobilePhone(party.getPartyContactInfo()
								.getMobilePhone());
						biz.setCompanyPhone(party.getPartyContactInfo()
								.getOfficePhone());
					}

					if (party.getIndividual() != null) {
						biz.setBirthday(DateUtil.dateToStr(party
								.getIndividual().getBirthday()));

					}

					if (party.getStaffOrganization() != null
							&& party.getStaffOrganization().getStaffSeq() != null) {
						biz.setShowNum(party.getStaffOrganization()
								.getStaffSeq().toString());
					}

					if (party.getStaff() != null
							&& party.getStaff().getStaffPositionName() != null) {
						staffOrganization.setStaffId(party.getStaff()
								.getStaffId());
						biz.setOccupation(party.getStaff()
								.getStaffPositionName());
					}
				}

				if (organization != null && organization.getOrgId() != null) {

					staffOrganization.setOrgId(organization.getOrgId());

					CascadeRelation cascadeRelationDb = cascadeRelationManager
							.queryCascadeRelationByStaffOrganization(
									cascadeRelation, staffOrganization);

					if (cascadeRelationDb != null
							&& !StrUtil.isEmpty(cascadeRelationDb
									.getRelaCascadeValue())) {
						biz.setPositionNumber(cascadeRelationDb
								.getRelaCascadeValue());
					}

					if (organization.getOrgPriority() != null) {
						biz.setPartMentNum(organization.getOrgPriority()
								.toString());
					} else {
						biz.setPartMentNum("200");
					}

					OrganizationRelation organizationRelation = organization
							.getOrganizationRelation(OrganizationConstant.RELA_CD_INNER);
					if (organizationRelation != null
							&& organizationRelation.getOrgId() != null) {
						Organization org = organizationRelation
								.getOrganization();
						if (org != null && org.getOrgName().contains("市分公司")) {
							biz.setCity(org.getOrgName().substring(0,
									org.getOrgName().lastIndexOf("分公司")));
						}

					}

					OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
					organizationExtendAttr.setOrgId(organization.getOrgId());
					organizationExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(organizationExtendAttr);

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						biz.setPartment(organizationExtendAttr
								.getOrgAttrValue());
					} else {
						biz.setPartment(organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
					}
				}

			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_16.equals(bizId)) {

				biz.setAccount(GroupMailConstant.GROUP_MAIL_MASTER_ACCOUNT);

				if (organization != null && organization.getOrgId() != null) {

					OrganizationRelation orgRela = new OrganizationRelation();
					orgRela.setOrgId(organization.getOrgId());
					Organization org = orgRela.getOrganization();

					if (organization.getOrgPriority() != null) {
						biz.setPartMentNum(organization.getOrgPriority()
								.toString());
					} else {
						biz.setPartMentNum("200");
					}

					OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
					organizationExtendAttr.setOrgId(organization.getOrgId());
					organizationExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(organizationExtendAttr);

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						biz.setPartment(organizationExtendAttr
								.getOrgAttrValue());
					} else {
						biz.setPartment(organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
					}

					if (org != null
							&& !organization.getOrgName().equals(
									org.getOrgName())) {

						biz.setModifyPartMentName(true);
						biz.setPartMentDesc(biz.getPartment().replaceFirst(
								org.getOrgName(), organization.getOrgName()));

					}

				}

			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_17.equals(bizId)) {

				biz.setAccount(GroupMailConstant.GROUP_MAIL_MASTER_ACCOUNT);

				if (organization != null && organization.getOrgId() != null) {

					OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
					organizationExtendAttr.setOrgId(organization.getOrgId());
					organizationExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(organizationExtendAttr);

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						biz.setPartment(organizationExtendAttr
								.getOrgAttrValue());
					} else {
						biz.setPartment(organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
					}
				}
			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_18.equals(bizId)) {

				biz.setAccount(GroupMailConstant.GROUP_MAIL_MASTER_ACCOUNT);

				if (organization != null && organization.getOrgId() != null) {

					OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
					organizationExtendAttr.setOrgId(organization.getOrgId());
					organizationExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(organizationExtendAttr);

					if (organizationExtendAttr != null
							&& !StrUtil.isEmpty(organizationExtendAttr
									.getOrgAttrValue())) {
						biz.setPartment(organizationExtendAttr
								.getOrgAttrValue());
					}

					if (organization.getRelaOrgId() != null) {

						organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgId(organization
								.getRelaOrgId());
						organizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

						organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(organizationExtendAttr);

						if (organizationExtendAttr != null
								&& !StrUtil.isEmpty(organizationExtendAttr
										.getOrgAttrValue())) {
							biz.setPartMentDesc(organizationExtendAttr
									.getOrgAttrValue());
						} else {
							organization.setOrgId(organization.getRelaOrgId());
							biz.setPartMentDesc(organization
									.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
						}

						// if (!StrUtil.isEmpty(biz.getPartMentDesc())) {
						// if (biz.getPartMentDesc().indexOf("000") == -1) {
						// biz.setPartment(biz.getPartMentDesc() + "#"
						// + organization.getOrgName());
						// } else {
						// biz.setPartment(biz.getPartMentDesc()
						// .replaceFirst("000",
						// organization.getOrgName()));
						// }
						// }

					}

				}
			} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2.equals(bizId)) {
				if (party != null) {
					if (party.getPartyContactInfo() != null) {
						biz.setAccount(party.getPartyContactInfo()
								.getGrpUnEmail());
					}
				}
			}

			try {
				json = JacksonUtil.Object2JSon(biz);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
			}

		} else {
			return timeStamp;
		}

		return this.groupMailSendInfo(bizId, msgId, json);

	}

	@Override
	public String groupMailSendInfo(String bizId, String msgId, String json) {

		IntfLog intfLog = new IntfLog();
		intfLog.setMsgId(msgId);
		intfLog.setTransId(msgId);
		intfLog.setSystem(WsConstants.SYSTEM_CODE_GROUP_EMAIL);
		intfLog.setResult(WsConstants.TASK_FAILED);
		intfLog.setBeginDate(new Date());
		intfLog.setRequestContent(json);

		if (!StrUtil.isEmpty(json)) {

			String encryJson = null;
			String oipServiceCode = UomClassProvider
					.getIntfUrl("oipServiceCodeGroupMail");

			if (StrUtil.isNullOrEmpty(oipServiceCode)) {
				intfLog.setErrMsg("oipServiceCodeGroupMail集团统一邮箱OIP服务编码没有配置");
				intfLogManager.saveIntfLog(intfLog);
				return "集团统一邮箱OIP服务编码没有配置";
			}

			String oipHttpUrl = UomClassProvider
					.getIntfUrl("oipRestUrlGroupMail");

			if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
				intfLog.setErrMsg("oipRestUrlGroupMail集团统一邮箱接口地址没有配置");
				intfLogManager.saveIntfLog(intfLog);
				return "集团统一邮箱接口地址没有配置";
			}

			GroupMailRootInParam rootIn = new GroupMailRootInParam();
			rootIn.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);
			try {
				rootIn.setAppSignature(TripleDESUtil
						.bytesToHexString(TripleDESUtil.encryptMode(
								GroupMailConstant.GROUP_MAIL_ENCODE_KEY
										.getBytes(), json
										.getBytes(TripleDESUtil.ENCODING_GBK))));
			} catch (UnsupportedEncodingException e) {
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
				return "AppSignature加密失败";
			}

			try {
				encryJson = JacksonUtil.Object2JSon(rootIn);
			} catch (JsonGenerationException e) {
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
				return "Object转换Json失败";
			} catch (JsonMappingException e) {
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
				return "Object转换Json失败";
			} catch (IOException e) {
				intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
						.getMessage() : e.getMessage().substring(0, 500));
				intfLogManager.saveIntfLog(intfLog);
				return "Object转换Json失败";
			}

			Client client = Client.create();

			client.setConnectTimeout(30 * 1000);

			WebResource webResource = client.resource(oipHttpUrl);

			@SuppressWarnings("rawtypes")
			MultivaluedMap queryParams = new MultivaluedMapImpl();
			queryParams.add("isRest", "true");
			queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
			queryParams.add("servCode", oipServiceCode);
			queryParams.add("msgId", msgId);
			queryParams.add("transactionId", msgId);
			queryParams.add("sysId", rootIn.getSysId());
			queryParams.add("appSignature", rootIn.getAppSignature());

			ClientResponse response = webResource.queryParams(queryParams)
					.entity(encryJson, MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);

			String entity = response.getEntity(String.class);

			intfLog.setResponseContent(entity);

			if (!StrUtil.isEmpty(entity)) {

				if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
					try {
						if (entity.contains("errorCode")
								&& entity.contains("errorDesc")) {
							OipError oipError = (OipError) JacksonUtil
									.JSon2Object(entity, OipError.class);
							intfLog.setErrCode(oipError.getErrorCode());
							intfLog.setErrMsg(oipError.getErrorDesc());
							intfLogManager.saveIntfLog(intfLog);
							response.close();
							client.destroy();
							return oipError.getErrorDesc();
						} else {

							GroupMailRootOutParam rootOut = (GroupMailRootOutParam) JacksonUtil
									.JSon2Object(entity,
											GroupMailRootOutParam.class);
							if (rootOut != null) {
								if (rootOut.isRet()) {
									intfLog.setErrMsg(rootOut.getMsg());
									intfLog.setResult(WsConstants.TASK_SUCCESS);

									if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2
											.equals(bizId)
											&& !StrUtil.isNullOrEmpty(rootOut
													.getUser())
											&& !StrUtil.isEmpty(rootOut
													.getUser().getAccount())) {
										intfLog.setEndDate(new Date());
										intfLog.setErrMsg("集团统一邮箱已经被使用："
												+ rootOut.getUser()
														.getAccount());
										intfLogManager.saveIntfLog(intfLog);
										response.close();
										client.destroy();
										return GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE;
									}

								} else {
									if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2
											.equals(bizId)) {
										intfLog.setEndDate(new Date());
										intfLog.setErrMsg("集团统一邮箱未被使用");
										intfLogManager.saveIntfLog(intfLog);
										response.close();
										client.destroy();
										return GroupMailConstant.GROUP_MAIL_BIZ_ID_2_FALSE;
									} else if (!StrUtil.isNullOrEmpty(rootOut
											.getRecommend())
											&& rootOut.getRecommend().length > 0) {

										String str = new String();

										for (int i = 0; i < rootOut
												.getRecommend().length; i++) {
											if (i == 0) {
												str = rootOut.getRecommend()[i];
											} else {
												str = str
														+ ","
														+ rootOut
																.getRecommend()[i];
											}
										}

										intfLog.setErrMsg("集团统一邮箱已经被使用，推荐使用集团统一邮箱："
												+ str);
										intfLogManager.saveIntfLog(intfLog);
										response.close();
										client.destroy();
										return "集团统一邮箱已经被使用，推荐使用集团统一邮箱：" + str;
									} else if (!StrUtil.isNullOrEmpty(rootOut
											.getAccountList())
											&& rootOut.getAccountList().length > 0) {

										String str = new String();

										for (int i = 0; i < rootOut
												.getAccountList().length; i++) {
											if (i == 0) {
												str = rootOut.getAccountList()[i];
											} else {
												str = str
														+ ","
														+ rootOut
																.getAccountList()[i];
											}
										}

										intfLog.setErrMsg("该组织不能删除，请将以下员工移除。集团统一邮箱账号分别为："
												+ str);
										intfLogManager.saveIntfLog(intfLog);
										response.close();
										client.destroy();
										return "该组织不能删除，请将以下员工移除。集团统一邮箱账号分别为："
												+ str;
									} else {
										intfLog.setErrMsg(rootOut.getMsg());
										intfLogManager.saveIntfLog(intfLog);
										response.close();
										client.destroy();
										return rootOut.getMsg();
									}
								}
							} else {
								intfLog.setErrMsg("集团统一邮箱返回的应答信息为空");
								intfLogManager.saveIntfLog(intfLog);
								response.close();
								client.destroy();
								return "集团统一邮箱返回的应答信息为空";
							}

						}

					} catch (JsonGenerationException e) {

						intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
								.getMessage() : e.getMessage()
								.substring(0, 500));
						intfLogManager.saveIntfLog(intfLog);
						response.close();
						client.destroy();
						return "Json转换Object失败";
					} catch (JsonMappingException e) {
						intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
								.getMessage() : e.getMessage()
								.substring(0, 500));
						intfLogManager.saveIntfLog(intfLog);
						response.close();
						client.destroy();
						return "Json转换Object失败";
					} catch (IOException e) {
						intfLog.setErrMsg(e.getMessage().length() <= 500 ? e
								.getMessage() : e.getMessage()
								.substring(0, 500));
						intfLogManager.saveIntfLog(intfLog);
						response.close();
						client.destroy();
						return "Json转换Object失败";
					}

				} else {
					intfLog.setErrMsg("返回的报文[Json]格式不正确");
					intfLogManager.saveIntfLog(intfLog);
					response.close();
					client.destroy();
					return "集团统一邮箱未返回应答报文";
				}

			} else {

				intfLog.setErrMsg("集团统一邮箱未返回应答报文");
				intfLogManager.saveIntfLog(intfLog);
				response.close();
				client.destroy();
				return "集团统一邮箱未返回应答报文";

			}

			intfLog.setEndDate(new Date());
			intfLogManager.saveIntfLog(intfLog);

			response.close();
			client.destroy();

		} else {
			intfLog.setErrMsg("主数据没有生成相应的JSON报文");
			intfLogManager.saveIntfLog(intfLog);
			return "主数据没有生成相应的JSON报文";
		}

		return null;

	}

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, IOException {

		String json = "{\"userId\" : 1001,\"userName\" : \"朱林涛\",\"age\" : null,\"sex\" : \"男\","
				+ "\"userRole\" : null,\"userCode\" : \"1001\"}";

		Client client = Client.create();

		client.setConnectTimeout(30 * 1000);

		WebResource webResource = client
				.resource("http://mail.chinatelecom.cn/mailinterface/inter");

		// MultivaluedMap queryParams = new MultivaluedMapImpl();
		// queryParams.add("param1", "val1");
		// queryParams.add("param2", "val2");

		ClientResponse response = webResource.entity(json, MediaType.TEXT_XML)
				.post(ClientResponse.class);

		String entity = response.getEntity(String.class);

		Object str = (Object) JacksonUtil.JSon2Object(entity, Object.class);

	}
}