package cn.ffcs.uom.publishLog.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.publishLog.model.PublishLog;

public interface PublishLogManager {

	/**
	 * 分页取类信息
	 * 
	 * @param orgTree
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgTree(PublishLog publishLog, int currentPage,
			int pageSize);
	
	/**
	 * 保存记录
	 * 
	 * @param publishLog
	 */
	public void addPublishLog(PublishLog publishLog);

}
