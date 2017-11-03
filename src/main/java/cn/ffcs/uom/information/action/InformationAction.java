/**
 * 
 */
package cn.ffcs.uom.information.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import cn.ffcs.uom.common.util.AjaxUtil;
import cn.ffcs.uom.common.util.OrgUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.information.dao.ArticleDao;
import cn.ffcs.uom.information.dao.AttachmentDao;
import cn.ffcs.uom.information.dao.CategoryDao;
import cn.ffcs.uom.information.dao.ImageDao;
import cn.ffcs.uom.information.dao.ThumbnailDao;
import cn.ffcs.uom.information.key.InfoKeys;
import cn.ffcs.uom.information.util.BeanUtil;
import cn.ffcs.uom.information.util.InfoCenterConstants;
import cn.ffcs.uom.information.vo.ArticleVo;
import cn.ffcs.uom.information.vo.BulletinSettingVo;
import cn.ffcs.uom.information.vo.CategoryVo;
import cn.ffcs.uom.information.vo.RollingItemVo;
import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2012-10-18
 *
 */
public class InformationAction extends HttpServlet{
	private static final long serialVersionUID = -1665293877442446622L;
	ArticleDao articleDaoImpl;
	CategoryDao categoryDaoImpl;
	AttachmentDao attachmentDaoImpl;
	ThumbnailDao thumbnailDaoImpl;
	ImageDao imageDaoImpl;
	//@Autowired
	//private RssService  rssService;

	public void setImageDaoImpl(ImageDao imageDaoImpl) {
		this.imageDaoImpl = imageDaoImpl;
	}
	public void setArticleDaoImpl(ArticleDao articleDaoImpl) {
		this.articleDaoImpl = articleDaoImpl;
	}
	public void setThumbnailDaoImpl(ThumbnailDao thumbnailDaoImpl) {
		this.thumbnailDaoImpl = thumbnailDaoImpl;
	}
	public void setCategoryDaoImpl(CategoryDao categoryDaoImpl) {
		this.categoryDaoImpl = categoryDaoImpl;
	}
	public void setAttachmentDaoImpl(AttachmentDao attachmentDaoImpl) {
		this.attachmentDaoImpl = attachmentDaoImpl;
	}

	/**
	 * 分页查询文章列表
	 * @author 曾臻
	 * @date 2012-11-22
	 * @param cond
	 * @param page
	 * @param size
	 * @return
	 */
	public Object queryArticle(ArticleVo cond,int page,int size,String sort,String order){
		try {
			if(StrUtil.isEmpty(sort)){
				sort="creation_date";
				order="desc";
			}
			BeanUtil.syncOrgId2Uuid(cond);//组织id数据库为uuid，这里做转换
			Object[] r=articleDaoImpl.queryArticlesPaged(cond, page, size,sort,order);
			BeanUtil.updateReleaserName((List<ArticleVo>)r[0]);
			return AjaxUtil.createReturnValueSuccess(r[0],r[1]);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 对公告栏查询文章（可用于查询滚动通知）
	 * @author 曾臻
	 * @date 2012-11-28
	 * @param cond
	 * @param page
	 * @param size
	 * @return
	 */
	public Object queryBulletinArticle(String catCode,String dispType,int page,int size){
		try {
			ArticleVo cond=new ArticleVo();
			cond.setCategoryCode(catCode);
			cond.setDisplayType(dispType);
			cond.setState(InfoCenterConstants.ARTICLE_STATE_RELEASED);
			cond.setCurrentDate(new Date().getTime());
			//TODO 暂时不限定组织可见性
			//cond.setCurrentOrgId(OrgUtil.getCurrentOrg().getOrgId());
			cond.setUserOrgIdList(OrgUtil.getCurrentUserOrgIds());
			cond.setCurrentType(OrgUtil.getCurrentGroupType());
			BeanUtil.syncOrgId2Uuid(cond);//组织id数据库为uuid，这里做转换
			Object[] r=articleDaoImpl.queryArticlesPaged(cond, page, size,"modif_date","desc");
			return AjaxUtil.createReturnValueSuccess(r[0],r[1]);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 查询指定公告栏下的滚动信息（标题连同内容一起返回）
	 * @author 曾臻
	 * @date 2013-2-2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object queryBulletinRollingList(String catCode,int maxlen,int counts){
		try {
			ArticleVo cond=new ArticleVo();
			cond.setCategoryCode(catCode);
			cond.setDisplayType(InfoCenterConstants.DISPLAY_TYPE_ROLLING);
			cond.setState(InfoCenterConstants.ARTICLE_STATE_RELEASED);
			cond.setCurrentDate(new Date().getTime());
			//TODO 暂时不限定组织可见性
			//cond.setCurrentOrgId(OrgUtil.getCurrentOrg().getOrgId());
			cond.setUserOrgIdList(OrgUtil.getCurrentUserOrgIds());
			cond.setCurrentType(OrgUtil.getCurrentGroupType());
			BeanUtil.syncOrgId2Uuid(cond);//组织id数据库为uuid，这里做转换
			Object[] r=articleDaoImpl.queryArticlesPaged(cond, 1, counts);
			
			List<ArticleVo> list=(List<ArticleVo>)r[0];
			List<RollingItemVo> result=new ArrayList<RollingItemVo>();
			for(ArticleVo vo:list){
//				RollingItemVo roll=new RollingItemVo();
//				roll.setArticleId(vo.getArticleId());
//				roll.setText(vo.getTitle());
//				roll.setType("title");
//				result.add(roll);
				
				RollingItemVo roll;
				String[] split;
				ArticleVo content=articleDaoImpl.loadArticle(vo.getArticleId());
				String limit="【"+content.getTitle()+"】 "+content.getContent();
				limit=StrUtil.html2text(limit);
				limit=StrUtil.filterRn(limit);
				if(limit.length()>500)
					limit=limit.substring(0,500)+"...";//限制为500
				split=StrUtil.split(limit,maxlen);
				for(String s:split){
						roll=new RollingItemVo();
						roll.setArticleId(vo.getArticleId());
						roll.setText(s);
						//roll.setType("content");
						result.add(roll);
				}
			}
			return AjaxUtil.createReturnValueSuccess(result);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 对公告栏查询图片新闻（目前数量为1）
	 * @author 曾臻
	 * @date 2013-1-17
	 * @param catCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object queryBulletinImageArticle(String catCode){
		try {
			ArticleVo cond=new ArticleVo();
			cond.setCategoryCode(catCode);
			cond.setDisplayType(InfoCenterConstants.DISPLAY_TYPE_IMAGE);
			cond.setState(InfoCenterConstants.ARTICLE_STATE_RELEASED);
			cond.setCurrentDate(new Date().getTime());
			//TODO 暂时不限定组织可见性
			//cond.setCurrentOrgId(OrgUtil.getCurrentOrg().getOrgId());
			cond.setUserOrgIdList(OrgUtil.getCurrentUserOrgIds());
			cond.setCurrentType(OrgUtil.getCurrentGroupType());
			BeanUtil.syncOrgId2Uuid(cond);//组织id数据库为uuid，这里做转换
			Object[] r=articleDaoImpl.queryArticlesPaged(cond, 1, 1);
			List<ArticleVo> list=(List<ArticleVo>)r[0];
			if(list.size()==0)
				return AjaxUtil.createReturnValueSuccess("notfound");
			ArticleVo a=list.get(0);
			ArticleVo a2=articleDaoImpl.loadArticle(a.getArticleId());
			return AjaxUtil.createReturnValueSuccess(a2);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 查询图片新闻（大图）
	 * @author 曾臻
	 * @date 2013-2-1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object queryBulletinImage(){
		try {
			ArticleVo cond=new ArticleVo();
			cond.setCategoryCode(InfoCenterConstants.CAT_IMAGE);
			cond.setState(InfoCenterConstants.ARTICLE_STATE_RELEASED);
			cond.setCurrentDate(new Date().getTime());
			//TODO 暂时不限定组织可见性
			//cond.setCurrentOrgId(OrgUtil.getCurrentOrg().getOrgId());
			cond.setUserOrgIdList(OrgUtil.getCurrentUserOrgIds());
			cond.setCurrentType(OrgUtil.getCurrentGroupType());
			BeanUtil.syncOrgId2Uuid(cond);//组织id数据库为uuid，这里做转换
			Object[] r=articleDaoImpl.queryArticlesPaged(cond, 1, 1,"modif_date","desc");
			List<ArticleVo> list=(List<ArticleVo>)r[0];
			if(list.size()==0)
				return AjaxUtil.createReturnValueSuccess("notfound");
			ArticleVo a=list.get(0);
			ArticleVo a2=articleDaoImpl.loadArticle(a.getArticleId());
			return AjaxUtil.createReturnValueSuccess(a2);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	
	public Object loadBulletinSetting(String catCode){
		try {
			BulletinSettingVo bsv=categoryDaoImpl.loadBulletinSetting(catCode);
			if(bsv==null){
				bsv=new BulletinSettingVo();
				bsv.setDisplayType(InfoCenterConstants.DISPLAY_TYPE_NORMAL);
			}
			return AjaxUtil.createReturnValueSuccess(bsv);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 发布新文章
	 * @author 曾臻
	 * @date 2012-11-22
	 * @param vo
	 * @return
	 */
	public Object addArticle(ArticleVo vo){
		try {
			vo.setReleaser(OrgUtil.getCurrentUser().getUserUuId());
			BeanUtil.makeImageOperations(vo);
			BeanUtil.syncOrgId2Uuid(vo);//组织id数据库为uuid，这里做转换
			long id=articleDaoImpl.addArticle(vo);
			BeanUtil.clearImageIds();
			processThumbnailAfterRelease(vo);
			return AjaxUtil.createReturnValueSuccess(id);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	};
	/**
	 * 修改文章
	 * @author 曾臻
	 * @date 2012-11-22
	 * @param vo
	 * @return
	 */
	public Object updateArticle(ArticleVo vo){
		try {
			//ALF,MOD FOR UOM(de-alf)
			//processAlfrescoAttachments(vo);
			BeanUtil.makeImageOperations(vo);
			BeanUtil.syncOrgId2Uuid(vo);//组织id数据库为uuid，这里做转换
			articleDaoImpl.updateArticle(vo);
			BeanUtil.clearImageIds();
			processThumbnailAfterRelease(vo);
			return AjaxUtil.createReturnValueSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	};
	/**
	 * 修改文章状态
	 * @author 曾臻
	 * @date 2012-11-22
	 * @param id
	 * @param state
	 * @return
	 */
	public Object updateArticleState(long id,String state){
		try {
			ArticleVo av=new ArticleVo();
			av.setArticleId(id);
			av.setState(state);
			articleDaoImpl.updateArticle(av);
			return AjaxUtil.createReturnValueSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	};
	/**
	 * 批量修改文章状态
	 * TODO 考虑批量事务（+service层 或 action层事务）
	 * @author 曾臻
	 * @date 2013-2-2
	 * @param ids
	 * @param state
	 * @return
	 */
	public Object updateArticleStateBatch(long[] ids,String state){
		try{
			for(long id:ids){
				ArticleVo av=new ArticleVo();
				av.setArticleId(id);
				av.setState(state);
				articleDaoImpl.updateArticle(av);
			}
			return AjaxUtil.createReturnValueSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 装载文章
	 * @author 曾臻
	 * @date 2012-11-22
	 * @param id
	 * @return
	 */
	public Object loadArticle(long id){
		try {
			ArticleVo av=articleDaoImpl.loadArticle(id);
			BeanUtil.syncOrgUuid2Id(av);//组织id数据库为uuid，这里做转换
			BeanUtil.updateReleaserName(av);
			
			//update buffer
			Map<Long,String> ids=BeanUtil.getImageIds(AjaxUtil.getHttpSession());
			Set<Long> ids2=BeanUtil.parseImageIdsThroughArticleContent(av.getContent());
			for(Long id2:ids2)
				ids.put(id2,null);
			
			return AjaxUtil.createReturnValueSuccess(av);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	
	public Object queryCategoryList(){
		try {
			List<CategoryVo> list=categoryDaoImpl.queryCategoryList();
			return AjaxUtil.createReturnValueSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	
	/**
	 * 清除所有没有和文章关联的临时附件
	 * @author 曾臻
	 * @date 2013-1-9
	 * @return
	 */
	public Object deleteAllTempAttachments(){
		//TODO
		return null;
	}
	
	public Object deleteTempAttachments(List<Long> ids){
		try {
			attachmentDaoImpl.deleteAttchments(ids);
			return AjaxUtil.createReturnValueSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxUtil.createReturnValueError();
		}
	}
	
	/**
	 * 发布后的缩略图处理（id缓存、数据库）
	 * @author 曾臻
	 * @date 2013-1-16
	 * @param a
	 */
	private void processThumbnailAfterRelease(ArticleVo a){
		/*
		 * 						发布前的id值
		 * 场景					buff.id		a.id	动作
		 * 发布，新建，发布		null		null	无
		 * 发布，上传，发布		A			A		清缓存
		 * 发布，编辑，发布		null		A		无
		 * 上传，置空，发布		A			0		清缓存，删数据
		 * 上传，重置，发布		A			B		清缓存，删数据
		 * 上传，新建，发布		A			null	清缓存，删数据		
		 */
		HttpSession ss=AjaxUtil.getHttpSession();
		Long id=(Long)ss.getAttribute(InfoKeys.PREV_THUMBNAIL_ID);
		if(id!=null){
			ss.removeAttribute(InfoKeys.PREV_THUMBNAIL_ID);
			ThumbnailVo t=a.getThumbnail();
			if(t==null||!id.equals(t.getThumbnailId())){
				List<Long> ids=new ArrayList<Long>();
				ids.add(id);
				thumbnailDaoImpl.deleteThumbnails(ids);
			}
		}
	}
	/**
	 * 处理alfresco附件。主要是删除operation为remove的附件。updateArticle时用到。
	 * @author 曾臻
	 * @date 2013-4-10
	 * @param a
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	//MOD FOR UOM(de-alf)
//	private void processAlfrescoAttachments(ArticleVo a) throws ClientProtocolException, IOException{
//		List<String> ids=new ArrayList<String>();
//		for(AttachmentVo av:a.getAttachmentList()){
//			if("remove".equals(av.getOperation()))
//				ids.add(av.getAlfId());
//		}
//		String json=JsonUtil.objectArray2Json(ids);
//		String result=AlfrescoAttachmentUtil.invoke("deleteFiles",json);
//		if(result.startsWith("error")){
//			String []arr=result.split(":");
//			throw new RuntimeException(arr[1]);
//		}
//	}
}
