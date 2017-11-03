package cn.ffcs.uom.ftpsyncfile.manager.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.constants.FtpFileConstant;
import cn.ffcs.uom.ftpsyncfile.dao.BuildFileSqlDao;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.manager.SyncTableColumnAliasConfigManager;
import cn.ffcs.uom.ftpsyncfile.manager.SyncTableColumnConfigManager;
import cn.ffcs.uom.ftpsyncfile.model.BuildFileSql;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.ftpsyncfile.model.SyncTableColumnConfig;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Service("buildFileSqlManager")
@Scope("prototype")
public class BuildFileSqlManagerImpl implements BuildFileSqlManager {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "buildFileSqlDao")
	private BuildFileSqlDao buildFileSqlDao;

	@Resource(name = "ftpTaskInstanceManager")
	private FtpTaskInstanceManager ftpTaskInstanceManager;

	@Resource(name = "syncTableColumnConfigManager")
	private SyncTableColumnConfigManager syncTableColumnConfigManager;

	@Resource(name = "syncTableColumnAliasConfigManager")
	private SyncTableColumnAliasConfigManager syncTableColumnAliasConfigManager;

	@Override
	public List<BuildFileSql> queryBuildFileSqlList(
			BuildFileSql queryBuildFileSql) {
		String sql = "SELECT * FROM BUILD_FILE_SQL A WHERE A.STATUS_CD=?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryBuildFileSql != null) {
		}
		return buildFileSqlDao.jdbcFindList(sql, params, BuildFileSql.class);
	}

	@Override
	public List<Map> getFtpFileGenerateInfo(Long treeId, Date lastDate,
			Date thisDate, String syncType) {
		List<Map> ftpFileGenerateInfoList = new ArrayList<Map>();
		/**
		 * 获取全部
		 */
		List<BuildFileSql> list = this
				.queryBuildFileSqlList(new BuildFileSql());
		if (list != null && list.size() > 0) {
			for (BuildFileSql buildFileSql : list) {
				if (buildFileSql != null) {
					Map ftpFileInfoMap = new HashMap();
					List params = new ArrayList();
					/**
					 * 增量
					 */
					String sql = "";
					String paramsStr = "";
					if (WsConstants.SYNC_PART.equals(syncType)) {
						sql = this.sqlConvert(buildFileSql.getIncreaseSql(),
								treeId);
						params = this.paramsConvert(
								buildFileSql.getParameterIs(), treeId,
								lastDate, thisDate);
					} else {
						/**
						 * 全量
						 */
						sql = this
								.sqlConvert(buildFileSql.getFullSql(), treeId);
						params = this.paramsConvert(
								buildFileSql.getParameterFl(), treeId,
								lastDate, thisDate);
					}
					ftpFileInfoMap.put("sql", sql);
					ftpFileInfoMap.put("params", params);
					ftpFileInfoMap.put("name", buildFileSql.getObjectName());
					ftpFileGenerateInfoList.add(ftpFileInfoMap);
				}
			}
		}
		return ftpFileGenerateInfoList;
	}

	/**
	 * 创建本地文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean createLocalFtpFiles(Long treeId, Date lastDate,
			Date thisDate, String syncType) {
		boolean result = false;
		String path = "";
		try {
			if (thisDate == null || lastDate == null) {
				throw new Exception("时间参数错误");
			}
			/**
			 * 生成中间表
			 */
			logger.info("3.2、生成中间表开始->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new Date()));
			this.buildFileSqlDao.createSyncTempTable(treeId,
					DateUtil.dateToStr(lastDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtil.dateToStr(thisDate, "yyyy-MM-dd HH:mm:ss"));
			logger.info("3.2、生成中间表结束->"
					+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
							new Date()));
			List<Map> ftpFileGenerateInfoList = this.getFtpFileGenerateInfo(
					treeId, lastDate, thisDate, syncType);
			/**
			 * 组织树同步表字段别名配置
			 */
			/*
			 * SyncTableColumnAliasConfig querySyncTableColumnAliasConfig = new
			 * SyncTableColumnAliasConfig();
			 * querySyncTableColumnAliasConfig.setTreeId(treeId);
			 * List<SyncTableColumnAliasConfig> syncTableColumnAliasConfigList =
			 * this.syncTableColumnAliasConfigManager
			 * .querySyncTableColumnAliasConfigList
			 * (querySyncTableColumnAliasConfig);
			 */

			if (ftpFileGenerateInfoList != null
					&& ftpFileGenerateInfoList.size() > 0) {
				path = UomClassProvider.getSystemConfig("localFtpPath");
				if (StrUtil.isEmpty(path)) {
					throw new Exception("本地路径未配置");
				} else {
					path += "/"
							+ DefaultDaoFactory.getDefaultDao().genTransId("1",
									10, "SEQ_FTP_BATCH_NBR");
				}

				FtpTaskInstance ftpTaskInstance = new FtpTaskInstance();
				ftpTaskInstance.setTreeId(treeId);
				ftpTaskInstance.setLastDate(lastDate);
				ftpTaskInstance.setThisDate(thisDate);
				ftpTaskInstance.setLocalPath(path);
				ftpTaskInstance
						.setResult(FtpFileConstant.FTP_TASK_INSTANCE_LOCAL);
				ftpTaskInstance.setSyncType(syncType);
				List<FtpTaskInstanceInfo> ftpTaskInstanceInfolist = new ArrayList<FtpTaskInstanceInfo>();
				logger.info("3.3、创建本地文件开始->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				for (Map map : ftpFileGenerateInfoList) {
					String sql = (String) map.get("sql");
					List params = (List) map.get("params");
					String name = (String) map.get("name");
					List list = DefaultDaoFactory.getDefaultDao()
							.getJdbcTemplate()
							.queryForList(sql, params.toArray());
					String fileName = name + ".txt";
					if (list != null && list.size() > 0) {
						// List<String> configColunmList =
						// this.syncTableColumnConfigManager
						// .queryTableColumnNameList(treeId, name);
						List<SyncTableColumnConfig> configColunmList = this.syncTableColumnConfigManager
								.querySyncTableColumnConfigList(treeId, name);
						if (configColunmList == null
								|| configColunmList.size() == 0) {
							throw new Exception("树：" + treeId + ",表字段未配置");
						}
						// FileUtil.createFile(list, fileName, path,
						// configColunmList,
						// syncTableColumnAliasConfigList);
						/*
						 * FileUtil.createSyncFtpFile(list, fileName, path,
						 * configColunmList, syncTableColumnAliasConfigList);
						 */
						FileUtil.createSyncFtpFile(list, fileName, path,
								configColunmList);
						FtpTaskInstanceInfo ftpTaskInstanceInfo = new FtpTaskInstanceInfo();
						ftpTaskInstanceInfo.setFileName(fileName);
						ftpTaskInstanceInfo.setDataCount(Long.parseLong(list
								.size() + ""));
						ftpTaskInstanceInfolist.add(ftpTaskInstanceInfo);
					}
				}
				logger.info("3.3、创建本地文件结束->"
						+ DateUtil.getDateByDateFormat("yyyy-MM-dd HH:mm:ss",
								new Date()));
				ftpTaskInstance
						.setFtpTaskInstanceInfoList(ftpTaskInstanceInfolist);
				logger.info("3.4、本地文件生成成功更新result为1 开始 ftpTaskInstanceId = "
						+ ftpTaskInstance.getFtpTaskInstanceId() + " ->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				ftpTaskInstanceManager.savaFtpTaskInstance(ftpTaskInstance);
				logger.info("3.4、本地文件生成成功更新result为1  结束 ftpTaskInstanceId = "
						+ ftpTaskInstance.getFtpTaskInstanceId() + " ->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				result = true;
			}
		} catch (Exception e) {
			try {
				FileUtil.deletefile(path);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 参数转换
	 * 
	 * @param paramsStr
	 *            :P1,P2,P3(逗号隔开) ;P1:组织树id P2:上次树发布时间 P3:本次发布时间
	 * @return
	 */
	private List paramsConvert(String paramsStr, Long treeId, Date lastDate,
			Date thisDate) {
		List params = new ArrayList();
		String[] paramsArray = paramsStr.split(",");
		if (paramsArray != null && paramsArray.length > 0) {
			for (String str : paramsArray) {
				if ("P1".equals(str)) {
					params.add(treeId);
				}
				if ("P2".equals(str)) {
					if (lastDate != null) {
						// params.add("2013/7/15 0:26:19");
						params.add(DateUtil.dateToStr(lastDate,
								"yyyy-MM-dd HH:mm:ss"));
					}
				}
				if ("P3".equals(str)) {
					if (thisDate != null) {
						// params.add("2013/7/15 0:55:13");
						params.add(DateUtil.dateToStr(thisDate,
								"yyyy-MM-dd HH:mm:ss"));
					}
				}
			}
		}
		return params;
	}

	/**
	 * sql转换
	 * 
	 * @param sql
	 * @return
	 */
	private String sqlConvert(String sql, Long treeId) {
		if (!StrUtil.isEmpty(sql)) {
			sql = sql.replaceAll("#1#", "ORG_BUILD_TEMP" + treeId);
			sql = sql.replaceAll("#2#", "STAFF_BUILD_TEMP" + treeId);
		}
		return sql;
	}

}
