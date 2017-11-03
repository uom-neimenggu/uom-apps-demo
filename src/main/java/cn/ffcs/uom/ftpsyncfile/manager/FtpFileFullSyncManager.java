package cn.ffcs.uom.ftpsyncfile.manager;

public interface FtpFileFullSyncManager {
	/**
	 * 用于轮询生成全量数据
	 */
	public void createFullSyncData();
}
