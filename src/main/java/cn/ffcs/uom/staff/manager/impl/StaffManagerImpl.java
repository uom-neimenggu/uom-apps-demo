package cn.ffcs.uom.staff.manager.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.kernel.util.DigesterUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.position.model.CtPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staff.model.StaffOrgTranTemp;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.webservices.bean.wechat.Msg;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-28
 * @功能说明：
 * 
 */
@Service("staffManager")
@Scope("prototype")
public class StaffManagerImpl implements StaffManager {

	@Resource
	private StaffDao staffDao;

	@Override
	public Serializable addStaff(Staff staff) {
		return staffDao.addStaff(staff);
	}

	@Override
	public void delStaff(Serializable id) {
		staffDao.delStaff(id);
	}

	@Override
	public void delStaff(Staff staff) {
		staffDao.delStaff(staff);
	}

	@Override
	public PageInfo forQuertyStaff(Staff staff, int currentPage, int pageSize) {
		return staffDao.forQuertyStaff(staff, currentPage, pageSize);
	}

	@Override
	public List<Staff> quertyStaffNoPage(Staff staff) {
		return staffDao.quertyStaffNoPage(staff);
	}

	@Override
	public PageInfo forQuertyStaffActivation(Staff staff, int currentPage,
			int pageSize) {
		return staffDao.forQuertyStaffActivation(staff, currentPage, pageSize);
	}

	@Override
	public PageInfo forQuertyCompareStaff(Staff staff, int currentPage,
			int pageSize) {
		return staffDao.forQuertyCompareStaff(staff, currentPage, pageSize);
	}

	@Override
	public Staff queryStaff(Serializable id) {
		return staffDao.queryStaff(id);
	}

	@Override
	public void updateStaff(Staff staff) {
		staffDao.updateStaff(staff);
	}

	@Override
	public String updateStaffList(List<Staff> staffList) {
		String msg = staffDao.updateStaffList(staffList);
		return msg;
	}

	/**
	 * 查询员工账号 .
	 * 
	 * @param id
	 * @param staffId
	 * @return
	 * @author Wong 2013-6-4 Wong
	 */
	@Override
	public StaffAccount getStaffAccount(Serializable id, Long staffId) {
		return staffDao.getStaffAccount(id, staffId);
	}

	@Override
	public String queryStaffAccountListStrByCertNum(String certNum) {
		List<StaffAccount> staffAccList = staffDao.queryStaffAccountByCertNum(certNum);
		Set<String> staffAccSet = new HashSet<String>();
		for(StaffAccount sa : staffAccList) {
			StringBuilder staffAccSb = new StringBuilder();
			staffAccSb.append(sa.getStaffAccount());
			staffAccSb.append("("+sa.getStatusCdName()+")");
			staffAccSet.add(staffAccSb.toString());
		}
		
		StringBuilder staffAccListSb = new StringBuilder();
		for(String str : staffAccSet) {
			staffAccListSb.append(str);
			staffAccListSb.append(" ");
		}
		
		return staffAccListSb.toString();
	}

	@Override
	public String getPartyNameByStaffId(Serializable staffId) {
		return staffDao.getPartyNameByStaffId(staffId);
	}

	@Override
	public PageInfo queryPageInfoByStaffPosition(Position queryPosition,
			Organization queryOrganization, Staff staff, int current,
			int pageSize) {
		return staffDao.queryPageInfoByStaffPosition(queryPosition,
				queryOrganization, staff, current, pageSize);
	}

	/**
	 * 员工组织业务关系 .
	 * 
	 * @param staff
	 * @param current
	 * @param pageSize
	 * @return
	 * @author wangyong 2013-6-9 wangyong
	 */
	@Override
	public PageInfo queryPageInfoStaffOrgTran(Staff staff,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {
		return staffDao.queryPageInfoStaffOrgTran(staff, staffOrganizationTran,
				currentPage, pageSize);
	}

	/**
	 * 员工组织业务关系 .
	 * 
	 * @param staff
	 * @param current
	 * @param pageSize
	 * @return
	 * @author wangyong 2013-6-9 wangyong
	 */
	@Override
	public PageInfo queryPageInfoStaffGridUnitTran(Staff staff,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {
		return staffDao.queryPageInfoStaffGridUnitTran(staff,
				staffOrganizationTran, currentPage, pageSize);
	}

	/**
	 * 员工组织业务关系 列表查询
	 * 
	 * @param staff
	 * @param staffOrganizationTran
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryStaffGridUnitTranList(Staff staff,
			StaffOrganizationTran staffOrganizationTran) {
		return staffDao
				.queryStaffGridUnitTranList(staff, staffOrganizationTran);
	}

	@Override
	public List<StaffOrganizationTran> queryStaffOrgTranList(
			StaffOrganizationTran staffOrgTran) {
		return staffDao.queryStaffOrgTranList(staffOrgTran);
	}

	/**
	 * 新增员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void saveStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		staffDao.saveStaffOrgTran(staffOrgTran);
	}

	/**
	 * 修改员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void updateStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		staffDao.updateStaffOrgTran(staffOrgTran);
	}

	/**
	 * 删除员工组织业务关系 .
	 * 
	 * @author wangyong 2013-6-17 wangyong
	 */
	@Override
	public void deleteStaffOrgTran(StaffOrganizationTran staffOrgTran) {
		staffDao.deleteStaffOrgTran(staffOrgTran);
	}

	/**
	 * 员工的扩展属性 .
	 * 
	 * @return
	 * @author wangyong 2013-6-20 wangyong
	 */
	@Override
	public List<StaffExtendAttr> getStaffExtendAttr(Long staffId) {
		return staffDao.getStaffExtendAttr(staffId);
	}

	@Override
	public String getSeqStaffCode() {
		return staffDao.getSeqStaffCode();
	}

	@Override
	public String getSeqStaffNbr() {
		return staffDao.getSeqStaffNbr();
	}

	@Override
	public List<StaffAccount> getStaffAccountList(StaffAccount sa) {
		return staffDao.getStaffAccountList(sa);
	}

	@Override
	public List<StaffAccount> getNoStatusStaffAccountList(StaffAccount sa) {
		return staffDao.getNoStatusStaffAccountList(sa);
	}

	@Override
	public Staff getStaffByStaffId(StaffAccount staffAccount) {
		String saStr = staffAccount.getStaffAccount();
		if (!StrUtil.isNullOrEmpty(saStr)) {
			List<StaffAccount> staffAccounts = getStaffAccountList(staffAccount);
			if (null != staffAccounts && staffAccounts.size() > 0) {
				StaffAccount sa = staffAccounts.get(0);
				Staff s = queryStaff(sa.getStaffId());
				return s;
			}
		}
		return null;
	}

	@Override
	public StaffAccount getStaffAccountByStaffAccount(String staffAccount) {
		return staffDao.getStaffAccountByStaffAccount(staffAccount);
	}

	@Override
	public String gennerateStaffCode() {
		String staffCd = this.getSeqStaffCode();
		if (!StrUtil.isNullOrEmpty(staffCd)) {
			int legh = 8 - staffCd.length();
			if (legh >= 0) {
				String px = "85";
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < legh; i++) {
					sb.append("0");
				}
				sb.append(staffCd);
				return px + sb.toString();
			}
		}
		return null;
	}

	@Override
	public String gennerateStaffNumber(Staff staff) {
		return null;
	}

	@Override
	public String gennerateStaffNumber(String workProp) {
		if (!StrUtil.isNullOrEmpty(workProp)) {
			String staffSeq = this.getSeqStaffNbr();
			if (!StrUtil.isNullOrEmpty(staffSeq)) {
				if (SffOrPtyCtants.WORKPROP_W_AGENT.equals(workProp)
						|| SffOrPtyCtants.WORKPROP_W_OTHER.equals(workProp)
						|| SffOrPtyCtants.WORKPROP_W_PROVIDER.equals(workProp)) {
					return "H8" + staffSeq;
				} else {
					return "L7" + staffSeq;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isExistStaffNumber(String staffNbr) {
		return staffDao.isExistStaffNumber(staffNbr);
	}

	@Override
	public String gennerateStaffAccount(Staff staff) {
		if (null != staff) {
			String workProp = staff.getWorkProp();
			if (!StrUtil.isNullOrEmpty(workProp)) {
				String staffNbr = staff.getStaffNbr();
				if (!StrUtil.isNullOrEmpty(staffNbr)) {
					return staffNbr;
				}
			}
		}
		return null;
	}

	@Override
	public StaffAccount getStaffAccountByPartyRoleId(String prId) {
		if (!StrUtil.isNullOrEmpty(prId)) {
			return staffDao.getStaffAccountByPartyRoleId(prId);
		}
		return null;
	}

	@Override
	public Staff getStaffByPartyRoleId(Long prId) {
		return staffDao.getStaffByPartyRoleId(prId);
	}

	@Override
	public void updateStaffByOperateHr(List<Staff> staffs) {
		staffDao.updateStaffByOperateHr(staffs);
	}

	/**
	 * 获取无状态的参与人角色
	 * 
	 * @param partyRoleId
	 * @return
	 */
	@Override
	public PartyRole getPartyRole(Long partyRoleId) {
		return staffDao.getPartyRole(partyRoleId);
	}

	/**
	 * 获取无状态的参与人
	 * 
	 * @param partyId
	 * @return
	 */
	@Override
	public Party getParty(Long partyId) {
		return staffDao.getParty(partyId);
	}

	/**
	 * 根据参与人ID获取关联表的信息
	 * 
	 * @param clazz
	 *            partyId
	 * @return
	 */
	@Override
	public List<?> getActivationObjNoStatusCd(Class<?> clazz, Long partyId) {
		return staffDao.getActivationObjNoStatusCd(clazz, partyId);
	}

	@Override
	public List<Staff> queryStaffList(Staff queryStaff) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF A WHERE A.STATUS_CD=? ");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryStaff != null) {
			if (!StrUtil.isEmpty(queryStaff.getStaffAccount())) {
				sb.append(" AND STAFF_ID IN(SELECT STAFF_ID FROM STAFF_ACCOUNT B WHERE B.STATUS_CD=? AND B.STAFF_ACCOUNT=? )");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(StringEscapeUtils.escapeSql(queryStaff.getStaffAccount()));
			}
			if (queryStaff.getPartyRoleId() != null) {
				sb.append(" AND PARTY_ROLE_ID = ?");
				params.add(queryStaff.getPartyRoleId());
			}
		}
		return this.staffDao.jdbcFindList(sb.toString(), params, Staff.class);
	}

	@Override
	public void updateStaffByIntf(Staff staff) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		if (staff.getIsNeedUpdate()) {
			staff.setBatchNumber(batchNumber);
			staff.update();
		}
		Party party = staff.getParty();
		if (party.getIsNeedUpdate()) {
			party.setBatchNumber(batchNumber);
			party.update();
		}
		PartyCertification partyCertification = party.getPartyCertification();
		if (partyCertification != null && partyCertification.getIsNeedUpdate()) {
			partyCertification.setBatchNumber(batchNumber);
			if (partyCertification.getPartyCertId() == null) {
				partyCertification.setPartyId(party.getPartyId());
				partyCertification.add();
			} else {
				partyCertification.update();
			}
		}
		PartyContactInfo partyContactInfo = party.getPartyContactInfo();
		if (partyContactInfo != null && partyContactInfo.getIsNeedUpdate()) {
			partyContactInfo.setBatchNumber(batchNumber);
			if (partyContactInfo.getContactId() == null) {
				partyContactInfo.setPartyId(party.getPartyId());
				partyContactInfo.add();
			}
			partyContactInfo.update();
		}
	}

	@Override
	public void updateStaffToLdap(final String staffAccount) {
		try {
			final String sql = "{call EAM_PRO_UPDATE_STAFF_DATA(?)}";
			staffDao.getJdbcTemplate().execute(new ConnectionCallback() {
				@Override
				public Object doInConnection(Connection conn)
						throws SQLException, DataAccessException {
					CallableStatement cstm = conn.prepareCall(sql);
					cstm.setString(1, staffAccount);
					cstm.execute();
					return null;
				}
			});

		} catch (Exception e) {
			// 异常忽略不处理
		}
	}

	@Override
	public void updateStaffPwd(Staff staff) {
		StaffAccount staffAccount = staff.getObjStaffAccount();
		if (staffAccount != null) {
			String batchNumber = OperateLog.gennerateBatchNumber();
			staffAccount.setBatchNumber(batchNumber);
			String staffPassword = DigesterUtil.digest("SHA",
					new String[] { staffAccount.getStaffAccount() + "123" });
			staffAccount.setStaffPassword(staffPassword);
			staffAccount.update();
		}
	}

	@Override
	public String getStaffWorkpropStr(TreeStaffSftRule tssr) {
		StringBuffer sb = new StringBuffer();
		List<AttrValue> avs = staffDao.getStaffWorkprop(tssr);
		if (null != avs && avs.size() > 0) {
			for (int i = 0; i < avs.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append("'").append(avs.get(i).getAttrValue()).append("'");
			}
		}
		return sb.toString();
	}

	@Override
	public String getStaffWorkpropIbeStr(String str) {
		StringBuffer sb = new StringBuffer();
		List<AttrValue> avs = staffDao.getStaffWorkpropIbe(str);
		if (null != avs && avs.size() > 0) {
			for (int i = 0; i < avs.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append("'").append(avs.get(i).getAttrValue()).append("'");
			}
		}
		return sb.toString();
	}

	@Override
	public List<StaffAccount> queryStaffAccountList(StaffAccount staffAccount) {
		return staffDao.queryStaffAccountList(staffAccount);
	}

	@Override
	public List<StaffAccount> queryStaffAccountListByStaffAccount(
			StaffAccount staffAccount) {
		return staffDao.queryStaffAccountListByStaffAccount(staffAccount);
	}

	@Override
	public PageInfo forQuertyStaffNoStatus(Staff staff, int currentPage,
			int pageSize) {
		return staffDao.forQuertyStaffNoStatus(staff, currentPage, pageSize);
	}

	@Override
	public List<Staff> queryStaffListByStaff(Staff staff) {
		return staffDao.queryStaffListByStaff(staff);
	}

	@Override
	public Staff queryStaffByPartyId(Long partyId) {
		return staffDao.queryStaffByPartyId(partyId);
	}

	@Override
	public void saveStaffOrgTranTempList(
			List<StaffOrgTranTemp> staffOrgTranTempList) {
		for (StaffOrgTranTemp staffOrgTranTemp : staffOrgTranTempList) {
			staffOrgTranTemp.addOnly();
		}
	}

	@Override
	public List<StaffOrgTranTemp> queryStaffOrgTranTempList(
			StaffOrgTranTemp staffOrgTranTemp) {
		return staffDao.queryStaffOrgTranTempList(staffOrgTranTemp);
	}

	@Override
	public boolean checkStaff(Msg msg) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select count(t.staff_id) from staff t,staff_account a,party_certification b,party_role c where t.staff_id=a.staff_id and t.party_role_id=c.party_role_id and c.party_id=b.party_id and t.status_cd=1000 and a.status_cd=1000 and b.status_cd=1000 and c.status_cd=1000 and t.staff_name=? and a.staff_account=? and b.cert_number=?";
		params.add(msg.getStaffName());
		params.add(msg.getStaffAccount());
		params.add(msg.getCardId());
		int count = staffDao.getJdbcTemplate().queryForInt(sql,
				params.toArray());
		return count > 0 ? true : false;
	}

	@Override
	public PageInfo queryPageInfoStaffOrgTran(Organization organization,
			StaffOrganizationTran staffOrganizationTran, int currentPage,
			int pageSize) {

		return staffDao.queryPageInfoStaffOrgTran(organization,
				staffOrganizationTran, currentPage, pageSize);
	}

	@Override
	public PageInfo queryPageInfoByStaffCtPosition(CtPosition queryPosition,
			Staff staff, int current, int pageSize) {
		return staffDao.queryPageInfoByStaffCtPosition(queryPosition, staff,
				current, pageSize);
	}

	/*
	 * @Override public Object getActivationObjNoStatusCdOne(Class<?> clazz,
	 * Long partyId) { // TODO Auto-generated method stub return
	 * staffDao.getActivationObjNoStatusCdOne(clazz, partyId); }
	 */
}
