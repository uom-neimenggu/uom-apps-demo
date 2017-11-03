package cn.ffcs.uom.bpm.manager;

import java.util.List;

import cn.ffcs.uom.bpm.model.QaUnPrincipal;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;

public interface BmpSystemConfigManager {
	
	public List<QaUnPrincipal> qryPrincipal(BusinessSystem businessSystem);
	public List<QaUnPrincipal> qryPrincipal(QaUnPrincipal principal);
	
	public void delPrincipal(BusinessSystem businessSystem,QaUnPrincipal principal);
	public void delPrincipal(QaUnPrincipal principal);
	
	public void savePrincipal(QaUnPrincipal qaUnPrincipal);
}
