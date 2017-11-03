package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.comparehr.manager.OperateHrManager;
import cn.ffcs.uom.comparehr.model.OperateHr;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.dao.StaffOrganizationDao;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.organization.vo.StaffOrganizationImportVo;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.manager.StaffPositionManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Service("staffOrganizationManager")
@Scope("prototype")
@SuppressWarnings({ "unchecked" })
public class StaffOrganizationManagerImpl implements StaffOrganizationManager {

	@Resource
	private StaffOrganizationDao staffOrganizationDao;
	@Resource
	private StaffDao staffDao;
	@Resource
	private OrganizationDao organizationDao;
	@Resource
	private OperateHrManager operateHrManager;
	@Resource
	private PositionManager positionManager;
	@Resource
	private StaffRoleManager staffRoleManager;
	@Resource
	private StaffPositionManager staffPostionManager;
	@Resource
	private PartyManager partyManager;

	@Override
	public PageInfo queryPageInfoStaffOrganization(
			StaffOrganization staffOrganization, int currentPage, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM STAFF_ORGANIZATION WHERE STATUS_CD = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (staffOrganization != null) {
			if (!StrUtil.isNullOrEmpty(staffOrganization.getStaffId())) {
				sql.append(" and STAFF_ID = ?");
				params.add(staffOrganization.getStaffId());
			}
		}
		return StaffOrganization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, StaffOrganization.class);
	}

	public PageInfo queryPageInfoByStaffOrganization(Staff staff,
			StaffOrganization staffOrganization, int currentPage, int pageSize) {

		StringBuffer sql = new StringBuffer(
				"SELECT SO.*, SA.STAFF_ACCOUNT FROM STAFF_ORGANIZATION SO LEFT JOIN STAFF_ACCOUNT SA ON SO.STAFF_ID = SA.STAFF_ID  WHERE SO.STATUS_CD = ? AND SA.STATUS_CD = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (staff != null && staffOrganization != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sql.append(" AND SA.STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffAccount().trim()) + "%");
			}

			if (staffOrganization.getOrgId() != null) {
				sql.append(" AND SO.ORG_ID= ?");
				params.add(staffOrganization.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getUserCode())) {
				sql.append(" AND SO.USERCODE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrganization.getUserCode().trim()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getRalaCd())) {
				sql.append(" AND SO.RALA_CD= ?");
				params.add(staffOrganization.getRalaCd().trim());
			}

			sql.append(" AND SO.STAFF_ID IN (SELECT ST.STAFF_ID FROM STAFF ST WHERE ST.STATUS_CD= ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffOrganization.getStaffId() != null) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staffOrganization.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sql.append(" AND ST.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName().trim()) + "%");
			}

			sql.append(")");

		}
		sql.append(" ORDER BY STAFF_SEQ,STAFF_ORG_ID");
		return StaffOrganization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, StaffOrganization.class);
	}

	public PageInfo queryPageInfoByStaffOrganizationNoStatusCd(Staff staff,
			StaffOrganization staffOrganization, int currentPage, int pageSize) {

		StringBuffer sql = new StringBuffer(
				"SELECT SO.*, SA.STAFF_ACCOUNT FROM STAFF_ORGANIZATION SO LEFT JOIN STAFF_ACCOUNT SA ON SO.STAFF_ID = SA.STAFF_ID  WHERE 1=1");

		List<Object> params = new ArrayList<Object>();

		if (staff != null && staffOrganization != null) {

			if (!StrUtil.isNullOrEmpty(staff.getStaffAccount())) {
				sql.append(" AND SA.STAFF_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffAccount().trim()) + "%");
			}

			if (staffOrganization.getOrgId() != null) {
				sql.append(" AND SO.ORG_ID= ?");
				params.add(staffOrganization.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getUserCode())) {
				sql.append(" AND SO.USERCODE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffOrganization.getUserCode().trim()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(staffOrganization.getRalaCd())) {
				sql.append(" AND SO.RALA_CD= ?");
				params.add(staffOrganization.getRalaCd().trim());
			}

			sql.append(" AND SO.STAFF_ID IN (SELECT ST.STAFF_ID FROM STAFF ST WHERE 1=1");

			if (staffOrganization.getStaffId() != null) {
				sql.append(" AND ST.STAFF_ID = ?");
				params.add(staffOrganization.getStaffId());
			}

			if (!StrUtil.isNullOrEmpty(staff.getStaffName())) {
				sql.append(" AND ST.STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staff.getStaffName().trim()) + "%");
			}

			sql.append(")");

		}
		sql.append(" ORDER BY SO.UPDATE_DATE DESC");
		return StaffOrganization.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, StaffOrganization.class);
	}

	@Override
	public void removeStaffOrganization(StaffOrganization staffOrganization) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrganization.setBatchNumber(batchNumber);
		if (staffOrganization.getRemoveNeedUpdateStaffOrganization() != null) {
			// 删除归属关系：如果另外一条不是归属则将其设置为归属关系（先删除旧的非归属关系新增一条新的归属关系）
			if (!BaseUnitConstants.RALA_CD_1.equals(staffOrganization
					.getRemoveNeedUpdateStaffOrganization().getRalaCd())) {
				StaffOrganization addSo = new StaffOrganization();
				StaffOrganization delSo = staffOrganization
						.getRemoveNeedUpdateStaffOrganization();
				BeanUtils.copyProperties(addSo, delSo);
				addSo.setUuid(StrUtil.getUUID());
				addSo.setBatchNumber(batchNumber);
				addSo.setRalaCd(BaseUnitConstants.RALA_CD_1);
				addSo.add();
				/**
				 * 员工添加角色
				 */
				staffRoleManager.updateBatchStaffRoleRela(addSo);
				delSo.setBatchNumber(batchNumber);
				delSo.remove();
			}
		}
		positionManager.removeStaffPostionByOrganization(staffOrganization);
		staffOrganization.remove();
	}

	@Override
	public void updateStaffOrganization(StaffOrganization staffOrganization) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrganization.setBatchNumber(batchNumber);
		staffOrganization.update();
		/**
		 * 员工添加角色
		 */
		staffRoleManager.updateBatchStaffRoleRela(staffOrganization);
	}

	@Override
	public void updateStaffOrganizationRelation(
			StaffOrganization staffOrganization) {
		// 20131024修改关系逻辑改为删除旧的关系，复制旧的关系信息新增新的一条关系
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrganization.setBatchNumber(batchNumber);
		staffOrganization.add();
		/**
		 * 员工添加角色
		 */
		staffRoleManager.updateBatchStaffRoleRela(staffOrganization);
		if (staffOrganization.getNeedRemoveList() != null
				&& staffOrganization.getNeedRemoveList().size() > 0) {
			for (StaffOrganization removeStaffOrganization : staffOrganization
					.getNeedRemoveList()) {
				removeStaffOrganization.setBatchNumber(batchNumber);
				removeStaffOrganization.remove();
			}
		}
		if (staffOrganization.getNeedUpdateToRela3List() != null
				&& staffOrganization.getNeedUpdateToRela3List().size() > 0) {
			for (StaffOrganization temp : staffOrganization
					.getNeedUpdateToRela3List()) {
				if (!BaseUnitConstants.RALA_CD_3.equals(temp.getRalaCd())) {
					StaffOrganization newStaffOrganization = new StaffOrganization();
					newStaffOrganization.setBatchNumber(batchNumber);
					BeanUtils.copyProperties(newStaffOrganization, temp);
					newStaffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_3);
					newStaffOrganization.add();
					temp.setBatchNumber(batchNumber);
					temp.remove();
				}
			}
		}
		if (staffOrganization.getNeedUpdateToRela1List() != null
				&& staffOrganization.getNeedUpdateToRela1List().size() > 0) {
			for (StaffOrganization temp : staffOrganization
					.getNeedUpdateToRela1List()) {
				if (!BaseUnitConstants.RALA_CD_1.equals(temp.getRalaCd())) {
					StaffOrganization newStaffOrganization = new StaffOrganization();
					newStaffOrganization.setBatchNumber(batchNumber);
					BeanUtils.copyProperties(newStaffOrganization, temp);
					newStaffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);
					newStaffOrganization.add();
					/**
					 * 员工添加角色
					 */
					staffRoleManager
							.updateBatchStaffRoleRela(newStaffOrganization);
					temp.setBatchNumber(batchNumber);
					temp.remove();
				}
			}
		}
	}

	@Override
	public void addStaffOrganization(StaffOrganization staffOrganization) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		/**
		 * 员工组织关系新增增加UUID
		 */
		staffOrganization.setUuid(StrUtil.getUUID());
		staffOrganization.setBatchNumber(batchNumber);
		staffOrganization.add();

		/**
		 * 员工添加角色
		 */
		staffRoleManager.updateBatchStaffRoleRela(staffOrganization);

		/**
		 * 新增归属关系的时候：20131023修改为先删除原归属关系，复制旧的归属关系信息新增兼职
		 */
		List<StaffOrganization> updateList = staffOrganization
				.getNeedUpdateStaffOrganizationlist();
		if (updateList != null && updateList.size() > 0) {
			for (StaffOrganization so : updateList) {
				if (BaseUnitConstants.RALA_CD_1.equals(so.getRalaCd())) {

					StaffOrganization newStaffOrganization = new StaffOrganization();
					BeanUtils.copyProperties(newStaffOrganization, so);
					newStaffOrganization.setUuid(StrUtil.getUUID());
					newStaffOrganization.setBatchNumber(batchNumber);
					newStaffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_3);
					newStaffOrganization.add();

					so.setBatchNumber(batchNumber);
					so.remove();
				}
			}
		}
	}

	@Override
	public StaffOrganization getById(Long id) {
		return (StaffOrganization) StaffOrganization.repository().getObject(
				StaffOrganization.class, id);
	}

	@Override
	public void removeStaffOrganizationList(
			List<StaffOrganization> staffOrganizationList) {
		for (StaffOrganization staffOrganization : staffOrganizationList) {
			this.removeStaffOrganization(staffOrganization);
		}
	}

	@Override
	public void updateBatchMoveStaffOrganization(
			List<StaffOrganization> staffOrganizationList,
			Organization targetOrganization) {
		/*
		 * zhulintao 2014-07-01 修复同一个员工从不同组织中移动到同一个组织中时，产生重复的员工组织关系。【
		 * 同一员工在同一组织中只能有一种关系
		 * 】以及用staffFlag来判断staffOrganizationList中的数据是同一个员工【不同组织下的同一个员工
		 * 】，还是不同的员工【同一个组织下不同的员工】 true表示同一个员工，false表示不同的员工
		 * ralaCd1Flag值为true时，表示该组织关系是主归属。为false时表示是其他关系
		 */
		boolean staffFlag = false;
		boolean ralaCd1Flag = false;

		String batchNumber = OperateLog.gennerateBatchNumber();

		if (targetOrganization != null && targetOrganization.getOrgId() != null) {
			for (int i = 0; i < staffOrganizationList.size(); i++) {

				StaffOrganization staffOrganization = new StaffOrganization();
				staffOrganization = staffOrganizationList.get(i);

				/**
				 * 移除员工岗位，盼盼要求-2013/8/14，修改人faq
				 */
				positionManager
						.removeStaffPostionByOrganization(staffOrganization);
				/**
				 * 修改移动员工为删除旧的，再新增
				 */
				staffOrganization.setBatchNumber(batchNumber);
				staffOrganization.remove();
				StaffOrganization newStaffOrganization = new StaffOrganization();
				BeanUtils.copyProperties(newStaffOrganization,
						staffOrganization);
				newStaffOrganization.setOrgId(targetOrganization.getOrgId());
				/**
				 * zhulintao 2014-07-01
				 * 查询是否存在归属组织【排除自身】，如果存在就将复制过来的RALA_CD中为1的改成3【兼职】，
				 * RALA_CD的其他值保持不变
				 */
				StaffOrganization queryStaffOrganization = new StaffOrganization();
				queryStaffOrganization.setStaffId(staffOrganization
						.getStaffId());
				queryStaffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);
				List<StaffOrganization> oldStaffOrganizationList = this
						.queryStaffOrganizationList(queryStaffOrganization);
				if (oldStaffOrganizationList != null
						&& oldStaffOrganizationList.size() > 0) {
					for (StaffOrganization oldStaffOrganization : staffOrganizationList) {
						if (!staffOrganization.getStaffOrgId().equals(
								oldStaffOrganization.getStaffOrgId())
								&& staffOrganization.getRalaCd().equals(
										BaseUnitConstants.RALA_CD_1)) {
							newStaffOrganization
									.setRalaCd(BaseUnitConstants.RALA_CD_3);
						}
					}
				}

				if (staffOrganizationList != null
						&& staffOrganizationList.size() > 1) {
					if (staffOrganizationList.get(0).getStaffId()
							.equals(staffOrganizationList.get(i).getStaffId())) {
						staffFlag = true;
					} else {
						staffFlag = false;
					}
				}

				/*
				 * 如果是不同的员工移动到同一个组织则全部添加员工组织关系 如果是同一个员工从不同组织移动到同一个组织中，只取第一条记录
				 */
				if (!staffFlag) {
					this.addStaffOrganization(newStaffOrganization);
				} else if (staffFlag) {
					if (newStaffOrganization.getRalaCd().equals(
							BaseUnitConstants.RALA_CD_1)) {
						this.addStaffOrganization(newStaffOrganization);
						ralaCd1Flag = true;
					} else if (!newStaffOrganization.getRalaCd().equals(
							BaseUnitConstants.RALA_CD_1)
							&& i == staffOrganizationList.size() - 1) {
						if (!ralaCd1Flag) {
							this.addStaffOrganization(newStaffOrganization);
						}
					}
				}
			}
		}
	}
	
	@Override
	public List<NodeVo> queryStaffOrgNodeVoList(Staff staff) {
		StaffOrganization staffOrganization = new StaffOrganization();
		staffOrganization.setStaffId(staff.getStaffId());
		List<StaffOrganization> staffOrganizationList = queryStaffOrganizationList(staffOrganization);
		if(staffOrganizationList != null && !staffOrganizationList.isEmpty()) {
			List<NodeVo> staffOrgNodeVoList = new ArrayList<NodeVo>();
			
			for(StaffOrganization so : staffOrganizationList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(so.getOrgId().toString());
				nodeVo.setName(so.getOrganizationName());
				nodeVo.setDesc(so.getOrganizationName());
				
				staffOrgNodeVoList.add(nodeVo);
			}
			return staffOrgNodeVoList;
		} else {
			return null;
		}
		
		
		
	}

	@Override
	public List<StaffOrganization> queryStaffOrganizationList(
			StaffOrganization staffOrganization) {
		StringBuffer hql = new StringBuffer(
				"From StaffOrganization where statusCd=?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (staffOrganization != null) {
			if (!StrUtil.isNullOrEmpty(staffOrganization.getRalaCd())) {
				hql.append(" and ralaCd=?");
				params.add(staffOrganization.getRalaCd());
			}
			if (staffOrganization.getOrgId() != null) {
				hql.append(" and orgId=?");
				params.add(staffOrganization.getOrgId());
			}
			if (!StrUtil.isNullOrEmpty(staffOrganization.getStaffId())) {
				hql.append(" and staffId=?");
				params.add(staffOrganization.getStaffId());
			}
		}
		return StaffOrganization.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

	/**
	 * 检查userCode是否被使用过
	 * 
	 * @return
	 * @author Wangy 2013-7-5
	 */
	public boolean isExitsUserCode(String userCode) {
		String sql = "SELECT COUNT(1) FROM STAFF_ORGANIZATION T WHERE T.USERCODE=?";
		int count = StaffOrganization.repository().getJdbcTemplate()
				.queryForInt(sql, new Object[] { userCode });
		if (count > 0) {
			return true;
		}
		return false;
	}
	

	public String getOrgUserCode() {
		String seqUserCode = staffOrganizationDao.getSeqOrgUserCode();
		seqUserCode = "OA" + seqUserCode;
		return seqUserCode;
	}

	/**
	 * 同一个员工的归属关系只能有一个 {@inheritDoc}
	 * 
	 * @see cn.ffcs.uom.organization.manager.StaffOrganizationManager#isCheckStaffOrgRalaCd(cn.ffcs.uom.organization.model.StaffOrganization)
	 * @author Wangy 2013-7-11
	 */
	public String isCheckStaffOrgRalaCd(StaffOrganization staffOrganization,
			String opType) {
		List<Object> params = new ArrayList<Object>();
		String hql = " FROM StaffOrganization m where m.staffId=? and m.statusCd=?";
		params.add(staffOrganization.getStaffId());
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<StaffOrganization> sffOrgList = StaffOrganization.repository()
				.findListByHQLAndParams(hql, params);
		if (null != sffOrgList && sffOrgList.size() > 0) {
			for (StaffOrganization sff : sffOrgList) {
				if (SffOrPtyCtants.ADD.equals(opType)) {
					if (staffOrganization.getOrgId().equals(sff.getOrgId())) {
						return SffOrPtyCtants.RESULT_9; // 新增已存在组织
					}
				}

				if (BaseUnitConstants.RALA_CD_1.equals(sff.getRalaCd())
						&& BaseUnitConstants.RALA_CD_1.equals(staffOrganization
								.getRalaCd())) {
					return SffOrPtyCtants.RESULT_1; // 已存在归属关系
				} else if (!BaseUnitConstants.RALA_CD_1
						.equals(staffOrganization.getRalaCd())) {
					return SffOrPtyCtants.RESULT_2; // 新增非归属组织关系
				}
			}
		}

		// String
		// sql="SELECT COUNT(1) FROM STAFF_ORGANIZATION T WHERE ROWNUM=1 AND T.STAFF_ID=? AND T.STATUS_CD=?";
		// int
		// count=StaffOrganization.repository().getJdbcTemplate().queryForInt(sql,
		// new
		// Object[]{staffOrganization.getStaffId(),BaseUnitConstants.ENTT_STATE_ACTIVE});
		// if(count==0){
		// return SffOrPtyCtants.RESULT_0; //首次新增员工组织关系
		// }
		// String ralaCd = staffOrganization.getRalaCd();
		// if(BaseUnitConstants.RALA_CD_1.equals(ralaCd)){
		// sql="SELECT COUNT(1) FROM STAFF_ORGANIZATION T WHERE ROWNUM=1 AND T.STAFF_ID=? AND T.STATUS_CD=? AND T.RALA_CD=?";
		// count =
		// StaffOrganization.repository().getJdbcTemplate().queryForInt(sql, new
		// Object[]{staffOrganization.getStaffId(),BaseUnitConstants.ENTT_STATE_ACTIVE,ralaCd});
		// if(count>0){
		// return SffOrPtyCtants.RESULT_1; //已存在归属关系
		// }
		// }
		return SffOrPtyCtants.RESULT_0; // 首次新增员工组织关系;
	}

	public void updateStaffOrgRalaCd(final StaffOrganization staffOrganization) {
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("UPDATE STAFF_ORGANIZATION T SET T.RALA_CD=")
				.append(BaseUnitConstants.RALA_CD_3)
				.append(" WHERE T.STAFF_ID=? AND T.RALA_CD=? AND T.STATUS_CD=?");
		String sql = sbSQL.toString();
		Object[] objs = new Object[] { staffOrganization.getStaffId(),
				BaseUnitConstants.RALA_CD_1,
				BaseUnitConstants.ENTT_STATE_ACTIVE };
		StaffOrganization.repository().getJdbcTemplate().update(sql, objs);
	}

	@Override
	public void addStaffOrganizationWithParty(
			StaffOrganization staffOrganization) throws Exception {
		String batchNumber = OperateLog.gennerateBatchNumber();
		staffOrganization = changePartyFieldFromOperateHrWhileAdding(staffOrganization);
		Party party = staffOrganization.getPartyObj();
		if (null != party) {
			party.setBatchNumber(batchNumber);
			party.add();
			Long partyId = party.getPartyId();
			PartyRole partyRole = party.getPartyRole();
			Individual indivi = party.getIndividual();
			PartyOrganization prtOrg = party.getPartyOrganization();
			PartyCertification ptyCerfic = party.getPartyCertification();
			PartyContactInfo ptyConInfo = party.getPartyContactInfo();
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
			Staff staff = staffOrganization.getStaffObj();
			if (null != staff) {
				staff.setPartyRoleId(Long.parseLong(party.getPartyRoleId()));
				staff.setBatchNumber(batchNumber);
				staff.add();
				StaffAccount sa = staff.getObjStaffAccount();
				sa.setStaffId(staff.getStaffId());
				sa.setBatchNumber(batchNumber);
				sa.add();
				
				List<StaffRole> staffRoles = staffOrganization.getAddStaffRoles();
				if (staffRoles!=null && staffRoles.size()>0) {
	                List<Staff> staffList = new ArrayList<Staff>();
	                staffList.add(staff);
	                staffRoleManager.saveStaffRoleRela(staffRoles, staffList);
                }

				staffOrganization.setStaffId(staff.getStaffId());
				if (SffOrPtyCtants.RESULT_9.equals(this.ralaCdRelation(
						staffOrganization, SffOrPtyCtants.ADD))) {
					ZkUtil.showError("该员工在该组织下已存在该关系，请确认", "提示信息");
					throw new Exception();
				}

				List<StaffExtendAttr> staffExtendAttrList = staff
						.getStaffExtendAttr();

				if (null != staffExtendAttrList
						&& staffExtendAttrList.size() > 0) {

					for (StaffExtendAttr staffExtendAttr : staffExtendAttrList) {
						staffExtendAttr.setStaffId(staff.getStaffId());
						staffExtendAttr.setBatchNumber(batchNumber);
						staffExtendAttr.add();
					}

				}
				staffOrganization.setBatchNumber(batchNumber);
				staffOrganization.add();
				/**
				 * 员工添加角色
				 */
				staffRoleManager.updateBatchStaffRoleRela(staffOrganization);
			}
		}
	}

	@Override
	public void updateStaffOrganizationWithParty(
			StaffOrganization staffOrganization) throws Exception {
		String batchNumber = OperateLog.gennerateBatchNumber();
		Party party = staffOrganization.getPartyObj();
		if (null != party) {
			party.setBatchNumber(batchNumber);
			party.update();
			Long partyId = party.getPartyId();
			PartyRole partyRole = party.getPartyRole();
			Individual indivi = party.getIndividual();
			PartyOrganization prtOrg = party.getPartyOrganization();
			PartyCertification ptyCerfic = party.getPartyCertification();
			PartyContactInfo ptyConInfo = party.getPartyContactInfo();
			if (null != partyRole) {
				partyRole.setPartyId(partyId);
				if (null == partyRole.getPartyRoleId()) {
					partyRole.setBatchNumber(batchNumber);
					partyRole.add();
				} else {
					partyRole.setBatchNumber(batchNumber);
					partyRole.update();
				}
			}
			if (null != indivi) {
				indivi.setPartyId(partyId);
				if (null == indivi.getIndividualId()) {
					indivi.setBatchNumber(batchNumber);
					indivi.add();
				} else {
					indivi.setBatchNumber(batchNumber);
					indivi.update();
				}
			}
			if (null != prtOrg) {
				prtOrg.setPartyId(partyId);
				if (null == prtOrg.getPratyOrganizationId()) {
					prtOrg.setBatchNumber(batchNumber);
					prtOrg.add();
				} else {
					prtOrg.setBatchNumber(batchNumber);
					prtOrg.update();
				}
			}
			if (null != ptyCerfic) {
				ptyCerfic.setPartyId(partyId);
				if (null == ptyCerfic.getPartyCertId()) {
					ptyCerfic.setBatchNumber(batchNumber);
					ptyCerfic.add();
				} else {
					ptyCerfic.setBatchNumber(batchNumber);
					ptyCerfic.update();
				}
			}
			if (null != ptyConInfo) {
				ptyConInfo.setPartyId(partyId);
				if (null == ptyConInfo.getContactId()) {
					ptyConInfo.setBatchNumber(batchNumber);
					ptyConInfo.add();
				} else {
					ptyConInfo.setBatchNumber(batchNumber);
					ptyConInfo.update();
				}
			}
			Staff staff = staffOrganization.getStaffObj();
			if (null != staff) {
				staff.setPartyRoleId(partyRole.getPartyRoleId());
				if (null == staff.getStaffId()) {
					staff.setBatchNumber(batchNumber);
					staff.add();
				} else {
					staff.setBatchNumber(batchNumber);
					staff.update();
				}
				StaffAccount sa = staff.getObjStaffAccount();
				sa.setStaffId(staff.getStaffId());
				if (null == sa.getStaffAccountId()) {
					sa.setBatchNumber(batchNumber);
					sa.add();
				} else {
					sa.setBatchNumber(batchNumber);
					sa.update();
				}

				List<StaffExtendAttr> staffExtendAttrList = staff
						.getStaffExtendAttr();

				if (null != staffExtendAttrList
						&& staffExtendAttrList.size() > 0) {
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

				staffOrganization.setStaffId(staff.getStaffId());
				// if (null == staffOrganization.getStaffOrgId()) {
				// staffOrganization.setBatchNumber(batchNumber);
				// staffOrganization.add();
				// } else {
				// staffOrganization.setBatchNumber(batchNumber);
				// staffOrganization.update();
				// }
			}
            List<StaffRole> addStaffRoles = staffOrganization.getAddStaffRoles();
            List<StaffRole> delStaffRoles = staffOrganization.getDelStaffRoles();
            if (addStaffRoles!=null && addStaffRoles.size()>0) {
                List<Staff> staffList = new ArrayList<Staff>();
                staffList.add(staff);
                staffRoleManager.saveStaffRoleRela(addStaffRoles, staffList);
            }
            if (delStaffRoles!=null && delStaffRoles.size()>0) {
                staffRoleManager.removeStaffRoleRela(delStaffRoles, staff);
            }
		}
	}

	/**
	 * 新增或修改员工组织关系的时候 判断
	 * 
	 * @param staffOrganization
	 * @author Wangy 2013-7-15
	 */
	private String ralaCdRelation(StaffOrganization staffOrganization,
			String opType) {
		// 版本暂时不上
		String resu = this.isCheckStaffOrgRalaCd(staffOrganization, opType);
		if (SffOrPtyCtants.RESULT_0.equals(resu)) {
			staffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);
		} else if (SffOrPtyCtants.RESULT_1.equals(resu)) {
			this.updateStaffOrgRalaCd(staffOrganization);
		}
		return resu;
	}

	@Override
	public StaffOrganization changePartyFieldFromOperateHrWhileAdding(
			StaffOrganization staffOrganization) throws Exception {
		if (null != staffOrganization) {
			Party party = staffOrganization.getPartyObj();
			if (null != party) {
				PartyCertification pcf = party.getPartyCertification();
				PartyCertification pcfTemp = new PartyCertification();
				pcfTemp.setCertNumber(pcf.getCertNumber());
				pcfTemp.setCertType(pcf.getCertType());
				List<PartyCertification> pcfList = partyManager.getPartyCertificationList(pcfTemp);
				boolean isExist = false;
				if (pcfList != null && pcfList.size() > 0) {
					isExist = true;
				}
				if (null != pcf) {
					OperateHr operateHr = null;
					OperateHr operateHr18 = null;
					OperateHr operateHr15 = null;
					if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
							&& null != pcf.getCertNumber()
							&& (PartyConstant.ATTR_VALUE_IDNO15 == pcf
									.getCertNumber().trim().length())) {
						operateHr = operateHrManager
								.queryOperateHrByCertNum(pcf.getCertNumber());
					}
					if (PartyConstant.ATTR_VALUE_IDNO.equals(pcf.getCertType())
							&& null != pcf.getCertNumber()
							&& (PartyConstant.ATTR_VALUE_IDNO18 == pcf
									.getCertNumber().trim().length())) {
						String certNum = CertUtil.getIDCard_15bit(pcf
								.getCertNumber());
						operateHr15 = operateHrManager
								.queryOperateHrByCertNum(certNum);
						operateHr18 = operateHrManager
								.queryOperateHrByCertNum(pcf.getCertNumber());
						if (null != operateHr18) {
							operateHr = operateHr18;
						} else if (null != operateHr15) {
							operateHr = operateHr15;
						}
					}
					if (null != operateHr) {
						try {
							if (!isExist) {
								Messagebox.show("侦测到存在该身份证对应的人力信息，系统已自动更新参与人的姓名、身份证、性别、生日、员工的工号、员工的账号、用户性质。");
							}else{
								Messagebox.show("侦测到存在该身份证对应的人力信息，系统已自动更新参与人的姓名、身份证、性别、生日。");
							}

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
						Staff staff = staffOrganization.getStaffObj();
						if (null != staff && !isExist) {
							staff.setWorkProp(operateHr.getWorkPop());
							String staffNbr = operateHr.getPsnCode();
							staff.setStaffNbr(staffNbr);
							StaffAccount sa = staff.getObjStaffAccount();
							if (null != sa) {
								String staffAccStr = "";
								if (SffOrPtyCtants.WORKPROP_N_H
										.equals(operateHr.getWorkPop())
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
									} else {
										throw new Exception("从人力中间表获取的工号为空。");
									}
								}
							}
						}
					}
				}
			}
		}
		return staffOrganization;
	}

	/**
	 * 目的不区分内部网点或外部网点【使用时再用下面的代码】 员工组织关系规则验证 - 公共规则控制（按上述业务规则）：
	 * 1、用工性质为“代理商人员”的，只能归属一个代理商网点，即“一个代理商人员只能归属一个网点下，且该网点能关联到代理商（经营主体中的一种）”;
	 * 2、用工性质为非“代理商人员”的，可以归属多个组织，但最多只能属于一个内部网点（即组织类型为网点，网点能关联到内部经营实体（经营主体中的一种）），
	 * 且该人员不允许挂载在代理商网点（能关联到代理商的网点）下;
	 * 3、代理商管理界面： 在录入商、店、员时，员工的用工性质锁定为“代理商人员”，非“代理商人员”不允许出现在代理商组织节点下;
	 * 4、内部组织管理界面：内部组织下录入建立人员关系时，不允许人员用工性质为“代理商人员”;
	 * 
	 * @param staff
	 * @param sourceOrganization
	 * @param targetOrganization
	 * @return
	 */
	@Override
	public String doStaffOrgRelRule(Staff staff,
			Organization sourceOrganization, Organization targetOrganization) {

		if (!StrUtil.isNullOrEmpty(targetOrganization)
				&& targetOrganization.getOrgId() != null) {

			List<OrgType> orgTypeList = targetOrganization.getOrgTypeList();
			//一个组织可以有多个组织关系，属于不同的组织树
			List<OrganizationRelation> organizationRelationList = targetOrganization
					.getOrganizationRelationList();

			boolean isIntNetwork = false;// 内部网点
			boolean isExtNetwork = false;// 外部网点
			boolean isAgent = false;// 代理商
			boolean isIntBusinessEntity = false;// 内部经营实体
			boolean isIntRela = false;// 上级管理机构
			boolean isExtRela = false;// 上级管理机构(外部)

			List<StaffOrganization> existList = null;

			// 查询该员工是否有其他员工组织关系
			if (!StrUtil.isNullOrEmpty(staff.getStaffId())) {
				StaffOrganization vo = new StaffOrganization();
				vo.setStaffId(staff.getStaffId());
				existList = this.queryStaffOrganizationList(vo);
			}

			// 判断该组织是内部网点或外部网点
			if (orgTypeList != null && orgTypeList.size() > 0) {
				for (OrgType orgType : orgTypeList) {

					if (orgType.getOrgTypeCd().equals(
							OrganizationConstant.ORG_TYPE_N0202010000)
							|| orgType.getOrgTypeCd().equals(
									OrganizationConstant.ORG_TYPE_N0202030000)
							|| orgType.getOrgTypeCd().equals(
									OrganizationConstant.ORG_TYPE_N0202040000)) {
						isIntNetwork = true;// 该组织是内部网点【目前不做区分内外部网点】
					}

					if (orgType.getOrgTypeCd().equals(
							OrganizationConstant.ORG_TYPE_N0202020000)
							|| orgType.getOrgTypeCd().equals(
									OrganizationConstant.ORG_TYPE_N0202050000)
							|| orgType.getOrgTypeCd().equals(
									OrganizationConstant.ORG_TYPE_N0202060000)) {
						isExtNetwork = true;// 该组织是外部网点【目前不做区分内外部网点】
					}

				}
			}

			if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
					&& staff.getWorkProp().equals(
							SffOrPtyCtants.WORKPROP_W_AGENT)) { //代理商用工性质

				// 代理商员工只能有一个员工组织关系,且归属组织必需是有经营主体的网点
				if (existList != null && existList.size() > 0) {
					for (StaffOrganization staffOrg : existList) {
						if (!targetOrganization.getId().equals(
								staffOrg.getOrgId())) {//只要有一个除了自己以外的员工组织关系，条件1归属一个代理商网点
							if (!StrUtil.isNullOrEmpty(sourceOrganization)) {
								if (!sourceOrganization.getId().equals(
										staffOrg.getOrgId())) {
									return "该代理商员工已经挂在一个外部网点上，不能再次添加员工组织关系.";
								}
							} else {
								return "该代理商员工已经挂在一个外部网点上，不能再次添加员工组织关系.";
							}
						}
					}
				}

				if (isExtNetwork || isIntNetwork) {
					if (organizationRelationList != null
							&& organizationRelationList.size() > 0) {
					    //该网点能关联到代理商.能表示只要有一个就可以了
						for (OrganizationRelation organizationRelation : organizationRelationList) {

							if (organizationRelation.getRelaCd().equals(
									OrganizationConstant.RELA_CD_INNER)) {
								isIntRela = true;// 内部关系
							}

							if (organizationRelation.getRelaCd().equals(
									OrganizationConstant.RELA_CD_EXTER)) {

								isExtRela = true;// 外部关系

								Organization agentOrganization = new Organization();
								agentOrganization.setOrgId(organizationRelation
										.getRelaOrgId());
								List<OrgType> agentOrgTypeList = agentOrganization
										.getOrgTypeList();

								// 判断该组织上级是否是代理商组织
								if (agentOrgTypeList != null
										&& agentOrgTypeList.size() > 0) {
									for (OrgType agentOrgType : agentOrgTypeList) {

										if (agentOrgType
												.getOrgTypeCd()
												.equals(OrganizationConstant.ORG_TYPE_AGENT)) {
											isAgent = true;// 该组织的上级组织是代理商组织
										}

									}
								}

							}

						}

						if (!isIntRela) {
							return "该员工所属的网点没有上级管理机构关系，不能添加代理商员工.";
						}

						if (!isExtRela) {
							return "该员工所属的网点没有上级管理机构(外部)关系，不能添加代理商员工.";
						} else if (!isAgent) {
							return "该员工所属网点的上级组织不是代理商，不能添加代理商员工.";
						}

					} else {
						return "该网点没有经营主体，不能添加代理商员工.";
					}

				} else {
					return "该组织不是外部网点，不能添加代理商员工.";
				}

			} else {

				if (isIntNetwork || isExtNetwork) {

					if (organizationRelationList != null
							&& organizationRelationList.size() > 0) {

						for (OrganizationRelation organizationRelation : organizationRelationList) {

							if (organizationRelation.getRelaCd().equals(
									OrganizationConstant.RELA_CD_INNER)) {
								isIntRela = true;// 内部关系
							}

							if (organizationRelation.getRelaCd().equals(
									OrganizationConstant.RELA_CD_EXTER)) {

								isExtRela = true;// 外部关系

								Organization intBusEntOrganization = new Organization();
								intBusEntOrganization
										.setOrgId(organizationRelation
												.getRelaOrgId());
								List<OrgType> intBusEntOrgTypeList = intBusEntOrganization
										.getOrgTypeList();

								// 判断该组织上级是否是内部经营实体组织
								if (intBusEntOrgTypeList != null
										&& intBusEntOrgTypeList.size() > 0) {
									for (OrgType intBusEntOrgType : intBusEntOrgTypeList) {

										if (intBusEntOrgType
												.getOrgTypeCd()
												.equals(OrganizationConstant.ORG_TYPE_N0903000000)) {
											isIntBusinessEntity = true;// 该组织的上级组织是内部经营实体
										}

									}
								}

							}
						}

						if (!isIntRela) {
							return "该员工所属的网点没有上级管理机构关系，不能添加该员工.";
						}

						if (!isExtRela) {
							return "该员工所属的网点没有上级管理机构(外部)关系，不能添加该员工.";
						} else if (!isIntBusinessEntity) {
							return "该员工所属网点的上级组织不是内部经营实体，不能添加该员工.";
						}

					}

					/**
					 * 用工性质为非“代理商人员”的，可以归属多个组织，但最多只能属于一个内部网点（即组织类型为网点，
					 * 且上级组织的组织类型是内部经营实体）
					 */
					if (existList != null && existList.size() > 0) {

						boolean isNewIntNetwork = false;// 存在内部网点的组织
						boolean isNewExtNetwork = false;// 存在外部网点的组织
						boolean isNewIntBusinessEntity = false;// 存在上级组织是内部经营实体的组织

						for (StaffOrganization staffOrg : existList) {

							if (!targetOrganization.getId().equals(
									staffOrg.getOrgId())) {

								Organization newOrganization = new Organization();

								if (!StrUtil.isNullOrEmpty(sourceOrganization)) {
									if (!sourceOrganization.getId().equals(
											staffOrg.getOrgId())) {
										newOrganization.setOrgId(staffOrg
												.getOrgId());
									}
								} else {
									newOrganization.setOrgId(staffOrg
											.getOrgId());
								}

								List<OrgType> newOrgTypeList = newOrganization
										.getOrgTypeList();
								// 判断该组织是不是内部网点
								if (newOrgTypeList != null
										&& newOrgTypeList.size() > 0) {
									for (OrgType orgType : newOrgTypeList) {
										if (orgType
												.getOrgTypeCd()
												.equals(OrganizationConstant.ORG_TYPE_N0202010000)
												|| orgType
														.getOrgTypeCd()
														.equals(OrganizationConstant.ORG_TYPE_N0202030000)
												|| orgType
														.getOrgTypeCd()
														.equals(OrganizationConstant.ORG_TYPE_N0202040000)) {
											isNewIntNetwork = true;// 存在内部网点的组织
										}

										if (orgType
												.getOrgTypeCd()
												.equals(OrganizationConstant.ORG_TYPE_N0202020000)
												|| orgType
														.getOrgTypeCd()
														.equals(OrganizationConstant.ORG_TYPE_N0202050000)
												|| orgType
														.getOrgTypeCd()
														.equals(OrganizationConstant.ORG_TYPE_N0202060000)) {
											isNewExtNetwork = true;// 该组织是外部网点【目前不做区分内外部网点】
										}
									}
								}

								if (isNewIntNetwork || isNewExtNetwork) {// 查询该组织的上级是不是内部经营实体

									List<OrganizationRelation> newOrganizationRelationList = newOrganization
											.getOrganizationRelationList();

									if (newOrganizationRelationList != null
											&& newOrganizationRelationList
													.size() > 0) {
										for (OrganizationRelation newOrganizationRelation : newOrganizationRelationList) {
											Organization intBusEntOrganization = new Organization();
											intBusEntOrganization
													.setOrgId(newOrganizationRelation
															.getRelaOrgId());
											List<OrgType> intBusEntOrgTypeList = intBusEntOrganization
													.getOrgTypeList();

											// 判断该组织上级是否是内部经营实体组织
											if (intBusEntOrgTypeList != null
													&& intBusEntOrgTypeList
															.size() > 0) {
												for (OrgType intBusEntOrgType : intBusEntOrgTypeList) {
													if (intBusEntOrgType
															.getOrgTypeCd()
															.equals(OrganizationConstant.ORG_TYPE_N0903000000)) {
														isNewIntBusinessEntity = true;// 该组织的上级组织是内部经营实体
													}

												}
											}
										}
									}
								}
							}
						}

						if (isNewIntBusinessEntity) {
							return "该员工已经挂在一个内部网点上，不能再挂内部网点.";
						}

					}

				}
			}

		}

		return null;

	}
	
	/**
     * 获取校验返回信息
     * 
     * @param i
     *            行
     * @param j
     *            列
     * @param validateType
     *            校验类型
     * @return
     */
    private String getValidateMsg(int rowNumber, int colNumber,
            String validateType, StringBuffer sb)
    {
        sb.setLength(0);
        if (StaffOrganizationConstant.NULL_OR_EMPTY.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.NULL_OR_EMPTY_STR)
                    .append("的信息； ").toString();
        }
        else if (StaffOrganizationConstant.FIELD_REPEAT.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.FIELD_REPEAT_STR)
                    .append("的信息； ").toString();
        }
        else if (StaffOrganizationConstant.LENGTH_LIMIT.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.LENGTH_LIMIT_STR)
                    .append("的信息； ").toString();
        }
        else if (StaffOrganizationConstant.FIELD_ERROR.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.FIELD_ERROR_STR)
                    .append("的信息； ").toString();
        }
        else if (StaffOrganizationConstant.FIELD_NOT_EXIST.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.FIELD_NOT_EXIST_STR)
                    .append("； ").toString();
        }
        else if (StaffOrganizationConstant.FIELD_ERROR_VAL.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.FIELD_ERROR_VAL_STR)
                    .append("； ").toString();
        }
        else if (StaffOrganizationConstant.FIELD_EXIST_VAL.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(StaffOrganizationConstant.FIELD_EXIST_VAL_STR)
                    .append("； ").toString();
        }
        
        return "";
    }

    @Override
    public int checkUpLoadData(
        List<StaffOrganizationImportVo> waitUpLoadStaffOrganizationInfoList,
        List<String> checkInfoList, String[][] objDataArray, int totalColumn)
        throws Exception {
        
        //循环遍历所有数据，校验所有的数据
        int errorDataCount = 0;
        StringBuffer sb = new StringBuffer();
        
        for(int i = 0; i < objDataArray.length; ++i)
        {
            //一个临时变量
            StaffOrganizationImportVo staffOrganizationImportVo = new StaffOrganizationImportVo();
            
            for(int j = 0; j < totalColumn; ++j)
            {
                //当前的数据
                String s = "";
                //获取当前的一个单元格的数据
                if(objDataArray[i][j] != null)
                {
                    //去掉空格
                    s = objDataArray[i][j].trim();
                }
                String strs[] = null;
                //提取数据所有的数据用-划分
                if(!StrUtil.isEmpty(s))
                {
                    strs = s.split("-");
                }
                //switch校验所有的数据
                switch(j)
                {
                    case 0: //操作类型，非空，且指定操作方式
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            if(strs.length == 1)
                            {
                                //未知操作类型,FIELD_ERROR_STR = "格式不正确"
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_ERROR, sb));
                                break;
                            }
                            
                            //给出的s必须是规定的add,edit,del这三个,否则就是未知操作类型
                            if(strs[1].equals("add") || strs[1].equals("edit") || strs[1].equals("del"))
                            {
                                //获取操作类型
                                staffOrganizationImportVo.setOperation(strs[1]);
                            }
                            else
                            {
                                //未知操作类型,FIELD_ERROR_STR = "格式不正确"
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 1: // 变更原因，非空
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            staffOrganizationImportVo.setReason(s);
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 2: // 员工姓名，非空
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            staffOrganizationImportVo.setStaffName(s);
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 3: // 员工账号，非空且存在
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            //如果存在，判断员工账号是否存在,STAFF_ACCOUNT表
                            StaffAccount staffAccountTemp = new StaffAccount();
                            staffAccountTemp.setStaffAccount(s);
                            //根据账号查询是否存在这个员工账号列表
                            List<StaffAccount> staffList = staffDao.getStaffAccountList(staffAccountTemp);
                            //如果不为空，说明账号存在，否则账号不存在
                            if(staffList.size() > 0)
                            {
                                //账号存在
                                staffOrganizationImportVo.setStaffAccount(s);
                            }
                            else
                            {
                                //账号不存在
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_NOT_EXIST, sb));
                            }
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 4: //员工编码，可空
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            staffOrganizationImportVo.setStaffCode(s);
                        }
                        break;
                    case 5: //归属组织编码，非空，且存在
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            //如果存在，判断员工账号是否存在,Organization表
                            Organization orgTemp = null;
                            //根据组织编码查询是否存在这个组织
                            orgTemp = organizationDao.queryOrganizationByOrgCode(s);
                            //如果不为空，说明组织存在，否则组织不存在
                            if(orgTemp != null)
                            {
                                //组织存在
                                staffOrganizationImportVo.setOrgCode(s);
                                staffOrganizationImportVo.setOrg(orgTemp);
                            }
                            else
                            {
                                //组织不存在
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_NOT_EXIST, sb));
                            }
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 6: //  归属组织全称，可空，
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            staffOrganizationImportVo.setOrgFullName(s);
                        }
                        break;
                    case 7: //  归属关联类型， 必填且指定枚举值
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            //1-归属（主部门），2-挂职，3-兼职，4-借调， 9-其他
                            if (strs[0].equals("1") || strs[0].equals("2") || strs[0].equals("3")
                                || strs[0].equals("4") || strs[0].equals("9")) {
                                staffOrganizationImportVo.setRalaCd(strs[0]);
                            }
                            else
                            {
                                //数据格式不正确,不正确的值
                                //FIELD_ERROR_VAL
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_ERROR_VAL, sb));
                            }
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 8: //  排序号，非空，且为数字
                        if(!StrUtil.isNullOrEmpty(s))
                        {
                            if(StringUtils.isNumeric(s))
                            {
                                staffOrganizationImportVo.setStaffSeq(Long.valueOf(s));
                            }
                            else
                            {
                                //不是数值类型
                                ++errorDataCount;
                                checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.FIELD_ERROR_VAL_STR, sb));
                            }
                        }
                        else
                        {
                            //为空
                            ++errorDataCount;
                            checkInfoList.add(getValidateMsg(i, j, StaffOrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    default:
                        break;
                }//swith
            }
            
            //一行值校验结束
            waitUpLoadStaffOrganizationInfoList.add(staffOrganizationImportVo);
        }
        //所有数据校验结束
        return errorDataCount;
    }

    @Override
    public void saveOrEditOrDelStaffOrganiaztion(List<StaffOrganization> addStaffOrganizationList,
        List<StaffOrganization> editStaffOrganizationList, List<StaffOrganization> editStaffOrganizationRelationList,
        List<StaffOrganization> delStaffOrganizationList, List<StaffOrganization> delStaffList) {
        //新增
        for(StaffOrganization staffOrganizationadd : addStaffOrganizationList)
        {
            addStaffOrganization(staffOrganizationadd);
        }
        
        //修改,1、如果员工，组织，关联关系都没变直接做更新
        for(StaffOrganization staffOrganizationedit : editStaffOrganizationList)
        {
            updateStaffOrganization(staffOrganizationedit);
        }
        
        //如果非归属关系改为非归属关系，则修改；如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
        for(StaffOrganization staffOrganizationedit : editStaffOrganizationRelationList)
        {
            updateStaffOrganizationRelation(staffOrganizationedit);
        }
        
        //删除关系
        for(StaffOrganization staffOrganizationdel : delStaffOrganizationList)
        {
            //如果删除后只剩下一条其他关系，则将其设置为归属关系（先删除旧的兼职关系新增一条新的归属关系）
            removeStaffOrganization(staffOrganizationdel);
        }
        
        for(StaffOrganization staffOrganizationdel : delStaffList)
        {
            // 如果只有一条直接删除
        	String reason = staffOrganizationdel.getReason();
        	Staff staffTemp = staffOrganizationdel.getStaff();
        	staffTemp.setReason(reason);
            staffDao.delStaff(staffTemp);
        }
    }

	/**
	 * 目的区分内部网点或外部网点【使用时再用下面的代码】 员工组织关系规则验证 - 公共规则控制（按上述业务规则）：
	 * 1、用工性质为“代理商人员”的，只能归属一个代理商网点，即“一个代理商人员只能归属一个网点下，且该网点能关联到代理商（经营主体中的一种）”;
	 * 2、用工性质为非“代理商人员”的，可以归属多个组织，但最多只能属于一个内部网点（即组织类型为网点，网点能关联到内部经营实体（经营主体中的一种）），
	 * 且该人员不允许挂载在代理商网点（能关联到代理商的网点）下;
	 * 3、代理商管理界面： 在录入商、店、员时，员工的用工性质锁定为“代理商人员”，非“代理商人员”不允许出现在代理商组织节点下;
	 * 4、内部组织管理界面：内部组织下录入建立人员关系时，不允许人员用工性质为“代理商人员”;
	 * 
	 * @param staff
	 * @param sourceOrganization
	 * @param targetOrganization
	 * @return
	 * @Override public String doStaffOrgRelRule(Staff staff, Organization
	 *           sourceOrganization, Organization targetOrganization) {
	 * 
	 *           if (!StrUtil.isNullOrEmpty(targetOrganization) &&
	 *           targetOrganization.getOrgId() != null) {
	 * 
	 *           List<OrgType> orgTypeList =
	 *           targetOrganization.getOrgTypeList(); List<OrganizationRelation>
	 *           organizationRelationList = targetOrganization
	 *           .getOrganizationRelationList();
	 * 
	 *           boolean isIntNetwork = false;// 内部网点 boolean isExtNetwork =
	 *           false;// 外部网点 boolean isAgent = false;// 代理商 boolean
	 *           isIntBusinessEntity = false;// 内部经营实体 boolean isIntRela =
	 *           false;// 上级管理机构 boolean isExtRela = false;// 上级管理机构(外部)
	 * 
	 *           List<StaffOrganization> existList = null;
	 * 
	 *           // 查询该员工是否有其他员工组织关系 if
	 *           (!StrUtil.isNullOrEmpty(staff.getStaffId())) {
	 *           StaffOrganization vo = new StaffOrganization();
	 *           vo.setStaffId(staff.getStaffId()); existList =
	 *           this.queryStaffOrganizationList(vo); }
	 * 
	 *           // 判断该组织是内部网点或外部网点 if (orgTypeList != null &&
	 *           orgTypeList.size() > 0) { for (OrgType orgType : orgTypeList) {
	 * 
	 *           if (orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202010000) ||
	 *           orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202030000) ||
	 *           orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202040000)) { isIntNetwork =
	 *           true;// 该组织是内部网点 }
	 * 
	 *           if (orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202020000) ||
	 *           orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202050000) ||
	 *           orgType.getOrgTypeCd().equals(
	 *           OrganizationConstant.ORG_TYPE_N0202060000)) { isExtNetwork =
	 *           true;// 该组织是外部网点 }
	 * 
	 *           } }
	 * 
	 *           if (!StrUtil.isNullOrEmpty(staff.getWorkProp()) &&
	 *           staff.getWorkProp().equals( SffOrPtyCtants.WORKPROP_W_AGENT)) {
	 * 
	 *           // 代理商员工只能有一个员工组织关系,且归属组织必需是有经营主体的网点 if (existList != null &&
	 *           existList.size() > 0) { for (StaffOrganization staffOrg :
	 *           existList) { if (!targetOrganization.getId().equals(
	 *           staffOrg.getOrgId())) { if
	 *           (!StrUtil.isNullOrEmpty(sourceOrganization)) { if
	 *           (!sourceOrganization.getId().equals( staffOrg.getOrgId())) {
	 *           return "该代理商员工已经挂在一个外部网点上，不能再次添加员工组织关系."; } } else { return
	 *           "该代理商员工已经挂在一个外部网点上，不能再次添加员工组织关系."; } } } }
	 * 
	 *           if (isExtNetwork) { if (organizationRelationList != null &&
	 *           organizationRelationList.size() > 0) {
	 * 
	 *           for (OrganizationRelation organizationRelation :
	 *           organizationRelationList) {
	 * 
	 *           if (organizationRelation.getRelaCd().equals(
	 *           OrganizationConstant.RELA_CD_INNER)) { isIntRela = true;// 内部关系
	 *           }
	 * 
	 *           if (organizationRelation.getRelaCd().equals(
	 *           OrganizationConstant.RELA_CD_EXTER)) {
	 * 
	 *           isExtRela = true;// 外部关系
	 * 
	 *           Organization agentOrganization = new Organization();
	 *           agentOrganization.setOrgId(organizationRelation
	 *           .getRelaOrgId()); List<OrgType> agentOrgTypeList =
	 *           agentOrganization .getOrgTypeList();
	 * 
	 *           // 判断该组织上级是否是代理商组织 if (agentOrgTypeList != null &&
	 *           agentOrgTypeList.size() > 0) { for (OrgType agentOrgType :
	 *           agentOrgTypeList) {
	 * 
	 *           if (agentOrgType .getOrgTypeCd()
	 *           .equals(OrganizationConstant.ORG_TYPE_AGENT)) { isAgent =
	 *           true;// 该组织的上级组织是代理商组织 }
	 * 
	 *           } }
	 * 
	 *           }
	 * 
	 *           }
	 * 
	 *           if (!isIntRela) { return "该员工所属的网点没有上级管理机构关系，不能添加代理商员工."; }
	 * 
	 *           if (!isExtRela) { return "该员工所属的网点没有上级管理机构(外部)关系，不能添加代理商员工."; }
	 *           else if (!isAgent) { return "该员工所属网点的上级组织不是代理商，不能添加代理商员工."; }
	 * 
	 *           } else { return "该网点没有经营主体，不能添加代理商员工."; }
	 * 
	 *           } else { return "该组织不是外部网点，不能添加代理商员工."; }
	 * 
	 *           } else {
	 * 
	 *           if (isExtNetwork) { return "该组织是外部网点，不能添加非代理商员工."; }
	 * 
	 *           if (isIntNetwork) {
	 * 
	 *           if (organizationRelationList != null &&
	 *           organizationRelationList.size() > 0) {
	 * 
	 *           for (OrganizationRelation organizationRelation :
	 *           organizationRelationList) {
	 * 
	 *           if (organizationRelation.getRelaCd().equals(
	 *           OrganizationConstant.RELA_CD_INNER)) { isIntRela = true;// 内部关系
	 *           }
	 * 
	 *           if (organizationRelation.getRelaCd().equals(
	 *           OrganizationConstant.RELA_CD_EXTER)) {
	 * 
	 *           isExtRela = true;// 外部关系
	 * 
	 *           Organization intBusEntOrganization = new Organization();
	 *           intBusEntOrganization .setOrgId(organizationRelation
	 *           .getRelaOrgId()); List<OrgType> intBusEntOrgTypeList =
	 *           intBusEntOrganization .getOrgTypeList();
	 * 
	 *           // 判断该组织上级是否是内部经营实体组织 if (intBusEntOrgTypeList != null &&
	 *           intBusEntOrgTypeList.size() > 0) { for (OrgType
	 *           intBusEntOrgType : intBusEntOrgTypeList) {
	 * 
	 *           if (intBusEntOrgType .getOrgTypeCd()
	 *           .equals(OrganizationConstant.ORG_TYPE_N0903000000)) {
	 *           isIntBusinessEntity = true;// 该组织的上级组织是内部经营实体 }
	 * 
	 *           } }
	 * 
	 *           } }
	 * 
	 *           if (!isIntRela) { return "该员工所属的网点没有上级管理机构关系，不能添加该员工."; }
	 * 
	 *           if (!isExtRela) { return "该员工所属的网点没有上级管理机构(外部)关系，不能添加该员工."; }
	 *           else if (!isIntBusinessEntity) { return
	 *           "该员工所属网点的上级组织不是内部经营实体，不能添加该员工."; }
	 * 
	 *           }
	 * 
	 *           /** 用工性质为非“代理商人员”的，可以归属多个组织，但最多只能属于一个内部网点（即组织类型为网点，
	 *           但不属于任何经营实体的）
	 * 
	 *           if (existList != null && existList.size() > 0) {
	 * 
	 *           boolean isNewIntNetwork = false;// 存在内部网点的组织
	 * 
	 *           for (StaffOrganization staffOrg : existList) {
	 * 
	 *           if (!targetOrganization.getId().equals( staffOrg.getOrgId())) {
	 * 
	 *           Organization newOrganization = new Organization();
	 * 
	 *           if (!StrUtil.isNullOrEmpty(sourceOrganization)) { if
	 *           (!sourceOrganization.getId().equals( staffOrg.getOrgId())) {
	 *           newOrganization.setOrgId(staffOrg .getOrgId()); } } else {
	 *           newOrganization.setOrgId(staffOrg .getOrgId()); }
	 * 
	 *           List<OrgType> newOrgTypeList = newOrganization
	 *           .getOrgTypeList(); // 判断该组织是不是内部网点 if (newOrgTypeList != null
	 *           && newOrgTypeList.size() > 0) { for (OrgType orgType :
	 *           newOrgTypeList) { if (orgType .getOrgTypeCd()
	 *           .equals(OrganizationConstant.ORG_TYPE_N0202010000) || orgType
	 *           .getOrgTypeCd()
	 *           .equals(OrganizationConstant.ORG_TYPE_N0202030000) || orgType
	 *           .getOrgTypeCd()
	 *           .equals(OrganizationConstant.ORG_TYPE_N0202040000)) {
	 *           isNewIntNetwork = true;// 存在内部网点的组织 } } }
	 * 
	 *           } }
	 * 
	 *           if (isNewIntNetwork) { return "该员工已经挂在一个内部网点上，不能再挂内部网点."; }
	 * 
	 *           }
	 * 
	 *           } }
	 * 
	 *           }
	 * 
	 *           return null;
	 * 
	 *           }
	 */
}
