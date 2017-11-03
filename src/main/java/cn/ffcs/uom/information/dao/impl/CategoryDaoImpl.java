/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.ffcs.uom.information.dao.CategoryDao;
import cn.ffcs.uom.information.vo.BulletinSettingVo;
import cn.ffcs.uom.information.vo.CategoryVo;

/**
 * @author 曾臻
 * @date 2012-10-18
 * 
 */
public class CategoryDaoImpl implements CategoryDao {

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * @author 曾臻
	 * @date 2012-11-26
	 * @return
	 */
	@Override
	public List<CategoryVo> queryCategoryList() {
		String sql="select * from info_category";
		List<CategoryVo> list=jdbcTemplate.query(sql, new CategoryMapper());
		return list;
	}
	/**
	 * @author 曾臻
	 * @date 2013-1-17
	 * @param categoryCode
	 * @return
	 */
	@Override
	public BulletinSettingVo loadBulletinSetting(String categoryCode) {
		String sql="select * from info_setting_bulletin where category_code=?";
		List<BulletinSettingVo> list=jdbcTemplate.query(sql, new BulletinSettingMapper(),categoryCode);
		if(list.size()==0)
			return null;
		return list.get(0);
	}
}

class CategoryMapper implements RowMapper<CategoryVo> {
	public CategoryVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		CategoryVo vo = new CategoryVo();
		vo.setCode(rs.getString("code"));
		vo.setName(rs.getString("name"));
		return vo;
	}
}
class BulletinSettingMapper implements RowMapper<BulletinSettingVo> {
	public BulletinSettingVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		BulletinSettingVo vo = new BulletinSettingVo();
		vo.setCategoryCode(rs.getString("category_code"));
		vo.setDisplayType(rs.getString("display_type"));
		return vo;
	}
}