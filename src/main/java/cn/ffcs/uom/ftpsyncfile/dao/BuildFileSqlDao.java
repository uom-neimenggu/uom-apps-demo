package cn.ffcs.uom.ftpsyncfile.dao;

import cn.ffcs.uom.common.dao.BaseDao;

public interface BuildFileSqlDao extends BaseDao {
	/**
	 * 生成中间表
	 * 
	 * @param treeId
	 * @param lastDate
	 * @param thisDate
	 */
	public void createSyncTempTable(Long treeId, String lastDate,
			String thisDate);
}
