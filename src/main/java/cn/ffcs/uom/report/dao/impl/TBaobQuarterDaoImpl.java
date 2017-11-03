package cn.ffcs.uom.report.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TBaobQuarterDao;
import cn.ffcs.uom.report.model.TBaobQuarter;

/**
 * 报表管理
 * 
 * @版权：福富软件 版权所有 (c) 2017
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-5-10
 * @功能说明：
 * 
 */
@Repository("tBaobQuarterDao")
public class TBaobQuarterDaoImpl extends BaseDaoImpl implements TBaobQuarterDao {

	@Override
	public PageInfo queryTBaobQuarterPageInfo(TBaobQuarter tBaobQuarter,
			int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		String str = "select t.id,to_char(t.日期,'yyyy-mm') report_date,t.区域分布 c1,t.本期人数合计 c2,t.本期合同制 c3,"
				+ "t.本期派遣制 c4,t.本期业务外包 c5,t.本期个体代办 c6,t.本期代理商人员 c7,t.本期供应商人员 c8,"
				+ "t.本期包区自聘 c9,t.本期其他 c10,t.上期人数合计 c11,t.上期合同制 c12,t.上期派遣制 c13,"
				+ "t.上期业务外包 c14,t.上期个体代办 c15,t.上期代理商人员 c16,t.上期供应商人员 c17,"
				+ "t.上期包区自聘 c18,t.上期其他 c19,t.增减人数 c20,t.增减比例 c21 from t_baob_quarter t "
				+ "where to_char(t.日期,'yyyy-mm') = ?";
		params.add(tBaobQuarter.getReportDate());
		
		return super.jdbcFindPageInfo(str, params, currentPage,
				pageSize, TBaobQuarter.class);
	}

	@Override
	public List<Map<String, Object>> queryTBaobQuarterList(
			TBaobQuarter tBaobQuarter) {
		List<Object> params = new ArrayList<Object>();
		String str = "select to_char(t.日期,'yyyy-mm') 日期,t.区域分布,t.本期人数合计,t.本期合同制,"
				+ "t.本期派遣制,t.本期业务外包,t.本期个体代办,t.本期代理商人员,t.本期供应商人员,"
				+ "t.本期包区自聘,t.本期其他,t.上期人数合计,t.上期合同制,t.上期派遣制,"
				+ "t.上期业务外包,t.上期个体代办,t.上期代理商人员,t.上期供应商人员,"
				+ "t.上期包区自聘,t.上期其他,t.增减人数,t.增减比例   from t_baob_quarter t "
				+ "where to_char(t.日期,'yyyy-mm') = ?";
		params.add(tBaobQuarter.getReportDate());
		
		return this.getJdbcTemplate().queryForList(str, params.toArray());
	}
	
}
