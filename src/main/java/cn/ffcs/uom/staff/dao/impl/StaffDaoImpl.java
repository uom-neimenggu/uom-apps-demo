package cn.ffcs.uom.staff.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.position.model.CtPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.model.CtStaffPositionRef;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staff.model.StaffOrgTranTemp;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;
import cn.ffcs.uom.staff.model.StaffPosition;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.webservices.manager.IntfLogManager;

/**
 * 
 * . StaffManagerdao员工管理
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-28
 * @功能说明：
 * 
 */
@Repository("staffDao")
@Transactional
@SuppressWarnings({ "unchecked", "unused" })
public class StaffDaoImpl extends BaseDaoImpl implements StaffDao {

	@Resource
	private IntfLogManager intfLogManager;
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.staff.dao.StaffDao#addStaff(cn.ffcs.uom.staff.model.Staff)
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public Serializable addStaff(Staff staff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staff.setBatchNumber(batchNumber);
		staff.add();
		StaffAccount sffAcc = staff.getObjStaffAccount();
		if (!StrUtil.isNullOrEmpty(sffAcc.getStaffAccount())
				&& !StrUtil.isNullOrEmpty(sffAcc.getStaffAccount())) {
			sffAcc.setStaffId(staff.getStaffId());
			sffAcc.setBatchNumber(batchNumber);
			sffAcc.add();
		}

		List<StaffExtendAttr> staffExtendAttrList = staff.getStaffExtendAttr();

		if (null != staffExtendAttrList && staffExtendAttrList.size() > 0) {

			for (StaffExtendAttr staffExtendAttr : staffExtendAttrList) {
				staffExtendAttr.setStaffId(staff.getStaffId());
				staffExtendAttr.setBatchNumber(batchNumber);
				staffExtendAttr.add();
			}

		}

		return null;

	}

	@Override
	public void delStaff(Serializable id) {

	}

	@Override
	public void delStaff(Staff staff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		Long LStaffId = staff.getStaffId();
		staff.setBatchNumber(batchNumber);
		staff.remove();
		
		String reason = staff.getReason();
		
		/**
		 * 删除员工时删除人力中间表 已过期
		 * 
		 * String sql =
		 * "UPDATE OPERATE_HR_TABLE_01 SET CURRENT_STATUS=? WHERE STAFF_ID=?";
		 * List params = new ArrayList();
		 * params.add(OperateHrConstant.STATUS_DEL); params.add(LStaffId);
		 * this.executeUpdateByJdbcAndParams(sql, params);
		 */
		List<StaffOrganization> staObj = (List<StaffOrganization>) getObjectByStatus(
				StaffOrganization.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffOrganization so : staObj) {
			so.setBatchNumber(batchNumber);
			so.setReason(reason);
			so.remove();
		}
		List<StaffPosition> spObj = (List<StaffPosition>) getObjectByStatus(
				StaffPosition.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffPosition so : spObj) {
			so.setBatchNumber(batchNumber);
			so.remove();
		}

		List<StaffAccount> sfAcc = (List<StaffAccount>) getObjectByStatus(
				StaffAccount.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffAccount so : sfAcc) {
			so.setBatchNumber(batchNumber);
			so.remove();
		}

		List<StaffOrganizationTran> sffOrgTr = (List<StaffOrganizationTran>) getObjectByStatus(
				StaffOrganizationTran.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffOrganizationTran so : sffOrgTr) {
			so.setBatchNumber(batchNumber);
			so.remove();
		}

		List<StaffExtendAttr> sffExAtt = (List<StaffExtendAttr>) getObjectByStatus(
				StaffExtendAttr.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffExtendAttr so : sffExAtt) {
			so.setBatchNumber(batchNumber);
			so.remove();
		}

		List<StaffRoleRela> srrObj = (List<StaffRoleRela>) getObjectByStatus(
				StaffRoleRela.class, LStaffId,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		for (StaffRoleRela srr : srrObj) {
			srr.setBatchNumber(batchNumber);
			srr.removeOnly();
		}
		// staffOrgPosiTran("STAFF_ORGANIZATION_TRAN",new Object[]{LStaffId});
	}

	@Override
	public Staff queryStaff(Serializable id) {

		List<Staff> liStaff = null;
		if (!StrUtil.isNullOrEmpty(id)) {
			String hql = " from Staff u where u.staffId=? and u.statusCd = ?";
			liStaff = getHibernateTemplate().find(hql,
					new Object[] { id, BaseUnitConstants.ENTT_STATE_ACTIVE });
		}
		if (null == liStaff || liStaff.size() == 0) {
			return null;
		}
		return liStaff.get(0);
	}

	public Staff queryStaff2(Serializable id) {

		if (StrUtil.isNullOrEmpty(id)) {
			return null;
		}
		String hql = " from Staff u where u.staffId=? and u.statusCd = ?";
		List<Staff> liStaff = getHibernateTemplate().find(hql,
				new Object[] { id, BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (null == liStaff || liStaff.size() == 0) {
			return null;
		}
		return liStaff.get(0);
	}

	@Override
	public void updateStaff(Staff staff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		if (null != staff.getObjStaffAccount().getStaffAccountId()) {
			staff.getObjStaffAccount().setBatchNumber(batchNumber);
			staff.getObjStaffAccount().update();
		} else {
			staff.getObjStaffAccount().setBatchNumber(batchNumber);
			staff.getObjStaffAccount().add();
		}

		List<StaffExtendAttr> staffExtendAttrList = staff.getStaffExtendAttr();

		if (null != staffExtendAttrList && staffExtendAttrList.size() > 0) {
			for (StaffExtendAttr staffExtendAttr : staffExtendAttrList) {
				if (null == staffExtendAttr.getStaffExtendAttrId()) {
					staffExtendAttr.setStaffId(staff.getStaffId());
					staffExtendAttr.setBatchNumber(batchNumber);
					staffExtendAttr.add();
				} else {
					staffExtendAttr.setBatchNumber(batchNumber);
					staffExtendAttr.update();
				}
			}
		}
		staff.setBatchNumber(batchNumber);
		staff.update();
	}

	public PartyRole getPartyRole(Long partyRoleId) {
		String hql = "from PartyRole s where s.partyRoleId = ?";
		List<PartyRole> liPartyRole = getHibernateTemplate().find(hql,
				new Object[] { partyRoleId });
		if (null != liPartyRole && liPartyRole.size() > 0) {
			return liPartyRole.get(0);
		}
		return null;
	}

	/**
	 * 获取生效的员工
	 * 
	 * @param staffId
	 * @param partyRoleId
	 * @return
	 * @author fangy 2016年12月14日 fangy
	 */
	public Staff getStaff(Long staffId, Long partyRoleId) {
		String hql = "from Staff s where s.staffId != ? and s.partyRoleId = ? and s.statusCd = ?";
		List<Staff> liStaff = getHibernateTemplate().find(
				hql,
				new Object[] { staffId, partyRoleId,
						BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (null != liStaff && liStaff.size() > 0) {
			return liStaff.get(0);
		}
		return null;
	}

	public Party getParty(Long partyId) {
		String hql = "from Party s where s.partyId = ?";
		List<Party> liParty = getHibernateTemplate().find(hql,
				new Object[] { partyId });
		if (null != liParty && liParty.size() > 0) {
			return liParty.get(0);
		}
		return null;
	}

	public List<PartyCertification> getPartyCertification(
			PartyCertification partyCertification) {
		String hql = " from PartyCertification a where a.partyId != ? and a.certType = ? and a.certNumber = ? and a.statusCd = ?";
		return getHibernateTemplate().find(
				hql,
				new Object[] { partyCertification.getPartyId(),
						partyCertification.getCertType(),
						partyCertification.getCertNumber(),
						BaseUnitConstants.ENTT_STATE_ACTIVE });
	}

	public List<PartyContactInfo> getMaxPartyContactInfo(Long partyId) {
		String hql = " from PartyContactInfo a where a.partyId = ? and a.headFlag = ? and a.contactId = (select max(contactId) from PartyContactInfo b where b.partyId = ? and b.headFlag = ?)";
		return getHibernateTemplate().find(
				hql,
				new Object[] { partyId, SffOrPtyCtants.HEADFLAG, partyId,
						SffOrPtyCtants.HEADFLAG });
	}

	public List<StaffOrganization> getMaxMainSffOrg(Long staffId) {
		String hql = " from StaffOrganization a where a.staffId = ? and a.ralaCd = ? and a.staffOrgId = (select max(staffOrgId) from StaffOrganization b where b.staffId = ? and b.ralaCd = ?)";
		return getHibernateTemplate().find(
				hql,
				new Object[] { staffId, BaseUnitConstants.RALA_CD_1, staffId,
						BaseUnitConstants.RALA_CD_1 });
	}

	@Override
	public List<?> getActivationObjNoStatusCd(Class<?> clazz, Long partyId) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
				.append("T.partyId=").append(partyId);
		return getHibernateTemplate().find(sBhql.toString());
	}

	@Override
	public String updateStaffList(List<Staff> staffList) {

		String msg = "";

		try {

			for (Staff staff : staffList) {

				boolean sign = false;
				Party party = null;
				PartyRole partyRole = null;
				String batchNumber = OperateLog.gennerateBatchNumber();

				if (null != staff.getStaffId()
						&& null != staff.getPartyRoleId()) {

					partyRole = this.getPartyRole(staff.getPartyRoleId());

					if (null != partyRole && null != partyRole.getPartyId()) {

						party = this.getParty(partyRole.getPartyId());

						if (BaseUnitConstants.ENTT_STATE_ACTIVE
								.equals(partyRole.getStatusCd())
								&& BaseUnitConstants.ENTT_STATE_ACTIVE
										.equals(party.getStatusCd())) {
							Staff staffEx = this.getStaff(staff.getStaffId(),
									partyRole.getPartyRoleId());
							if (null != staffEx) {
								msg += "员工" + staff.getStaffName()
										+ "关联的参与人已经被其他员工使用！\n";
								party = null;
								partyRole = null;
								continue;
							}

						} else if (BaseUnitConstants.ENTT_STATE_INACTIVE
								.equals(partyRole.getStatusCd())) {
							if (BaseUnitConstants.ENTT_STATE_INACTIVE
									.equals(party.getStatusCd())) {

								List<PartyCertification> partyCertificationList = (List<PartyCertification>) getActivationObjNoStatusCd(
										PartyCertification.class,
										party.getPartyId());
								//
								// for (PartyCertification partyCertification :
								// partyCertificationList) {
								//
								// if (!StrUtil.isEmpty(partyCertification
								// .getCertType())
								// && partyCertification.getCertType()
								// .equals("1")) {
								// if
								// (this.getPartyCertification(partyCertification)
								// != null
								// && this.getPartyCertification(
								// partyCertification)
								// .size() > 0) {
								// msg += "员工" + staff.getStaffName()
								// + "关联的证件号重复！\n";
								// party = null;
								// partyRole = null;
								// sign = true;// 证件号重复为true，不重复为false
								// break;
								// }
								// }
								// }
								//
								// if (sign) {// 证件号重复为true
								// sign = false;
								// continue;
								// } else {// 证件号不重复false
								for (PartyCertification partyCertification : partyCertificationList) {
									String idNum = partyCertification.getCertNumber();
									String idName = partyCertification.getCertName();
									if (!PartyConstant.PARTY_CERTIFICATION_IS_REAL_NAME_Y
											.equals(partyCertification.getIsRealName())
										&& PartyConstant.ATTR_VALUE_IDNO.equals(partyCertification
											.getCertType())) {
										return "该员工未实名,请实名后再激活！";
									}
								}
								for (PartyCertification partyCertification : partyCertificationList) {
									if (partyCertification.getStatusCd().equals(
										BaseUnitConstants.ENTT_STATE_INACTIVE)) {
										partyCertification
											.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
										partyCertification.setBatchNumber(batchNumber);
										partyCertification.update();
									}
								}
								
								party.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								party.setBatchNumber(batchNumber);
								party.update();
								
								partyRole.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								partyRole.setBatchNumber(batchNumber);
								partyRole.update();
								
								List<PartyContactInfo> partyContactInfoList = (List<PartyContactInfo>) getMaxPartyContactInfo(party
									.getPartyId());

								for (PartyContactInfo partyContactInfo : partyContactInfoList) {
									if (partyContactInfo.getStatusCd().equals(
										BaseUnitConstants.ENTT_STATE_INACTIVE)) {
										partyContactInfo
											.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
										partyContactInfo.setBatchNumber(batchNumber);
										partyContactInfo.update();
									}
								}

								List<Individual> individualList = (List<Individual>) getActivationObjNoStatusCd(
										Individual.class, party.getPartyId());

								for (Individual individual : individualList) {
									if (individual
											.getStatusCd()
											.equals(BaseUnitConstants.ENTT_STATE_INACTIVE)) {
										individual
												.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
										individual.setBatchNumber(batchNumber);
										individual.update();
									}
								}

								// }

							}

						}

						List<StaffAccount> staffAccountList = (List<StaffAccount>) getObjectByStatus(
								StaffAccount.class, staff.getStaffId(),
								BaseUnitConstants.ENTT_STATE_INACTIVE);

						for (StaffAccount staffAccount : staffAccountList) {

							if (isExistStaffAccount(staffAccount)) {// 判断是否存在相同的账号，相同则新建人力号
								String staffNbr = gennerateStaffNumber(staff);
								if (!StrUtil.isNullOrEmpty(staffNbr)) {// 人力号与账号相同
									staff.setStaffNbr(staffNbr);
									staffAccount.setStaffAccount(staffNbr);
								}
							}

							if (staffAccount.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								staffAccount
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								staffAccount.setBatchNumber(batchNumber);
								staffAccount.update();
							}
						}

						List<StaffExtendAttr> staffExtendAttrList = (List<StaffExtendAttr>) getActivationObj(
								StaffExtendAttr.class, staff.getStaffId());

						for (StaffExtendAttr staffExtendAttr : staffExtendAttrList) {
							if (staffExtendAttr.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								staffExtendAttr
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								staffExtendAttr.setBatchNumber(batchNumber);
								staffExtendAttr.update();
							}
						}

						staff.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
						staff.setBatchNumber(batchNumber);
						staff.update();

						// 20161215增加激活员工的时候恢复员工最近的一条主归属组织关系
						List<StaffOrganization> staffOrganizationList = getMaxMainSffOrg(staff
								.getStaffId());
						for (StaffOrganization staffOrganization : staffOrganizationList) {
							if (staffOrganization.getStatusCd().equals(
									BaseUnitConstants.ENTT_STATE_INACTIVE)) {
								staffOrganization
										.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
								staffOrganization.setBatchNumber(batchNumber);
								staffOrganization.update();
							}
						}
					}
					party = null;
					partyRole = null;
				} else {
					msg += "员工" + staff.getStaffName() + "没有关联的参与人！\n";
					party = null;
					partyRole = null;
				}
			}
			return msg;
		} catch (Exception e) {
			msg = e.getMessage();
			return msg;

		}
	}

	public String gennerateStaffNumber(Staff staff) {
		if (!StrUtil.isNullOrEmpty(staff.getWorkProp())) {
			String staffSeq = this.getSeqStaffNbr();
			if (!StrUtil.isNullOrEmpty(staffSeq)) {
				if (SffOrPtyCtants.WORKPROP_W_AGENT.equals(staff.getWorkProp())
						|| SffOrPtyCtants.WORKPROP_W_OTHER.equals(staff
								.getWorkProp())
						|| SffOrPtyCtants.WORKPROP_W_PROVIDER.equals(staff
								.getWorkProp())) {
					return "H8" + staffSeq;
				} else {
					return "L7" + staffSeq;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 CREATE OR REPLACE VIEW VIEW_STAFFS AS SELECT
	 * C2.*,C4.ROLE_TYPE,C3.STAFF_PASSWORD,C3.STAFF_ACCOUNT,C3.GUID, (SELECT
	 * B0.LOCATION_NAME FROM POLITICAL_LOCATION B0 WHERE
	 * B0.LOCATION_ID=C2.LOCATION_ID) LOC_NAME FROM PARTY_ROLE C4,STAFF_ACCOUNT
	 * C3,STAFF C2 WHERE C2.STATUS_CD =1000 AND C3.STATUS_CD =1000 AND
	 * C4.STATUS_CD =1000 AND C4.PARTY_ROLE_ID=C2.PARTY_ROLE_ID AND
	 * C3.staff_id=c2.staff_id
	 * 
	 */
	public PageInfo forQuertyStaff(Staff staff, int currentPage, int pageSize) {

		Map<String, Object> mp = getStaffByCotantSQL(staff);
		if (null != mp) {
			return super.jdbcFindPageInfo((String) mp.get("sql"),
					(List<Object>) mp.get("params"), currentPage, pageSize,
					Staff.class);
		}

		return null;
	}

	/**
	 * 组装员工查询SQL
	 * 
	 * @param staff
	 * @return
	 */
	private Map<String, Object> getStaffByCotantSQL(Staff staff) {
		Map<String, Object> mp = new HashMap<String, Object>();
		try {
			List<Object> params = new ArrayList<Object>();
			StringBuffer sb = new StringBuffer();

			List<Organization> li_og = staff.getPermissionOrganizationList();
			if (null != li_og) {
				if (1 == li_og.size()
						&& OrganizationConstant.ROOT_TREE_PARENT_ORG_ID
								.equals(li_og.get(0).getOrgId())) {
					sb.append(SffOrPtyCtants.SQL_QUERY_ADMIN);
				} else {
					sb.append("SELECT Z.* FROM (WITH WH_RE_OR AS( ")
							.append("SELECT M.ORG_ID FROM ( ")
							.append("SELECT T0.ORG_ID,T0.RELA_ORG_ID FROM ORGANIZATION_RELATION T0 WHERE T0.STATUS_CD=1000 AND T0.Rela_Cd=?) M ")
							.append("START WITH ");

					params.add(OrganizationConstant.RELA_CD_INNER);

					if (li_og.size() == 1
							&& OrganizationConstant.ROOT_TREE_PARENT_ORG_ID
									.equals(li_og.get(0).getOrgId())) {
						sb.append("M.RELA_ORG_ID IN ( ");
					} else {
						sb.append("M.ORG_ID IN ( ");
					}

					for (int i = 0; i < li_og.size(); i++) {
						if (i == 0) {
							sb.append("? ");
						} else {
							sb.append(",? ");
						}
						params.add(li_og.get(i).getOrgId());
					}

					sb.append(
							")CONNECT BY NOCYCLE  PRIOR M.ORG_ID =  M.RELA_ORG_ID) ")
							.append("SELECT DISTINCT V1.*, C1.ORG_ID FROM VIEW_STAFFS V1,STAFF_ORGANIZATION C1,WH_RE_OR C0  ")
							.append("WHERE V1.STAFF_ID=C1.STAFF_ID AND C1.STATUS_CD = 1000 AND C1.RALA_CD = 1 AND C0.ORG_ID=C1.ORG_ID ")
							.append("UNION SELECT V1.*, -1 FROM VIEW_STAFFS V1 WHERE NOT EXISTS ")
							.append("(SELECT * FROM STAFF_ORGANIZATION C2 WHERE C2.Status_Cd=1000 AND V1.STAFF_ID = C2.STAFF_ID)) Z WHERE 1=1 ");
				}

			} else { // admin 用户
				sb.append(SffOrPtyCtants.SQL_QUERY_ADMIN);
			}

			if (null != staff.getObjStaffAccount()
					&& !StrUtil.isNullOrEmpty(staff.getObjStaffAccount()
							.getStaffAccount())) {
				sb.append(" AND Z.STAFF_ACCOUNT=?");
				params.add(staff.getObjStaffAccount().getStaffAccount());
			}

			String staffName = staff.getStaffName();
			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sb.append(" AND Z.STAFF_NAME LIKE ?");
				params.add("%" + staffName + "%");
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffCode())) {
				sb.append(" AND Z.STAFF_CODE =?");
				params.add(staff.getStaffCode());
			}

			mp.put("sql", sb.toString());
			mp.put("params", params);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mp;
	}

	public List<Staff> quertyStaffNoPage(Staff staff) {
		Map<String, Object> mp = getStaffByCotantSQL(staff);
		if (null != mp) {
			return super.jdbcFindList((String) mp.get("sql"),
					(List<Object>) mp.get("params"), Staff.class);
		}
		return null;
	}

	/**
	 * 分页查询失效员工信息
	 * 
	 * @param staff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo forQuertyStaffActivation(Staff staff, int currentPage,
			int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select distinct * from (SELECT A.*,B.LOCATION_NAME ,C.ROLE_TYPE,D.STAFF_PASSWORD,D.STAFF_ACCOUNT,E.ORG_ID")
				.append(" FROM STAFF A LEFT JOIN POLITICAL_LOCATION B ON (A.LOCATION_ID=B.LOCATION_ID AND A.STATUS_CD=B.STATUS_CD)")
				.append(" LEFT JOIN PARTY_ROLE C ON (A.PARTY_ROLE_ID=C.PARTY_ROLE_ID AND A.STATUS_CD=C.STATUS_CD)")
				.append(" LEFT JOIN STAFF_ACCOUNT D ON (A.STAFF_ID=D.STAFF_ID AND A.STATUS_CD=D.STATUS_CD)")
				.append(" LEFT JOIN STAFF_ORGANIZATION E ON (A.STAFF_ID=E.STAFF_ID AND E.RALA_CD=?")
				.append(" AND A.STATUS_CD=E.STATUS_CD)")
				.append("WHERE A.STATUS_CD=?");

		params.add(BaseUnitConstants.RALA_CD_1);
		params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
		/*
		 * 员工账号
		 */
		StaffAccount sc = staff.getObjStaffAccount();
		if (null != sc) {
			String staffAcc = sc.getStaffAccount();
			if (!StrUtil.isNullOrEmpty(staffAcc)) {
				sb.append(" AND A.STAFF_ID  IN (SELECT ST.STAFF_ID FROM STAFF_ACCOUNT ST WHERE ST.STATUS_CD=? AND ST.STAFF_ACCOUNT=?)");
				params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
				params.add(StringEscapeUtils.escapeSql(staffAcc));
			}
		}

		String staffName = staff.getStaffName();
		if (!StrUtil.isNullOrEmpty(staffName)) {
			sb.append(" AND A.STAFF_NAME LIKE ?");
			params.add("%" + StringEscapeUtils.escapeSql(staffName) + "%");
		}

		String staffCode = staff.getStaffCode();
		if (!StrUtil.isNullOrEmpty(staffCode)) {
			sb.append(" AND A.STAFF_CODE = ?");
			params.add(StringEscapeUtils.escapeSql(staffCode));
		}

		String workProp = staff.getWorkProp();
		if (!StrUtil.isNullOrEmpty(workProp)) {
			sb.append(" AND A.WORK_PROP = ?");
			params.add(StringEscapeUtils.escapeSql(workProp));
		}
		sb.append(" order by a.eff_Date desc)");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, Staff.class);
	}

	@Override
	public PageInfo forQuertyCompareStaff(Staff staff, int currentPage,
			int pageSize) {
		List<Object> params = new ArrayList<Object>();
		// append(" FROM OPERATE_HR_TABLE_01 C LEFT JOIN STAFF A ON (C.STAFF_ID=A.STAFF_ID AND A.STAFF_NBR!=C.PSNCODE)").
		StringBuffer sb = new StringBuffer();
		sb.append(
				"SELECT A.*, B.STAFF_PASSWORD,B.STAFF_ACCOUNT,C.PSNCODE AS HRSTAFFNBR,C.UPDATE_STAFF_ACCOUNT AS HRSTAFFACCOUNT")
				.append(" FROM OPERATE_HR_TABLE_01 C LEFT JOIN STAFF A ON C.STAFF_ID=A.STAFF_ID")
				.append(" LEFT JOIN STAFF_ACCOUNT B ON (A.STAFF_ID=B.STAFF_ID)")
				.append(" WHERE A.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		StaffAccount sc = staff.getObjStaffAccount();
		if (null != sc) {
			String staffAcc = sc.getStaffAccount();
			if (!StrUtil.isNullOrEmpty(staffAcc)) {
				sb.append(" AND A.STAFF_ID  IN (SELECT ST.STAFF_ID FROM STAFF_ACCOUNT ST WHERE ST.STATUS_CD=? AND ST.STAFF_ACCOUNT=?)");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(StringEscapeUtils.escapeSql(staffAcc));
			}
		}
		String staffName = staff.getStaffName();
		if (!StrUtil.isNullOrEmpty(staffName)) {
			sb.append(" AND A.STAFF_NAME LIKE ?");
			params.add("%" + StringEscapeUtils.escapeSql(staffName) + "%");
		}
		String staffCode = staff.getStaffCode();
		if (!StrUtil.isNullOrEmpty(staffCode)) {
			sb.append(" AND A.STAFF_CODE = ?");
			params.add(StringEscapeUtils.escapeSql(staffCode));
		}
		String currentStatus = staff.getCurrentStatus();
		if (!StrUtil.isNullOrEmpty(currentStatus)) {
			sb.append(" AND C.CURRENT_STATUS = ?");
			params.add(StringEscapeUtils.escapeSql(currentStatus));
		}
		OrganizationRelation orgRelation = staff.getOrganizationRelation();
		if (!StrUtil.isNullOrEmpty(orgRelation)) {
			sb.append(" AND A.STAFF_ID IN (SELECT SO.STAFF_ID FROM STAFF_ORGANIZATION SO WHERE SO.STATUS_CD=? AND SO.ORG_ID=?)");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(orgRelation.getOrgId());
		}
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, Staff.class);
	}
	
	@Override
	public List<StaffAccount> queryStaffAccountByCertNum(String certNum) {
		String sql = new String(
				"SELECT D.*  FROM PARTY_CERTIFICATION A, PARTY_ROLE B, STAFF C, STAFF_ACCOUNT D WHERE A.PARTY_ID = B.PARTY_ID "
				+ "AND B.PARTY_ROLE_ID = C.PARTY_ROLE_ID AND C.STAFF_ID = D.STAFF_ID AND A.CERT_NUMBER = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(StringEscapeUtils.escapeSql(certNum));
		
		List<StaffAccount> list = jdbcFindList(sql, params, StaffAccount.class);
		
		return list;
	}

	/**
	 * 查询员工账号 .
	 * 
	 * @param id
	 * @param staffId
	 * @return
	 * @author Wong 2013-6-4 Wong
	 */
	public StaffAccount getStaffAccount(Serializable id, Long staffId) {
		String hql = null;
		List<StaffAccount> liSaff = null;
		if (!StrUtil.isNullOrEmpty(id)) {
			hql = "from StaffAccount u where staffAccountId=?";
			liSaff = getHibernateTemplate().find(hql, new Object[] { id });
		}
		if (null == liSaff || liSaff.size() == 0) {
			if (!StrUtil.isNullOrEmpty(staffId)) {
				hql = "from StaffAccount u Where  u.staffId=?";
				liSaff = getHibernateTemplate().find(hql,
						new Object[] { staffId });
			}
		}
		if (null == liSaff || liSaff.size() == 0) {
			return null;
		}

		return liSaff.get(0);
	}

	public String getPartyNameByStaffId(Serializable staffId) {
		String sql = "SELECT C.STAFF_NAME FROM STAFF C, PARTY_ROLE A, PARTY B WHERE A.PARTY_ID = B.PARTY_ID AND C.PARTY_ROLE_ID = A.PARTY_ROLE_ID AND C.STAFF_ID=?";
		return (String) getJdbcTemplate().queryForObject(sql,
				new Object[] { staffId }, String.class);
	}

	@Override
	public PageInfo queryPageInfoByStaffPosition(Position queryPosition,
			Organization queryOrganization, Staff staff, int current,
			int pageSize) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_POSITION WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryPosition != null && queryOrganization != null && staff != null) {

			if (staff.getStaffId() != null) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staff.getStaffId());
			}

			sb.append(" AND ORG_POSITION_RELA_ID IN (SELECT ORG_POSITION_ID FROM ORG_POSITION WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			sb.append(" AND POSITION_ID IN (SELECT POSITION_ID FROM POSITION WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(queryPosition.getPositionName())) {
				sb.append(" AND POSITION_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryPosition.getPositionName().trim()) + "%");
			}

			sb.append(") AND ORG_ID IN (SELECT ORG_ID FROM ORGANIZATION WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(queryOrganization.getOrgName())) {
				sb.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryOrganization.getOrgName().trim()) + "%");
			}

			sb.append("))");
			sb.append(" ORDER BY STAFF_POSITION_ID");

		}
		return this.jdbcFindPageInfo(sb.toString(), params, current, pageSize,
				StaffPosition.class);
	}

	/**
	 * 员工组织业务关系 .
	 * 
	 * @param staff
	 * @param current
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoStaffOrgTran(Staff staff,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {

		StringBuffer sql = new StringBuffer(
				"SELECT SOT.*, SA.STAFF_ACCOUNT FROM STAFF_ORGANIZATION_TRAN SOT LEFT JOIN STAFF_ACCOUNT SA ON SOT.STAFF_ID = SA.STAFF_ID  WHERE SOT.STATUS_CD = ? AND SA.STATUS_CD = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staff != null && staffOrganizationTran != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sql.append(" AND SA.STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffAccount().trim()) + "%");
			}

			if (staffOrganizationTran.getOrgId() != null) {
				sql.append(" AND SOT.ORG_ID= ?");
				params.add(staffOrganizationTran.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getRalaCd())) {
				sql.append(" AND SOT.RALA_CD= ?");
				params.add(staffOrganizationTran.getRalaCd());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getStaffSort())) {
				sql.append(" AND STAFF_SORT = ?");
				params.add(staffOrganizationTran.getStaffSort());
			}

			sql.append(" AND SOT.STAFF_ID IN (SELECT ST.STAFF_ID FROM STAFF ST WHERE ST.STATUS_CD= ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffOrganizationTran.getStaffId() != null) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staffOrganizationTran.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sql.append(" AND ST.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName().trim()) + "%");
			}

			sql.append(")");

		}

		sql.append(" ORDER BY STAFF_SORT ASC,RALA_CD DESC");

		return StaffOrganization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, StaffOrganizationTran.class);

	}

	/**
	 * 员工组织业务关系 .
	 * 
	 * @param staff
	 * @param current
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoStaffGridUnitTran(Staff staff,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {

		StringBuffer sql = new StringBuffer(
				"SELECT SOT.*,SA.STAFF_ACCOUNT,GU.AREA_NAME,GU.SUBAREA_NAME,GU.MME_FID,GU.GRID_NAME FROM STAFF_ORGANIZATION_TRAN SOT LEFT JOIN STAFF_ACCOUNT SA  ON SOT.STAFF_ID = SA.STAFF_ID");
		sql.append(" LEFT JOIN GRID_UNIT GU ON SOT.ORG_ID = GU.GRID_UNIT_ID WHERE SOT.STATUS_CD = ? AND SA.STATUS_CD = ? AND GU.STATUS_CD = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staff != null && staffOrganizationTran != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sql.append(" AND SA.STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffAccount().trim()) + "%");
			}

			if (staffOrganizationTran.getOrgId() != null) {
				sql.append(" AND SOT.ORG_ID = ?");
				params.add(staffOrganizationTran.getOrgId());
			}

			if (staffOrganizationTran.getGridName() != null) {
				sql.append(" AND GU.GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrganizationTran.getGridName()) + "%");
			}

			if (staffOrganizationTran.getMmeFid() != null) {
				sql.append(" AND GU.MME_FID = ?");
				params.add(staffOrganizationTran.getMmeFid());
			}

			sql.append(" AND GU.AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (staffOrganizationTran.getPermissionTelcomRegion() != null
					&& staffOrganizationTran.getPermissionTelcomRegion()
							.getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(staffOrganizationTran
									.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(staffOrganizationTran
									.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sql.append("  AND CASCADE_VALUE = ?");
				params.add(staffOrganizationTran.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sql.append(")");

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getRalaCd())) {
				sql.append(" AND SOT.RALA_CD = ?");
				params.add(staffOrganizationTran.getRalaCd());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getStaffSort())) {
				sql.append(" AND STAFF_SORT = ?");
				params.add(staffOrganizationTran.getStaffSort());
			}

			sql.append(" AND SOT.STAFF_ID IN (SELECT ST.STAFF_ID FROM STAFF ST WHERE ST.STATUS_CD= ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffOrganizationTran.getStaffId() != null) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staffOrganizationTran.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sql.append(" AND ST.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName().trim()) + "%");
			}

			sql.append(")");

		}

		sql.append(" ORDER BY STAFF_SORT ASC,RALA_CD DESC");

		return StaffOrganization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, StaffOrganizationTran.class);

	}
	
	/**
	 * 员工组织业务关系列表查询
	 */
	@Override
	public List<Map<String, Object>> queryStaffGridUnitTranList(Staff staff,
			StaffOrganizationTran staffOrganizationTran) {

		StringBuffer sql = new StringBuffer(
				"SELECT GU.GRID_UNIT_ID 网格单元ID,GU.AREA_NAME 分公司,GU.SUBAREA_NAME 子公司,GU.MME_FID 网格单元标识,GU.GRID_NAME 网格单元名称,SC.ATTR_VALUE_NAME 网格单元维护人类型,S.STAFF_CODE 设备维护人,SA.STAFF_ACCOUNT 设备维护人的工号,S.STAFF_NAME 设备维护人人的姓名,SA.STAFF_ID 设备维护人对应员工标识,to_char(SOT.CREATE_DATE, 'yyyy-mm-dd hh24:mi:ss') 生效时间,SOT.STAFF_SORT 维护人序号    "
				+ "FROM STAFF_ORGANIZATION_TRAN SOT LEFT JOIN STAFF_ACCOUNT SA  ON SOT.STAFF_ID = SA.STAFF_ID LEFT JOIN STAFF S ON SOT.STAFF_ID = S.STAFF_ID");
		sql.append(" LEFT JOIN (select c.* from sys_class a, attr_spec b, attr_value c where a.class_id = b.class_id and b.attr_id = c.attr_id and a.java_code = 'StaffOrganizationTran' and b.java_code = 'ralaCd') SC ON SC.ATTR_VALUE = SOT.RALA_CD");
		sql.append(" LEFT JOIN GRID_UNIT GU ON SOT.ORG_ID = GU.GRID_UNIT_ID WHERE SOT.STATUS_CD = ? AND SA.STATUS_CD = ? AND GU.STATUS_CD = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staff != null && staffOrganizationTran != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sql.append(" AND SA.STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffAccount().trim()) + "%");
			}

			if (staffOrganizationTran.getOrgId() != null) {
				sql.append(" AND SOT.ORG_ID = ?");
				params.add(staffOrganizationTran.getOrgId());
			}

			if (staffOrganizationTran.getGridName() != null) {
				sql.append(" AND GU.GRID_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrganizationTran.getGridName()) + "%");
			}

			if (staffOrganizationTran.getMmeFid() != null) {
				sql.append(" AND GU.MME_FID = ?");
				params.add(staffOrganizationTran.getMmeFid());
			}

			sql.append(" AND GU.AREA_EID IN (SELECT RELA_CASCADE_VALUE FROM CASCADE_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(CascadeRelationConstants.RELA_CD_6);

			if (staffOrganizationTran.getPermissionTelcomRegion() != null
					&& staffOrganizationTran.getPermissionTelcomRegion()
							.getTelcomRegionId() != null
					&& !TelecomRegionConstants.ROOT_TELECOM_REGION_ID
							.equals(staffOrganizationTran
									.getPermissionTelcomRegion()
									.getTelcomRegionId())
					&& !TelecomRegionConstants.AH_TELECOM_REGION_ID
							.equals(staffOrganizationTran
									.getPermissionTelcomRegion()
									.getTelcomRegionId())) {
				sql.append("  AND CASCADE_VALUE = ?");
				params.add(staffOrganizationTran.getPermissionTelcomRegion()
						.getTelcomRegionId());
			}

			sql.append(")");

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getRalaCd())) {
				sql.append(" AND SOT.RALA_CD = ?");
				params.add(staffOrganizationTran.getRalaCd());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganizationTran.getStaffSort())) {
				sql.append(" AND STAFF_SORT = ?");
				params.add(staffOrganizationTran.getStaffSort());
			}

			sql.append(" AND SOT.STAFF_ID IN (SELECT ST.STAFF_ID FROM STAFF ST WHERE ST.STATUS_CD= ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffOrganizationTran.getStaffId() != null) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staffOrganizationTran.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sql.append(" AND ST.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName().trim()) + "%");
			}

			sql.append(")");

		}

		sql.append(" ORDER BY STAFF_SORT ASC,RALA_CD DESC");

		return this.getJdbcTemplate().queryForList(sql.toString(),params.toArray());

	}

	@Override
	public List<StaffOrganizationTran> queryStaffOrgTranList(
			StaffOrganizationTran staffOrgTran) {

		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_ORGANIZATION_TRAN WHERE STATUS_CD = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staffOrgTran != null) {

			if (staffOrgTran.getStaffOrgTranId() != null) {
				sb.append(" AND STAFF_ORG_TRAN_ID = ?");
				params.add(staffOrgTran.getStaffOrgTranId());
			}

			if (staffOrgTran.getStaffId() != null) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staffOrgTran.getStaffId());
			}

			if (staffOrgTran.getOrgId() != null) {
				sb.append(" AND ORG_ID = ?");
				params.add(staffOrgTran.getOrgId());
			}

			if (!StrUtil.isEmpty(staffOrgTran.getRalaCd())) {
				sb.append(" AND RALA_CD = ?");
				params.add(staffOrgTran.getRalaCd());
			}

			if (!StrUtil.isNullOrEmpty(staffOrgTran.getStaffSort())) {
				sb.append(" AND STAFF_SORT = ?");
				params.add(staffOrgTran.getStaffSort());
			}

		}

		sb.append(" ORDER BY STAFF_SORT ASC,RALA_CD DESC");

		return this.jdbcFindList(sb.toString(), params,
				StaffOrganizationTran.class);
	}

	/**
	 * 新增员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void saveStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrgTran.setBatchNumber(batchNumber);
		staffOrgTran.add();
	}

	/**
	 * 修改员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void updateStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrgTran.setBatchNumber(batchNumber);
		staffOrgTran.update();
	}

	/**
	 * 删除员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void deleteStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrgTran.setBatchNumber(batchNumber);
		staffOrgTran.remove();
	}

	private List<?> getObjectByStatus(Class<?> clazz, Long staffId,
			String statusCd) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE T.staffId=")
				.append(staffId).append(" AND T.statusCd=").append(statusCd);
		return getHibernateTemplate().find(sBhql.toString());
	}

	// private List<?> getObj(Class<?> clazz, Long staffId) {
	// StringBuilder sBhql = new StringBuilder("FROM ");
	// sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
	// .append("T.staffId=").append(staffId)
	// .append(" AND T.statusCd=")
	// .append(BaseUnitConstants.ENTT_STATE_ACTIVE);
	// return getHibernateTemplate().find(sBhql.toString());
	// }

	private List<?> getActivationObj(Class<?> clazz, Long staffId) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
				.append("T.staffId=").append(staffId)
				.append(" AND T.statusCd=")
				.append(BaseUnitConstants.ENTT_STATE_INACTIVE);
		return getHibernateTemplate().find(sBhql.toString());
	}

	/**
	 * 员工的扩展属性 .
	 * 
	 * @return
	 * @author wangyong 2013-6-20 wangyong
	 */
	public List<StaffExtendAttr> getStaffExtendAttr(Long staffId) {
		String hql = " FROM StaffExtendAttr A where A.staffId = ?";
		return getHibernateTemplate().find(hql, new Object[] { staffId });
	}

	public String getSeqStaffCode() {
		String sql = "SELECT SEQ_STAFF_CODE.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}

	public String getSeqStaffNbr() {
		String sql = "SELECT SEQ_STAFF_NBR.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}

	public Long getSeqStaffFixId() {
		return Long.parseLong(this.getSeqNextval("SEQ_STAFF_FIX_ID"));
	}

	@Override
	public List<StaffAccount> getStaffAccountList(StaffAccount sa) {
		String hql = " FROM StaffAccount A where A.staffAccount = ? AND A.statusCd = ?";
		return getHibernateTemplate().find(
				hql,
				new Object[] { sa.getStaffAccount(),
						BaseUnitConstants.ENTT_STATE_ACTIVE });
	}

	@Override
	public List<StaffAccount> getNoStatusStaffAccountList(StaffAccount sa) {
		String hql = " FROM StaffAccount A where A.staffAccount = ?";
		return getHibernateTemplate().find(hql,
				new Object[] { sa.getStaffAccount() });
	}

	@Override
	public StaffAccount getStaffAccountByStaffAccount(String staffAccount) {
		String hql = null;
		List<StaffAccount> liStaff = null;
		if (!StrUtil.isNullOrEmpty(staffAccount)) {
			hql = " from StaffAccount u where u.staffAccount=? and u.statusCd = ?";
			liStaff = getHibernateTemplate().find(
					hql,
					new Object[] { staffAccount,
							BaseUnitConstants.ENTT_STATE_ACTIVE });
		}
		if (null != liStaff && liStaff.size() > 0) {
			return liStaff.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("serial")
	public boolean isExistStaffNumber(String staffNbr) {
		if (!StrUtil.isNullOrEmpty(staffNbr)) {
			StringBuilder sb = new StringBuilder(
					"SELECT T.STAFF_NBR FROM STAFF T WHERE T.STATUS_CD= ? AND T.STAFF_NBR= ?");
			List<Object> params = new ArrayList<Object>() {
			};
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(staffNbr);
			int i = super.jdbcGetSize(sb.toString(), params);
			if (i > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isExistStaffAccount(StaffAccount staffAccount) {
		if (!StrUtil.isNullOrEmpty(staffAccount.getStaffAccount())) {
			StringBuilder sb = new StringBuilder(
					"SELECT T.STAFF_ACCOUNT FROM STAFF_ACCOUNT T WHERE T.STATUS_CD= ? AND T.STAFF_ID!= ? AND T.STAFF_ACCOUNT= ?");
			List<Object> params = new ArrayList<Object>() {
			};
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(staffAccount.getStaffId());
			params.add(staffAccount.getStaffAccount());
			int i = super.jdbcGetSize(sb.toString(), params);
			if (i > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public StaffAccount getStaffAccountByPartyRoleId(String prId) {
		String sql = "SELECT SA.* FROM STAFF_ACCOUNT SA, STAFF S WHERE S.PARTY_ROLE_ID = ? AND SA.STAFF_ID= S.STAFF_ID AND SA.STATUS_CD= ?";
		return (StaffAccount) getJdbcTemplate().queryForObject(sql,
				new Object[] { prId, BaseUnitConstants.ENTT_STATE_ACTIVE },
				StaffAccount.class);
	}

	@Override
	public Staff getStaffByPartyRoleId(Long prId) {
		String hql = "from Staff s where s.partyRoleId = ? and s.statusCd = ?";
		List<Staff> liStaff = getHibernateTemplate().find(hql,
				new Object[] { prId, BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (null != liStaff && liStaff.size() > 0) {
			return liStaff.get(0);
		}
		return null;
	}

	@Override
	public void updateStaffByOperateHr(List<Staff> staffs) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		if (null != staffs && staffs.size() > 0) {
			for (Staff staff : staffs) {
				String hrStaffNbr = staff.getHrStaffNbr();
				String hrStaffAccount = staff.getHrStaffAccount();
				if (!StrUtil.isNullOrEmpty(hrStaffNbr)) {
					if (hrStaffNbr.startsWith("34")) {
						hrStaffAccount = hrStaffNbr.substring(2);
					} else if (hrStaffNbr.startsWith("W34")) {
						hrStaffAccount = hrStaffAccount.replace("W34", "W9");
					}
				}
				staff = this.queryStaff(staff.getStaffId());
				if (null != staff) {
					staff.setStaffNbr(hrStaffNbr);
					staff.setBatchNumber(batchNumber);
					staff.update();
					StaffAccount sa = this.getStaffAccount(null,
							staff.getStaffId());
					if (null != sa) {
						sa.setStaffAccount(hrStaffAccount);
						sa.setBatchNumber(batchNumber);
						sa.update();
					}
				}
			}
		}
	}

	@Override
	public List<AttrValue> getStaffWorkprop(TreeStaffSftRule tssr) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t3.attr_value, t3.attr_value_name from attr_value t3")
				.append(" where t3.status_cd = ? and t3.attr_id in (select attr_id")
				.append(" from attr_spec t2 where t2.status_cd = ? and t2.java_code = 'workProp'")
				.append(" and t2.class_id in (select class_id from sys_class t1 where")
				.append(" t1.java_code = 'Staff' and t1.status_cd = ?))")
				.append(" and t3.attr_value in (select ref_staff_type_cd from TREE_STAFF_SFT_RULE")
				.append(" where status_cd = ? and org_tree_id = ?)");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(tssr.getOrgTreeId());
		List<AttrValue> attrs = super.jdbcFindList(sb.toString(), params,
				AttrValue.class);
		return attrs;
	}

	@Override
	public List<AttrValue> getStaffWorkpropIbe(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t3.attr_value, t3.attr_value_name from attr_value t3")
				.append(" where t3.status_cd = ? and t3.attr_id in (select attr_id")
				.append(" from attr_spec t2 where t2.status_cd = ? and t2.java_code = 'workProp'")
				.append(" and t2.class_id in (select class_id from sys_class t1 where")
				.append(" t1.java_code = 'Staff' and t1.status_cd = ?))")
				.append(" and t3.attr_value != ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(str);
		List<AttrValue> attrs = super.jdbcFindList(sb.toString(), params,
				AttrValue.class);
		return attrs;
	}

	@Override
	public List<StaffAccount> queryStaffAccountList(StaffAccount staffAccount) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_ACCOUNT WHERE STATUS_CD = ?");
		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staffAccount != null) {

			if (!StrUtil.isNullOrEmpty(staffAccount.getStaffAccountId())) {
				sb.append(" AND STAFF_ACCOUNT_ID = ?");
				params.add(staffAccount.getStaffAccountId());
			}

			if (!StrUtil.isNullOrEmpty(staffAccount.getStaffId())) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staffAccount.getStaffId());
			}

			if (!StrUtil.isEmpty(staffAccount.getStaffAccount())) {
				sb.append(" AND STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffAccount.getStaffAccount()) + "%");
			}
			if (!StrUtil.isEmpty(staffAccount.getGuid())) {
				sb.append(" AND GUID = ?");
				params.add(StringEscapeUtils.escapeSql(staffAccount.getGuid()));
			}

			sb.append(" ORDER BY STAFF_ACCOUNT_ID");

		}

		List<StaffAccount> staffAccountList = Staff.repository().jdbcFindList(
				sb.toString(), params, StaffAccount.class);

		return staffAccountList;
	}

	@Override
	public List<StaffAccount> queryStaffAccountListByStaffAccount(
			StaffAccount staffAccount) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_ACCOUNT WHERE STATUS_CD = ?");
		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staffAccount != null) {

			if (!StrUtil.isNullOrEmpty(staffAccount.getStaffAccountId())) {
				sb.append(" AND STAFF_ACCOUNT_ID = ?");
				params.add(staffAccount.getStaffAccountId());
			}

			if (!StrUtil.isNullOrEmpty(staffAccount.getStaffId())) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staffAccount.getStaffId());
			}

			if (!StrUtil.isEmpty(staffAccount.getStaffAccount())) {
				sb.append(" AND STAFF_ACCOUNT = ?");
				params.add(staffAccount.getStaffAccount());
			}
			if (!StrUtil.isEmpty(staffAccount.getGuid())) {
				sb.append(" AND GUID = ?");
				params.add(staffAccount.getGuid());
			}

			sb.append(" ORDER BY STAFF_ACCOUNT_ID");

		}

		List<StaffAccount> staffAccountList = Staff.repository().jdbcFindList(
				sb.toString(), params, StaffAccount.class);

		return staffAccountList;
	}

	@Override
	public PageInfo forQuertyStaffNoStatus(Staff staff, int currentPage,
			int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT S.*,SA.staff_account FROM STAFF S,STAFF_ACCOUNT SA WHERE S.STAFF_ID = SA.STAFF_ID ");
		List<Object> params = new ArrayList<Object>();
		if (staff != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffId())) {
				sb.append(" AND S.STAFF_ID = ?");
				params.add(staff.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sb.append(" AND S.STAFF_NAME = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffName()));
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffCode())) {
				sb.append(" AND S.STAFF_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffCode()));
			}

			if (!StrUtil.isEmpty(staff.getStaffAccount())) {
				sb.append(" AND SA.STAFF_ACCOUNT = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffAccount()));
			}

			sb.append(" ORDER BY S.STAFF_ID");

		}
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, Staff.class);
	}

	@Override
	public List<Staff> queryStaffListByStaff(Staff staff) {

		StringBuffer sb = new StringBuffer(
				"SELECT S.* FROM STAFF S,STAFF_ACCOUNT SA WHERE S.STAFF_ID = SA.STAFF_ID AND S.STATUS_CD = ? AND SA.STATUS_CD = ?");
		List params = new ArrayList();

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staff != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffId())) {
				sb.append(" AND S.STAFF_ID = ?");
				params.add(staff.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sb.append(" AND S.STAFF_NAME = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffName()));
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffCode())) {
				sb.append(" AND S.STAFF_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffCode()));
			}

			if (!StrUtil.isEmpty(staff.getStaffAccount())) {
				sb.append(" AND SA.STAFF_ACCOUNT = ?");
				params.add(StringEscapeUtils.escapeSql(staff.getStaffAccount()));
			}

			sb.append(" ORDER BY S.STAFF_ID");

		}

		List<Staff> staffList = Staff.repository().jdbcFindList(sb.toString(),
				params, Staff.class);

		return staffList;
	}

	@Override
	public Staff queryStaffByPartyId(Long partyId) {

		if (partyId != null) {

			List params = new ArrayList();

			StringBuffer sb = new StringBuffer(
					"SELECT * FROM STAFF S,PARTY_ROLE P WHERE S.PARTY_ROLE_ID = P.PARTY_ROLE_ID AND S.STATUS_CD = ? AND P.STATUS_CD = ? AND P.PARTY_ID = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(partyId);

			List<Staff> staffList = this.jdbcFindList(sb.toString(), params,
					Staff.class);
			if (staffList != null && staffList.size() > 0) {
				return staffList.get(0);
			} else {
				return null;
			}

		}

		return null;
	}

	@Override
	public List<StaffOrgTranTemp> queryStaffOrgTranTempList(
			StaffOrgTranTemp staffOrgTranTemp) {

		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_ORG_TRAN_TEMP WHERE STATUS_CD = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staffOrgTranTemp != null) {

			if (staffOrgTranTemp.getStaffOrgTranTempId() != null) {
				sb.append(" AND STAFF_ORG_TRAN_TEMP_ID = ?");
				params.add(staffOrgTranTemp.getStaffOrgTranTempId());
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getTransId())) {
				sb.append(" AND TRANS_ID = ?");
				params.add(staffOrgTranTemp.getTransId());
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getOperationType())) {
				sb.append(" AND OPERATION_TYPE = ?");
				params.add(staffOrgTranTemp.getOperationType());
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getStaffName())) {
				sb.append(" AND STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrgTranTemp.getStaffName()) + "%");
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getStaffAccount())) {
				sb.append(" AND STAFF_ACCOUNT = ?");
				params.add(StringEscapeUtils.escapeSql(staffOrgTranTemp.getStaffAccount()));
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getStaffCode())) {
				sb.append(" AND STAFF_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staffOrgTranTemp.getStaffCode()));
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getOrgCode())) {
				sb.append(" AND ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staffOrgTranTemp.getOrgCode()));
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getOrgFullName())) {
				sb.append(" AND ORG_FULL_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrgTranTemp.getOrgFullName()) + "%");
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getRalaCd())) {
				sb.append(" AND RALA_CD = ?");
				params.add(staffOrgTranTemp.getRalaCd());
			}

			if (staffOrgTranTemp.getStaffSort() != null) {
				sb.append(" AND STAFF_SORT = ?");
				params.add(staffOrgTranTemp.getStaffSort());
			}

			if (staffOrgTranTemp.getResult() != null) {
				sb.append(" AND RESULT = ?");
				params.add(staffOrgTranTemp.getResult());
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getErrCode())) {
				sb.append(" AND ERR_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(staffOrgTranTemp.getErrCode()));
			}

			if (!StrUtil.isEmpty(staffOrgTranTemp.getErrMsg())) {
				sb.append(" AND ERR_MSG = ?");
				params.add(StringEscapeUtils.escapeSql(staffOrgTranTemp.getErrMsg()));
			}

		}

		sb.append(" ORDER BY RESULT ASC");

		return this.jdbcFindList(sb.toString(), params, StaffOrgTranTemp.class);
	}

	@Override
	public PageInfo queryPageInfoStaffOrgTran(Organization organization,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {

		StringBuffer sbsql = new StringBuffer(
				"select t2.*, t3.staff_account from staff_organization t1, "
						+ "staff_organization_tran t2, staff_account t3 where t1.rala_cd = 1 and t1.staff_id = t2.staff_id and "
						+ "t1.staff_id = t3.staff_id and t2.status_cd=? and t1.status_cd = ? and t3.status_cd = ? ");

		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (organization != null) {
			// 如果组织不为空，根据归属于当前组织的员工的员工组织业务关系查询
			if (organization.getId() != null) {
				sbsql.append(" and t1.org_id = ? ");
				params.add(organization.getId());
			}
		}

		// 返回查询结果
		// List<StaffOrganizationTran> result =
		// this.getJdbcTemplate().query(sbsql.toString(), params.toArray(), new
		// BeanPropertyRowMapper<StaffOrganizationTran>(StaffOrganizationTran.class));
		return StaffOrganization.repository().jdbcFindPageInfo(
				sbsql.toString(), params, currentPage, pageSize,
				StaffOrganizationTran.class);

	}

	@Override
	public PageInfo queryPageInfoByStaffCtPosition(CtPosition queryPosition,Staff staff, int current,
			int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM CT_STAFF_POSITION_REF WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryPosition != null && staff != null) {

			if (staff.getStaffId() != null) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staff.getStaffId());
			}

			sb.append(" AND CT_POSITION_ID IN (SELECT CT_POSITION_ID FROM CT_POSITION WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(queryPosition.getPositionName())) {
				sb.append(" AND POSITION_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryPosition.getPositionName().trim()) + "%");
			}

			sb.append(")");
			sb.append(" ORDER BY CT_STAFF_POSITION_REFID");
		}
		return this.jdbcFindPageInfo(sb.toString(), params, current, pageSize,
				CtStaffPositionRef.class);
	}
}
