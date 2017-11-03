package cn.ffcs.uom.webservices.timetask;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.manager.FtpTaskInstanceManager;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;
import cn.ffcs.uom.webservices.bean.ftpinform.Root;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.InformFtpInfoManager;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Component("ftpInfoInformTimerTask")
@Scope("prototype")
public class FtpInfoInformTimerTask extends TimerTask {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 接口任务实例manager
	 */
	@Resource
	private IntfTaskInstanceManager intfTaskInstanceManager;
	/**
	 * FTP任务实例manager
	 */
	@Resource
	private FtpTaskInstanceManager ftpTaskInstanceManager;
	/**
	 * webservices manager
	 */
	@Resource
	private InformFtpInfoManager informFtpInfoManager;
	/**
	 * 业务系统
	 */
	@Resource
	private BusinessSystemManager businessSystemManager;
	/**
	 * 短信配置
	 */
	@Resource
	private SystemMessageLogManager systemMessageLogManager;
	/**
	 * 查询条件
	 */
	private BusinessSystem businessSystem;

	@Resource
	private SystemIntfInfoConfigManager systemIntfInfoConfigManager;

	@Override
	public void run() {

		logger.info("FtpInfoInformTimerTask run at!");
		logger.info("1、通知下游系统接收数据开始->"
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));

		try {
			/**
			 * 按id升序顺序取出系统未执行的FTP通知
			 */
			List<IntfTaskInstance> informToCrmTaskList = intfTaskInstanceManager
					.queryActiveTaskList(WsConstants.INTF_CODE_FTP_INFO_INFORM,
							WsConstants.LIMIT_TIMES_FTP);
			if (informToCrmTaskList != null && informToCrmTaskList.size() > 0) {

				for (IntfTaskInstance intfTaskInstance : informToCrmTaskList) {
					if (intfTaskInstance != null
							&& intfTaskInstance.getIntfTaskInstanceId() != null) {
						Boolean flag = intfTaskInstance
								.hasPrevFailEdIntfTaskInstance();
						businessSystem = new BusinessSystem();
						businessSystem.setSystemCode(intfTaskInstance
								.getTargetSystem());

						/**
						 * 前面没有失败的记录
						 */
						if (flag != null && !flag) {

							// 增量阀值控制
							if (UomClassProvider
									.isOpenSwitch(Constants.THRESHOLD_ALARM)) {// 增量阀值开关

								FtpTaskInstance ftpTaskInstance = new FtpTaskInstance();
								ftpTaskInstance
										.setFtpTaskInstanceId(intfTaskInstance
												.getFtpTaskInstanceId());

								List<FtpTaskInstance> ftpTaskInstanceList = ftpTaskInstanceManager
										.queryFtpTaskInstanceList(ftpTaskInstance);

								if (ftpTaskInstanceList != null
										&& ftpTaskInstanceList.size() > 0) {
									ftpTaskInstance = ftpTaskInstanceList
											.get(0);
								}

								if (WsConstants.SYNC_PART
										.equals(ftpTaskInstance.getSyncType())) {// 对增量发布进行短信告警监控
									logger.info("2、对增量发布进行短信告警监控开始"
											+ intfTaskInstance
													.getTargetSystem()
											+ "->"
											+ DateUtil.dateToStr(new Date(),
													"yyyy-MM-dd HH:mm:ss"));

									List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList = ftpTaskInstance
											.getFtpTaskInstanceInfoList();

									if (ftpTaskInstanceInfoList != null
											&& ftpTaskInstanceInfoList.size() > 0) {

										boolean thresholdAlarmSign = false;// 阀值告警控制标志
										String thresholdValues = UomClassProvider
												.getSystemConfig(Constants.THRESHOLD_VALUES);

										for (FtpTaskInstanceInfo ftpTaskInstanceInfo : ftpTaskInstanceInfoList) {

											String fileName = ftpTaskInstanceInfo
													.getFileName()
													.substring(
															0,
															ftpTaskInstanceInfo
																	.getFileName()
																	.indexOf(
																			"."));

											if (ftpTaskInstanceInfo
													.getDataCount() > Long
													.parseLong(thresholdValues)
													&& !fileName
															.equals("ORGANIZATION_EXTEND_ATTR")) {// 增量下发数据量大于阀值
																									// 排除ORGANIZATION_EXTEND_ATTR表
												thresholdAlarmSign = true;
												break;
											}
										}

										if (thresholdAlarmSign) {

											SystemIntfInfoConfig systemIntfInfoConfig = systemIntfInfoConfigManager
													.querySystemIntfInfoConfigBySystemCode(intfTaskInstance
															.getTargetSystem());

											if (!StrUtil
													.isNullOrEmpty(systemIntfInfoConfig)
													&& !StrUtil
															.isNullOrEmpty(systemIntfInfoConfig
																	.getSystemCode())) {
												systemIntfInfoConfig
														.setIntfSwitchIncrease(WsConstants.INTF_SWITCH_CLOSE);
												systemIntfInfoConfig
														.updateOnly();
											}

											BusinessSystem systemMessageConfigAlarm = new BusinessSystem();
											systemMessageConfigAlarm
													.setSystemCode(WsConstants.SYSTEM_CODE_UOM);// UOM主数据平台

											BusinessSystem businessSystem = new BusinessSystem();
											businessSystem
													.setSystemCode(intfTaskInstance
															.getTargetSystem());
											List<BusinessSystem> businessSystemList = businessSystemManager
													.queryBusinessSystemList(businessSystem);
											if (businessSystemList != null
													&& businessSystemList
															.size() == 1) {
												businessSystem = businessSystemList
														.get(0);
											}

											systemMessageLogManager
													.saveSystemMessageLog(
															systemMessageConfigAlarm,
															null,
															"增量数据量超过阀值，关闭"
																	+ businessSystem
																			.getSystemDesc()
																	+ "系统增量下发接口!",
															intfTaskInstance);

										}
									}
									logger.info("2、对增量发布进行短信告警监控结束"
											+ intfTaskInstance
													.getTargetSystem()
											+ "->"
											+ DateUtil.dateToStr(new Date(),
													"yyyy-MM-dd HH:mm:ss"));
								}
							}

							/**
							 * msgType为空
							 */
							if (StrUtil.isEmpty(intfTaskInstance.getMsgType())) {
								intfTaskInstance
										.setInvokeTimes(intfTaskInstance
												.getInvokeTimes() + 1);
								intfTaskInstance
										.setInvokeResule(WsConstants.TASK_MSGTYPE_NULL);
								systemMessageLogManager.saveSystemMessageLog(
										businessSystem, null,
										"MSG_TYPE为空", intfTaskInstance);
							}

							Method method = informFtpInfoManager.getClass()
									.getMethod(intfTaskInstance.getMsgType(),
											IntfTaskInstance.class);
							/**
							 * msgType错误
							 */
							if (method == null) {
								intfTaskInstance
										.setInvokeTimes(intfTaskInstance
												.getInvokeTimes() + 1);
								intfTaskInstance
										.setInvokeResule(WsConstants.TASK_MSGTYPE_ERROR);
								systemMessageLogManager
										.saveSystemMessageLog(
												businessSystem,
												null,
												"MSG_TYPE配置错误"
														+ intfTaskInstance
																.getMsgType(),
												intfTaskInstance);
							}

							Root root = null;

							try {

								logger.info("3、第"
										+ intfTaskInstance.getInvokeTimes()
										+ "次开始调用下游系统接口"
										+ intfTaskInstance.getTargetSystem()
										+ "->"
										+ DateUtil.dateToStr(new Date(),
												"yyyy-MM-dd HH:mm:ss"));
								root = (Root) method.invoke(
										informFtpInfoManager, intfTaskInstance);
								logger.info("3、第"
										+ intfTaskInstance.getInvokeTimes()
										+ "次结束调用下游系统接口"
										+ intfTaskInstance.getTargetSystem()
										+ "->"
										+ DateUtil.dateToStr(new Date(),
												"yyyy-MM-dd HH:mm:ss"));
							} catch (Exception e) {
								logger.info("3、调用下游系统接口失败"
										+ intfTaskInstance.getTargetSystem()
										+ "->"
										+ DateUtil.dateToStr(new Date(),
												"yyyy-MM-dd HH:mm:ss"));
								intfTaskInstance
										.setInvokeTimes(intfTaskInstance
												.getInvokeTimes() + 1);
								intfTaskInstance
										.setInvokeResule(WsConstants.TASK_INVOKE_ERROR);
								systemMessageLogManager.saveSystemMessageLog(
										businessSystem, e.getMessage(),
										"方法反射错误", intfTaskInstance);
							}
							boolean result = false;
							if (root != null
									&& root.getMsgBody() != null
									&& root.getMsgBody().getOutParam() != null
									&& !StrUtil.isEmpty(root.getMsgBody()
											.getOutParam().getResult())) {
								if (WsConstants.SUCCESS
										.equals(root.getMsgBody().getOutParam()
												.getResult())) {
									result = true;
								}
							}
							intfTaskInstance.setInvokeTimes(intfTaskInstance
									.getInvokeTimes() + 1);
							/**
							 * 通知成功
							 */
							if (result) {
								intfTaskInstance
										.setInvokeResule(WsConstants.TASK_SUCCESS);
							} else {
								intfTaskInstance
										.setInvokeResule(WsConstants.TASK_FAILED);
								systemMessageLogManager.saveSystemMessageLog(
										businessSystem, null,
										"数据未成功下发，请检查接口服务是否存在异常情况！",
										intfTaskInstance);
							}
						} else {
							intfTaskInstance
									.setInvokeResule(WsConstants.TASK_FRONT_ERROR);
						}
						logger.info("5、更新InvokeResule结果开始"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						intfTaskInstanceManager
								.updateIntfTaskInstance(intfTaskInstance);
						logger.info("5、更新InvokeResule结果结束"
								+ intfTaskInstance.getTargetSystem()
								+ "->"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
					}
				}
			}
		} catch (Exception e) {
			logger.info("1、通知下游系统接收数据失败->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			e.printStackTrace();
		}
		logger.info("1、通知下游系统接收数据结束->"
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}

}
