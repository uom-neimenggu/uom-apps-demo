package cn.ffcs.uom.ftpsyncfile.manager;

import java.util.List;

import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnAliasConfig;

public interface SyncTableColumnAliasConfigManager {
	/**
	 * 获取列表
	 * 
	 * @param querySyncTableColumnAliasConfig
	 * @return
	 */
	public List<SyncTableColumnAliasConfig> querySyncTableColumnAliasConfigList(
			SyncTableColumnAliasConfig querySyncTableColumnAliasConfig);
}
