/**
 * 
 */
package cn.ffcs.uom.publishLog.dao;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.dao.BaseDao;

/**
 * @author wenyaopeng
 *
 */
public interface PublishDetectionDao extends BaseDao {

	/**
	 * 查询下发范围
	 * @param arg
	 * @return
	 */
	public List queryPublishRange(Map arg);
	
}
