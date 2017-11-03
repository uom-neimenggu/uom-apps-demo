package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.ffcs.uom.businesssystem.manager.SystemOrgTreeConfigManager;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.webservices.bean.comm.MsgHead;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.dao.IntfTaskInstanceDao;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;
import cn.ffcs.uom.webservices.util.CastorParser;
import cn.ffcs.uom.webservices.util.RootBuilder;
import cn.ffcs.uom.webservices.util.WsUtil;

@Service("intfTaskInstanceManager")
@Scope("prototype")
public class IntfTaskInstanceManagerImpl implements IntfTaskInstanceManager {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private IntfTaskInstanceDao intfTaskInstanceDao;

	@Resource
	private SystemOrgTreeConfigManager systemOrgTreeConfigManager;
	/**
	 * 系统接口配置
	 */
	@Resource
	private SystemIntfInfoConfigManager systemIntfInfoConfigManager;

	@Override
	public List<IntfTaskInstance> queryActiveTaskList(String intfCode,
			Long limitTimes) {
		String sql = "SELECT * FROM INTF_TASK_INSTANCE A WHERE A.INTF_CODE=? AND A.INVOKE_TIMES<? AND A.INVOKE_RESULE!=? ORDER BY A.INTF_TASK_INSTANCE_ID";
		List params = new ArrayList();
		params.add(intfCode);
		params.add(limitTimes);
		params.add(WsConstants.TASK_SUCCESS);
		return this.intfTaskInstanceDao.jdbcFindList(sql, params,
				IntfTaskInstance.class);
	}

	@Override
	public List<IntfTaskInstance> queryIntfTaskInstanceList(
			IntfTaskInstance intfTaskInstance) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM INTF_TASK_INSTANCE A WHERE A.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (intfTaskInstance != null) {

			if (!StrUtil
					.isNullOrEmpty(intfTaskInstance.getIntfTaskInstanceId())) {
				sb.append(" AND A.INTF_TASK_INSTANCE_ID = ?");
				params.add(intfTaskInstance.getIntfTaskInstanceId());
			}

			if (!StrUtil.isNullOrEmpty(intfTaskInstance.getFtpTaskInstanceId())) {
				sb.append(" AND A.FTP_TASK_INSTANCE_ID = ?");
				params.add(intfTaskInstance.getFtpTaskInstanceId());
			}

			if (!StrUtil.isEmpty(intfTaskInstance.getTargetSystem())) {
				sb.append(" AND A.TARGET_SYSTEM = ?");
				params.add(intfTaskInstance.getTargetSystem());
			}

			if (!StrUtil.isEmpty(intfTaskInstance.getMsgType())) {
				sb.append(" AND A.MSG_TYPE = ?");
				params.add(intfTaskInstance.getMsgType());
			}

			if (!StrUtil.isNullOrEmpty(intfTaskInstance.getInvokeResule())) {
				sb.append(" AND A.INVOKE_RESULE = ?");
				params.add(intfTaskInstance.getInvokeResule());
			}

			if (!StrUtil.isNullOrEmpty(intfTaskInstance.getInvokeTimes())) {
				sb.append(" AND A.INVOKE_TIMES = ?");
				params.add(intfTaskInstance.getInvokeTimes());
			}
		}

		sb.append(" ORDER BY A.INTF_TASK_INSTANCE_ID");

		return this.intfTaskInstanceDao.jdbcFindList(sb.toString(), params,
				IntfTaskInstance.class);
	}

	@Override
	public void updateIntfTaskInstance(IntfTaskInstance intfTaskInstance) {
		if (intfTaskInstance != null
				&& intfTaskInstance.getIntfTaskInstanceId() != null) {
			intfTaskInstance.updateOnly();
		}
	}

	@Override
	public void addFtpInformIntfTaskInstance(String treeId, String syncType,
			String filePath, List<FtpTaskInstanceInfo> fileList, Date dataDate,
			Long ftpTaskInstanceId) {
		/**
		 * 通知系统列表
		 */
		List<BusinessSystem> businessSystemlist = systemOrgTreeConfigManager
				.queryBusinessSystemListByTreeId(Long.parseLong(treeId));
		if (businessSystemlist != null && businessSystemlist.size() > 0) {
			logger.info("2、树" + treeId + "下面挂的系统开始生成报文->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			for (BusinessSystem businessSystem : businessSystemlist) {
				logger.info("3、系统编码为：" + businessSystem.getSystemCode()
						+ "开始生成报文->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				IntfTaskInstance intfTaskInstance = new IntfTaskInstance();
				intfTaskInstance
						.setIntfCode(WsConstants.INTF_CODE_FTP_INFO_INFORM);
				intfTaskInstance.setInvokeResule(WsConstants.TASK_INIT);
				intfTaskInstance.setInvokeTimes(0L);
				intfTaskInstance.setMsgType(getFtpInformMsgType(businessSystem
						.getSystemCode()));
				intfTaskInstance.setFtpTaskInstanceId(ftpTaskInstanceId);
				intfTaskInstance
						.setTargetSystem(businessSystem.getSystemCode());
				cn.ffcs.uom.webservices.bean.ftpinform.Root root = new cn.ffcs.uom.webservices.bean.ftpinform.Root();
				MsgHead msgHead = RootBuilder.getMsgHead(
						getFtpInformMsgType(businessSystem.getSystemCode()),
						WsConstants.SYSTEM_CODE_UOM,
						businessSystem.getSystemCode(), new Date());
				cn.ffcs.uom.webservices.bean.ftpinform.MsgBody msgBody = new cn.ffcs.uom.webservices.bean.ftpinform.MsgBody();
				cn.ffcs.uom.webservices.bean.ftpinform.InParam inParam = new cn.ffcs.uom.webservices.bean.ftpinform.InParam();
				inParam.setFilePath(filePath);
				inParam.setSyncType(syncType);
				cn.ffcs.uom.webservices.bean.ftpinform.FileList fileListWs = new cn.ffcs.uom.webservices.bean.ftpinform.FileList();
				for (FtpTaskInstanceInfo ftpTaskInstanceInfo : fileList) {
					cn.ffcs.uom.webservices.bean.ftpinform.FileInfo fileInfo = new cn.ffcs.uom.webservices.bean.ftpinform.FileInfo();
					fileInfo.setFileName(ftpTaskInstanceInfo.getFileName());
					fileInfo.setDataCount(ftpTaskInstanceInfo.getDataCount()
							+ "");
					fileListWs.addFileInfo(fileInfo);
				}
				inParam.setFileList(fileListWs);
				inParam.setDataDate(DateUtil.dateToStr(dataDate,
						"yyyyMMddHH24mmss"));
				msgBody.setInParam(inParam);
				root.setMsgHead(msgHead);
				root.setMsgBody(msgBody);
				String requestContent = CastorParser.toXML(root);
				/**
				 * 删除命名空间
				 */
				requestContent = WsUtil.delNameSpace(requestContent);
				intfTaskInstance.setRequestContent(requestContent);
				intfTaskInstance.addOnly();
				logger.info("3、系统编码为：" + businessSystem.getSystemCode()
						+ "结束生成报文->"
						+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			logger.info("2、树" + treeId + "下面挂的系统结束生成报文->"
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
	}

	/**
	 * 
	 * @param systemCode
	 * @return
	 */
	private String getFtpInformMsgType(String systemCode) {
		SystemIntfInfoConfig systemIntfInfoConfig = systemIntfInfoConfigManager
				.querySystemIntfInfoConfigBySystemCode(systemCode);
		if (systemIntfInfoConfig != null) {
			return systemIntfInfoConfig.getMsgType();
		}
		return "";
	}

	/**
	 * 
	 */
	public int thresholdAlarm(Map<String, Object> mps) {
		StringBuilder sb = new StringBuilder()
				.append("SELECT COUNT(1) FROM INTF_TASK_INSTANCE T1,FTP_TASK_INSTANCE T2,")
				.append(" FTP_TASK_INSTANCE_INFO T3 WHERE T1.FTP_TASK_INSTANCE_ID=T2.FTP_TASK_INSTANCE_ID")
				.append(" AND T3.FTP_TASK_INSTANCE_ID=T2.FTP_TASK_INSTANCE_ID AND T1.EFF_DATE>(SELECT MIN(T.PRE_TIME) FROM ORG_TREE T)")
				.append(" AND T2.EFF_DATE>(SELECT MIN(T.PRE_TIME) FROM ORG_TREE T) ");

		Assert.notNull(mps);

		List<Object> params = new ArrayList<Object>();
		if (null != mps.get("thresholdValues")
				&& !"".equals(mps.get("thresholdValues"))) {
			sb.append(" AND T3.DATA_COUNT>?");
			params.add(mps.get("thresholdValues"));
		}

		if (null != mps.get("syncType") && !"".equals(mps.get("syncType"))) {
			sb.append(" AND T2.SYNC_TYPE<>?");
			params.add(mps.get("syncType"));
		}

		return this.intfTaskInstanceDao.jdbcGetSize(sb.toString(), params);
	}

	/**
	 * 修改AttrValue 中接口状态值
	 * 
	 * @param objs
	 * @author wongs
	 * @date 2014-8-25 下午3:05:11
	 * @comment
	 */
	public void interfaceStatus(Object[] objs) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ATTR_VALUE T1 SET T1.ATTR_VALUE=")
				.append(objs[0])
				.append(" WHERE EXISTS (")
				.append("SELECT * FROM ATTR_SPEC T2 WHERE T1.ATTR_ID=T2.ATTR_ID AND T2.Class_Id=6 AND T2.STATUS_CD=1000")
				.append(") AND T1.STATUS_CD=1000 AND T1.ATTR_ID IN(")
				.append(objs[1]).append(") AND T1.ATTR_VALUE=" + objs[2]);
		this.intfTaskInstanceDao.getJdbcTemplate().update(sb.toString());
	}
}
