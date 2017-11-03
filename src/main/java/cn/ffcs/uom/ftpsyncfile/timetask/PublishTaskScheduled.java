package cn.ffcs.uom.ftpsyncfile.timetask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.common.constants.OperateLogConstants;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaManager;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgStaffRelaManager;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.publishLog.manager.PublishLogManager;
import cn.ffcs.uom.publishLog.model.PublishLog;
import cn.ffcs.uom.webservices.util.WsClientUtil;

@Component("publishTaskScheduled")
public class PublishTaskScheduled {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "orgTreeManager")
	private OrgTreeManager orgTreeManager;

	@Resource(name = "operateLogManager")
	private OperateLogManager operateLogManager;

	@Resource(name = "publishLogManager")
	private PublishLogManager publishLogManager;

	@Resource
	private TreeOrgStaffRelaManager treeOrgStaffRelaManager;

	@Resource
	private TreeOrgRelaManager treeOrgRelaManager;

	/**
	 * jdbcTemplate
	 */
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 发布和生成增量数据，用来轮循
	 */
	@SuppressWarnings("null")
	public void publishAndCreateSyncData() {

		logger.info("PublishTimerTask run at: "
				+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));

		List<OrgTree> orgTreeList = orgTreeManager.queryOrgTreeList(null);
		OrgTree publishing = new OrgTree();
		publishing.setIsPublishing(1L);
		List<OrgTree> orgTreeList2 = orgTreeManager.queryOrgTreeList(publishing);

		if (orgTreeList != null && orgTreeList.size() > 0 && (orgTreeList2 == null || orgTreeList2.size() == 0)) {
			logger.info("----------------1");
			for (OrgTree orgTree : orgTreeList) {
				logger.info("----------------2-----"+orgTree.getOrgTreeId());
				/**
				 * 记录发布日志
				 */
				PublishLog publishLog = PublishLog.newInstance();
				publishLog.setOrgTreeId(orgTree.getOrgTreeId());
				publishLog.setPublishDate(new Date());
				publishLog.setGenDate(orgTree.getLastTime());
				publishLog.setLastGenDate(orgTree.getPreTime());

				boolean treeSwitch = UomClassProvider.isOpenSwitch(orgTree
						.getOrgTreeId().toString());
				/**
				 * 开关关闭
				 */
				if (!treeSwitch) {
					publishLog.setPublishMsg("The tree "+ orgTree.getOrgTreeId() + " switch off");
					publishLogManager.addPublishLog(publishLog);
					continue;
				}
				OperateLog operateLog = null;
				try {
					/**
					 * 锁住树：只能同时一个在发布
					 */
					orgTree = orgTreeManager.getOrgTreeByOrgTreeId(orgTree.getOrgTreeId());
					if (orgTree == null) {
						publishLog.setPublishMsg("The tree " + orgTree.getOrgTreeId() + " status is error");
						publishLogManager.addPublishLog(publishLog);
						continue;
					} else {

						operateLog = new OperateLog();
						operateLog.setCreateDate(orgTree.getLastTime());

						List<OperateLog> operateLogList = operateLogManager
								.queryMaxOperateLogList(operateLog);

						if (operateLogList != null && operateLogList.size() > 0) {
							operateLog = operateLogList.get(0);
						} else {
							operateLog = null;
						}

						if (operateLog == null) {
							publishLog.setPublishMsg("The tree "+ orgTree.getOrgTreeId() + " operateLog is null");
							publishLogManager.addPublishLog(publishLog);
							continue;
						}

						/**
						 * 发布时间必须小于要发布的时间
						 */
						if (null != orgTree.getLastTime() && null != operateLog.getCreateDate()) {
							int r = orgTree.getLastTime().compareTo(operateLog.getCreateDate());
							logger.info("----------------3");
							if (r >= 0) {
								publishLog.setPublishMsg("The tree" + orgTree.getOrgTreeId() + " recording has been published");
								publishLogManager.addPublishLog(publishLog);
								continue;
							}
						} else {
							logger.info("----------------4");
							publishLog.setPublishMsg("The last release time or tree " + orgTree.getOrgTreeId() + " log time error cannot be published");
							publishLogManager.addPublishLog(publishLog);
							continue;
						}

						// 已经发布中
						OrgTree publisingOrgTree = new OrgTree();
						publisingOrgTree.setIsPublishing(OperateLogConstants.IS_PUBLISHING);
						List<OrgTree> publishingTrees = orgTreeManager.queryOrgTreeList(publisingOrgTree);
						if (publishingTrees != null && publishingTrees.size() > 0) {
							publishLog.setPublishMsg("树" + publishingTrees.get(0).getOrgTreeId() + "正在发布，取消此次定时任务");
							publishLogManager.addPublishLog(publishLog);
							break;
						}
						// 锁定
						orgTree.setIsPublishing(OperateLogConstants.IS_PUBLISHING);
						orgTreeManager.updateOrgTree(orgTree);
						String callUrl = UomClassProvider.getIntfUrl("localIncrementalDataUrl");
						if (StrUtil.isEmpty(callUrl)) {
							// 解除锁定
							orgTree.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
							orgTreeManager.updateOrgTree(orgTree);
							publishLog.setPublishMsg("Failed to publish, address localIncrementalDataUrl is not configured");
							publishLogManager.addPublishLog(publishLog);
							break;
						}
						List<Object> paramsList = new ArrayList<Object>();
						paramsList.add(DateUtil.getDateByDateFormat(
								"yyyyMMddHHmmss", orgTree.getLastTime()));
						paramsList.add(DateUtil.getDateByDateFormat(
								"yyyyMMddHHmmss", operateLog.getCreateDate()));
						paramsList.add(orgTree.getOrgTreeId());
						logger.info("5----------------调用存储过程开始,orgTreeId="+orgTree.getOrgTreeId());
						logger.info("6----------------"+paramsList.get(0));
						logger.info("7----------------"+paramsList.get(1));
						logger.info("8----------------"+paramsList.get(2));
						DbUtil.procedureCall(
								jdbcTemplate,
								"{call PKG_OPERATELOG_PUBLISH.updateTreeOrgDate(?,?,?)}",
								paramsList);
						logger.info("9----------------调用存储过程结束,orgTreeId="+orgTree.getOrgTreeId());
						// treeOrgRelaManager.updateTreeOrgRelaData(orgTree
						// .getOrgTreeId(), operateLog.getCreateDate());
						// treeOrgStaffRelaManager.updateTreeOrgStaffRelaData(
						// orgTree.getOrgTreeId(), operateLog
						// .getCreateDate());
						/*
						 * treeOrgRelaManager.updateTreeOrgRelaData(
						 * orgTree.getOrgTreeId(), orgTree .getLastTime(),
						 * operateLog .getCreateDate());
						 * treeOrgStaffRelaManager.updateTreeOrgStaffRelaData(
						 * orgTree.getOrgTreeId(), orgTree .getLastTime(),
						 * operateLog .getCreateDate());
						 */
						/**
						 * 改调接口（发布机发布）
						 */
						String outXml = WsClientUtil
								.wsCallCreateIncrementalData(callUrl,
										"createIncrementalData",
										orgTree.getOrgTreeId(),
										orgTree.getLastTime(),
										operateLog.getCreateDate(), "0");

						if (!StrUtil.isEmpty(outXml)) {
							// 解除锁定（不能在finally中处理）
							orgTree.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
							orgTreeManager.updateOrgTree(orgTree);
							publishLog
									.setPublishMsg("Generate the FTP file failed："
											+ (outXml.length() > 200 ? outXml
													.substring(0, 200) : outXml));
							publishLogManager.addPublishLog(publishLog);
							continue;
						}
						/**
						 * 组织树时间信息变更
						 */
						orgTree.setPreTime(orgTree.getLastTime());
						orgTree.setLastTime(operateLog.getCreateDate());
						orgTreeManager.updateOrgTree(orgTree);

						// 解除锁定
						orgTree.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
						orgTreeManager.updateOrgTree(orgTree);
						publishLog.setPublishMsg("The tree"
								+ orgTree.getOrgTreeId()
								+ " publish success"
								+ DateUtil.dateToStr(new Date(),
										"yyyy-MM-dd HH:mm:ss"));
						publishLogManager.addPublishLog(publishLog);
					}
				} catch (Exception e) {
					e.printStackTrace();
					orgTree.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
					orgTreeManager.updateOrgTree(orgTree);
					publishLog.setPublishMsg("The tree"
							+ orgTree.getOrgTreeId()
							+ " exception："
							+ (e.getMessage().length() > 200 ? e.getMessage()
									.substring(0, 200) : e.getMessage()));
					publishLogManager.addPublishLog(publishLog);
				}

			}
		}
	}
}
