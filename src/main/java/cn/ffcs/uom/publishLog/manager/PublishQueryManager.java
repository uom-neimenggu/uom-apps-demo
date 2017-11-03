package cn.ffcs.uom.publishLog.manager;

import java.util.List;

import org.zkoss.zul.Listbox;

import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

public interface PublishQueryManager {

	/**
	 * 查询 表名
	 * @return
	 */
	public List<NodeVo> queryTableName();

	public List<NodeVo> traversalOrgTree();
	
	public List<NodeVo> queryBusinessSystem();
	
	public PageInfo queryBusinessSystemResults(Listbox businessListbox, int currentPage, int pageSize);
	
	
	/**
	 * FTP消息 重发验证
	 * @param syncType 同步类型
	 * @param ftpTaskInstanceId ftp任务实例ID
	 * @param intfTaskInstanceId 接口任务实例ID
	 * @return
	 */
	public boolean msgResendCheck(String syncType,String ftpTaskInstanceId,String intfTaskInstanceId);
}
