package cn.ffcs.uom.roleauth.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.roleauth.dao.AuthorityDao;
import cn.ffcs.uom.syslist.dao.SysListDao;
import cn.ffcs.uom.syslist.model.SysList;

public class SysAuthorityRela extends UomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long sysAuthorityRelaId;
	public Long getSysAuthorityRelaId() {
		return sysAuthorityRelaId;
	}
	public void setSysAuthorityRelaId(Long sysAuthorityRelaId) {
		super.setId(sysAuthorityRelaId);
		this.sysAuthorityRelaId = sysAuthorityRelaId;
	}
	@Getter
	@Setter
	private Long authorityId;
	@Getter
	@Setter
	private Long sysListId;
	@Setter
	private StaffAuthority authority;
	public StaffAuthority getAuthority() {
		if(!StrUtil.isNullOrEmpty(this.authorityId)){
			authority = (StaffAuthority)repository().getObject(StaffAuthority.class, this.authorityId);
			return authority;
		}
		return null;
	}
	public static AuthorityDao repository() {
		return (AuthorityDao) ApplicationContextUtil.getBean("authorityDao");
	}
	@Setter
	private SysList sysList;
	@Getter
	@Setter
	private SysList qrySysList;
	public SysList getSysList() {
		if(!StrUtil.isNullOrEmpty(this.sysListId)){
			sysList = (SysList)repositorySysList().getObject(SysList.class, this.sysListId);
			return sysList;			
		}
		return null;
	}
	public static SysListDao repositorySysList() {
		return (SysListDao) ApplicationContextUtil.getBean("sysListDao");
			
	}
	
}
