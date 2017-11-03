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
import cn.ffcs.uom.information.dao.ThumbnailDao;
import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2013-1-14
 * 
 */
public class ThumbnailDownloadAction extends HttpServlet {

	private static final long serialVersionUID = -1055176281573498433L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		long thumbnailId = Long.valueOf((String) request.getParameter("id"));
		ServletOutputStream os = response.getOutputStream();
		ThumbnailDao dao = (ThumbnailDao) ApplicationContextUtil.getBean("thumbnailDaoImpl");
		ThumbnailVo vo = dao.loadThumbnail(thumbnailId);
		String userAgent = request.getHeader("User-Agent");
		response.reset();
		response.setContentType("image");
		response.addHeader("Content-Disposition",
				//XXX mod for uom
				"attachment; filename=\"" + StrUtil.encodeFileName("/uom-apps/thumbnailImageDownload.action?id="+vo.getThumbnailId(), userAgent) + "\"");
				//"attachment; filename=\"" + StrUtil.encodeFileName("/uniportal-plugins/thumbnailImageDownload.action?id="+vo.getThumbnailId(), userAgent) + "\"");
		response.addHeader("Content-Length", String.valueOf(vo.getLength()));
		dao.loadThumbnailContent(thumbnailId, os);
		os.flush();
		os.close();
	}
}
