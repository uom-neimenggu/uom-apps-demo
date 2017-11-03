package cn.ffcs.uom.publishLog.dao;

import java.util.List;

import org.zkoss.zul.Listbox;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

public interface PublishQueryDao extends BaseDao {

	
	/**
	 * 查询 表名
	 * @return
	 */
	public List<NodeVo> queryTableName();

	public List<NodeVo> traversalOrgTree();
	
	public List<NodeVo> queryBusinessSystem();
	
	public PageInfo queryBusinessSystemResults(Listbox businessListbox, int currentPage, int pageSize);
	
	/**
	 * 修改数据结果（ 下发结果和下发次数）
	 * @param intfTaskInstanceId
	 * @return
	 */
	public boolean updateDataInvoke(String intfTaskInstanceId);
}
