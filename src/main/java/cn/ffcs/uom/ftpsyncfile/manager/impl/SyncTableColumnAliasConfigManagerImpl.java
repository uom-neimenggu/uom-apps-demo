package cn.ffcs.uom.ftpsyncfile.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.ftpsyncfile.dao.SyncTableColumnAliasConfigDao;
import cn.ffcs.uom.ftpsyncfile.manager.SyncTableColumnAliasConfigManager;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnAliasConfig;

@Service("syncTableColumnAliasConfigManager")
@Scope("prototype")
public class SyncTableColumnAliasConfigManagerImpl implements
		SyncTableColumnAliasConfigManager {

	@Resource
	private SyncTableColumnAliasConfigDao syncTableColumnAliasConfigDao;

	@Override
	public List<SyncTableColumnAliasConfig> querySyncTableColumnAliasConfigList(
			SyncTableColumnAliasConfig querySyncTableColumnAliasConfig) {
		StringBuffer hql = new StringBuffer(
				"From SyncTableColumnAliasConfig where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (querySyncTableColumnAliasConfig != null) {
			if (querySyncTableColumnAliasConfig.getTreeId() != null) {
				hql.append(" and treeId = ?");
				params.add(querySyncTableColumnAliasConfig.getTreeId());
			}
		}
		return syncTableColumnAliasConfigDao.findListByHQLAndParams(hql
				.toString(), params);
	}

}
