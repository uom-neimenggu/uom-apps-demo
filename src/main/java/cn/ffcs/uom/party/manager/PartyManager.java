package cn.ffcs.uom.party.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.model.CertIdVarInfo;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 参与人相关的管理接口 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-29
 * @功能说明：
 * 
 */
public interface PartyManager {
	/**
	 * 返回新增参与人角色的主键ID .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public void addParty(Party party) throws Exception;

	/**
	 * 修改参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public void updateParty(Party party);

	/**
	 * 根据ID删除参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public void delParty(Long id);

	/**
	 * 新增参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public void delParty(Party party);

	/**
	 * 检查参与人的名字是否被使用 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public boolean checkPartyNameExits(String partyName);

	/**
	 * 查询参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public Party queryParty(Long partyId);

	/**
	 * 根据参与人标识获取参与人证件实体 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	public List<PartyCertification> getPartyCerfion(Long partyId);

	/**
	 * 根据参与人标识获取参与人联系方式 .
	 * 
	 * @param id
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	public List<PartyContactInfo> getPartyContInfo(Long partyId);

	/**
	 * 根据参与人标识获取参与人默认证件实体
	 * 
	 * @param partyId
	 * @return
	 * @author faq 2013-9-6 faq
	 */
	public PartyCertification getDefaultPartyCertification(Long partyId);

	/**
	 * @author 朱林涛
	 */
	public List<PartyCertification> queryPartyCertificationList(Staff staff,
			PartyCertification pc);

	/**
	 * 根据参与人标识获取参与人首选联系人实体
	 * 
	 * @param partyId
	 * @return
	 * @author faq 2013-9-6 faq
	 */
	public PartyContactInfo getDefaultPartyContactInfo(Long partyId);

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
	public boolean checkIsExistCertificate(String certNumber);

	/**
	 * 
	 * .
	 * 
	 * @param party
	 * @param includeAll
	 * @param relaCd
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author Wong 2013-5-30 Wong
	 */
	public PageInfo forQuertyParty(Party party, int currentPage, int pageSize);

	/**
	 * 根据参与人标识获取个人信息 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	public Individual getIndividual(Long partyId);

	/**
	 * 获取参与人组织信息 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-6-3 Wong
	 */
	public PartyOrganization getPartyOrg(Long partyId);

	/**
	 * 获取所有参与人 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	public PageInfo getPartyContactInfos(PartyContactInfo pInfo,
			int activePage, int pageSize);

	/**
	 * 获取所有参与人证件 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	public PageInfo getPartyCertifications(PartyCertification paCerInfo,
			int activePage, int pageSize);

	/**
	 * 
	 * .
	 * 
	 * @param id
	 * @author Wong 2013-6-8 Wong
	 */
	public void removePartyCertification(Serializable id);

	/**
	 * 
	 * .
	 * 
	 * @param id
	 * @author Wong 2013-6-8 Wong
	 */
	public void removePartyContactInfo(Serializable id);

	/**
	 * 参与人证件删除，最少保留一个证件存在 .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	public boolean removePartyCertification(
			PartyCertification partyCertification);

	/**
	 * 
	 * .
	 * 
	 * @param partyContactInfo
	 * @author Wong 2013-6-8 Wong
	 */
	public void removePartyContactInfo(PartyContactInfo partyContactInfo);

	/**
	 * 员工组织关系 .
	 * 
	 * @param staffOrg
	 * @param activePag
	 * @param pageSize
	 * @author Wong 2013-6-8 Wong
	 */
	public PageInfo toQuertyStaffOrg(StaffOrganization staffOrg, int activePag,
			int pageSize);

	/**
	 * 根据ClassName attrName获取AttrValue .
	 * 
	 * @param className
	 * @param attrName
	 * @return
	 * @author wangyong 2013-6-15 wangyong
	 */
	public List<Map<String, String>> getListForAttrValue(String className,
			String attrName);

	public void save(PartyCertification partyCerfi);

	public void update(PartyCertification partyCerfi);

	public void save(PartyContactInfo partyContactInfo);

	public void update(PartyContactInfo partyContactInfo);

	public void save(Individual indiv);

	public void update(Individual indiv);

	public List<PartyRole> getPartyRoleByPtId(Long staffId);

	public void remove(UomEntity ue);

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	public void addPartyRole(PartyRole partyRole);

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	public void updatePartyRole(PartyRole partyRole);

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	public void removePartyRole(PartyRole partyRole);

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
	public PageInfo getPartyRolePageInfo(Party party, int currentPage,
			int pageSize);

	public void modStaffNameForPtyName(Party party);

	/**
	 * 参与人-新增、修改-增加手机号排重
	 * 
	 * @param partyContactInfo
	 * @return
	 * @author faq 2013-07-21 faq
	 */
	public boolean isExistsMobilePhone(PartyContactInfo partyContactInfo);

	/**
	 * 根据员工ID获取参与人信息
	 * 
	 * @param staffId
	 * @return
	 * @author faq 2013-07-22 faq
	 */
	public Party getPartyByStaff(Staff staff);

	/**
	 * 获取角色实体
	 * 
	 * @param id
	 * @return
	 * @author faq 2013-07-24 faq
	 */
	public PartyRole getPartyRole(Long partyRoleId);

	public PartyRole getPartyRoleByPartyId(Long partyId);

	/**
	 * 增加员工、参与人、组织....
	 * 
	 * @param party
	 * @throws Exception
	 */
	public void addPartyStaffOrganizations(List<Party> partys) throws Exception;
	
	/**
	 * 修改员工、参与人、组织....
	 * 
	 * @param party
	 * @throws Exception
	 */
	public void updatePartyStaffOrganizations(List<Party> partys) throws Exception;

	/**
	 * 当有新增数据到参与人时，获取人力中间表字段来改变参与人相关字段属性
	 * 
	 * @param party
	 * @return
	 */
	public Party changePartyFieldFromOperateHrWhileAdding(Party party)
			throws Exception;

	/**
	 * 修改组织参与人信息，只修改party的个人，组织的信息不好吧角色等
	 */
	public void updateOrganizationParty(Party party);

	/**
	 * 获取参与人证件
	 * 
	 * @param queryPartyCertification
	 * @return
	 */
	public List<PartyCertification> getPartyCertificationList(
			PartyCertification queryPartyCertification);

	/**
	 * 获取参与人联系方式
	 * 
	 * @param queryContactInfo
	 * @return
	 */
	public List<PartyContactInfo> getPartyContInfoList(
			PartyContactInfo queryContactInfo);

	/**
	 * 根据参与人证件信息或者参与人角色列表
	 * 
	 * @param queryPartyCertification
	 * @return
	 */
	public List<PartyRole> getPartyRoleListByPartyCertification(
			PartyCertification queryPartyCertification);

	/**
	 * 根据参与人证件信息或者参与人角色
	 * 
	 * @param queryPartyCertification
	 * @return
	 */
	public PartyRole getPartyRoleByPartyCertification(
			PartyCertification queryPartyCertification);

	public List<PartyContactInfo> getPartyContactInfo(
			PartyContactInfo queryContactInfo);

	/**
	 * 身份证验证
	 * 
	 * @param certNumber
	 * @return
	 */
	public boolean checkCertNumber(String certNumber);

	/**
	 * 验证参与人身份证是否存在并且参与人同名
	 * 
	 * @param certNumber
	 *            证件号
	 * @param partyNme
	 *            参与人姓名
	 * @return
	 */
	public boolean checkCertNumberAndParytNameIsExist(String certNumber,
			String partyNme);

	/**
	 * 侦测人力系统中的工号是否与主数据中的工号重复
	 * 
	 * @param pcf
	 * @return
	 */
	public String checkStaffAccount(PartyCertification pcf);

	/**
	 * 侦测人力系统中的工号是否与主数据中的工号重复
	 * 
	 * @param pcf
	 * @return
	 */
	public String checkStaffAccountNew(PartyCertification pcf, String partyName);

	public List<PartyContactInfo> queryDefaultPartyContactInfo(
			PartyContactInfo partyContactInfo);

	public String generateGrpUnEmail(Party party,
			List<Map<String, String>> mapList, Long tailNumber);

	public Party getPartyByStaffAccount(String staffAccount);

	public void saveOrEditOrDelPartyCertification(
			List<PartyCertification> addPartyCertificationList,
			List<PartyCertification> editPartyCertificationList,
			List<PartyCertification> delPartyCertificationList);

	public void delPartyStaffList(List<Party> delPartyList,
			List<Staff> delStaffList);

	public CertIdVarInfo getCertIdVarInfo(String identity, String name);
}
