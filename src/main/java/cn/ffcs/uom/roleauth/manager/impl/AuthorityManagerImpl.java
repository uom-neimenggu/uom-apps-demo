package cn.ffcs.uom.roleauth.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.roleauth.dao.AuthorityDao;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.model.SysList;

@Service("authorityManager")
@Scope("prototype")
public class AuthorityManagerImpl implements AuthorityManager {

	@Autowired
	private AuthorityDao authorityDao;
	
	@Override
	public void saveRoleAuthorityRela(List<StaffAuthority> authoritys, List<StaffRole> staffRoles) {
		if(null == authoritys || null == staffRoles){
			return;
		}
		//String batchNumber = OperateLog.gennerateBatchNumber();
		for (StaffAuthority sr : authoritys) {
			for (StaffRole staffRole : staffRoles) {
				RoleAuthorityRela roleAuthorityRela = new RoleAuthorityRela();
				//staffSysRela.setBatchNumber(batchNumber);
				roleAuthorityRela.setRoleId(staffRole.getRoleId());
				roleAuthorityRela.setAuthorityId(sr.getAuthorityId());
				if(null == this.queryRoleAuthorityRela(roleAuthorityRela)){
					//roleAuthorityRela.add();
					roleAuthorityRela.addOnly();
				}
				
			}
		}
	}

	@Override
	public void saveAuthority(StaffAuthority authority) {
		authority.addOnly();
	}

	@Override
	public void updateAuthority(StaffAuthority authority) {
		authority.updateOnly();
	}

	@Override
	public void removeAuthority(StaffAuthority authority) {
		authority.removeOnly();
	}

	@Override
	public PageInfo queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela, int currentPage, int pageSize) {
		return authorityDao.queryRoleAuthorityRela(roleAuthorityRela, currentPage, pageSize);
	}

	@Override
	public void removeRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
		roleAuthorityRela.removeOnly();		
	}

	@Override
	public void saveRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
		roleAuthorityRela.addOnly();
	}

	@Override
	public RoleAuthorityRela queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
		return authorityDao.queryRoleAuthorityRela(roleAuthorityRela);
	}

	@Override
	public SysAuthorityRela querySysAuthorityRela(SysAuthorityRela sysAuthorityRela) {
		return authorityDao.querySysAuthorityRela(sysAuthorityRela);
	}

	@Override
	public void removeSysAuthorityRela(SysAuthorityRela sysAuthorityRela){
		sysAuthorityRela.removeOnly();
	}

	@Override
	public void saveSysAuthorityRela(List<StaffAuthority> authoritys, List<SysList> sysLists){
		if(null == authoritys || null == sysLists){
			return;
		}
		//String batchNumber = OperateLog.gennerateBatchNumber();
		for (StaffAuthority sr : authoritys) {
			for (SysList sysList : sysLists) {
				SysAuthorityRela sysAuthorityRela = new SysAuthorityRela();
				//sysAuthorityRela.setBatchNumber(batchNumber);
				sysAuthorityRela.setSysListId(sysList.getSysListId());
				sysAuthorityRela.setAuthorityId(sr.getAuthorityId());
				if(null == this.querySysAuthorityRela(sysAuthorityRela)){
					//sysAuthorityRela.add();
					sysAuthorityRela.addOnly();
				}
				
			}
		}
	}

	@Override
	public PageInfo querySysAuthorityRela(SysAuthorityRela sysAuthorityRela, int currentPage, int pageSize) {
		return authorityDao.querySysAuthorityRela(sysAuthorityRela, currentPage, pageSize);
	}
	
}
