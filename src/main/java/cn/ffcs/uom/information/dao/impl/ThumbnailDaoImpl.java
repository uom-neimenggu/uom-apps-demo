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
import cn.ffcs.uom.information.dao.ThumbnailDao;
import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2013-01-15
 */
public class ThumbnailDaoImpl implements ThumbnailDao {

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
	 * @date 2013-1-15
	 * @param a
	 * @param os
	 * @return
	 */
	@Override
	public long addThumbnail(final ThumbnailVo i,final InputStream is) {
		final long id = DbUtil.fetchNextSeq(jdbcTemplate, "seq_info_thumbnail_id");
		String sql="insert into info_thumbnail(thumbnail_id,content,length)" +
				" values(?,?,?)";
		jdbcTemplate.execute(sql,new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
			@Override
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
					DataAccessException {
				ps.setObject(1, id);
				lobCreator.setBlobAsBinaryStream(ps, 2, is, i.getLength());
				ps.setObject(3, i.getLength());
			}
		});
		return id;
	}
	
	/**
	 * @author 曾臻
	 * @date 2013-1-15
	 * @param ids
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void deleteThumbnails(List<Long> ids) {
		for(final long id:ids){
			String sql = "delete from info_thumbnail where thumbnail_id=?";
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
	 * @date 2013-1-15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void loadThumbnailContent(long id,final OutputStream os) {
		String sql="select * from info_thumbnail where thumbnail_id=?";
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
	 * @date 2013-1-15
	 * @param id
	 * @return
	 */
	@Override
	public ThumbnailVo loadThumbnail(long id) {
		String sql="select * from info_thumbnail where thumbnail_id=?";
		List<ThumbnailVo> list=jdbcTemplate.query(sql, new ThumbnailMapper(),id);
		if(list.size()==0)
			throw new RuntimeException("根据id找不到缩略图。");
		return list.get(0);
	}
}
