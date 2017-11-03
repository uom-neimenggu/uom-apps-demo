package cn.ffcs.uom.ftpsyncfile.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;

public interface SyncTableColumnConfigDao extends BaseDao {
	/**
	 * 查询列表
	 * 
	 * @param querySyncTableColumnConfig
	 * @return
	 */
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			SyncTableColumnConfig querySyncTableColumnConfig);
}
