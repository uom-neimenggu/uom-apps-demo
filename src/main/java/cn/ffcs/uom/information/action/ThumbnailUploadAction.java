/**
 * 
 */
package cn.ffcs.uom.information.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.ffcs.raptornuke.plugin.common.json.jackson.JsonUtil;
import cn.ffcs.uom.common.session.SessionContext;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.information.dao.ThumbnailDao;
import cn.ffcs.uom.information.util.BeanUtil;
import cn.ffcs.uom.information.util.UploadUtil;
import cn.ffcs.uom.information.vo.ThumbnailVo;

public class ThumbnailUploadAction extends HttpServlet {
	private static final long serialVersionUID = -5683225040771276464L;
	public static final int MB = 1024 * 1024;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		String ssid=request.getParameter("jsessionid");
		HttpSession ss=SessionContext.getInstance().getSession(ssid);
		response.setContentType("text/html; charset=UTF-8");
		Map<Long,String> imgIds=BeanUtil.getImageIds(ss);
		List<ThumbnailVo> list=new ArrayList<ThumbnailVo>();
		String tmpdir=System.getProperty("java.io.tmpdir");
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(10 * MB, new File(tmpdir));
		ServletFileUpload uploader = new ServletFileUpload(fileItemFactory);
		uploader.setSizeMax(10 * MB);
		List fields = null;
		try {
			fields = uploader.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			return;
		}
		Iterator iter = fields.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			
			if (!item.isFormField()) {
				try {
					UploadUtil.checkFileType(item);
					ThumbnailVo i=addUploadedFile(item);
					clearPrevThumbnail(ss,i.getThumbnailId());
					list.add(i);
				}catch(RuntimeException e){
					e.printStackTrace();
					response.getOutputStream().println(UploadUtil.getError(e.getMessage()));
					return;
				}catch (Exception e) {
					e.printStackTrace();
					response.getOutputStream().println(UploadUtil.getError("上传图片失败。"));
					return;
				}
			}
		}
		for(ThumbnailVo i:list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("error",0 );
			map.put("id",i.getThumbnailId());
			String json=JsonUtil.object2Json(map);
			response.getOutputStream().println(json);
		}
	}
	private ThumbnailVo addUploadedFile(FileItem item) throws Exception {
		InputStream is=item.getInputStream();
		ThumbnailVo a=new ThumbnailVo();
		a.setLength((int)item.getSize());
		ThumbnailDao dao=(ThumbnailDao)ApplicationContextUtil.getBean("thumbnailDaoImpl");
		long id=dao.addThumbnail(a, is);
		a.setThumbnailId(id);
		return a;
	}
	/**
	 * 上传后清除上一个缩略图
	 */
	private void clearPrevThumbnail(HttpSession ss,long newId){
		Long prevId=BeanUtil.reUploadThumbnail(ss, newId);
		if(prevId!=null){
			ThumbnailDao dao=(ThumbnailDao)ApplicationContextUtil.getBean("thumbnailDaoImpl");
			List<Long> ids=new ArrayList<Long>();
			ids.add(prevId);
			dao.deleteThumbnails(ids);
		}
	}

}
