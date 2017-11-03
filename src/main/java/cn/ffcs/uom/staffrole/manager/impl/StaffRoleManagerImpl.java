package cn.ffcs.uom.staffrole.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffExtendAttrManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.dao.StaffRoleDao;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

@Service("staffRoleManager")
@Scope("prototype")
public class StaffRoleManagerImpl implements StaffRoleManager {

	@Autowired
	private StaffRoleDao staffRoleDao;

	@Resource
	private StaffExtendAttrManager staffExtendAttrManager;

	@Resource
	private CascadeRelationManager cascadeRelationManager;
	
	@Resource
	private OrganizationRelationManager organizationRelationManager;

	public List<StaffRole> queryStaffRoles(StaffRole staffRole) {
		return staffRoleDao.queryStaffRoles(staffRole);
	}

	public void saveStaffRoleRela(List<StaffRole> staffRoles, List<Staff> staffs) {
		if (null == staffRoles || null == staffs) {
			return;
		}
		// String batchNumber = OperateLog.gennerateBatchNumber();
		for (Staff staff : staffs) {
			StaffRoleRela srrParam = new StaffRoleRela();
			srrParam.setStaffId(staff.getStaffId());
			boolean isContainVpn = this.isContainsRoleParent2(staffRoles, new Long(10017L));
			boolean isContainVpnSms = this.isContainsRole2(staffRoles, new Long(10006L));
			if (isContainVpn && !isContainVpnSms) {
				StaffRole sr = new StaffRole();
				sr.setRoleId(new Long(10006L));
				staffRoles.add(sr);
			} 
			
			for (StaffRole sr : staffRoles) {
				StaffRoleRela staffRoleRela = new StaffRoleRela();
				// staffRoleRela.setBatchNumber(batchNumber);
				staffRoleRela.setRoleId(sr.getRoleId());
				staffRoleRela.setStaffId(staff.getStaffId());
				if (null == this.queryStaffRoleRela(staffRoleRela)) {
					// staffRoleRela.add();
					staffRoleRela.addOnly();
				}
			}
		}
	}

	public void saveStaffRole(StaffRole staffRole) {
		// String batchNumber = OperateLog.gennerateBatchNumber();
		// staffRole.setBatchNumber(batchNumber);
		// staffRole.add();
		staffRole.addOnly();
	}

	public void updateStaffRole(StaffRole staffRole) {
		// String batchNumber = OperateLog.gennerateBatchNumber();
		// staffRole.setBatchNumber(batchNumber);
		// staffRole.update();
		staffRole.updateOnly();
	}

	public void removeStaffRole(StaffRole staffRole) {
		// String batchNumber = OperateLog.gennerateBatchNumber();
		// staffRole.setBatchNumber(batchNumber);
		// staffRole.remove();
		staffRole.removeOnly();
	}

	public PageInfo queryStaffRoleRela(StaffRoleRela staffRoleRela,
			int currentPage, int pageSize) {
		return staffRoleDao.queryStaffRoleRela(staffRoleRela, currentPage,
				pageSize);
	}

	public void removeStaffRoleRela(StaffRoleRela staffRoleRela) {
		// staffRoleRela.remove();
		staffRoleRela.removeOnly();
	}

	public void saveStaffRoleRela(StaffRoleRela staffRoleRela) {
		// staffRoleRela.add();
		staffRoleRela.addOnly();
	}

	public StaffRoleRela queryStaffRoleRela(StaffRoleRela staffRoleRela) {
		return staffRoleDao.queryStaffRoleRela(staffRoleRela);
	}

	public void updateBatchStaffRoleRela(StaffOrganization staffOrganization) {

		boolean vpnRoleSign = true;
		Organization organization = null;
		StaffRoleRela staffRoleRela = null;
		CascadeRelation cascadeRelation = null;
		List<StaffRole> addStaffRoles = staffOrganization.getAddStaffRoles();

		if (BaseUnitConstants.RALA_CD_1.equals(staffOrganization.getRalaCd())
				&& BaseUnitConstants.ENTT_STATE_ACTIVE.equals(staffOrganization
						.getStatusCd())) {

			staffRoleRela = new StaffRoleRela();
			staffRoleRela.setStaffId(staffOrganization.getStaffId());
			staffRoleRela.setRoleId(StaffRoleConstants.VPN_LOGIN_10006);
			if (addStaffRoles != null && addStaffRoles.size() > 0) {
				StaffRole staffRole1 = new StaffRole();
				staffRole1.setRoleId(StaffRoleConstants.VPN_LOGIN_10006);
				if (!addStaffRoles.contains(staffRole1)
						&& StrUtil.isNullOrEmpty(staffRoleDao
								.queryStaffRoleRela(staffRoleRela))) {
					staffRoleRela.addOnly();
				}
			} else {
				if (StrUtil.isNullOrEmpty(staffRoleDao
						.queryStaffRoleRela(staffRoleRela))) {
					staffRoleRela.addOnly();
				}
			}
			organization = new Organization();
			organization.setOrgId(staffOrganization.getOrgId());
			boolean IsNetworkPoint = organizationRelationManager.checkOrgIsNetworkPoint(staffOrganization.getOrgId());
			OrganizationRelation organizationRelation = organization
					.getOrganizationRelation(OrganizationConstant.RELA_CD_INNER);

			if (organizationRelation != null
					&& organizationRelation.getOrgId() != null) {
				cascadeRelation = new CascadeRelation();
				cascadeRelation.setCascadeValue(organizationRelation.getOrgId()
						.toString());
				if (IsNetworkPoint) {
					cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_33);
				} else {
					cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_3);
				}
				
				cascadeRelation = cascadeRelationManager
						.queryCascadeRelation(cascadeRelation);
				if (cascadeRelation != null
						&& !StrUtil.isEmpty(cascadeRelation
								.getRelaCascadeValue())) {

					staffRoleRela = new StaffRoleRela();
					staffRoleRela.setStaffId(staffOrganization.getStaffId());
					staffRoleRela
							.setRoleParentId(StaffRoleConstants.VPN_ROLE_10017);

					List<StaffRoleRela> staffRoleRelaList = staffRoleDao
							.queryStaffRoleList(staffRoleRela);

					if (staffRoleRelaList != null
							&& staffRoleRelaList.size() > 0) {

						for (StaffRoleRela staffRoleRelaDb : staffRoleRelaList) {
							if (cascadeRelation.getRelaCascadeValue().equals(
									staffRoleRelaDb.getRoleId().toString())) {
								vpnRoleSign = false;
							} else if (!StaffRoleConstants.VPN_ROLE_11177
									.equals(staffRoleRelaDb.getRoleId())) {
								staffRoleRelaDb.removeOnly();
							}
						}

					}
					if (addStaffRoles != null && addStaffRoles.size() > 0) {

						if (vpnRoleSign) {
							StaffRole staffRole2 = new StaffRole();
							staffRoleRela = new StaffRoleRela();
							staffRoleRela.setStaffId(staffOrganization
									.getStaffId());
							staffRoleRela.setRoleId(Long
									.parseLong(cascadeRelation
											.getRelaCascadeValue()));
							staffRole2.setRoleId(Long.parseLong(cascadeRelation
									.getRelaCascadeValue()));
							if (!addStaffRoles.contains(staffRole2)) {
								staffRoleRela.addOnly();
							}

						}
					} else {
						if (vpnRoleSign) {
							staffRoleRela = new StaffRoleRela();
							staffRoleRela.setStaffId(staffOrganization
									.getStaffId());
							staffRoleRela.setRoleId(Long
									.parseLong(cascadeRelation
											.getRelaCascadeValue()));
							staffRoleRela.addOnly();
						}
					}
				}
			}
		}
	}

	@Override
	public void saveStaffRoleRelaList(List<StaffRoleRela> staffRoleRelaList) {

		List<StaffExtendAttr> staffExtendAttrList = new ArrayList<StaffExtendAttr>();

		for (StaffRoleRela staffRoleRela : staffRoleRelaList) {

			if (!StrUtil.isEmpty(staffRoleRela.getStaffAttrValue())) {

				StaffExtendAttr staffExtendAttr = new StaffExtendAttr();
				staffExtendAttr.setStaffId(staffRoleRela.getStaffId());
				staffExtendAttr
						.setStaffAttrSpecId(SffOrPtyCtants.STAFF_ATTR_SPEC_ID_2);

				StaffExtendAttr sea = staffExtendAttrManager
						.queryStaffExtendAttr(staffExtendAttr);

				if (sea != null && sea.getStaffExtendAttrId() != null) {
					if (StrUtil.isEmpty(sea.getStaffAttrValue())) {
						sea.setStaffAttrValue(staffRoleRela.getStaffAttrValue());

						if (staffExtendAttrList.size() == 0) {
							sea.update();
						}

					}
				} else {

					staffExtendAttr.setStaffAttrValue(staffRoleRela
							.getStaffAttrValue());

					if (staffExtendAttrList.size() == 0) {
						staffExtendAttr.add();
					}

				}
				staffExtendAttrList.add(staffExtendAttr);
			}

			staffRoleRela.add();

		}
	}

	@Override
	public List<StaffRole> queryStaffaRoles(Staff staff) {
		return staffRoleDao.queryStaffRoles(staff);
	}

	@Override
	public void removeStaffRoleRela(List<StaffRole> staffRoles, Staff staff) {
		for (StaffRole staffRole : staffRoles) {
			StaffRoleRela staffRoleRela = new StaffRoleRela();
			staffRoleRela.setRoleId(staffRole.getRoleId());
			staffRoleRela.setStaffId(staff.getStaffId());
			staffRoleRela = this.queryStaffRoleRela(staffRoleRela);
			staffRoleRela.removeOnly();
		}
	}

	@Override
	public void removeStaffRoleRelaList(List<StaffRoleRela> delStaffRoleRelaList) {
		for(StaffRoleRela staffRoleRela : delStaffRoleRelaList) {
			staffRoleRela.removeOnly();
		}
	}
	
	public boolean isContainsRole1(List<StaffRoleRela> list,Long roleId){
		
		for (StaffRoleRela staffRoleRela : list) {
			if (staffRoleRela.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}
	public boolean isContainsRole2(List<StaffRole> rolelist,Long roleId){
		
		for (StaffRole staffRole : rolelist) {
			if (staffRole.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}	
	public boolean isContainsRoleParent1(List<StaffRoleRela> list,Long roleId){
		
		for (StaffRoleRela staffRoleRela : list) {
			if (staffRoleRela.getRoleParentId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isContainsRoleParent2(List<StaffRole> list,Long roleId){
		
		for (StaffRole staffRole : list) {
			if (staffRole.getRoleParentId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}
}
