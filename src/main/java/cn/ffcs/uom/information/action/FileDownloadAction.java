/**
 * 
 */
package cn.ffcs.uom.information.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.information.dao.AttachmentDao;
import cn.ffcs.uom.information.vo.AttachmentVo;

/**
 * @author 曾臻
 * @date 2013-1-10
 * 
 */
public class FileDownloadAction extends HttpServlet {
	private static final long serialVersionUID = 6429563282236150480L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		long attachmentId = Long.valueOf((String) request.getParameter("id"));
		ServletOutputStream os = response.getOutputStream();
		AttachmentDao dao = (AttachmentDao) ApplicationContextUtil.getBean("attachmentDaoImpl");
		AttachmentVo vo = dao.loadAttachment(attachmentId);
		String userAgent = request.getHeader("User-Agent");
		response.reset();
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition",
				"attachment; filename=\"" + StrUtil.encodeFileName(vo.getName(), userAgent) + "\"");
		response.addHeader("Content-Length", String.valueOf(vo.getLength()));
		dao.loadAttachmentContent(attachmentId, os);
		os.flush();
		os.close();
	}
}
