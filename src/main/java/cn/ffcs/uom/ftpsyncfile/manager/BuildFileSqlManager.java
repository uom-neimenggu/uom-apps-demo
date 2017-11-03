package cn.ffcs.uom.ftpsyncfile.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.ftpsyncfile.model.BuildFileSql;

public interface BuildFileSqlManager {
	/**
	 * 查询列表
	 * 
	 * @param queryBuildFileSql
	 * @return
	 */
	public List<BuildFileSql> queryBuildFileSqlList(
			BuildFileSql queryBuildFileSql);

	/**
	 * 返回FTP生成文件信息map： sql列表,参数列表,表名
	 * 
	 * @param treeId
	 * @param lastDate
	 * @param thisDate
	 * @param syncType
	 * @return
	 */
	public List<Map> getFtpFileGenerateInfo(Long treeId, Date lastDate,
			Date thisDate, String syncType);

	/**
	 * 生成本地文件
	 * 
	 * @param ftpFileGenerateInfoList
	 * @return
	 */
	public boolean createLocalFtpFiles(Long treeId, Date lastDate,
			Date thisDate, String syncType) throws Exception;
}
