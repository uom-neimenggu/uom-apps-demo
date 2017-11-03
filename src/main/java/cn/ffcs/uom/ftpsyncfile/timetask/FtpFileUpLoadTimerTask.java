package cn.ffcs.uom.ftpsyncfile.timetask;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.FtpUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.constants.FtpFileConstant;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;

@Component("ftpFileUpLoadTimerTask")
@Scope("prototype")
public class FtpFileUpLoadTimerTask extends TimerTask {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ftpTaskInstanceManager")
	private FtpTaskInstanceManager ftpTaskInstanceManager;

	@Resource(name = "intfTaskInstanceManager")
	private IntfTaskInstanceManager intfTaskInstanceManager;

	@Override
	public void run() {
		logger.info("1、本地文件上传开始->"
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		try {
			FtpTaskInstance queryFtpTaskInstance = new FtpTaskInstance();
			queryFtpTaskInstance
					.setResult(FtpFileConstant.FTP_TASK_INSTANCE_LOCAL);
			/**
			 * 查询本地提交成功的实例
			 */
			List<FtpTaskInstance> ftpTaskInstanceList = ftpTaskInstanceManager
					.queryFtpTaskInstanceList(queryFtpTaskInstance);
			if (ftpTaskInstanceList != null && ftpTaskInstanceList.size() > 0) {
				logger.info("1.1、本地待上传的文件记录共有" + ftpTaskInstanceList.size()
						+ "条->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				/**
				 * 远程路径格式：路径/日期/批次号
				 */
				String dateStr = DateUtil.dateToStr(new Date(), "yyyyMMdd");
				String ftpHost = UomClassProvider.getSystemConfig("ftpHost");
				String ftpUsername = UomClassProvider
						.getSystemConfig("ftpUsername");
				String ftpPassword = UomClassProvider
						.getSystemConfig("ftpPassword");
				if (StrUtil.isEmpty(ftpHost) || StrUtil.isEmpty(ftpUsername)
						|| StrUtil.isEmpty(ftpPassword)) {
					throw new Exception("FTP信息配置错误");
				}
				logger.info("1.2、开始连接远程FTP服务器->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				FtpUtil.connect(ftpHost, 21, ftpUsername, ftpPassword);
				logger.info("1.4、成功连接远程FTP服务器->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				for (FtpTaskInstance ftpTaskInstance : ftpTaskInstanceList) {
					String batchNbr = DefaultDaoFactory.getDefaultDao()
							.genTransId("1", 10, "SEQ_FTP_REMOTE_BATCH_NBR");
					String remotePath = UomClassProvider
							.getSystemConfig("remoteFtpPath"
									+ ftpTaskInstance.getTreeId());
					if (StrUtil.isEmpty(remotePath)) {
						throw new Exception("远程ftp地址未配置");
					}
					String noticePath = "/" + dateStr + "/" + batchNbr;
					remotePath = remotePath + "/" + dateStr + "/" + batchNbr;
					/**
					 * 使查库
					 */
					ftpTaskInstance.setFtpTaskInstanceInfoList(null);
					List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList = ftpTaskInstance
							.getFtpTaskInstanceInfoList();
					if (ftpTaskInstanceInfoList != null
							&& ftpTaskInstanceInfoList.size() > 0) {
						String localPath = ftpTaskInstance.getLocalPath();
						logger.info("1.5、开始读取本地文件路径 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						List<String> filePathList = FileUtil
								.readfile(localPath);
						logger.info("1.6、结束读取本地文件路径 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						if (filePathList != null && filePathList.size() > 0) {
							logger.info("1.7、开始创建远程FTP文件路径 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
							FtpUtil.makeDirectory(remotePath);
							logger.info("1.11、结束创建远程FTP文件路径 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
							logger.info("1.12、开始上传远程FTP文件 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
							for (String path : filePathList) {
								String fileName = path.substring(path
										.lastIndexOf("/") + 1);
								logger.info("1.13、开始上传远程FTP文件: "
										+ fileName
										+ " ,ftpTaskInstanceId = "
										+ ftpTaskInstance
												.getFtpTaskInstanceId()
										+ " ->"
										+ DateUtil.dateToStr(new Date(),
												"yyyy-MM-dd HH:mm:ss"));
								FtpUtil.uploadFile(fileName, path, remotePath);
								logger.info("1.18、结束上传远程FTP文件: "
										+ fileName
										+ " ,ftpTaskInstanceId = "
										+ ftpTaskInstance
												.getFtpTaskInstanceId()
										+ " ->"
										+ DateUtil.dateToStr(new Date(),
												"yyyy-MM-dd HH:mm:ss"));
							}
							logger.info("1.19、结束上传远程FTP文件 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
						}

						/**
						 * 删除本地文件
						 */
						logger.info("1.19、开始删除本地FTP文件 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						FileUtil.deletefile(localPath);
						logger.info("1.21、成功删除本地FTP文件 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));

						/**
						 * 上传成功更新result为2
						 */
						ftpTaskInstance
								.setResult(FtpFileConstant.FTP_TASK_INSTANCE_REMOTE);
						ftpTaskInstance.setRemotePath(remotePath);
						ftpTaskInstance.setNoticePath(noticePath);
						logger.info("1.22、上传成功更新result为2 开始 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						ftpTaskInstanceManager
								.updateFtpTaskInstance(ftpTaskInstance);
						logger.info("1.23、上传成功更新result为2 结束 ftpTaskInstanceId = "
								+ ftpTaskInstance.getFtpTaskInstanceId()
								+ " ->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
					} else {
						/**
						 * 没有文件
						 */
						ftpTaskInstance
								.setResult(FtpFileConstant.FTP_TASK_INSTANCE_NOT_FILE);
						ftpTaskInstanceManager
								.updateFtpTaskInstance(ftpTaskInstance);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				FtpUtil.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		logger.info("1、本地文件上传结束->"
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		/**
		 * 生成webservices实例
		 */
		try {
			FtpTaskInstance queryFtpTaskInstance = new FtpTaskInstance();
			queryFtpTaskInstance
					.setResult(FtpFileConstant.FTP_TASK_INSTANCE_REMOTE);
			/**
			 * 查询本地提交成功的实例
			 */
			List<FtpTaskInstance> ftpTaskInstanceList = ftpTaskInstanceManager
					.queryFtpTaskInstanceList(queryFtpTaskInstance);
			if (ftpTaskInstanceList != null && ftpTaskInstanceList.size() > 0) {
				for (FtpTaskInstance ftpTaskInstance : ftpTaskInstanceList) {
					if (ftpTaskInstance.hasPrevUnInformFtpTaskInstance() != null
							&& !ftpTaskInstance
									.hasPrevUnInformFtpTaskInstance()) {
						ftpTaskInstance.setFtpTaskInstanceInfoList(null);
						List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList = ftpTaskInstance
								.getFtpTaskInstanceInfoList();
						if (ftpTaskInstanceInfoList != null
								&& ftpTaskInstanceInfoList.size() > 0) {
							intfTaskInstanceManager
									.addFtpInformIntfTaskInstance(
											ftpTaskInstance.getTreeId() + "",
											ftpTaskInstance.getSyncType(),
											ftpTaskInstance.getNoticePath(),
											ftpTaskInstanceInfoList,
											ftpTaskInstance.getThisDate(),
											ftpTaskInstance
													.getFtpTaskInstanceId());
							/**
							 * 生成通知
							 */
							ftpTaskInstance
									.setResult(FtpFileConstant.FTP_TASK_INSTANCE_INFORM);
							logger.info("4、报文生成成功更新result为3 开始 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
							ftpTaskInstanceManager
									.updateFtpTaskInstance(ftpTaskInstance);
							logger.info("4、报文生成成功更新result为3 结束 ftpTaskInstanceId = "
									+ ftpTaskInstance.getFtpTaskInstanceId()
									+ " ->"
									+ DateUtil.dateToStr(new Date(),
											"yyyy-MM-dd HH:mm:ss"));
						}
					} else {
						/**
						 * 只更新时间，不生成通知
						 */
						ftpTaskInstanceManager
								.updateFtpTaskInstance(ftpTaskInstance);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
