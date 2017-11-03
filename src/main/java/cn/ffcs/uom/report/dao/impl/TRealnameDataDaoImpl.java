package cn.ffcs.uom.report.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TRealnameDataDao;
import cn.ffcs.uom.report.model.TBaobUnit;
import cn.ffcs.uom.report.model.TRealnameData;

/**
 * HR实名制数据
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-28
 * @功能说明：
 * 
 */
@Repository("tRealnameDataDao")
public class TRealnameDataDaoImpl extends BaseDaoImpl implements TRealnameDataDao {

	@Override
	public PageInfo queryTRealnameDataPageInfo(TRealnameData tRealnameData,
			int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		String str = "select t.id,t.hrname,t.有效已实名数量 c1,t.有效未实名数量 c2,"
				+ "t.失效已实名数量 c3,t.失效未实名数量 c4,to_char(t.create_date,'yyyy-mm') create_date "
				+ " from t_realname_data t where to_char(t.create_date,'yyyy-mm') = ?";
		params.add(tRealnameData.getCreateDate());
		
		return super.jdbcFindPageInfo(str, params, currentPage,
				pageSize, TRealnameData.class);
	}

	@Override
	public List<Map<String, Object>> queryTRealnameDataList(TRealnameData tRealnameData) {
		List<Object> params = new ArrayList<Object>();
		String str = "select to_char(t.create_date,'yyyy-mm') 日期 ,t.hrname 单位,t.有效已实名数量,t.有效未实名数量,"
				+ "t.失效已实名数量,t.失效未实名数量"
				+ " from t_realname_data t where to_char(t.create_date,'yyyy-mm') = ?";
		params.add(tRealnameData.getCreateDate());
		
		return this.getJdbcTemplate().queryForList(str,params.toArray());
	}
	
}
