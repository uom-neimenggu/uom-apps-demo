package cn.ffcs.uom.accconfig.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.accconfig.dao.AccConfigDao;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.syslist.model.SysList;

@Service("accConfigManager")
@Scope("prototype")
public class AccConfigManagerImpl implements AccConfigManager {
	
	@Autowired
	private AccConfigDao accConfigDao;
	
	public void removeAccConfig(AccConfig accConfig) {
		//String batchNumber = OperateLog.gennerateBatchNumber();
		//accConfig.setBatchNumber(batchNumber);
		//accConfig.remove();
		accConfig.removeOnly();
	}

	@Override
	public void removeSysAccRela(SysAccRela sysAccRela) {
		sysAccRela.removeOnly();
	}

	@Override
	public PageInfo querySysAccRela(SysAccRela sysAccRela, int currentPage, int pageSize) {
		return accConfigDao.querySysAccRela(sysAccRela, currentPage, pageSize);
	}
	
	@Override
	public SysAccRela querySysAccRela(SysAccRela sysAccRela) {
		return accConfigDao.querySysAccRela(sysAccRela);
	}

	@Override
	public void saveSysAccRelas(List<AccConfig> accConfigs, List<SysList> sysLists) {
		if(null == accConfigs || null == sysLists){
			return;
		}
		//String batchNumber = OperateLog.gennerateBatchNumber();
		for (AccConfig ac : accConfigs) {
			for (SysList sysList : sysLists) {
				SysAccRela sysAccRela = new SysAccRela();
				//sysAccRela.setBatchNumber(batchNumber);
				sysAccRela.setAccConfigId(ac.getAccConfigId());
				sysAccRela.setSysListId(sysList.getSysListId());
				if(null == this.querySysAccRela(sysAccRela)){
					//sysAccRela.add();
					sysAccRela.addOnly();
				}
				
			}
		}
	}

	@Override
	public void updateAccConfig(AccConfig accConfig) {
		accConfig.updateOnly();
	}

	@Override
	public void saveAccConfig(AccConfig accConfig) {
		accConfig.addOnly();
	}
	
}
