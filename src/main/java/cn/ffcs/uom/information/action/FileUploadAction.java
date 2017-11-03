/**
 * 
 */
package cn.ffcs.uom.information.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.ffcs.raptornuke.plugin.common.json.jackson.JsonUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.information.dao.AttachmentDao;
import cn.ffcs.uom.information.vo.AttachmentVo;

public class FileUploadAction extends HttpServlet {
	private static final long serialVersionUID = -4435906467836721804L;
	public static final int MB = 1024 * 1024;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {

		List<Long> ids=new ArrayList<Long>();
		String tmpdir=System.getProperty("java.io.tmpdir");
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(10 * MB, new File(tmpdir));
		ServletFileUpload uploader = new ServletFileUpload(fileItemFactory);
		uploader.setSizeMax(1200 * MB);
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
					long id=addUploadedFile(item);
					ids.add(id);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
		String json=JsonUtil.objectArray2Json(ids);
		response.getOutputStream().println(json);
	}

	
	private long addUploadedFile(FileItem item) throws Exception {
		InputStream is=item.getInputStream();
		AttachmentVo a=new AttachmentVo();
		a.setName(item.getName());
		a.setLength((int)item.getSize());
		AttachmentDao dao=(AttachmentDao)ApplicationContextUtil.getBean("attachmentDaoImpl");
		long id=dao.addAttachment(a, is);
		return id;
	}
}
