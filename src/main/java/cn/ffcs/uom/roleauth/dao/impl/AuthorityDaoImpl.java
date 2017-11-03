package cn.ffcs.uom.roleauth.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.roleauth.dao.AuthorityDao;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.model.SysList;

@Repository("authorityDao")
@Transactional
public class AuthorityDaoImpl extends BaseDaoImpl implements AuthorityDao {
	
	public List<StaffAuthority> queryAuthority(StaffAuthority authority){
		StringBuilder sb = new  StringBuilder("select * from RBAC_STAFF_AUTHORITY a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != authority){
			if(null != authority.getAuthorityParentId()){
				sb.append(" and a.AUTHORITY_PARENT_ID = ?");
				params.add(authority.getAuthorityParentId());
			}
		}
		sb.append(" order by a.AUTHORITY_ID asc");
		return super.jdbcFindList(sb.toString(), params, StaffAuthority.class);
	}

	public RoleAuthorityRela queryRoleAuthorityRela(RoleAuthorityRela rar) {
		StringBuilder sb = new  StringBuilder("select * from RBAC_ROLE_AUTHORITY_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != rar){
			if(null != rar.getRoleId()){
				sb.append(" and a.ROLE_ID = ?");
				params.add(rar.getRoleId());
			}
			if(null != rar.getAuthorityId()){
				sb.append(" and a.AUTHORITY_ID = ?");
				params.add(rar.getAuthorityId());
			}
		}
		List<RoleAuthorityRela> rars = super.jdbcFindList(sb.toString(), params, RoleAuthorityRela.class);
		if(null != rars && rars.size() > 0){
			return rars.get(0);
		}
		return null;
	}

	public SysAuthorityRela querySysAuthorityRela(SysAuthorityRela sar) {
		StringBuilder sb = new  StringBuilder("select * from RBAC_SYSTEM_AUTHORITY_RELA a where a.status_cd = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sar){
			if(null != sar.getSysListId()){
				sb.append(" and a.SYS_LIST_ID = ?");
				params.add(sar.getSysListId());
			}
			if(null != sar.getAuthorityId()){
				sb.append(" and a.AUTHORITY_ID = ?");
				params.add(sar.getAuthorityId());
			}
		}
		List<SysAuthorityRela> rars = super.jdbcFindList(sb.toString(), params, SysAuthorityRela.class);
		if(null != rars && rars.size() > 0){
			return rars.get(0);
		}
		return null;
	}

	public PageInfo queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela, int currentPage, int pageSize) {
		StringBuilder sb = new  StringBuilder("select b.* from RBAC_STAFF_ROLE a,RBAC_ROLE_AUTHORITY_RELA b,RBAC_STAFF_AUTHORITY c where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != roleAuthorityRela){
			if(null != roleAuthorityRela.getRoleId()){
				sb.append(" and b.ROLE_ID = ?");
				params.add(roleAuthorityRela.getRoleId());
			}
			if(null != roleAuthorityRela.getAuthorityId()){
				sb.append(" and b.AUTHORITY_ID = ?");
				params.add(roleAuthorityRela.getAuthorityId());
			}
			StaffRole staffRole = roleAuthorityRela.getQryStaffRole();
			if(null != staffRole && !StrUtil.isNullOrEmpty(staffRole.getRoleName())){
				sb.append(" and a.ROLE_NAME LIKE ?");
				params.add("%"+ staffRole.getRoleName() +"%");
			}
			if(null != staffRole && !StrUtil.isNullOrEmpty(staffRole.getRoleCode())){
				sb.append(" and a.ROLE_CODE = ?");
				params.add(staffRole.getRoleCode());
			}
		}
		sb.append(" and a.ROLE_ID = b.ROLE_ID and b.AUTHORITY_ID = c.AUTHORITY_ID");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, RoleAuthorityRela.class);
	}

	public PageInfo querySysAuthorityRela(SysAuthorityRela sysAuthorityRela, int currentPage, int pageSize) {
		StringBuilder sb = new  StringBuilder("select b.* from RBAC_SYSTEM_LIST a,RBAC_SYSTEM_AUTHORITY_RELA b,RBAC_STAFF_AUTHORITY c where a.status_cd = ? ");
		sb.append(" and b.status_cd = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(null != sysAuthorityRela){
			if(null != sysAuthorityRela.getSysListId()){
				sb.append(" and b.SYS_LIST_ID = ?");
				params.add(sysAuthorityRela.getSysListId());
			}
			if(null != sysAuthorityRela.getAuthorityId()){
				sb.append(" and b.AUTHORITY_ID = ?");
				params.add(sysAuthorityRela.getAuthorityId());
			}
			SysList sysList = sysAuthorityRela.getQrySysList();
			if(null != sysList && !StrUtil.isNullOrEmpty(sysList.getSysName())){
				sb.append(" and a.SYS_NAME LIKE ?");
				params.add("%"+ sysList.getSysName() +"%");
			}
			if(null != sysList && !StrUtil.isNullOrEmpty(sysList.getClientCode())){
				sb.append(" and a.CLIENT_CODE = ?");
				params.add(sysList.getClientCode());
			}
		}
		sb.append(" and a.SYS_LIST_ID = b.SYS_LIST_ID and b.AUTHORITY_ID = c.AUTHORITY_ID");
		return super.jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, SysAuthorityRela.class);
	}
	
}
