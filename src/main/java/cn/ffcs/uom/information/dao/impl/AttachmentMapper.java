/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.information.vo.AttachmentVo;

/**
 * @author 曾臻
 * @date 2013-1-10
 *
 */
public class AttachmentMapper implements RowMapper<AttachmentVo> {
	public AttachmentVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		AttachmentVo vo=new AttachmentVo();
		vo.setAttachmentId(rs.getLong("attachment_id"));
		vo.setCreationDate(DbUtil.toTimeInMillis(rs.getTimestamp("creation_date")));
		vo.setLength(rs.getInt("length"));
		vo.setName(rs.getString("name"));
		return vo;
	}
}