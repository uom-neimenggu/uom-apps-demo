package cn.ffcs.uom.ftpsyncfile.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;

@Service("ftpTaskInstanceManager")
@Scope("prototype")
public class FtpTaskInstanceManagerImpl implements FtpTaskInstanceManager {

	@Override
	public void savaFtpTaskInstance(FtpTaskInstance ftpTaskInstance) {
		if (ftpTaskInstance != null) {
			ftpTaskInstance.addOnly();
			List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList = ftpTaskInstance
					.getFtpTaskInstanceInfoList();
			if (ftpTaskInstanceInfoList != null) {
				for (FtpTaskInstanceInfo ftpTaskInstanceInfo : ftpTaskInstanceInfoList) {
					ftpTaskInstanceInfo.setFtpTaskInstanceId(ftpTaskInstance
							.getFtpTaskInstanceId());
					ftpTaskInstanceInfo.addOnly();
				}
			}
		}
	}

	@Override
	public void updateFtpTaskInstance(FtpTaskInstance ftpTaskInstance) {
		ftpTaskInstance.updateOnly();
	}

	@Override
	public List<FtpTaskInstance> queryFtpTaskInstanceList(
			FtpTaskInstance queryFtpTaskInstance) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM FTP_TASK_INSTANCE A WHERE A.STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryFtpTaskInstance != null) {
			if (!StrUtil.isNullOrEmpty(queryFtpTaskInstance
					.getFtpTaskInstanceId())) {
				sb.append(" AND A.FTP_TASK_INSTANCE_ID = ?");
				params.add(queryFtpTaskInstance.getFtpTaskInstanceId());
			}

			if (!StrUtil.isNullOrEmpty(queryFtpTaskInstance.getTreeId())) {
				sb.append(" AND A.TREE_ID = ?");
				params.add(queryFtpTaskInstance.getTreeId());
			}

			if (!StrUtil.isNullOrEmpty(queryFtpTaskInstance.getSyncType())) {
				sb.append(" AND A.SYNC_TYPE = ?");
				params.add(queryFtpTaskInstance.getSyncType());
			}

			if (!StrUtil.isEmpty(queryFtpTaskInstance.getResult())) {
				sb.append(" AND A.RESULT = ?");
				params.add(queryFtpTaskInstance.getResult());
			}
		}
		sb.append(" ORDER BY FTP_TASK_INSTANCE_ID");
		return DefaultDaoFactory.getDefaultDao().jdbcFindList(sb.toString(),
				params, FtpTaskInstance.class);
	}

}
