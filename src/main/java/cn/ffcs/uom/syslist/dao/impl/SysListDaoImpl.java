package cn.ffcs.uom.syslist.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.dao.SysListDao;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

@Repository("sysListDao")
@Transactional
public class SysListDaoImpl extends BaseDaoImpl implements SysListDao {

	@Override
	public List<SysList> querySysLists(SysList sysList) {
		StringBuilder sb = new  StringBuilder("select * from RBAC_SYSTEM_LIST a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sysList){
			if(null != sysList.getRelaDomainId()){
				sb.append(" and RELA_DOMAIN_ID = ?");
				params.add(sysList.getRelaDomainId());
			}
		}
		return super.jdbcFindList(sb.toString(), params, SysList.class);
	}

	@Override
	public StaffSysRela queryStaffSysRela(StaffSysRela ssr) {
		StringBuilder sb = new  StringBuilder("select * from RBAC_STAFF_SYS_LIST_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != ssr){
			if(null != ssr.getStaffId()){
				sb.append(" and STAFF_ID = ?");
				params.add(ssr.getStaffId());
			}
			if(null != ssr.getSysListId()){
				sb.append(" and SYS_LIST_ID = ?");
				params.add(ssr.getSysListId());
			}
		}
		List<StaffSysRela> ssrs = super.jdbcFindList(sb.toString(), params, StaffSysRela.class);
		if(null != ssrs && ssrs.size() > 0){
			return ssrs.get(0);
		}
		return null;
	}

	@Override
	public PageInfo queryStaffSysRela(StaffSysRela staffSysRela, int currentPage, int pageSize) {
		StringBuilder sb = new  StringBuilder("select b.* from STAFF a,RBAC_STAFF_SYS_LIST_RELA b,RBAC_SYSTEM_LIST c where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != staffSysRela){
			if(null != staffSysRela.getSysListId()){
				sb.append(" and b.SYS_LIST_ID = ?");
				params.add(staffSysRela.getSysListId());
			}
			if(null != staffSysRela.getStaffId()){
				sb.append(" and b.STAFF_ID = ?");
				params.add(staffSysRela.getStaffId());
			}
			Staff staff = staffSysRela.getQryStaff();
			if(null != staff && !StrUtil.isNullOrEmpty(staff.getStaffName())){
				sb.append(" and a.STAFF_NAME LIKE ?");
				params.add("%"+ staff.getStaffName() +"%");
			}
			if(null != staff && !StrUtil.isNullOrEmpty(staff.getStaffCode())){
				sb.append(" and a.STAFF_CODE = ?");
				params.add(staff.getStaffCode());
			}
		}
		sb.append(" and a.staff_id = b.staff_id and b.sys_list_id = c.sys_list_id");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, StaffSysRela.class);
	}

	@Override
	public PageInfo querySysRoleRela(SysRoleRela sysRoleRela, int currentPage, int pageSize) {
		StringBuilder sb = new  StringBuilder("select b.* from RBAC_STAFF_ROLE a,RBAC_SYS_ROLE_RELA b,RBAC_SYSTEM_LIST c where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sysRoleRela){
			if(null != sysRoleRela.getSysListId()){
				sb.append(" and b.SYS_LIST_ID = ?");
				params.add(sysRoleRela.getSysListId());
			}
			if(null != sysRoleRela.getRoleId()){
				sb.append(" and b.ROLE_ID = ?");
				params.add(sysRoleRela.getRoleId());
			}
			StaffRole staffRole = sysRoleRela.getQryStaffRole();
			if(null != staffRole && !StrUtil.isNullOrEmpty(staffRole.getRoleName())){
				sb.append(" and a.ROLE_NAME LIKE ?");
				params.add("%"+ staffRole.getRoleName() +"%");
			}
			if(null != staffRole && !StrUtil.isNullOrEmpty(staffRole.getRoleCode())){
				sb.append(" and a.ROLE_CODE = ?");
				params.add(staffRole.getRoleCode());
			}
		}
		sb.append(" and a.role_id = b.role_id and b.sys_list_id = c.sys_list_id");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, SysRoleRela.class);
	}

	public SysRoleRela querySysRoleRela(SysRoleRela srr){
		StringBuilder sb = new  StringBuilder("select * from RBAC_SYS_ROLE_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != srr){
			if(null != srr.getRoleId()){
				sb.append(" and ROLE_ID = ?");
				params.add(srr.getRoleId());
			}
			if(null != srr.getSysListId()){
				sb.append(" and SYS_LIST_ID = ?");
				params.add(srr.getSysListId());
			}
		}
		List<SysRoleRela> srrs = super.jdbcFindList(sb.toString(), params, SysRoleRela.class);
		if(null != srrs && srrs.size() > 0){
			return srrs.get(0);
		}
		return null;
	}
	
}
