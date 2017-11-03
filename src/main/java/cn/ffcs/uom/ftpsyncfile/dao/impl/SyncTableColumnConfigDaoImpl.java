package cn.ffcs.uom.ftpsyncfile.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.ftpsyncfile.dao.SyncTableColumnConfigDao;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;

@Repository("syncTableColumnConfigDao")
public class SyncTableColumnConfigDaoImpl extends BaseDaoImpl implements
		SyncTableColumnConfigDao {

	@Override
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			SyncTableColumnConfig querySyncTableColumnConfig) {
		// TODO Auto-generated method stub
		return null;
	}
}
