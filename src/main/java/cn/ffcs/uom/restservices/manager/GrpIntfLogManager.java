package cn.ffcs.uom.restservices.manager;

import java.util.List;

import cn.ffcs.uom.restservices.model.GrpIntfLog;

public interface GrpIntfLogManager {

	public List<GrpIntfLog> queryGrpIntfLogList(GrpIntfLog queryGrpIntfLog);

	public void addGrpIntfLog(GrpIntfLog grpIntfLog);

	public void removeGrpIntfLog(GrpIntfLog systemOrgTreeConfig);

	public void updateGrpIntfLog(GrpIntfLog grpIntfLog);

}
