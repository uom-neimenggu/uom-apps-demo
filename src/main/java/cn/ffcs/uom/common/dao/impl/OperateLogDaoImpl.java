/**
 * 
 */
package cn.ffcs.uom.common.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.dao.OperateLogDao;

/**
 * @author yahui
 * 
 */
@Repository("operateLogDao")
@Transactional
public class OperateLogDaoImpl extends BaseDaoImpl implements OperateLogDao {

	public String getSeqBatchNumber() {
		String sql = "SELECT SEQ_BATCH_NUMBER.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}
}
