package cn.ffcs.uom.webservices.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;

public interface IntfTaskInstanceManager {
	/**
	 * 查找未激活的
	 * 
	 * @param intfCode
	 *            接口编码
	 * @param times
	 *            同步上限次数
	 * @param targetSystem
	 *            目标系统
	 * @return
	 */
	public List<IntfTaskInstance> queryActiveTaskList(String intfCode,
			Long limitTimes);

	public List<IntfTaskInstance> queryIntfTaskInstanceList(
			IntfTaskInstance intfTaskInstance);

	/**
	 * 更新
	 * 
	 * @param intfTaskInstance
	 */
	public void updateIntfTaskInstance(IntfTaskInstance intfTaskInstance);

	/**
	 * 新增ftp通知
	 * 
	 * @param treeId
	 * @param syncType
	 * @param filePath
	 * @param fileList
	 */
	public void addFtpInformIntfTaskInstance(String treeId, String syncType,
			String filePath, List<FtpTaskInstanceInfo> fileList, Date dataDate,
			Long ftpTaskInstanceId);

	/**
	 * 
	 * @param mps
	 * @return
	 * @author wongs
	 * @date 2014-8-21 下午12:14:12
	 * @comment
	 */
	public int thresholdAlarm(Map<String, Object> mps);

	/**
	 * 修改AttrValue 中接口状态值
	 * 
	 * @param objs
	 * @author wongs
	 * @date 2014-8-25 下午3:05:11
	 * @comment
	 */
	public void interfaceStatus(Object[] objs);

}
