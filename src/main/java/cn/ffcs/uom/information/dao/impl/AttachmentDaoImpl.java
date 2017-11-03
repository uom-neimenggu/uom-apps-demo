/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.information.dao.AttachmentDao;
import cn.ffcs.uom.information.vo.AttachmentVo;

/**
 * @author 曾臻
 * @date 2013-01-06
 */
public class AttachmentDaoImpl implements AttachmentDao {

	private JdbcTemplate jdbcTemplate;
	private LobHandler lobHandler;
	
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * @author 曾臻
	 * @date 2013-1-6
	 * @param a
	 * @param os
	 * @return
	 */
	@Override
	public long addAttachment(final AttachmentVo a,final InputStream is) {
		final long id = DbUtil.fetchNextSeq(jdbcTemplate, "seq_info_attachment_id");
		String sql="insert into info_attachment(attachment_id,content,name,length,creation_date)" +
				" values(?,?,?,?,sysdate)";
		jdbcTemplate.execute(sql,new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
			@Override
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
					DataAccessException {
				ps.setObject(1, id);
				lobCreator.setBlobAsBinaryStream(ps, 2, is, a.getLength());
				ps.setObject(3, a.getName());
				ps.setObject(4, a.getLength());
			}
		});
		return id;
	}
	
	/**
	 * @author 曾臻
	 * @date 2013-1-9
	 * @param ids
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void deleteAttchments(List<Long> ids) {
		for(final long id:ids){
			String sql = "delete from info_attachment where attachment_id=?";
			jdbcTemplate.execute(sql, new PreparedStatementCallback() {
				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
						DataAccessException {
					ps.setLong(1, id);
					return ps.execute();
				}
			});
		}
	}
	
	/**
	 * @author 曾臻
	 * @date 2013-1-10
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void loadAttachmentContent(long id,final OutputStream os) {
		String sql="select * from info_attachment where attachment_id=?";
		jdbcTemplate.query(sql,new Object[]{id},new AbstractLobStreamingResultSetExtractor() {
			@Override
			protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
				InputStream is=lobHandler.getBlobAsBinaryStream(rs, "content");
				IOUtils.copy(is, os);
				is.close();
			}
		});
	}
	
	/**
	 * @author 曾臻
	 * @date 2013-1-10
	 * @param id
	 * @return
	 */
	@Override
	public AttachmentVo loadAttachment(long id) {
		String sql="select * from info_attachment where attachment_id=?";
		List<AttachmentVo> list=jdbcTemplate.query(sql, new AttachmentMapper(),id);
		if(list.size()==0)
			throw new RuntimeException("根据id找不到附件。");
		return list.get(0);
	}
}
