/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2013-1-15
 *
 */
public class ThumbnailMapper implements RowMapper<ThumbnailVo> {
	public ThumbnailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		ThumbnailVo vo=new ThumbnailVo();
		vo.setThumbnailId(rs.getLong("thumbnail_id"));
		vo.setLength(rs.getInt("length"));
		vo.setBrief(rs.getString("brief"));
		return vo;
	}
}