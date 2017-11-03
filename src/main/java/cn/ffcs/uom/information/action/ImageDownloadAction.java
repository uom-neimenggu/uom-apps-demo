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
import cn.ffcs.uom.information.dao.ImageDao;
import cn.ffcs.uom.information.vo.ImageVo;

/**
 * @author 曾臻
 * @date 2013-1-14
 * 
 */
public class ImageDownloadAction extends HttpServlet {

	private static final long serialVersionUID = -7721505436719999734L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		long imageId = Long.valueOf((String) request.getParameter("id"));
		ServletOutputStream os = response.getOutputStream();
		ImageDao dao = (ImageDao) ApplicationContextUtil.getBean("imageDaoImpl");
		ImageVo vo = dao.loadImage(imageId);
		String userAgent = request.getHeader("User-Agent");
		response.reset();
		response.setContentType("image");
		response.addHeader("Content-Disposition",
				//XXX mod for uom
				"attachment; filename=\"" + StrUtil.encodeFileName("/uom-apps/informationImageDownload.action?id="+vo.getImageId(), userAgent) + "\"");
				//"attachment; filename=\"" + StrUtil.encodeFileName("/uniportal-plugins/informationImageDownload.action?id="+vo.getImageId(), userAgent) + "\"");
		response.addHeader("Content-Length", String.valueOf(vo.getLength()));
		dao.loadImageContent(imageId, os);
		os.flush();
		os.close();
	}
}
