package cn.ffcs.uom.common.manager;

import java.util.List;

import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

public interface OperateLogManager {

	/**
	 * 方法功能: 获取OrgTree属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	public List<NodeVo> getValuesList();
	
	   /**
     * 方法功能: 获取OrgTree属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
     * 
     */
    public List<NodeVo> getValuesListDw();

	/**
	 * 分页取类信息
	 * 
	 * @param orgTree
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgTree(OperateLog operateLog,
			int currentPage, int pageSize);

	/**
	 * 
	 * @param operateLog
	 * @return
	 */
	public List<OperateLog> queryOperateLogList(OperateLog operateLog);

	/**
	 * 删除记录
	 * 
	 * @param operateLog
	 */
	public void removeOperateLog(OperateLog operateLog);

	/**
	 * 更新记录
	 * 
	 * @param operateLog
	 */
	public void updateOperateLog(OperateLog operateLog);

	/**
	 * 保存记录
	 * 
	 * @param operateLog
	 */
	public void addOperateLog(OperateLog operateLog);

	public void saveOperateLog(OperateLog operateLog, boolean isAdd);

	public OperateLog getById(Long id);

	public String getSeqBatchNumber();

	public List<OperateLog> queryMaxOperateLogList(OperateLog operateLog);

}
