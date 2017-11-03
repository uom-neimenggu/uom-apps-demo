package cn.ffcs.uom.ftpsyncfile.manager;

import java.util.List;

import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;

public interface FtpTaskInstanceManager {
	/**
	 * 保存ftp任务实例
	 * 
	 * @param ftpTaskInstance
	 */
	public void savaFtpTaskInstance(FtpTaskInstance ftpTaskInstance);

	/**
	 * 更新ftp任务实例
	 * 
	 * @param ftpTaskInstance
	 */
	public void updateFtpTaskInstance(FtpTaskInstance ftpTaskInstance);

	/**
	 * 查询
	 * 
	 * @param queryFtpTaskInstance
	 */
	public List<FtpTaskInstance> queryFtpTaskInstanceList(
			FtpTaskInstance queryFtpTaskInstance);
}
