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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.information.dao.ImageDao;
import cn.ffcs.uom.information.util.BeanUtil;
import cn.ffcs.uom.information.util.UploadUtil;
import cn.ffcs.uom.information.vo.ImageVo;

public class ImageUploadAction extends HttpServlet {
	private static final long serialVersionUID = -3811872183199934950L;
	public static final int MB = 1024 * 1024;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		HttpSession ss=request.getSession();
		response.setContentType("text/html; charset=UTF-8");
		Map<Long,String> imgIds=BeanUtil.getImageIds(ss);
		List<ImageVo> list=new ArrayList<ImageVo>();
		String tmpdir=System.getProperty("java.io.tmpdir");
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(10 * MB, new File(tmpdir));
		ServletFileUpload uploader = new ServletFileUpload(fileItemFactory);
		uploader.setSizeMax(50 * MB);
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
					ImageVo i=addUploadedFile(item);
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
		for(ImageVo i:list){
			imgIds.put(i.getImageId(),"add");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("error",0 );
			//XXX mod for uom
			String url="/uom-apps/informationImageDownload.action?id="+i.getImageId();
			//String url="/uniportal-plugins/informationImageDownload.action?id="+i.getImageId();
			map.put("url",url);
			String json=JsonUtil.object2Json(map);
			response.getOutputStream().println(json);
		}
	}
	private ImageVo addUploadedFile(FileItem item) throws Exception {
		InputStream is=item.getInputStream();
		ImageVo a=new ImageVo();
		a.setType(UploadUtil.getFileExt(item));
		a.setLength((int)item.getSize());
		ImageDao dao=(ImageDao)ApplicationContextUtil.getBean("imageDaoImpl");
		long id=dao.addImage(a, is);
		a.setImageId(id);
		return a;
	}
}
