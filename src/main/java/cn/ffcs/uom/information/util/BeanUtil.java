/**
 * 
 */
package cn.ffcs.uom.information.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Organization;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.raptornuke.portal.service.OrganizationLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.UserLocalServiceUtil;
import cn.ffcs.uom.common.util.AjaxUtil;
import cn.ffcs.uom.information.key.InfoKeys;
import cn.ffcs.uom.information.vo.ArticleVo;
import cn.ffcs.uom.information.vo.ImageVo;
import cn.ffcs.uom.information.vo.TargetOrgVo;

/**
 * @author 曾臻
 * @date 2012-12-15
 * 
 */
public class BeanUtil {
	public static void updateReleaserName(List<ArticleVo> list) {
		for (ArticleVo a : list) {
			try {
				User user = UserLocalServiceUtil.getUserByUuid(a.getReleaser());
				a.setReleaserText(user.getFirstName() + user.getLastName());
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateReleaserName(ArticleVo a) {
		try {
			User user = UserLocalServiceUtil.getUserByUuid(a.getReleaser());
			a.setReleaserText(user.getFirstName() + user.getLastName());
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 用于发布时对比最终标识集，以得出需要清除的数据库临时图片。
	 * @author 曾臻
	 * @date 2013-1-14
	 * @param oldIds
	 * @return
	 */
	public static List<Long> computeImageIdsToRemove(Map<Long,String> oldIds,Set<Long> newIds){
		List<Long> result=new ArrayList<Long>();
		for(Long id:oldIds.keySet())
			if(!newIds.contains(id))
				result.add(id);
		return result;
	}
	/**
	 * 比较得出需要添加关联的图片
	 * @author 曾臻
	 * @date 2013-1-14
	 * @param oldIds
	 * @param newIds
	 * @return
	 */
	public static List<Long> computeImageIdsToAdd(Map<Long,String> oldIds,Set<Long> newIds){
		List<Long> result=new ArrayList<Long>();
		for(Long id:oldIds.keySet())
			if("add".equals(oldIds.get(id))&&newIds.contains(id))
				result.add(id);
		return result;
	}
	
	/**
	 * 获取缓存中的标识集
	 * @author 曾臻
	 * @date 2013-1-14
	 * @param ss
	 * @return
	 */
	public static Map<Long,String> getImageIds(HttpSession ss){
		@SuppressWarnings("unchecked")
		Map<Long,String> imgIds=(HashMap<Long,String>)ss.getAttribute(InfoKeys.UPLOADED_AND_LOADED_IMG_IDS);
		if(imgIds==null){
			imgIds=new HashMap<Long,String>();
			ss.setAttribute(InfoKeys.UPLOADED_AND_LOADED_IMG_IDS, imgIds);
		}
		return imgIds;
	}
	
	/**
	 * 根据文章内容获取发布时实际提交的图片标识
	 * @author 曾臻
	 * @date 2013-1-14
	 * @param content
	 * @return
	 */
	public static Set<Long> parseImageIdsThroughArticleContent(String content){
		Set<Long> ids=new HashSet<Long>();
		//XXX mod for uom
		Pattern p=Pattern.compile("<img\\ssrc=\"/uom\\-apps/informationImageDownload.action\\?id=(\\d+)\"");
		//Pattern p=Pattern.compile("<img\\ssrc=\"/uniportal\\-plugins/informationImageDownload.action\\?id=(\\d+)\"");
		Matcher m=p.matcher(content);
		while(m.find()){
			String id=m.group(1);
			ids.add(Long.valueOf(id));
		}
		return ids;
	}
	/**
	 * 根据前后图片标识集对比得出需要新增关联/删除关联的图片标识列表
	 * @author 曾臻
	 * @date 2013-1-14
	 * @param av
	 */
	public static void makeImageOperations(ArticleVo av){
		HttpSession ss=AjaxUtil.getHttpSession();
		Map<Long,String> ids=BeanUtil.getImageIds(ss);
		Set<Long> ids2=BeanUtil.parseImageIdsThroughArticleContent(av.getContent());
		List<Long> ids3=BeanUtil.computeImageIdsToRemove(ids, ids2);
		List<Long> ids4=BeanUtil.computeImageIdsToAdd(ids, ids2);
		
		List<ImageVo> voList=new ArrayList<ImageVo>();
		for(Long id:ids3){
			ImageVo i=new ImageVo();
			i.setImageId(id);
			i.setOperation("remove");
			voList.add(i);
		}
		for(Long id:ids4){
			ImageVo i=new ImageVo();
			i.setImageId(id);
			i.setOperation("add");
			voList.add(i);
		}
		av.setImageList(voList);
	}
	/**
	 * 用于发布时关联图片标识后清除缓存
	 * @author 曾臻
	 * @date 2013-1-14
	 */
	public static void clearImageIds(){
		HttpSession ss=AjaxUtil.getHttpSession();
		@SuppressWarnings("unchecked")
		Map<Long,String> map=(Map<Long,String>)ss.getAttribute(InfoKeys.UPLOADED_AND_LOADED_IMG_IDS);
		map.clear();
	}
	
	public static Long reUploadThumbnail(HttpSession ss,Long newId){
		Long id=(Long)ss.getAttribute(InfoKeys.PREV_THUMBNAIL_ID);
		ss.setAttribute(InfoKeys.PREV_THUMBNAIL_ID, newId);
		return id;
	}
	
	/**
	 * 将articleVo中的组织id转换成uuid并存储
	 * @author 曾臻
	 * @date 2013-4-8
	 * @param av
	 * @throws SystemException 
	 * @throws PortalException 
	 */
	public static void syncOrgId2Uuid(ArticleVo av) throws PortalException, SystemException{
		String uuid;
		//current org id
		uuid=getOrgUuidById(av.getCurrentOrgId());
		av.setCurrentOrgUuid(uuid);
		
		//target org list
		if(av.getTargetOrgList()!=null){
			for(TargetOrgVo tov:av.getTargetOrgList()){
				uuid=getOrgUuidById(tov.getOrgId());
				tov.setOrgUuid(uuid);
			}
		}
		
		//user org id list
		if(av.getUserOrgIdList()!=null){
			List<String> uuidList=new ArrayList<String>();
			for(Long id:av.getUserOrgIdList()){
				uuid=getOrgUuidById(id);
				uuidList.add(uuid);
			}
			av.setUserOrgUuidList(uuidList);
		}
		
		
	}
	/**
	 *  将articleVo中的组织uuid转换成id并存储
	 * @author 曾臻
	 * @date 2013-4-8
	 * @param av
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void syncOrgUuid2Id(ArticleVo av) throws PortalException, SystemException{
		Long plainid;
		//current org id
		plainid=getOrgIdByUuid(av.getCurrentOrgUuid());
		av.setCurrentOrgId(plainid);
		
		//target org list
		if(av.getTargetOrgList()!=null){
			for(TargetOrgVo tov:av.getTargetOrgList()){
				plainid=getOrgIdByUuid(tov.getOrgUuid());
				tov.setOrgId(plainid);
			}
		}
		
		//user org id list
		if(av.getUserOrgUuidList()!=null){
			List<Long> idList=new ArrayList<Long>();
			for(String id:av.getUserOrgUuidList()){
				plainid=getOrgIdByUuid(id);
				idList.add(plainid);
			}
			av.setUserOrgIdList(idList);
		}
		
	}
	private static Long getOrgIdByUuid(String uuid) throws PortalException, SystemException{
		if(uuid==null)
			return null;
		else if("0".equals(uuid))
			return 0l;
		else if("-1".equals(uuid))
			return -1l;
		else {
			Organization o=OrganizationLocalServiceUtil.getOrganizationByUuid(uuid);
			return o.getOrganizationId();
		}
	}
	private static String getOrgUuidById(Long id) throws PortalException, SystemException{
		if(id==null)
			return null;
		else if(id==0l)
			return "0";
		else if (id==-1l)
			return "-1";
		else{
			Organization o=OrganizationLocalServiceUtil.getOrganization(id);
			return o.getUuid();
		}
	}
}
