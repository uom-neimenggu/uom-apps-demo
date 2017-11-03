package cn.ffcs.uom.ftpsyncfile.manager.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.ftpsyncfile.manager.FtpFileFullSyncManager;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Component("ftpFileFullSyncManager")
public class FtpFileFullSyncManagerImpl implements FtpFileFullSyncManager {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "orgTreeManager")
	private OrgTreeManager orgTreeManager;

	@Resource(name = "buildFileSqlManager")
	private BuildFileSqlManager buildFileSqlManager;

	/**
	 * 生成全量数据，用来轮询
	 */
	public void createFullSyncData() {
		logger.info("createFullSyncData:"
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		List<OrgTree> orgTreeList = orgTreeManager.queryOrgTreeList(null);
		if (orgTreeList != null && orgTreeList.size() > 0) {
			for (OrgTree orgTree : orgTreeList) {
				Long treeId = orgTree.getOrgTreeId();
				boolean fullSwitch = UomClassProvider
						.isOpenSwitch(Constants.TREE_FULL_SWITCH + treeId);
				if (!fullSwitch) {
					continue;
				}
				Date lastDate = orgTree.getPreTime();
				Date thisDate = orgTree.getLastTime();
				String syncType = WsConstants.SYNC_ALL;
				try {
					boolean flag = buildFileSqlManager.createLocalFtpFiles(
							treeId, lastDate, thisDate, syncType);
					logger.info(DateUtil.dateToStr(new Date(),
							"yyyy-MM-dd HH:mm:ss")
							+ ":treeId:"
							+ treeId
							+ " flag:" + flag);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
