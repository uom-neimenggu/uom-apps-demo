package cn.ffcs.uom.publishLog.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.publishLog.manager.PublishLogManager;
import cn.ffcs.uom.publishLog.model.PublishLog;

@Service("publishLogManager")
@Scope("prototype")
@SuppressWarnings("static-access")
public class PublishLogManagerImpl implements PublishLogManager {

	@Override
	public PageInfo queryPageInfoByOrgTree(PublishLog queryPublishLog,
			int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM PUBLISH_LOG A WHERE A.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryPublishLog != null) {
			if (queryPublishLog.getOrgTreeId() != null) {
				sql.append(" AND A.ORG_TREE_ID = ?");
			}
			params.add(queryPublishLog.getOrgTreeId());
		}
		sql.append(" ORDER BY A.PUBLISH_DATE DESC");
		return queryPublishLog.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, PublishLog.class);
	}

	@Override
	public void addPublishLog(PublishLog publishLog) {
		publishLog.addOnly();
	}
}
