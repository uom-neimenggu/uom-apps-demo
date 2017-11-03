/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.ffcs.uom.information.vo.ImageVo;

/**
 * @author 曾臻
 * @date 2013-1-14
 *
 */
public class ImageMapper implements RowMapper<ImageVo> {
	public ImageVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		ImageVo vo=new ImageVo();
		vo.setImageId(rs.getLong("image_id"));
		vo.setLength(rs.getInt("length"));
		vo.setType(rs.getString("type"));
		return vo;
	}
}