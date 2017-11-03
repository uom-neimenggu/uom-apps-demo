package cn.ffcs.uom.report.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.dao.TBaobUnitDao;
import cn.ffcs.uom.report.model.TBaobUnit;

/**
 * 参与人的管理 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-28
 * @功能说明：
 * 
 */
@Repository("tBaobUnitDao")
public class TBaobUnitDaoImpl extends BaseDaoImpl implements TBaobUnitDao {

	@Override
	public PageInfo queryTBaobUnitPageInfo(TBaobUnit tBaobUnit,
			int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		String str = "select t.id,to_char(t.日期,'yyyy-mm') report_date,t.单位 c1,t.本期人数合计 c2,t.上期人数合计 c3,"
				+ "t.总增减人数 c4,t.总增减比例 c5,t.本期合同制 c6,t.上期合同制 c7,"
				+ "t.合同制增减人数 c8,t.合同制增减比例 c9,t.本期派遣制 c10,t.上期派遣制 c11,"
				+ "t.派遣制增减人数 c12,t.派遣制增减比例 c13,t.本期业务外包 c14,t.上期业务外包 c15,"
				+ "t.业务外包增减人数 c16,t.业务外包增减比例 c17,t.本期个体代办 c18,t.上期个体代办 c19,"
				+ "t.个体代办增减人数 c20,t.个体代办增减比例 c21,t.本期代理商人员 c22,t.上期代理商人员 c23,"
				+ "t.代理商人员增减人数 c24,t.代理商人员增减比例 c25,t.本期供应商人员 c26,t.上期供应商人员 c27,"
				+ "t.供应商人员增减人数 c28,t.供应商人员增减比例 c29,t.本期包区自聘 c30,t.上期包区自聘 c31,"
				+ "t.包区自聘增减人数 c32,t.包区自聘增减比例 c33,t.本期其他 c34,t.上期其他 c35,t.其他增减人数 c36,t.其他增减比例 c37 "
				+ " from t_baob_unit t where to_char(t.日期,'yyyy-mm') = ?";
		params.add(tBaobUnit.getReportDate());
		
		return super.jdbcFindPageInfo(str, params, currentPage,
				pageSize, TBaobUnit.class);
	}

	@Override
	public List<Map<String, Object>> queryTBaobUnitList(TBaobUnit tBaobUnit) {
		List<Object> params = new ArrayList<Object>();
		String str = "select to_char(t.日期,'yyyy-mm') 日期,t.单位,t.本期人数合计,t.上期人数合计 ,"
				+ "t.总增减人数,t.总增减比例,t.本期合同制,t.上期合同制,"
				+ "t.合同制增减人数,t.合同制增减比例,t.本期派遣制,t.上期派遣制,"
				+ "t.派遣制增减人数,t.派遣制增减比例,t.本期业务外包,t.上期业务外包,"
				+ "t.业务外包增减人数,t.业务外包增减比例,t.本期个体代办,t.上期个体代办,"
				+ "t.个体代办增减人数,t.个体代办增减比例,t.本期代理商人员,t.上期代理商人员,"
				+ "t.代理商人员增减人数,t.代理商人员增减比例,t.本期供应商人员,t.上期供应商人员,"
				+ "t.供应商人员增减人数,t.供应商人员增减比例,t.本期包区自聘,t.上期包区自聘,"
				+ "t.包区自聘增减人数,t.包区自聘增减比例,t.本期其他,t.上期其他,t.其他增减人数,t.其他增减比例  "
				+ " from t_baob_unit t where to_char(t.日期,'yyyy-mm') = ?";
		params.add(tBaobUnit.getReportDate());
		
		return this.getJdbcTemplate().queryForList(str,params.toArray());
	}
	
}
