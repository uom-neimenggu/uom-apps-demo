package cn.ffcs.uom.ftpsyncfile.manager;

import java.util.List;

import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;

public interface SyncTableColumnConfigManager {
	/**
	 * 获取列表
	 * 
	 * @param querySyncTableColumnConfig
	 * @return
	 */
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			SyncTableColumnConfig querySyncTableColumnConfig);

	/**
	 * 获取列表
	 * 
	 * @param querySyncTableColumnConfig
	 * @return
	 */
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			Long orgTreeId, String tableName);

	/**
	 * 获取列名列表
	 * 
	 * @param querySyncTableColumnConfig
	 * @return
	 */
	public List<String> queryTableColumnNameList(Long orgTreeId,
			String tableName);
}
