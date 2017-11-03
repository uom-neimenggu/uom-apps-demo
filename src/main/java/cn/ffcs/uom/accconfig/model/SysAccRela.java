package cn.ffcs.uom.accconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.accconfig.dao.AccConfigDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.syslist.dao.SysListDao;
import cn.ffcs.uom.syslist.model.SysList;

public class SysAccRela extends UomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long accSystemListRelaId;

	public Long getAccSystemListRelaId() {
		return accSystemListRelaId;
	}
	public void setAccSystemListRelaId(Long accSystemListRelaId) {
		super.setId(accSystemListRelaId);
		this.accSystemListRelaId = accSystemListRelaId;
	}
	@Getter
	@Setter
	private Long accConfigId;

	@Getter
	@Setter
	private Long sysListId;
	
	@Setter
	private AccConfig accConfig;

	@Getter
	@Setter
	private AccConfig qryAccConfig;
	@Getter
	@Setter
	private SysList qrySysList;
	
	@Setter
	private SysList sysList;

	public AccConfig getAccConfig() {
		if(!StrUtil.isNullOrEmpty(this.accConfigId)){
			accConfig = (AccConfig)repository().getObject(AccConfig.class, this.accConfigId);
			return accConfig;			
		}
		return null;
	}
	public SysList getSysList() {
		if(!StrUtil.isNullOrEmpty(this.sysListId)){
			sysList = (SysList)repositorySysList().getObject(SysList.class, this.sysListId);
			return sysList;			
		}
		return null;
	}
	public static AccConfigDao repository() {
		return (AccConfigDao) ApplicationContextUtil.getBean("accConfigDao");
	}
	public static SysListDao repositorySysList() {
		return (SysListDao) ApplicationContextUtil.getBean("sysListDao");
	}

}
