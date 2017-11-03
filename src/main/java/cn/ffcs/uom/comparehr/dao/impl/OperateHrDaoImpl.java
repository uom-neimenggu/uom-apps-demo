package cn.ffcs.uom.comparehr.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.comparehr.dao.OperateHrDao;
import cn.ffcs.uom.comparehr.model.OperateHr;

/**
 * 人力中间表管理
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-8-9
 * @功能说明：
 *
 */
@Repository("operateHrDao")
public class OperateHrDaoImpl extends BaseDaoImpl implements OperateHrDao {

	@SuppressWarnings("unchecked")
	public OperateHr queryOperateHrByCertNum(String certNum){
		List<Object> params = new ArrayList<Object>();
		String sql = "FROM OperateHr P WHERE P.lastCertNum = ?";
		params.add(certNum);
		List<OperateHr> listOperateHr = findListByHQLAndParams(sql, params);
		if (null != listOperateHr && listOperateHr.size() > 0) {
			return listOperateHr.get(0);
		}
		return null;
	}

}
