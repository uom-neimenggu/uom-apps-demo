package cn.ffcs.uom.publishLog.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstance;
import cn.ffcs.uom.ftpsyncfile.model.FtpTaskInstanceInfo;
import cn.ffcs.uom.publishLog.dao.PublishQueryDao;
import cn.ffcs.uom.publishLog.manager.PublishQueryManager;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Service("publishQueryManager")
@Scope("prototype")
@SuppressWarnings("static-access")
public class PublishQueryManagerImpl implements PublishQueryManager {
	
	@Resource
	private PublishQueryDao publishQueryDao;
	
	@Override
	public List<NodeVo> queryTableName() {
		return publishQueryDao.queryTableName();
	}

	@Override
	public List<NodeVo> traversalOrgTree() {
		return publishQueryDao.traversalOrgTree();
	}

	@Override
	public List<NodeVo> queryBusinessSystem() {
		return publishQueryDao.queryBusinessSystem();
	}
	
	@Override
	public PageInfo queryBusinessSystemResults(Listbox businessListbox, int currentPage, int pageSize){
		return publishQueryDao.queryBusinessSystemResults(businessListbox, currentPage, pageSize);
	}
	
	
	/**
	 * FTP消息 重发验证
	 * @param syncType 同步类型
	 * @param ftpTaskInstanceId ftp任务实例ID
	 * @param intfTaskInstanceId 接口任务实例ID
	 * @return
	 */
	@Override
	public boolean msgResendCheck(String syncType,String ftpTaskInstanceId,String intfTaskInstanceId){
		 if(WsConstants.SYNC_PART.equals(syncType)){//增量消息重发
			if(UomClassProvider.isOpenSwitch(Constants.THRESHOLD_ALARM)){//增量阀值开
				if(!StrUtil.isEmpty(ftpTaskInstanceId)){//验证数据量
					FtpTaskInstance fti = new FtpTaskInstance();
					fti.setFtpTaskInstanceId(Long.valueOf(ftpTaskInstanceId));
					List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList = fti.getFtpTaskInstanceInfoList();
					if (ftpTaskInstanceInfoList != null && ftpTaskInstanceInfoList.size() > 0) {
						boolean thresholdAlarmSign = false;// 阀值告警控制标志
						String thresholdValues = UomClassProvider.getSystemConfig(Constants.THRESHOLD_VALUES);
						for (FtpTaskInstanceInfo ftpTaskInstanceInfo : ftpTaskInstanceInfoList) {
							if (ftpTaskInstanceInfo.getDataCount() > Long.parseLong(thresholdValues)) {// 增量下发数据量大于阀值
								thresholdAlarmSign = true;
								break;
							}
						}
						if (thresholdAlarmSign) { //数据量超过阀值
							ZkUtil.showError("数据量超过阀值，重发失败！", "提示信息");
						}else{ //数据量未超过阀值
							msgResendConfirm("确定要重新下发增量消息通知?","增量通知重发",intfTaskInstanceId);
						}
					}else{
						ZkUtil.showExclamation("无FTP文件内容！", "警告");
					}
				}else{
					ZkUtil.showExclamation("未找到FTP任务实例ID！", "警告");
				}
			}else{//增量阀值关
				msgResendConfirm("确定要重新下发增量消息通知?","增量通知重发",intfTaskInstanceId);
			}
		}else if(WsConstants.SYNC_ALL.equals(syncType)){//全量消息重发
			msgResendConfirm("确定要重新下发全量消息通知?","全量通知重发",intfTaskInstanceId);
		}else{
			ZkUtil.showError("同步类型异常！", "提示信息");
		}
		return true;
	}
	
	
	
	/**
	 * FTP消息 重发确认
	 * @return
	 */
	private void msgResendConfirm(final String confMsg,final String confTitle,final String intfTaskInstanceId){
		ZkUtil.showQuestion(confMsg, confTitle, new EventListener() {
		    @Override
		    public void onEvent(Event event) throws Exception {
		    	Integer result = (Integer) event.getData();
			    if (result == Messagebox.OK) {
			    	if(publishQueryDao.updateDataInvoke(intfTaskInstanceId)){
			    		ZkUtil.showError("正在重发，请稍后重新查询下发状态！", "提示信息");
			    	}else{
			    		ZkUtil.showError("重发失败！", "提示信息");
			    	}
			    }
		    }
		});
	}
}
