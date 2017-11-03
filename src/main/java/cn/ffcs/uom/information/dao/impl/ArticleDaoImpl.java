/**
 * 
 */
package cn.ffcs.uom.information.dao.impl;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import cn.ffcs.uom.common.util.Constants;
import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.information.dao.ArticleDao;
import cn.ffcs.uom.information.util.InfoCenterConstants;
import cn.ffcs.uom.information.vo.ArticleVo;
import cn.ffcs.uom.information.vo.AttachmentVo;
import cn.ffcs.uom.information.vo.ImageVo;
import cn.ffcs.uom.information.vo.TargetOrgVo;
import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2012-10-18
 * 历史：
 * 2013-04-07，数据库orgid改为uuid
 * 2013-04-10，附件存储改用alfresco。标志：ALF
 */
public class ArticleDaoImpl implements ArticleDao {

	private JdbcTemplate jdbcTemplate;
	private LobHandler lobHandler;
	
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private Object[] createUpdate(ArticleVo record){
		String sql="";
		List<Object> params=new ArrayList<Object>();
		List<Object> clobParams=new ArrayList<Object>();
		if (DbUtil.isFieldAssigned(record.getCategoryCode())) {
			sql += ",category_code=?";
			params.add(record.getCategoryCode());
		}
		if (DbUtil.isFieldAssigned(record.getEffectiveDate())) {
			sql += ",effective_date=?";
			params.add(new Timestamp(record.getEffectiveDate()));
		}
		if (DbUtil.isFieldAssigned(record.getExpiredDate())) {
			sql += ",expired_date= ?";
			params.add(new Timestamp(record.getExpiredDate()));
		}
		if (DbUtil.isFieldAssigned(record.getReleaser())) {
			sql += ",releaser=?";
			params.add(record.getReleaser());
		}
		if (DbUtil.isFieldAssigned(record.getState())) {
			sql += ",state=?";
			params.add(record.getState());
		}
		if (DbUtil.isFieldAssigned(record.getTitle())) {
			sql += ",title=?";
			params.add( record.getTitle());
		}
		if(DbUtil.isFieldAssigned(record.getDisplayType())){
			sql+=" ,display_type=?";
			params.add(record.getDisplayType());
		}
		if(DbUtil.isFieldAssigned(record.getContent())){
			sql+=" ,content=?";
			clobParams.add(record.getContent());
		}
		
		if(sql.startsWith(","))
			sql=sql.substring(1);
		
		return new Object[] { sql, params,clobParams };
	}
	private Object[] createQuery(ArticleVo condition,String ... asName) {
		String sql = " 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (DbUtil.isFieldAssigned(condition.getCategoryCode())) {
			sql += " and category_code=?";
			params.add(condition.getCategoryCode());
		}
		if (DbUtil.isFieldAssigned(condition.getCurrentDate())) {
			sql += " and effective_date<=?";
			sql += " and trunc(expired_date + interval '1' day,'dd') > ?";
			params.add(new Timestamp(condition.getCurrentDate()));
			params.add(new Timestamp(condition.getCurrentDate()));
		}
		if (DbUtil.isFieldAssigned(condition.getBeginDate())) {
			sql+=" and ?<=creation_date";
			params.add(new Timestamp(condition.getBeginDate()));
		}
		if (DbUtil.isFieldAssigned(condition.getEndDate())) {
			sql += " and trunc(? + interval '1' day,'dd') > creation_date";
			params.add(new Timestamp(condition.getEndDate()));
		}
		if (DbUtil.isFieldAssigned(condition.getReleaser())) {
			sql += " and releaser=?";
			params.add(condition.getReleaser());
		}
		if (DbUtil.isFieldAssigned(condition.getState())) {
			sql += " and state=?";
			params.add(condition.getState());
		}
		if (DbUtil.isFieldAssigned(condition.getTitle())) {
			sql += " and title like ?";
			params.add("%" + condition.getTitle() + "%");
		}
		if(DbUtil.isFieldAssigned(condition.getCurrentOrgUuid())){
			sql+=" and ? in (select org_id from info_article_target_org where article_id ="+asName[0]+".article_id) ";
			params.add(condition.getCurrentOrgUuid());
			
			if(DbUtil.isFieldAssigned(condition.getCurrentType())){
				int type=0;
				if(Constants.GROUP_TYPE_PRIVATE.equals(condition.getCurrentType()))
					type=1;
				else if(Constants.GROUP_TYPE_PUBLIC.equals(condition.getCurrentType()))
					type=2;
				sql+=" and exists (select type from info_article_target_org iato where ?=iato.org_id " +
						"and iato.article_id="+asName[0]+".article_id   and (select bitand(?,iato.type) from dual)>0 )";
				params.add(condition.getCurrentOrgUuid());
				params.add(type);
			}
		}else if(condition.getUserOrgUuidList()!=null&&condition.getUserOrgUuidList().size()>0){
			List<String> ids=condition.getUserOrgUuidList();
			sql+=" and exists (select org_id from info_article_target_org iato"+
					" where iato.article_id ="+asName[0]+".article_id and iato.org_id in (";
			for(String id:ids){
				sql+="?,";
				params.add(id);
			}
			sql=sql.substring(0, sql.length()-1);
			sql+="))";
		}
		if (DbUtil.isFieldAssigned(condition.getDisplayType())) {
			if(InfoCenterConstants.DISPLAY_TYPE_NORMAL.equals(condition.getDisplayType())){
				sql += " and (display_type=? or display_type is null)";
			}else{
				sql+=" and display_type=?";
			}
			params.add(condition.getDisplayType());
		}
		return new Object[] { sql, params };
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] queryArticlesPaged(ArticleVo condition, int page, int size,String sort,String order) {
		String sql = "select * from info_article ia where state != 'deleted' and ";
		String extCol=",(select name from info_category a where a.code=src.category_code) category_text";
				
		List<Object> params;
		Object[] q = createQuery(condition,"ia");
		sql += q[0];
		params = (List<Object>) q[1];

		if(StrUtil.isEmpty(sort)){
			sql+=" order by creation_date desc";
		}else{
			sql+=" order by "+sort+" "+order;
		}
		
		Object[] r = DbUtil.queryPaged(jdbcTemplate, sql,extCol,params, new ArticleMapper(false),
				page, size);
		return r;
	}
	
	public Object[] queryArticlesPaged(ArticleVo condition, int page, int size) {
		return queryArticlesPaged(condition, page, size, "creation_date", "desc");
	}
	
	@Override
	public long addArticle(final ArticleVo obj) {
		obj.setCreationDate(new Date().getTime());
		obj.setModifDate(obj.getCreationDate());
		
		final long id = DbUtil.fetchNextSeq(jdbcTemplate, "seq_info_article_id");
		String sql="insert into info_article(article_id,title,content,releaser,category_code,state," +
				"effective_date,expired_date,creation_date,modif_date,display_type) " +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.execute(sql,new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
			@Override
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
					DataAccessException {
				ps.setObject(1,id);
				ps.setObject(2,obj.getTitle());
				lobCreator.setClobAsString(ps, 3, obj.getContent());
				ps.setObject(4,obj.getReleaser());
				ps.setObject(5,obj.getCategoryCode());
				ps.setObject(6,obj.getState());
				ps.setObject(7,DbUtil.toTimestamp(obj.getEffectiveDate()));
				ps.setObject(8,DbUtil.toTimestamp(obj.getExpiredDate()));
				ps.setObject(9,DbUtil.toTimestamp(obj.getCreationDate()));
				ps.setObject(10,DbUtil.toTimestamp(obj.getModifDate()));
				ps.setObject(11,obj.getDisplayType());
			}
		});
		
		//update relation table
		if(obj.getTargetOrgList()!=null&& obj.getTargetOrgList().size()>0)
			updateTargetOrgs(id,obj.getTargetOrgList());
		
		//ALF,MOD FOR UOM(de-alf)
		if(obj.getAttachmentList()!=null&&obj.getAttachmentList().size()>0)
			processAttachmentOperations(id,obj.getAttachmentList());
			//processAttachmentOperationsAlf(id,obj.getAttachmentList());
		
		if(obj.getImageList()!=null&&obj.getImageList().size()>0)
			processImageOperations(id,obj.getImageList());
		
		if(obj.getThumbnail()!=null){
			obj.setArticleId(id);
			ThumbnailVo t=obj.getThumbnail();
			if(t.getThumbnailId()!=0)
				bindThumbnail(obj,t);
			else
				deleteThumbnail(id);
		}
		
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateArticle(ArticleVo obj) {
		final long id=obj.getArticleId();
		Object[] u=createUpdate(obj);
		String setter=(String)u[0];
		final List<Object> params=(List<Object>)u[1];
		final List<Object> clobParams=(List<Object>)u[2];
		String sql="update info_article set modif_date=?,"+setter+" where article_id=?";
		
		jdbcTemplate.execute(sql,new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
			@Override
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
					DataAccessException {
				ps.setObject(1, DbUtil.toTimestamp(new Date().getTime()));
				int i=2;
				for(Object p:params)
					ps.setObject(i++, p);
				for(Object p:clobParams)
					lobCreator.setClobAsString(ps, i++, (String)p);
				ps.setObject(i++, id);
				ps.execute();
			}
		});
		
		//update relation table
		if(obj.getTargetOrgList()!=null&& obj.getTargetOrgList().size()>0)
			updateTargetOrgs(id,obj.getTargetOrgList());
		
		if(obj.getAttachmentList()!=null&&obj.getAttachmentList().size()>0)
			//ALF,MOD FOR UOM(de-alf)
			processAttachmentOperations(id,obj.getAttachmentList());
			//processAttachmentOperationsAlf(id,obj.getAttachmentList());
		
		if(obj.getImageList()!=null&&obj.getImageList().size()>0)
			processImageOperations(id,obj.getImageList());
		
		/*
		 * 附件、缩略图字段处理方式：
		 * 1.上传blob内容
		 * 2.新增/更新时：
		 * 	1.更新从表其他字段
		 * 	2.关联主表id
		 * 	3.将其他图片文章设置为普通（因为目前只支持1个图片新闻）TODO 今后采用选择机制
		 */
		if(obj.getThumbnail()!=null){
			ThumbnailVo t=obj.getThumbnail();
			if(t.getThumbnailId()!=0)
				bindThumbnail(obj,t);
			else
				deleteThumbnail(id);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deleteArticle(final long id) {
		String sql = "delete from info_article where article_id=?";
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
					DataAccessException {
				ps.setLong(1, id);
				return ps.execute();
			}
		});
		
		deleteTargetOrgs(id);
	}

	@Override
	public ArticleVo loadArticle(long id) {
		String sql = "select * from info_article where article_id=?";
		List<ArticleVo> list = jdbcTemplate.query(sql, new ArticleMapper(true), id);
		if (list.size() == 0)
			throw new RuntimeException("根据id找不到文章。");
		
		ArticleVo a=list.get(0);
		sql="select * from info_article_target_org  where article_id=?";
		List<TargetOrgVo> list2=jdbcTemplate.query(sql,new TargetOrgMapper(),id);
		a.setTargetOrgList(list2);
		
		sql="select * from info_attachment where article_id=?";
		List<AttachmentVo> list3=jdbcTemplate.query(sql, new AttachmentMapper(),id);
		a.setAttachmentList(list3);
		
		sql="select * from info_thumbnail where article_id=?";
		List<ThumbnailVo> list4=jdbcTemplate.query(sql, new ThumbnailMapper(),id);
		if(list4.size()>0)
			a.setThumbnail(list4.get(0));
		
		
		return a;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteTargetOrgs(final long articleId){
		String sql = "delete from info_article_target_org where article_id=?";
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
					DataAccessException {
				ps.setLong(1, articleId);
				return ps.execute();
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateTargetOrgs(final long articleId, List<TargetOrgVo> orgList) {
		deleteTargetOrgs(articleId);
		
		for(final TargetOrgVo org:orgList){
			String sql="insert into info_article_target_org(article_id,org_id,type) values(?,?,?)";
			jdbcTemplate.execute(sql,new PreparedStatementCallback() {
				@Override
				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
						DataAccessException {
					ps.setObject(1,articleId);
					ps.setObject(2,org.getOrgUuid());
					ps.setObject(3,org.getType());
					return ps.execute();
				}
			});
		}
	}
	/**
	 * 处理附件“动作清单”，实际上附件已经生成，这里做的是删除、和article的关联
	 * @author 曾臻
	 * @date 2013-1-6
	 * @param alist
	 */
//ALF,MOD FOR UOM(de-alf)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processAttachmentOperations(final long articleId,List<AttachmentVo> alist){
		for(final AttachmentVo a:alist){
			if("add".equals(a.getOperation())){
				String sql="update info_attachment set article_id=? where attachment_id=?";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, articleId);
						ps.setObject(2, a.getAttachmentId());
						return ps.execute();
					}
				});
			}else if("remove".equals(a.getOperation())){
				String sql="delete from info_attachment where attachment_id=?";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, a.getAttachmentId());
						return ps.execute();
					}
				});
			}
		}
	}
	
	/**
	 * 同上，不过是针对alfresco，注意：删除操作本关联表还必须另外删除alfresco里的文件
	 * @author 曾臻
	 * @date 2013-4-10
	 * @param articleId
	 * @param alist
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processAttachmentOperationsAlf(final long articleId,List<AttachmentVo> alist){
		for(final AttachmentVo a:alist){
			if("add".equals(a.getOperation())){
				final long id = DbUtil.fetchNextSeq(jdbcTemplate, "seq_info_attachment_id");
				String sql="insert into info_attachment(attachment_id,article_id,alf_id) " +
						"values( ?,?,?)";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, id);
						ps.setObject(2, articleId);
						ps.setObject(3, a.getAlfId());
						return ps.execute();
					}
				});
			}else if("remove".equals(a.getOperation())){
				String sql="delete from info_attachment where alf_id=?";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, a.getAlfId());
						return ps.execute();
					}
				});
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processImageOperations(final long articleId,List<ImageVo> alist){
		for(final ImageVo a:alist){
			if("add".equals(a.getOperation())){
				String sql="update info_image set article_id=? where image_id=?";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, articleId);
						ps.setObject(2, a.getImageId());
						return ps.execute();
					}
				});
			}else if("remove".equals(a.getOperation())){
				String sql="delete from info_image where image_id=?";
				jdbcTemplate.execute(sql,new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setObject(1, a.getImageId());
						return ps.execute();
					}
				});
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bindThumbnail(final ArticleVo a,final ThumbnailVo t){
		//更新前先删除旧的
		String sql = "delete from info_thumbnail where article_id =? and thumbnail_id != ?";
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
					DataAccessException {
				ps.setLong(1, a.getArticleId());
				ps.setLong(2, t.getThumbnailId());
				return ps.execute();
			}
		});
		
		//绑定已上传图片
		sql="update info_thumbnail set article_id=?,brief=? where thumbnail_id=?";
		jdbcTemplate.execute(sql,new PreparedStatementCallback() {
			@Override
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setObject(1, a.getArticleId());
				ps.setObject(2, t.getBrief());
				ps.setObject(3, t.getThumbnailId());
				return ps.execute();
			}
		});
		
		//将其他图片文章设置为普通（目前只支持同时显示一条图片信息）
		sql="update info_article set display_type=? where category_code=? and article_id!=? and display_type=?";
		jdbcTemplate.execute(sql,new PreparedStatementCallback() {
			@Override
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setObject(1, InfoCenterConstants.DISPLAY_TYPE_NORMAL);
				ps.setObject(2, a.getCategoryCode());
				ps.setObject(3, a.getArticleId());
				ps.setObject(4, InfoCenterConstants.DISPLAY_TYPE_IMAGE);
				return ps.execute();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteThumbnail(final long articleId){
		String sql = "delete from info_thumbnail where article_id =? ";
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
					DataAccessException {
				ps.setLong(1, articleId);
				return ps.execute();
			}
		});
	}
}

class ArticleMapper implements RowMapper<ArticleVo> {
	private boolean isLoadContent=false;
	public ArticleMapper(boolean loadContent) {
		isLoadContent=loadContent;
	}
	public ArticleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		ArticleVo vo = new ArticleVo();
		vo.setArticleId(rs.getLong("article_id"));
		vo.setCategoryCode(rs.getString("category_code"));
		if(DbUtil.isHaveColumn(rs,"category_text"))
			vo.setCategoryText(rs.getString("category_text"));
		vo.setState(rs.getString("state"));
		vo.setStateText(DbUtil.decode(vo.getState(), 
				InfoCenterConstants.ARTICLE_STATE_DRAFT,"草稿",
				InfoCenterConstants.ARTICLE_STATE_DELETED,"已删除",
				InfoCenterConstants.ARTICLE_STATE_RELEASED,"已发布"));
		vo.setEffectiveDate(DbUtil.toTimeInMillis(rs.getTimestamp("effective_date")));
		vo.setExpiredDate(DbUtil.toTimeInMillis(rs.getTimestamp("expired_date")));
		vo.setCreationDate(DbUtil.toTimeInMillis(rs.getTimestamp("creation_date")));
		vo.setModifDate(DbUtil.toTimeInMillis(rs.getTimestamp("modif_date")));
		vo.setTitle(rs.getString("title"));
		vo.setReleaser(rs.getString("releaser"));
		if(isLoadContent){
			Clob clob=rs.getClob("content");
			vo.setContent(clob.getSubString(1, (int)clob.length()));
		}
		vo.setDisplayType(rs.getString("display_type"));
		vo.setDisplayTypeText(DbUtil.decode(vo.getDisplayType(), 
				InfoCenterConstants.DISPLAY_TYPE_IMAGE,"图片",
				InfoCenterConstants.DISPLAY_TYPE_NORMAL,"列表",
				InfoCenterConstants.DISPLAY_TYPE_ROLLING,"滚动"));
		return vo;
	}
}

class TargetOrgMapper implements RowMapper<TargetOrgVo> {
	public TargetOrgVo mapRow(ResultSet rs, int rowNum) throws SQLException {
		TargetOrgVo vo=new TargetOrgVo();
		vo.setOrgUuid(rs.getString("org_id"));
		vo.setType(rs.getLong("type"));
		return vo;
	}
}
