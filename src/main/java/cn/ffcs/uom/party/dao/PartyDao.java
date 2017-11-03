package cn.ffcs.uom.party.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.dao.BaseDao;
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

public interface PartyDao extends BaseDao {

	/**
	 * 返回新增参与人角色的主键ID .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	public void addParty(Party party);

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
	 * 根据参与人标识获取参与人默认证件实体
	 * 
	 * @param partyId
	 * @return
	 * @author faq 2013-9-6 faq
	 */
	public PartyCertification getDefaultPartyCertification(Long partyId);

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
	 * 根据参与人标识获取参与人联系方式 .
	 * 
	 * @param id
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	public List<PartyContactInfo> getPartyContInfo(Long partyId);

	/**
	 * 根据证件类型和证件号码判断该证件是否被使用达到身份证使用上限
	 * 
	 * @param certType
	 *            证件类型
	 * @param certNumber
	 *            证件号码
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	public boolean checkIsExistCertificate(String certNumber);

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
	 * 
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	public void removePartyCertification(PartyCertification partyCertification);

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
	public List<Map<String, Object>> getListForAttrValue(String className,
			String attrName);

	/**
	 * 新增参与人证件 .
	 * 
	 * @param partyCerfi
	 * @author wangyong 2013-6-29 wangyong
	 */
	public void save(PartyCertification partyCerfi);

	/**
	 * 修改参与人证件 .
	 * 
	 * @param partyCerfi
	 * @author wangyong 2013-6-29 wangyong
	 */
	public void update(PartyCertification partyCerfi);

	/**
	 * 检查参与人证件是否是最后一个 .
	 * 
	 * @param partyCerfi
	 * @author wangyong 2013-6-29 wangyong
	 */
	public boolean isLastPartyCertification(Long partyId);

	/**
	 * 新增参与人联系人 .
	 * 
	 * @param partyContactInfo
	 * @author wangyong 2013-6-29 wangyong
	 */
	public void save(PartyContactInfo partyContactInfo);

	/**
	 * 修改参与人联系人 .
	 * 
	 * @param partyContactInfo
	 * @author wangyong 2013-6-29 wangyong
	 */
	public void update(PartyContactInfo partyContactInfo);

	/**
	 * 保存参与人个人信息 .
	 * 
	 * @param indiv
	 * @author wangyong 2013-6-29 wangyong
	 */
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

	public List<PartyContactInfo> getPartyContactInfo(
			PartyContactInfo queryContactInfo);

	public List<PartyContactInfo> queryDefaultPartyContactInfo(
			PartyContactInfo partyContactInfo);

	public Party getPartyByStaffAccount(String staffAccount);

	public int getPartyCertificationUsedMax();

	public CertIdVarInfo getCertIdVarInfo(String identity, String name);

}
