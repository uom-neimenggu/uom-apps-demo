package cn.ffcs.uom.party.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.dao.PartyDao;
import cn.ffcs.uom.party.model.CertIdVarInfo;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 参与人的管理 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-28
 * @功能说明：
 * 
 */
@Repository("partyDao")
@SuppressWarnings({ "unchecked", "serial" })
public class PartyDaoImpl extends BaseDaoImpl implements PartyDao {

	@Autowired
	private GroupManager groupManager;

	/**
	 * 返回新增参与人角色的主键ID {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.party.dao.PartyDao#addParty(cn.ffcs.uom.party.model.Party)
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public void addParty(Party party) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		party.setBatchNumber(batchNumber);

		party.add();

		Long partyId = party.getPartyId();

		PartyRole partyRole = party.getPartyRole();
		Individual indivi = party.getIndividual();
		PartyOrganization prtOrg = party.getPartyOrganization();

		PartyCertification ptyCerfic = party.getPartyCertification();
		PartyContactInfo ptyConInfo = party.getPartyContactInfo();

		Staff staff = party.getStaff();

		if (null != partyRole) {
			partyRole.setPartyId(partyId);
			partyRole.setBatchNumber(batchNumber);
			partyRole.add();
			party.setPartyRoleId(partyRole.getPartyRoleId().toString());
		}

		if (null != indivi) {
			indivi.setPartyId(partyId);
			indivi.setBatchNumber(batchNumber);
			indivi.add();
		}

		if (null != prtOrg) {
			prtOrg.setPartyId(partyId);
			prtOrg.setBatchNumber(batchNumber);
			prtOrg.add();
		}

		if (null != ptyCerfic) {
			ptyCerfic.setPartyId(partyId);
			ptyCerfic.setBatchNumber(batchNumber);
			ptyCerfic.add();
		}

		if (null != ptyConInfo) {
			ptyConInfo.setPartyId(partyId);
			ptyConInfo.setBatchNumber(batchNumber);
			ptyConInfo.add();
		}

		if (null != staff) {
			staff.setPartyRoleId(partyRole.getPartyRoleId());
			staff.setBatchNumber(batchNumber);
			staff.add();
			StaffAccount sa = staff.getObjStaffAccount();
			if (null != sa) {
				sa.setStaffId(staff.getStaffId());
				sa.setBatchNumber(batchNumber);
				sa.add();
			}

			Group group = party.getGroup();
			if (null != null) {
				groupManager.updateGroupProofread(group, staff);
			}

		}
	}

	/**
	 * 修改参与人 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public void updateParty(Party party) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		PartyRole partyRole = party.getPartyRole();
		Individual indivi = party.getIndividual();
		PartyOrganization prtOrg = party.getPartyOrganization();
		Staff staff = party.getStaff();
		if (null != partyRole) {
			partyRole.setBatchNumber(batchNumber);
			partyRole.update();
		}
		if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(party.getPartyType())) {
			if (null != indivi) {
				if (StrUtil.isNullOrEmpty(indivi.getIndividualId())) {
					indivi.setPartyId(party.getPartyId());
					indivi.setBatchNumber(batchNumber);
					indivi.add();
				} else {
					indivi.setBatchNumber(batchNumber);
					indivi.update();
				}
			}
		} else {
			if (null != prtOrg) {
				if (StrUtil.isNullOrEmpty(prtOrg.getPratyOrganizationId())) {
					prtOrg.setPartyId(party.getPartyId());
					prtOrg.setBatchNumber(batchNumber);
					prtOrg.add();
				} else {
					prtOrg.setBatchNumber(batchNumber);
					prtOrg.update();
				}
			}
		}
		if (null != staff) {
			staff.setBatchNumber(batchNumber);
			staff.update();
			StaffAccount sa = staff.getObjStaffAccount();
			if (null != sa.getStaffAccountId()) {
				sa.setBatchNumber(batchNumber);
				sa.update();
			} else {
				sa.setBatchNumber(batchNumber);
				sa.add();
			}
		}
		party.setBatchNumber(batchNumber);
		party.update();
	}

	@Override
	public void delParty(Long id) {
		super.removeObject(Party.class, id);
	}

	/**
	 * 删除参与人相关的信息 {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.party.dao.PartyDao#delParty(cn.ffcs.uom.party.model.Party)
	 * @author Wong 2013-6-3 Wong
	 */
	@Override
	public void delParty(Party party) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		Long _LPartyId = party.getPartyId();

		List<PartyRole> lPyR = (List<PartyRole>) getObj(PartyRole.class,
				_LPartyId);
		if (null != lPyR) {
			for (PartyRole so : lPyR) {
				so.setBatchNumber(batchNumber);
				so.remove();
			}
		}
		List<Individual> ldiv = (List<Individual>) getObj(Individual.class,
				_LPartyId);
		if (null != ldiv) {
			for (Individual so : ldiv) {
				so.setBatchNumber(batchNumber);
				so.remove();
			}
		}
		List<PartyOrganization> lPyOrg = (List<PartyOrganization>) getObj(
				PartyOrganization.class, _LPartyId);
		if (null != lPyOrg) {
			for (PartyOrganization so : lPyOrg) {
				so.setBatchNumber(batchNumber);
				so.remove();
			}
		}
		List<PartyContactInfo> lParCon = (List<PartyContactInfo>) getObj(
				PartyContactInfo.class, _LPartyId);
		if (null != lParCon) {
			for (PartyContactInfo pc : lParCon) {
				pc.setBatchNumber(batchNumber);
				pc.remove();
			}
		}
		List<PartyCertification> lParCer = (List<PartyCertification>) getObj(
				PartyCertification.class, _LPartyId);
		if (null != lParCer) {
			for (PartyCertification pcf : lParCer) {
				pcf.setBatchNumber(batchNumber);
				pcf.remove();
			}
		}
		party.setBatchNumber(batchNumber);
		party.remove();
	}

	@Override
	public Party queryParty(Long partyId) {
		// return (Party)super.getObject(Party.class, id);
		String hql = null;
		List<Party> liParty = null;
		if (!StrUtil.isNullOrEmpty(partyId)) {
			hql = " from Party u where u.partyId=? and u.statusCd = ?";
			liParty = getHibernateTemplate()
					.find(hql,
							new Object[] { partyId,
									BaseUnitConstants.ENTT_STATE_ACTIVE });
		}
		if (null == liParty || liParty.size() == 0) {
			return null;
		}
		return liParty.get(0);
	}

	/**
	 * 检查参与人的名字是否被使用 .
	 * 
	 * @author Wong 2013-5-28 Wong
	 */
	@Override
	public boolean checkPartyNameExits(String partyName) {
		String sql = "SELECT COUNT(1) FROM PARTY T WHERE T.PARTY_NAME=? AND T.STATUS_CD=?";
		int counts = getJdbcTemplate().queryForInt(
				sql,
				new Object[] { StringEscapeUtils.escapeSql(partyName),
						BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (counts > 0) {
			return true;
		}
		return false;
	}

	public void save(PartyCertification partyCerfi) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		try {
			String hql = "SELECT t.partyCertId FROM PartyCertification t WHERE t.partyId=? AND t.statusCd=? AND t.certSort=?";
			List<Object> params = new ArrayList<Object>();
			params.add(partyCerfi.getPartyId());
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(SffOrPtyCtants.CERT_SORT_1);
			List<Object> lt = DefaultDaoFactory.getDefaultDao()
					.findListByHQLAndParams(hql, params);

			if (lt != null && lt.size() > 0
					&& !StrUtil.isNullOrEmpty(lt.get(0))) {
				if (SffOrPtyCtants.CERT_SORT_1.equals(partyCerfi.getCertSort())) {
					String upSql = "UPDATE PARTY_CERTIFICATION T SET T.CERT_SORT="
							+ SffOrPtyCtants.CERT_SORT_2
							+ " WHERE T.PARTY_ID="
							+ partyCerfi.getPartyId()
							+ " AND T.STATUS_CD="
							+ BaseUnitConstants.ENTT_STATE_ACTIVE;
					executeSQL(upSql);
				}
			} else {
				if (SffOrPtyCtants.CERT_SORT_2.equals(partyCerfi.getCertSort())) {
					partyCerfi.setCertSort(SffOrPtyCtants.CERT_SORT_1);
				}
			}
			partyCerfi.setBatchNumber(batchNumber);
			partyCerfi.add();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(PartyCertification partyCerfi) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		try {
			if (SffOrPtyCtants.CERT_SORT_1.equals(partyCerfi.getCertSort())) {
				String upSql = "UPDATE PARTY_CERTIFICATION T SET T.CERT_SORT="
						+ SffOrPtyCtants.CERT_SORT_2 + " WHERE T.PARTY_ID="
						+ partyCerfi.getPartyId() + " AND T.STATUS_CD="
						+ BaseUnitConstants.ENTT_STATE_ACTIVE;
				executeSQL(upSql);
			}
			partyCerfi.setBatchNumber(batchNumber);
			partyCerfi.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(PartyContactInfo partyContactInfo) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		try {
			String sql = "SELECT T.CONTACT_ID CN FROM PARTY_CONTACT_INFO T WHERE ROWNUM=1 AND T.PARTY_ID=? AND T.STATUS_CD=? AND T.HEAD_FLAG=?";
			List<Map<String, Object>> lt = getJdbcTemplate().queryForList(
					sql,
					new Object[] { partyContactInfo.getPartyId(),
							BaseUnitConstants.ENTT_STATE_ACTIVE,
							SffOrPtyCtants.HEADFLAG });
			if (lt != null && lt.size() > 0
					&& !StrUtil.isNullOrEmpty(lt.get(0).get("CN"))) {
				if (SffOrPtyCtants.HEADFLAG.equals(partyContactInfo
						.getHeadFlag())) {
					String upSql = "UPDATE PARTY_CONTACT_INFO T SET T.HEAD_FLAG="
							+ SffOrPtyCtants.NO_HEADFLAG
							+ " WHERE T.PARTY_ID="
							+ partyContactInfo.getPartyId()
							+ " AND T.STATUS_CD="
							+ BaseUnitConstants.ENTT_STATE_ACTIVE;
					executeSQL(upSql);
				}
			} else {
				if (SffOrPtyCtants.NO_HEADFLAG.equals(partyContactInfo
						.getHeadFlag())) {
					partyContactInfo.setHeadFlag(SffOrPtyCtants.HEADFLAG);
				}
			}
			partyContactInfo.setBatchNumber(batchNumber);
			partyContactInfo.add();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(PartyContactInfo partyContactInfo) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		try {

			if (SffOrPtyCtants.HEADFLAG.equals(partyContactInfo.getHeadFlag())) {
				String upSql = "UPDATE PARTY_CONTACT_INFO T SET T.HEAD_FLAG="
						+ SffOrPtyCtants.NO_HEADFLAG + " WHERE T.PARTY_ID="
						+ partyContactInfo.getPartyId() + " AND T.STATUS_CD="
						+ BaseUnitConstants.ENTT_STATE_ACTIVE;
				executeSQL(upSql);
			}
			partyContactInfo.setBatchNumber(batchNumber);
			partyContactInfo.update();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(Individual indiv) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		indiv.setBatchNumber(batchNumber);
		indiv.add();
	}

	public void update(Individual indiv) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		indiv.setBatchNumber(batchNumber);
		indiv.update();
	}

	/**
	 * 
	 * .
	 * 
	 * @param id
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyCertification(Serializable id) {

	}

	/**
	 * 
	 * .
	 * 
	 * @param id
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyContactInfo(Serializable id) {

	}

	/**
	 * 
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyCertification(PartyCertification partyCertification) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		partyCertification.setBatchNumber(batchNumber);
		partyCertification = getHibernateTemplate().merge(partyCertification);
		partyCertification.remove();
	}

	/**
	 * 检查参与人证件是否是最后一个 .
	 * 
	 * @param partyCerfi
	 * @author wangyong 2013-6-29 wangyong
	 */
	public boolean isLastPartyCertification(Long partyId) {
		String sql = "SELECT COUNT(1) FROM PARTY_CERTIFICATION T WHERE T.STATUS_CD = ? AND T.PARTY_ID=?";
		int count = getJdbcTemplate().queryForInt(sql,
				new Object[] { BaseUnitConstants.ENTT_STATE_ACTIVE, partyId });
		if (count == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * .
	 * 
	 * @param partyContactInfo
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyContactInfo(PartyContactInfo partyContactInfo) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		partyContactInfo.setBatchNumber(batchNumber);
		partyContactInfo.remove();
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
		return (List<PartyCertification>) getHibernateTemplate()
				.find("from PartyCertification A WHERE A.partyId=? AND A.statusCd = ?",
						new Object[] { partyId,
								BaseUnitConstants.ENTT_STATE_ACTIVE });
	}

	@Override
	public PartyCertification getDefaultPartyCertification(Long partyId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "FROM PartyCertification P WHERE P.partyId = ? AND P.statusCd = ? AND P.certSort = ?";
		params.add(partyId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(SffOrPtyCtants.CERT_SORT_1);
		List<PartyCertification> pcs = findListByHQLAndParams(sql, params);
		if (null != pcs && pcs.size() > 0) {
			return pcs.get(0);
		}
		return null;
	}

	@Override
	public List<PartyCertification> queryPartyCertificationList(Staff staff,
			PartyCertification pc) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT PC.* FROM PARTY_CERTIFICATION PC JOIN PARTY_ROLE PR ON PC.PARTY_ID = PR.PARTY_ID JOIN STAFF ST ON PR.PARTY_ROLE_ID = ST.PARTY_ROLE_ID");
		sql.append(" WHERE PC.STATUS_CD = ? AND PR.STATUS_CD = ? AND ST.STATUS_CD = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (null != staff) {

			if (null != staff.getStaffId()) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staff.getStaffId());
			}

		}

		if (null != pc) {

			if (null != pc.getPartyCertId()) {
				sql.append(" AND PC.PARTY_CERT_ID = ?");
				params.add(pc.getPartyCertId());
			}

			if (null != pc.getPartyId()) {
				sql.append(" AND PC.PARTY_ID = ?");
				params.add(pc.getPartyId());
			}

			if (!StrUtil.isEmpty(pc.getCertType())) {
				sql.append(" AND PC.CERT_TYPE = ?");
				params.add(pc.getCertType());
			}

			if (!StrUtil.isEmpty(pc.getCertSort())) {
				sql.append(" AND PC.CERT_SORT = ?");
				params.add(pc.getCertSort());
			}

		}

		return jdbcFindList(sql.toString(), params, PartyCertification.class);

	}

	@Override
	public PartyContactInfo getDefaultPartyContactInfo(Long partyId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "FROM PartyContactInfo P WHERE P.partyId = ? AND P.statusCd = ? AND P.headFlag = ?";
		params.add(partyId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(SffOrPtyCtants.HEADFLAG);
		List<PartyContactInfo> pci = findListByHQLAndParams(sql, params);
		if (null != pci && pci.size() > 0) {
			return pci.get(0);
		}
		return null;
	}

	@Override
	public List<PartyContactInfo> queryDefaultPartyContactInfo(
			PartyContactInfo partyContactInfo) {

		if (partyContactInfo != null) {

			List params = new ArrayList();

			StringBuffer sb = new StringBuffer(
					"SELECT * FROM PARTY_CONTACT_INFO WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (partyContactInfo.getContactId() != null) {
				sb.append(" AND CONTACT_ID = ?");
				params.add(partyContactInfo.getContactId());
			}

			if (partyContactInfo.getPartyId() != null) {
				sb.append(" AND PARTY_ID = ?");
				params.add(partyContactInfo.getPartyId());
			}

			if (partyContactInfo.getHeadFlag() != null) {
				sb.append(" AND HEAD_FLAG = ?");
				params.add(partyContactInfo.getHeadFlag());
			}

			if (partyContactInfo.getGrpUnEmail() != null) {
				sb.append(" AND GRP_UN_EMAIL = ?");
				params.add(StringEscapeUtils.escapeSql(partyContactInfo
						.getGrpUnEmail()));
			}

			sb.append(" ORDER BY CONTACT_ID");

			return this.jdbcFindList(sb.toString(), params,
					PartyContactInfo.class);

		}

		return null;

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
		String hql = "from PartyContactInfo t where t.partyId=? and t.statusCd = ?";
		return getHibernateTemplate().find(hql,
				new Object[] { partyId, BaseUnitConstants.ENTT_STATE_ACTIVE });
	}

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
	@Override
	public boolean checkIsExistCertificate(String certNumber) {
		String sql = "SELECT p.PARTY_CERT_ID FROM PARTY_CERTIFICATION p "
				+ " WHERE NOT exists(SELECT 1 FROM ORGANIZATION o WHERE o.party_id = p.party_id) "
				+ " and p.CERT_NUMBER=? AND p.STATUS_CD=?";
		List<Map<String, Object>> lsm = getJdbcTemplate().queryForList(
				sql,
				new Object[] { StringEscapeUtils.escapeSql(certNumber),
						BaseUnitConstants.ENTT_STATE_ACTIVE });
		int partyCertificationUsedMax = getPartyCertificationUsedMax();

		if (null != lsm && lsm.size() >= partyCertificationUsedMax) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.party.dao.PartyDao#forQuertyParty(cn.ffcs.uom.party.model.Party,
	 *      boolean, java.lang.String, int, int)
	 * @author Wong 2013-5-30 Wong
	 */
	@Override
	public PageInfo forQuertyParty(Party party, int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT A.*, B.PARTY_ROLE_ID, B.ROLE_TYPE,C.CERT_NUMBER,D.MOBILE_PHONE FROM PARTY A");
		sb.append(
				" LEFT OUTER JOIN PARTY_ROLE B ON (A.PARTY_ID=B.PARTY_ID AND A.STATUS_CD=B.STATUS_CD)")
				.append(" LEFT OUTER JOIN PARTY_CERTIFICATION C ON (A.PARTY_ID=C.PARTY_ID AND A.STATUS_CD=C.STATUS_CD")
				.append(" AND C.CERT_SORT=")
				.append(SffOrPtyCtants.CERT_SORT_1)
				.append(")")
				.append(" LEFT OUTER JOIN PARTY_CONTACT_INFO D ON (A.PARTY_ID=D.PARTY_ID AND A.STATUS_CD=D.STATUS_CD")
				.append(" AND D.HEAD_FLAG=").append(SffOrPtyCtants.HEADFLAG)
				.append(")");
		/**
		 * 增加按“员工账号”查询的条件
		 */
		if (null != party.getStaff()) {
			if (!StrUtil.isNullOrEmpty(party.getStaff().getStaffId())) {
				int start = sb.indexOf("FROM");
				sb.insert(start, ",F.STAFF_ACCOUNT ");
				sb.append(" INNER JOIN STAFF E ON (E.PARTY_ROLE_ID=B.PARTY_ROLE_ID AND E.STATUS_CD=B.STATUS_CD AND E.STAFF_ID=?) INNER JOIN STAFF_ACCOUNT F ON (E.STAFF_ID=F.STAFF_ID AND E.STATUS_CD=F.STATUS_CD) ");
				params.add(party.getStaff().getStaffId());
			}
		}
		sb.append(" WHERE A.STATUS_CD=? AND B.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (!StrUtil.isNullOrEmpty(party.getPartyName())) {
			sb.append(" AND A.PARTY_NAME Like ?");
			params.add("%" + StringEscapeUtils.escapeSql(party.getPartyName())
					+ "%");
		}
		if (!StrUtil.isNullOrEmpty(party.getMobilePhone())) {
			sb.append(" AND D.MOBILE_PHONE = ?");
			params.add(StringEscapeUtils.escapeSql(party.getMobilePhone()));
		}
		if (!StrUtil.isNullOrEmpty(party.getCertNumber())) {
			sb.append(" AND A.PARTY_ID IN(select C.PARTY_ID from PARTY_CERTIFICATION C WHERE C.CERT_NUMBER=?)");
			params.add(StringEscapeUtils.escapeSql(party.getCertNumber()));
		}
		if (!StrUtil.isNullOrEmpty(party.getPartyType())) {
			sb.append(" AND A.PARTY_TYPE=? ");
			params.add(party.getPartyType());
		}
		if (!StrUtil.isNullOrEmpty(party.getPartyId())) {
			sb.append(" AND A.PARTY_ID=? ");
			params.add(party.getPartyId());
		}

		if (!StrUtil.isNullOrEmpty(party.getFreeParty())) {
			if (party.getFreeParty().equals(PartyConstant.FREE_PARTY)) {// 游离参与人人

				if (!StrUtil.isNullOrEmpty(party.getPartyType())) {
					if (party.getPartyType().equals(
							PartyConstant.ATTR_VALUE_PARTYTYPE_PERSONAL)) {
						sb.append(" AND A.PARTY_ID NOT IN ");
						sb.append(" (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD = ? AND PARTY_ROLE_ID IN (SELECT PARTY_ROLE_ID FROM STAFF WHERE STATUS_CD = ?)) ");
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					} else {
						sb.append(" AND A.PARTY_ID NOT IN ");
						sb.append(" (SELECT PARTY_ID FROM ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID IS NOT NULL) ");
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					}
				} else {
					sb.append(" AND A.PARTY_ID NOT IN ");
					sb.append(" (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD = ? AND PARTY_ROLE_ID IN (SELECT PARTY_ROLE_ID FROM STAFF WHERE STATUS_CD = ?) ");
					sb.append(" UNION ALL ");
					sb.append(" SELECT PARTY_ID FROM ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID IS NOT NULL) ");
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				}

			} else if (party.getFreeParty()
					.equals(PartyConstant.NOT_FREE_PARTY)) {// 非游离参与人

				if (!StrUtil.isNullOrEmpty(party.getPartyType())) {
					if (party.getPartyType().equals(
							PartyConstant.ATTR_VALUE_PARTYTYPE_PERSONAL)) {
						sb.append(" AND A.PARTY_ID IN ");
						sb.append(" (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD = ? AND PARTY_ROLE_ID IN (SELECT PARTY_ROLE_ID FROM STAFF WHERE STATUS_CD = ?)) ");
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					} else {
						sb.append(" AND A.PARTY_ID IN ");
						sb.append(" (SELECT PARTY_ID FROM ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID IS NOT NULL) ");
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					}
				} else {
					sb.append(" AND A.PARTY_ID IN ");
					sb.append(" (SELECT PARTY_ID FROM PARTY_ROLE WHERE STATUS_CD = ? AND PARTY_ROLE_ID IN (SELECT PARTY_ROLE_ID FROM STAFF WHERE STATUS_CD = ?) ");
					sb.append(" UNION ALL ");
					sb.append(" SELECT PARTY_ID FROM ORGANIZATION WHERE STATUS_CD = ? AND PARTY_ID IS NOT NULL) ");
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				}

			}
		}

		return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, Party.class);
	}

	/**
	 * 根据参与人标识获取个人信息 .
	 * 
	 * @param partyId
	 * @return
	 * @author Wong 2013-5-29 Wong
	 */
	@Override
	public Individual getIndividual(Long partyId) {
		String hql = "from Individual t where t.partyId=? and t.statusCd=?";
		List<Individual> liPi = getHibernateTemplate().find(hql,
				new Object[] { partyId, BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (null != liPi && liPi.size() > 0) {
			return liPi.get(0);
		}
		return null;
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
		String hql = "from PartyOrganization t where t.partyId=? and t.statusCd=?";
		List<PartyOrganization> liPi = getHibernateTemplate().find(hql,
				new Object[] { partyId, BaseUnitConstants.ENTT_STATE_ACTIVE });
		if (null != liPi && liPi.size() > 0) {
			return liPi.get(0);
		}
		return null;
	}

	/**
	 * 获取所有参与人 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	@Override
	public PageInfo getPartyContactInfos(PartyContactInfo pInfo,
			int activePage, int pageSize) {
		StringBuilder sb = new StringBuilder(
				" FROM PartyContactInfo t where t.statusCd=? and t.partyId=?");
		List<Object> params = new ArrayList<Object>() {
		};
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(pInfo.getPartyId());
		if (!StrUtil.isEmpty(pInfo.getContactName())) {
			sb.append(" and t.contactName like ?");
			params.add("%"
					+ StringEscapeUtils
							.escapeSql(pInfo.getContactName().trim()) + "%");
		}
		return super.findPageInfoByHQLAndParams(sb.toString(), params,
				activePage, pageSize);

	}

	/**
	 * 获取所有参与人证件 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	@Override
	public PageInfo getPartyCertifications(PartyCertification paCerInfo,
			int activePage, int pageSize) {
		String sql = "SELECT T.* FROM PARTY_CERTIFICATION T WHERE T.STATUS_CD=? AND T.PARTY_ID=?";
		List<Object> params = new ArrayList<Object>() {
		};
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(paCerInfo.getPartyId());

		if (!StrUtil.isEmpty(paCerInfo.getCertNumber())) {
			sql += " AND T.CERT_NUMBER LIKE ?";
			params.add("%"
					+ StringEscapeUtils.escapeSql(paCerInfo.getCertNumber()
							.trim()) + "%");
		}

		if (!StrUtil.isEmpty(paCerInfo.getCertOrg())) {
			sql += " AND CERT_ORG LIKE ?";
			params.add("%"
					+ StringEscapeUtils
							.escapeSql(paCerInfo.getCertOrg().trim()) + "%");
		}

		return super.jdbcFindPageInfo(sql, params, activePage, pageSize,
				PartyCertification.class);
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
		StringBuilder sql = new StringBuilder(
				"SELECT T.* FROM STAFF_ORGANIZATION T WHERE T.STATUS_CD=?");
		List<Object> params = new ArrayList<Object>() {
		};
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (StrUtil.isNullOrEmpty(staffOrg.getOrgId())) {
			sql.append(" AND T.ORG_ID=?");
			params.add(staffOrg.getOrgId());
		}
		if (StrUtil.isNullOrEmpty(staffOrg.getStaffId())) {
			sql.append(" AND T.STAFF_ID=?");
			params.add(staffOrg.getStaffId());
		}
		return super.jdbcFindPageInfo(sql.toString(), params, activePag,
				pageSize, StaffOrganization.class);
	}

	/**
	 * 执行SQL语句 .
	 * 
	 * @param sql
	 * @author wangyong 2013-6-13 wangyong
	 */
	private void executeSQL(String sql) {
		getJdbcTemplate().execute(sql);
	}

	/**
	 * 根据ClassName attrName获取AttrValue .
	 * 
	 * @param className
	 * @param attrName
	 * @return
	 * @author wangyong 2013-6-15 wangyong
	 */
	public List<Map<String, Object>> getListForAttrValue(String className,
			String attrName) {
		StringBuilder sbsql = new StringBuilder(
				"SELECT C.ATTR_VALUE,C.ATTR_VALUE_NAME FROM ATTR_VALUE C WHERE C.ATTR_ID=(");
		sbsql.append(" SELECT B.ATTR_ID FROM ATTR_SPEC B WHERE B.CLASS_ID=(");
		sbsql.append(" SELECT T.CLASS_ID FROM SYS_CLASS T WHERE T.JAVA_CODE=?) AND B.JAVA_CODE=?)");
		return getJdbcTemplate().queryForList(sbsql.toString(),
				new Object[] { className, attrName });
	}

	private List<?> getObj(Class<?> clazz, Long partyId) {
		StringBuilder sBhql = new StringBuilder("FROM ");
		sBhql.append(StrUtil.getClazzName(clazz)).append(" T WHERE ")
				.append("T.partyId=").append(partyId);
		return getHibernateTemplate().find(sBhql.toString());
	}

	@Override
	public List<PartyRole> getPartyRoleByPtId(Long staffId) {
		StringBuilder sb = new StringBuilder(
				" FROM PartyRole T WHERE T.statusCd =? AND T.partyId=?");
		return getHibernateTemplate().find(sb.toString(),
				new Object[] { BaseUnitConstants.ENTT_STATE_ACTIVE, staffId });
	}

	@Override
	public void remove(UomEntity ue) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		ue.setBatchNumber(batchNumber);
		ue.remove();
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void addPartyRole(PartyRole partyRole) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		partyRole.setBatchNumber(batchNumber);
		partyRole.add();
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void updatePartyRole(PartyRole partyRole) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		partyRole.setBatchNumber(batchNumber);
		partyRole.update();
	}

	/**
	 * .
	 * 
	 * @param partyCertification
	 * @author Wong 2013-6-8 Wong
	 */
	@Override
	public void removePartyRole(PartyRole partyRole) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		partyRole.setBatchNumber(batchNumber);
		partyRole.remove();
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
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM PARTY_ROLE T WHERE T.STATUS_CD=? AND T.PARTY_ID=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(party.getPartyId());

		if (!StrUtil.isEmpty(party.getRoleType())) {
			sb.append(" AND T.ROLE_TYPE = ?");
			params.add(party.getRoleType().trim());
		}

		return jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize,
				PartyRole.class);
	}

	@Override
	public void modStaffNameForPtyName(Party party) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT T.* FROM STAFF T WHERE T.PARTY_ROLE_ID=(SELECT PR.PARTY_ROLE_ID FROM PARTY_ROLE PR WHERE ROWNUM=1 AND PR.PARTY_ID=?)";
		params.add(party.getPartyId());
		Staff staff = jdbcFindObject(sql, params, Staff.class);
		if (null != staff) {
			staff.setStaffName(party.getPartyName());
			staff.update();
		}
	}

	@Override
	public boolean isExistsMobilePhone(PartyContactInfo partyContactInfo) {
		// if(null != partyContactInfo){
		// String mobilePhone = partyContactInfo.getMobilePhone();
		// if(!StrUtil.isNullOrEmpty(mobilePhone)){
		// StringBuilder sb = new
		// StringBuilder("SELECT T.* FROM PARTY_CONTACT_INFO T WHERE T.STATUS_CD= ? AND T.MOBILE_PHONE= ?");
		// List<Object> params = new ArrayList<Object>(){};
		// params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		// params.add(partyContactInfo.getMobilePhone());
		// int i = super.jdbcGetSize(sb.toString(), params);
		// if(i > 0){
		// return true;
		// }
		// }
		// }
		return false;
	}

	@Override
	public Party getPartyByStaff(Staff staff) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT P.* FROM PARTY P INNER JOIN PARTY_ROLE PR ON (P.PARTY_ID = PR.PARTY_ID AND P.STATUS_CD = ?) INNER JOIN STAFF SO ON (SO.PARTY_ROLE_ID = PR.PARTY_ROLE_ID AND SO.STAFF_ID = ?)";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(staff.getStaffId());
		List<Party> partys = jdbcFindList(sql, params, Party.class);
		if (null != partys && partys.size() > 0) {
			return partys.get(0);
		}
		return null;
	}

	@Override
	public Party getPartyByStaffAccount(String staffAccount) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT P.* FROM PARTY P "
				+ "INNER JOIN PARTY_ROLE PR ON (P.PARTY_ID = PR.PARTY_ID AND P.STATUS_CD = ?) "
				+ "INNER JOIN STAFF SO ON (SO.PARTY_ROLE_ID = PR.PARTY_ROLE_ID AND SO.STATUS_CD = ?) "
				+ "INNER JOIN STAFF_ACCOUNT sa ON (SO.STAFF_ID = SA.STAFF_ID AND SA.STATUS_CD = ? "
				+ "AND SA.STAFF_ACCOUNT = ?) WHERE P.STATUS_CD = ?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(StringEscapeUtils.escapeSql(staffAccount));
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<Party> partys = jdbcFindList(sql, params, Party.class);
		if (null != partys && partys.size() > 0) {
			return partys.get(0);
		}
		return null;
	}

	@Override
	public PartyRole getPartyRole(Long partyRoleId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT T.* FROM PARTY_ROLE T WHERE T.STATUS_CD=? AND T.PARTY_ROLE_ID=?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(partyRoleId);
		List<PartyRole> partyRoles = jdbcFindList(sql, params, PartyRole.class);
		if (null != partyRoles && partyRoles.size() > 0) {
			return partyRoles.get(0);
		}
		return null;
	}

	@Override
	public PartyRole getPartyRoleByPartyId(Long partyId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT T.* FROM PARTY_ROLE T WHERE T.STATUS_CD=? AND T.PARTY_ID=?";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(partyId);
		List<PartyRole> partyRoles = jdbcFindList(sql, params, PartyRole.class);
		if (null != partyRoles && partyRoles.size() > 0) {
			return partyRoles.get(0);
		}
		return null;
	}

	@Override
	public List<PartyContactInfo> getPartyContactInfo(
			PartyContactInfo queryContactInfo) {
		String hql = " from PartyContactInfo a where a.partyId = ? and a.statusCd = ?";
		return getHibernateTemplate().find(
				hql,
				new Object[] { queryContactInfo.getPartyId(),
						BaseUnitConstants.ENTT_STATE_ACTIVE });
	}

	@Override
	public int getPartyCertificationUsedMax() {
		return Integer.parseInt(UomClassProvider
				.getSystemConfig("partyCertificationUsedMax"));
	}

	@Override
	public CertIdVarInfo getCertIdVarInfo(String identity, String name) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT *  FROM TB_PTY_CERTIF_ID_VAR_INFO WHERE "
				+ " IDENTITY = ? AND NAME = ?";
		params.add(identity);
		params.add(name);
		List<CertIdVarInfo> certIdVarInfoList = jdbcFindList(sql, params,
				CertIdVarInfo.class);
		
		if (null != certIdVarInfoList && certIdVarInfoList.size() > 0) {
			return certIdVarInfoList.get(0);
		}
		
		return null;
	}
}
