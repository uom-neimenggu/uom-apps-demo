package cn.ffcs.uom.ftpsyncfile.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.dao.SyncTableColumnConfigDao;
import cn.ffcs.uom.ftpsyncfile.manager.SyncTableColumnConfigManager;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;

@Service("syncTableColumnConfigManager")
@Scope("prototype")
public class SyncTableColumnConfigManagerImpl implements
		SyncTableColumnConfigManager {

	@Resource
	private SyncTableColumnConfigDao syncTableColumnConfigDao;

	@Override
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			SyncTableColumnConfig querySyncTableColumnConfig) {
		StringBuffer hql = new StringBuffer(
				"From SyncTableColumnConfig where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (querySyncTableColumnConfig != null) {
			if (querySyncTableColumnConfig.getOrgTreeId() != null) {
				hql.append(" and orgTreeId = ?");
				params.add(querySyncTableColumnConfig.getOrgTreeId());
			}
			if (!StrUtil.isEmpty(querySyncTableColumnConfig.getTableName())) {
				hql.append(" and tableName = ?");
				params.add(querySyncTableColumnConfig.getTableName());
			}
		}
		hql.append("order by column_seq");
		return syncTableColumnConfigDao.findListByHQLAndParams(hql.toString(),
				params);
	}

	@Override
	public List<SyncTableColumnConfig> querySyncTableColumnConfigList(
			Long orgTreeId, String tableName) {
		SyncTableColumnConfig syncTableColumnConfig = new SyncTableColumnConfig();
		syncTableColumnConfig.setOrgTreeId(orgTreeId);
		syncTableColumnConfig.setTableName(tableName);
		return querySyncTableColumnConfigList(syncTableColumnConfig);
	}

	@Override
	public List<String> queryTableColumnNameList(Long orgTreeId,
			String tableName) {
		List<SyncTableColumnConfig> list = this.querySyncTableColumnConfigList(
				orgTreeId, tableName);
		List columnNameList = new ArrayList();
		if (list != null && list.size() > 0) {
			for (SyncTableColumnConfig syncTableColumnConfig : list) {
				columnNameList.add(syncTableColumnConfig.getColumnName());
			}
		}
		return columnNameList;
	}
}
